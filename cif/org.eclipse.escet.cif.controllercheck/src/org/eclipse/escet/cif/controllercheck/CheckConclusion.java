//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

    /** Output the details of the check, some of the output may be controlled by options. */
    public void printDetails();
}
