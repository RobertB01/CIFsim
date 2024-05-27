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

package org.eclipse.escet.cif.controllercheck.boundedresponse;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;

import org.eclipse.escet.cif.controllercheck.CheckConclusion;

/** Conclusion of the bounded response check. */
public class BoundedResponseCheckConclusion implements CheckConclusion {
    /** The bound on the number of transitions that can be executed for uncontrollable events. */
    private final Bound uncontrollableBound;

    /** The bound on the number of transitions that can be executed for controllable events. */
    private final Bound controllableBound;

    /**
     * Constructor for the {@link BoundedResponseCheckConclusion} class.
     *
     * @param uncontrollableBound The bound on the number of transitions that can be executed for uncontrollable events.
     * @param controllableBound The bound on the number of transitions that can be executed for controllable events.
     */
    public BoundedResponseCheckConclusion(Bound uncontrollableBound, Bound controllableBound) {
        this.uncontrollableBound = uncontrollableBound;
        this.controllableBound = controllableBound;
    }

    @Override
    public boolean propertyHolds() {
        return uncontrollableBound.isBounded() && controllableBound.isBounded();
    }

    @Override
    public void printDetails() {
        if (!uncontrollableBound.hasInitialState() || !controllableBound.hasInitialState()) {
            warn("The specification can not be initialized.");
        }

        if (uncontrollableBound.isBounded()) {
            out("[OK] The specification has bounded response for uncontrollable events (bound: %,d).",
                    uncontrollableBound.getBound());
        } else {
            out("[ERROR] The specification does NOT have bounded response for uncontrollable events.");
        }

        if (controllableBound.isBounded()) {
            out("[OK] The specification has bounded response for controllable events (bound: %,d).",
                    controllableBound.getBound());
        } else {
            out("[ERROR] The specification does NOT have bounded response for controllable events.");
        }
    }
}
