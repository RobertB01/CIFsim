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

import java.util.EnumSet;

import org.eclipse.escet.cif.checkers.checks.PrintNoSpecificPrintDeclsCheck;

/** Test class that disallows all specific print declaration features. */
public class PrintNoSpecificPrintDeclsCheckAll extends PrintNoSpecificPrintDeclsCheck {
    /** Constructor of the {@link PrintNoSpecificPrintDeclsCheckAll} class. */
    public PrintNoSpecificPrintDeclsCheckAll() {
        super(EnumSet.allOf(NoSpecificPrintDecl.class));
    }
}
