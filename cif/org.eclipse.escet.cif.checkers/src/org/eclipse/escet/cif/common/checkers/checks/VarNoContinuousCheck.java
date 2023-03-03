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

package org.eclipse.escet.cif.common.checkers.checks;

import org.eclipse.escet.cif.common.checkers.CifCheck;
import org.eclipse.escet.cif.common.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;

/** CIF check that does not allow continuous variables. */
public class VarNoContinuousCheck extends CifCheck {
    @Override
    protected void preprocessContVariable(ContVariable var, CifCheckViolations violations) {
        violations.add(var, "Variable is a continuous variable");
    }
}
