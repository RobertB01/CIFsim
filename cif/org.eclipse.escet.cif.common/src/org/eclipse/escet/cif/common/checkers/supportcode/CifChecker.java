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

package org.eclipse.escet.cif.common.checkers.supportcode;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.java.CompositeCifWithArgWalker;

/** CIF checker. Checks whether a given CIF specification satisfies certain {@link CifCheck conditions}. */
public class CifChecker extends CompositeCifWithArgWalker<CifCheckViolations> {
    /**
     * Constructor for the {@link CifChecker} class.
     *
     * @param conditions The conditions to check.
     */
    public CifChecker(List<CifCheck> conditions) {
        super(conditions.toArray(i -> new CifCheck[i]));
    }

    /**
     * Constructor for the {@link CifChecker} class.
     *
     * @param conditions The conditions to check.
     */
    public CifChecker(CifCheck[] conditions) {
        super(conditions);
    }

    /**
     * Check whether a given CIF specification satisfies the given conditions.
     *
     * @param spec The CIF specification to check.
     * @return The violations.
     */
    public CifCheckViolations check(Specification spec) {
        CifCheckViolations violations = new CifCheckViolations();
        walkSpecification(spec, violations);
        return violations;
    }
}
