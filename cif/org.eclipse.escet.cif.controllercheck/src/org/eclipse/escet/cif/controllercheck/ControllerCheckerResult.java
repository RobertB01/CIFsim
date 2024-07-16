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

package org.eclipse.escet.cif.controllercheck;

import org.eclipse.escet.cif.controllercheck.boundedresponse.BoundedResponseCheckConclusion;
import org.eclipse.escet.cif.controllercheck.confluence.ConfluenceCheckConclusion;
import org.eclipse.escet.cif.controllercheck.finiteresponse.FiniteResponseCheckConclusion;
import org.eclipse.escet.cif.controllercheck.nonblockingundercontrol.NonBlockingUnderControlCheckConclusion;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.typechecker.annotations.builtin.ControllerPropertiesAnnotationProvider;

/** Controller properties checker result. */
public class ControllerCheckerResult {
    /** Conclusion of the bounded response check, or {@code null} if the check was skipped. */
    public final BoundedResponseCheckConclusion boundedResponseConclusion;

    /** Conclusion of the confluence check, or {@code null} if the check was skipped. */
    public final ConfluenceCheckConclusion confluenceConclusion;

    /** Conclusion of the finite response check, or {@code null} if the check was skipped. */
    public final FiniteResponseCheckConclusion finiteResponseConclusion;

    /** Conclusion of the non-blocking under control check, or {@code null} if the check was skipped. */
    public final NonBlockingUnderControlCheckConclusion nonBlockingUnderControlConclusion;

    /**
     * Constructor for the {@link ControllerCheckerResult} class.
     *
     * @param boundedResponseConclusion Conclusion of the bounded response check, or {@code null} if the check was
     *     skipped.
     * @param confluenceConclusion Conclusion of the confluence check, or {@code null} if the check was skipped.
     * @param finiteResponseConclusion Conclusion of the finite response check, or {@code null} if the check was
     *     skipped.
     * @param nonBlockingUnderControlConclusion Conclusion of the non-blocking under control check, or {@code null} if
     *     the check was skipped.
     */
    public ControllerCheckerResult(BoundedResponseCheckConclusion boundedResponseConclusion,
            ConfluenceCheckConclusion confluenceConclusion, FiniteResponseCheckConclusion finiteResponseConclusion,
            NonBlockingUnderControlCheckConclusion nonBlockingUnderControlConclusion)
    {
        this.boundedResponseConclusion = boundedResponseConclusion;
        this.confluenceConclusion = confluenceConclusion;
        this.finiteResponseConclusion = finiteResponseConclusion;
        this.nonBlockingUnderControlConclusion = nonBlockingUnderControlConclusion;
    }

    /**
     * Returns whether all checks (that were not skipped) hold.
     *
     * @return {@code true} if all checks hold or where skipped, {@code false} if any check failed.
     */
    public boolean allChecksHold() {
        boolean result = true;
        result &= (boundedResponseConclusion == null) || boundedResponseConclusion.propertyHolds();
        result &= (confluenceConclusion == null) || confluenceConclusion.propertyHolds();
        result &= (finiteResponseConclusion == null) || finiteResponseConclusion.propertyHolds();
        result &= (nonBlockingUnderControlConclusion == null) || nonBlockingUnderControlConclusion.propertyHolds();
        return result;
    }

    /**
     * Update a specification for the outcome of the checks. If a check was not performed, the corresponding annotation
     * arguments are not updated for that check, and thus the current result of the check (if any) is kept. That way,
     * users can do checks one by one, or they can redo only a certain check.
     *
     * @param spec The specification to update. Is modified in-place.
     */
    public void updateSpecification(Specification spec) {
        if (boundedResponseConclusion != null) {
            Integer unctrlBound = boundedResponseConclusion.propertyHolds()
                    ? boundedResponseConclusion.uncontrollablesBound.getBound() : null;
            Integer ctrlBound = boundedResponseConclusion.propertyHolds()
                    ? boundedResponseConclusion.controllablesBound.getBound() : null;
            ControllerPropertiesAnnotationProvider.setBoundedResponse(spec, unctrlBound, ctrlBound);
        }

        if (confluenceConclusion != null) {
            ControllerPropertiesAnnotationProvider.setConfluence(spec, confluenceConclusion.propertyHolds());
        }

        if (finiteResponseConclusion != null) {
            ControllerPropertiesAnnotationProvider.setFiniteResponse(spec, finiteResponseConclusion.propertyHolds());
        }

        if (nonBlockingUnderControlConclusion != null) {
            ControllerPropertiesAnnotationProvider.setNonBlockingUnderControl(spec,
                    nonBlockingUnderControlConclusion.propertyHolds());
        }
    }
}
