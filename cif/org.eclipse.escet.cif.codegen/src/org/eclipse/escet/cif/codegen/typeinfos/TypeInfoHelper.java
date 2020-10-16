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

package org.eclipse.escet.cif.codegen.typeinfos;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.java.JavaDataValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/** Helper class containing support code for the Java target language code generation. */
public class TypeInfoHelper {
    /** Constructor of the {@link TypeInfoHelper} class. */
    private TypeInfoHelper() {
        // Static class.
    }

    /**
     * Generate a binary expression in the target language using the supplied pattern.
     *
     * @param expr Expression to convert to the target language
     * @param target Text to use in the conversion. Contains '${left-value}' or '${left-ref}' and '${right-value}' or
     *     '${right-ref}' at the position where the left respectively right sub-expression is expected.
     * @param dest Storage destination of the result.
     * @param ctxt Code context of the expression.
     * @return The generated expression.
     */
    public static ExprCode convertBinaryExpressionPattern(BinaryExpression expr, String target, Destination dest,
            CodeContext ctxt)
    {
        ExprCode leftCode = ctxt.exprToTarget(expr.getLeft(), null);
        ExprCode rightCode = ctxt.exprToTarget(expr.getRight(), null);
        return convertBinaryExpressionPattern(leftCode, rightCode, target, dest, ctxt);
    }

    /**
     * Generate a binary expression in the target language using the supplied pattern.
     *
     * @param leftCode Result of the left side conversion.
     * @param rightCode Result of the right side conversion.
     * @param target Text to use in the conversion. Contains '${left-value}' or '${left-ref}' and '${right-value}' or
     *     '${right-ref}' at the position where the left respectively right sub-expression is expected.
     * @param dest Storage destination of the result.
     * @param ctxt Code context of the expression.
     * @return The generated expression.
     */
    public static ExprCode convertBinaryExpressionPattern(ExprCode leftCode, ExprCode rightCode, String target,
            Destination dest, CodeContext ctxt)
    {
        ExprCode result = new ExprCode();
        result.add(leftCode);
        result.add(rightCode);

        target = convertBinaryExpressionValues(leftCode.getRawDataValue(), rightCode.getRawDataValue(), target);
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(target));
        return result;
    }

    /**
     * Generate a binary expression code fragment from two data values and a text template.
     *
     * @param leftValue Value to use as left side.
     * @param rightValue Value to use as right side.
     * @param template Template text to use.
     * @return Code fragment with the arguments applied to the template.
     */
    public static String convertBinaryExpressionValues(DataValue leftValue, DataValue rightValue, String template) {
        template = tryReplaceValue(template, "${left-value}", leftValue);
        template = tryReplaceValue(template, "${right-value}", rightValue);
        template = tryReplaceReference(template, "${left-ref}", leftValue);
        template = tryReplaceReference(template, "${right-ref}", rightValue);
        return template;
    }

    /**
     * Try to replace a data value 'pattern' in the text 'target', with the data value in 'exprCode'.
     *
     * @param target Text to modify.
     * @param pattern Pattern to find and replace.
     * @param value Data value to use as replacement.
     * @return The text, after replacing the pattern, if appropriate.
     */
    private static String tryReplaceValue(String target, String pattern, DataValue value) {
        if (target.contains(pattern)) {
            return target.replace(pattern, value.getData());
        }
        return target;
    }

    /**
     * Try to replace a data reference 'pattern' in the text 'target', with the data value in 'exprCode'.
     *
     * @param target Text to modify.
     * @param pattern Pattern to find and replace.
     * @param value Data value to use as replacement.
     * @return The text, after replacing the pattern, if appropriate.
     */
    private static String tryReplaceReference(String target, String pattern, DataValue value) {
        if (target.contains(pattern)) {
            return target.replace(pattern, value.getReference());
        }
        return target;
    }

    /**
     * Generate a function call in the target language using the supplied pattern.
     *
     * @param args Arguments of the function being called.
     * @param pattern Pattern to use in the conversion. Contains '${args}' where the translated argument value list is
     *     expected.
     * @param dest Storage destination of the result.
     * @param ctxt Code context of the expression.
     * @return The generated expression.
     */
    public static ExprCode convertFunctionCallPattern(String pattern, List<Expression> args, Destination dest,
            CodeContext ctxt)
    {
        // Convert the arguments.
        ExprCode result = new ExprCode();
        StringBuilder argsList = new StringBuilder();
        boolean first = true;
        for (Expression arg: args) {
            ExprCode argCode = ctxt.exprToTarget(arg, null);
            result.add(argCode);
            if (!first) {
                argsList.append(", ");
            }
            first = false;
            argsList.append(argCode.getData());
        }

        pattern = pattern.replace("${args}", argsList.toString());
        result.setDestination(dest);
        result.setDataValue(new JavaDataValue(pattern));
        return result;
    }
}
