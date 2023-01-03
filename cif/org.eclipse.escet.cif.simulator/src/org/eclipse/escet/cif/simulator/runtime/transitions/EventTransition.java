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

package org.eclipse.escet.cif.simulator.runtime.transitions;

import org.eclipse.escet.cif.simulator.runtime.model.RuntimeEvent;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;

/**
 * Runtime event transition representation.
 *
 * @param <S> The type of state objects to use.
 */
public class EventTransition<S extends RuntimeState> extends Transition<S> {
    /** The event of the transition. */
    public final RuntimeEvent<S> event;

    /** The target state of the transition. */
    private final S target;

    /**
     * Constructor for the {@Link EventTransition} class.
     *
     * @param source The source state of the transition.
     * @param event The event of the transition.
     * @param target The target state of the transition.
     */
    public EventTransition(S source, RuntimeEvent<S> event, S target) {
        super(source);
        this.event = event;
        this.target = target;
    }

    @Override
    public S getTargetState(Double targetTime, Boolean strict) {
        return target;
    }

    @Override
    public String toString() {
        return "event " + event.name;
    }
}
