//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * CIF check that only allows continuous variables that are decreasing timers. A continuous variable is a decreasing
 * timer if:
 * <ul>
 * <li>The derivative of the continuous variable is defined with its declaration, not using equations.</li>
 * <li>The derivative of the continuous variable is {@code -1}, and this can be statically determined.</li>
 * <li>The initial value of the continuous variable (if specified) is non-negative, and this can be statically
 * determined.</li>
 * <li>The continuous variable is only assigned non-negative values, and this can be statically determined.</li>
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
            checkValue(contVar.getValue(), "has an initial value", contVar, violations);
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
        EObject exprParent = cvExpr.eContainer();
        if (exprParent instanceof Assignment asg) {
            // Handle simple assignment to the continuous variable.
            // TODO Allow multi-assignments like (cv1, cv2) := (1, 2);
            if (cvExpr == asg.getAddressable()) {
                // Continuous variable gets assigned, check the value.
                // We allow 0. Useless but fine if the user wants that.
                checkValue(asg.getValue(), "is assigned a value", cvExpr, violations);
                return;
            }
        } else if (exprParent instanceof BinaryExpression binExpr) {
            // Continuous variable comparison is the only way to read a continuous variable.
            boolean varAtLeft = (binExpr.getLeft() == cvExpr);
            if (varAtLeft) {
                // Allow only 'var <= ...'.
                if (binExpr.getOperator() == BinaryOperator.LESS_EQUAL) {
                    checkValue(binExpr.getRight(), "is compared to a value", cvExpr, violations);
                    return;
                }
            } else {
                // Allow only '... >= var'.
                if (binExpr.getOperator() == BinaryOperator.GREATER_EQUAL) {
                    checkValue(binExpr.getLeft(), "is compared to a value", cvExpr, violations);
                    return;
                }
            }

            // If it is a disallowed comparison, report that specifically.
            if (binExpr.getOperator() == BinaryOperator.EQUAL || binExpr.getOperator() == BinaryOperator.UNEQUAL
                    || binExpr.getOperator() == BinaryOperator.GREATER_EQUAL
                    || binExpr.getOperator() == BinaryOperator.GREATER_THAN
                    || binExpr.getOperator() == BinaryOperator.LESS_EQUAL
                    || binExpr.getOperator() == BinaryOperator.LESS_THAN)
            {
                violations.add(cvExpr,
                        "Continuous variable is compared, but not as \"variable <= ...\" or \"... >= variable\"");
                return;
            }

            // Not a comparison at all.
        }

        // Code generation supports multi-assignments, but this check does not yet support it (see above). Check for it.
        PositionObject ancestor = cvExpr;
        while (true) {
            // Move up until as long as we're still in an expression, which includes addressables.
            PositionObject newAncestor = (PositionObject)ancestor.eContainer();
            if (newAncestor != null && newAncestor instanceof Expression) {
                ancestor = newAncestor;
                continue;
            }

            // If it is at the left-hand side of an assignment, it is a multi-assignment, since simple assignments have
            // been handled above, and projected assignments are not possible for continuous variables.
            if (newAncestor instanceof Assignment asgn && asgn.getAddressable() == ancestor) {
                violations.add(cvExpr, "Continuous variable is assigned in a multi-assignment");
                return;
            }
            break;
        }

        // Indicate that continuous variable is used in a way that it is not one of the allowed ones.
        violations.add(cvExpr, "Continuous variable is not assigned or compared");
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

        Object evalValue = getStaticEvaluableValue(derivative, "has a derivative", contVar, violations);
        if (evalValue == null || evalValue instanceof Integer i && i == -1
                || evalValue instanceof Double d && d == -1.0)
        {
            return;
        } else {
            violations.add(contVar, "Continuous variable has a derivative that is not -1 or -1.0");
        }
    }

    /**
     * Check that the given continuous variable value is a non-negative number.
     *
     * @param value Value to check.
     * @param valueDescr The description of the value, to use in violation messages.
     * @param reportObj Object with position on which to report violations.
     * @param violations Already found violations, may be extended in-place.
     */
    private void checkValue(Expression value, String valueDescr, PositionObject reportObj,
            CifCheckViolations violations)
    {
        Object evalValue = getStaticEvaluableValue(value, valueDescr, reportObj, violations);
        if (evalValue == null || (evalValue instanceof Integer i && i >= 0)
                || (evalValue instanceof Double d && d >= 0.0))
        {
            return;
        }
        violations.add(reportObj, "Continuous variable " + valueDescr + " that is negative");
    }

    /**
     * Try to evaluate the given expression as a continuous variable value or derivative. If the evaluation fails, the
     * violation is reported and {@code null} is returned. Otherwise the evaluated value is returned.
     *
     * @param value Value to evaluate.
     * @param valueDescr The description of the value, to use in violation messages.
     * @param reportObj Object with position on which to report violations.
     * @param violations Already found violations, may be extended in-place.
     * @return {@code null} if the given expression could not be evaluated or does not represent a static single value,
     *     else the found value is returned as Java value. See {@link CifEvalUtils} class description for details.
     */
    private Object getStaticEvaluableValue(Expression value, String valueDescr, PositionObject reportObj,
            CifCheckViolations violations)
    {
        if (!CifValueUtils.hasSingleValue(value, CifValueUtils.isInitialExpr(value), true)) {
            violations.add(reportObj, "Continuous variable " + valueDescr + " that cannot be evaluated statically");
            return null;
        } else {
            try {
                return CifEvalUtils.eval(value, CifValueUtils.isInitialExpr(value));
            } catch (CifEvalException ex) {
                violations.add(reportObj, "Continuous variable " + valueDescr + " that cannot be "
                        + "evaluated statically, as evaluating it results in an evaluation error");
                return null;
            }
        }
    }
}
