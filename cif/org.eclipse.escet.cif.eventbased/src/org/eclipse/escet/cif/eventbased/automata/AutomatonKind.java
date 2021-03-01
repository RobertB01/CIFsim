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

package org.eclipse.escet.cif.eventbased.automata;

/** Kinds of automata. */
public enum AutomatonKind {
    /** Plant automaton. */
    PLANT,

    /** Requirement automaton. */
    REQUIREMENT,

    /** Supervisor automaton. */
    SUPERVISOR,

    /** Unknown kind of automaton. */
    UNKNOWN;
}
