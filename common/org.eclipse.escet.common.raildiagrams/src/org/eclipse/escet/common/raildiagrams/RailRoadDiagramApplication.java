//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.Strings.fmt;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.FilesOption;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.raildiagrams.parser.RailRoadParser;
import org.eclipse.escet.common.raildiagrams.railroad.RailRule;
import org.eclipse.escet.setext.runtime.DebugMode;

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
     * Constructor for the {@link DsmApplication} class.
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
        // Setup graphics object.
        // Image is needed for getting a Graphics2D instance to query text sizes.
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Configuration config = new Configuration(image.createGraphics());
        if (isTerminationRequested())
            return 0;

        // Load the configuration file.
        String configFilename = ConfigFileOption.getConfigFilename();
        if (!configFilename.isEmpty())
            config.loadPropertiesFile(configFilename);
        if (isTerminationRequested())
            return 0;

        // Process the provided files.
        List<String> inputFiles = FilesOption.getPaths();
        for (String inputFile: inputFiles) {
            // Parse the input.
            // A file may contain several rules, which are assumed to belong together in one diagram.
            RailRoadParser parser = new RailRoadParser();
            List<RailRule> rules = parser.parseFile(Paths.resolve(inputFile), DebugMode.NONE);
            if (isTerminationRequested())
                return 0;

            // Generate a graphic collection for it.
            //
            // First, compute required size for all boxes in the diagram.
            // Boxes are put under each other.
            double diagramWidth = 0;
            double diagramHeight = 0;
            for (RailRule rule: rules) {
                rule.create(config, 1);
                Size2D size = rule.getSize();
                diagramWidth = Math.max(diagramWidth, size.width);
                diagramHeight += Math.ceil(size.height);

                if (isTerminationRequested())
                    return 0;
            }

            // Second, position everything and generate the graphic elements.
            //
            // Create the 'real' image.
            Graphics2D gd;
            {
                int width = (int)Math.ceil(diagramWidth);
                int height = (int)Math.ceil(diagramHeight);

                image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                gd = image.createGraphics();
                gd.setColor(config.getRgbColor("diagram.background.color"));
                gd.fillRect(0, 0, width, height);
            }

            // Paint graphics to the image.
            double top = 0;
            for (RailRule rule: rules) {
                rule.paint(0, top, gd);
                Size2D size = rule.getSize();
                top += Math.ceil(size.height);
                if (isTerminationRequested())
                    return 0;
            }

            // Write the image.
            String imageFile = WriteImageOption.getOutputPath(inputFile);
            if (imageFile != null)
                saveImage(image, imageFile);
        }

        return 0;
    }

    /**
     * Save the created image to the file system.
     *
     * @param imageFile Name of the file to write.
     */
    private void saveImage(BufferedImage image, String imageFile) {
        try {
            ImageIO.write(image, "png", new File(imageFile));
        } catch (IOException ex) {
            String msg = fmt("Failed to write PNG image file \"%s\".", imageFile);
            throw new RuntimeException(msg, ex);
        }
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected OptionCategory getAllOptions() {
        OptionCategory generalOpts = getGeneralOptionCategory();

        OptionCategory diagramOpts = new OptionCategory("Railroad Diagram Generator Options",
                "Options to generate railroad diagrams", list(), list(Options.getInstance(FilesOption.class),
                        Options.getInstance(ConfigFileOption.class), Options.getInstance(WriteImageOption.class)));

        OptionCategory options;
        options = new OptionCategory("Railroad Diagram Generator Tool Options",
                "All options for the Railroad Diagram Generator Tool.", list(generalOpts, diagramOpts), list());
        return options;
    }
}
