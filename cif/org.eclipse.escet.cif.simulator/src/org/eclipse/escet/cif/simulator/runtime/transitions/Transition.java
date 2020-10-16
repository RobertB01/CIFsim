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

package org.eclipse.escet.cif.simulator.runtime.transitions;

import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.cif.simulator.runtime.ode.Trajectories;

/**
 * Runtime transition representation.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class Transition<S extends RuntimeState> {
    /** The source state of the transition. */
    public final S source;

    /**
     * Constructor for the {@link Transition} class.
     *
     * @param source The source state of the transition.
     */
    public Transition(S source) {
        this.source = source;
    }

    /**
     * Returns the target state of the transition.
     *
     * <p>
     * If for time transitions, the requested time point (value of variable 'time') does not exist, the closest time
     * point is used. This time point must be 'very close' to the requested time.
     * </p>
     *
     * @param targetTime The requested time point (value of variable 'time') of the target state, or {@code null} for
     *     non-time transitions.
     * @param strict Whether to fail if no exact or 'very close' time point is present in the trajectories ({@code true}
     *     = strict), or return {@code null} in such cases ({@code false} = non-strict). Is {@code null} for non-time
     *     transitions.
     * @return The target state of the transition. For time transitions, the value of variable 'time' may not be exactly
     *     the requested time. Also for time transitions, {@code null} is returned if no exact or 'very close' time
     *     point is present in the trajectories, and a non-strict answer is requested.
     *
     * @see Trajectories#getValuesForTime
     */
    public abstract S getTargetState(Double targetTime, Boolean strict);

    @Override
    public abstract String toString();
}
