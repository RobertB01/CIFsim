//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.checkers.checks.TypeListSizeLimitsCheck;

/** Check that limits array list type sizes of variables between 5 and 10. */
public class TypeListSizeLimitsCheckArray5to10 extends TypeListSizeLimitsCheck {
    /** Constructor of the {@link TypeListSizeLimitsCheckArray5to10} class. */
    public TypeListSizeLimitsCheckArray5to10() {
        super(5, 10, TypeListSizeLimitsCheck.UNLIMITED, TypeListSizeLimitsCheck.UNLIMITED);
    }
}
