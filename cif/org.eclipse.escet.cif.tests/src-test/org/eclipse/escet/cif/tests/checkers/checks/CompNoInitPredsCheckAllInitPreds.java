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

import org.eclipse.escet.cif.common.checkers.checks.CompNoInitPredsCheck;

/** {@link CompNoInitPredsCheck} class where only presence of initialization predicates is checked. */
public class CompNoInitPredsCheckAllInitPreds extends CompNoInitPredsCheck {
    /** Constructor of the {@link CompNoInitPredsCheckAllInitPreds} class. */
    public CompNoInitPredsCheckAllInitPreds() {
        super(false);
    }
}
