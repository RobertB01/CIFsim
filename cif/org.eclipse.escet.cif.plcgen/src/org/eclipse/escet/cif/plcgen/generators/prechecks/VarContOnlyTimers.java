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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifAddressableUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * CIF check that only allows continuous variables that look like decreasing timers. A continuous variable is a
 * decreasing timer if:
 * <ul>
 * <li>The derivative of the continuous variable is defined with its declaration, not using equations.</li>
 * <li>The derivative of the continuous variable is {@code -1}, and this can be statically determined.</li>
 * <li>The derivative of the continuous variable may not be assigned a new value.</li>
 * <li>The initial value of the continuous variable (if specified) is non-negative, and this can be statically
 * determined.</li>
 * <li>The value of the continuous variable is only used by comparing it against a non-negative value, and this can be
 * statically determined.</li>
 * <li>The value of the continuous variable is only comparing using the "<", "<=", ">", or ">=" binary operators.</li>
 * <li>The value of the continuous variable may only be compared in value ranges where PLC semantics and CIF semantics
 * are not distinguishable.</li>
 * <li>The continuous variable is only assigned in a single-variable assignment, not in multi-assingments.</li>
 * </ul>
 */
public class VarContOnlyTimers extends CifCheck {
    @Override
    protected void preprocessComplexComponent(ComplexComponent complexComponent, CifCheckViolations violations) {
        // Find and check continuous variable declarations.
        for (Declaration decl: complexComponent.getDeclarations()) {
            if (decl instanceof ContVariable contVar) {
                if (contVar.getValue() != null) {
                    checkValue(contVar.getValue(), violations);
                }

                checkDerivativeInitialization(contVar, violations);
            }
        }
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression cvExpr, CifCheckViolations violations) {
        EObject exprParent = cvExpr.eContainer();

        // Derivative can be read but not written.
        if (cvExpr.isDerivative()) {
            // Don't have a derivative as the addressable of an update.
            while (exprParent instanceof Expression) {
                exprParent = cvExpr.eContainer();
            }
            if (exprParent instanceof Assignment asg) {
                List<Expression> assigneds = CifAddressableUtils.getRefExprs(asg.getAddressable());
                if (assigneds.contains(cvExpr)) {
                    violations.add(cvExpr, "Continuous variable derivative can only be read.");
                }
            }
            return;
        }

        // Check use of the value of the continuous variable.
        if (exprParent instanceof Assignment asg) {
            // Handle simple assignment to the continuous variable.
            // TODO Allow multi-assignments like (cv1, cv2) := (1, 2);
            Expression value = asg.getValue();
            if (cvExpr != value) {
                // Continuous variable gets assigned, check the value.
                checkValue(value, violations); // We allow 0. Useless but fine if the user wants that.
                return;
            }
        } else if (exprParent instanceof BinaryExpression binExpr) {
            // Continuous variable comparison is the only way to read a continuous variable..
            boolean varAtLeft = (binExpr.getLeft() == cvExpr);
            Expression valueExpr;
            if (varAtLeft) {
                // Allow only var >= N.
                if (binExpr.getOperator() != BinaryOperator.GREATER_EQUAL) {
                    violations.add(binExpr, "Continuous variable mmay only be compared with '>='");
                    return;
                }
                valueExpr = binExpr.getRight();
            } else {
                // Allow only N <= var.
                if (binExpr.getOperator() != BinaryOperator.LESS_EQUAL) {
                    violations.add(binExpr, "Continuous variable may only be compared with '<='");
                    return;
                }
                valueExpr = binExpr.getLeft();
            }
            checkValue(valueExpr, violations);
            return;
        }
        violations.add(cvExpr,
                "Continuous variable value may only be compared or assigned in a single-variable " + "assignment");
    }

    /**
     * Check that the derivative of the given continuous variable is initialized with the value @{code -1} or
     * {@code -1.0}.
     *
     * @param contVar Continuous variable to check.
     * @param violations Already found precheck violations, may be extended in-place.
     */
    private void checkDerivativeInitialization(ContVariable contVar, CifCheckViolations violations) {
        Expression derivative = contVar.getDerivative();
        if (derivative == null) {
            violations.add(contVar, "Derivative must be initialized to -1 or -1.0");
            return;
        }

        Object evalValue = getStaticEvaluableValue(derivative, true, violations);
        if (evalValue == null || evalValue instanceof Integer i && i == -1
                || evalValue instanceof Double d && d == -1.0)
        {
            return;
        } else {
            violations.add(derivative, "Continuous variable must have a derivative that is either -1 or -1.0");
        }
    }

    /**
     * Check that the given continuous variable value is a non-negative number.
     *
     * @param value Value to check.
     * @param violations Already found precheck violations, may be extended in-place.
     */
    private void checkValue(Expression value, CifCheckViolations violations) {
        Object evalValue = getStaticEvaluableValue(value, false, violations);
        if (evalValue == null || (evalValue instanceof Integer i && i >= 0)
                || (evalValue instanceof Double d && d >= 0.0))
        {
            return;
        }
        violations.add(value, "Value must be at least 0");
    }

    /**
     * Try to evaluate the given expression as a value assigned to a continuous variable. A violation is reported if the
     * evaluation fails and {@code null} is returned. Otherwise the evaluation value is returned.
     *
     * @param value Value to evaluate.
     * @param isDerivative Whether the given expression is used to assign to the derivative of a continuous variable.
     * @param violations Already found precheck violations, may be extended in-place.
     * @return {@code null} if the given expression could not be evaluated or does not represent a static single value,
     *     else the found value is returned as Java value. See {@link CifEvalUtils} class description for details.
     */
    private Object getStaticEvaluableValue(Expression value, boolean isDerivative, CifCheckViolations violations) {
        String valueText = "a " + (isDerivative ? "derivative " : "") + "value";

        if (!CifValueUtils.hasSingleValue(value, false, true)) {
            violations.add(value, "Continuous variable has " + valueText + " that cannot be evaluated statically");
            return null;
        } else {
            try {
                return CifEvalUtils.eval(value, false);
            } catch (CifEvalException ex) {
                Expression reportExpr = (ex.expr != null) ? ex.expr : value;
                violations.add(reportExpr, "Continuous variable has " + valueText + " that cannot be evaluated "
                        + "statically, as evaluating it results in an evaluation error");
                return null;
            }
        }
    }
}
