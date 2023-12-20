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

package org.eclipse.escet.cif.codegen.javascript.typeinfos;

import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;
import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertFunctionCallPattern;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.javascript.JavaScriptDataValue;
import org.eclipse.escet.cif.codegen.typeinfos.StringTypeInfo;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.StringType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.FormatDecoder;
import org.eclipse.escet.common.java.FormatDescription;
import org.eclipse.escet.common.java.FormatDescription.Conversion;
import org.eclipse.escet.common.java.Strings;

/** JavaScript type information about the string type. */
public class JavaScriptStringTypeInfo extends StringTypeInfo {
    /** Name of the main object in the generated code. Is used as prefix to ensure fully-qualified variable names. */
    private final String prefix;

    /**
     * Constructor for {@link JavaScriptStringTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param prefix Name of the main object in the generated code. Is used as prefix to ensure fully-qualified variable
     *     names.
     */
    public JavaScriptStringTypeInfo(CifType cifType, String prefix) {
        super(cifType);
        this.prefix = prefix;
    }

    @Override
    public String getTargetType() {
        return "var";
    }

    @Override
    public void generateCode(CodeContext ctxt) {
        // Nothing to generate.
    }

    @Override
    public void storeValue(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("%s.%s = %s;", this.prefix, dest.getData(), sourceValue.getData());
    }

    @Override
    public void declareInit(CodeBox code, DataValue sourceValue, Destination dest) {
        code.add(dest.getCode());
        code.add("var %s = %s;", dest.getData(), sourceValue.getData());
    }

    @Override
    public ExprCode convertLiteral(String value, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(value));
        return result;
    }

    @Override
    public String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt) {
        // Use 'equalObjs' instead of '==' to avoid object equality for two String objects.
        if (binOp.equals(BinaryOperator.EQUAL)) {
            return fmt("%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
        } else if (binOp.equals(BinaryOperator.UNEQUAL)) {
            return fmt("!%sUtils.equalObjs(${left-value}, ${right-value})", ctxt.getPrefix());
        }

        throw new RuntimeException("Unexpected binary operator: " + str(binOp));
    }

    @Override
    public ExprCode convertConcatenation(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr,
                fmt("%sUtils.addString(${left-value}, ${right-value})", ctxt.getPrefix()), dest, ctxt);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof StringTypeInfo;
    }

    @Override
    public int hashCode() {
        return JavaScriptStringTypeInfo.class.hashCode();
    }

    @Override
    public ExprCode convertSizeStdLib(Expression expr, Destination dest, CodeContext ctxt) {
        return convertFunctionCallPattern(fmt("%sUtils.sizeString(${args})", ctxt.getPrefix()), list(expr), dest, ctxt);
    }

    @Override
    public ExprCode getProjectedValue(ExprCode childCode, ExprCode indexCode, Destination dest, CodeContext ctxt) {
        ExprCode result = new ExprCode();
        result.add(childCode);
        result.add(indexCode);
        String resultText = fmt("%sUtils.projectString(%s, %s)", ctxt.getPrefix(), childCode.getData(),
                indexCode.getData());
        result.setDestination(dest);
        result.setDataValue(new JavaScriptDataValue(resultText));
        return result;
    }

    @Override
    public ExprCode convertFormatFunction(String pattern, List<Expression> args, List<CifType> argTypes,
            Destination dest, CodeContext ctxt)
    {
        // See also CifMath for similar code.
        ExprCode result = new ExprCode();
        result.setDestination(dest);

        // Decode pattern.
        FormatDecoder decoder = new FormatDecoder();
        List<FormatDescription> parts = decoder.decode(pattern);

        // Process all parts.
        StringBuilder patternCode = new StringBuilder();
        List<String> argCodes = listc(parts.size());
        int implicitIndex = 0;
        for (FormatDescription part: parts) {
            // Literal.
            if (part.conversion == Conversion.LITERAL) {
                if (part.text.equals("%")) {
                    patternCode.append("%%");
                } else {
                    patternCode.append(part.text);
                }
                continue;
            }

            // Get 0-based index of specifier.
            int idx;
            if (!part.index.isEmpty()) {
                idx = part.getExplicitIndex() - 1;
            } else {
                idx = implicitIndex;
                implicitIndex++;
            }

            // Get argument code.
            ExprCode argCode = ctxt.exprToTarget(args.get(idx), null);
            result.add(argCode);
            String argText = argCode.getData();

            // Add to formatted result.
            switch (part.conversion) {
                case BOOLEAN:
                case INTEGER:
                case REAL: {
                    patternCode.append(part.toString(false));
                    argCodes.add(argText);
                    break;
                }

                case STRING: {
                    patternCode.append(part.toString(false));
                    CifType nt = argTypes.get(idx);
                    if (!(nt instanceof StringType)) {
                        argText = fmt("%sUtils.valueToStr(%s)", ctxt.getPrefix(), argText);
                    }
                    argCodes.add(argText);
                    break;
                }

                default:
                    String msg = "Unexpected: " + part.conversion;
                    throw new RuntimeException(msg);
            }
        }

        // Return actual code for the entire 'fmt' function call.
        StringBuilder rslt = new StringBuilder();
        rslt.append(fmt("%sUtils.fmt(", ctxt.getPrefix()));
        rslt.append(Strings.stringToJava(patternCode.toString()));
        if (!argCodes.isEmpty()) {
            rslt.append(", ");
        }
        rslt.append(String.join(", ", argCodes));
        rslt.append(")");

        result.setDataValue(new JavaScriptDataValue(rslt.toString()));
        return result;
    }
}