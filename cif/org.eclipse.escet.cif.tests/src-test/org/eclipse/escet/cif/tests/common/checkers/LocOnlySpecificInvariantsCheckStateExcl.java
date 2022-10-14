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

import org.eclipse.escet.cif.common.checkers.checks.LocOnlySpecificInvariantsCheck;

/** {@link LocOnlySpecificInvariantsCheck} with allowing state/event exclusion invariants in locations enabled. */
public class LocOnlySpecificInvariantsCheckStateExcl extends LocOnlySpecificInvariantsCheck {
    /** Constructor of the {@link LocOnlySpecificInvariantsCheckStateExcl} class. */
    public LocOnlySpecificInvariantsCheckStateExcl() {
        super(false, true);
    }
}
