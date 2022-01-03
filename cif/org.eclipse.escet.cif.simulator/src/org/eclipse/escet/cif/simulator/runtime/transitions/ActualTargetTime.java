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

package org.eclipse.escet.cif.simulator.runtime.transitions;

import org.eclipse.escet.cif.simulator.input.ChosenTargetTime;
import org.eclipse.escet.cif.simulator.input.InputComponent;
import org.eclipse.escet.common.java.Assert;

/** The target time of a time transition, as it is to be actually taken. */
public class ActualTargetTime {
    /** The source time of the time transition. */
    public final double sourceTime;

    /**
     * The target time of the time transition, which is always strictly after the {@link #sourceTime} of the time
     * transition.
     */
    public final double targetTime;

    /**
     * Was the time transition {@link InputComponent#interruptTimeTrans interrupted} by the {@link InputComponent input
     * component}, before the chosen target time?
     */
    public final boolean interrupted;

    /**
     * Constructor for the {@link ActualTargetTime} class, in case the time transition was not interrupted before the
     * chosen target time.
     *
     * @param chosenTargetTime The chosen target time of the time transition.
     */
    public ActualTargetTime(ChosenTargetTime chosenTargetTime) {
        this(chosenTargetTime.sourceTime, chosenTargetTime.targetTime, false);
    }

    /**
     * Constructor for the {@link ActualTargetTime} class, in case the time transition was interrupted before the chosen
     * target time.
     *
     * @param chosenTargetTime The chosen target time of the time transition.
     * @param actualTargetTime The actual target time of the time transition.
     */
    public ActualTargetTime(ChosenTargetTime chosenTargetTime, double actualTargetTime) {
        this(chosenTargetTime.sourceTime, actualTargetTime, true);
        Assert.check(actualTargetTime < chosenTargetTime.targetTime);
    }

    /**
     * Constructor for the {@link ActualTargetTime} class.
     *
     * @param sourceTime The source time of the time transition.
     * @param targetTime The target time of the time transition, which is always strictly after the start time of the
     *     time transition.
     * @param interrupted Was the time transition {@link InputComponent#interruptTimeTrans interrupted} by the
     *     {@link InputComponent input component}, before the chosen target time?
     */
    private ActualTargetTime(double sourceTime, double targetTime, boolean interrupted) {
        this.sourceTime = sourceTime;
        this.targetTime = targetTime;
        this.interrupted = interrupted;
        Assert.check(sourceTime < targetTime);
    }

    /**
     * Returns the actual delay amount.
     *
     * @return The actual delay amount.
     */
    public double getDelay() {
        return targetTime - sourceTime;
    }
}
