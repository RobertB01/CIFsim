//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
//
// See the NOTICE file(s) distributed with this work for additional
// information regarding copyright ownership.
//
// This program and the accompanying materials are made available
// under the terms of the MIT License which is available at
// https://opensource.org/licenses/MIT
//
// SPDX-License-Identifier: MIT
//////////////////////////////////////////////////////////////////////////////

package org.eclipse.escet.cif.multilevel;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dodbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyValuesOptimized;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.multilevel.ciftodmm.CifRelations;
import org.eclipse.escet.cif.multilevel.ciftodmm.CifToDmm;
import org.eclipse.escet.cif.multilevel.clustering.ComputeMultiLevelTree;
import org.eclipse.escet.cif.multilevel.clustering.TreeNode;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.dsm.ClusterInput;
import org.eclipse.escet.common.dsm.Dmm;
import org.eclipse.escet.common.dsm.Dsm;
import org.eclipse.escet.common.dsm.DsmClustering;

/**
 * CIF multi-level synthesis application.
 *
 * <p>
 * This implementation is based on the paper Goorden 2020:
 *
 * M. Goorden, J. v. d. Mortel-Fronczak, M. Reniers, W. Fokkink and J. Rooda, "Structuring Multilevel Discrete-Event
 * Systems With Dependence Structure Matrices", IEEE Transactions on Automatic Control, volume 65, issue 4, pages
 * 1625-1639, 2020, doi:<a href="https://doi.org/10.1109/TAC.2019.2928119">10.1109/TAC.2019.2928119</a>.
 * </p>
 */
public class MultilevelApp extends Application<IOutputComponent> {
    /** Matrix debug output format. */
    private static final RealMatrixFormat MAT_DEBUG_FORMAT;

    static {
        NumberFormat valueFmt = NumberFormat.getIntegerInstance(Locale.US);
        MAT_DEBUG_FORMAT = new RealMatrixFormat("", "", "  ", "", "\n", " ", valueFmt);
    }

    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        MultilevelApp app = new MultilevelApp();
        app.run(args, true);
    }

    /** Constructor of the {@link MultilevelApp} class. */
    public MultilevelApp() {
        // Nothing to do.
    }

    /**
     * Constructor of the {@link MultilevelApp} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public MultilevelApp(AppStreams streams) {
        super(streams);
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected int runInternal() {
        // Load the provided CIF specification.
        CifReader cifReader = new CifReader().init();
        Specification spec = cifReader.read();
        String absSpecPath = Paths.resolve(InputFileOption.getPath());
        if (isTerminationRequested()) {
            return 0;
        }

        // Specification transformations.
        new ElimComponentDefInst().transform(spec);
        new ElimSelf().transform(spec);

        // Remove/ignore I/O declarations, to increase the supported subset.
        RemoveIoDecls removeIoDecls = new RemoveIoDecls();
        removeIoDecls.transform(spec);
        if (removeIoDecls.haveAnySvgInputDeclarationsBeenRemoved()) {
            OutputProvider.warn("The specification contains CIF/SVG input declarations. These will be ignored.");
        }

        new SimplifyValuesOptimized().transform(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        // Verify pre-conditions.
        // TODO: CifToDmm prechecker only verifies conditions needed for the CIF to DMM transformation, other parts of
        // TODO: the multilevel application need more or other checks.
        CifToDmm.checkSpec(spec, absSpecPath);
        if (isTerminationRequested()) {
            return 0;
        }

        // Figure 1 of Goorden 2020: Record relations.
        //
        // CIF to DMM transformation.
        CifRelations cifRelations = CifToDmm.transformToDmms(spec);
        if (isTerminationRequested()) {
            return 0;
        }

        if (WriteDMMsOption.writeDmms()) {
            String outPath = OutputFileOption.getDerivedPath(".cif", ".dmms.txt");
            cifRelations.writeDmms(InputFileOption.getPath(), outPath);
        }
        Dmm reqsPlantsDmm = cifRelations.relations; // Requirement-group rows against plant-groups columns.
        if (dodbg()) {
            dbg("Plant groups:");
            dbg(cifRelations.plantGroups.toString());
            dbg();
            dbg("Requirement groups:");
            dbg(cifRelations.requirementGroups.toString());
            dbg();
            dbg("Requirement / Plant relations:");
            dbg(cifRelations.relations.toString());
            dbg();
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Figure 1 of Goorden 2020: Transform the relations.
        //
        // Convert to a plant groups DSM on requirement groups relations.
        // Do the standard T . T^-1 conversion, except here T is already transposed.
        RealMatrix unclusteredMatrix = reqsPlantsDmm.adjacencies.transpose().multiply(reqsPlantsDmm.adjacencies);
        dbg("Unclustered reqsPlants:");
        dbg(MAT_DEBUG_FORMAT.format(unclusteredMatrix));
        dbg();
        if (isTerminationRequested()) {
            return 0;
        }

        // Figure 1 of Goorden 2020: And cluster the DSM.
        dbg("--- Start of clustering --");
        idbg();
        ClusterInput clusteringInput = new ClusterInput(unclusteredMatrix, reqsPlantsDmm.columnLabels,
                OutputProvider.getDebugOutputStream());
        Dsm clusteredDsm = DsmClustering.flowBasedMarkovClustering(clusteringInput);
        ddbg();
        dbg("--- End of clustering --");
        dbg();

        // The cluster result contains the found cluster groups with original indices. For debugging however, it seems
        // useful to also dump the clustered DSM, to understand group information.
        dbg("Clustered DSM for reqsPlantsDmm (for information only, this data is not actually used):");
        dbg(MAT_DEBUG_FORMAT.format(clusteredDsm.adjacencies));
        dbg();
        if (isTerminationRequested()) {
            return 0;
        }

        // Figure 1 of Goorden 2020: Create the node tree.
        TreeNode rootNode = ComputeMultiLevelTree.transformCluster(clusteredDsm.rootGroup, unclusteredMatrix,
                reqsPlantsDmm.adjacencies);
        if (isTerminationRequested()) {
            return 0;
        }

        // Dump the multi-level tree.
        for (TreeNode node: rootNode.linearizeTree()) {
            out("Index: %d", node.index);
            out("Plant groups: %s", node.plantGroups.isEmpty() ? "<none>" : node.plantGroups.toString());
            out("Req groups:   %s", node.requirementGroups.isEmpty() ? "<none>" : node.requirementGroups.toString());
            String childNodeNumbers = node.childNodes.stream().map(n -> String.valueOf(n.index))
                    .collect(Collectors.joining(", "));
            out("Child nodes: %s", childNodeNumbers.isEmpty() ? "<none>" : childNodeNumbers);
            out();
        }

        OutputProvider.warn("Multi-level synthesis not yet implemented.");
        return 0;
    }

    @Override
    public String getAppName() {
        return "CIF multi-level synthesis";
    }

    @Override
    public String getAppDescription() {
        return "Performs synthesis by making several smaller co-operating supervisors.";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> programOpts = List.of(Options.getInstance(InputFileOption.class),
                Options.getInstance(OutputFileOption.class), Options.getInstance(WriteDMMsOption.class));
        OptionCategory programCat = new OptionCategory("Multi-level synthesis", "Multi-level synthesis options.",
                List.of(), programOpts);

        return new OptionCategory("CIF Multi-level Synthesis Options",
                "All options for the CIF multi-level synthesis tool.", List.of(generalCat, programCat), List.of());
    }
}
