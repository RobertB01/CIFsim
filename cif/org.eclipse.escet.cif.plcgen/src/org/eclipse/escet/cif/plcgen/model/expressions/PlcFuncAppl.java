//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription;
import org.eclipse.escet.common.java.Assert;

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
        this.function = function;

        // Store the supplied arguments.
        arguments = mapc(argumentList.size());
        for (PlcNamedValue arg: argumentList) {
            arguments.put(arg.name, arg);
        }

        // Verify there is at least one argument and no duplicates.
        Assert.check(!argumentList.isEmpty()); // PLCs don't support calls without arguments.
        Assert.areEqual(arguments.size(), argumentList.size());

        // All supplied arguments should have a matching parameter.
        long paramMatches = Arrays.stream(function.parameters).filter(arg -> arguments.containsKey(arg.name)).count();
        Assert.check(paramMatches == argumentList.size()); // long and int are never "Assert.areEqual".
    }
}
