//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2plc.options;

/** Should enumeration data types be converted? */
public enum ConvertEnums {
    /** No, keep enumeration data types. */
    NO,

    /** Yes, convert enumerations to integers. */
    INTS,

    /** Yes, convert enumerations to constants. */
    CONSTS;
}
