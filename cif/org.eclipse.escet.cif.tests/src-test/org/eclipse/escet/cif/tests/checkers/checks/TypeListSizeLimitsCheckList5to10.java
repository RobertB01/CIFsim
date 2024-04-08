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

/** Check that limits non-array list type sizes of declarations between 5 and 10. */
public class TypeListSizeLimitsCheckList5to10 extends TypeListSizeLimitsCheck {
    /** Constructor of the {@link TypeListSizeLimitsCheckList5to10} class. */
    public TypeListSizeLimitsCheckList5to10() {
        super(TypeListSizeLimitsCheck.UNLIMITED, TypeListSizeLimitsCheck.UNLIMITED, 5, 10);
    }
}
