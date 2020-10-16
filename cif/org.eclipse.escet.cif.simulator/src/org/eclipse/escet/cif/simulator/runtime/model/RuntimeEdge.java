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
 * Runtime edge representation base class, for all event use.
 *
 * @param <S> The type of state objects to use.
 */
public abstract class RuntimeEdge<S extends RuntimeState> {
    /**
     * Evaluates the guards of the edge.
     *
     * @param state The state for which to evaluate the guards.
     * @return {@code true} if all guards evaluate to {@code true}, {@code false} otherwise.
     */
    public abstract boolean evalGuards(S state);
}
