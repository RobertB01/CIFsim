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

package org.eclipse.escet.common.app.framework.management;

import org.eclipse.escet.common.app.framework.AppEnvData;
import org.eclipse.escet.common.app.framework.Application;

/**
 * Application status. Status of an application framework application.
 *
 * @see Application
 * @see AppManager
 */
public enum AppStatus {
    /**
     * Starting. Application class has been created and the application framework is running the application. The
     * {@link Application#runInternal} method has not yet been invoked. The application framework is busy initializing
     * the application, performing command line option processing, showing the option dialog, or so.
     */
    STARTING,

    /**
     * Running. The application is running. Either the {@link Application#runInternal} method is running, or the method
     * has finished and the application framework is finalizing the execution of the application.
     */
    RUNNING,

    /**
     * Terminating. The application is still running, but termination has been requested by the user.
     *
     * @see AppEnvData#isTerminationRequested
     */
    TERMINATING,

    /**
     * Terminated. The application is no longer running any code, after it has been terminated at the request of the
     * user.
     */
    TERMINATED,

    /** Finished. The application is no longer running any code, after it has successfully finished execution. */
    FINISHED,

    /** Failed. The application is no longer running any code, after it has unsuccessfully finished execution. */
    FAILED,

    /** Crashed. The application is no longer running any code, after it has crashed. */
    CRASHED;

    /**
     * Is this is a busy status, i.e. the application is still executing code?
     *
     * @return {@code true} if this is a busy status, {@code false} otherwise.
     */
    public boolean isBusy() {
        switch (this) {
            case STARTING:
            case RUNNING:
            case TERMINATING:
                return true;

            case TERMINATED:
            case FINISHED:
            case FAILED:
            case CRASHED:
                return false;
        }
        throw new RuntimeException("Unhandled status: " + this);
    }

    /**
     * Is this is a done status, i.e. the application has terminated successfully or unsuccessfully and is no longer
     * executing code?
     *
     * @return {@code true} if this is a done status, {@code false} otherwise.
     */
    public boolean isDone() {
        return !isBusy();
    }

    /**
     * Does this status indicate that a termination request can be given to the application, and it will have an effect?
     *
     * @return {@code true} if termination can be requested and will have an effect, {@code false} otherwise.
     */
    public boolean canTerminate() {
        switch (this) {
            case STARTING:
            case RUNNING:
                return true;

            case TERMINATING:
            case TERMINATED:
            case FINISHED:
            case FAILED:
            case CRASHED:
                return false;
        }
        throw new RuntimeException("Unhandled status: " + this);
    }
}
