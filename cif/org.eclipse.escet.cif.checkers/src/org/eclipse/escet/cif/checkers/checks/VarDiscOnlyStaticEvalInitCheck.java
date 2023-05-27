//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/** CIF check that does not allow discrete variables with initial values that cannot be evaluated statically. */
public class VarDiscOnlyStaticEvalInitCheck extends CifCheck {
    @Override
    protected void preprocessDiscVariable(DiscVariable var, CifCheckViolations violations) {
        // Ignore discrete variables that represent function parameters or local variables of functions.
        EObject parent = var.eContainer();
        if (!(parent instanceof ComplexComponent)) {
            return;
        }

        // Check for 'any' initial value.
        VariableValue values = var.getValue();
        if (values == null) {
            return;
        }

        // Check if all initial values can be evaluated statically.
        for (Expression value: values.getValues()) {
            if (!CifValueUtils.hasSingleValue(value, true, true)) {
                violations.add(value, "Discrete variable has an initial value that cannot be evaluated statically");
            } else {
                try {
                    CifEvalUtils.eval(value, true);
                } catch (CifEvalException e) {
                    Expression reportExpr = (e.expr != null) ? e.expr : value;
                    violations.add(reportExpr,
                            "Discrete variable has an initial value that cannot be evaluated statically, "
                                    + "as evaluating it results in an evaluation error");
                }
            }
        }
    }
}
