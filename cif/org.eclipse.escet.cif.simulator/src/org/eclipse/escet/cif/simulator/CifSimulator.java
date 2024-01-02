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

package org.eclipse.escet.cif.simulator;

import static org.eclipse.escet.common.java.Lists.cast;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.escet.cif.simulator.compiler.CifCompiler;
import org.eclipse.escet.cif.simulator.compiler.CifCompilerContext;
import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.input.SvgInputComponent;
import org.eclipse.escet.cif.simulator.options.AskToTerminateMode;
import org.eclipse.escet.cif.simulator.options.AskToTerminateOption;
import org.eclipse.escet.cif.simulator.options.CifSpecOption;
import org.eclipse.escet.cif.simulator.options.CompileOnlyOption;
import org.eclipse.escet.cif.simulator.options.DebugCodeOption;
import org.eclipse.escet.cif.simulator.options.EndTimeOption;
import org.eclipse.escet.cif.simulator.options.HistoryOption;
import org.eclipse.escet.cif.simulator.options.InputMode;
import org.eclipse.escet.cif.simulator.options.InputModeOption;
import org.eclipse.escet.cif.simulator.options.MaxDelayOption;
import org.eclipse.escet.cif.simulator.options.TestModeOption;
import org.eclipse.escet.cif.simulator.output.plotviz.PlotVisualizationFiltersOption;
import org.eclipse.escet.cif.simulator.output.plotviz.PlotVisualizationOption;
import org.eclipse.escet.cif.simulator.output.plotviz.PlotVisualizerOutputComponent;
import org.eclipse.escet.cif.simulator.output.print.PrintOutputComponent;
import org.eclipse.escet.cif.simulator.output.print.RuntimePrintDecls;
import org.eclipse.escet.cif.simulator.output.stateviz.StateVisualizationFiltersOption;
import org.eclipse.escet.cif.simulator.output.stateviz.StateVisualizationOption;
import org.eclipse.escet.cif.simulator.output.stateviz.StateVisualizerOutputComponent;
import org.eclipse.escet.cif.simulator.output.svgviz.RuntimeCifSvgDecls;
import org.eclipse.escet.cif.simulator.output.svgviz.SvgOutputComponent;
import org.eclipse.escet.cif.simulator.output.svgviz.SvgVisualizationOption;
import org.eclipse.escet.cif.simulator.output.trajdata.TrajDataOption;
import org.eclipse.escet.cif.simulator.output.trajdata.TrajDataOutputComponent;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.ActualTargetTime;
import org.eclipse.escet.cif.simulator.runtime.transitions.HistoryTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.javacompiler.InMemoryJarClassLoader;
import org.eclipse.escet.common.app.framework.javacompiler.JarClassLoader;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.eclipse.escet.common.java.exceptions.InvalidModelException;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.exceptions.UnsupportedException;
import org.eclipse.ui.PlatformUI;

/** CIF simulator. */
public final class CifSimulator {
    /** The simulation context. */
    public final CifSimulatorContext ctxt;

    /** The specification, or {@code null} if not yet or no longer available. */
    public RuntimeSpec<?> spec;

    /** The current state. Is {@code null} before the initial state becomes available. */
    public RuntimeState state;

    /**
     * Constructor for the {@link CifSimulator} class.
     *
     * @param ctxt The simulation context.
     */
    public CifSimulator(CifSimulatorContext ctxt) {
        this.ctxt = ctxt;
    }

    /**
     * Simulate a CIF specification.
     *
     * @param app The simulator application.
     * @return The simulation result, or {@code null} if not actually simulated.
     */
    public SimulationResult simulate(CifSimulatorApp app) {
        // Simulate.
        SimulationResult rslt;
        try {
            // Perform 'normal' simulation, and record the result.
            rslt = simulateInternal(app);

            // If not actually simulated, we are done.
            if (rslt == null) {
                return null;
            }
        } catch (SimulatorExitException ex) {
            // If we stopped outside of the normal simulation loop, record the
            // result.
            rslt = ex.result;
        } catch (CifSimulatorException ex) {
            // Run-time error during simulation.
            RuntimeState errorState = getErrorState(ex);
            String msg;
            if (errorState == null) {
                msg = "Simulation resulted in a runtime error.";
            } else {
                // We don't include the algebraic variables and derivatives of
                // the continuous variables, as they may fail to evaluate (they
                // are derived state). The state variables themselves are
                // already just values, and don't require evaluation.
                msg = fmt("Simulation resulted in a runtime error, for state: %s.",
                        errorState.toSingleLineString(null, false, false));
            }
            throw new InvalidModelException(msg, ex);
        } finally {
            // Cleanup.
            if (spec != null && spec.resourceClassLoader != null) {
                if (spec.resourceClassLoader instanceof Closeable) {
                    try {
                        ((Closeable)spec.resourceClassLoader).close();
                    } catch (IOException ex) {
                        String msg = "Failed to close class loader.";
                        throw new InputOutputException(msg, ex);
                    }
                }
            }
            spec = null;
        }

        // Indicate the result to the output components.
        ctxt.provider.simulationEnded(rslt, state);

        // Ask to confirm termination of the simulator.
        confirmTermination();

        // Return the simulation result.
        return rslt;
    }

    /**
     * Returns the runtime state in which the given exception occurred. The state is determined as follows:
     * <ul>
     * <li>Use the state provided by the root exception, the cause of the root exception, the cause of the cause of the
     * root exception, etc, if available. If multiple of these exceptions provide a state, they should all be the same
     * state.</li>
     * <li>If the exceptions don't provide a state, use the current {@link #state}, if available.</li>
     * <li>If none of the exceptions provide a state, and the current {@link #state} is not available, use
     * {@code null}.</li>
     * </ul>
     *
     * @param rootException The root exception of the simulator runtime error.
     * @return The runtime state in which the given exception occurred, or {@code null}.
     */
    private RuntimeState getErrorState(CifSimulatorException rootException) {
        // Initialize result.
        RuntimeState rslt = null;

        // Obtain state from the exceptions.
        Throwable ex = rootException;
        while (ex != null) {
            if (ex instanceof CifSimulatorException) {
                RuntimeState exState = ((CifSimulatorException)ex).state;
                if (exState != null) {
                    if (rslt == null) {
                        rslt = exState;
                    } else {
                        Assert.check(rslt == exState);
                    }
                }
            }
            ex = ex.getCause();
        }

        // If nothing found yet, use current state.
        if (rslt == null) {
            rslt = this.state;
        }

        // Return the runtime error state. May be 'null'.
        return rslt;
    }

    /**
     * Simulate a CIF specification.
     *
     * @param app The simulator application.
     * @return The simulation result, or {@code null} if not actually simulated.
     */
    private SimulationResult simulateInternal(CifSimulatorApp app) {
        // Load specification.
        RuntimeSpec<?> spec = loadSpec(app);
        if (spec == null) {
            return null;
        }
        ctxt.checkTermination();

        // Initialize specification.
        spec.init(ctxt);
        ctxt.checkTermination();

        // Register trajectory data output component.
        if (TrajDataOption.isEnabled()) {
            OutputProvider.register(new TrajDataOutputComponent());
        }

        // Register state visualizer output components.
        if (StateVisualizationOption.isEnabled()) {
            String[] filtersTxts;
            filtersTxts = StateVisualizationFiltersOption.getFiltersPerViz();
            for (String filtersTxt: filtersTxts) {
                IOutputComponent comp = new StateVisualizerOutputComponent(filtersTxt);
                OutputProvider.register(comp);
            }
        }

        // Register plot visualizer output components.
        if (PlotVisualizationOption.isEnabled()) {
            String[] filtersTxts;
            filtersTxts = PlotVisualizationFiltersOption.getFiltersPerViz();
            for (String filtersTxt: filtersTxts) {
                IOutputComponent comp = new PlotVisualizerOutputComponent(filtersTxt);
                OutputProvider.register(comp);
            }
        }

        // Register SVG output components, and link the input to the outputs.
        boolean svgEnabled = SvgVisualizationOption.isEnabled();
        List<RuntimeCifSvgDecls> cifSvgDecls = spec.getCifSvgDecls();
        InputMode imode = InputModeOption.getInputMode();
        if (svgEnabled && !cifSvgDecls.isEmpty()) {
            for (RuntimeCifSvgDecls cifSvgDecl: cifSvgDecls) {
                SvgOutputComponent comp = new SvgOutputComponent(cifSvgDecl, ctxt);
                OutputProvider.register(comp);
                if (imode == InputMode.SVG) {
                    ((SvgInputComponent<?>)spec.input).initialize(comp);
                }
            }
        } else {
            // Make sure we don't need it for SVG input.
            if (imode == InputMode.SVG) {
                if (!svgEnabled) {
                    String msg = "The SVG input mode requires the use of SVG visualization, which is currently "
                            + "disabled.";
                    throw new InvalidOptionException(msg);
                } else {
                    Assert.check(cifSvgDecls.isEmpty());
                    String msg = "The SVG input mode requires the use of SVG visualization, but the CIF "
                            + "specification being simulated has no CIF/SVG declarations.";
                    throw new InvalidOptionException(msg);
                }
            }
        }

        // Register print output components, before the default console output
        // component.
        List<RuntimePrintDecls> printDeclsList = spec.getPrintDecls();
        for (RuntimePrintDecls printDecls: printDeclsList) {
            PrintOutputComponent comp = new PrintOutputComponent(printDecls, ctxt);
            OutputProvider.register(comp, false);
        }

        // Can't perform real-time simulation without a real-time output
        // component. If test mode is enabled, real-time delays are always
        // skipped, and we can't ever perform real-time simulation, but it is
        // still useful to allow that for testing.
        if (ctxt.realTime && !ctxt.testMode && !ctxt.provider.supportsRealTimeSimulation()) {
            String msg = "Real-time simulation is enabled, but no real-time output components are active.";
            throw new UnsupportedException(msg);
        }

        // Get the initial state (the first source state).
        state = spec.createInitialState(spec);
        ctxt.checkTermination();

        // Check initialization for initial state.
        if (!state.checkInitialization()) {
            // Initialization failure in initial state.
            return SimulationResult.INIT_FAILED;
        }
        ctxt.checkTermination();

        // Send initial state to output components.
        ctxt.provider.initialState(state);
        ctxt.checkTermination();

        // State history initialization.
        if (HistoryOption.isEnabled()) {
            spec.input.history = new CifSimulatorHistory(state);
        }

        // Simulation initialization.
        Double optionEndTime = EndTimeOption.getEndTime();
        Double optionMaxDelay = MaxDelayOption.getMaxDelay();

        // Main simulation loop.
        while (true) {
            // Check for application termination.
            ctxt.checkTermination();

            // Can't perform real-time simulation without a real-time output
            // component. If test mode is enabled, real-time delays are always
            // skipped, and we can't ever perform real-time simulation, but it
            // is still useful to allow that for testing.
            if (ctxt.realTime && !ctxt.testMode && !ctxt.provider.supportsRealTimeSimulation()) {
                // Stop simulation at the request of the user.
                return SimulationResult.USER_TERMINATED;
            }

            // Get possible transitions.
            List<Transition<?>> transitions;
            SimulationResult simulationResult;
            if (optionEndTime != null && state.getTime() >= optionEndTime) {
                // User-provided end time is reached. No more transitions.
                transitions = Collections.emptyList();
                simulationResult = SimulationResult.ENDTIME_REACHED;
            } else {
                // Get the maximum allowed end time for the next time transition.
                Double inputMaxEndTime = state.getNextMaxEndTime();
                Double nextMaxEndTime = optionEndTime;
                if (nextMaxEndTime == null) { // Option inf, use input.
                    nextMaxEndTime = inputMaxEndTime;
                } else if (inputMaxEndTime != null) { // Neither inf, use minimum.
                    nextMaxEndTime = Math.min(nextMaxEndTime, inputMaxEndTime);
                }

                // Calculate possible transitions.
                state.calcTransitions(nextMaxEndTime, optionMaxDelay);
                transitions = cast(spec.transitions);

                // Check for deadlock.
                if (transitions.isEmpty()) {
                    simulationResult = SimulationResult.DEADLOCK;
                } else {
                    simulationResult = null;
                }
            }

            // Check for application termination.
            ctxt.checkTermination();

            // Inform output components of the possible transitions.
            ctxt.provider.possibleTransitions(transitions, simulationResult);

            // Choose a transition.
            Transition<?> transition = state.chooseTransition(state, transitions, simulationResult);
            if (transition == null) {
                // No transition has been chosen. Try again.
                continue;
            }
            ctxt.checkTermination();

            // Get target time (for time transitions).
            ChosenTargetTime chosenTargetTime = null;
            if (transition instanceof TimeTransition) {
                TimeTransition<?> timeTrans = (TimeTransition<?>)transition;
                double maxTargetTime = timeTrans.getLastTime();
                chosenTargetTime = state.chooseTargetTime(maxTargetTime);
                ctxt.checkTermination();
            }

            // Inform the output components of the transition that was chosen.
            ctxt.provider.transitionChosen(state, transition, chosenTargetTime);

            // Take the transition. Since time transitions may be interrupted
            // by the input component, we update the chosen target time to the
            // actual target time.
            ActualTargetTime actualTargetTime = null;
            if (transition instanceof TimeTransition) {
                TimeTransition<?> timeTrans = (TimeTransition<?>)transition;
                actualTargetTime = timeTrans.takeTimeTransition(chosenTargetTime);
                ctxt.checkTermination();
            }

            // Get the target state of the transition. If non-strict request,
            // we may not get an answer.
            Double targetStateTime = (actualTargetTime == null) ? null : actualTargetTime.targetTime;
            Boolean strictTargetTime = (chosenTargetTime == null) ? null : chosenTargetTime.strictTarget;
            RuntimeState targetState = transition.getTargetState(targetStateTime, strictTargetTime);

            // If non-strict request, and no answer, we have no exact/close
            // time point in the trajectories.
            if (targetState == null) {
                // Paranoia check: time transition and non-strict request.
                Assert.check(strictTargetTime != null && !strictTargetTime);

                // Clear all previously computed transitions, and recompute the
                // time transition.
                state.spec.transitions.clear();
                state.calcTimeTransition(targetStateTime);
                ctxt.checkTermination();

                Assert.check(state.spec.transitions.size() == 1);
                Transition<?> trans = first(state.spec.transitions);
                TimeTransition<?> timeTrans = (TimeTransition<?>)trans;

                // Take the recomputed time transition (strict request).
                targetState = timeTrans.getTargetState(targetStateTime, true);
            }

            // Move on to the target state, before informing the output
            // components. This ensures that if we evaluate anything in the
            // output components, the simulator state is correctly reflected
            // in error messages.
            RuntimeState sourceState = state;
            state = targetState;

            // Inform the output components of the transition that was taken.
            Boolean interrupted = (actualTargetTime == null) ? null : actualTargetTime.interrupted;
            ctxt.provider.transitionTaken(sourceState, transition, targetState, interrupted);

            // Add new state to state history. However, if reached via a
            // history transition, the history has already been updated.
            if (spec.input.history != null && !(transition instanceof HistoryTransition)) {
                spec.input.history.addState(state);
            }
        }
    }

    /**
     * Loads the CIF specification.
     *
     * @param app The simulator application.
     * @return The loaded CIF specification, or {@code null} if code is only compiled but not loaded and simulation
     *     should not be performed.
     */
    private static RuntimeSpec<?> loadSpec(CifSimulatorApp app) {
        // Decide how to load the specification, based on several options.
        ClassLoader classLoader;
        String classPath = DebugCodeOption.getClasspath();
        if (classPath == null) {
            // Get input CIF specification file path.
            String inputPath = CifSpecOption.getCifSpecPath();

            // Decide what to do based on file name.
            if (inputPath.toLowerCase(Locale.US).endsWith(".cifcode")) {
                // Get path to already compiled code file.
                String path = Paths.resolve(inputPath);
                File file = new File(path);
                if (!file.exists()) {
                    String msg = fmt("Could not find compiled code file \"%s\".", inputPath);
                    throw new InvalidOptionException(msg);
                } else if (!file.isFile()) {
                    String msg = fmt("Compiled code file path \"%s\" does not refer to a file.", inputPath);
                    throw new InvalidOptionException(msg);
                }

                // Use already compiled code.
                ClassLoader dependencyLoader = CifSimulator.class.getClassLoader();
                classLoader = new InMemoryJarClassLoader(path, dependencyLoader);
            } else {
                // Compile the specification.
                CifCompilerContext ctxt = new CifCompilerContext();
                ctxt.app = app;
                CifCompiler.compileSpec(inputPath, ctxt);
                ctxt.app = null;

                // Get class loader.
                classLoader = ctxt.getClassLoader();

                // If compile only, we are done.
                if (CompileOnlyOption.isEnabled()) {
                    return null;
                }
            }
        } else {
            // Determine the absolute class path.
            classPath = Paths.resolve(classPath);

            // The class path is a directory. 'URLClassLoader' requires directories to end with a '/'. If not,
            // 'URLClassLoader' assumes there is a single JAR file.
            String platformSeperator = Character.toString(Paths.getPlatformSeparator());
            if (!classPath.endsWith(platformSeperator)) {
                classPath += platformSeperator;
            }

            // Determine the class path URL.
            URL url;
            try {
                url = Paths.createJavaURI(classPath).toURL();
            } catch (MalformedURLException e) {
                String msg = fmt("Invalid classpath specified for the --debug-code option: \"%s\".", classPath);
                throw new InvalidOptionException(msg, e);
            }

            // Construct class loader for the simulator, extended with the
            // given class path.
            URL[] urls = new URL[] {url};
            classLoader = CifSimulator.class.getClassLoader();
            classLoader = new URLClassLoader(urls, classLoader);
        }

        // Check version.
        checkVersion(classLoader, app);

        // Load specification class.
        String specClassName = CifCompilerContext.getClassName("Spec");
        Class<?> specClass;
        try {
            specClass = classLoader.loadClass(specClassName);
        } catch (ClassNotFoundException e) {
            if (classPath != null) {
                // Debug code .
                String msg = fmt("Failed to load specification from debug code (classpath: \"%s\").", classPath);
                throw new InvalidOptionException(msg, e);
            } else if (classLoader instanceof JarClassLoader) {
                // Compiled code (.cifcode JAR).
                String msg = fmt("Failed to load specification from compiled code file \"%s\". Make sure the "
                        + "file is really a compiled code file.", CifSpecOption.getCifSpecPath());
                throw new InvalidOptionException(msg, e);
            } else {
                // Unexpected. Note that for in-memory compilation, this should
                // never fail.
                String msg = "Unexpected class loader: " + classLoader;
                throw new RuntimeException(msg);
            }
        }

        // Get the specification.
        Field specField;
        try {
            specField = specClass.getField("SPEC");
        } catch (SecurityException | NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        }

        Object specObj;
        try {
            specObj = specField.get(null);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }

        RuntimeSpec<?> spec = (RuntimeSpec<?>)specObj;

        // Store the class loader for later use.
        spec.resourceClassLoader = classLoader;

        // Return the specification.
        return spec;
    }

    /**
     * Check the version of the simulator that compiled the code against the version of the current simulator.
     *
     * @param classLoader The class loader to use to obtain the version resource.
     * @param app The simulator application.
     */
    private static void checkVersion(ClassLoader classLoader, CifSimulatorApp app) {
        // Get version resource path.
        String path = CifCompilerContext.VERSION_RES_NAME;
        path = CifCompilerContext.PACKAGE + "/" + path;

        // Get versions.
        String currentVersion = app.getAppVersion();
        String loadedVersion;

        try (InputStream stream = classLoader.getResourceAsStream(path)) {
            if (stream == null) {
                if (classLoader instanceof JarClassLoader) {
                    // Compiled code (.cifcode JAR).
                    String msg = fmt("Failed to obtain version from compiled code file \"%s\". Make sure "
                            + "the file is really a compiled code file.", CifSpecOption.getCifSpecPath());
                    throw new InvalidOptionException(msg);
                } else if (classLoader instanceof URLClassLoader) {
                    // Debug code. This case must be after the
                    // 'JarClassLoader', as that class extends from
                    // 'URLClassLoader'.
                    String msg = fmt("Failed to obtain version from debug code (classpath: \"%s\").",
                            DebugCodeOption.getClasspath());
                    throw new InvalidOptionException(msg);
                } else {
                    // Unexpected. Note that for in-memory compilation, this
                    // should never fail.
                    String msg = "Unexpected class loader: " + classLoader;
                    throw new RuntimeException(msg);
                }
            }

            // Get versions.
            loadedVersion = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            String msg = "Failed to read version of compiled code.";
            throw new InputOutputException(msg, ex);
        }

        // Check version.
        if (!currentVersion.equals(loadedVersion)) {
            String msg = fmt("Compiled code version (%s) vs simulator version (%s) mismatch. Please recompile.",
                    loadedVersion, currentVersion);
            OutputProvider.warn(msg);
        }
    }

    /** Ask to confirm termination of the simulator. */
    private void confirmTermination() {
        // Only ask the user if requested by the option.
        AskToTerminateMode mode = AskToTerminateOption.getMode();
        switch (mode) {
            case ON:
                // Always ask.
                break;

            case OFF:
                // Never ask.
                return;

            case AUTO: {
                // Simulator decides.
                if (Platform.isRunning() && PlatformUI.isWorkbenchRunning()) {
                    return;
                }
                if (TestModeOption.isEnabled()) {
                    return;
                }
                if (!ctxt.provider.hasVisualInterface()) {
                    return;
                }
                break;
            }
        }

        // Ask the user.
        String question = "Press <ENTER> to terminate the simulator...";
        ctxt.appEnvData.getStreams().out.print(question);
        ctxt.appEnvData.getStreams().out.flush();

        // Wait for user to press <ENTER>.
        try {
            ctxt.appEnvData.getStreams().in.readLine();
        } catch (IOException ex) {
            String msg = "Could not read input from stdin.";
            throw new InputOutputException(msg, ex);
        }
    }
}
