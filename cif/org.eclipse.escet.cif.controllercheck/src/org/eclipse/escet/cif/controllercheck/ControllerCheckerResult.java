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
}
