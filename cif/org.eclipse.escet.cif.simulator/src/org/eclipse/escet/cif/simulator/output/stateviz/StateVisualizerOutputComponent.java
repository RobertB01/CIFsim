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

package org.eclipse.escet.cif.simulator.output.stateviz;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.simulator.options.CifSpecOption;
import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.cif.simulator.options.InputModeOption;
import org.eclipse.escet.cif.simulator.output.NullSimulatorOutputComponent;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.SWTUtils;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;

/** Output component to show the state of the automata and variables, as text in a table. */
public class StateVisualizerOutputComponent extends NullSimulatorOutputComponent {
    /** The state visualizer. Is {@code null} if not available. */
    private StateVisualizer visualizer;

    /** Is an {@link InputModeOption#isInteractive interactive} simulation mode enabled? */
    private final boolean interactive;

    /** Is real-time simulation enabled? */
    private final boolean realTime;

    /** Whether to update the state visualizer GUI synchronously ({@code true}) or asynchronously ({@code false}). */
    private final boolean sync = true;

    /**
     * Constructor for the {@link StateVisualizerOutputComponent} class.
     *
     * @param filtersTxt The state objects filters text.
     */
    public StateVisualizerOutputComponent(String filtersTxt) {
        if (testMode) {
            visualizer = null;
        } else {
            String path = CifSpecOption.getCifSpecPath();
            visualizer = ControlEditor.show(path, StateVisualizer.class, "show the state visualizer");
            visualizer.filtersTxt = filtersTxt;
        }

        interactive = InputModeOption.isInteractive();
        realTime = FrameRateOption.isRealTimeEnabled();
    }

    @Override
    public void initialState(final RuntimeState state) {
        // Always update for initial state.
        final StateVisualizer viz = visualizer;
        if (viz == null) {
            return;
        }
        viz.initMeta(state);
        SWTUtils.exec(sync, new Runnable() {
            @Override
            public void run() {
                if (!viz.isAvailable()) {
                    return;
                }
                viz.initTable(state);
                viz.update(state);
            }
        });
    }

    @Override
    public void intermediateTrajectoryState(final RuntimeState state) {
        // Always update for intermediate states, which are only generated for
        // real-time simulation.
        final StateVisualizer viz = visualizer;
        if (viz == null) {
            return;
        }
        SWTUtils.exec(sync, new Runnable() {
            @Override
            public void run() {
                if (!viz.isAvailable()) {
                    return;
                }
                viz.update(state);
            }
        });
    }

    @Override
    public void transitionTaken(RuntimeState sourceState, Transition<?> transition, final RuntimeState targetState,
            Boolean interrupted)
    {
        // For non-interactive real-time simulation, we only update for
        // non-event transitions (e.g. the end of time transitions, history
        // transitions). This prevents updating for practically
        // infinitely fast occurring event transitions, in case of an event
        // transition livelock (events happen very fast, with no time passing
        // in between). In such situations, the GUI thread becomes consumed by
        // processing the asynchronous state visualizer update requests/code,
        // with more new asynchronous requests being created than can be
        // handled in that same time, thus blocking the GUI.
        if (!interactive && realTime) {
            if (transition instanceof EventTransition) {
                return;
            }
        }

        // Update visualizer.
        final StateVisualizer viz = visualizer;
        if (viz == null) {
            return;
        }
        SWTUtils.exec(sync, new Runnable() {
            @Override
            public void run() {
                if (!viz.isAvailable()) {
                    return;
                }
                viz.update(targetState);
            }
        });
    }

    @Override
    public void cleanup() {
        if (visualizer != null) {
            visualizer = null;
        }
    }

    @Override
    public boolean hasVisualInterface() {
        return visualizer != null && visualizer.isAvailable();
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
        opts.add(Options.getInstance(StateVisualizationOption.class));
        opts.add(Options.getInstance(StateVisualizationFiltersOption.class));

        return new OptionCategory("State visualization", "State visualization options.", subCats, opts);
    }
}
