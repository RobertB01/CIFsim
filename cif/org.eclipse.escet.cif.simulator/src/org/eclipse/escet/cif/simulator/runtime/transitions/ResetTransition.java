//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

/**
 * Runtime reset transition representation.
 *
 * @param <S> The type of state objects to use.
 */
public class ResetTransition<S extends RuntimeState> extends HistoryTransition<S> {
    /**
     * Constructor for the {@link ResetTransition} class.
     *
     * @param source The source state of the transition.
     * @param initialState The initial state, to use as target state of the transition. Must have been obtained by a
     *     call to {@link CifSimulatorHistory#reset}.
     */
    public ResetTransition(S source, S initialState) {
        super(source, initialState);
    }

    @Override
    public String toString() {
        return "reset to initial state";
    }
}
