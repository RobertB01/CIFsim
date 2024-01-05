//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.generator;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.java.Lists.list;

import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.setext.io.SeTextReader;
import org.eclipse.escet.setext.parser.ast.Specification;
import org.eclipse.escet.setext.parser.ast.parser.StartSymbol;

/** SeText scanner/parser generator application. */
public class SeTextGeneratorApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        SeTextGeneratorApp app = new SeTextGeneratorApp();
        app.run(args, true);
    }

    /** Constructor for the {@link SeTextGeneratorApp} class. */
    public SeTextGeneratorApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link SeTextGeneratorApp} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
     */
    public SeTextGeneratorApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "SeText scanner/parser generator";
    }

    @Override
    public String getAppDescription() {
        return "Generates scanner(s) and LALR(1) parser(s) for an SeText specification.";
    }

    @Override
    protected int runInternal() {
        // Start time measurement.
        long startTime = System.nanoTime();

        // Read SeText specification.
        Specification spec = new SeTextReader().init().read();

        // Generate scanner (one for all start/main) symbols.
        SeTextGenerator.generateScanner(spec);

        // Generate parser per start/main symbol.
        for (StartSymbol start: spec.getStartSymbols()) {
            SeTextGenerator.generateParser(spec, start);
        }

        // Generate skeleton for hooks class.
        if (spec.hooksClass != null) {
            String hooksFilePath = spec.hooksClass.getSimpleClassName();
            hooksFilePath += ".skeleton";
            hooksFilePath = Paths.resolve(hooksFilePath);
            SeTextGenerator.writeHooksSkeleton(spec, hooksFilePath);
        }

        // Show completion time.
        double duration = (System.nanoTime() - startTime) / 1e6;
        out("Generation completed in %.0f millisecs.", duration);

        // All done.
        return 0;
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    protected OptionCategory getAllOptions() {
        OptionCategory generalOpts = getGeneralOptionCategory();

        OptionCategory genOpts = new OptionCategory("Generation", "Generation options.", list(),
                list(Options.getInstance(InputFileOption.class), Options.getInstance(OutputJavaFilesOption.class),
                        Options.getInstance(OutputDebugFilesOption.class),
                        Options.getInstance(OutputBnfFileOption.class), Options.getInstance(JavaHeaderOption.class)));

        OptionCategory options = new OptionCategory("SeText Scanner/Parser Generator Options",
                "All options for the SeText scanner/parser generator.", list(generalOpts, genOpts), list());

        return options;
    }
}
