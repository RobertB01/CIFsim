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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.CifSimulatorHistory;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;

/**
 * Runtime undo transition representation.
 *
 * @param <S> The type of state objects to use.
 */
public class UndoTransition<S extends RuntimeState> extends HistoryTransition<S> {
    /** The number of transitions that have been undone. */
    public final int count;

    /**
     * Constructor for the {@link UndoTransition} class.
     *
     * @param source The source state of the transition.
     * @param target The target state of the transition. Must have been obtained by a call to
     *     {@link CifSimulatorHistory#undo}.
     * @param count The number of transitions that have been undone.
     */
    public UndoTransition(S source, S target, int count) {
        super(source, target);
        this.count = count;
    }

    @Override
    public String toString() {
        return fmt("undo %,d transition%s", count, (count == 1) ? "" : "s");
    }
}
