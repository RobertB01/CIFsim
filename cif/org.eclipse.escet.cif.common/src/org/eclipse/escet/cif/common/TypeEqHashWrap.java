//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifTypeUtils.checkTypeCompat;
import static org.eclipse.escet.cif.common.CifTypeUtils.hashType;

import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.java.Assert;

/** CIF metamodel types wrapper class, for proper hashing and equality. */
public class TypeEqHashWrap {
    /** The wrapped type. */
    public final CifType type;

    /** Whether to ignore ranges of integer types. */
    public final boolean ignoreRangesInt;

    /** Whether to ignore ranges of list types. */
    public final boolean ignoreRangesList;

    /**
     * Constructor for the {@link TypeEqHashWrap}.
     *
     * @param type The wrapped type.
     * @param ignoreRanges Whether to ignore integer and list type ranges ({@code true}, use
     *     {@link RangeCompat#IGNORE}), or take integer and list type ranges into account ({@code false}, use
     *     {@link RangeCompat#EQUAL}).
     */
    public TypeEqHashWrap(CifType type, boolean ignoreRanges) {
        this(type, ignoreRanges, ignoreRanges);
    }

    /**
     * Constructor for the {@link TypeEqHashWrap}.
     *
     * @param type The wrapped type.
     * @param ignoreRangesInt Whether to ignore integer type ranges ({@code true}, use {@link RangeCompat#IGNORE}), or
     *     take integer type ranges into account ({@code false}, use {@link RangeCompat#EQUAL}).
     * @param ignoreRangesList Whether to ignore list type ranges ({@code true}, use {@link RangeCompat#IGNORE}), or
     *     take list type ranges into account ({@code false}, use {@link RangeCompat#EQUAL}).
     */
    public TypeEqHashWrap(CifType type, boolean ignoreRangesInt, boolean ignoreRangesList) {
        this.type = type;
        this.ignoreRangesInt = ignoreRangesInt;
        this.ignoreRangesList = ignoreRangesList;
    }

    @Override
    public int hashCode() {
        return hashType(type, ignoreRangesInt, ignoreRangesList);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        TypeEqHashWrap other = (TypeEqHashWrap)obj;
        Assert.check(ignoreRangesInt == other.ignoreRangesInt);
        Assert.check(ignoreRangesList == other.ignoreRangesList);
        RangeCompat rangeCompatInt = ignoreRangesInt ? RangeCompat.IGNORE : RangeCompat.EQUAL;
        RangeCompat rangeCompatList = ignoreRangesList ? RangeCompat.IGNORE : RangeCompat.EQUAL;
        return checkTypeCompat(type, other.type, rangeCompatInt, rangeCompatList);
    }
}
