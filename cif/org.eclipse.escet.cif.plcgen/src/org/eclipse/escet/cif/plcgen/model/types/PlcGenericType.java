//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.types;

import java.util.function.Predicate;

/** Generic type. */
public class PlcGenericType extends PlcAbstractType {
    /** Any int type. */
    public static final PlcGenericType ANY_INT_TYPE = new PlcGenericType(PlcGenericKind.ANY_INT);

    /** Any real type. */
    public static final PlcGenericType ANY_REAL_TYPE = new PlcGenericType(PlcGenericKind.ANY_REAL);

    /** Any numeric type. */
    public static final PlcGenericType ANY_NUM_TYPE = new PlcGenericType(PlcGenericKind.ANY_NUM);

    /** Any elementary type. */
    public static final PlcGenericType ANY_ELEMENTARY_TYPE = new PlcGenericType(PlcGenericKind.ANY_ELEMENTARY);

    /** Any type. */
    public static final PlcGenericType ANY_TYPE = new PlcGenericType(PlcGenericKind.ANY);

    /** Kind of generic type. */
    public final PlcGenericKind kind;

    /**
     * Constructor of the {@link PlcGenericType} class.
     *
     * @param kind Kind of generic type.
     */
    private PlcGenericType(PlcGenericKind kind) {
        this.kind = kind;
    }

    /**
     * Check that the type is part of the generic type.
     *
     * @param type Type to check.
     * @return Whether the type is part of the generic type.
     */
    public boolean checkMatch(PlcType type) {
        return kind.isOfGenericKind.test(type);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PlcGenericType genType && genType.kind == kind;
    }

    @Override
    public int hashCode() {
        return kind.hashCode();
    }

    /** Available kinds of generic type. */
    public static enum PlcGenericKind {
        /** Any integer type (incomplete wrt the standard). */
        ANY_INT(type -> PlcElementaryType.isIntType(type)),

        /** Any real type. */
        ANY_REAL(type -> PlcElementaryType.isRealType(type)),

        /** Any numeric type. */
        ANY_NUM(type -> PlcElementaryType.isIntType(type) || PlcElementaryType.isRealType(type)),

        /** Any elementary type (incomplete wrt the standard). */
        ANY_ELEMENTARY(type -> PlcElementaryType.isIntType(type) || PlcElementaryType.isRealType(type)
                || type.equals(PlcElementaryType.BOOL_TYPE)),

        /** Any type. */
        ANY(type -> true);

        /** Test function whether a type is part of the generic type. */
        private final Predicate<PlcType> isOfGenericKind;

        /**
         * Constructor of the {@link PlcGenericKind} class.
         *
         * @param isOfGenericKind Test function whether a type is part of the generic type.
         */
        private PlcGenericKind(Predicate<PlcType> isOfGenericKind) {
            this.isOfGenericKind = isOfGenericKind;
        }
    }
}
