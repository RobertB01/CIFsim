//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.compiler;

import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;

/** Input variable checker. Input variables are unsupported. */
public class InputVariableChecker {
    /** Constructor for the {@link InputVariableChecker} class. */
    private InputVariableChecker() {
        // Static class.
    }

    /**
     * Recursively checks the component for components with input variables.
     *
     * @param comp The component.
     */
    public static void checkInputVars(ComplexComponent comp) {
        // Check locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof InputVariable) {
                String msg = fmt("Input variable \"%s\" is currently not supported by the CIF simulator.",
                        getAbsName(decl));
                throw new UnsupportedException(msg);
            }
        }

        // Check recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                checkInputVars((ComplexComponent)child);
            }
        }
    }
}
