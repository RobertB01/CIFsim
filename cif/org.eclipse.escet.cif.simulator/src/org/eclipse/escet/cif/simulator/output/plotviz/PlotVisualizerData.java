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

package org.eclipse.escet.cif.simulator.output.plotviz;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateFilterer;
import org.eclipse.escet.cif.simulator.runtime.meta.RuntimeStateObjectMeta;
import org.eclipse.escet.cif.simulator.runtime.meta.StateObjectType;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/** Holds the data for a {@link PlotVisualizer}. */
public class PlotVisualizerData {
    /**
     * The length of the plot visualization range, or {@code null} for infinite.
     *
     * @see PlotVisualizationRangeOption
     */
    public final Double range;

    /** The state objects filters text, or {@code null} if not available. */
    private String filtersTxt;

    /** The meta data of the filtered state objects. Is {@code null} until set by {@link #initMeta}. */
    public List<RuntimeStateObjectMeta> metas;

    /**
     * The data series for the variables. Is {@code null} for live plotting mode. Is also {@code null} for postponed
     * mode, until initialized by the {@link #initVarDatas} method.
     *
     * @see PlotVisualizer#varDatas
     */
    public List<PlotVisualizerDataSeries> varDatas = null;

    /**
     * Constructor for the {@link PlotVisualizerData} class.
     *
     * @param filtersTxt The state objects filters text.
     */
    public PlotVisualizerData(String filtersTxt) {
        range = PlotVisualizationRangeOption.getRange();
        this.filtersTxt = filtersTxt;
    }

    /**
     * Initializes the state object meta data for the given state.
     *
     * @param state The state for which to initialize the state object meta data.
     * @throws InvalidOptionException If one of the plot visualizer filters has invalid syntax.
     */
    public void initMeta(RuntimeState state) {
        // Get state object meta data, filtered based on object type and data
        // type.
        metas = list();
        for (RuntimeStateObjectMeta meta: state.spec.stateObjectsMeta) {
            // Filter out automata.
            if (meta.type == StateObjectType.AUTOMATON) {
                continue;
            }

            // Keep numeric and boolean types.
            Object value = state.getVarValue(meta);
            if (!isSupportedType(value)) {
                continue;
            }

            // Add state object meta data.
            metas.add(meta);
        }

        // Filter state objects based on their absolute names.
        metas = RuntimeStateFilterer.filter(metas, filtersTxt, "Plot visualizer", "shown in the plot visualizer");
        filtersTxt = null;
    }

    /**
     * Initializes the data series for the given variables. Only needed for postponed mode.
     *
     * @see PlotVisualizer#initVarDatas
     */
    public void initVarDatas() {
        varDatas = listc(metas.size());
        for (RuntimeStateObjectMeta meta: metas) {
            PlotVisualizerDataSeries seriesData = new PlotVisualizerDataSeries(meta);
            varDatas.add(seriesData);
        }
    }

    /**
     * Add data points for the variables of the given state.
     *
     * <p>
     * For live plotting mode, the 'targetDatas' is {@link PlotVisualizer#varDatas}. For postponed plotting, the
     * 'targetDatas' is {@link #varDatas}.
     * </p>
     *
     * @param state The state from which to obtain the values for the data points.
     * @param metas The meta data of the filtered state objects.
     * @param targetDatas The data series for the variables. Is modified in-place.
     * @see PlotVisualizerOutputComponent#addState
     */
    public void add(RuntimeState state, List<RuntimeStateObjectMeta> metas,
            List<PlotVisualizerDataSeries> targetDatas)
    {
        // If the range is not infinite, remove old data points.
        if (range != null) {
            // If no variables, then no data, and no range.
            if (targetDatas.isEmpty()) {
                return;
            }

            // Get time before which to remove all points.
            double curTime = state.getTime();
            double lastTime = curTime - range;
            if (lastTime < 0) {
                lastTime = 0;
            }

            // We want to remove all data points older than 'lastTime'. If we
            // have an exact match, the points before that exact match are all
            // removed. If we don't have an exact match, the points before the
            // largest time value less than 'lastTime' are removed, to ensure
            // that the range that we keep is at least as large as the
            // requested range.
            //
            // Get index of the first data point to keep. May be -1 to keep
            // all data points. We use a linear search, as it leads to simpler
            // code. Also, for linked lists, the performance is better that
            // way, as a binary search uses explicit indexing.
            Deque<PlotVisualizerDataPoint> points0 = first(targetDatas).points;
            int itemsToRemove = 0;
            for (PlotVisualizerDataPoint point: points0) {
                double timeValue = point.x;
                if (timeValue < lastTime) {
                    itemsToRemove++;
                } else if (timeValue == lastTime) {
                    break;
                } else {
                    itemsToRemove--;
                    if (itemsToRemove < 0) {
                        itemsToRemove = 0;
                    }
                    break;
                }
            }

            // Remove older items, from before the requested range.
            for (int mi = 0; mi < metas.size(); mi++) {
                Deque<PlotVisualizerDataPoint> points = targetDatas.get(mi).points;
                for (int ri = 0; ri < itemsToRemove; ri++) {
                    points.removeFirst();
                }
            }
        }

        // Add new data points.
        double t = state.getTime();
        for (int i = 0; i < metas.size(); i++) {
            Object value = state.getVarValue(metas.get(i));
            double number = valueToDouble(value);
            PlotVisualizerDataSeries series = targetDatas.get(i);
            series.points.add(new PlotVisualizerDataPoint(t, number));
        }
    }

    /**
     * Remove all data points after a given time, for an undo or reset transition.
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
     * <p>
     * For live plotting mode, the 'targetDatas' is {@link PlotVisualizer#varDatas}. For postponed plotting, the
     * 'targetDatas' is {@link #varDatas}.
     * </p>
     *
     * @param time The time.
     * @param targetDatas The data series for the variables. Is modified in-place.
     * @see PlotVisualizerOutputComponent#removeStates
     */
    public void remove(double time, List<PlotVisualizerDataSeries> targetDatas) {
        // If no variables, then no data, and nothing to remove.
        if (targetDatas.isEmpty()) {
            return;
        }

        // If no data, then nothing to remove.
        Deque<PlotVisualizerDataPoint> points0 = first(targetDatas).points;
        if (points0.isEmpty()) {
            return;
        }

        // Get 'time' value of last time point.
        double lastTime = points0.getLast().x;

        // Remove items.
        if (time == 0.0) {
            // Go back to initial state. Remove all data points.
            for (PlotVisualizerDataSeries series: targetDatas) {
                series.points.clear();
            }
        } else if (time == lastTime) {
            // We don't go back in time. Undo only non-time transitions. Keep all data points.
        } else {
            // Get number of items to remove. We use a reverse linear search, as it leads to simpler code.
            // Also, due to the use of linked lists, the performance is better that way, as a binary search uses
            // explicit indexing.
            int itemsToRemove = 0;
            Iterator<PlotVisualizerDataPoint> iter = points0.descendingIterator();
            while (iter.hasNext()) {
                double t = iter.next().x;
                if (t > time) {
                    // Time beyond the time to remove. Remove this item and continue to find more items.
                    itemsToRemove++;
                } else if (t == time) {
                    // Found the exact point. We assume this is the start of the time transition being removed. Thus
                    // we remove this item. We keep the previous item, which is the end of the previous time
                    // transition, and has the same 'time' value as the current item.
                    itemsToRemove++;
                    break;
                } else if (t < time) {
                    // We somehow missed the requested time. We expected it to exist, as it is both the end of a time
                    // transition, and the beginning of the next one.
                    throw new RuntimeException("Moved beyond requested time: " + time);
                }
            }

            // Remove items.
            for (PlotVisualizerDataSeries series: targetDatas) {
                Deque<PlotVisualizerDataPoint> points = series.points;
                for (int i = 0; i < itemsToRemove; i++) {
                    points.removeLast();
                }
            }
        }
    }

    /**
     * Is the given value of a supported type?
     *
     * @param value The value.
     * @return {@code true} if the value is of a supported type, {@code false} otherwise.
     */
    private static boolean isSupportedType(Object value) {
        return value instanceof Boolean || value instanceof Integer || value instanceof Double;
    }

    /**
     * Converts a given value to a double representation.
     *
     * @param value The value to convert.
     * @return A double representation of the value.
     */
    private static double valueToDouble(Object value) {
        if (value instanceof Boolean) {
            return ((Boolean)value) ? 1 : 0;
        } else if (value instanceof Integer) {
            return ((Integer)value).doubleValue();
        } else if (value instanceof Double) {
            return (double)value;
        }
        throw new RuntimeException("Unexpected value: " + value);
    }
}
