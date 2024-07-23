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

package org.eclipse.escet.cif.controllercheck.checks.nonblockingundercontrol;

import org.eclipse.escet.cif.controllercheck.checks.CheckConclusion;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

/** Conclusion of the non-blocking under control check. */
public class NonBlockingUnderControlCheckConclusion implements CheckConclusion {
    /** Whether the specification is non-blocking under control. */
    public final boolean isNonBlockingUnderControl;

    /**
     * Constructor for the {@link NonBlockingUnderControlCheckConclusion} class.
     *
     * @param isNonBlockingUnderControl Whether the specification is non-blocking under control.
     */
    public NonBlockingUnderControlCheckConclusion(boolean isNonBlockingUnderControl) {
        this.isNonBlockingUnderControl = isNonBlockingUnderControl;
    }

    @Override
    public boolean propertyHolds() {
        return isNonBlockingUnderControl;
    }

    @Override
    public boolean hasDetails() {
        return false;
    }

    @Override
    public void printResult(DebugNormalOutput out, WarnOutput warn) {
        if (isNonBlockingUnderControl) {
            out.line("[OK] The specification is non-blocking under control.");
        } else {
            out.line("[ERROR] The specification is NOT non-blocking under control.");
        }
    }
}
