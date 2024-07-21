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

package org.eclipse.escet.cif.bdd.conversion.preconditions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;

/**
 * CIF check that allows discrete and input variables only if they have a supported type:
 * <ul>
 * <li>Boolean type.</li>
 * <li>Ranged integer type with a non-negative lower bound.</li>
 * <li>Enumeration type.</li>
 * </ul>
 */
public class CifBddVarOnlySpecificTypesCheck extends CifCheckNoCompDefInst {
    @Override
    protected void preprocessDiscVariable(DiscVariable discVar, CifCheckViolations violations) {
        // Ignore discrete variables that represent function parameters or local variables of functions.
        EObject parent = discVar.eContainer();
        if (!(parent instanceof ComplexComponent)) {
            return;
        }

        // Check discrete variable.
        checkType(discVar.getType(), "Discrete", violations);
    }

    @Override
    protected void preprocessInputVariable(InputVariable inputVar, CifCheckViolations violations) {
        checkType(inputVar.getType(), "Input", violations);
    }

    /**
     * Check the type of a discrete or input variable.
     *
     * @param type The type of the variable.
     * @param kindTxt The kind of variable.
     * @param violations The violations reported so far.
     */
    private void checkType(CifType type, String kindTxt, CifCheckViolations violations) {
        CifType normType = CifTypeUtils.normalizeType(type);

        if (normType instanceof BoolType) {
            // OK.
        } else if (normType instanceof IntType intType) {
            if (CifTypeUtils.isRangeless(intType)) {
                violations.add(type, "%s variable has a rangeless integer type", kindTxt);
            } else {
                if (intType.getLower() < 0) {
                    violations.add(type, "%s variable has an integer type that allows negative numbers", kindTxt);
                }
            }
        } else if (normType instanceof EnumType) {
            // OK.
        } else {
            violations.add(type, "%s variable does not have a boolean, integer or enumeration type", kindTxt);
        }
    }
}
