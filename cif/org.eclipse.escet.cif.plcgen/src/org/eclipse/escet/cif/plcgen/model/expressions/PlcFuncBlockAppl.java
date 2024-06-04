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

package org.eclipse.escet.cif.plcgen.model.expressions;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFunctionBlockDescription;
import org.eclipse.escet.cif.plcgen.model.types.PlcFuncBlockType;

/** Function application of a function block. */
public class PlcFuncBlockAppl extends PlcFuncAppl {
    /** Variable holding the function block instance data. */
    public final PlcBasicVariable variable;

    /**
     * Constructor of the {@link PlcFuncBlockAppl} class.
     *
     * @param variable Variable holding the function block instance data.
     * @param argumentList Arguments of the function application.
     */
    public PlcFuncBlockAppl(PlcBasicVariable variable, List<PlcNamedValue> argumentList) {
        super(getFuncBlockDescription(variable), argumentList);
        this.variable = variable;
    }

    /**
     * Extract the function block description from the variable type.
     *
     * @param variable Variable to use to get the function block description.
     * @return The retrieved function block description.
     */
    private static PlcFunctionBlockDescription getFuncBlockDescription(PlcBasicVariable variable) {
        if (!(variable.type instanceof PlcFuncBlockType blockType)) {
            throw new AssertionError(fmt("Variable \"%s\" is not a function block instance.", variable.varName));
        }
        return blockType.funcBlockcDescription;
    }
}
