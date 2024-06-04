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

import org.eclipse.escet.cif.checkers.checks.ExprNoSpecificUnaryExprsCheck;

/** {@link ExprNoSpecificUnaryExprsCheck} with boolean inverse disallowed while ignoring annotations. */
public class ExprNoSpecificUnaryExprsCheckIgnoreAnnos extends ExprNoSpecificUnaryExprsCheck {
    /** Constructor for the {@link ExprNoSpecificUnaryExprsCheckIgnoreAnnos} class. */
    public ExprNoSpecificUnaryExprsCheckIgnoreAnnos() {
        super(NoSpecificUnaryOp.INVERSE);
        ignoreAnnotations();
    }
}