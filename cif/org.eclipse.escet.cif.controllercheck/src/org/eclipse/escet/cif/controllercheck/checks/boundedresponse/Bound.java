//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.checks.boundedresponse;

import org.eclipse.escet.common.java.Assert;

/** A bound on the number of transitions that can be executed, for certain events. */
public class Bound {
    /** Whether the controller has an initial state. */
    private final boolean hasInitialState;

    /** Whether the number of transitions that can be executed is bounded. */
    private final boolean bounded;

    /** The bound on the number of transitions, or {@code null} if not {@link #bounded}. */
    private final Integer bound;

    /**
     * Constructor for the {@link Bound} class.
     *
     * @param hasInitialState Whether the controller has an initial state.
     * @param bounded Whether the number of transitions that can be executed is bounded.
     * @param bound The bound on the number of transitions, or {@code null} if not {@link #bounded}.
     */
    public Bound(boolean hasInitialState, boolean bounded, Integer bound) {
        this.hasInitialState = hasInitialState;
        this.bounded = bounded;
        this.bound = bound;
        Assert.areEqual(bounded, bound != null);
    }

    /**
     * Returns whether the controller has an initial state.
     *
     * @return {@code true} if the controller has at least one initial state, {@code false} if it has no initial state.
     */
    public boolean hasInitialState() {
        return hasInitialState;
    }

    /**
     * Returns whether the number of transitions that can be executed is bounded.
     *
     * @return {@code true} if is bounded, {@code false} if it is not bounded.
     */
    public boolean isBounded() {
        return bounded;
    }

    /**
     * Returns the bound on the number of transitions.
     *
     * @return The bound.
     * @throws IllegalArgumentException If the number of transitions is not {@link #bounded}.
     */
    public int getBound() {
        if (!bounded) {
            throw new IllegalArgumentException("Not bounded");
        }
        return bound;
    }
}
