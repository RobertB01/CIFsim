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

package org.eclipse.escet.tooldef.common;

import org.eclipse.escet.tooldef.metamodel.tooldef.types.ToolDefType;

/**
 * ToolDef metamodel type wrapper class, for proper {@link ToolDefTypeUtils#hashType hashing},
 * {@link ToolDefTypeUtils#areEqualTypes equality}, and {@link ToolDefTypeUtils#compareTypes ordering}.
 */
public class ToolDefTypeEqWrap implements Comparable<ToolDefTypeEqWrap> {
    /** The wrapped type. */
    public ToolDefType type;

    /**
     * Constructor for the {@link ToolDefTypeEqWrap}.
     *
     * @param type The wrapped type.
     */
    public ToolDefTypeEqWrap(ToolDefType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return ToolDefTypeUtils.hashType(type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        ToolDefTypeEqWrap other = (ToolDefTypeEqWrap)obj;
        return ToolDefTypeUtils.areEqualTypes(type, other.type);
    }

    @Override
    public int compareTo(ToolDefTypeEqWrap other) {
        return ToolDefTypeUtils.compareTypes(this.type, other.type);
    }
}
