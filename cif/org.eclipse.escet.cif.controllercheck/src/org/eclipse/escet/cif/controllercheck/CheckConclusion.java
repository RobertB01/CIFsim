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

package org.eclipse.escet.cif.controllercheck;

/** Interface for querying and printing the conclusion of the check. */
public interface CheckConclusion {
    /**
     * Return the conclusion of the check.
     *
     * @return Whether the check succeeded in proving the property.
     */
    public boolean propertyHolds();

    /**
     * Whether the {@link #printResult printing of the results} prints details.
     *
     * @return {@code true} if detailed are printed, {@code false} if no details are printed.
     */
    public boolean printsDetails();

    /** Output the result of the check. Some of the output may be controlled by options. */
    public void printResult();
}
