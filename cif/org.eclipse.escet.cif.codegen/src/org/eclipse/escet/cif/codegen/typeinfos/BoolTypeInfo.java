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

package org.eclipse.escet.cif.codegen.typeinfos;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** Generic type info about the boolean type. */
public abstract class BoolTypeInfo extends TypeInfo {
    /**
     * Constructor for {@link BoolTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     */
    public BoolTypeInfo(CifType cifType) {
        super(cifType);
    }

    @Override
    public String makeTypeName() {
        return "B";
    }

    /**
     * Convert the literal value expression to the target language.
     *
     * @param value Literal value.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Result of the conversion.
     */
    public abstract ExprCode convertLiteral(boolean value, Destination dest, CodeContext ctxt);

    /**
     * Convert an invert expression (a 'not x') to the target language.
     *
     * @param child Child expression.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Result of the conversion.
     */
    public abstract ExprCode convertInvert(Expression child, Destination dest, CodeContext ctxt);

    /**
     * Convert a binary short circuit expression (a 'x {implies, or, and} y') to the target language.
     *
     * @param expr expression to convert.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Result of the conversion.
     */
    public abstract ExprCode convertShortCircuit(BinaryExpression expr, Destination dest, CodeContext ctxt);

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        // Nothing to do, range is always valid.
    }
}
