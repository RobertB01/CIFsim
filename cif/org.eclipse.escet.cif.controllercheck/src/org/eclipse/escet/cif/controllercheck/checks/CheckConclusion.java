//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

/** Interface for querying and printing the conclusion of the check. */
public interface CheckConclusion {
    /**
     * Return the conclusion of the check.
     *
     * @return Whether the check succeeded in proving the property.
     */
    public boolean propertyHolds();

    /**
     * Whether the conclusion has details that will be printed by {@link #printResult}.
     *
     * @return {@code true} if the conclusion has details, {@code false} if it has no details.
     */
    public boolean hasDetails();

    /** Output the result of the check. Some of the output may be controlled by options. */
    public void printResult();
}
