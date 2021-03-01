//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

/** Select choice for waiting for started child processes. */
public class SelectWaitRunChilds extends SelectChoice {
    /** Process being monitored. */
    public BaseProcess process;

    /**
     * Constructor of the {@link SelectWaitRunChilds} class.
     *
     * @param chiCoordinator Coordinator of the Chi simulation.
     * @param process Process waiting for the child processes.
     * @param choice Number of the next statement block to execute.
     */
    public SelectWaitRunChilds(ChiCoordinator chiCoordinator, BaseProcess process, int choice) {
        super(chiCoordinator, GuardKind.CHILDS, ChannelKind.NONE, null, choice);
        this.process = process;
    }

    @Override
    public BaseProcess getProcess() {
        return process;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <em>Unlike the general {@link SelectChoice#getGuard} method, this method is safe to call without having to catch
     * user evaluation exceptions.</em>
     * </p>
     */
    @Override
    public boolean getGuard() {
        return process.allChildProcessesFinished();
    }
}
