//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.input;

import org.eclipse.escet.common.java.Assert;

/** The target time of a time transition, chosen by an input component. */
public class ChosenTargetTime {
    /** The source time of the time transition. */
    public final double sourceTime;

    /**
     * The target time of the time transition, which is always strictly after the {@link #sourceTime} of the time
     * transition.
     */
    public final double targetTime;

    /** Whether a time point must exist in the trajectories for the target time (or a time point 'very close' to it). */
    public final boolean strictTarget;

    /**
     * Constructor for the {@link ChosenTargetTime} class.
     *
     * @param sourceTime The source time of the time transition.
     * @param targetTime The target time of the time transition, which is always strictly after the start time of the
     *     time transition.
     * @param strictTarget Whether a time point must exist in the trajectories for the target time (or a time 'very
     *     close' to it).
     */
    public ChosenTargetTime(double sourceTime, double targetTime, boolean strictTarget) {
        this.sourceTime = sourceTime;
        this.targetTime = targetTime;
        this.strictTarget = strictTarget;
        Assert.check(sourceTime < targetTime);
    }

    /**
     * Returns the chosen delay amount.
     *
     * @return The chosen delay amount.
     */
    public double getDelay() {
        return targetTime - sourceTime;
    }
}
