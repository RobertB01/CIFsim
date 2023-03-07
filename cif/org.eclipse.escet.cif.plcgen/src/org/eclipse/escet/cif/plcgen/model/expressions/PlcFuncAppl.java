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

import java.util.List;

import org.eclipse.escet.common.java.Assert;

/** Function application expression. */
public class PlcFuncAppl extends PlcExpression {
    /** Function being applied. */
    public final PlcBasicFuncDescription function;

    /** Arguments of the function application. */
    public final List<PlcNamedValue> arguments;

    /**
     * Constructor of the {@link PlcFuncAppl} class.
     *
     * @param function Function being applied.
     * @param arguments Arguments of the function application.
     */
    public PlcFuncAppl(PlcBasicFuncDescription function, List<PlcNamedValue> arguments) {
        this.function = function;
        this.arguments = arguments;

        Assert.check(!arguments.isEmpty());
    }
}
