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

package org.eclipse.escet.cif.simulator.runtime.model;

/**
 * Runtime edge representation, for send event use.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class RuntimeSendEdge<S extends RuntimeState> extends RuntimeEdge<S> {
    /**
     * Evaluates the send value of the edge.
     *
     * @param state The state for which to evaluate the guards.
     * @return The send value, or {@code null} for 'void' channels.
     */
    public abstract Object evalSendValue(S state);

    /**
     * Applies the updates of the edge to the target location.
     *
     * <p>
     * Note that the provided {@code target} state is a shallow copy of the {@code source} state. That is, the
     * sub-states are the exact same objects (instances) as for the source state. Implementations should copy the
     * sub-state they intent to modify (if any), replace that sub-state in the target state with the copy, and apply the
     * updates in-place in the copied sub-state.
     * </p>
     *
     * <p>
     * For instance, put something like this at the beginning of the implementation, for a sub-state (automaton) named
     * {@code X}:
     *
     * <pre>target.s_X = source.s_X.copy();</pre>
     *
     * Subsequently apply the updates to {@code target.s_X}.
     * </p>
     *
     * @param source The source state to use for evaluations.
     * @param target The target state to update.
     */
    public abstract void update(S source, S target);
}
