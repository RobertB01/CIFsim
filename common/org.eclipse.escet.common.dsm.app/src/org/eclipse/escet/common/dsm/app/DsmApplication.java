//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.app;

import static org.eclipse.escet.common.dsm.DsmClustering.flowBasedMarkovClustering;
import static org.eclipse.escet.common.dsm.DsmHelper.shuffleArray;
import static org.eclipse.escet.common.dsm.io.ReadMatrix.readMatrixFile;
import static org.eclipse.escet.common.dsm.io.WriteMatrix.writeGroups;
import static org.eclipse.escet.common.dsm.io.WriteMatrix.writeMatrixFile;
import static org.eclipse.escet.common.java.Lists.list;

import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.options.OutputFileOption;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.dsm.ClusterInput;
import org.eclipse.escet.common.dsm.Dsm;
import org.eclipse.escet.common.dsm.Label;

/** Application wrapper class for performing DSM clustering and bus detection. */
public class DsmApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        DsmApplication app = new DsmApplication();
        app.run(args, true);
    }

    /** Constructor for the {@link DsmApplication} class. */
    public DsmApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link DsmApplication} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public DsmApplication(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "DSM clustering tool";
    }

    @Override
    public String getAppDescription() {
        return "Clusters nodes in a graph and generates a DSM with a bus and clusters.";
    }

    @Override
    protected int runInternal() {
        String inPath = Paths.resolve(InputFileOption.getPath());
        ClusterInput inputData = readMatrixFile(inPath, OutputProvider.getDebugOutputStream());
        inputData.evap = DsmEvaporationOption.getEvaporationFactor();
        inputData.inflation = DsmInflationOption.getInflationFactor();
        inputData.busDetectionAlgorithm = DsmBusDetectionAlgorithmOption.getBusAlgorithm();
        inputData.busInclusion = DsmBusFactorOption.getBusFactor();
        inputData.epsilon = ConvergenceOption.getConvergenceValue();
        inputData.stepCount = DsmStepCountOption.getStepCountValue();

        Dsm dsm = flowBasedMarkovClustering(inputData);
        String outPath = OutputFileOption.getDerivedPath(".dsm", "_out.dsm");
        String absOutPath = Paths.resolve(outPath);

        try (FileAppStream stream = new FileAppStream(outPath, absOutPath)) {
            Label[] labels = shuffleArray(inputData.labels, dsm.nodeShuffle);
            writeMatrixFile(stream, dsm.adjacencies, labels);
            if (OutputGroupsOption.getOutputGroupsOptionValue()) {
                writeGroups(stream, dsm.rootGroup);
            }
        }
        return 0;
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected OptionCategory getAllOptions() {
        OptionCategory generalOpts = getGeneralOptionCategory();

        OptionCategory clusterOpts = new OptionCategory("Clustering", "Options to steer the clustering algorithms.",
                list(),
                list(Options.getInstance(InputFileOption.class), Options.getInstance(OutputFileOption.class),
                        Options.getInstance(DsmEvaporationOption.class), Options.getInstance(DsmInflationOption.class),
                        Options.getInstance(DsmBusDetectionAlgorithmOption.class),
                        Options.getInstance(DsmBusFactorOption.class), Options.getInstance(ConvergenceOption.class),
                        Options.getInstance(OutputGroupsOption.class), Options.getInstance(DsmStepCountOption.class)));

        OptionCategory options;
        options = new OptionCategory("DSM Clustering Tool Options", "All options for the DSM clustering tool.",
                list(generalOpts, clusterOpts), list());
        return options;
    }
}
