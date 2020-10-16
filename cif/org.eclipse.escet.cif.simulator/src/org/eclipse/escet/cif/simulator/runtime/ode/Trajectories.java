//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.runtime.ode;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;

/**
 * Trajectories of continuous variables. Trajectories should not contain {@code NaN}, {@code +inf}, and {@code -inf}.
 */
public class Trajectories {
    /**
     * The time points of the trajectory. The time points are in ascending order, and no two time points may have the
     * same time value. Modified in-place by the {@link #add} method.
     */
    private final List<Double> times = list();

    /**
     * The values of the continuous variables, for each of the {@link #times time points}. Modified in-place by the
     * {@link #add} method.
     */
    private final List<double[]> values = list();

    /**
     * Returns the time points of the trajectory. The time points are in ascending order, and no two time points may
     * have the same time value. Avoid using this method if possible, since it exposes internal details.
     *
     * @return The time points of the trajectory.
     */
    public List<Double> getTimes() {
        return times;
    }

    /**
     * Adds a time point to the trajectories. The time point must be strictly after any previously added time points.
     *
     * @param time The time of the time point. Must not be {@code NaN}, {@code +inf}, or {@code -inf}.
     * @param y The values of the continuous variables at the time point. They must not be {@code NaN}, {@code +inf}, or
     *     {@code -inf}.
     */
    public void add(double time, double[] y) {
        // Make sure time is never negative.
        Assert.check(time >= 0);

        // Make sure we add a time point that is strictly after any previous
        // time points.
        if (getCount() > 0) {
            double tLast = last(times);
            Assert.check(time > tLast);
        }

        // Copy 'y' array, as it is modified in-place by the ODE solver.
        double[] y2 = new double[y.length];
        System.arraycopy(y, 0, y2, 0, y.length);

        // Add time point.
        times.add(time);
        values.add(y2);
    }

    /**
     * Returns the number of time points present in the trajectories.
     *
     * @return The number of time points present in the trajectories.
     */
    public int getCount() {
        return times.size();
    }

    /**
     * Returns the last value of variable 'time' for which values were added to the trajectories. If no time points have
     * been added thus far, {@code -1} is returned.
     *
     * @return The last value of variable 'time' for which values were added to the trajectories, or {@code -1}.
     */
    public double getLastTime() {
        if (times.isEmpty()) {
            return -1;
        }
        return last(times);
    }

    /**
     * Returns the maximum delay for these trajectories. If {@code x} is returned, the maximum delay interval is
     * {@code (0..x]}. If possible, avoid calculations with the returned value, to avoid issues with the precision of
     * binary floating point representations.
     *
     * @return The maximum delay for these trajectories.
     */
    public double getMaxDelay() {
        // Make sure the interval is non-empty.
        Assert.check(getCount() >= 2);

        // Return the length of the interval.
        double t0 = first(times);
        double tLast = last(times);
        return tLast - t0;
    }

    /**
     * Returns the values of the continuous variables, for the time point with the given index. Avoid using this method
     * if possible, since it exposes internal details.
     *
     * @param idx The 0-based index of the time point for which to return the values.
     * @return The values of the continuous variables.
     */
    public double[] getValuesForIndex(int idx) {
        return values.get(idx);
    }

    /**
     * Returns the values of the continuous variables, at the requested time point (value of variable 'time'). That is,
     * the trajectories are 'evaluated' for a single time point. If the requested time point (value of variable 'time')
     * does not exist, the closest time point is used. This time point must be 'very close' to the requested time.
     *
     * @param reqTime The requested time point (value of variable 'time').
     * @param strict Whether to fail if no exact or 'very close' time point is present in the trajectories ({@code true}
     *     = strict), or return {@code null} in such cases ({@code false} = non-strict).
     * @param tolerance The tolerance to use, i.e. the maximum difference between the requested time point, and the time
     *     point in the trajectories that is closest to it. The tolerance in ulps is relative to the requested time.
     * @return The actual time for which the values of the continuous variables are returned (may be the requested time,
     *     or a time 'very close' to it), and the values of the continuous variables, at that time. {@code null} is
     *     returned instead, if no exact or 'very close' time point is present, and a non-strict answer is requested.
     */
    @SuppressWarnings("null")
    public Pair<Double, double[]> getValuesForTime(double reqTime, boolean strict, int tolerance) {
        // Get the time domain interval of the trajectory.
        Assert.check(getCount() >= 2);
        double firstTime = first(times);
        double lastTime = last(times);

        // If the requested time equals the end time, return the end values.
        // This special case is added for performance reasons only.
        if (reqTime == lastTime) {
            return pair(reqTime, last(values));
        }

        // The requested time may have been obtained by adding and subtracting
        // time and delay values. If the delay amount is exactly the maximum
        // delay time, or is very close to it, then due to rounding in the
        // binary double precision floating point representation, the resulting
        // value may be just after the last time point. This is compensated
        // by using the closest time point, and does not just apply to the
        // maximum delay, but to all time points. For the lower bound however,
        // we don't compensate. That is, we don't support time points before
        // the start of the time transition.
        Assert.check(firstTime <= reqTime);

        // Find index of the the first time point greater than or equal to the
        // requested time value.
        int idx = Collections.binarySearch(times, reqTime);
        if (idx < 0) {
            idx = -(idx + 1);
        }

        // If we found the exact time, return the corresponding values.
        if (idx < times.size() && times.get(idx) == reqTime) {
            return pair(reqTime, values.get(idx));
        }

        // No exact match found. Get closest time points, and compute deltas.
        double beforeTime = times.get(idx - 1);
        Double afterTime = (idx == times.size()) ? null : times.get(idx);
        double beforeDelta = reqTime - beforeTime;
        Double afterDelta = (afterTime == null) ? null : afterTime - reqTime;

        // Get closest time point, its index, and its delta.
        double closestTime;
        double closestDelta;
        int closestIdx;

        if (afterDelta == null) { // No after time, use before time.
            closestTime = beforeTime;
            closestDelta = beforeDelta;
            closestIdx = idx - 1;
        } else if (beforeDelta < afterDelta) { // Before closer than after.
            closestTime = beforeTime;
            closestDelta = beforeDelta;
            closestIdx = idx - 1;
        } else { // After closer than before (or as close as before).
            closestTime = afterTime;
            closestDelta = afterDelta;
            closestIdx = idx;
        }

        // Check delta, as we need to be 'very close'.
        double reqTimeUlp = Math.ulp(reqTime);
        double deltaUlps = closestDelta / reqTimeUlp;
        if (deltaUlps > tolerance) {
            // Non-strict request: no answer.
            if (!strict) {
                return null;
            }

            // Strict request: failure.
            String msg = fmt("Requested time point \"%s\" is \"%s\" ulps (1 ulp = %s) away from the closest time "
                    + "point in the trajectories \"%s\", which is farther away than the maximum tolerance of \"%d\" "
                    + "ulps. You can try increasing the value of the maximum time point tolerance option. Please "
                    + "also contact the development team.", CifSimulatorMath.realToStr(reqTime),
                    CifSimulatorMath.realToStr(deltaUlps), CifSimulatorMath.realToStr(reqTimeUlp),
                    CifSimulatorMath.realToStr(closestTime), tolerance);
            throw new CifSimulatorException(msg);
        }

        // Return values of the continuous variables at the closest time point.
        return pair(closestTime, values.get(closestIdx));
    }
}
