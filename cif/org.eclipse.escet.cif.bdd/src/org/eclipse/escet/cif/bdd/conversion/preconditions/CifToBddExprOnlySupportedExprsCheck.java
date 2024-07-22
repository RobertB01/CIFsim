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
     * @return Whether the expression is supported.
     */
    public static boolean checkExprOrPred(Expression expr, boolean initial, boolean allowSubtract,
            CifCheckViolations violations)
    {
        CifType type = CifTypeUtils.normalizeType(expr.getType());
        if (type instanceof BoolType) {
            // Predicate.
            return checkPred(expr, initial, violations);
        } else if (type instanceof IntType || type instanceof EnumType) {
            // Non-boolean expression.
            return checkExpr(expr, initial, allowSubtract, violations);
        } else {
            // Unsupported.
            violations.add(expr, "A value of type \"%s\" is used", CifTextUtils.typeToStr(type));
            return false;
        }
    }

    /**
     * Check CIF predicates.
     *
     * @param preds The CIF predicates.
     * @param initial Whether the predicates apply only to the initial state ({@code true}) or any state ({@code false},
     *     includes the initial state).
     * @param violations The violations collected so far.
     * @return Whether the predicates are supported.
     */
    public static boolean checkPreds(List<Expression> preds, boolean initial, CifCheckViolations violations) {
        boolean supported = true;
        for (Expression pred: preds) {
            supported &= checkPred(pred, initial, violations);
        }
        return supported;
    }

    /**
     * Check a CIF predicate.
     *
     * @param pred The CIF predicate.
     * @param initial Whether the predicate applies only to the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param violations The violations collected so far.
     * @return Whether the predicate is supported.
     */
    public static boolean checkPred(Expression pred, boolean initial, CifCheckViolations violations) {
        // Sanity check.
        CifType type = CifTypeUtils.normalizeType(pred.getType());
        Assert.check(type instanceof BoolType);

        // Handle different expressions.
        if (pred instanceof BoolExpression) {
            // Boolean literal.
            return true;
        } else if (pred instanceof DiscVariableExpression) {
            // Boolean discrete variable reference.
            return true;
        } else if (pred instanceof InputVariableExpression) {
            // Boolean input variable reference.
            return true;
        } else if (pred instanceof AlgVariableExpression algExpr) {
            // Boolean algebraic variable reference. Check the single defining value expression, representing the value
            // of the variable. It is in an 'if' expression if an equation is provided per location of an automaton with
            // more than one location.
            AlgVariable var = algExpr.getVariable();
            Assert.check(CifTypeUtils.normalizeType(var.getType()) instanceof BoolType);
            Expression value = CifEquationUtils.getSingleValueForAlgVar(var);
            return checkPred(value, initial, violations);
        } else if (pred instanceof LocationExpression) {
            // Location reference.
            return true;
        } else if (pred instanceof ConstantExpression) {
            // Boolean constant reference. Type checker already checks that the value is statically evaluable.
            return true;
        } else if (pred instanceof UnaryExpression unaryExpr) {
            // Unary expression.
            UnaryOperator op = unaryExpr.getOperator();
            if (op != UnaryOperator.INVERSE) {
                violations.add(pred, "Unary operator \"%s\" is used", CifTextUtils.operatorToStr(op));
                return false;
            }
            return checkPred(unaryExpr.getChild(), initial, violations);
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
                        return false;
                    }

                    return checkPred(lhs, initial, violations) && checkPred(rhs, initial, violations);
                }

                // => / <=>
                case IMPLICATION:
                case BI_CONDITIONAL:
                    return checkPred(lhs, initial, violations) && checkPred(rhs, initial, violations);

                // Comparisons.
                case EQUAL:
                case GREATER_EQUAL:
                case GREATER_THAN:
                case LESS_EQUAL:
                case LESS_THAN:
                case UNEQUAL:
                    return checkCmpPred(binaryExpr, initial, violations);

                // Unsupported.
                default: {
                    violations.add(pred, "Binary operator \"%s\" is used", CifTextUtils.operatorToStr(op));
                    return false;
                }
            }
        } else if (pred instanceof IfExpression ifExpr) {
            // Conditional expression with boolean result values.
            boolean supported = true;
            supported &= checkPreds(ifExpr.getGuards(), initial, violations);
            supported &= checkPred(ifExpr.getThen(), initial, violations);
            for (ElifExpression elif: ifExpr.getElifs()) {
                supported &= checkPreds(elif.getGuards(), initial, violations);
                supported &= checkPred(elif.getThen(), initial, violations);
            }
            supported &= checkPred(ifExpr.getElse(), initial, violations);
            return supported;
        } else if (pred instanceof SwitchExpression switchExpr) {
            // Switch expression with boolean result values.
            Expression value = switchExpr.getValue();
            boolean isAutSwitch = CifTypeUtils.isAutRefExpr(value);
            boolean supported = true;
            if (!isAutSwitch) {
                supported &= checkExprOrPred(value, initial, false, violations);
            }
            for (SwitchCase switchCase: switchExpr.getCases()) {
                if (switchCase.getKey() != null) {
                    supported &= checkExprOrPred(switchCase.getKey(), initial, false, violations);
                }
                supported &= checkPred(switchCase.getValue(), initial, violations);
            }
            return supported;
        } else {
            // Others: unsupported.
            violations.add(pred, "Predicate is not supported");
            return false;
        }
    }

    /**
     * Check a CIF comparison predicate.
     *
     * @param cmpPred The comparison predicate.
     * @param initial Whether the predicate applies only to the initial state ({@code true}) or any state
     *     ({@code false}, includes the initial state).
     * @param violations The violations collected so far.
     * @return Whether the comparison is supported.
     */
    private static boolean checkCmpPred(BinaryExpression cmpPred, boolean initial, CifCheckViolations violations) {
        Expression lhs = cmpPred.getLeft();
        Expression rhs = cmpPred.getRight();
        CifType ltype = CifTypeUtils.normalizeType(lhs.getType());
        CifType rtype = CifTypeUtils.normalizeType(rhs.getType());

        if (ltype instanceof BoolType && rtype instanceof BoolType) {
            // Predicates.
            return checkPred(lhs, initial, violations) && checkPred(rhs, initial, violations);
        } else if ((ltype instanceof EnumType && rtype instanceof EnumType)
                || (ltype instanceof IntType && rtype instanceof IntType))
        {
            // Non-boolean expressions.
            return checkExpr(lhs, initial, false, violations) && checkExpr(rhs, initial, false, violations);
        } else {
            // Unsupported.
            violations.add(cmpPred, "Binary operator \"%s\" is used on values of types \"%s\" and \"%s\"",
                    CifTextUtils.operatorToStr(cmpPred.getOperator()), CifTextUtils.typeToStr(ltype),
                    CifTextUtils.typeToStr(rtype));
            return false;
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
     * @return Whether the expression is supported.
     */
    public static boolean checkExpr(Expression expr, boolean initial, boolean allowSubtract,
            CifCheckViolations violations)
    {
        // Sanity check.
        CifType type = CifTypeUtils.normalizeType(expr.getType());
        Assert.check(type instanceof IntType || type instanceof EnumType);

        // Variable references.
        if (expr instanceof DiscVariableExpression) {
            // Discrete variable reference.
            return true;
        } else if (expr instanceof InputVariableExpression) {
            // Input variable reference.
            return true;
        } else if (expr instanceof AlgVariableExpression algExpr) {
            // Algebraic variable reference. Check the single defining value expression, representing the value of the
            // variable. It is in an 'if' expression if an equation is provided per location of an automaton with more
            // than one location.
            AlgVariable var = algExpr.getVariable();
            Expression value = CifEquationUtils.getSingleValueForAlgVar(var);
            return checkExpr(value, initial, allowSubtract, violations);
        }

        // Unary expression.
        if (expr instanceof UnaryExpression unaryExpr) {
            UnaryOperator op = unaryExpr.getOperator();
            if (op == UnaryOperator.PLUS) {
                return checkExpr(unaryExpr.getChild(), initial, false, violations);
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
                    return checkExpr(lhs, initial, false, violations) && checkExpr(rhs, initial, false, violations);

                case SUBTRACTION:
                    if (allowSubtract) {
                        return checkExpr(lhs, initial, false, violations) && checkExpr(rhs, initial, false, violations);
                    }
                    break; // Continue, to try to evaluate the expression statically.

                case INTEGER_DIVISION:
                case MODULUS: {
                    // Check lhs.
                    boolean supported = checkExpr(lhs, initial, false, violations);

                    // Check statically evaluable divisor/rhs.
                    Expression notSingleValue = CifValueUtils.checkSingleValue(rhs, initial, true);
                    if (notSingleValue != null) {
                        violations.add(notSingleValue, "Value is too complex to be statically evaluated, "
                                + "or evaluation results in a runtime error");
                        return false;
                    }

                    // Evaluate divisor/rhs.
                    Object rhsValueObj;
                    try {
                        rhsValueObj = CifEvalUtils.eval(rhs, initial);
                    } catch (CifEvalException ex) {
                        Expression reportObj = (ex.expr != null) ? ex.expr : rhs;
                        violations.add(reportObj, "Failed to statically evaluate the divisor for \"%s\"", op);
                        return false;
                    }

                    // Check divisor/rhs value.
                    int divisor = (int)rhsValueObj;
                    if (divisor == 0) {
                        violations.add(rhs, "Division by zero for \"%s\"", op);
                        return false;
                    } else if (divisor < 0) {
                        violations.add(rhs, "Division by a negative value for \"%s\"", op);
                        return false;
                    }

                    // RHS is supported. Report whether LHS is supported.
                    return supported;
                }

                default:
                    break; // Continue, to try to evaluate the expression statically.
            }
        }

        // Conditional expression.
        if (expr instanceof IfExpression ifExpr) {
            boolean supported = true;
            supported &= checkPreds(ifExpr.getGuards(), initial, violations);
            supported &= checkExpr(ifExpr.getThen(), initial, false, violations);
            for (ElifExpression elif: ifExpr.getElifs()) {
                supported &= checkPreds(elif.getGuards(), initial, violations);
                supported &= checkExpr(elif.getThen(), initial, false, violations);
            }
            supported &= checkExpr(ifExpr.getElse(), initial, false, violations);
            return supported;
        }

        // Switch expression.
        if (expr instanceof SwitchExpression switchExpr) {
            Expression value = switchExpr.getValue();
            boolean isAutSwitch = CifTypeUtils.isAutRefExpr(value);
            boolean supported = true;
            if (!isAutSwitch) {
                supported &= checkExprOrPred(value, initial, false, violations);
            }
            for (SwitchCase switchCase: switchExpr.getCases()) {
                if (switchCase.getKey() != null) {
                    supported &= checkExprOrPred(switchCase.getKey(), initial, false, violations);
                }
                supported &= checkExpr(switchCase.getValue(), initial, false, violations);
            }
            return supported;
        }

        // Check for statically evaluable expression.
        Expression notSingleValue = CifValueUtils.checkSingleValue(expr, initial, true);
        if (notSingleValue != null) {
            violations.add(notSingleValue, "Value is too complex to be statically evaluated, "
                    + "or evaluation results in a runtime error");
            return false;
        }

        // Evaluate expression.
        Object valueObj;
        try {
            valueObj = CifEvalUtils.eval(expr, initial);
        } catch (CifEvalException ex) {
            Expression reportObj = (ex.expr != null) ? ex.expr : expr;
            violations.add(reportObj, "Failed to statically evaluate an expression");
            return false;
        }

        // Check evaluated value.
        if (valueObj instanceof Integer value) {
            // Negative integer values not supported.
            if (value < 0) {
                violations.add(expr, "A negative integer value is used (%d)", value);
                return false;
            }
            return true;
        } else if (valueObj instanceof CifEnumLiteral) {
            return true;
        } else {
            throw new AssertionError("Unexpected value: " + valueObj);
        }
    }
}
