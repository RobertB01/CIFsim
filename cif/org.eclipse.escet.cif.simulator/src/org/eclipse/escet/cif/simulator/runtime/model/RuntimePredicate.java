//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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
 * Interface for runtime predicates.
 *
 * @param <S> The type of state objects to use.
 */
public interface RuntimePredicate<S extends RuntimeState> {
    /**
     * Evaluates the predicate.
     *
     * @param state The state for which to evaluate the predicate.
     * @return {@code true} if the predicate evaluated to {@code true}, {@code false} otherwise.
     */
    public boolean eval(S state);
}
