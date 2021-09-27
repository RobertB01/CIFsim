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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariable;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newDiscVariableExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIntType;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.controllercheck.multivaluetrees.CifVarInfoBuilder;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.ConvertExpression;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.IntegerValueCollection;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.MvSpecBuilder;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;
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
        testBooleanOperator(BinaryOperator.BI_CONDITIONAL, biConditionalOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanConjunction() {
        testBooleanOperator(BinaryOperator.CONJUNCTION, conjunctionOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanDisjunction() {
        testBooleanOperator(BinaryOperator.DISJUNCTION, disjunctionOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanImplication() {
        testBooleanOperator(BinaryOperator.IMPLICATION, implicationOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanEqual() {
        testBooleanOperator(BinaryOperator.EQUAL, equalOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBooleanUnequal() {
        testBooleanOperator(BinaryOperator.UNEQUAL, unequalOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerEqual() {
        testIntegerOperator(BinaryOperator.EQUAL, equalOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerUnequal() {
        testIntegerOperator(BinaryOperator.UNEQUAL, unequalOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerGreaterEqual() {
        testIntegerOperator(BinaryOperator.GREATER_EQUAL, greaterEqualOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerGreaterThan() {
        testIntegerOperator(BinaryOperator.GREATER_THAN, greaterThanOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerLessEqual() {
        testIntegerOperator(BinaryOperator.LESS_EQUAL, lessEqualOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerLessThan() {
        testIntegerOperator(BinaryOperator.LESS_THAN, lessThanOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerAddition() {
        testIntegerOperator(BinaryOperator.ADDITION, additionOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerSubtraction() {
        testIntegerOperator(BinaryOperator.SUBTRACTION, subtractionOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerDivision() {
        testIntegerOperator(BinaryOperator.INTEGER_DIVISION, integerDivisionOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerModulus() {
        testIntegerOperator(BinaryOperator.MODULUS, modulusOp);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testIntegerMultiplication() {
        testIntegerOperator(BinaryOperator.MULTIPLICATION, multiplicationOp);
    }

    /**
     * Creates a binary expression 'b1 binOp b2', with b1 and b2 Boolean typed discrete variables and binOp the supplied
     * binary operator. The binary expression is converted to a MDD. The conversion result is compared with the expected
     * result, obtained from the supplied binary operation.
     *
     * @param binOperator The binary operator to use.
     * @param binOperation The binary operation from which the expected result is computed.
     */
    public void testBooleanOperator(BinaryOperator binOperator, BinaryOperation binOperation) {
        // Create expression 'b1 binOperator b2'.
        DiscVariableExpression b1Expr = newDiscVariableExpression(null, newBoolType(), b1);
        DiscVariableExpression b2Expr = newDiscVariableExpression(null, newBoolType(), b2);
        Expression binExpr = newBinaryExpression(b1Expr, binOperator, null, b2Expr, newBoolType());

        // Convert expression.
        IntegerValueCollection binExprIVC = convert.convert(binExpr);

        // Compare result.
        compareBinExprResult(binExprIVC, binOperation, b1VarInfo, b2VarInfo, 0, 1);
    }

    /**
     * Creates a binary expression 'i1 binOp i2', with i1 and i2 (ranged) integer typed discrete variables and binOp the
     * supplied binary operator. The binary expression is converted to a MDD. The conversion result is compared with the
     * expected result, obtained from the supplied binary operation.
     *
     * @param binOperator The binary operator to use.
     * @param binOperation The binary operation from which the expected result is computed.
     */
    public void testIntegerOperator(BinaryOperator binOperator, BinaryOperation binOperation) {
        // Create expression 'i1 binOperator i2'.
        DiscVariableExpression i1Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i1);
        DiscVariableExpression i2Expr = newDiscVariableExpression(null, newIntType(INT_LOWER, null, INT_UPPER), i2);
        Expression binExpr = newBinaryExpression(i1Expr, binOperator, null, i2Expr, newIntType());

        // Convert expression.
        IntegerValueCollection binExprIVC = convert.convert(binExpr);

        // Compare result.
        compareBinExprResult(binExprIVC, binOperation, i1VarInfo, i2VarInfo, INT_LOWER, INT_UPPER);
    }

    /**
     * Compares the MDD conversion of a binary expression with the expected result for a range of values (between lower
     * and upper).
     *
     * @param expression The MDD.
     * @param binOperation The binary operation from which the expected result is computed.
     * @param var1 The left-hand side variable.
     * @param var2 The right-hand side variable.
     * @param lower The lowest possible value of the variable, either 0 for Booleans or `lower` for ranged integers
     * @param upper The highest possible value of the variable, either 1 for Booleans or `upper` for ranged integers.
     */
    public void compareBinExprResult(IntegerValueCollection expression, BinaryOperation binOperation, VarInfo var1,
            VarInfo var2, int lower, int upper)
    {
        for (int var1Value = lower; var1Value < upper + 1; var1Value++) {
            for (int var2Value = lower; var2Value < upper + 1; var2Value++) {
                // Expected result, may be null if it cannot be computed (e.g., division by zero).
                Integer expectedResult = binOperation.perform(var1Value, var2Value);

                // MDD result.
                Map<VarInfo, Integer> valuations = mapc(2);
                valuations.put(var1, var1Value);
                valuations.put(var2, var2Value);

                // For all possible results, only the value node of the expected result should evaluate to 'true', all
                // other nodes should evaluate to 'false'. If the result cannot be computed, all value nodes should
                // evaluate to 'false'.
                int correctValueFound = 0;
                for (Entry<Integer, Node> valueNode: expression.valueNodes.entrySet()) {
                    if (valueNode.getKey() == expectedResult) {
                        assertTrue(valueNode.getValue().evaluate(valuations));
                        correctValueFound++;
                    } else {
                        assertFalse(valueNode.getValue().evaluate(valuations));
                    }
                }

                assertTrue(correctValueFound == 1 || expectedResult == null);
            }
        }
    }

    /** Abstraction of an operation performed on two integer values. */
    public interface BinaryOperation {
        /**
         * Perform a computation with both arguments and return the result.
         *
         * @param leftValue Left-hand side of the operation.
         * @param rightValue Right-hand side of the operation.
         * @return Result value of the operation, or {@code null} if it cannot be performed.
         */
        public Integer perform(Integer leftValue, Integer rightValue);
    }

    /** Bi-conditional operation class. */
    public class BiConditionalOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            Assert.check(leftValue == 0 || leftValue == 1);
            Assert.check(rightValue == 0 || rightValue == 1);
            return (leftValue == 0 && rightValue == 0) || (leftValue == 1 && rightValue == 1) ? 1 : 0;
        }
    }

    /** Conjunction operation class. */
    public class ConjunctionOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            Assert.check(leftValue == 0 || leftValue == 1);
            Assert.check(rightValue == 0 || rightValue == 1);
            return (leftValue == 1 && rightValue == 1) ? 1 : 0;
        }
    }

    /** Disjunction operation class. */
    public class DisjunctionOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            Assert.check(leftValue == 0 || leftValue == 1);
            Assert.check(rightValue == 0 || rightValue == 1);
            return (leftValue == 1 || rightValue == 1) ? 1 : 0;
        }
    }

    /** Implication operation class. */
    public class ImplicationOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            Assert.check(leftValue == 0 || leftValue == 1);
            Assert.check(rightValue == 0 || rightValue == 1);
            return (leftValue == 0 || rightValue == 1) ? 1 : 0;
        }
    }

    /** Equality operation class. */
    public class EqualOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return (leftValue == rightValue) ? 1 : 0;
        }
    }

    /** Un-equality operation class. */
    public class UnequalOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return (leftValue != rightValue) ? 1 : 0;
        }
    }

    /** Greater equal operation class. */
    public class GreaterEqualOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return (leftValue >= rightValue) ? 1 : 0;
        }
    }

    /** Greater than operation class. */
    public class GreaterThanOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return (leftValue > rightValue) ? 1 : 0;
        }
    }

    /** Less equal operation class. */
    public class LessEqualOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return (leftValue <= rightValue) ? 1 : 0;
        }
    }

    /** Less than operation class. */
    public class LessThanOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return (leftValue < rightValue) ? 1 : 0;
        }
    }

    /** Addition operation class. */
    public class AdditionOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return (leftValue + rightValue);
        }
    }

    /** Subtraction operation class. */
    public class SubtractionOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return (leftValue - rightValue);
        }
    }

    /** Integer division operation class. */
    public class IntegerDivisionOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            if (rightValue == 0) {
                return null;
            }
            return (leftValue / rightValue);
        }
    }

    /** Integer modulus operation class. */
    public class ModulusOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            if (rightValue == 0) {
                return null;
            }
            return (leftValue % rightValue);
        }
    }

    /** Multiplication operation class. */
    public class MultiplicationOperation implements BinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return (leftValue * rightValue);
        }
    }

    /** Bi conditional operator instance. */
    private final BiConditionalOperation biConditionalOp = new BiConditionalOperation();

    /** Conjunction operator instance. */
    private final ConjunctionOperation conjunctionOp = new ConjunctionOperation();

    /** Disjunction operator instance. */
    private final DisjunctionOperation disjunctionOp = new DisjunctionOperation();

    /** Implication operator instance. */
    private final ImplicationOperation implicationOp = new ImplicationOperation();

    /** Equal operator instance. */
    private final EqualOperation equalOp = new EqualOperation();

    /** Unequal operator instance. */
    private final UnequalOperation unequalOp = new UnequalOperation();

    /** Greater equal operator instance. */
    private final GreaterEqualOperation greaterEqualOp = new GreaterEqualOperation();

    /** Greater than operator instance. */
    private final GreaterThanOperation greaterThanOp = new GreaterThanOperation();

    /** Less equal operator instance. */
    private final LessEqualOperation lessEqualOp = new LessEqualOperation();

    /** Less than operator instance. */
    private final LessThanOperation lessThanOp = new LessThanOperation();

    /** Addition operator instance. */
    private final AdditionOperation additionOp = new AdditionOperation();

    /** Subtraction operator instance. */
    private final SubtractionOperation subtractionOp = new SubtractionOperation();

    /** Integer division operator instance. */
    private final IntegerDivisionOperation integerDivisionOp = new IntegerDivisionOperation();

    /** Modulus operator instance. */
    private final ModulusOperation modulusOp = new ModulusOperation();

    /** Multiplication operator instance. */
    private final MultiplicationOperation multiplicationOp = new MultiplicationOperation();
}
