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

import org.eclipse.escet.cif.plcgen.model.expressions.PlcBasicFuncDescription.ExprBinding;

/** Function description extended with the semantic operation being performed in a function application. */
public class PlcSemanticFuncDescription extends PlcBasicFuncDescription {
    /** The semantic operation performed by the function application. */
    public final PlcFuncOperation operation;

    /**
     * Constructor of the {@link PlcSemanticFuncDescription} class.
     *
     * @param operation The semantic operation performed by the function application.
     * @param prefixFuncName Name of the function in prefix notation, or {@code null} if the prefix form does not exist.
     * @param prefixParameters Prefix notation properties of the function parameters.
     * @param infixFuncName Name of the function in infix notation, {@code null} if infix form does not exist. For
     *     single parameter functions the infix name is assumed to be a prefix directly attached to the parameter,
     *     otherwise the infix name is assumed to be between arguments surrounded by spaces.
     * @param infixPriority Priority of the function application in infix notation. Use {@link ExprBinding#NO_PRIORITY}
     *     for functions that have no infix notation.
     */
    public PlcSemanticFuncDescription(PlcFuncOperation operation, String prefixFuncName,
            PlcParameterDescription[] prefixParameters, String infixFuncName, ExprBinding infixPriority)
    {
        super(prefixFuncName, prefixParameters, infixFuncName, infixPriority);
        this.operation = operation;
    }

    /**
     * Constructor of the {@link PlcSemanticFuncDescription} class for a function without infix notation.
     *
     * @param operation The semantic operation performed by the function application.
     * @param prefixFuncName Prefix notation properties of the function parameters.
     * @param prefixParameters Names of the function parameters (input, output, in-out) in prefix notation.
     */
    public PlcSemanticFuncDescription(PlcFuncOperation operation, String prefixFuncName,
            PlcParameterDescription[] prefixParameters)
    {
        this(operation, prefixFuncName, prefixParameters, null, ExprBinding.NO_PRIORITY);
    }
}
