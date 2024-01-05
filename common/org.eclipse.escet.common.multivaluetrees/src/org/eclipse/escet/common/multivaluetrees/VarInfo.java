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

import static org.eclipse.escet.common.java.Strings.fmt;

/** Variable represented by a level in the multi-value tree. */
public class VarInfo {
    /** Level of the variable in the tree, smaller number is closer to the root. */
    public final int level;

    /** Number of child nodes for this variable. */
    public final int length;

    /** Lowest allowed value of the variable (at index value 0). */
    public final int lower;

    /** Kind of use for the variable. */
    public final int useKind;

    /** Name of the variable. */
    public final String name;

    /**
     * Constructor of the {@link VarInfo} class.
     *
     * <p>
     * Do not use this constructor directly, it's simpler if you use a {@link VarInfoBuilder}.
     * </p>
     *
     * @param level Level of the variable in the tree, smaller is closer to the root.
     * @param name Name of the variable.
     * @param useKind Kind of use for the variable.
     * @param lower Lowest allowed value of the variable.
     * @param length Number of values in the domain of the variable.
     */
    public VarInfo(int level, String name, int useKind, int lower, int length) {
        this.level = level;
        this.length = length;
        this.lower = lower;
        this.useKind = useKind;
        this.name = name;
    }

    /**
     * Get largest allowed value of the variable.
     *
     * @return Largest allowed value of the variable.
     */
    public int getUpper() {
        return lower + length - 1;
    }

    @Override
    public String toString() {
        return fmt("%s#%d[%d..%d]", name, useKind, lower, getUpper());
    }
}
