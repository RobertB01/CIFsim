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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * CIF check that only allows continuous variables that are decreasing timers. A continuous variable is a decreasing
 * timer if:
 * <ul>
 * <li>The derivative of the continuous variable is defined with its declaration, not using equations.</li>
 * <li>The derivative of the continuous variable is {@code -1}, and this can be statically determined.</li>
 * <li>The initial value of the continuous variable (if specified) is non-negative, and this can be statically
 * determined.</li>
 * <li>The value of the continuous variable is only used by comparing it against a non-negative value, and this can be
 * statically determined.</li>
 * <li>The value of the continuous variable is compared in value ranges where PLC semantics and CIF semantics are not
 * distinguishable. Hence, it is in the form of 'var <= ...' or '... >= var'.</li>
 * <li>The continuous variable is only assigned in single-variable assignments, not in multi-assignments.</li>
 * </ul>
 */
public class VarContOnlyTimers extends CifCheckNoCompDefInst {
    @Override
    protected void preprocessContVariable(ContVariable contVar, CifCheckViolations violations) {
        if (contVar.getValue() != null) {
            checkValue(contVar.getValue(), violations);
        }

        checkDerivative(contVar, violations);
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression cvExpr, CifCheckViolations violations) {
        // The use of the derivative has no additional restrictions.
        if (cvExpr.isDerivative()) {
            return;
        }

        // Check use of the value of the continuous variable.
        boolean reportViolation = false;

        EObject exprParent = cvExpr.eContainer();
        if (exprParent instanceof Assignment asg) {
            // Handle simple assignment to the continuous variable.
            // TODO Allow multi-assignments like (cv1, cv2) := (1, 2);
            if (cvExpr == asg.getAddressable()) {
                // Continuous variable gets assigned, check the value.
                checkValue(asg.getValue(), violations); // We allow 0. Useless but fine if the user wants that.
            } else {
                // Using continuous variable as value in assignment is not allowed.
                reportViolation = true;
            }
        } else if (exprParent instanceof BinaryExpression binExpr) {
            // Continuous variable comparison is the only way to read a continuous variable.
            boolean varAtLeft = (binExpr.getLeft() == cvExpr);
            Expression valueExpr;
            if (varAtLeft) {
                // Allow only 'var <= ...'.
                reportViolation |= (binExpr.getOperator() != BinaryOperator.LESS_EQUAL);
                valueExpr = binExpr.getRight();
            } else {
                // Allow only '... >= var'.
                reportViolation |= (binExpr.getOperator() != BinaryOperator.GREATER_EQUAL);
                valueExpr = binExpr.getLeft();
            }
            checkValue(valueExpr, violations);
        } else {
            // Don't allow a continuous variable in other expressions.
            reportViolation = true;
        }

        if (reportViolation) {
            violations.add(cvExpr, "Continuous variable value is not compared as \"variable <= ...\" or \"... >= "
                    + "variable\", nor assigned in a single-variable assignment");
        }
    }

    /**
     * Check that the derivative of the given continuous variable is {@code -1} or {@code -1.0}.
     *
     * @param contVar Continuous variable to check.
     * @param violations Already found violations, may be extended in-place.
     */
    private void checkDerivative(ContVariable contVar, CifCheckViolations violations) {
        Expression derivative = contVar.getDerivative();
        if (derivative == null) {
            violations.add(contVar, "Continuous variable has its derivative declared through one or more equations, "
                    + "rather than directly with its declaration");
            return;
        }

        Object evalValue = getStaticEvaluableValue(derivative, false, violations);
        if (evalValue == null || evalValue instanceof Integer i && i == -1
                || evalValue instanceof Double d && d == -1.0)
        {
            return;
        } else {
            violations.add(derivative, "Continuous variable has a derivative that is not a constant -1 or -1.0");
        }
    }

    /**
     * Check that the given continuous variable value is a non-negative number.
     *
     * @param value Value to check.
     * @param violations Already found violations, may be extended in-place.
     */
    private void checkValue(Expression value, CifCheckViolations violations) {
        Object evalValue = getStaticEvaluableValue(value, true, violations);
        if (evalValue == null || (evalValue instanceof Integer i && i >= 0)
                || (evalValue instanceof Double d && d >= 0.0))
        {
            return;
        }
        violations.add(value, "Continuous variable is initialized to, assigned, or compared to, a negative value");
    }

    /**
     * Try to evaluate the given expression as an initial value, assigned value, or derivative of a continuous variable,
     * or a value against which a continuous variable is compared. If the evaluation fails, the violation is reported
     * and {@code null} is returned. Otherwise the evaluated value is returned.
     *
     * @param value Value to evaluate.
     * @param isValue If {@code true}, the given expression is assumed to be the value of a continuous variable, else it
     *     is assumed to be the value of a derivative of a continuous variable.
     * @param violations Already found violations, may be extended in-place.
     * @return {@code null} if the given expression could not be evaluated or does not represent a static single value,
     *     else the found value is returned as Java value. See {@link CifEvalUtils} class description for details.
     */
    private Object getStaticEvaluableValue(Expression value, boolean isValue, CifCheckViolations violations) {
        String valueText = isValue ? "value" : "derivative";

        if (!CifValueUtils.hasSingleValue(value, CifValueUtils.isInitialExpr(value), true)) {
            violations.add(value, "Continuous variable has a " + valueText + " that cannot be evaluated statically");
            return null;
        } else {
            try {
                return CifEvalUtils.eval(value, CifValueUtils.isInitialExpr(value));
            } catch (CifEvalException ex) {
                Expression reportExpr = (ex.expr != null) ? ex.expr : value;
                violations.add(reportExpr, "Continuous variable has a " + valueText + " that cannot be evaluated "
                        + "statically, as evaluating it results in an evaluation error");
                return null;
            }
        }
    }
}
