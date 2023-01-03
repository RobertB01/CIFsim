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

import static org.eclipse.escet.cif.simulator.runtime.io.RuntimeValueToString.runtimeToString;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.model.RuntimeEvent;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;

/**
 * Channel communication transition.
 *
 * @param <S> The type of state objects to use.
 */
public class CommunicationTransition<S extends RuntimeState> extends EventTransition<S> {
    /** The communicated value, or {@code null} for 'void' channels. */
    public final Object value;

    /**
     * Constructor for the {@Link CommunicationTransition} class.
     *
     * @param source The source state of the transition.
     * @param event The event of the transition.
     * @param target The target state of the transition.
     * @param value The communicated value, or {@code null} for 'void' channels.
     */
    public CommunicationTransition(S source, RuntimeEvent<S> event, S target, Object value) {
        super(source, event, target);
        this.value = value;
    }

    @Override
    public String toString() {
        String rslt = super.toString();
        rslt += (value == null) ? fmt(" (communicates no value)")
                : fmt(" (communicates value %s)", runtimeToString(value));
        return rslt;
    }
}
