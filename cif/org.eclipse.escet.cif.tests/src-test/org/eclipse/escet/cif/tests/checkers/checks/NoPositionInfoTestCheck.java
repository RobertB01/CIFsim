//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.tests.checkers.checks;

import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificExprsCheck;

/**
 * CIF check that eliminates location references prior to performing a check for disallowed enumeration literal
 * reference expressions. Elimination of location references introduces a location pointer variable that has an
 * enumeration literal as its initial value, without position information. Due to re-parsing, position information is
 * available for the enumeration literals.
 */
public class NoPositionInfoTestCheck extends ExprNoSpecificExprsCheck {
    /** Constructor for the {@link NoPositionInfoTestCheck} class. */
    public NoPositionInfoTestCheck() {
        super(NoSpecificExpr.ENUM_LIT_REFS);
    }
}
