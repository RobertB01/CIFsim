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

package org.eclipse.escet.cif.checkers.checks;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * CIF check that does not allow marker predicates in components, that is, does not allow marker predicates outside
 * locations.
 */
public class CompNoMarkerPredsCheck extends CifCheck {
    /** Whether to ignore marker predicates that trivially hold. */
    private boolean ignoreTriviallyTrueMarkerPreds;

    /**
     * Constructor of the {@link CompNoMarkerPredsCheck} class.
     *
     * <p>
     * Using this constructor will cause the check to also report marker predicates that trivially hold.
     * </p>
     */
    public CompNoMarkerPredsCheck() {
        this(false);
    }

    /**
     * Constructor of the {@link CompNoMarkerPredsCheck} class.
     *
     * @param ignoreTriviallyTrueMarkerPreds Whether to ignore marker predicates that trivially hold.
     */
    public CompNoMarkerPredsCheck(boolean ignoreTriviallyTrueMarkerPreds) {
        this.ignoreTriviallyTrueMarkerPreds = ignoreTriviallyTrueMarkerPreds;
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp, CifCheckViolations violations) {
        for (Expression marked: comp.getMarkeds()) {
            if (!ignoreTriviallyTrueMarkerPreds || !CifValueUtils.isTriviallyTrue(marked, false, true)) {
                violations.add(marked, "Component has a marker predicate");
            }
        }
    }
}
