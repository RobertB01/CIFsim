//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers;

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** CIF check condition violations. */
public class CifCheckViolations {
    /** The violations collected so far. */
    private final Set<CifCheckViolation> violations = set();

    /**
     * Returns whether any violations were collected so far.
     *
     * @return {@code true} if violations were collected, {@code false} otherwise.
     */
    public boolean hasViolations() {
        return !violations.isEmpty();
    }

    /**
     * Returns the violations collected so far.
     *
     * @return The violations.
     */
    public Stream<CifCheckViolation> getViolations() {
        return violations.stream();
    }

    /**
     * Add a violation.
     *
     * @param cifObject The named CIF object for which the violation is reported, or {@code null} to report it for the
     *     CIF specification.
     * @param message The message describing the violation. E.g., {@code "event is a channel"},
     *     {@code "automaton is a kindless automaton, lacking a supervisory kind"} or
     *     {@code "specification has no automata"}.
     */
    public void add(PositionObject cifObject, String message) {
        violations.add(new CifCheckViolation(cifObject, message));
    }
}
