//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.tests.common.checkers;

import org.eclipse.escet.cif.common.checkers.checks.CompNoInitPredsCheck;

/** {@link CompNoInitPredsCheck} class where only presence of initialization predicates is checked. */
public class CompNoInitPredsCheckNonTrivialInitPreds extends CompNoInitPredsCheck {
    /** Constructor of the {@link CompNoInitPredsCheckNonTrivialInitPreds} class. */
    public CompNoInitPredsCheckNonTrivialInitPreds() {
        super(true);
    }
}
