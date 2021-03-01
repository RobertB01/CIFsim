//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output.plotviz;

import static org.eclipse.escet.cif.simulator.output.plotviz.PlotVisualizationMode.LIVE;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;
import java.util.function.Consumer;

import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.options.CifSpecOption;
import org.eclipse.escet.cif.simulator.options.FrameRateOption;
import org.eclipse.escet.cif.simulator.output.NullSimulatorOutputComponent;
import org.eclipse.escet.cif.simulator.runtime.SimulationResult;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.transitions.EventTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.HistoryTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.TimeTransition;
import org.eclipse.escet.cif.simulator.runtime.transitions.Transition;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;
import org.eclipse.escet.common.java.Assert;

/** Output component to graphically plot the values of variables as time progresses, during simulation. */
public class PlotVisualizerOutputComponent extends NullSimulatorOutputComponent {
    /** Is real-time mode enabled? */
    private final boolean realTime;

    /** The plot visualization mode. */
    private final PlotVisualizationMode mode;

    /** The plot visualizer data. */
    private PlotVisualizerData data;

    /** The plot visualizer. Is {@code null} if not available. */
    private PlotVisualizer visualizer;

    /**
     * Constructor for the {@link PlotVisualizerOutputComponent} class.
     *
     * @param filtersTxt The state objects filters text.
     */
    public PlotVisualizerOutputComponent(String filtersTxt) {
        realTime = FrameRateOption.isRealTimeEnabled();
        mode = PlotVisualizationModeOption.getPlotVizMode();
        data = new PlotVisualizerData(filtersTxt);
        if (mode == LIVE) {
            showVisualizer();
        }
    }

    @Override
    public void initialState(RuntimeState state) {
        data.initMeta(state);

        if (mode == LIVE) {
            doVisualizer(v -> {
                v.initVarDatas(data.metas);
                addState(state, v);
                setRange(v);
            });
        } else {
            data.initVarDatas();
            addState(state, null);
        }
    }

    @Override
    public void transitionChosen(RuntimeState sourceState, Transition<?> transition,
            ChosenTargetTime chosenTargetTime)
    {
        if (transition instanceof EventTransition) {
            // Skip event transition.
        } else if (transition instanceof TimeTransition) {
            // Add initial state of time transition.
            if (mode == LIVE) {
                doVisualizer(v -> {
                    addState(sourceState, v);
                    setRange(v);
                });
            } else {
                addState(sourceState, null);
            }
        } else if (transition instanceof HistoryTransition) {
            // Skip history transition.
        } else {
            // Unknown transition.
            throw new RuntimeException("Unknown transition: " + transition);
        }
    }

    @Override
    public void intermediateTrajectoryState(RuntimeState state) {
        // Add intermediate frame state.
        Assert.check(realTime);
        if (mode == LIVE) {
            doVisualizer(v -> {
                addState(state, v);
                setRange(v);
            });
        } else {
            addState(state, null);
        }
    }

    @Override
    public void transitionTaken(RuntimeState sourceState, Transition<?> transition, RuntimeState targetState,
            Boolean interrupted)
    {
        if (transition instanceof EventTransition) {
            // Skip event transition.
        } else if (transition instanceof TimeTransition) {
            // Define operation to perform.
            Consumer<PlotVisualizer> consumer = v -> transitionTakenTime(v, (TimeTransition<?>)transition, targetState);

            // Execute operation.
            if (mode == LIVE) {
                doVisualizer(consumer);
            } else {
                consumer.accept(null);
            }
        } else if (transition instanceof HistoryTransition) {
            // Remove states, rolling back to time of the target state of the
            // history transition.
            double time = transition.getTargetState(null, null).getTime();

            if (mode == LIVE) {
                doVisualizer(v -> {
                    removeStates(time, v);
                    setRange(v);
                });
            } else {
                removeStates(time, null);
            }
        } else {
            // Unknown transition.
            throw new RuntimeException("Unknown transition: " + transition);
        }
    }

    /**
     * Handler for the event that indicates that a time transition was taken, and the state of the simulator has been
     * updated.
     *
     * <p>
     * Must only be invoked by {@link #transitionTaken}.
     * </p>
     *
     * @param v The plot visualizer to use for live mode, or {@code null} for postponed mode.
     * @param timeTrans The time transition that was taken.
     * @param targetState The target state of the transition.
     */
    private void transitionTakenTime(PlotVisualizer v, TimeTransition<?> timeTrans, RuntimeState targetState) {
        // If real-time simulation is disabled, add intermediate trajectory
        // states (all but the first and last one). The time of the target
        // state is taken into account, as it is possible to choose a delay
        // that is shorter than the maximum allowed delay, and thus shorter
        // than the trajectory.
        if (!realTime) {
            double targetTime = targetState.getTime();
            int count = timeTrans.getTrajectories().getCount();
            for (int i = 1; i < count - 1; i++) {
                RuntimeState state = timeTrans.getTargetStateForIndex(i);
                if (state.getTime() >= targetTime) {
                    break;
                }
                addState(state, v);
            }
        }

        // Add target state of time transition.
        addState(targetState, v);

        // Update range, for live mode.
        if (v != null) {
            setRange(v);
        }
    }

    @Override
    public void simulationEnded(SimulationResult rslt, RuntimeState state) {
        if (mode == LIVE) {
            doVisualizer(v -> { setRange(v); });
        } else {
            showVisualizer();
            doVisualizer(v -> {
                v.initVarDatas(data.metas);
                moveDataToVisualizer(v);
                setRange(v);
            });
        }
    }

    /** Shows the visualizer. */
    private void showVisualizer() {
        if (testMode) {
            return;
        }
        String path = CifSpecOption.getCifSpecPath();
        visualizer = ControlEditor.show(path, PlotVisualizer.class, "show the plot visualizer");
    }

    /**
     * Perform operations on the visualizer.
     *
     * @param consumer The operations to perform, given the plot visualizer to perform them on.
     */
    private void doVisualizer(Consumer<PlotVisualizer> consumer) {
        // Check whether to skip.
        if (testMode) {
            return;
        }
        PlotVisualizer viz = visualizer;
        if (viz == null || !viz.isAvailable()) {
            return;
        }

        // Perform operations.
        try {
            consumer.accept(viz);
        } finally {
            viz.postUpdate();
        }
    }

    /**
     * Updates the plot visualizer by adding data for a given state.
     *
     * @param state The state for which to add data.
     * @param viz The plot visualizer to use for live mode, or {@code null} for postponed mode.
     * @see PlotVisualizerData#add
     */
    private void addState(final RuntimeState state, PlotVisualizer viz) {
        if (mode == LIVE) {
            // For live mode, add variable data directly to the visualizer.
            data.add(state, data.metas, viz.varDatas);
        } else {
            // For postponed mode, add variable data to the collected plot
            // visualizer data, which is stored for later use.
            data.add(state, data.metas, data.varDatas);
        }
    }

    /**
     * Updates the plot visualizer by removing all data after a given time, for an undo or reset transition.
     *
     * <p>
     * Note that we can only undo an entire transition. As such, if we reset, we end up at time zero, and need to remove
     * all data points. If we undo a non-time transition, there is nothing to remove, as nothing was added. For time
     * transitions, the start time is always the same as the end time of the previous time transition. As such, if the
     * last transition that is undone is a time transition, the target state has a 'time' value that is both the end of
     * a previous time transition and the start of the time transition that we just undid. As such, keep one such time
     * value, and remove the second occurrence and everything that follows after it.
     * </p>
     *
     * @param time The time.
     * @param viz The plot visualizer to use for live mode, or {@code null} for postponed mode.
     * @see PlotVisualizerData#remove
     */
    private void removeStates(double time, PlotVisualizer viz) {
        if (mode == LIVE) {
            // For live mode, remove variable data directly from the visualizer.
            data.remove(time, viz.varDatas);
        } else {
            // For postponed mode, remove variable data from the collected plot
            // visualizer data, which is stored for later use.
            data.remove(time, data.varDatas);
        }
    }

    /**
     * Moves the collected plot visualizer data (data points) to the actual visualizer. Used for postponed mode only.
     *
     * @param v The plot visualizer to use.
     */
    private void moveDataToVisualizer(PlotVisualizer v) {
        Assert.check(mode == PlotVisualizationMode.POSTPONED);

        // Copy data series to visualizer.
        v.varDatas = data.varDatas;

        // Clear collected data to free memory.
        data.varDatas = null;
    }

    /**
     * Sets the range of the x-axis.
     *
     * @param v The plot visualizer to use.
     */
    private void setRange(PlotVisualizer v) {
        // If no variables, then no data, and no range to set.
        if (v.varDatas == null) {
            return;
        }
        if (v.varDatas.isEmpty()) {
            return;
        }

        // Get first and last time of all current data points.
        PlotVisualizerDataSeries series0 = first(v.varDatas);
        if (series0.points.isEmpty()) {
            return;
        }
        double t0 = series0.points.getFirst().x;
        double t1 = series0.points.getLast().x;

        // Apply range limitations configured through the plot visualizer range option.
        if (data.range != null) {
            // Show range only. Calculate range back from last time.
            double t = t1 - data.range;

            // Compensate for range starting before the start of the
            // simulation.
            if (t < 0) {
                t = 0;
            }

            // Set start of range. Compensate for less data being
            // available then fits within the range.
            t0 = Math.max(t0, t);
        }
        v.rangeX = new PlotVisualizerRange(t0, t1);
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
        opts.add(Options.getInstance(PlotVisualizationOption.class));
        opts.add(Options.getInstance(PlotVisualizationModeOption.class));
        opts.add(Options.getInstance(PlotVisualizationFiltersOption.class));
        opts.add(Options.getInstance(PlotVisualizationRangeOption.class));

        return new OptionCategory("Plot visualization", "Plot visualization options.", subCats, opts);
    }
}
