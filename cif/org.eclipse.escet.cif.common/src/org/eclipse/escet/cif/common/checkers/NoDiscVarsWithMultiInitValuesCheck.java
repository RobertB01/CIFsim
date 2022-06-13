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

package org.eclipse.escet.cif.common.checkers;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;

/** CIF check that does not allow discrete variables with multiple potential initial values. */
public class NoDiscVarsWithMultiInitValuesCheck extends CifCheck {
    @Override
    protected void preprocessDiscVariable(DiscVariable var, CifCheckViolations violations) {
        // Ignore discrete variables that represent function parameters or local variables of functions.
        EObject parent = var.eContainer();
        if (!(parent instanceof ComplexComponent)) {
            return;
        }

        // Ignore variables implicit default value.
        if (var.getValue() == null) {
            return;
        }

        // Check number of potential initial values.
        int count = var.getValue().getValues().size();
        if (count == 0) { // 0 means 'any' initial value.
            violations.add(var, "discrete variable has multiple potential initial values (any value in its domain)");
        } else if (count > 1) {
            violations.add(var, fmt("discrete variable has multiple (%d) potential initial values", count));
        }
    }
}
