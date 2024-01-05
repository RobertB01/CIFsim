//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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
 * CIF check with an SVG file reference and SVG output mapping. Tests that the checker framework correctly resolves the
 * file while re-parsing and re-type-checking the specification.
 */
public class SvgFileReferenceTestCheck extends ExprNoSpecificExprsCheck {
    /** Constructor for the {@link SvgFileReferenceTestCheck} class. */
    public SvgFileReferenceTestCheck() {
        super(NoSpecificExpr.BOOL_LITS);
    }
}
