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

package org.eclipse.escet.cif.simulator.runtime.transitions;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeSpec;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.ode.Trajectories;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;

/**
 * Runtime time transition representation.
 *
 * @param <S> The type of state objects to use.
 */
public class TimeTransition<S extends RuntimeState> extends Transition<S> {
    /** The specification. */
    public final RuntimeSpec<S> spec;

    /** The trajectories of the continuous variables. */
    private final Trajectories trajectories;

    /**
     * Constructor for the {@link TimeTransition} class.
     *
     * @param spec The specification.
     * @param source The source state of the transition.
     * @param trajectories The trajectories of the continuous variables.
     */
    public TimeTransition(RuntimeSpec<S> spec, S source, Trajectories trajectories) {
        super(source);
        this.spec = spec;
        this.trajectories = trajectories;
    }

    /**
     * Returns the last value of variable 'time' for which values exist in the trajectories of this time transition.
     *
     * @return The last value of variable 'time' for which values exist in the trajectories of this time transition.
     */
    public double getLastTime() {
        double endTime = trajectories.getLastTime();
        Assert.check(endTime != -1);
        return endTime;
    }

    @Override
    public S getTargetState(Double targetTime, Boolean strict) {
        // Get closest time point, and the values of the continuous variables
        // at that time.
        Pair<Double, double[]> rslt;
        rslt = trajectories.getValuesForTime(targetTime, strict, spec.maxTimePointTol);

        // If no exact/close match, and non-strict, we have no answer.
        if (rslt == null) {
            return null;
        }

        // Exact/close match found. Update target time to that time point, and
        // get the values of the continuous variables at that (updated) time.
        targetTime = rslt.left;
        double[] values = rslt.right;

        // Construct new state.
        return spec.getOdeSolver().makeState(source, targetTime, values, true);
    }

    /**
     * Returns the state of the time transition, for the time point with the given index.
     *
     * @param idx The 0-based index of the time point for which to return the values.
     * @return The state of the transition, for the time point with the given index.
     */
    public S getTargetStateForIndex(int idx) {
        double time = trajectories.getTimes().get(idx);
        double[] values = trajectories.getValuesForIndex(idx);
        return spec.getOdeSolver().makeState(source, time, values, true);
    }

    /**
     * Returns the trajectories. Avoid using this method if possible, and use for instance {@link #getTargetState}
     * instead.
     *
     * @return The trajectories.
     */
    public Trajectories getTrajectories() {
        return trajectories;
    }

    /**
     * Takes the time transition by dealing with the intermediate frames, and the possible interruption of the time
     * transition.
     *
     * @param chosenTargetTime The chosen target time of the time transition to take.
     * @return The actual target time of the time transition.
     */
    public ActualTargetTime takeTimeTransition(ChosenTargetTime chosenTargetTime) {
        return spec.takeTimeTransition(this, chosenTargetTime);
    }

    @Override
    public String toString() {
        double maxDelay = trajectories.getMaxDelay();
        return fmt("delay for (0 .. %s]", CifSimulatorMath.realToStr(maxDelay));
    }
}
