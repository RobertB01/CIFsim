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

package org.eclipse.escet.cif.codegen.simulink;

import org.eclipse.escet.cif.checkers.CifCheck;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;

/**
 * CIF check that does not allow input variables with types that are incompatible with Simulink. It allows only input
 * variables with a boolean, enumeration, integer, or real type, or arrays with such element types (vectors), or arrays
 * of arrays with such element types (matrices).
 */
public class VarInputOnlySimulinkCompatibleTypesCheck extends CifCheck {
    @Override
    protected void preprocessInputVariable(InputVariable var, CifCheckViolations violations) {
        if (!SimulinkTypeUtils.isGoodType(var.getType())) {
            violations.add(var.getType(),
                    "Input variable has a type that is not a boolean, enumeration, integer, or real type, or an array "
                            + "with such an element type, or an array of an array with such an element type");
        }
    }
}
