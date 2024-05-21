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

/** {@link TypeIntBoundsCheck} with allowing only range [5..5] while ignoring annotations. */
public class TypeIntBoundsCheckIgnoreAnnos extends TypeIntBoundsCheck {
    /** Constructor of the {@link TypeIntBoundsCheckIgnoreAnnos} class. */
    public TypeIntBoundsCheckIgnoreAnnos() {
        super(true, 5, 5, 5, 5);
        ignoreAnnotations();
    }
}
