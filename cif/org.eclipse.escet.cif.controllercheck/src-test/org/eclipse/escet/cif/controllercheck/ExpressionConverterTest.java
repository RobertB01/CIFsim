//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck;

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
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newUnaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newVariableValue;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

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
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.VarInfo;
import org.junit.Before;
import org.junit.Test;

/** Tests of the expression converter. */
public class ExpressionConverterTest {
    /** Lower value for integer variables in the test. */
    private static final Integer INT_LOWER = -5;

    /** Upper value for integer variables in the test. */
    private static final Integer INT_UPPER = 5;

    /** Boolean typed discrete variable. */
    private DiscVariable b1;

    /** Boolean typed discrete variable. */
    private DiscVariable b2;

    /** Ranged integer typed discrete variable. */
    private DiscVariable i1;

    /** Ranged integer typed discrete variable. */
    private DiscVariable i2;

    /** Corresponding MDD variable for b1. */
    private VarInfo b1VarInfo;

    /** Corresponding MDD variable for b2. */
    private VarInfo b2VarInfo;

    /** Corresponding MDD variable for i1. */
    private VarInfo i1VarInfo;

    /** Corresponding MDD variable for i2. */
    private VarInfo i2VarInfo;

    /** The MDD expression converter. */
    private ConvertExpression convert;

    @Before
    @SuppressWarnings("javadoc")
    public void beforeTest() {
        // Create CIF variables.
        b1 = newDiscVariable("b1", null, newBoolType(), null);
        b2 = newDiscVariable("b1", null, newBoolType(), null);
        i1 = newDiscVariable("i1", null, newIntType(INT_LOWER, null, INT_UPPER), null);
        i2 = newDiscVariable("i2", null, newIntType(INT_LOWER, null, INT_UPPER), null);

        List<Declaration> variables = listc(4);
        variables.add(b1);
        variables.add(b2);
        variables.add(i1);
        variables.add(i2);

        // Get MDD expression converter.
        final int readIndex = 0;
        final int writeIndex = 1;
        CifVarInfoBuilder cifVarInfoBuilder = new CifVarInfoBuilder(2);
        cifVarInfoBuilder.addVariablesGroupOnVariable(variables);
        MvSpecBuilder builder = new MvSpecBuilder(cifVarInfoBuilder, readIndex, writeIndex);
        convert = builder.getExpressionConvertor();

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
    public void testInverse() {
        // Create expression 'not b1'.
        DiscVariableExpression b1Expr = newDiscVariableExpression(null, newBoolType(), b1);

        UnaryExpression unaryExpr = newUnaryExpression(b1Expr, UnaryOperator.INVERSE, null, newBoolType());

        // Convert expression.
        IntegerValueCollection unaryExprIVC = convert.convert(unaryExpr);

        // Compare result.
        compareExprResult(unaryExprIVC, unaryExpr, b1, b2, b1VarInfo, b2VarInfo, 0, 1);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testNegate() {
        // Create expression '-i1'.
        DiscVariableExpression i1Expr = newDiscVariableExpression(null, newBoolType(), i1);

        UnaryExpression unaryExpr = newUnaryExpression(i1Expr, UnaryOperator.NEGATE, null, newIntType());

        // Convert expression.
        IntegerValueCollection unaryExprIVC = convert.convert(unaryExpr);

        // Compare result.
        compareExprResult(unaryExprIVC, unaryExpr, i1, i2, i1VarInfo, i2VarInfo, INT_LOWER, INT_UPPER);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testPlus() {
        // Create expression '+i1'.
        DiscVariableExpression i1Expr = newDiscVariableExpression(null, newBoolType(), i1);

        UnaryExpression unaryExpr = newUnaryExpression(i1Expr, UnaryOperator.PLUS, null, newIntType());

        // Convert expression.
        IntegerValueCollection unaryExprIVC = convert.convert(unaryExpr);

        // Compare result.
        compareExprResult(unaryExprIVC, unaryExpr, i1, i2, i1VarInfo, i2VarInfo, INT_LOWER, INT_UPPER);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTrue() {
        // Create expression 'true'.
        BoolExpression boolExpr = makeTrue();

        // Convert expression.
        IntegerValueCollection boolExprIVC = convert.convert(boolExpr);

        // Compare result.
        compareExprResult(boolExprIVC, boolExpr, b1, b2, b1VarInfo, b2VarInfo, 0, 1);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFalse() {
        // Create expression 'true'.
        BoolExpression boolExpr = makeFalse();

        // Convert expression.
        IntegerValueCollection boolExprIVC = convert.convert(boolExpr);

        // Compare result.
        compareExprResult(boolExprIVC, boolExpr, b1, b2, b1VarInfo, b2VarInfo, 0, 1);
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
        IntegerValueCollection ifExprIVC = convert.convert(ifExpr);

        // Compare result.
        compareExprResult(ifExprIVC, ifExpr, b1, b2, b1VarInfo, b2VarInfo, 0, 1);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testSwitchExpression() {
        // Create expression 'switch i1: case 0: i2 else 1 end'.
        DiscVariableExpression i1Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i1);
        DiscVariableExpression i2Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i2);

        SwitchCase switchCase0 = newSwitchCase(makeInt(0), null, i2Expr);
        SwitchCase switchCaseElse = newSwitchCase(null, null, makeInt(1));
        SwitchExpression switchExpr = newSwitchExpression(list(switchCase0, switchCaseElse), null, newIntType(),
                i1Expr);

        // Convert expression.
        IntegerValueCollection switchExprIVC = convert.convert(switchExpr);

        // Compare result.
        compareExprResult(switchExprIVC, switchExpr, i1, i2, i1VarInfo, i2VarInfo, INT_LOWER, INT_UPPER);
    }

    /**
     * Creates a binary expression 'b1 binOperator b2', with b1 and b2 Boolean typed discrete variables and
     * {@code binOperator} the supplied binary operator. The binary expression is converted to a MDD. The conversion
     * result is compared with the expected result, obtained from evaluating the CIF expression.
     *
     * @param binOperator The binary operator to use.
     */
    private void testBooleanOperator(BinaryOperator binOperator) {
        // Create expression 'b1 binOperator b2'.
        DiscVariableExpression b1Expr = newDiscVariableExpression(null, newBoolType(), b1);
        DiscVariableExpression b2Expr = newDiscVariableExpression(null, newBoolType(), b2);
        BinaryExpression binExpr = newBinaryExpression(b1Expr, binOperator, null, b2Expr, newBoolType());

        // Convert expression.
        IntegerValueCollection binExprIVC = convert.convert(binExpr);

        // Compare result.
        compareExprResult(binExprIVC, binExpr, b1, b2, b1VarInfo, b2VarInfo, 0, 1);
    }

    /**
     * Creates a binary expression 'i1 binOperator i2', with i1 and i2 (ranged) integer typed discrete variables and
     * {@code binOperator} the supplied binary operator. The binary expression is converted to a MDD. The conversion
     * result is compared with the expected result, obtained from evaluating the CIF expression.
     *
     * @param binOperator The binary operator to use.
     */
    private void testIntegerOperator(BinaryOperator binOperator) {
        // Create expression 'i1 binOperator i2'.
        DiscVariableExpression i1Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i1);
        DiscVariableExpression i2Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i2);
        BinaryExpression binExpr = newBinaryExpression(i1Expr, binOperator, null, i2Expr, newIntType());

        // Convert expression.
        IntegerValueCollection binExprIVC = convert.convert(binExpr);

        // Compare result.
        compareExprResult(binExprIVC, binExpr, i1, i2, i1VarInfo, i2VarInfo, INT_LOWER, INT_UPPER);
    }

    /**
     * Compares the MDD conversion of a CIF expression with two variables, with the expected result for a range of
     * values (between lower and upper).
     *
     * @param expression The converted MDD expression.
     * @param cifExpr The original CIF expression.
     * @param var1 The first variable.
     * @param var2 The second variable.
     * @param var1Info The converted first variable.
     * @param var2Info The converted second variable.
     * @param lower The lowest possible value of the variables, either {@code 0} for Booleans or the lower bound for
     *     ranged integers.
     * @param upper The highest possible value of the variables, either {@code 1} for Booleans or the upper bound for
     *     ranged integers.
     */
    private void compareExprResult(IntegerValueCollection expression, Expression cifExpr, DiscVariable var1,
            DiscVariable var2, VarInfo var1Info, VarInfo var2Info, int lower, int upper)
    {
        for (int var1Value = lower; var1Value <= upper; var1Value++) {
            for (int var2Value = lower; var2Value <= upper; var2Value++) {
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

                // Determine the expected result, obtained from evaluating the CIF expression.
                Object evalResult;
                try {
                    evalResult = CifEvalUtils.eval(cifExpr, true);
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
                Map<VarInfo, Integer> valuation = mapc(2);
                valuation.put(var1Info, var1Value);
                valuation.put(var2Info, var2Value);

                // For all possible results, only the value node of the expected result should evaluate to 'true', all
                // other nodes should evaluate to 'false'. If the result cannot be computed, all value nodes should
                // evaluate to 'false'.
                int correctValuesFound = 0;
                for (Entry<Integer, Node> valueNode: expression.valueNodes.entrySet()) {
                    if (Objects.equals(valueNode.getKey(), expectedResult)) {
                        assertTrue(valueNode.getValue().evaluate(valuation));
                        correctValuesFound++;
                    } else {
                        assertFalse(valueNode.getValue().evaluate(valuation));
                    }
                }

                assertTrue(correctValuesFound == 1 || expectedResult == null);
            }
        }
    }
}
