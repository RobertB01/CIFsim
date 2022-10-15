//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifEvalUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.checkers.messages.LiteralMessage;
import org.eclipse.escet.cif.common.checkers.messages.ReportObjectTypeDescrMessage;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.VariableValue;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/**
 * CIF check that does not allow discrete variables with initial values that cannot be evaluated statically. Only checks
 * discrete variables with exactly one initial value.
 */
public class VarDiscOnlyStaticEvalInitCheck extends CifCheck {
    @Override
    protected void preprocessDiscVariable(DiscVariable var, CifCheckViolations violations) {
        // Ignore discrete variables that represent function parameters or local variables of functions.
        EObject parent = var.eContainer();
        if (!(parent instanceof ComplexComponent)) {
            return;
        }

        // Only supports discrete variable with exactly one initial value.
        VariableValue values = var.getValue();
        if (values != null && values.getValues().size() == 1) {
            Expression value = values.getValues().get(0);
            if (!CifValueUtils.hasSingleValue(value, true, true)) {
                // Report violation.
                violations.add(var,
                        new LiteralMessage("discrete variable has initial value that cannot be evaluated statically"));
            } else {
                try {
                    CifEvalUtils.eval(value, true);
                } catch (CifEvalException e) {
                    // Report violation.
                    violations.add(var, new LiteralMessage("discrete variable has "
                            + "initial value that cannot be evaluated statically, as the evaluation resulted in an "
                            + "evaluation error"));
                }
            }
        }
    }
}
