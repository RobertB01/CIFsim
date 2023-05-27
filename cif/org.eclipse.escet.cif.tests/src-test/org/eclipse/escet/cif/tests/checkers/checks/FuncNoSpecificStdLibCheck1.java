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

package org.eclipse.escet.cif.tests.checkers.checks;

import org.eclipse.escet.cif.checkers.checks.FuncNoSpecificStdLibCheck;

/** Test class that disallows the standard library 'abs' function and all standard library trigonometry functions. */
public class FuncNoSpecificStdLibCheck1 extends FuncNoSpecificStdLibCheck {
    /** Constructor of the {@link FuncNoSpecificStdLibCheck1} class. */
    public FuncNoSpecificStdLibCheck1() {
        super(NoSpecificStdLib.STD_LIB_ABS, NoSpecificStdLib.STD_LIB_TRIGONOMETRY_GROUP);
    }
}
