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

package org.eclipse.escet.cif.plcgen.model.functions;

import java.util.EnumSet;

import org.eclipse.escet.cif.plcgen.model.types.PlcAbstractType;
import org.eclipse.escet.common.java.Assert;

/** Description of a regular function. */
public class PlcPlainFuncDescription extends PlcBasicFuncDescription {
    /** Name of the function in infix notation, {@code null} if infix form does not exist. */
    public final String infixFuncName;

    /**
     * Binding of the function application for laying out the infix notation. Use
     * {@link PlcBasicFuncDescription.ExprBinding#NO_PRIORITY} for functions that have no infix notation.
     */
    public final ExprBinding infixBinding;

    /**
     * Constructor of the {@link PlcPlainFuncDescription} class.
     *
     * @param operation The semantic operation performed by the function.
     * @param prefixFuncName Name of the function in prefix notation, the empty string if the function name should not
     *     be used, or {@code null} if the prefix form does not exist.
     * @param parameters Parameters of the function.
     * @param infixFuncName Name of the function in infix notation, {@code null} if infix form does not exist.
     * @param infixBinding Binding of the function application for laying out the infix notation. Use
     *     {@link PlcBasicFuncDescription.ExprBinding#NO_PRIORITY} for functions that have no infix notation.
     * @param notations Notations of the function that are supported by the target. May get restricted based on
     *     available infix and prefix function names.
     * @param resultType Type of the result of the function.
     * @param typeExtension Condition for appending a {@code _TYPE} extension to a prefix function name.
     */
    public PlcPlainFuncDescription(PlcFuncOperation operation, String prefixFuncName,
            PlcParameterDescription[] parameters, String infixFuncName, ExprBinding infixBinding,
            EnumSet<PlcFuncNotation> notations, PlcAbstractType resultType, PlcFuncTypeExtension typeExtension)
    {
        super(operation, prefixFuncName, parameters, computeFuncApplNotations(prefixFuncName, infixFuncName, notations),
                resultType, typeExtension);
        Assert.implies(infixFuncName == null, (infixBinding == ExprBinding.NO_PRIORITY));

        this.infixFuncName = infixFuncName;
        this.infixBinding = infixBinding;
    }

    /**
     * Restrict available function application notations based on available function names.
     *
     * @param prefixFuncName Name of the function in prefix notation, the empty string if the function name should not
     *     be used, or {@code null} if the prefix form does not exist.
     * @param infixFuncName Name of the function in infix notation, {@code null} if infix form does not exist.
     * @param notations Notations of the function that are supported by the target. May get restricted based on
     *     available infix and prefix function names.
     * @return Restricted notation forms of applying this function.
     */
    private static EnumSet<PlcFuncNotation> computeFuncApplNotations(String prefixFuncName, String infixFuncName,
            EnumSet<PlcFuncNotation> notations)
    {
        notations = EnumSet.copyOf(notations); // Make a private copy to avoid changing caller data.
        if (infixFuncName == null) {
            notations.retainAll(PlcFuncNotation.NOT_INFIX);
        }
        if (prefixFuncName == null) {
            notations.retainAll(PlcFuncNotation.INFIX_ONLY);
        }
        return notations;
    }

    @Override
    public String getFuncName() {
        if (prefixFuncName != null && !prefixFuncName.isEmpty()) {
            return super.getFuncName();
        } else if (infixFuncName != null) {
            return "infix-\"" + infixFuncName + "\"";
        }
        return super.getFuncName();
    }
}
