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

import static org.eclipse.escet.chi.codegen.Constants.getClassname;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.convertExpression;
import static org.eclipse.escet.chi.codegen.expressions.ExpressionBase.makeExpression;
import static org.eclipse.escet.chi.codegen.types.TypeIDCreation.createTypeID;
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
import org.eclipse.escet.chi.metamodel.chi.RealNumber;
import org.eclipse.escet.chi.metamodel.chi.TimeLiteral;
import org.eclipse.escet.chi.metamodel.chi.UnaryExpression;
import org.eclipse.escet.common.java.Assert;

/** Class representing a Chi real data type in the code generator. */
public class RealTypeID extends PrimitiveTypeID {
    /** Constructor for the {@link RealTypeID} class. */
    public RealTypeID() {
        super(TypeKind.REAL);
    }

    @Override
    public String getTypeText() {
        return "real";
    }

    @Override
    public String getJavaType() {
        return "double";
    }

    @Override
    public String getJavaClassType() {
        return "Double";
    }

    @Override
    public String getStaticReadFuncName() {
        return Constants.READ_REAL_FQM;
    }

    @Override
    public String getWriteName(String stream, String val, JavaFile jf) {
        Assert.check(isPrintable());

        jf.addImport(Constants.WRITE_REAL_FQM, true);
        return fmt("%s(%s, %s);", getClassname(Constants.WRITE_REAL_FQM), stream, val);
    }

    @Override
    public String getToString(String val, JavaFile jf) {
        return fmt("String.valueOf(%s)", val);
    }

    @Override
    public String getHashCodeName(String val, JavaFile jf) {
        return "(int)Double.doubleToLongBits(" + val + ")";
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        return "0.0";
    }

    @Override
    public String getSimplestJavaValue() {
        return "0.0";
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile currentFile) {
        // Real number literal
        if (expr instanceof RealNumber) {
            RealNumber num = (RealNumber)expr;
            // Convert literal real number to a representation acceptable to
            // the Java compiler. Overflow has been caught by the type checker,
            // underflow is converted here.
            String text = Double.valueOf(num.getValue()).toString();
            return new SimpleExpression(text, num);
        }
        // Time literal
        if (expr instanceof TimeLiteral) {
            return new SimpleExpression("chiCoordinator.getCurrentTime()", expr);
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

                case DIVISION: {
                    TypeID ltid = createTypeID(binexpr.getLeft().getType(), ctxt);
                    String lcast = (ltid.kind != TypeKind.REAL) ? "(double)" : "";
                    TypeID rtid = createTypeID(binexpr.getRight().getType(), ctxt);
                    String rcast = (rtid.kind != TypeKind.REAL) ? "(double)" : "";
                    text = fmt("(%s%s) / (%s%s)", lcast, lv, rcast, rv);
                    break;
                }

                case MULTIPLICATION:
                    text = fmt("(%s) * (%s)", lv, rv);
                    break;

                case POWER: {
                    TypeID rtid = createTypeID(binexpr.getRight().getType(), ctxt);
                    if (rtid.kind == TypeKind.INT) {
                        String s = Constants.INT_POWER_FQM;
                        currentFile.addImport(s, true);
                        text = fmt("intPower(%s, %s)", lv, rv);
                    } else {
                        currentFile.addImport("java.lang.Math.pow", true);
                        text = fmt("Math.pow(%s, %s)", lv, rv);
                    }
                    break;
                }

                case SUBTRACTION:
                    text = fmt("(%s) - (%s)", lv, rv);
                    break;

                default:
                    Assert.fail("Unexpected binary operator " + binexpr.getOp().toString()
                            + " in RealTypeID.convertExprNode");
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
                    Expression child = unexpr.getChild();
                    ExpressionBase chExpr = convertExpression(child, ctxt, currentFile);
                    return makeExpression(chExpr.getCode(), "-(" + chExpr.getValue() + ")", expr);
                }

                case PLUS: // +
                    return convertExpression(unexpr.getChild(), ctxt, currentFile);

                default:
                    Assert.fail("Unexpected unary operator " + unexpr.getOp().toString()
                            + " in RealTypeID.convertExprNode");
                    return null;
            }
        }

        Assert.fail("Unexpected expression in convertExprNode(" + expr.toString() + "): kind=" + kind.toString());
        return null;
    }
}
