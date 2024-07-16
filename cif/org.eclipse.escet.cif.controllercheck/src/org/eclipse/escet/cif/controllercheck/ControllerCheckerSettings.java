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

import java.util.function.Supplier;

import org.eclipse.escet.common.java.exceptions.InvalidOptionException;
import org.eclipse.escet.common.java.output.BlackHoleOutputProvider;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.java.output.WarnOutput;

/** Settings for the controller properties checker. */
public class ControllerCheckerSettings {
    /** Whether to perform the bounded response check. */
    private boolean checkBoundedResponse = true;

    /** Whether to perform the confluence check. */
    private boolean checkConfluence = true;

    /** Whether to perform the finite response check. */
    private boolean checkFiniteResponse = true;

    /** Whether to perform the non-blocking under control check. */
    private boolean checkNonBlockingUnderControl = true;

    /** Callback that indicates whether execution should be terminated on user request. */
    private Supplier<Boolean> shouldTerminate = () -> false;

    /** Callback to send normal output to the user. */
    private DebugNormalOutput normalOutput = new BlackHoleOutputProvider().getNormalOutput();

    /** Callback to send debug output to the user. */
    private DebugNormalOutput debugOutput = new BlackHoleOutputProvider().getDebugOutput();

    /** Callback to send warnings to the user. */
    private WarnOutput warnOutput = new BlackHoleOutputProvider().getWarnOutput();

    /**
     * Returns whether to perform the bounded response check.
     *
     * @return {@code true} to perform the check, {@code false} otherwise.
     */
    boolean getCheckBoundedResponse() {
        return checkBoundedResponse;
    }

    /**
     * Sets whether to perform the bounded response check.
     *
     * @param checkBoundedResponse {@code true} to perform the check, {@code false} to skip it.
     */
    void setCheckBoundedResponse(boolean checkBoundedResponse) {
        this.checkBoundedResponse = checkBoundedResponse;
    }

    /**
     * Returns whether to perform the confluence check.
     *
     * @return {@code true} to perform the check, {@code false} otherwise.
     */
    boolean getCheckConfluence() {
        return checkConfluence;
    }

    /**
     * Sets whether to perform the confluence check.
     *
     * @param checkConfluence {@code true} to perform the check, {@code false} to skip it.
     */
    void setCheckConfluence(boolean checkConfluence) {
        this.checkConfluence = checkConfluence;
    }

    /**
     * Returns whether to perform the finite response check.
     *
     * @return {@code true} to perform the check, {@code false} otherwise.
     */
    boolean getCheckFiniteResponse() {
        return checkFiniteResponse;
    }

    /**
     * Sets whether to perform the finite response check.
     *
     * @param checkFiniteResponse {@code true} to perform the check, {@code false} to skip it.
     */
    void setCheckFiniteResponse(boolean checkFiniteResponse) {
        this.checkFiniteResponse = checkFiniteResponse;
    }

    /**
     * Returns whether to perform the bounded response check.
     *
     * @return {@code true} to perform the check, {@code false} otherwise.
     */
    boolean getCheckNonBlockingUnderControl() {
        return checkNonBlockingUnderControl;
    }

    /**
     * Sets whether to perform the non-blocking under control check.
     *
     * @param checkNonBlockingUnderControl {@code true} to perform the check, {@code false} to skip it.
     */
    void setCheckNonBlockingUnderControl(boolean checkNonBlockingUnderControl) {
        this.checkNonBlockingUnderControl = checkNonBlockingUnderControl;
    }

    /**
     * Gets the callback that indicates whether execution should be terminated on user request.
     *
     * @return The callback.
     */
    Supplier<Boolean> getShouldTerminate() {
        return shouldTerminate;
    }

    /**
     * Sets the callback that indicates whether execution should be terminated on user request.
     *
     * @param shouldTerminate The callback.
     */
    void setShouldTerminate(Supplier<Boolean> shouldTerminate) {
        this.shouldTerminate = shouldTerminate;
    }

    /**
     * Get the callback to send normal output to the user.
     *
     * @return The callback.
     */
    DebugNormalOutput getNormalOutput() {
        return normalOutput;
    }

    /**
     * Set the callback to send normal output to the user.
     *
     * @param normalOutput The callback.
     */
    void setNormalOutput(DebugNormalOutput normalOutput) {
        this.normalOutput = normalOutput;
    }

    /**
     * Get the callback to send debug output to the user.
     *
     * @return The callback.
     */
    DebugNormalOutput getDebugOutput() {
        return debugOutput;
    }

    /**
     * Set the callback to send debug output to the user.
     *
     * @param debugOutput The callback.
     */
    void setDebugOutput(DebugNormalOutput debugOutput) {
        this.debugOutput = debugOutput;
    }

    /**
     * Get the callback to send warnings to the user.
     *
     * @return The callback.
     */
    WarnOutput getWarnOutput() {
        return warnOutput;
    }

    /**
     * Set the callback to send warnings to the user.
     *
     * @param warnOutput The callback.
     */
    void setWarnOutput(WarnOutput warnOutput) {
        this.warnOutput = warnOutput;
    }

    /**
     * Check settings.
     *
     * @throws InvalidOptionException If a problem is found in the settings.
     */
    public void check() {
        // Ensure at least one check is enabled.
        if (!checkBoundedResponse && !checkNonBlockingUnderControl && !checkFiniteResponse && !checkConfluence) {
            throw new InvalidOptionException(
                    "No checks enabled. Enable one of the checks for the controller properties checker to check.");
        }
    }
}
