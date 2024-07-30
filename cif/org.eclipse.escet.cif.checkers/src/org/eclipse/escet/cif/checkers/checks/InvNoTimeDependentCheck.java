//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.checkers.checks;

import java.util.Arrays;
import java.util.EnumSet;

import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEquationUtils;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TimeExpression;

/**
 * CIF check that disallows time-dependent invariants. A subset of invariants can be checked, by using
 * {@link #setInvKinds}.
 */
public class InvNoTimeDependentCheck extends CifCheckNoCompDefInst {
    /** The kinds of invariants to check. */
    private EnumSet<InvKind> invKindsToCheck = EnumSet.allOf(InvKind.class);

    /** The kind of invariant currently being checked, or {@code null} if not currently checking an invariant. */
    private InvKind curInvKind = null;

    /**
     * Sets which invariants to check. If this method is not used, all invariants are checked.
     *
     * @param invKinds The kinds of invariants to check.
     * @return This check, for chaining.
     */
    public InvNoTimeDependentCheck setInvKinds(InvKind... invKinds) {
        this.invKindsToCheck = EnumSet.copyOf(Arrays.asList(invKinds));
        return this;
    }

    @Override
    protected void preprocessInvariant(Invariant inv, CifCheckViolations violations) {
        // Check only the invariants that should be checked.
        if (!invKindsToCheck.contains(inv.getInvKind())) {
            return;
        }

        // Only check invariant predicate for violations. We do an explicit extra walk over the expression, as we don't
        // want to check other features of the invariant, such as its annotations.
        curInvKind = inv.getInvKind();
        walkExpression(inv.getPredicate(), violations);
        curInvKind = null;
    }

    @Override
    protected void preprocessTimeExpression(TimeExpression timeRef, CifCheckViolations violations) {
        if (curInvKind != null) {
            violations.add(timeRef, "Variable \"time\" is used in %s invariant",
                    getInvViolationDescription(curInvKind));
        }
    }

    @Override
    protected void preprocessContVariableExpression(ContVariableExpression contVarRef, CifCheckViolations violations) {
        if (curInvKind != null) {
            if (contVarRef.isDerivative()) {
                // Derivative: check its defining expressions.
                for (Expression deriv: CifEquationUtils.getDerivativesForContVar(contVarRef.getVariable(), false)) {
                    walkExpression(deriv, violations);
                }
            } else {
                // Continuous variable: violation.
                violations.add(contVarRef, "A continuous variable is used in %s invariant",
                        getInvViolationDescription(curInvKind));
            }
        }
    }

    @Override
    protected void preprocessAlgVariableExpression(AlgVariableExpression algVarRef, CifCheckViolations violations) {
        if (curInvKind != null) {
            // Check the defining expressions of the algebraic variable.
            for (Expression deriv: CifEquationUtils.getValuesForAlgVar(algVarRef.getVariable(), false)) {
                walkExpression(deriv, violations);
            }
        }
    }

    /**
     * Get a description of the kind of invariant that has a violation.
     *
     * @param invKind The invariant kind of the invariant with a violation.
     * @return The description.
     */
    private String getInvViolationDescription(InvKind invKind) {
        if (invKindsToCheck.size() == InvKind.values().length) {
            return "an"; // All invariants are checked.
        }

        return switch (invKind) {
            case EVENT_DISABLES -> "a state/event exclusion"
                    + (!invKindsToCheck.contains(InvKind.EVENT_NEEDS) ? " \"disables\"" : "");
            case EVENT_NEEDS -> "a state/event exclusion"
                    + (!invKindsToCheck.contains(InvKind.EVENT_DISABLES) ? " \"needs\"" : "");
            case STATE -> "a state";
        };
    }
}
