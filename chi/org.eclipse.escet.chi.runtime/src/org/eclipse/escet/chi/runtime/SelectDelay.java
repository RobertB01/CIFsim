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

package org.eclipse.escet.chi.runtime;

import org.eclipse.escet.chi.runtime.data.BaseProcess;
import org.eclipse.escet.chi.runtime.data.Timer;

/** Select choice class for the 'delay' statement. */
public class SelectDelay extends SelectChoice {
    /** Process that delays. */
    private BaseProcess process;

    /**
     * Constructor for the {@link SelectDelay} class.
     *
     * @param chiCoordinator Coordinator of the Chi simulation.
     * @param process Process performing the delay.
     * @param jumpDest Next statement to execute after the delay has finished.
     */
    public SelectDelay(ChiCoordinator chiCoordinator, BaseProcess process, int jumpDest) {
        super(chiCoordinator, GuardKind.TIMER, ChannelKind.NONE, null, jumpDest);
        this.process = process;
    }

    @Override
    public BaseProcess getProcess() {
        return process;
    }

    @Override
    public boolean getGuard() {
        return process.delayTimer.isReady();
    }

    /**
     * Get the timer being blocked on.
     *
     * @return The timer of the delay statement.
     */
    public Timer getTimer() {
        return process.delayTimer;
    }
}
