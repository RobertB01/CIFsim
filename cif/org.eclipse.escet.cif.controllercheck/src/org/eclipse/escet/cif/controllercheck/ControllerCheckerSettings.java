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

import org.eclipse.escet.common.java.Termination;
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

    /**
     * Whether to print the events that appear in finite response control loops as part of printing the finite response
     * check conclusion.
     */
    private boolean printFiniteResponseControlLoops = true;

    /** Cooperative termination query function. */
    private Termination termination = Termination.NEVER;

    /** Callback to send normal output to the user. */
    private DebugNormalOutput normalOutput = new BlackHoleOutputProvider().getNormalOutput();

    /** Callback to send debug output to the user. */
    private DebugNormalOutput debugOutput = new BlackHoleOutputProvider().getDebugOutput();

    /** Callback to send warnings to the user. */
    private WarnOutput warnOutput = new BlackHoleOutputProvider().getWarnOutput();

    /**
     * Returns whether to perform the bounded response check.
     *
     * <p>
     * By default, the check is enabled.
     * </p>
     *
     * @return {@code true} to perform the check, {@code false} otherwise.
     */
    public boolean getCheckBoundedResponse() {
        return checkBoundedResponse;
    }

    /**
     * Sets whether to perform the bounded response check.
     *
     * <p>
     * By default, the check is enabled.
     * </p>
     *
     * @param checkBoundedResponse {@code true} to perform the check, {@code false} to skip it.
     */
    public void setCheckBoundedResponse(boolean checkBoundedResponse) {
        this.checkBoundedResponse = checkBoundedResponse;
    }

    /**
     * Returns whether to perform the confluence check.
     *
     * <p>
     * By default, the check is enabled.
     * </p>
     *
     * @return {@code true} to perform the check, {@code false} otherwise.
     */
    public boolean getCheckConfluence() {
        return checkConfluence;
    }

    /**
     * Sets whether to perform the confluence check.
     *
     * <p>
     * By default, the check is enabled.
     * </p>
     *
     * @param checkConfluence {@code true} to perform the check, {@code false} to skip it.
     */
    public void setCheckConfluence(boolean checkConfluence) {
        this.checkConfluence = checkConfluence;
    }

    /**
     * Returns whether to perform the finite response check.
     *
     * <p>
     * By default, the check is enabled.
     * </p>
     *
     * @return {@code true} to perform the check, {@code false} otherwise.
     */
    public boolean getCheckFiniteResponse() {
        return checkFiniteResponse;
    }

    /**
     * Sets whether to perform the finite response check.
     *
     * <p>
     * By default, the check is enabled.
     * </p>
     *
     * @param checkFiniteResponse {@code true} to perform the check, {@code false} to skip it.
     */
    public void setCheckFiniteResponse(boolean checkFiniteResponse) {
        this.checkFiniteResponse = checkFiniteResponse;
    }

    /**
     * Returns whether to perform the bounded response check.
     *
     * <p>
     * By default, the check is enabled.
     * </p>
     *
     * @return {@code true} to perform the check, {@code false} otherwise.
     */
    public boolean getCheckNonBlockingUnderControl() {
        return checkNonBlockingUnderControl;
    }

    /**
     * Sets whether to perform the non-blocking under control check.
     *
     * <p>
     * By default, the check is enabled.
     * </p>
     *
     * @param checkNonBlockingUnderControl {@code true} to perform the check, {@code false} to skip it.
     */
    public void setCheckNonBlockingUnderControl(boolean checkNonBlockingUnderControl) {
        this.checkNonBlockingUnderControl = checkNonBlockingUnderControl;
    }

    /**
     * Returns whether to print the events that appear in finite response control loops as part of printing the finite
     * response check conclusion.
     *
     * <p>
     * By default, the events are printed.
     * </p>
     *
     * @return {@code true} to print the events, {@code false} to not print them.
     */
    public boolean getPrintFiniteResponseControlLoops() {
        return printFiniteResponseControlLoops;
    }

    /**
     * Sets whether to print the events that appear in finite response control loops as part of printing the finite
     * response check conclusion.
     *
     * <p>
     * By default, the events are printed.
     * </p>
     *
     * @param printFiniteResponseControlLoops {@code true} to print the events, {@code false} to not print them.
     */
    public void setPrintFiniteResponseControlLoops(boolean printFiniteResponseControlLoops) {
        this.printFiniteResponseControlLoops = printFiniteResponseControlLoops;
    }

    /**
     * Gets the cooperative termination query function.
     *
     * <p>
     * By default, termination is never requested.
     * </p>
     *
     * @return The cooperative termination query function.
     */
    public Termination getTermination() {
        return termination;
    }

    /**
     * Sets the cooperative termination query function.
     *
     * <p>
     * By default, termination is never requested.
     * </p>
     *
     * @param termination The cooperative termination query function.
     */
    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    /**
     * Get the callback to send normal output to the user.
     *
     * <p>
     * By default, all normal output is ignored.
     * </p>
     *
     * @return The callback.
     */
    public DebugNormalOutput getNormalOutput() {
        return normalOutput;
    }

    /**
     * Set the callback to send normal output to the user.
     *
     * <p>
     * By default, all normal output is ignored.
     * </p>
     *
     * @param normalOutput The callback.
     */
    public void setNormalOutput(DebugNormalOutput normalOutput) {
        this.normalOutput = normalOutput;
    }

    /**
     * Get the callback to send debug output to the user.
     *
     * <p>
     * By default, all debug output is ignored.
     * </p>
     *
     * @return The callback.
     */
    public DebugNormalOutput getDebugOutput() {
        return debugOutput;
    }

    /**
     * Set the callback to send debug output to the user.
     *
     * <p>
     * By default, all debug output is ignored.
     * </p>
     *
     * @param debugOutput The callback.
     */
    public void setDebugOutput(DebugNormalOutput debugOutput) {
        this.debugOutput = debugOutput;
    }

    /**
     * Get the callback to send warnings to the user.
     *
     * <p>
     * By default, all warnings are ignored.
     * </p>
     *
     * @return The callback.
     */
    public WarnOutput getWarnOutput() {
        return warnOutput;
    }

    /**
     * Set the callback to send warnings to the user.
     *
     * <p>
     * By default, all warnings are ignored.
     * </p>
     *
     * @param warnOutput The callback.
     */
    public void setWarnOutput(WarnOutput warnOutput) {
        this.warnOutput = warnOutput;
    }

    /**
     * Returns the number of checks that are enabled.
     *
     * @return The number of enabled checks.
     */
    public int getNumberOfChecksEnabled() {
        int result = 0;
        if (checkBoundedResponse) {
            result++;
        }
        if (checkConfluence) {
            result++;
        }
        if (checkFiniteResponse) {
            result++;
        }
        if (checkNonBlockingUnderControl) {
            result++;
        }
        return result;
    }

    /**
     * Check settings.
     *
     * @throws InvalidOptionException If a problem is found in the settings.
     */
    public void check() {
        // Ensure at least one check is enabled.
        if (getNumberOfChecksEnabled() == 0) {
            throw new InvalidOptionException(
                    "No checks enabled. Enable one of the checks for the controller properties checker to check.");
        }
    }
}
