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

package org.eclipse.escet.cif.simulator.runtime.meta;

/** State object type (the kind of state object, not its data type). */
public enum StateObjectType {
    /** Automaton. */
    AUTOMATON,

    /** Variable 'time'. */
    TIME,

    /** Discrete variable. */
    DISCRETE,

    /** Input variable. */
    INPUT,

    /** Continuous variable. */
    CONTINUOUS,

    /** Derivative of a continuous variable. */
    DERIVATIVE,

    /** Algebraic variable. */
    ALGEBRAIC;
}
