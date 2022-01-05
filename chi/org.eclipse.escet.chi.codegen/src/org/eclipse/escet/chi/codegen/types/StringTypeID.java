//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.chi.codegen.Constants.DATA_SUPPORT_FQC;
import static org.eclipse.escet.chi.codegen.Constants.getClassname;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.makeExpression;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.stringToJava;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.expressions.SimpleExpression;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.SliceExpression;
import org.eclipse.escet.chi.metamodel.chi.StringLiteral;
import org.eclipse.escet.common.java.Assert;

/** Representation of the string type in the code generator. */
public class StringTypeID extends StateLessObjectTypeID {
    /** Constructor of the {@link StringTypeID} class. */
    public StringTypeID() {
        super(false, TypeKind.STRING);
    }

    @Override
    public String getTypeText() {
        return "string";
    }

    @Override
    public String getJavaClassType() {
        return "String";
    }

    @Override
    public String getReadName(String stream, JavaFile jf) {
        Assert.check(isPrintable());

        String funcPath = Constants.READ_STRING_FQM;
        jf.addImport(funcPath, true);
        return fmt("%s(%s)", getClassname(funcPath), stream);
    }

    @Override
    public String getWriteName(String stream, String val, JavaFile jf) {
        Assert.check(isPrintable());

        jf.addImport(Constants.WRITE_STRING_FQM, true);
        return fmt("%s(%s, %s);", getClassname(Constants.WRITE_STRING_FQM), stream, val);
    }

    @Override
    public String getToString(String val, JavaFile jf) {
        return val;
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        return "\"\"";
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        // String literal
        if (expr instanceof StringLiteral) {
            StringLiteral txt = (StringLiteral)expr;
            String code = stringToJava(txt.getValue());
            return new SimpleExpression(code, txt);
        }
        // Binary expression
        if (expr instanceof BinaryExpression) {
            BinaryExpression binexpr = (BinaryExpression)expr;
            ExpressionBase left, right;
            left = convertExpression(binexpr.getLeft(), ctxt, currentFile);
            right = convertExpression(binexpr.getRight(), ctxt, currentFile);
            String lv = left.getValue();
            String rv = right.getValue();

            switch (binexpr.getOp()) {
                case ADDITION: {
                    List<String> lines = list();
                    lines.addAll(left.getCode());
                    lines.addAll(right.getCode());
                    String text = fmt("(%s + %s)", lv, rv);
                    return makeExpression(lines, text, expr);
                }

                case PROJECTION: {
                    List<String> lines = list();
                    lines.addAll(left.getCode());
                    lines.addAll(right.getCode());
                    String line = DATA_SUPPORT_FQC + ".stringProjection";
                    currentFile.addImport(line, true);
                    line = fmt("stringProjection(%s, %s)", lv, rv);
                    return makeExpression(lines, line, expr);
                }

                default:
                    Assert.fail("Unexpected binary operator " + binexpr.getOp().toString()
                            + " in StringTypeID.convertExprNode");
                    return null;
            }
        }

        if (expr instanceof SliceExpression) {
            SliceExpression se = (SliceExpression)expr;
            String srcStr, startStr, endStr, stepStr;
            ExpressionBase eb;
            List<String> lines = list();
            // Convert source expression.
            srcStr = ctxt.makeUniqueName("str");
            eb = convertExpression(se.getSource(), ctxt, currentFile);
            lines.addAll(eb.getCode());
            lines.add("String " + srcStr + " = " + eb.getValue() + ";");
            // Convert start expression.
            if (se.getStart() == null) {
                startStr = "0";
            } else {
                eb = convertExpression(se.getStart(), ctxt, currentFile);
                lines.addAll(eb.getCode());
                startStr = eb.getValue();
            }
            // Convert end expression.
            if (se.getEnd() == null) {
                endStr = srcStr + ".length()";
            } else {
                eb = convertExpression(se.getEnd(), ctxt, currentFile);
                lines.addAll(eb.getCode());
                endStr = eb.getValue();
            }
            // Convert step expression.
            if (se.getStep() == null) {
                stepStr = "1";
            } else {
                eb = convertExpression(se.getStep(), ctxt, currentFile);
                lines.addAll(eb.getCode());
                stepStr = eb.getValue();
            }
            String line = DATA_SUPPORT_FQC + ".stringSlice";
            currentFile.addImport(line, true);
            line = fmt("stringSlice(%s, %s, %s, %s)", srcStr, startStr, endStr, stepStr);
            return makeExpression(lines, line, expr);
        }

        Assert.fail("Unexpected expression in convertExprNode(" + expr.toString() + "): kind=" + kind.toString());
        return null;
    }
}
