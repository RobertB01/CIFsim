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
import static org.eclipse.escet.common.app.framework.output.OutputProvider.err;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.makeFixedLengthNumberText;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;
import org.eclipse.escet.cif.cif2cif.ElimComponentDefInst;
import org.eclipse.escet.cif.cif2cif.ElimSelf;
import org.eclipse.escet.cif.cif2cif.RemoveIoDecls;
import org.eclipse.escet.cif.cif2cif.SimplifyValuesOptimized;
import org.eclipse.escet.cif.io.CifReader;
import org.eclipse.escet.cif.io.CifWriter;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.multilevel.ciftodmm.CifRelations;
import org.eclipse.escet.cif.multilevel.ciftodmm.CifToDmm;
import org.eclipse.escet.cif.multilevel.clustering.ComputeMultiLevelTree;
import org.eclipse.escet.cif.multilevel.clustering.TreeNode;
import org.eclipse.escet.cif.multilevel.subplant.PartialSpecsBuilder;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
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
import org.eclipse.escet.common.java.BitSetIterator;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

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
    /** Regular expression pattern of a generated subplant specification file. */
    private static final Pattern SPEC_FILE_PATTERN = Pattern.compile("spec_[0-9]+\\.cif");

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
        List<TreeNode> linearizedTree = rootNode.linearizeTree();
        for (TreeNode node: linearizedTree) {
            out("Index: %d", node.index);
            out("Plant groups: %s", node.plantGroups.isEmpty() ? "<none>" : node.plantGroups.toString());
            out("Req groups:   %s", node.requirementGroups.isEmpty() ? "<none>" : node.requirementGroups.toString());
            String childNodeNumbers = node.childNodes.stream().map(n -> String.valueOf(n.index))
                    .collect(Collectors.joining(", "));
            out("Child nodes: %s", childNodeNumbers.isEmpty() ? "<none>" : childNodeNumbers);
            out();
        }

        if (linearizedTree.isEmpty()) {
            err("No partial specifications were created.");
            return 0;
        }

        // Create a directory for storing the partial specifications, if it does not yet exist.
        String partialSpecsDir = OutputFileOption.getDerivedPath(".cif", "_partial_specs");
        Path absDirPath = java.nio.file.Paths.get(Paths.resolve(partialSpecsDir));
        boolean dirExists = false;
        try {
            Files.createDirectory(absDirPath);
        } catch (FileAlreadyExistsException ex) {
            if (Files.isDirectory(absDirPath)) {
                dirExists = true;
            } else {
                String msg = fmt("Failed to create output directory \"%s\" for the partial specifications.",
                        absDirPath);
                throw new InputOutputException(msg, ex);
            }
        } catch (IOException ex) {
            String msg = fmt("Failed to create output directory \"%s\" for the partial specifications.", absDirPath);
            throw new InputOutputException(msg, ex);
        }

        // In case the directory already exists, delete existing "spec_NNN.cif" entries.
        if (dirExists) {
            try (Stream<Path> dirContent = Files.list(absDirPath)) {
                Predicate<String> matcher = SPEC_FILE_PATTERN.asMatchPredicate();
                dirContent.filter(p -> matcher.test(p.getFileName().toString())).forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException ex) {
                        String msg = fmt("Failed to clean output directory \"%s\" for the partial specifications.",
                                absDirPath);
                        throw new InputOutputException(msg, ex);
                    }
                });
            } catch (IOException ex) {
                String msg = fmt("Failed to clean output directory \"%s\" for the partial specifications.", absDirPath);
                throw new InputOutputException(msg, ex);
            }
        }

        // Construct the partial specifications. The partial builder is re-used for each partial specification.
        PartialSpecsBuilder partialBuilder = new PartialSpecsBuilder(spec);

        // And construct and write the partial specifications.
        for (TreeNode node: linearizedTree) {
            // Find the objects that should be in the partial specification of this node.
            List<PositionObject> neededObjects = list();
            for (int plantGrp: new BitSetIterator(node.plantGroups)) {
                neededObjects.addAll(cifRelations.getPlantsOfGroup(plantGrp));
            }
            for (int reqGrp: new BitSetIterator(node.requirementGroups)) {
                neededObjects.addAll(cifRelations.getRequirementsOfGroup(reqGrp));
            }

            // Construct the specification.
            Specification partialSpec = partialBuilder.createPartialSpecification(neededObjects);

            // And write the output file.
            String outPath = Paths.join(Paths.resolve(partialSpecsDir),
                    "spec_" + makeFixedLengthNumberText(node.index, linearizedTree.size()) + ".cif");
            CifWriter.writeCifSpec(partialSpec, outPath, cifReader.getAbsDirPath());
        }
        out("Wrote %d partial specifications to directory %s", linearizedTree.size(), partialSpecsDir);

        // TODO Implement.
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
