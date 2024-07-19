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

package org.eclipse.escet.cif.controllercheck.checks.boundedresponse;

import org.eclipse.escet.cif.controllercheck.checks.CheckConclusion;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

/** Conclusion of the bounded response check. */
public class BoundedResponseCheckConclusion implements CheckConclusion {
    /** The bound on the number of transitions that can be executed for uncontrollable events. */
    public final Bound uncontrollablesBound;

    /** The bound on the number of transitions that can be executed for controllable events. */
    public final Bound controllablesBound;

    /**
     * Constructor for the {@link BoundedResponseCheckConclusion} class.
     *
     * @param uncontrollablesBound The bound on the number of transitions that can be executed for uncontrollable
     *     events.
     * @param controllablesBound The bound on the number of transitions that can be executed for controllable events.
     */
    public BoundedResponseCheckConclusion(Bound uncontrollablesBound, Bound controllablesBound) {
        this.uncontrollablesBound = uncontrollablesBound;
        this.controllablesBound = controllablesBound;
    }

    @Override
    public boolean propertyHolds() {
        return uncontrollablesBound.isBounded() && controllablesBound.isBounded();
    }

    @Override
    public boolean hasDetails() {
        return true;
    }

    @Override
    public void printResult(DebugNormalOutput out, WarnOutput warn) {
        if (!uncontrollablesBound.hasInitialState() || !controllablesBound.hasInitialState()) {
            warn.line("The specification cannot be initialized.");
        }

        if (propertyHolds()) {
            out.line("[OK] The specification has bounded response:");
        } else {
            out.line("[ERROR] The specification does NOT have bounded response:");
        }

        out.line();
        out.inc();

        if (uncontrollablesBound.isBounded()) {
            int bound = uncontrollablesBound.getBound();
            if (bound == 0) {
                out.line("- No transitions are possible for uncontrollable events.");
            } else {
                out.line("- A sequence of at most %,d transition%s is possible for uncontrollable events.",
                        bound, (bound == 1) ? "" : "s");
            }
        } else {
            out.line("- An infinite sequence of transitions is possible for uncontrollable events.");
        }

        if (controllablesBound.isBounded()) {
            int bound = controllablesBound.getBound();
            if (bound == 0) {
                out.line("- No transitions are possible for controllable events.");
            } else {
                out.line("- A sequence of at most %,d transition%s is possible for controllable events.",
                        bound, (bound == 1) ? "" : "s");
            }
        } else {
            out.line("- An infinite sequence of transitions is possible for controllable events.");
        }

        out.dec();
    }
}
