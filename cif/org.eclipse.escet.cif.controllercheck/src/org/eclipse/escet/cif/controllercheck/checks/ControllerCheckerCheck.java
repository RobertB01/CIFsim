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

package org.eclipse.escet.cif.controllercheck.checks;

/**
 * A check that can be performed by the controller properties checker.
 *
 * @param <T> The type of the conclusion of the check.
 */
public abstract class ControllerCheckerCheck<T extends CheckConclusion> {
    /**
     * Returns the name of the property to check.
     *
     * @return The property name. Starts with a lower-case letter and does not end with a period.
     */
    public abstract String getPropertyName();

    /**
     * Returns whether this check is BDD-based.
     *
     * @return {@code true} if this check is BDD-based, {@code false} otherwise.
     */
    public boolean isBddBasedCheck() {
        return this instanceof ControllerCheckerBddBasedCheck;
    }

    /**
     * Returns whether this check is MDD-based.
     *
     * @return {@code true} if this check is MDD-based, {@code false} otherwise.
     */
    public boolean isMddBasedCheck() {
        return this instanceof ControllerCheckerMddBasedCheck;
    }
}
