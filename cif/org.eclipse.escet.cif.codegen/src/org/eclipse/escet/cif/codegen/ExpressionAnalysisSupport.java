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

package org.eclipse.escet.cif.codegen;

import static org.eclipse.escet.cif.common.CifTypeUtils.isRangeless;
import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;

/** Support code for performing type analysis on CIF expressions. */
public class ExpressionAnalysisSupport {
    /** Constructor of the {@link ExpressionAnalysisSupport} class. */
    private ExpressionAnalysisSupport() {
        // Static class.
    }

    /**
     * Check whether the given expression is of type 'int'.
     *
     * @param expr Expression to test.
     * @return Whether the expression is of type 'int'.
     */
    public static boolean checkExprIntType(Expression expr) {
        CifType exprType = normalizeType(expr.getType());
        return exprType instanceof IntType;
    }

    /**
     * Check whether a value of the given type can be the minimum integer value.
     *
     * @param type Type to check.
     * @return Whether the value of the given type can be the minimum integer value.
     */
    public static boolean typeAllowsMinInt(CifType type) {
        IntType it = (IntType)type;
        return it.getLower() == null || it.getLower() == Integer.MIN_VALUE;
    }

    /**
     * Can a value of the given integer type be {@code 0}?
     *
     * @param type Integer type to test.
     * @return Whether a value of the given type can be {@code 0}.
     */
    public static boolean typeAllowsZero(CifType type) {
        IntType it = (IntType)type;
        if (it.getUpper() == null) {
            return true;
        }
        if (it.getUpper() < 0) {
            return false;
        }
        // Upper bound is defined, and at or above 0.
        return it.getLower() == null || it.getLower() <= 0;
    }

    /**
     * Can a value of the given integer type be {@code -1}?
     *
     * @param type Integer type to test.
     * @return Whether a value of the given type can be {@code -1}.
     */
    public static boolean typeAllowsMinusOne(CifType type) {
        IntType it = (IntType)type;
        if (it.getUpper() == null) {
            return true;
        }
        if (it.getUpper() < -1) {
            return false;
        }
        // Upper bound is defined, and at or above -1.
        return it.getLower() == null || it.getLower() <= -1;
    }

    /**
     * Decide whether the integer type is limited in range such that its values will never overflow.
     *
     * @param type Integer type to test.
     * @return Whether values of the given type never overflow.
     */
    public static boolean typeIsRanged(CifType type) {
        IntType it = (IntType)type;
        return !isRangeless(it);
    }
}
