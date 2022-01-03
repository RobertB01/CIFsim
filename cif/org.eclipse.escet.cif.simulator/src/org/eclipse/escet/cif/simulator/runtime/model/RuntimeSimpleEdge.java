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

package org.eclipse.escet.cif.simulator.runtime.model;

import org.eclipse.escet.cif.simulator.compiler.AutomatonCodeGenerator;

/**
 * Runtime edge representation, for {@link AutomatonCodeGenerator#isSimpleAut simple} automata.
 *
 * @param <S> The type of state objects to use.
 */
public class RuntimeSimpleEdge<S extends RuntimeState> extends RuntimeSyncEdge<S> {
    /** The automaton that contains this edge. */
    private final RuntimeSimpleAutomaton<S> aut;

    /** The target location index. This is the new value of the location pointer variable, if the edge is taken. */
    private final int targetLocIdx;

    /**
     * Constructor for the {@link RuntimeSimpleEdge} class.
     *
     * @param aut The automaton that contains this edge.
     * @param targetLocIdx The target location index. This is the new value of the location pointer variable, if the
     *     edge is taken.
     */
    public RuntimeSimpleEdge(RuntimeSimpleAutomaton<S> aut, int targetLocIdx) {
        this.aut = aut;
        this.targetLocIdx = targetLocIdx;
    }

    @Override
    public boolean evalGuards(S state) {
        String msg = "Must not eval simple edge guards.";
        throw new UnsupportedOperationException(msg);
    }

    @Override
    public void update(S source, S target) {
        aut.updateLocPointerValue(target, targetLocIdx);
    }
}
