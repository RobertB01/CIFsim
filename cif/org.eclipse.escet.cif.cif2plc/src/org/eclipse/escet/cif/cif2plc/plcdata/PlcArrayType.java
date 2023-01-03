//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2plc.plcdata;

import org.eclipse.escet.common.java.Assert;

/** PLC array type. */
public class PlcArrayType extends PlcType {
    /** The lower bound index of the array, i.e. the lowest index value that exists. Always zero for CIF. */
    public final int lower;

    /** The upper bound index of the array, i.e. the highest index value that exists. */
    public final int upper;

    /** The element type. */
    public final PlcType elemType;

    /**
     * Constructor for the {@link PlcArrayType} class.
     *
     * @param lower The lower bound index of the array, i.e. the lowest index value that exists. Always zero for CIF.
     * @param upper The upper bound index of the array, i.e. the highest index value that exists.
     * @param elemType The element type.
     */
    public PlcArrayType(int lower, int upper, PlcType elemType) {
        this.lower = lower;
        this.upper = upper;
        this.elemType = elemType;

        Assert.check(lower == 0);
        Assert.check(upper >= lower);
    }
}
