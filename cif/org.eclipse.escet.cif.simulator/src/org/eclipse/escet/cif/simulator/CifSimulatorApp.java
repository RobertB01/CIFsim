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

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.simulator.options.AskToTerminateOption;
import org.eclipse.escet.cif.simulator.options.AutoAlgoOption;
import org.eclipse.escet.cif.simulator.options.AutoTimeDurationOption;
import org.eclipse.escet.cif.simulator.options.CifSpecInitOption;
import org.eclipse.escet.cif.simulator.options.CifSpecOption;
import org.eclipse.escet.cif.simulator.options.CompileOnlyOption;
import org.eclipse.escet.cif.simulator.options.CompiledCodeFileOption;
import org.eclipse.escet.cif.simulator.options.CompleteModeOption;
import org.eclipse.escet.cif.simulator.options.DebugCodeOption;
import org.eclipse.escet.cif.simulator.options.DistributionSeedOption;
import org.eclipse.escet.cif.simulator.options.EndTimeOption;
import org.eclipse.escet.cif.simulator.options.EnvironmentEventsOption;
import org.eclipse.escet.cif.simulator.options.ExtFuncAsyncOption;
import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.cif.simulator.options.HistoryOption;
import org.eclipse.escet.cif.simulator.options.HistorySizeOption;
import org.eclipse.escet.cif.simulator.options.InputModeOption;
import org.eclipse.escet.cif.simulator.options.InteractiveAutoChooseOption;
import org.eclipse.escet.cif.simulator.options.MaxDelayOption;
import org.eclipse.escet.cif.simulator.options.MaxTimePointTolOption;
import org.eclipse.escet.cif.simulator.options.ProfilingOption;
import org.eclipse.escet.cif.simulator.options.SimulationSpeedOption;
import org.eclipse.escet.cif.simulator.options.TestModeOption;
import org.eclipse.escet.cif.simulator.options.TraceInputFileOption;
import org.eclipse.escet.cif.simulator.output.DebugOutputOption;
import org.eclipse.escet.cif.simulator.output.NormalOutputOption;
import org.eclipse.escet.cif.simulator.output.NormalOutputStateFiltersOption;
import org.eclipse.escet.cif.simulator.output.SimulatorOutputComponent;
import org.eclipse.escet.cif.simulator.output.SimulatorOutputProvider;
import org.eclipse.escet.cif.simulator.output.SimulatorStreamOutputComponent;
import org.eclipse.escet.cif.simulator.output.plotviz.PlotVisualizerOutputComponent;
import org.eclipse.escet.cif.simulator.output.stateviz.StateVisualizerOutputComponent;
import org.eclipse.escet.cif.simulator.output.svgviz.SvgOutputComponent;
import org.eclipse.escet.cif.simulator.output.trajdata.TrajDataOutputComponent;
import org.eclipse.escet.cif.simulator.runtime.ode.IntegratorAbsTolOption;
import org.eclipse.escet.cif.simulator.runtime.ode.IntegratorAlgoOption;
import org.eclipse.escet.cif.simulator.runtime.ode.IntegratorMaxStepOption;
import org.eclipse.escet.cif.simulator.runtime.ode.IntegratorMinStepOption;
import org.eclipse.escet.cif.simulator.runtime.ode.IntegratorNumStepsOption;
import org.eclipse.escet.cif.simulator.runtime.ode.IntegratorRelTolOption;
import org.eclipse.escet.cif.simulator.runtime.ode.OdeSolverOutStepOption;
import org.eclipse.escet.cif.simulator.runtime.ode.RootFinderAbsTolOption;
import org.eclipse.escet.cif.simulator.runtime.ode.RootFinderAlgoOption;
import org.eclipse.escet.cif.simulator.runtime.ode.RootFinderMaxCheckOption;
import org.eclipse.escet.cif.simulator.runtime.ode.RootFinderMaxIterOption;
import org.eclipse.escet.cif.simulator.runtime.ode.RootFinderRelTolOption;
import org.eclipse.escet.common.app.framework.AppProperties;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.javacompiler.JavaCompilerOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/** CIF simulator application. */
public class CifSimulatorApp extends Application<SimulatorOutputComponent> {
    /**
     * Application main method.
     *
     * @param args The command line arguments supplied to the application.
     */
    public static void main(String[] args) {
        CifSimulatorApp app = new CifSimulatorApp();
        app.run(args);
    }

    /** Constructor for the {@link CifSimulatorApp} class. */
    public CifSimulatorApp() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link CifSimulatorApp} class.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public CifSimulatorApp(AppStreams streams) {
        super(streams);
    }

    /**
     * Constructor for the {@link CifSimulatorApp} class.
     *
     * @param streams The application stream to use for the application, or {@code null} to use {@link System#in},
     *     {@link System#out}, and {@link System#err} as streams.
     * @param provider The output provider to use for the application, or {@code null} to ask the application for the
     *     output provider.
     * @param options The options to use for the application, or {@code null} to create a fresh {@link Options}
     *     instance.
     * @param properties The properties to use for the application, or {@code null} to create a fresh
     *     {@link AppProperties} instance.
     */
    public CifSimulatorApp(AppStreams streams, OutputProvider<?> provider, Options options, AppProperties properties) {
        super(streams);
    }

    @Override
    protected OutputProvider<SimulatorOutputComponent> getProvider() {
        return new SimulatorOutputProvider();
    }

    @Override
    protected IOutputComponent getStreamOutputComponent(AppStream out, AppStream err) {
        return new SimulatorStreamOutputComponent(out, err);
    }

    @Override
    public String getAppName() {
        return "CIF simulator";
    }

    @Override
    public String getAppDescription() {
        return "The simulator can be used to explore the state space of CIF specifications, either interactively, "
                + "or using a more automated approach. Powerful visualization features allow for interactive "
                + "visualization based validation of the CIF specification.";
    }

    @Override
    protected int runInternal() {
        // Simulate.
        CifSimulatorContext ctxt = new CifSimulatorContext(getAppEnvData());
        CifSimulator simulator = new CifSimulator(ctxt);
        simulator.simulate(this);

        // If we get here, simulation was successful.
        return 0;
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected OptionCategory getAllOptions() {
        // General.
        OptionCategory genCat = getGeneralOptionCategory();

        // Simulator.
        List<Option> simOpts = list();
        simOpts.add(Options.getInstance(CifSpecOption.class));
        simOpts.add(Options.getInstance(CifSpecInitOption.class));
        simOpts.add(Options.getInstance(EndTimeOption.class));
        simOpts.add(Options.getInstance(MaxDelayOption.class));
        simOpts.add(Options.getInstance(DistributionSeedOption.class));
        simOpts.add(Options.getInstance(CompleteModeOption.class));
        simOpts.add(Options.getInstance(ExtFuncAsyncOption.class));
        simOpts.add(Options.getInstance(ProfilingOption.class));
        simOpts.add(Options.getInstance(AskToTerminateOption.class));
        simOpts.add(Options.getInstance(MaxTimePointTolOption.class));
        simOpts.add(Options.getInstance(DebugCodeOption.class));
        List<OptionCategory> simSubCats = list();
        OptionCategory simCat = new OptionCategory("Simulator", "Simulator options.", simSubCats, simOpts);

        // Compiler.
        List<Option> compileOpts = list();
        compileOpts.add(Options.getInstance(CompileOnlyOption.class));
        compileOpts.add(Options.getInstance(CompiledCodeFileOption.class));
        compileOpts.add(Options.getInstance(JavaCompilerOption.class));
        List<OptionCategory> compileSubCats = list();
        OptionCategory compileCat = new OptionCategory("Compiler", "Compiler options.", compileSubCats, compileOpts);

        // Input.
        List<Option> inOpts = list();
        inOpts.add(Options.getInstance(InputModeOption.class));
        inOpts.add(Options.getInstance(AutoAlgoOption.class));
        inOpts.add(Options.getInstance(AutoTimeDurationOption.class));
        inOpts.add(Options.getInstance(TraceInputFileOption.class));
        inOpts.add(Options.getInstance(InteractiveAutoChooseOption.class));
        inOpts.add(Options.getInstance(EnvironmentEventsOption.class));
        inOpts.add(Options.getInstance(HistoryOption.class));
        inOpts.add(Options.getInstance(HistorySizeOption.class));
        List<OptionCategory> inSubCats = list();
        OptionCategory inCat = new OptionCategory("Input", "Input mode options.", inSubCats, inOpts);

        // Output.
        List<Option> outOpts = list();
        outOpts.add(Options.getInstance(NormalOutputOption.class));
        outOpts.add(Options.getInstance(NormalOutputStateFiltersOption.class));
        outOpts.add(Options.getInstance(DebugOutputOption.class));
        outOpts.add(Options.getInstance(FrameRateOption.class));
        outOpts.add(Options.getInstance(SimulationSpeedOption.class));
        outOpts.add(Options.getInstance(TestModeOption.class));
        List<OptionCategory> outSubCats = list();
        outSubCats.add(SvgOutputComponent.getOptions());
        outSubCats.add(TrajDataOutputComponent.getOptions());
        outSubCats.add(PlotVisualizerOutputComponent.getOptions());
        outSubCats.add(StateVisualizerOutputComponent.getOptions());
        OptionCategory outCat = new OptionCategory("Output", "Output options.", outSubCats, outOpts);

        // ODE integrator.
        List<Option> intOpts = list();
        intOpts.add(Options.getInstance(IntegratorAlgoOption.class));
        intOpts.add(Options.getInstance(IntegratorAbsTolOption.class));
        intOpts.add(Options.getInstance(IntegratorRelTolOption.class));
        intOpts.add(Options.getInstance(IntegratorMinStepOption.class));
        intOpts.add(Options.getInstance(IntegratorMaxStepOption.class));
        intOpts.add(Options.getInstance(IntegratorNumStepsOption.class));
        List<OptionCategory> intSubCats = list();
        OptionCategory intCat = new OptionCategory("ODE integrator", "ODE solver integrator options.", intSubCats,
                intOpts);

        // ODE root finder.
        List<Option> rootOpts = list();
        rootOpts.add(Options.getInstance(RootFinderMaxCheckOption.class));
        rootOpts.add(Options.getInstance(RootFinderAlgoOption.class));
        rootOpts.add(Options.getInstance(RootFinderAbsTolOption.class));
        rootOpts.add(Options.getInstance(RootFinderRelTolOption.class));
        rootOpts.add(Options.getInstance(RootFinderMaxIterOption.class));
        List<OptionCategory> rootSubCats = list();
        OptionCategory rootCat = new OptionCategory("ODE root finder", "ODE solver root finder options.", rootSubCats,
                rootOpts);

        // ODE solver.
        List<Option> odeOpts = list();
        odeOpts.add(Options.getInstance(OdeSolverOutStepOption.class));
        List<OptionCategory> odeSubCats = list(intCat, rootCat);
        OptionCategory odeCat = new OptionCategory("ODE solver", "ODE solver options.", odeSubCats, odeOpts);

        // All options.
        return new OptionCategory("CIF Simulator Options", "All options for the CIF simulator.",
                list(genCat, simCat, compileCat, inCat, outCat, odeCat), list());
    }
}
