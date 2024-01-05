//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.multivaluetrees;

/** Variable class useful for multi-value trees without external connections. */
public class SimpleVariable {
    /** Name of the variable. */
    public final String name;

    /** Lowest allowed value of the variable. */
    public final int lowestValue;

    /** Number of different values allowed in the variable. */
    public final int length;

    /**
     * Constructor of the {@link SimpleVariable} class.
     *
     * @param name Name of the variable.
     * @param lowestValue Lowest allowed value of the variable.
     * @param length Number of different values allowed in the variable.
     */
    public SimpleVariable(String name, int lowestValue, int length) {
        this.name = name;
        this.lowestValue = lowestValue;
        this.length = length;
    }
}
