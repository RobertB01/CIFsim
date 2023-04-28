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

package org.eclipse.escet.tooldef.interpreter;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.HelpOption;
import org.eclipse.escet.common.app.framework.options.InputFileOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.tooldef.io.ToolDefReader;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;

/** ToolDef interpreter application. */
public class ToolDefInterpreterApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        ToolDefInterpreterApp app = new ToolDefInterpreterApp();
        app.run(args);
    }

    /** Constructor for the {@link ToolDefInterpreterApp} class. */
    public ToolDefInterpreterApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link ToolDefInterpreterApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public ToolDefInterpreterApp(AppStreams streams) {
        super(streams);
    }

    @Override
    public String getAppName() {
        return "ToolDef interpreter";
    }

    @Override
    public String getAppDescription() {
        return "The ToolDef interpreter executes ToolDef scripts.";
    }

    @Override
    public void printHelpExitCodes(AppStream s) {
        super.printHelpExitCodes(s);
        HelpOption.outw(s, "In certain cases, the ToolDef script may produce an exit code different from the default "
                + "exit code. For instance, in case an \"exit\" statement with a custom exit code is successfully "
                + "executed, or in case the \"tooldef\" tool is used to execute a ToolDef script that produces a "
                + "non-zero exit code and \"ignoreNonZeroExitCode\" is set to \"false\".");
    }

    @Override
    protected int runInternal() {
        // Initialize debugging.
        boolean dbgEnabled = OutputModeOption.getOutputMode() == OutputMode.DEBUG;

        // Read ToolDef script.
        String inputPath = InputFileOption.getPath();
        if (dbgEnabled) {
            dbg("Reading ToolDef script file \"%s\".", inputPath);
        }
        ToolDefReader reader = new ToolDefReader().init();
        Script script = reader.read();
        if (isTerminationRequested()) {
            return 0;
        }

        // Get script path.
        String scriptPath = Paths.resolve(inputPath);

        // Execute script.
        if (dbgEnabled) {
            dbg("Executing ToolDef script.");
        }
        int exitCode = ToolDefInterpreter.execute(script, scriptPath, this);
        if (dbgEnabled) {
            dbg("Finished executing ToolDef script (exit code %d).", exitCode);
        }
        return exitCode;
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> transOpts = list();
        transOpts.add(Options.getInstance(InputFileOption.class));
        List<OptionCategory> transSubCats = list();
        OptionCategory transCat = new OptionCategory("Interpreter", "Interpreter options.", transSubCats, transOpts);

        List<OptionCategory> cats = list(generalCat, transCat);
        OptionCategory options = new OptionCategory("ToolDef Interpreter Options",
                "All options for the ToolDef interpreter.", cats, list());

        return options;
    }
}
