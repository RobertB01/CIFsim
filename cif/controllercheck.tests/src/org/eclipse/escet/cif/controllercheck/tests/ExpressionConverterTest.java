//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.tests;

import static org.eclipse.escet.cif.common.CifValueUtils.makeFalse;
import static org.eclipse.escet.cif.common.CifValueUtils.makeInt;
import static org.eclipse.escet.cif.common.CifValueUtils.makeTrue;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSwitchCase;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSwitchExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newVariableValue;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.CifVarInfoBuilder;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.ConvertExpression;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.IntegerValueCollection;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.MvSpecBuilder;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.VarInfo;
import org.junit.Test;

/** Tests of the expression convert. */
public class ExpressionConverterTest {
    /** Lower value for integer variables in the test. */
    public static final Integer INT_LOWER = -5;

    /** Upper value for integer variables in the test. */
    public static final Integer INT_UPPER = 5;

    /** Boolean typed discrete variable. */
    public static DiscVariable b1;

    /** Boolean typed discrete variable. */
    public static DiscVariable b2;

    /** Ranged integer typed discrete variable. */
    public static DiscVariable i1;

    /** Ranged integer typed discrete variable. */
    public static DiscVariable i2;

    /** Corresponding MDD variable for b1. */
    public static VarInfo b1VarInfo;

    /** Corresponding MDD variable for b2. */
    public static VarInfo b2VarInfo;

    /** Corresponding MDD variable for i1. */
    public static VarInfo i1VarInfo;

    /** Corresponding MDD variable for i2. */
    public static VarInfo i2VarInfo;

    /** The MDD expression converter. */
    public static ConvertExpression convert;

    static {
        // Create cif variables.
        b1 = newDiscVariable("b1", null, newBoolType(), null);
        b2 = newDiscVariable("b1", null, newBoolType(), null);
        i1 = newDiscVariable("i1", null, newIntType(INT_LOWER, null, INT_UPPER), null);
        i2 = newDiscVariable("i2", null, newIntType(INT_LOWER, null, INT_UPPER), null);

        List<Declaration> variables = listc(4);
        variables.add(b1);
        variables.add(b2);
        variables.add(i1);
        variables.add(i2);

        // Construct the MDD tree.
        final int readIndex = 0;
        final int writeIndex = 1;
        CifVarInfoBuilder cifVarInfoBuilder = new CifVarInfoBuilder(2);
        cifVarInfoBuilder.addVariablesGroupOnVariable(variables);
        MvSpecBuilder builder = new MvSpecBuilder(cifVarInfoBuilder, readIndex, writeIndex);
        convert = builder.getPredicateConvertor();

        // Get the corresponding MDD variables.
        b1VarInfo = cifVarInfoBuilder.getVarInfos(b1)[readIndex];
        b2VarInfo = cifVarInfoBuilder.getVarInfos(b2)[readIndex];
        i1VarInfo = cifVarInfoBuilder.getVarInfos(i1)[readIndex];
        i2VarInfo = cifVarInfoBuilder.getVarInfos(i2)[readIndex];
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanBiConditional() {
        testBooleanOperator(BinaryOperator.BI_CONDITIONAL);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanConjunction() {
        testBooleanOperator(BinaryOperator.CONJUNCTION);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanDisjunction() {
        testBooleanOperator(BinaryOperator.DISJUNCTION);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanImplication() {
        testBooleanOperator(BinaryOperator.IMPLICATION);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanEqual() {
        testBooleanOperator(BinaryOperator.EQUAL);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanUnequal() {
        testBooleanOperator(BinaryOperator.UNEQUAL);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerEqual() {
        testIntegerOperator(BinaryOperator.EQUAL);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerUnequal() {
        testIntegerOperator(BinaryOperator.UNEQUAL);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerGreaterEqual() {
        testIntegerOperator(BinaryOperator.GREATER_EQUAL);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerGreaterThan() {
        testIntegerOperator(BinaryOperator.GREATER_THAN);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerLessEqual() {
        testIntegerOperator(BinaryOperator.LESS_EQUAL);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerLessThan() {
        testIntegerOperator(BinaryOperator.LESS_THAN);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerAddition() {
        testIntegerOperator(BinaryOperator.ADDITION);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerSubtraction() {
        testIntegerOperator(BinaryOperator.SUBTRACTION);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerDivision() {
        testIntegerOperator(BinaryOperator.INTEGER_DIVISION);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus() {
        testIntegerOperator(BinaryOperator.MODULUS);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerMultiplication() {
        testIntegerOperator(BinaryOperator.MULTIPLICATION);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIfExpression() {
        // Create expression 'if b1: 0 elif b2: 1 else 2 end'.
        DiscVariableExpression b1Expr = newDiscVariableExpression(null, newBoolType(), b1);
        DiscVariableExpression b2Expr = newDiscVariableExpression(null, newBoolType(), b2);

        ElifExpression elifExpr = newElifExpression(list(b2Expr), null, makeInt(1));
        IfExpression ifExpr = newIfExpression(list(elifExpr), makeInt(2), list(b1Expr), null, makeInt(0), newIntType());

        // Convert expression.
        IntegerValueCollection binExprIVC = convert.convert(ifExpr);

        // Compare result.
        compareBinExprResult(binExprIVC, ifExpr, b1, b2, b1VarInfo, b2VarInfo, 0, 1);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSwitchExpression() {
        // Create expression 'switch i1: case 0: i2 else 1 end'.
        DiscVariableExpression i1Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i1);
        DiscVariableExpression i2Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i2);

        SwitchCase switchCase0 = newSwitchCase(makeInt(0), null, i2Expr);
        SwitchCase switchCaseElse = newSwitchCase(null, null, makeInt(1));
        SwitchExpression switchExpr = newSwitchExpression(list(switchCase0, switchCaseElse), null, newIntType(), i1Expr);

        // Convert expression.
        IntegerValueCollection binExprIVC = convert.convert(switchExpr);

        // Compare result.
        compareBinExprResult(binExprIVC, switchExpr, i1, i2, i1VarInfo, i2VarInfo, INT_LOWER, INT_UPPER);
    }

    /**
     * Creates a binary expression 'b1 binOp b2', with b1 and b2 Boolean typed discrete variables and binOp the supplied
     * binary operator. The binary expression is converted to a MDD. The conversion result is compared with the expected
     * result, obtained from evaluating the cif expression.
     *
     * @param binOperator The binary operator to use.
     */
    public void testBooleanOperator(BinaryOperator binOperator) {
        // Create expression 'b1 binOperator b2'.
        DiscVariableExpression b1Expr = newDiscVariableExpression(null, newBoolType(), b1);
        DiscVariableExpression b2Expr = newDiscVariableExpression(null, newBoolType(), b2);
        BinaryExpression binExpr = newBinaryExpression(b1Expr, binOperator, null, b2Expr, newBoolType());

        // Convert expression.
        IntegerValueCollection binExprIVC = convert.convert(binExpr);

        // Compare result.
        compareBinExprResult(binExprIVC, binExpr, b1, b2, b1VarInfo, b2VarInfo, 0, 1);
    }

    /**
     * Creates a binary expression 'i1 binOp i2', with i1 and i2 (ranged) integer typed discrete variables and binOp the
     * supplied binary operator. The binary expression is converted to a MDD. The conversion result is compared with the
     * expected result, obtained from evaluating the cif expression.
     *
     * @param binOperator The binary operator to use.
     */
    public void testIntegerOperator(BinaryOperator binOperator) {
        // Create expression 'i1 binOperator i2'.
        DiscVariableExpression i1Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i1);
        DiscVariableExpression i2Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i2);
        BinaryExpression binExpr = newBinaryExpression(i1Expr, binOperator, null, i2Expr, newIntType());

        // Convert expression.
        IntegerValueCollection binExprIVC = convert.convert(binExpr);

        // Compare result.
        compareBinExprResult(binExprIVC, binExpr, i1, i2, i1VarInfo, i2VarInfo, INT_LOWER, INT_UPPER);
    }

    /**
     * Compares the MDD conversion of a binary expression with the expected result for a range of values (between lower
     * and upper).
     *
     * @param expression The converted MDD expression.
     * @param binExpr The original cif expression.
     * @param var1 The left-hand side variable.
     * @param var2 The right-hand side variable.
     * @param var1Info The converted left-hand side variable.
     * @param var2Info The converted right-hand side variable.
     * @param lower The lowest possible value of the variable, either 0 for Booleans or `lower` for ranged integers
     * @param upper The highest possible value of the variable, either 1 for Booleans or `upper` for ranged integers.
     */
    public void compareBinExprResult(IntegerValueCollection expression, Expression binExpr, DiscVariable var1,
            DiscVariable var2, VarInfo var1Info, VarInfo var2Info, int lower, int upper)
    {
        for (int var1Value = lower; var1Value < upper + 1; var1Value++) {
            for (int var2Value = lower; var2Value < upper + 1; var2Value++) {
                // Set the (initial) value of the discrete variables.
                Expression var1ValueExpr;
                Expression var2ValueExpr;
                if (var1.getType() instanceof BoolType && var2.getType() instanceof BoolType) {
                    var1ValueExpr = var1Value == 1 ? makeTrue() : makeFalse();
                    var2ValueExpr = var2Value == 1 ? makeTrue() : makeFalse();
                } else {
                    var1ValueExpr = makeInt(var1Value);
                    var2ValueExpr = makeInt(var2Value);
                }

                VariableValue vvalue1 = newVariableValue(null, list(var1ValueExpr));
                VariableValue vvalue2 = newVariableValue(null, list(var2ValueExpr));
                var1.setValue(vvalue1);
                var2.setValue(vvalue2);

                // Determine the expected result, obtained from evaluating the cif expression.
                Object evalResult;
                try {
                    evalResult = CifEvalUtils.eval(binExpr, true);
                } catch (CifEvalException e) {
                    // This exception is thrown for division by zero or modulus of zero. In that case, the MDD will
                    // be null.
                    evalResult = null;
                }

                Integer expectedResult;
                if (evalResult == null) {
                    expectedResult = null;
                } else if (evalResult instanceof Integer) {
                    expectedResult = (Integer)evalResult;
                } else {
                    expectedResult = (Boolean)evalResult ? 1 : 0;
                }

                // MDD result.
                Map<VarInfo, Integer> valuations = mapc(2);
                valuations.put(var1Info, var1Value);
                valuations.put(var2Info, var2Value);

                // For all possible results, only the value node of the expected result should evaluate to 'true', all
                // other nodes should evaluate to 'false'. If the result cannot be computed, all value nodes should
                // evaluate to 'false'.
                int correctValuesFound = 0;
                for (Entry<Integer, Node> valueNode: expression.valueNodes.entrySet()) {
                    if (valueNode.getKey() == expectedResult) {
                        assertTrue(valueNode.getValue().evaluate(valuations));
                        correctValuesFound++;
                    } else {
                        assertFalse(valueNode.getValue().evaluate(valuations));
                    }
                }

                assertTrue(correctValuesFound == 1 || expectedResult == null);
            }
        }
    }
}
