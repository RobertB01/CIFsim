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

import org.eclipse.escet.cif.checkers.checks.TypeIntBoundsCheck;

/** {@link TypeIntBoundsCheck} that checks all integer types. */
public class TypeIntBoundsCheckAllIntTypes extends TypeIntBoundsCheck {
    /** Constructor of the {@link TypeIntBoundsCheckAllIntTypes} class. */
    public TypeIntBoundsCheckAllIntTypes() {
        super(true, 0, 10, 0, 10);
    }
}
