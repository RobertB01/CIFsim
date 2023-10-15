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

package org.eclipse.escet.cif.codegen.typeinfos;

import static org.eclipse.escet.cif.common.CifTypeUtils.normalizeType;
import static org.eclipse.escet.common.java.Lists.listc;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.StringExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** Generic type info about the string type. */
public abstract class StringTypeInfo extends TypeInfo {
    /**
     * Constructor for {@link StringTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     */
    public StringTypeInfo(CifType cifType) {
        super(cifType);
    }

    @Override
    protected String makeTypeName() {
        return "S";
    }

    /**
     * Convert the literal value expression to the target language.
     *
     * @param value Literal value, escaped string, with double quotes added around it.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Result of the conversion.
     */
    public abstract ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt);

    /**
     * Convert the string concatenation expression to the target language.
     *
     * @param expr Concatenation expression to convert.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Result of the conversion.
     */
    public abstract ExprCode convertConcatenation(BinaryExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'size' standard library function call to the target language.
     *
     * @param expression Argument of the function call.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Result of the conversion.
     */
    public abstract ExprCode convertSizeStdLib(Expression expression, Destination dest, CodeContext ctxt);

    /**
     * Convert the 'format' standard library function call to the target language.
     *
     * @param args Arguments of the function call.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Result of the conversion.
     */
    public ExprCode convertFormatStdLib(List<Expression> args, Destination dest, CodeContext ctxt) {
        // Get pattern.
        StringExpression patternExpr = (StringExpression)args.get(0);
        String pattern = patternExpr.getValue();

        // Generate code for the values (remaining arguments), and also
        // get their types.
        List<CifType> valueTypes = listc(args.size() - 1);
        for (int i = 1; i < args.size(); i++) {
            valueTypes.add(normalizeType(args.get(i).getType()));
        }

        // Generate code for the pattern.
        return convertFormatFunction(pattern, args.subList(1, args.size()), valueTypes, dest, ctxt);
    }

    /**
     * Convert the 'format' pattern and its arguments to construction of a string in the target language.
     *
     * @param pattern Pattern argument of the 'format' function call.
     * @param args Arguments of the pattern.
     * @param argTypes Normalized types of the arguments of the pattern.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Result of the conversion.
     */
    public abstract ExprCode convertFormatFunction(String pattern, List<Expression> args, List<CifType> argTypes,
            Destination dest, CodeContext ctxt);

    /**
     * Access a character of the string.
     *
     * @param childCode Code to obtain the string.
     * @param indexCode Code to obtain the index value.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Code expressing accessing the character in the string at the requested index.
     */
    public abstract ExprCode getProjectedValue(ExprCode childCode, ExprCode indexCode, Destination dest,
            CodeContext ctxt);

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        // Nothing to do, range is always valid.
    }
}
