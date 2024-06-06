//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription;

/** Function application expression. */
public class PlcFuncAppl extends PlcExpression {
    /** Function being applied. */
    public final PlcBasicFuncDescription function;

    /** Arguments of the function application findable by their name. */
    public final Map<String, PlcNamedValue> arguments;

    /**
     * Constructor of the {@link PlcFuncAppl} class.
     *
     * @param function Function being applied.
     * @param argumentList Arguments of the function application.
     */
    public PlcFuncAppl(PlcBasicFuncDescription function, List<PlcNamedValue> argumentList) {
        super(function.deriveResultType(argumentList));
        this.function = function;

        // Store the supplied arguments.
        arguments = mapc(argumentList.size());
        for (PlcNamedValue arg: argumentList) {
            arguments.put(arg.name, arg);
        }

        // Arguments sanity checks are done in the "deriveResultType" function in the super call.
    }

    @Override
    public String toString() {
        return fmt("PlcFuncAppl(%s(%s) -> %s)", function.getFuncName(), String.join(", ", arguments.keySet()), type);
    }
}
