//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.tests.common.checkers;

import org.eclipse.escet.cif.cif2cif.ElimLocRefExprs;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.checks.ExprNoSpecificExprsCheck;
import org.eclipse.escet.cif.metamodel.cif.Specification;

/**
 * CIF check that eliminates location references prior to performing a check for disallowed enumeration literal
 * reference expressions. Elimination of location references introduce a location pointer variable without position
 * information, that has an enumeration literal as its initial value. The violation is reported on the variable, the
 * closest named ancestor of the expression, without position information being available.
 */
public class NoPositionInfoTestCheck extends ExprNoSpecificExprsCheck {
    /** Constructor for the {@link NoPositionInfoTestCheck} class. */
    public NoPositionInfoTestCheck() {
        super(NoSpecificExpr.ENUM_LIT_REFS);
    }

    @Override
    protected void preprocessSpecification(Specification spec, CifCheckViolations violations) {
        new ElimLocRefExprs().transform(spec);
        super.preprocessSpecification(spec, violations);
    }
}
