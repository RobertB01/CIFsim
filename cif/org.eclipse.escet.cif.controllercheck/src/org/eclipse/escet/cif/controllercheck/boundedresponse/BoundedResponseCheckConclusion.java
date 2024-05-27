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
import org.eclipse.escet.common.java.Assert;

/** Conclusion of the bounded response check. */
public class BoundedResponseCheckConclusion implements CheckConclusion {
    /**
     * The bound on the number of uncontrollable events per cycle. Is {@code -1} if the system cannot be initialized, a
     * non-negative integer indicating the bound if it has bounded response, and {@code null} if it does not have
     * bounded response.
     */
    private final Integer uncontrollableBound;

    /**
     * The bound on the number of controllable events per cycle. Is {@code -1} if the system cannot be initialized, a
     * non-negative integer indicating the bound if it has bounded response, and {@code null} if it does not have
     * bounded response.
     */
    private final Integer controllableBound;

    /**
     * Constructor for the {@link BoundedResponseCheckConclusion} class.
     *
     * @param uncontrollableBound The bound on the number of uncontrollable events per cycle. Is {@code -1} if the
     *     system cannot be initialized, a non-negative integer indicating the bound if it has bounded response, and
     *     {@code null} if it does not have bounded response.
     * @param controllableBound The bound on the number of controllable events per cycle. Is {@code -1} if the system
     *     cannot be initialized, a non-negative integer indicating the bound if it has bounded response, and
     *     {@code null} if it does not have bounded response.
     */
    public BoundedResponseCheckConclusion(Integer uncontrollableBound, Integer controllableBound) {
        this.uncontrollableBound = uncontrollableBound;
        this.controllableBound = controllableBound;

        if (uncontrollableBound != null) {
            Assert.check(uncontrollableBound >= -1);
        }
        if (controllableBound != null) {
            Assert.check(controllableBound >= -1);
        }
    }

    @Override
    public boolean propertyHolds() {
        return uncontrollableBound != null && controllableBound != null;
    }

    @Override
    public void printDetails() {
        if ((uncontrollableBound != null && uncontrollableBound == -1)
                || (controllableBound != null && controllableBound == -1))
        {
            warn("The specification can not be initialized.");
        }

        if (uncontrollableBound == null) {
            out("[ERROR] The specification does NOT have bounded response for uncontrollable events.");
        } else {
            out("[OK] The specification has bounded response for uncontrollable events (bound: %,d).",
                    Math.max(0, uncontrollableBound));
        }

        if (controllableBound == null) {
            out("[ERROR] The specification does NOT have bounded response for controllable events.");
        } else {
            out("[OK] The specification has bounded response for controllable events (bound: %,d).",
                    Math.max(0, controllableBound));
        }
    }
}
