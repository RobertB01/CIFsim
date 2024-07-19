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

import org.eclipse.escet.cif.bdd.spec.CifBddSpec;

/**
 * A BDD-based check that can be performed by the controller properties checker.
 *
 * @param <T> The type of the conclusion of the check.
 */
public abstract class ControllerCheckerBddBasedCheck<T extends CheckConclusion> extends ControllerCheckerCheck<T> {
    /**
     * Performs the check.
     *
     * @param cifBddSpec The BDD representation of the specification to check.
     * @return The check result, or {@code null} if termination was requested.
     */
    public abstract T performCheck(CifBddSpec cifBddSpec);
}
