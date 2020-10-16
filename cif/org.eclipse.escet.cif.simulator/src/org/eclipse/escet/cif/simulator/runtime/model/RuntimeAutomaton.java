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

package org.eclipse.escet.cif.simulator.runtime.model;

/**
 * Runtime automaton representation.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class RuntimeAutomaton<S extends RuntimeState> {
    /**
     * Returns the absolute name of the automaton.
     *
     * @return The absolute name of the automaton.
     */
    public abstract String getName();

    /**
     * Returns the number of locations of the automaton.
     *
     * @return The number of locations of the automaton.
     */
    public abstract int getLocCount();

    /**
     * Returns the name of the given location, or {@code "*"} if the automaton has only a single nameless location.
     *
     * @param idx The 0-based index of the location.
     * @return The name of the given location, or {@code "*"}.
     */
    public abstract String getLocName(int idx);

    /**
     * Returns the name of the current location of the automaton, in the given state, or {@code "*"} if the automaton
     * has only a single nameless location.
     *
     * @param state The state.
     * @return The name of the current location of the automaton, or {@code "*"}.
     */
    public abstract String getCurLocName(S state);
}
