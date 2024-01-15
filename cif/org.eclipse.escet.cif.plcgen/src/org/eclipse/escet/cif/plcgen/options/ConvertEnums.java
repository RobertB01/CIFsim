//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.options;

/** Available ways to handle enumeration conversion. */
public enum ConvertEnums {
    /** Let the target decide what to do. */
    AUTO,

    /** Keep enumeration data types. */
    KEEP,

    /** Convert enumerations to integers. */
    INTS,

    /** Convert enumerations to constants. */
    CONSTS;
}
