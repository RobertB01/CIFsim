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

package org.eclipse.escet.cif.simulator.runtime.transitions;

import org.eclipse.escet.cif.simulator.CifSimulatorHistory;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.java.Assert;

/**
 * Base class for runtime history transitions, that go back to previous states.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class HistoryTransition<S extends RuntimeState> extends Transition<S> {
    /**
     * The target state of the transition. Must have been obtained by a call to {@link CifSimulatorHistory#reset} or
     * {@link CifSimulatorHistory#undo}.
     */
    private final S target;

    /**
     * Constructor for the {@link HistoryTransition} class.
     *
     * @param source The source state of the transition.
     * @param target The target state of the transition. Must have been obtained by a call to
     *     {@link CifSimulatorHistory#reset} or {@link CifSimulatorHistory#undo}.
     */
    public HistoryTransition(S source, S target) {
        super(source);
        this.target = target;
        Assert.check(source != target);
    }

    @Override
    public S getTargetState(Double targetTime, Boolean strict) {
        return target;
    }
}
