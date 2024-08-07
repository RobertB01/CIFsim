//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator;

import org.eclipse.escet.cif.checkers.CifPreconditionChecker;
import org.eclipse.escet.cif.checkers.checks.InvNoTimeDependentCheck;
import org.eclipse.escet.cif.checkers.checks.SvgInNoUpdatesCheck;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.common.java.Termination;

/** CIF simulator precondition checker. */
public class CifSimulatorPreChecker extends CifPreconditionChecker {
    /**
     * Constructor for the {@link CifSimulatorPreChecker} class.
     *
     * @param termination Cooperative termination query function.
     */
    public CifSimulatorPreChecker(Termination termination) {
        super(termination,

                // Time-dependent state invariants are not supported.
                new InvNoTimeDependentCheck().setInvKinds(InvKind.STATE),

                // No SVG input mappings with updates.
                new SvgInNoUpdatesCheck()

        );
    }
}
