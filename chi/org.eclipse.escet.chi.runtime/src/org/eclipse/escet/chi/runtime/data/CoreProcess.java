//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.runtime.data;

import static org.eclipse.escet.common.java.Assert.check;

import org.eclipse.escet.common.java.removablelist.RemovableElement;

/**
 * A process with state.
 *
 * @param <T> Concrete type of process.
 */
public abstract class CoreProcess<T extends RemovableElement<T>> extends RemovableElement<T> {
    /** Result of executing a part of) the process body. */
    public static enum RunResult {
        /** There is no process. */
        DUMMY,

        /** The process has just started. */
        STARTED,

        /** The process has become blocked on a select. */
        BLOCKED,

        /** The process has finished execution. */
        FINISHED,

        /** The process has terminated. */
        TERMINATED,
    }

    /** Process state (needed for deciding the process has ended). */
    private RunResult state;

    /**
     * Constructor for the {@link CoreProcess} class.
     *
     * @param state State of the process.
     */
    public CoreProcess(RunResult state) {
        this.state = state;
    }

    /**
     * Set the state of the process.
     *
     * @param state New state of the process.
     */
    public void setState(RunResult state) {
        this.state = state;
    }

    /**
     * Obtain the state of the process.
     *
     * @return State of the process.
     */
    public RunResult getState() {
        check(this.state != RunResult.DUMMY);
        return this.state;
    }

    /**
     * Has the process finished?
     *
     * @return The process has finished executing.
     */
    public boolean isFinished() {
        return getState() == RunResult.FINISHED;
    }
}
