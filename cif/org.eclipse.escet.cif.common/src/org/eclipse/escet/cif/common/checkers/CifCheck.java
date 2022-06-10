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

import java.util.Set;

import org.eclipse.escet.cif.metamodel.java.CifWalker;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * CIF check. Checks whether a given CIF specification satisfies a certain condition.
 *
 * @implSpec Override only the relevant {@code preprocess*} and {@code postprocess*} methods. The {@code *crawl*} and
 *     {@code walk*} methods should not be overridden, as they are ignored by {@link CifChecker}.
 */
public abstract class CifCheck extends CifWalker {
    /** The violations collected so far. */
    private Set<CifCheckViolation> violations;

    /**
     * Sets the set in which to collect violations.
     *
     * @param violations The set.
     */
    public void setViolations(Set<CifCheckViolation> violations) {
        this.violations = violations;
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
    protected void addViolation(PositionObject cifObject, String message) {
        violations.add(new CifCheckViolation(cifObject, message));
    }
}
