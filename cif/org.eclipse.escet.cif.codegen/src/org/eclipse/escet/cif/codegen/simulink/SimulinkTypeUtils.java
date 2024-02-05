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

package org.eclipse.escet.cif.codegen.simulink;

import static org.eclipse.escet.cif.common.CifTypeUtils.isArrayType;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.cif.metamodel.cif.types.ListType;
import org.eclipse.escet.cif.metamodel.cif.types.RealType;
import org.eclipse.escet.common.java.Assert;

/** Utility methods related to compatibility of CIF and Simulink types. */
public class SimulinkTypeUtils {
    /** Constructor for the {@link SimulinkTypeUtils} class. */
    private SimulinkTypeUtils() {
        // Static class.
    }

    /**
     * Check whether the given type is compatible with Simulink types, for Simulink code generation.
     *
     * @param type Type to check.
     * @return {@code true} if the type if compatible, {@code false} if it is incompatible.
     */
    public static boolean isSimulinkCompatibleType(CifType type) {
        type = normalizeType(type);

        // Optionally, peel off up to two layers of 'list type' (for vector or matrix of the element type).
        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            type = normalizeType(ltype.getElementType());
        }
        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            type = normalizeType(ltype.getElementType());
        }

        if (type instanceof BoolType) {
            return true;
        } else if (type instanceof IntType) {
            return true;
        } else if (type instanceof EnumType) {
            return true;
        } else if (type instanceof RealType) {
            return true;
        }
        return false;
    }

    /**
     * Get the number of rows from the given type, or {@code 0} if there is no rows list. Function also gives the length
     * of a Matlab vector.
     *
     * <p>
     * {@link #isSimulinkCompatibleType} should hold.
     * </p>
     *
     * @param type Type to test.
     * @return Number of rows from the outer list, or else {@code 0}.
     */
    public static int getRowCount(CifType type) {
        type = normalizeType(type);

        Assert.check(isSimulinkCompatibleType(type));
        if (type instanceof ListType) {
            ListType ltype = (ListType)type;
            if (isArrayType(ltype)) {
                return ltype.getLower();
            }
        }
        return 0;
    }

    /**
     * Get the number of columns from the given type, or {@code 0} if there is no column list.
     *
     * <p>
     * {@link #isSimulinkCompatibleType} should hold. Also, for useful results, the {@link #getRowCount} method should
     * not return {@code 0}.
     * </p>
     *
     * @param type Type to test.
     * @return Number of columns from the inner list, or else {@code 0}.
     */
    public static int getColumnCount(CifType type) {
        type = normalizeType(type);

        Assert.check(isSimulinkCompatibleType(type));
        if (type instanceof ListType) { // Peel off the rows if available.
            ListType ltype = (ListType)type;
            type = normalizeType(ltype.getElementType());
        }

        if (type instanceof ListType) { // If this holds, it is the inner array.
            ListType ltype = (ListType)type;
            if (isArrayType(ltype)) {
                return ltype.getLower();
            }
        }
        return 0;
    }
}
