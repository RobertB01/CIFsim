//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators.prechecks;

import java.util.EnumSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;

/**
 * CIF check that only allows use of continuous variables in initialization, assignments, and ordered comparisons
 * against non-negative numbers.
 */
public class CheckContvarUses extends CifCheck {
    @Override
    protected void preprocessContVariable(ContVariable contVar, CifCheckViolations violations) {
        if (contVar.eContainer() instanceof ComplexComponent) {
            // Continuous variable declaration, check initial value and derivative.
            if (contVar.getValue() != null) {
                checkValue(contVar.getValue(), true, violations);
            }

            Assert.check(contVar.getDerivative() != null); // Implied by CIF semantics.
            checkDerivative(contVar.getDerivative(), true, violations);
        }
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression cvExpr, CifCheckViolations violations) {
        Set<BinaryOperator> goodCompareOps = EnumSet.of(BinaryOperator.LESS_EQUAL, BinaryOperator.LESS_THAN,
                BinaryOperator.GREATER_EQUAL, BinaryOperator.GREATER_THAN);

        EObject exprParent = cvExpr.eContainer();
        // Continuous variable comparison.
        if (exprParent instanceof BinaryExpression binExpr) {
            if (!goodCompareOps.contains(binExpr.getOperator())) {
                violations.add(cvExpr, "Not comparing a continuous variable with an '<', '<=', '>', or '>=' operator");
            }
            // Check the value compared against.
            boolean isLessThanCompare = binExpr.getOperator().equals(BinaryOperator.LESS_THAN);
            checkValue(binExpr.getLeft() == exprParent ? binExpr.getRight() : binExpr.getLeft(), true,
                    !isLessThanCompare, violations);
            return;
        } else {
            // Update of the continuous variable.
            // TODO Handle multi-assignments like (cv1, cv2) := (1, 2);
            if (exprParent instanceof Assignment asg) {
                Expression value = asg.getValue();
                if (cvExpr != value) {
                    // Continuous variable at the left side, verify the assigned value.
                    checkValue(value, false, violations);
                    return;
                }
                // Continuous variable being read at the right side.
                violations.add(cvExpr, "Reading continuous variable outside an compare expression");
            }
        }
        violations.add(cvExpr, "Continuous variable is not compared or assigned in a single-variable assignment");
    }

    /**
     * Check that the given continuous variable derivative is a @{code -1}.
     *
     * @param derivative Derivative to check.
     * @param isInitialState Whether the expression is always evaluated in the initial state.
     * @param violations Already found precheck violations, may be extended in-place.
     */
    private void checkDerivative(Expression derivative, boolean isInitialState, CifCheckViolations violations) {
        Object value;
        try {
            value = CifEvalUtils.eval(derivative, isInitialState);
        } catch (CifEvalException ex) {
            violations.add(derivative, "Derivative cannot be statically evaluated");
            return;
        }

        boolean goodValue;
        if (value instanceof Integer i) {
            goodValue = (i == -1);
        } else if (value instanceof Double d) {
            goodValue = (d == -1.0);
        } else {
            goodValue = false;
        }
        if (!goodValue) {
            violations.add(derivative, "Continuous variable has a derivative that is not -1");
        }
    }

    /**
     * Check that the given continuous variable value is a non-negative number.
     *
     * @param value Value to check.
     * @param isInitialState Whether the expression is always evaluated in the initial state.
     * @param violations Already found precheck violations, may be extended in-place.
     */
    private void checkValue(Expression value, boolean isInitialState, CifCheckViolations violations) {
        checkValue(value, isInitialState, true, violations);
    }

    /**
     * Check that the given continuous variable value is a non-negative number.
     *
     * @param value Value to check.
     * @param isInitialState Whether the expression is always evaluated in the initial state.
     * @param mayBeZero Whether it is allowed to compare against value {@code 0}.
     * @param violations Already found precheck violations, may be extended in-place.
     */
    private void checkValue(Expression value, boolean isInitialState, boolean mayBeZero,
            CifCheckViolations violations)
    {
        Object evalValue;
        try {
            evalValue = CifEvalUtils.eval(value, isInitialState);
        } catch (CifEvalException ex) {
            violations.add(value, "Value cannot be statically evaluated");
            return;
        }

        // Less than checking is not allowed for value 0, as PLC timers never go below 0. See also class description of
        // DefaultContinuousVarsGenerator.
        boolean goodValue;
        if (evalValue instanceof Integer i) {
            goodValue = mayBeZero ? (i >= 0) : (i > 0);
        } else if (evalValue instanceof Double d) {
            goodValue = mayBeZero ? (d >= 0.0) : (d > 0.0);
        } else {
            goodValue = false;
        }
        if (!goodValue) {
            if (mayBeZero) {
                violations.add(value, "Value is not a number or not at least 0");
            } else {
                violations.add(value, "Value is not a number or not larger than 0");
            }
        }
    }
}
