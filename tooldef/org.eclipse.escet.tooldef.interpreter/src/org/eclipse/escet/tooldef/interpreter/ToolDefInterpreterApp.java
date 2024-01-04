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

package org.eclipse.escet.tooldef.interpreter;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.stream.Collectors;

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
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.typechecker.SemanticProblem;
import org.eclipse.escet.common.typechecker.SemanticProblemSeverity;
import org.eclipse.escet.setext.runtime.DebugMode;
import org.eclipse.escet.setext.runtime.exceptions.SyntaxException;
import org.eclipse.escet.tooldef.io.ToolDefReader;
import org.eclipse.escet.tooldef.metamodel.java.ToolDefConstructors;
import org.eclipse.escet.tooldef.metamodel.tooldef.Script;
import org.eclipse.escet.tooldef.metamodel.tooldef.expressions.ToolInvokeExpression;
import org.eclipse.escet.tooldef.metamodel.tooldef.statements.ToolInvokeStatement;
import org.eclipse.escet.tooldef.parser.ToolDefInvokeParser;
import org.eclipse.escet.tooldef.typechecker.CheckerContext;
import org.eclipse.escet.tooldef.typechecker.ToolDefTypeChecker;

/** ToolDef interpreter application. */
public class ToolDefInterpreterApp extends Application<IOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        ToolDefInterpreterApp app = new ToolDefInterpreterApp();
        app.run(args, true);
    }

    /** Constructor for the {@link ToolDefInterpreterApp} class. */
    public ToolDefInterpreterApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link ToolDefInterpreterApp} class.
     *
     * @param streams The streams to use for input, output, warning, and error streams.
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

        // Get tool invocation, if any.
        String invocationText = ToolDefInvokeOption.getInvocationText();
        ToolInvokeStatement invocation = invocationText == null ? null
                : getInvocation(invocationText, reader.getTypeChecker());

        // Execute script or tool invocation.
        String whatIsExecuted = (invocation == null) ? "script" : "tool";
        if (dbgEnabled) {
            dbg("Executing ToolDef %s.", whatIsExecuted);
        }
        int exitCode = ToolDefInterpreter.execute(script, scriptPath, invocation, this);
        if (dbgEnabled) {
            dbg("Finished executing ToolDef %s (exit code %d).", whatIsExecuted, exitCode);
        }
        return exitCode;
    }

    /**
     * Get the tool invocation for the given tool invocation text, by parsing and type checking it.
     *
     * @param invocationText The tool invocation text, in ToolDef textual syntax. Maybe
     * @param tchecker The type checker use to check the ToolDef script.
     * @return The tool invocation.
     */
    private ToolInvokeStatement getInvocation(String invocationText, ToolDefTypeChecker tchecker) {
        // Parse it.
        ToolDefInvokeParser parser = new ToolDefInvokeParser();
        ToolInvokeExpression invocationExpr;
        try {
            invocationExpr = parser.parseString(invocationText, CheckerContext.TOOL_INVOCATION_LOCATION, null,
                    DebugMode.NONE);
        } catch (SyntaxException e) {
            String msg = fmt("The tool invocation provided via the 'Tool invocation' option is invalid: \"%s\".",
                    invocationText);
            throw new InvalidOptionException(msg, e);
        }
        Assert.check(parser.getWarnings().isEmpty());

        // Type check it. Ignore any warnings.
        ToolInvokeStatement invocationStatement = ToolDefConstructors.newToolInvokeStatement(invocationExpr, null);
        List<SemanticProblem> problems = tchecker.typeCheck(invocationStatement);
        List<SemanticProblem> errors = problems.stream().filter(p -> p.severity == SemanticProblemSeverity.ERROR)
                .toList();
        if (!errors.isEmpty()) {
            String msg = fmt("The tool invocation provided via the 'Tool invocation' option is invalid: \"%s\":\n",
                    invocationText) + errors.stream().map(p -> " - " + p.toString()).collect(Collectors.joining("\n"));
            throw new InvalidOptionException(msg);
        }
        return invocationStatement;
    }

    @Override
    protected OutputProvider<IOutputComponent> getProvider() {
        return new OutputProvider<>();
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        OptionCategory generalCat = getGeneralOptionCategory();

        List<Option> interpreterOpts = list();
        interpreterOpts.add(Options.getInstance(InputFileOption.class));
        interpreterOpts.add(Options.getInstance(ToolDefInvokeOption.class));
        List<OptionCategory> interpreterSubCats = list();
        OptionCategory transCat = new OptionCategory("Interpreter", "Interpreter options.", interpreterSubCats,
                interpreterOpts);

        List<OptionCategory> cats = list(generalCat, transCat);
        OptionCategory options = new OptionCategory("ToolDef Interpreter Options",
                "All options for the ToolDef interpreter.", cats, list());

        return options;
    }
}
