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

package org.eclipse.escet.chi.codegen.types;

import static org.eclipse.escet.chi.codegen.Constants.getClassname;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.makeExpression;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.chi.codegen.CodeGeneratorContext;
import org.eclipse.escet.chi.codegen.Constants;
import org.eclipse.escet.chi.codegen.expressions.CodeExpression;
import org.eclipse.escet.chi.codegen.expressions.ExpressionBase;
import org.eclipse.escet.chi.codegen.expressions.SimpleExpression;
import org.eclipse.escet.chi.codegen.java.JavaFile;
import org.eclipse.escet.chi.metamodel.chi.BinaryExpression;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.IntNumber;
import org.eclipse.escet.chi.metamodel.chi.UnaryExpression;
import org.eclipse.escet.common.java.Assert;

/** Class representing the integer data type in the code generator. */
public class IntTypeID extends PrimitiveTypeID {
    /** Constructor of the {@link IntTypeID} class. */
    public IntTypeID() {
        super(TypeKind.INT);
    }

    @Override
    public String getTypeText() {
        return "int";
    }

    @Override
    public String getJavaType() {
        return "int";
    }

    @Override
    public String getJavaClassType() {
        return "Integer";
    }

    @Override
    public String getStaticReadFuncName() {
        return Constants.READ_INT_FQM;
    }

    @Override
    public String getWriteName(String stream, String val, JavaFile jf) {
        Assert.check(isPrintable());

        jf.addImport(Constants.WRITE_INT_FQM, true);
        return fmt("%s(%s, %s);", getClassname(Constants.WRITE_INT_FQM), stream, val);
    }

    @Override
    public String getToString(String val, JavaFile jf) {
        return fmt("String.valueOf(%s)", val);
    }

    @Override
    public String getHashCodeName(String val, JavaFile jf) {
        return val;
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        return "0";
    }

    @Override
    public String getSimplestJavaValue() {
        return "0";
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        // integer literal
        if (expr instanceof IntNumber) {
            IntNumber num = (IntNumber)expr;
            return new SimpleExpression(num.getValue(), num);
        }
        // Binary expression
        if (expr instanceof BinaryExpression) {
            BinaryExpression binexpr = (BinaryExpression)expr;
            ExpressionBase left, right;
            left = convertExpression(binexpr.getLeft(), ctxt, currentFile);
            right = convertExpression(binexpr.getRight(), ctxt, currentFile);
            String lv = left.getValue();
            String rv = right.getValue();

            String text;
            switch (binexpr.getOp()) {
                case ADDITION:
                    text = fmt("(%s) + (%s)", lv, rv);
                    break;

                case FLOOR_DIVISION:
                    currentFile.addImport(Constants.DIV_FQM, true);
                    text = fmt("floorDivision(%s, %s)", lv, rv);
                    break;

                case MODULUS:
                    currentFile.addImport(Constants.MOD_FQM, true);
                    text = fmt("modulus(%s, %s)", lv, rv);
                    break;

                case MULTIPLICATION:
                    text = fmt("(%s) * (%s)", lv, rv);
                    break;

                case SUBTRACTION:
                    text = fmt("(%s) - (%s)", lv, rv);
                    break;

                default:
                    Assert.fail("Unexpected binary operator " + binexpr.getOp().toString()
                            + " in IntTypeID.convertExprNode");
                    return null;
            }

            if (!left.getCode().isEmpty() || !right.getCode().isEmpty()) {
                List<String> lines = list();
                lines.addAll(left.getCode());
                lines.addAll(right.getCode());
                return new CodeExpression(lines, text, expr);
            }
            return new SimpleExpression(text, expr);
        }

        // Unary expression -, +
        if (expr instanceof UnaryExpression) {
            UnaryExpression unexpr = (UnaryExpression)expr;
            switch (unexpr.getOp()) {
                case NEGATE: { // -
                    ExpressionBase chExpr = convertExpression(unexpr.getChild(), ctxt, currentFile);
                    return makeExpression(chExpr.getCode(), "-(" + chExpr.getValue() + ")", expr);
                }

                case PLUS: // +
                    return convertExpression(unexpr.getChild(), ctxt, currentFile);

                default:
                    Assert.fail(
                            "Unexpected unary operator " + unexpr.getOp().toString() + " in IntTypeID.convertExprNode");
                    return null;
            }
        }

        Assert.fail("Unexpected expression in convertExprNode(" + expr.toString() + "): kind=" + kind.toString());
        return null;
    }
}
