//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.bdd.conversion.preconditions;

import java.util.List;

import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEnumLiteral;
import org.eclipse.escet.cif.common.CifEquationUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ConstantExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.InputVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ReceivedExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchCase;
import org.eclipse.escet.cif.metamodel.cif.expressions.SwitchExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.UnaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.java.Assert;

/**
 * CIF check that allows expressions only if they are supported by the CIF to BDD conversion:
 *
 * <ul>
 * <li>Assignment right hand sides must be valid expressions/predicates.</li>
 * <li>Discrete variable explicit initial values must be valid expressions/predicates.</li>
 * <li>Edge guards must be valid predicates.</li>
 * <li>Initialization predicates in components and locations must be valid predicates.</li>
 * <li>Invariant predicates must be valid predicates.</li>
 * <li>Marker predicates in components and locations must be valid predicates.</li>
 * <li>Send values of edges must be valid expressions/predicates.</li>
 * </ul>
 */
public class CifToBddExprOnlySupportedExprsCheck extends CifCheckNoCompDefInst {
    @Override
    protected void preprocessAssignment(Assignment asgn, CifCheckViolations violations) {
        checkExprOrPred(asgn.getValue(), false, true, violations);
    }

    @Override
    protected void preprocessVariableValue(VariableValue values, CifCheckViolations violations) {
        for (Expression value: values.getValues()) {
            checkExprOrPred(value, true, false, violations);
        }
    }

    @Override
    protected void preprocessEdge(Edge edge, CifCheckViolations violations) {
        checkPreds(edge.getGuards(), false, violations);
    }

    @Override
    protected void preprocessComplexComponent(ComplexComponent comp, CifCheckViolations violations) {
        checkPreds(comp.getInitials(), true, violations);
        checkPreds(comp.getMarkeds(), false, violations);
    }

    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        checkPreds(loc.getInitials(), true, violations);
        checkPreds(loc.getMarkeds(), false, violations);
    }

    @Override
    protected void preprocessInvariant(Invariant inv, CifCheckViolations violations) {
        checkPred(inv.getPredicate(), false, violations);
    }

    @Override
    protected void preprocessEdgeSend(EdgeSend edgeSend, CifCheckViolations violations) {
        Expression value = edgeSend.getValue();
        if (value != null) {
            checkExprOrPred(value, false, false, violations);
        }
    }

    /**
     * Check a CIF expression as a non-boolean expression or as a predicate.
     *
     * @param expr The expression.
     * @param initial Whether the expression applies only to the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param allowSubtract Whether a subtraction is allowed ({@code true}) or not ({@code false}). It must only be
     *     allowed for top-level expressions, if the caller can handle the potential unrepresentable results. For
     *     sub-expressions, subtraction is never allowed.
     * @param violations The violations collected so far.
     */
    public static void checkExprOrPred(Expression expr, boolean initial, boolean allowSubtract,
            CifCheckViolations violations)
    {
        CifType type = CifTypeUtils.normalizeType(expr.getType());
        if (type instanceof BoolType) {
            // Predicate.
            checkPred(expr, initial, violations);
        } else if (type instanceof IntType || type instanceof EnumType) {
            // Non-boolean expression.
            checkExpr(expr, initial, allowSubtract, violations);
        } else {
            // Unsupported.
            violations.add(expr, "A value of type \"%s\" is used", CifTextUtils.typeToStr(type));
        }
    }

    /**
     * Check CIF predicates.
     *
     * @param preds The CIF predicates.
     * @param initial Whether the predicates apply only to the initial state ({@code true}) or any state ({@code false},
     *     includes the initial state).
     * @param violations The violations collected so far.
     */
    public static void checkPreds(List<Expression> preds, boolean initial, CifCheckViolations violations) {
        for (Expression pred: preds) {
            checkPred(pred, initial, violations);
        }
    }

    /**
     * Check a CIF predicate.
     *
     * @param pred The CIF predicate.
     * @param initial Whether the predicate applies only to the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param violations The violations collected so far.
     */
    public static void checkPred(Expression pred, boolean initial, CifCheckViolations violations) {
        // Sanity check.
        CifType type = CifTypeUtils.normalizeType(pred.getType());
        Assert.check(type instanceof BoolType);

        // Handle different expressions.
        if (pred instanceof BoolExpression) {
            // Boolean literal.
            return;
        } else if (pred instanceof DiscVariableExpression) {
            // Boolean discrete variable reference.
            return;
        } else if (pred instanceof InputVariableExpression) {
            // Boolean input variable reference.
            return;
        } else if (pred instanceof AlgVariableExpression algExpr) {
            // Boolean algebraic variable reference. Check the single defining value expression, representing the value
            // of the variable. It is in an 'if' expression if an equation is provided per location of an automaton with
            // more than one location.
            AlgVariable var = algExpr.getVariable();
            Assert.check(CifTypeUtils.normalizeType(var.getType()) instanceof BoolType);
            Expression value = CifEquationUtils.getSingleValueForAlgVar(var);
            checkPred(value, initial, violations);
            return;
        } else if (pred instanceof LocationExpression) {
            // Location reference.
            return;
        } else if (pred instanceof ConstantExpression) {
            // Boolean constant reference. Type checker already checks that the value is statically evaluable.
            return;
        } else if (pred instanceof UnaryExpression unaryExpr) {
            // Unary expression.
            UnaryOperator op = unaryExpr.getOperator();
            switch (op) {
                // not
                case INVERSE:
                    checkPred(unaryExpr.getChild(), initial, violations);
                    break;

                // Unsupported.
                default:
                    violations.add(pred, "Unary operator \"%s\" is used", CifTextUtils.operatorToStr(op));
                    break;
            }
            return;
        } else if (pred instanceof BinaryExpression binaryExpr) {
            // Binary expression.
            Expression lhs = binaryExpr.getLeft();
            Expression rhs = binaryExpr.getRight();
            BinaryOperator op = binaryExpr.getOperator();
            switch (op) {
                // and / or
                case CONJUNCTION:
                case DISJUNCTION: {
                    CifType ltype = CifTypeUtils.normalizeType(lhs.getType());
                    CifType rtype = CifTypeUtils.normalizeType(rhs.getType());
                    if (!(ltype instanceof BoolType) || !(rtype instanceof BoolType)) {
                        violations.add(pred, "Binary operator \"%s\" is used on values of types \"%s\" and \"%s\"",
                                CifTextUtils.operatorToStr(op), CifTextUtils.typeToStr(ltype),
                                CifTextUtils.typeToStr(rtype));
                        return;
                    }

                    checkPred(lhs, initial, violations);
                    checkPred(rhs, initial, violations);
                    return;
                }

                // => / <=>
                case IMPLICATION:
                case BI_CONDITIONAL:
                    checkPred(lhs, initial, violations);
                    checkPred(rhs, initial, violations);
                    return;

                // Comparisons.
                case EQUAL:
                case GREATER_EQUAL:
                case GREATER_THAN:
                case LESS_EQUAL:
                case LESS_THAN:
                case UNEQUAL:
                    checkCmpPred(binaryExpr, initial, violations);
                    return;

                // Unsupported.
                default: {
                    violations.add(pred, "Binary operator \"%s\" is used", CifTextUtils.operatorToStr(op));
                    return;
                }
            }
        } else if (pred instanceof IfExpression ifExpr) {
            // Conditional expression with boolean result values.
            checkPreds(ifExpr.getGuards(), initial, violations);
            checkPred(ifExpr.getThen(), initial, violations);
            for (ElifExpression elif: ifExpr.getElifs()) {
                checkPreds(elif.getGuards(), initial, violations);
                checkPred(elif.getThen(), initial, violations);
            }
            checkPred(ifExpr.getElse(), initial, violations);
            return;
        } else if (pred instanceof SwitchExpression switchExpr) {
            // Switch expression with boolean result values.
            Expression value = switchExpr.getValue();
            boolean isAutSwitch = CifTypeUtils.isAutRefExpr(value);
            if (!isAutSwitch) {
                checkExprOrPred(value, initial, false, violations);
            }
            for (SwitchCase switchCase: switchExpr.getCases()) {
                if (switchCase.getKey() != null) {
                    checkExprOrPred(switchCase.getKey(), initial, false, violations);
                }
                checkPred(switchCase.getValue(), initial, violations);
            }
            return;
        } else if (pred instanceof ReceivedExpression) {
            // Ignore, since during the actual conversion, we will not have channels anymore. We check the send values
            // elsewhere in this class.
            return;
        } else {
            // Others: unsupported.
            violations.add(pred, "Predicate is not supported");
            return;
        }
    }

    /**
     * Check a CIF comparison predicate.
     *
     * @param cmpPred The comparison predicate.
     * @param initial Whether the predicate applies only to the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param violations The violations collected so far.
     */
    private static void checkCmpPred(BinaryExpression cmpPred, boolean initial, CifCheckViolations violations) {
        Expression lhs = cmpPred.getLeft();
        Expression rhs = cmpPred.getRight();
        CifType ltype = CifTypeUtils.normalizeType(lhs.getType());
        CifType rtype = CifTypeUtils.normalizeType(rhs.getType());

        if (ltype instanceof BoolType && rtype instanceof BoolType) {
            // Predicates.
            checkPred(lhs, initial, violations);
            checkPred(rhs, initial, violations);
        } else if ((ltype instanceof EnumType && rtype instanceof EnumType)
                || (ltype instanceof IntType && rtype instanceof IntType))
        {
            // Non-boolean expressions.
            checkExpr(lhs, initial, false, violations);
            checkExpr(rhs, initial, false, violations);
        } else {
            // Unsupported.
            violations.add(cmpPred, "Binary operator \"%s\" is used on values of types \"%s\" and \"%s\"",
                    CifTextUtils.operatorToStr(cmpPred.getOperator()), CifTextUtils.typeToStr(ltype),
                    CifTextUtils.typeToStr(rtype));
        }
    }

    /**
     * Check a non-boolean CIF expression.
     *
     * @param expr The CIF expression, with an integer or enumeration type.
     * @param initial Whether the expression applies only to the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param allowSubtract Whether a subtraction is allowed ({@code true}) or not ({@code false}). It must only be
     *     allowed for top-level expressions, if the caller can handle the potential unrepresentable results. For
     *     sub-expressions, subtraction is never allowed.
     * @param violations The violations collected so far.
     */
    public static void checkExpr(Expression expr, boolean initial, boolean allowSubtract,
            CifCheckViolations violations)
    {
        // Sanity check.
        CifType type = CifTypeUtils.normalizeType(expr.getType());
        Assert.check(type instanceof IntType || type instanceof EnumType);

        // Variable references.
        if (expr instanceof DiscVariableExpression) {
            // Discrete variable reference.
            return;
        } else if (expr instanceof InputVariableExpression) {
            // Input variable reference.
            return;
        } else if (expr instanceof AlgVariableExpression algExpr) {
            // Algebraic variable reference. Check the single defining value expression, representing the value of the
            // variable. It is in an 'if' expression if an equation is provided per location of an automaton with more
            // than one location.
            AlgVariable var = algExpr.getVariable();
            Expression value = CifEquationUtils.getSingleValueForAlgVar(var);
            checkExpr(value, initial, allowSubtract, violations);
            return;
        }

        // Unary expression.
        if (expr instanceof UnaryExpression unaryExpr) {
            UnaryOperator op = unaryExpr.getOperator();
            if (op == UnaryOperator.PLUS) {
                checkExpr(unaryExpr.getChild(), initial, false, violations);
                return;
            }
            // Continue, to try to evaluate the expression statically.
        }

        // Binary expression.
        if (expr instanceof BinaryExpression binaryExpr) {
            Expression lhs = binaryExpr.getLeft();
            Expression rhs = binaryExpr.getRight();
            BinaryOperator op = binaryExpr.getOperator();
            switch (op) {
                case ADDITION:
                    checkExpr(lhs, initial, false, violations);
                    checkExpr(rhs, initial, false, violations);
                    return;

                case SUBTRACTION:
                    if (allowSubtract) {
                        checkExpr(lhs, initial, false, violations);
                        checkExpr(rhs, initial, false, violations);
                        return;
                    }
                    break; // Continue, to try to evaluate the expression statically.

                case INTEGER_DIVISION:
                case MODULUS: {
                    // Check lhs.
                    checkExpr(lhs, initial, false, violations);

                    // Check statically evaluable divisor/rhs.
                    Expression notSingleValue = CifValueUtils.checkSingleValue(rhs, initial, true);
                    if (notSingleValue != null) {
                        violations.add(notSingleValue, "Value is too complex to be statically evaluated, "
                                + "or evaluation results in a runtime error");
                        return;
                    }

                    // Evaluate divisor/rhs.
                    Object rhsValueObj;
                    try {
                        rhsValueObj = CifEvalUtils.eval(rhs, initial);
                    } catch (CifEvalException ex) {
                        Expression reportObj = (ex.expr != null) ? ex.expr : rhs;
                        violations.add(reportObj, "Failed to statically evaluate the divisor for \"%s\"", op);
                        return;
                    }

                    // Check divisor/rhs value.
                    int divisor = (int)rhsValueObj;
                    if (divisor == 0) {
                        violations.add(rhs, "Division by zero for \"%s\"", CifTextUtils.operatorToStr(op));
                        return;
                    } else if (divisor < 0) {
                        violations.add(rhs, "Division by a negative value for \"%s\"", CifTextUtils.operatorToStr(op));
                        return;
                    }

                    // Done.
                    return;
                }

                default:
                    break; // Continue, to try to evaluate the expression statically.
            }
        }

        // Conditional expression.
        if (expr instanceof IfExpression ifExpr) {
            checkPreds(ifExpr.getGuards(), initial, violations);
            checkExpr(ifExpr.getThen(), initial, false, violations);
            for (ElifExpression elif: ifExpr.getElifs()) {
                checkPreds(elif.getGuards(), initial, violations);
                checkExpr(elif.getThen(), initial, false, violations);
            }
            checkExpr(ifExpr.getElse(), initial, false, violations);
            return;
        }

        // Switch expression.
        if (expr instanceof SwitchExpression switchExpr) {
            Expression value = switchExpr.getValue();
            boolean isAutSwitch = CifTypeUtils.isAutRefExpr(value);
            if (!isAutSwitch) {
                checkExprOrPred(value, initial, false, violations);
            }
            for (SwitchCase switchCase: switchExpr.getCases()) {
                if (switchCase.getKey() != null) {
                    checkExprOrPred(switchCase.getKey(), initial, false, violations);
                }
                checkExpr(switchCase.getValue(), initial, false, violations);
            }
            return;
        }

        // Received expression.
        if (expr instanceof ReceivedExpression) {
            // Ignore, since during the actual conversion, we will not have channels anymore. We check the send values
            // elsewhere in this class.
            return;
        }

        // Check for statically evaluable expression.
        Expression notSingleValue = CifValueUtils.checkSingleValue(expr, initial, true);
        if (notSingleValue != null) {
            violations.add(notSingleValue, "Value is too complex to be statically evaluated, "
                    + "or evaluation results in a runtime error");
            return;
        }

        // Evaluate expression.
        Object valueObj;
        try {
            valueObj = CifEvalUtils.eval(expr, initial);
        } catch (CifEvalException ex) {
            Expression reportObj = (ex.expr != null) ? ex.expr : expr;
            violations.add(reportObj, "Failed to statically evaluate an expression");
            return;
        }

        // Check evaluated value.
        if (valueObj instanceof Integer value) {
            // Negative integer values not supported.
            if (value < 0) {
                violations.add(expr, "A negative integer value is used (%d)", value);
                return;
            }
            return;
        } else if (valueObj instanceof CifEnumLiteral) {
            return;
        } else {
            throw new AssertionError("Unexpected value: " + valueObj);
        }
    }
}
