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

package org.eclipse.escet.cif.simulator.output.svgviz;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.apache.commons.math3.util.Precision;
import org.eclipse.escet.cif.simulator.CifSimulatorContext;
import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.cif.simulator.options.InputModeOption;
import org.eclipse.escet.cif.simulator.options.MaxTimePointTolOption;
import org.eclipse.escet.cif.simulator.output.NullSimulatorOutputComponent;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;
import org.eclipse.escet.cif.simulator.runtime.SimulatorExitException;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.ApplicationException;
import org.eclipse.escet.common.svg.SvgCanvas;
import org.eclipse.escet.common.svg.SvgException;
import org.eclipse.escet.common.svg.SvgVisualizer;

/**
 * CIF simulator output component that visualizes the current state of the CIF specification by using an SVG image file
 * and CIF/SVG declarations provided by the user.
 *
 * <p>
 * The SVG output component runs on the same thread as the simulator. It forwards states to the {@link SvgRenderThread},
 * pre-computing states to ensure that a state is always available when the render thread has time to render one.
 * </p>
 */
public class SvgOutputComponent extends NullSimulatorOutputComponent {
    /** Whether to output internal/developer debugging information. */
    private static final boolean DEBUG = false;

    /** The CIF/SVG declarations. */
    public final RuntimeCifSvgDecls cifSvgDecls;

    /** The SVG canvas. */
    public final SvgCanvas canvas;

    /** The SVG render thread. */
    public final SvgRenderThread renderThread;

    /** The SVG paint thread. */
    public final SvgPaintThread paintThread;

    /** Whether shutdown of the render and paint threads has been requested. */
    private boolean shutdownRequested = false;

    /** Is an {@link InputModeOption#isInteractive interactive} simulation mode enabled? */
    private final boolean interactive;

    /** Is real-time simulation enabled? */
    private final boolean realTime;

    /** The model time delta used for real-time simulation, or {@code null} if real-time simulation is disabled. */
    private final Double modelTimeDelta;

    /** The maximum tolerance of time points in ulps, for the retrieval of values from trajectories. */
    private final int maxTimePointTol;

    /** The last model time for which we forwarded a state/frame. */
    private double lastTime;

    /**
     * Constructor for the {@link SvgOutputComponent} class.
     *
     * @param cifSvgDecls The CIF/SVG declarations.
     * @param ctxt The simulator runtime context.
     */
    public SvgOutputComponent(RuntimeCifSvgDecls cifSvgDecls, CifSimulatorContext ctxt) {
        // Store CIF/SVG declarations and options.
        this.cifSvgDecls = cifSvgDecls;
        this.interactive = InputModeOption.isInteractive();
        this.realTime = FrameRateOption.isRealTimeEnabled();
        this.modelTimeDelta = FrameRateOption.getModelTimeDelta();
        this.maxTimePointTol = MaxTimePointTolOption.getMaxTimePointTol();

        // Open the SVG visualizer.
        SvgVisualizer visualizer = ControlEditor.show(cifSvgDecls.getSvgAbsPath(), SvgVisualizer.class,
                "show the SVG visualizer", !testMode);

        // If loading the SVG image failed, simulation fails as well.
        SvgException svgLoadErr = visualizer.getSvgLoadError();
        if (svgLoadErr != null) {
            throw svgLoadErr;
        }

        // Get the SVG canvas, now that we know that loading succeeded.
        canvas = visualizer.getSvgCanvas();
        canvas.path = cifSvgDecls.getSvgRelPath();

        // Initialize the CIF/SVG declarations.
        cifSvgDecls.init(visualizer.getDocument(), visualizer.getSvgCanvas().getBridgeContext(), ctxt);

        // Create threads.
        paintThread = new SvgPaintThread(visualizer);
        renderThread = new SvgRenderThread(cifSvgDecls, visualizer, paintThread);

        // Start threads.
        renderThread.start();
        paintThread.start();
    }

    @Override
    public void cleanup() {
        if (!shutdownRequested) {
            // Don't shutdown more than once.
            shutdownRequested = true;

            // Send shutdown request first, to ensure that we always request
            // the threads to die.
            renderThread.addState(ShutdownRuntimeState.INSTANCE);

            // Wait for threads to complete their shutdown. No new crashes can
            // occur after this.
            try {
                renderThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                paintThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Check for crashes, one final time. We check this at the very
            // end, and not at the beginning, as throwing an exception before
            // we send the shutdown request leaves the threads alive.
            checkThreadCrash();
        }
    }

    /**
     * Check for crashes (and other runtime errors) of the render and paint threads. If one of them crashed, make the
     * simulator thread crash with that exception. Cleanup of the output component by the application framework will
     * send shutdown requests to the other treads.
     */
    private void checkThreadCrash() {
        // Initialization.
        Throwable ex = null;
        Thread thread = null;

        // See if the render or paint thread crashed or has an other error.
        // Get the exception, and reset it, to make sure we don't report it
        // more than once.
        ex = renderThread.exception.getAndSet(null);
        if (ex != null) {
            thread = renderThread;
        }

        if (ex == null) {
            ex = paintThread.exception.getAndSet(null);
            if (ex != null) {
                thread = paintThread;
            }
        }

        if (ex == null) {
            // No crashes/errors, we are done.
            return;
        }

        // Error/crash found. Handle simulator exceptions as well as the
        // exceptions that the application framework handles, as special cases.
        if (ex instanceof SimulatorExitException) {
            // Simulator exit.
            return;
        } else if (ex instanceof CifSimulatorException) {
            // Simulator runtime error.
            throw (CifSimulatorException)ex;
        } else if (ex instanceof ApplicationException) {
            // General application error.
            throw (ApplicationException)ex;
        } else if (ex instanceof OutOfMemoryError) {
            // Out of memory crash.
            throw (OutOfMemoryError)ex;
        } else {
            // Other internal crash.
            @SuppressWarnings("null")
            String threadName = thread.getName();
            throw new RuntimeException(threadName + " crashed.", ex);
        }
    }

    /**
     * Checks whether the visualizer has been closed by the user. If so, the output component is
     * {@link OutputProvider#unregister unregistered}, to ensure we don't spent useless time rendering, etc.
     *
     * @return Whether the visualizer has been closed ({@code true}) or not ({@code false}).
     */
    private boolean checkVisualizerClosed() {
        // If canvas has not been disposed, the visualizer is still alive.
        if (!canvas.isDisposed()) {
            return false;
        }

        // Canvas was disposed. Unregister output component and signal closure
        // back to the caller.
        OutputProvider.unregister(this);
        return true;
    }

    @Override
    public void initialState(RuntimeState state) {
        // Always send initial state.
        checkThreadCrash();
        if (checkVisualizerClosed()) {
            return;
        }

        renderThread.addState(state);
        lastTime = state.getTime();
        Assert.check(lastTime == 0);

        if (DEBUG) {
            debug("Initial state: " + state.toSingleLineString(null, false, false));
        }
    }

    @Override
    public void intermediateTrajectoryState(RuntimeState state) {
        // Always send intermediate frames. Note that intermediate frame states
        // are only generated if real-time simulation is enabled.
        checkThreadCrash();
        if (checkVisualizerClosed()) {
            return;
        }

        renderThread.addState(state);
        lastTime = state.getTime();

        if (DEBUG) {
            debug("Frame state: " + state.toSingleLineString(null, false, false));
        }
    }

    @Override
    public void transitionTaken(RuntimeState sourceState, Transition<?> transition, RuntimeState targetState,
            Boolean interrupted)
    {
        // Whether the target state becomes a frame depends on various options.
        if (generateFrame(transition, targetState)) {
            checkThreadCrash();
            if (checkVisualizerClosed()) {
                return;
            }

            renderThread.addState(targetState);
            lastTime = targetState.getTime();

            if (DEBUG) {
                debug("Target state: " + targetState.toSingleLineString(null, false, false));
            }
        }
    }

    /**
     * Should a frame be generated for the target state of the given transition?
     *
     * <p>
     * If the interactive simulator input mode is used, and/or real-time simulation is disabled, frames are generated
     * for all states. This includes the initial state, as well as all target states of transitions. For time
     * transitions, no intermediate frame states are generated, and they thus also don't lead to additional intermediate
     * frames. Note that it is essential for interactive mode, that when interactive input is asked for, the current
     * state is displayed. As such, the results of all transitions must be displayed.
     * </p>
     *
     * <p>
     * If real-time simulation is enabled, frames are only generated at multiples of the model time delta used for
     * real-time simulation. That is, the initial state, as well as all intermediate frame states, result in frames
     * being generated. Furthermore, target states of time transitions that have a target time that is a multiple of the
     * model time delta, also result in frames.
     * </p>
     *
     * @param transition The transition that was taken.
     * @param targetState The target state of the transition.
     * @return {@code true} if a frame should be generated, {@code false} otherwise.
     */
    private boolean generateFrame(Transition<?> transition, RuntimeState targetState) {
        // For interactive mode, make sure we have up to date output at the
        // moment we are asked to make a choice from the command line. Note
        // that we don't take into account semi-automatic mode.
        if (interactive) {
            return true;
        }

        // If not real-time, generate frames for all states.
        if (!realTime) {
            return true;
        }

        // Real-time. Skip event transitions.
        if (transition instanceof EventTransition) {
            return false;
        }

        // Non-interactive. Can't have history transitions, so must be a time
        // transition.
        Assert.check(transition instanceof TimeTransition);

        // Check whether target state is the next multiple of the model
        // time delta used for real-time simulation.
        double targetTime = targetState.getTime();
        double nextMultiple = lastTime + modelTimeDelta;
        return Precision.equals(targetTime, nextMultiple, maxTimePointTol);
    }

    /**
     * Prints a line of internal/developer debug output. Should only be invoked if {@link #DEBUG} is {@code true}.
     *
     * @param txt The line of text to print.
     */
    private void debug(String txt) {
        System.out.println(getClass().getSimpleName() + ": " + txt);
    }

    /**
     * Returns the option category with options for this output component.
     *
     * @return The option category with options for this output component.
     */
    @SuppressWarnings("rawtypes")
    public static OptionCategory getOptions() {
        List<OptionCategory> subCats = list();

        List<Option> opts = list();
        opts.add(Options.getInstance(SvgVisualizationOption.class));

        return new OptionCategory("SVG visualization", "SVG visualization options.", subCats, opts);
    }

    @Override
    public boolean hasVisualInterface() {
        return !testMode && !canvas.isDisposed();
    }

    @Override
    public boolean supportsRealTimeSimulation() {
        return true;
    }
}
