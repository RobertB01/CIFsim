//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.mdd;

import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IntExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;
import org.eclipse.escet.common.multivaluetrees.VarInfo;

/** Convert an expression with boolean and/or bounded integer variables to an MDD nodes collection. */
public class MddConvertExpression {
    /** {@link VarInfo} builder containing the conversion between variables and tree nodes. */
    public final MddCifVarInfoBuilder cifVarInfoBuilder;

    /** MDD nodes storage. */
    public final Tree tree;

    /** Use-kind value for readable variables in the converted expressions. */
    public final int readUseKind;

    /** Use-kind value for writable variables in the converted expressions. */
    public final int writeUseKind;

    /** Available integer variables, lazily extended. */
    private Map<Declaration, MddIntegerValueCollection> variableValues = map();

    /**
     * Constructor of the {@link MddConvertExpression} class.
     *
     * @param cifVarInfoBuilder {@link VarInfo} builder containing the conversion between variables and tree nodes.
     * @param tree MDD nodes storage.
     * @param readUseKind Use-kind value for reading variables in the converted expressions.
     * @param writeUseKind Use-kind value for writing variables in the converted expressions.
     */
    public MddConvertExpression(MddCifVarInfoBuilder cifVarInfoBuilder, Tree tree, int readUseKind, int writeUseKind) {
        this.cifVarInfoBuilder = cifVarInfoBuilder;
        this.tree = tree;
        this.readUseKind = readUseKind;
        this.writeUseKind = writeUseKind;
    }

    /**
     * Return the collection of values that an identifier can have.
     *
     * <p>
     * Lazily builds and stores the collections for future use.
     * </p>
     *
     * @param var Variable to convert.
     * @return Collection of values and the associated nodes for the variable.
     */
    private MddIntegerValueCollection getVariable(Declaration var) {
        MddIntegerValueCollection collection = variableValues.get(var);
        if (collection != null) {
            return collection;
        }

        // Variable is accessed for the first time, construct a collection for it.
        VarInfo readInfo = cifVarInfoBuilder.getVarInfo(var, readUseKind);
        collection = new MddIntegerValueCollection(readInfo.length);
        for (int idx = 0; idx < readInfo.length; idx++) {
            Node n = tree.buildEqualityIndex(readInfo, idx);
            collection.addValue(tree, readInfo.lower + idx, n);
        }
        variableValues.put(var, collection);
        return collection;
    }

    /**
     * Convert a conjunction of expressions to a collection of viable values with MDD trees expressing the condition for
     * each value.
     *
     * @param exprs Expressions to convert.
     * @return The collection reachable values with the condition when it can be reached.
     */
    public MddIntegerValueCollection convert(List<Expression> exprs) {
        Node trueVal = Tree.ONE;
        Node falseVal = Tree.ZERO;
        for (Expression expr: exprs) {
            MddIntegerValueCollection collection = convert(expr);
            falseVal = tree.disjunct(falseVal, collection.getExist(0));
            trueVal = tree.conjunct(trueVal, collection.getExist(1));
        }
        MddIntegerValueCollection result = new MddIntegerValueCollection(2);
        result.addValue(tree, 0, falseVal);
        result.addValue(tree, 1, trueVal);
        return result;
    }

    /**
     * Convert an expression to a collection of viable values with MDD trees expressing the condition for each value.
     *
     * @param expr Expression to convert.
     * @return The collection reachable values with the condition when it can be reached.
     */
    public MddIntegerValueCollection convert(Expression expr) {
        if (expr instanceof IntExpression) {
            IntExpression iExpr = (IntExpression)expr;
            MddIntegerValueCollection collection = new MddIntegerValueCollection(1);
            collection.addValue(tree, iExpr.getValue(), Tree.ONE);
            return collection;
        }

        if (expr instanceof BoolExpression) {
            BoolExpression bExpr = (BoolExpression)expr;
            MddIntegerValueCollection collection = new MddIntegerValueCollection(2);
            if (bExpr.isValue()) {
                collection.addValue(tree, 1, Tree.ONE);
                collection.addValue(tree, 0, Tree.ZERO);
            } else {
                collection.addValue(tree, 1, Tree.ZERO);
                collection.addValue(tree, 0, Tree.ONE);
            }
            return collection;
        }

        if (expr instanceof DiscVariableExpression) {
            DiscVariableExpression dve = (DiscVariableExpression)expr;
            return getVariable(dve.getVariable());
        }

        if (expr instanceof InputVariableExpression) {
            InputVariableExpression ive = (InputVariableExpression)expr;
            return getVariable(ive.getVariable());
        }

        if (expr instanceof UnaryExpression) {
            UnaryExpression unExpr = (UnaryExpression)expr;
            MddIntegerValueCollection subColl = convert(unExpr.getChild());

            switch (unExpr.getOperator()) {
                case INVERSE: {
                    Assert.check(subColl.size() == 2);
                    MddIntegerValueCollection collection = new MddIntegerValueCollection(subColl.size());
                    for (Entry<Integer, Node> entry: subColl.valueNodes.entrySet()) {
                        collection.valueNodes.put(1 - entry.getKey(), entry.getValue());
                    }
                    return collection;
                }
                case NEGATE: {
                    MddIntegerValueCollection collection = new MddIntegerValueCollection(subColl.size());
                    for (Entry<Integer, Node> entry: subColl.valueNodes.entrySet()) {
                        collection.valueNodes.put(-entry.getKey(), entry.getValue());
                    }
                    return collection;
                }
                case PLUS:
                    return subColl;

                // Cases that should never happen.
                // case SAMPLE:

                default:
                    throw new AssertionError(fmt("Unexpected unary operator '%s'.", unExpr.getOperator()));
            }
        }

        if (expr instanceof BinaryExpression) {
            BinaryExpression binExpr = (BinaryExpression)expr;
            MddIntegerValueCollection leftColl = convert(binExpr.getLeft());
            MddIntegerValueCollection rightColl = convert(binExpr.getRight());

            switch (binExpr.getOperator()) {
                case BI_CONDITIONAL: {
                    Node trueVal = Tree.ZERO;
                    for (int val = 0; val < 2; val++) {
                        Node lnode = leftColl.getExist(val);
                        Node rnode = rightColl.getExist(val);
                        trueVal = tree.disjunct(trueVal, tree.conjunct(lnode, rnode));
                    }
                    MddIntegerValueCollection collection = new MddIntegerValueCollection(2);
                    collection.addValue(tree, 1, trueVal);
                    collection.addValue(tree, 0, tree.invert(trueVal));
                    return collection;
                }

                case CONJUNCTION: {
                    MddIntegerValueCollection collection = new MddIntegerValueCollection(2);
                    // True case.
                    Node lnode = leftColl.getExist(1);
                    Node rnode = rightColl.getExist(1);
                    collection.addValue(tree, 1, tree.conjunct(lnode, rnode));

                    // False case.
                    lnode = leftColl.getExist(0);
                    rnode = rightColl.getExist(0);
                    collection.addValue(tree, 0, tree.disjunct(lnode, rnode));
                    return collection;
                }

                case DISJUNCTION: {
                    MddIntegerValueCollection collection = new MddIntegerValueCollection(2);
                    // True case.
                    Node lnode = leftColl.getExist(1);
                    Node rnode = rightColl.getExist(1);
                    collection.addValue(tree, 1, tree.disjunct(lnode, rnode));

                    // False case.
                    lnode = leftColl.getExist(0);
                    rnode = rightColl.getExist(0);
                    collection.addValue(tree, 0, tree.conjunct(lnode, rnode));
                    return collection;
                }

                case IMPLICATION: {
                    MddIntegerValueCollection collection = new MddIntegerValueCollection(2);
                    // True case.
                    Node lnode = leftColl.getExist(0);
                    Node rnode = rightColl.getExist(1);
                    collection.addValue(tree, 1, tree.disjunct(lnode, rnode));

                    // False case.
                    lnode = leftColl.getExist(1);
                    rnode = rightColl.getExist(0);
                    collection.addValue(tree, 0, tree.conjunct(lnode, rnode));
                    return collection;
                }

                case EQUAL:
                    return performBoolOp(leftColl, rightColl, equalOp);
                case UNEQUAL:
                    return performBoolOp(leftColl, rightColl, unequalOp);
                case GREATER_EQUAL:
                    return performBoolOp(leftColl, rightColl, greaterEqualOp);
                case GREATER_THAN:
                    return performBoolOp(leftColl, rightColl, greaterThanOp);
                case LESS_EQUAL:
                    return performBoolOp(leftColl, rightColl, lessEqualOp);
                case LESS_THAN:
                    return performBoolOp(leftColl, rightColl, lessThanOp);

                case ADDITION:
                    return performIntOp(leftColl, rightColl, additionOp);
                case SUBTRACTION:
                    return performIntOp(leftColl, rightColl, subtractionOp);
                case INTEGER_DIVISION:
                    return performIntOp(leftColl, rightColl, intDivisionOp);
                case MODULUS:
                    return performIntOp(leftColl, rightColl, modulusOp);
                case MULTIPLICATION:
                    return performIntOp(leftColl, rightColl, multiplicationOp);

                // Cases that should never happen.
                // case SUBSET:
                // case DIVISION:
                // case ELEMENT_OF:

                default:
                    throw new AssertionError(fmt("Unexpected binary operator '%s'.", binExpr.getOperator()));
            }
        }

        if (expr instanceof IfExpression) {
            IfExpression ifExpr = (IfExpression)expr;

            // Convert else.
            MddIntegerValueCollection rslt = convert(ifExpr.getElse());

            // Convert elifs/thens.
            for (int i = ifExpr.getElifs().size() - 1; i >= 0; i--) {
                ElifExpression elifExpr = ifExpr.getElifs().get(i);
                MddIntegerValueCollection elifGuards = convert(elifExpr.getGuards());
                MddIntegerValueCollection elifThen = convert(elifExpr.getThen());
                rslt = ifThenElse(elifGuards, elifThen, rslt);
            }

            // Convert if/then.
            MddIntegerValueCollection ifGuards = convert(ifExpr.getGuards());
            MddIntegerValueCollection ifThen = convert(ifExpr.getThen());
            return ifThenElse(ifGuards, ifThen, rslt);
        }

        if (expr instanceof SwitchExpression) {
            SwitchExpression switchExpr = (SwitchExpression)expr;
            Expression value = switchExpr.getValue();
            List<SwitchCase> cases = switchExpr.getCases();

            // Convert else.
            MddIntegerValueCollection rslt = convert(last(cases).getValue());

            // Convert cases.
            for (int i = cases.size() - 2; i >= 0; i--) {
                SwitchCase cse = cases.get(i);
                MddIntegerValueCollection caseGuard = CifTypeUtils.isAutRefExpr(value) ? convert(value)
                        : performBoolOp(convert(value), convert(cse.getKey()), equalOp);
                MddIntegerValueCollection caseThen = convert(cse.getValue());
                rslt = ifThenElse(caseGuard, caseThen, rslt);
            }

            return rslt;
        }

        throw new AssertionError(fmt("Unexpected expression node '%s'.", expr));
    }

    /**
     * Perform an if-then-else operation.
     *
     * @param guardValues If guard collection.
     * @param thenValues Then collection.
     * @param elseValues Else collection.
     * @return The resulting fresh collection.
     */
    private MddIntegerValueCollection ifThenElse(MddIntegerValueCollection guardValues,
            MddIntegerValueCollection thenValues, MddIntegerValueCollection elseValues)
    {
        MddIntegerValueCollection result = new MddIntegerValueCollection(thenValues.size() + elseValues.size());

        // Add all 'then' values to the result under the condition that the guard holds.
        for (Entry<Integer, Node> thenEntry: thenValues.valueNodes.entrySet()) {
            result.addValue(tree, thenEntry.getKey(), tree.conjunct(guardValues.getExist(1), thenEntry.getValue()));
        }

        // Add all 'else' values to the result under the condition that the guard does not hold.
        for (Entry<Integer, Node> elseEntry: elseValues.valueNodes.entrySet()) {
            result.addValue(tree, elseEntry.getKey(), tree.conjunct(guardValues.getExist(0), elseEntry.getValue()));
        }
        return result;
    }

    /** Equality operation class. */
    private static class EqualOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return leftValue.equals(rightValue) ? 1 : 0;
        }
    }

    /** Un-equality operation class. */
    private static class UnequalOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return leftValue.equals(rightValue) ? 0 : 1;
        }
    }

    /** Less than operation class. */
    private static class LessThanOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return leftValue < rightValue ? 1 : 0;
        }
    }

    /** At most operation class. */
    private static class LessEqualOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return leftValue <= rightValue ? 1 : 0;
        }
    }

    /** At least operation class. */
    private static class GreaterThanOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return leftValue > rightValue ? 1 : 0;
        }
    }

    /** Bigger than operation class. */
    private static class GreaterEqualOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            return leftValue >= rightValue ? 1 : 0;
        }
    }

    /** Addition operation class. */
    private static class AdditionOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            long rslt = (long)leftValue + (long)rightValue;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return Integer.valueOf((int)rslt);
            }
            return null;
        }
    }

    /** Subtraction operation class. */
    private static class SubtractionOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            long rslt = (long)leftValue - (long)rightValue;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return Integer.valueOf((int)rslt);
            }
            return null;
        }
    }

    /** Multiplication operation class. */
    private static class MultiplicationOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            long rslt = (long)leftValue * (long)rightValue;
            if (Integer.MIN_VALUE <= rslt && rslt <= Integer.MAX_VALUE) {
                return Integer.valueOf((int)rslt);
            }
            return null;
        }
    }

    /** Integer division operation class. */
    private static class IntDivisionOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            if (rightValue == 0) {
                return null;
            }
            if (leftValue == Integer.MIN_VALUE && rightValue == -1) {
                return null;
            }
            return leftValue / rightValue;
        }
    }

    /** Integer modulus operation class. */
    private static class ModulusOp implements MddBinaryOperation {
        @Override
        public Integer perform(Integer leftValue, Integer rightValue) {
            if (rightValue == 0) {
                return null;
            }
            return leftValue % rightValue;
        }
    }

    /** Equal operator instance. */
    private final EqualOp equalOp = new EqualOp();

    /** Unequal operator instance. */
    private final UnequalOp unequalOp = new UnequalOp();

    /** At most operator instance. */
    private final LessThanOp lessThanOp = new LessThanOp();

    /** Lees or equal operator instance. */
    private final LessEqualOp lessEqualOp = new LessEqualOp();

    /** At least operator instance. */
    private final GreaterThanOp greaterThanOp = new GreaterThanOp();

    /** Greater or equal operator instance. */
    private final GreaterEqualOp greaterEqualOp = new GreaterEqualOp();

    /** Addition operator instance. */
    private final AdditionOp additionOp = new AdditionOp();

    /** Subtraction operator instance. */
    private final SubtractionOp subtractionOp = new SubtractionOp();

    /** Multiplication operator instance. */
    private final MultiplicationOp multiplicationOp = new MultiplicationOp();

    /** Integer division operator instance. */
    private final IntDivisionOp intDivisionOp = new IntDivisionOp();

    /** Modulus operator instance. */
    private final ModulusOp modulusOp = new ModulusOp();

    /**
     * Perform an integer computation between both sides.
     *
     * @param leftSide Left side collection.
     * @param rightSide Right side collection.
     * @param binOp Binary operator instance.
     * @return The resulting fresh collection.
     */
    private MddIntegerValueCollection performIntOp(MddIntegerValueCollection leftSide,
            MddIntegerValueCollection rightSide, MddBinaryOperation binOp)
    {
        return performIntOp(leftSide, rightSide, binOp, -1);
    }

    /**
     * Perform an integer computation between both sides.
     *
     * @param leftSide Left side collection.
     * @param rightSide Right side collection.
     * @param binOp Binary operator instance.
     * @param expectedCount If non-zero and positive, the expected number of values in the result.
     * @return The resulting fresh collection.
     */
    private MddIntegerValueCollection performIntOp(MddIntegerValueCollection leftSide,
            MddIntegerValueCollection rightSide, MddBinaryOperation binOp, int expectedCount)
    {
        MddIntegerValueCollection result;
        if (expectedCount <= 0) {
            result = new MddIntegerValueCollection();
        } else {
            result = new MddIntegerValueCollection(expectedCount);
        }

        for (Entry<Integer, Node> leftEntry: leftSide.valueNodes.entrySet()) {
            for (Entry<Integer, Node> rightEntry: rightSide.valueNodes.entrySet()) {
                Integer resValue = binOp.perform(leftEntry.getKey(), rightEntry.getKey());
                if (resValue != null) {
                    Node resNode = tree.conjunct(leftEntry.getValue(), rightEntry.getValue());
                    result.addValue(tree, resValue, resNode);
                }
            }
        }

        return result;
    }

    /**
     * Perform a binary boolean computation between both sides.
     *
     * <p>
     * This function guarantees that the result will have precisely values {@code 0} and {@code 1}.
     * </p>
     *
     * @param leftSide Left side collection.
     * @param rightSide Right side collection.
     * @param binOp Binary operator instance.
     * @return The resulting fresh collection.
     */
    private MddIntegerValueCollection performBoolOp(MddIntegerValueCollection leftSide,
            MddIntegerValueCollection rightSide, MddBinaryOperation binOp)
    {
        MddIntegerValueCollection result = performIntOp(leftSide, rightSide, binOp);
        if (!result.valueNodes.containsKey(0)) {
            result.valueNodes.put(0, Tree.ZERO);
        }
        if (!result.valueNodes.containsKey(1)) {
            result.valueNodes.put(1, Tree.ZERO);
        }
        Assert.check(result.size() == 2);
        return result;
    }

    /**
     * Compute an MDD tree that assigns a collection of values to a variable.
     *
     * <p>
     * Note that illegal values from the collection are simply ignored, however this also happens while converting the
     * expression (e.g. division by zero). If all is well, the synthesis algorithm should sweep this part of the state
     * space into the bad state.
     * </p>
     *
     * @param destVar Variable to relate to the values.
     * @param collection Collection of available values with their conditions.
     * @param useKind The variable use-kind to relate the value to.
     * @return MDD tree expressing the assignment or comparison.
     */
    private Node assignCollection(Declaration destVar, MddIntegerValueCollection collection, int useKind) {
        VarInfo writeInfo = cifVarInfoBuilder.getVarInfo(destVar, useKind);

        Node n = Tree.ZERO;
        for (int idx = 0; idx < writeInfo.length; idx++) {
            Node cond = collection.get(writeInfo.lower + idx);
            if (cond == null) {
                continue;
            }
            Node writeVar = tree.buildEqualityIndex(writeInfo, idx);
            Node valueNode = tree.conjunct(cond, writeVar);
            n = tree.disjunct(n, valueNode);
        }
        return n;
    }

    /**
     * Convert an assignment to an MDD tree.
     *
     * @param destVar Variable to assign to.
     * @param rhs Value to write.
     * @return MDD tree expressing the assignment.
     */
    public Node convertAssignment(Declaration destVar, Expression rhs) {
        MddIntegerValueCollection collection = convert(rhs);
        return assignCollection(destVar, collection, writeUseKind);
    }

    /**
     * Convert an assignment to an MDD tree.
     *
     * @param destVar Variable to assign to.
     * @param rhs Value to write.
     * @return MDD tree expressing the assignment.
     */
    public Node convertToEquality(Declaration destVar, Expression rhs) {
        MddIntegerValueCollection collection = convert(rhs);
        return assignCollection(destVar, collection, readUseKind);
    }
}
