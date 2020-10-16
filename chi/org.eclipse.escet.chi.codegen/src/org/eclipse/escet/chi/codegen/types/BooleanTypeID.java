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
import org.eclipse.escet.chi.metamodel.chi.BoolLiteral;
import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.UnaryExpression;
import org.eclipse.escet.common.java.Assert;

/** Representation of the boolean data type. */
public class BooleanTypeID extends PrimitiveTypeID {
    /** Constructor for the {@link BooleanTypeID} class. */
    public BooleanTypeID() {
        super(TypeKind.BOOL);
    }

    @Override
    public String getTypeText() {
        return "bool";
    }

    @Override
    public String getJavaType() {
        return "boolean";
    }

    @Override
    public String getJavaClassType() {
        return "Boolean";
    }

    @Override
    public String getStaticReadFuncName() {
        return Constants.READ_BOOL_FQM;
    }

    @Override
    public String getWriteName(String stream, String val, JavaFile jf) {
        Assert.check(isPrintable());

        jf.addImport(Constants.WRITE_BOOL_FQM, true);
        return fmt("%s(%s, %s);", getClassname(Constants.WRITE_BOOL_FQM), stream, val);
    }

    @Override
    public String getToString(String val, JavaFile jf) {
        return fmt("String.valueOf(%s)", val);
    }

    @Override
    public String getHashCodeName(String val, JavaFile jf) {
        return "(" + val + ") ? 0 : 1";
    }

    @Override
    public String getEqual(String lhs, String rhs) {
        return generateEqual(lhs, rhs, true);
    }

    @Override
    public String getUnequal(String lhs, String rhs) {
        return generateEqual(lhs, rhs, false);
    }

    @Override
    public String getEmptyValue(JavaFile jf) {
        return "false";
    }

    @Override
    public String getSimplestJavaValue() {
        return "false";
    }

    @Override
    public ExpressionBase convertExprNode(Expression expr, CodeGeneratorContext ctxt, JavaFile javaFile) {
        // Bool literal
        if (expr instanceof BoolLiteral) {
            BoolLiteral bl = (BoolLiteral)expr;
            String text = (bl.isValue()) ? "true" : "false";
            return new SimpleExpression(text, expr);
        }
        // Binary expression
        if (expr instanceof BinaryExpression) {
            BinaryExpression binexpr = (BinaryExpression)expr;
            ExpressionBase left, right;
            left = convertExpression(binexpr.getLeft(), ctxt, javaFile);
            right = convertExpression(binexpr.getRight(), ctxt, javaFile);

            String text;
            switch (binexpr.getOp()) {
                case CONJUNCTION: // and
                    text = fmt("(%s) && (%s)", left.getValue(), right.getValue());
                    break;

                case DISJUNCTION: // or
                    text = fmt("(%s) || (%s)", left.getValue(), right.getValue());
                    break;

                case EQUAL:
                    return equalityCompare(binexpr, left, right, true, ctxt);
                case NOT_EQUAL:
                    return equalityCompare(binexpr, left, right, false, ctxt);

                case LESS_THAN:
                case LESS_EQUAL:
                case GREATER_THAN:
                case GREATER_EQUAL:
                    return orderedCompare(binexpr, left, right, ctxt);

                default:
                    Assert.fail("Unexpected binary operator " + binexpr.getOp().toString()
                            + " in BooleanTypeID.convertExprNode");
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
        // Unary expression
        if (expr instanceof UnaryExpression) {
            UnaryExpression unexpr = (UnaryExpression)expr;
            ExpressionBase child = convertExpression(unexpr.getChild(), ctxt, javaFile);
            switch (unexpr.getOp()) {
                case INVERSE: // not
                    return makeExpression(child.getCode(), "!(" + child.getValue() + ")", expr);

                default:
                    Assert.fail("Unexpected unary operator " + unexpr.getOp().toString()
                            + " in BooleanTypeID.convertExprNode");
                    return null;
            }
        }
        Assert.fail("Unexpected expression in convertExprNode(" + expr.toString() + "): kind=" + kind.toString());
        return null;
    }

    /**
     * Generate an expression to test two values for equality or in-equality.
     *
     * @param binexpr Binary expression containing the (in)equality.
     * @param trLeft Translated left child.
     * @param trRight Translated right child.
     * @param isEqual Expression should test for equality.
     * @param ctxt Code generator context.
     * @return Generated (in)equality expression.
     */
    private ExpressionBase equalityCompare(BinaryExpression binexpr, ExpressionBase trLeft, ExpressionBase trRight,
            boolean isEqual, CodeGeneratorContext ctxt)
    {
        TypeID leftTid = createTypeID(binexpr.getLeft().getType(), ctxt);
        String text;
        if (isEqual) {
            text = leftTid.getEqual(trLeft.getValue(), trRight.getValue());
        } else {
            text = leftTid.getUnequal(trLeft.getValue(), trRight.getValue());
        }

        if (!trLeft.getCode().isEmpty() || !trRight.getCode().isEmpty()) {
            List<String> lines = list();
            lines.addAll(trLeft.getCode());
            lines.addAll(trRight.getCode());
            return new CodeExpression(lines, text, binexpr);
        }
        return new SimpleExpression(text, binexpr);
    }

    /**
     * Generate an expression to test two values for their relative order.
     *
     * @param binexpr Binary expression containing the comparison expression.
     * @param trLeft Translated left child.
     * @param trRight Translated right child.
     * @param ctxt Code generator context.
     * @return Generated compare expression.
     */
    private ExpressionBase orderedCompare(BinaryExpression binexpr, ExpressionBase trLeft, ExpressionBase trRight,
            CodeGeneratorContext ctxt)
    {
        String text;
        switch (binexpr.getOp()) {
            case LESS_THAN:
                text = "<";
                break;
            case LESS_EQUAL:
                text = "<=";
                break;
            case GREATER_THAN:
                text = ">";
                break;
            case GREATER_EQUAL:
                text = ">=";
                break;
            default:
                Assert.fail();
                text = "???";
        }

        TypeID leftTid = createTypeID(binexpr.getLeft().getType(), ctxt);
        if (leftTid.kind == TypeKind.STRING) {
            // String compare.
            text = fmt("(%s).compareTo(%s) %s 0", trLeft.getValue(), trRight.getValue(), text);
        } else {
            // Numeric compare.
            text = fmt("(%s) %s (%s)", trLeft.getValue(), text, trRight.getValue());
        }
        if (!trLeft.getCode().isEmpty() || !trRight.getCode().isEmpty()) {
            List<String> lines = list();
            lines.addAll(trLeft.getCode());
            lines.addAll(trRight.getCode());
            return new CodeExpression(lines, text, binexpr);
        }
        return new SimpleExpression(text, binexpr);
    }

    /**
     * Detect and handle the simple case of comparing with 'true' or 'false'. Call this function twice, once without
     * swapping the arguments and once with swapping.
     *
     * @param lhs Left-hand side of the test.
     * @param rhs Right-hand side of the test.
     * @param equal Test for equality ({@code false} means test for inequality).
     * @return Code to execute in case a simple compare is found, else {@code null} is returned.
     */
    private String generateSimpleEquality(String lhs, String rhs, boolean equal) {
        if (lhs.equals("true")) {
            if (equal) {
                return rhs;
            } else {
                return fmt("!(%s)", rhs);
            }
        } else if (lhs.equals("false")) {
            if (equal) {
                return fmt("!(%s)", rhs);
            } else {
                return rhs;
            }
        }
        return null;
    }

    /**
     * Generate an expression to compute equality of two boolean values.
     *
     * @param lhs Expression resulting in the first value.
     * @param rhs Expression resulting in the second value.
     * @param equal Compute equality (else in-equality is computed).
     * @return An expression to compute equality of two boolean values.
     */
    public String generateEqual(String lhs, String rhs, boolean equal) {
        String line = generateSimpleEquality(lhs, rhs, equal);
        if (line != null) {
            return line;
        }
        line = generateSimpleEquality(rhs, lhs, equal);
        if (line != null) {
            return line;
        }

        // Non-simple boolean test.
        if (equal) {
            line = fmt("(%s) == (%s)", lhs, rhs);
        } else {
            line = fmt("(%s) != (%s)", lhs, rhs);
        }
        return line;
    }
}
