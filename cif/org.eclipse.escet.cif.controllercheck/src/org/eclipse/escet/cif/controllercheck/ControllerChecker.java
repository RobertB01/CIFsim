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

import org.eclipse.escet.common.java.exceptions.InvalidOptionException;

/** Controller properties checker. */
public class ControllerChecker {
    /** Constructor for the {@link ControllerChecker} class. */
    private ControllerChecker() {
        // Static class.
    }

    /**
     * Perform controller properties checks. At least one check must be enabled.
     *
     * @param settings The settings to use.
     */
    public static void performChecks(ControllerCheckerSettings settings) {
        // Get checks to perform.
        boolean checkBoundedResponse = settings.getCheckBoundedResponse();
        boolean checkConfluence = settings.getCheckConfluence();
        boolean checkFiniteResponse = settings.getCheckFiniteResponse();
        boolean checkNonBlockingUnderControl = settings.getCheckNonBlockingUnderControl();

        // Ensure at least one check is enabled.
        if (!checkBoundedResponse && !checkNonBlockingUnderControl && !checkFiniteResponse && !checkConfluence) {
            throw new InvalidOptionException(
                    "No checks enabled. Enable one of the checks for the controller properties checker to check.");
        }
    }
}
