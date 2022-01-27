//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams;

import static org.eclipse.escet.common.java.Lists.list;

import java.awt.Color;
import java.util.List;

import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.FilesOption;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.raildiagrams.config.ConfigFileOption;
import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.output.DebugImageOutput;
import org.eclipse.escet.common.raildiagrams.output.NormalImageOutput;
import org.eclipse.escet.common.raildiagrams.output.OutputTarget;
import org.eclipse.escet.common.raildiagrams.parser.RailRoadDiagramParser;
import org.eclipse.escet.common.raildiagrams.railroad.RailRule;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;
import org.eclipse.escet.common.raildiagrams.util.Size2D;
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.SyntaxWarning;

/** Application to generate railroad diagram image files. */
public class RailRoadDiagramApplication extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        RailRoadDiagramApplication app = new RailRoadDiagramApplication();
        app.run(args);
    }

    /** Constructor for the {@link RailRoadDiagramApplication} class. */
    public RailRoadDiagramApplication() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link RailRoadDiagramApplication} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public RailRoadDiagramApplication(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "Railroad Diagram Generator Tool";
    }

    @Override
    public String getAppDescription() {
        return "Generates an image showing the syntax of a language construct as a railroad diagram.";
    }

    @Override
    protected int runInternal() {
        // Setup output target and configuration.
        OutputTarget outputTarget;
        switch (OutputFormatOption.getFormat()) {
            case DBG_IMAGES:
                outputTarget = new DebugImageOutput();
                break;
            case IMAGES:
                outputTarget = new NormalImageOutput();
                break;
            default:
                throw new AssertionError("Unknown output format found: " + OutputFormatOption.getFormat());
        }

        Configuration config = new Configuration(outputTarget);
        if (isTerminationRequested()) {
            return 0;
        }

        // Load the configuration file.
        String configFilePath = ConfigFileOption.getFilePath();
        if (configFilePath != null) {
            String configFileAbsolutePath = Paths.resolve(configFilePath);
            config.loadPropertiesFile(configFileAbsolutePath);
        }
        if (isTerminationRequested()) {
            return 0;
        }

        // Process the provided files.
        List<String> inputFiles = FilesOption.getPaths();
        for (String inputFile: inputFiles) {
            // Parse the input.
            // A file may contain several rules, which are assumed to belong together in one diagram.
            RailRoadDiagramParser parser = new RailRoadDiagramParser();
            List<RailRule> rules = parser.parseFile(Paths.resolve(inputFile), DebugMode.NONE);
            for (SyntaxWarning warning: parser.getWarnings()) {
                OutputProvider.warn(warning.toString());
            }
            if (isTerminationRequested()) {
                return 0;
            }

            // Generate a graphic collection for it.
            //
            // First, compute required size for all boxes in the diagram.
            // Boxes are put under each other.
            int diagramWidth = 0;
            int diagramHeight = 0;
            for (RailRule rule: rules) {
                rule.create(config, 1);
                Size2D size = rule.getSize();
                diagramWidth = Math.max(diagramWidth, size.width);
                diagramHeight += Math.ceil(size.height);

                if (isTerminationRequested()) {
                    return 0;
                }
            }

            // Second, position everything and generate the graphic elements.
            int width = (int)Math.ceil(diagramWidth);
            int height = (int)Math.ceil(diagramHeight);

            Color bgColor = config.getRgbColor("diagram.background.color");
            outputTarget.prepareOutputFile(width, height, bgColor);

            // Paint graphics to the image.
            boolean dumpAbsCoords = config.getDebugSetting(DebugDisplayKind.ABS_COORDINATES);
            int top = 0;
            for (RailRule rule: rules) {
                rule.paint(0, top, outputTarget, dumpAbsCoords);
                Size2D size = rule.getSize();
                top += Math.ceil(size.height);
                if (isTerminationRequested()) {
                    return 0;
                }
            }

            // Write the image.
            String imageFile = WriteImageOption.getOutputPath(inputFile);
            if (imageFile != null) {
                outputTarget.writeOutputFile(imageFile);
            }
            if (isTerminationRequested()) {
                return 0;
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

        OptionCategory diagramOpts = new OptionCategory("Generator", "Railroad diagram generation options.", list(),
                list(Options.getInstance(FilesOption.class), Options.getInstance(ConfigFileOption.class),
                        Options.getInstance(WriteImageOption.class), Options.getInstance(OutputFormatOption.class)));

        OptionCategory options;
        options = new OptionCategory("Railroad Diagram Generator Tool Options",
                "All options for the Railroad Diagram Generator Tool.", list(generalOpts, diagramOpts), list());
        return options;
    }
}
