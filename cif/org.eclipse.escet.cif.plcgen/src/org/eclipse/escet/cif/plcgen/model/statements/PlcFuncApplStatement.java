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

package org.eclipse.escet.cif.plcgen.model.statements;

import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncAppl;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcFuncNotation;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;
import org.eclipse.escet.common.java.Assert;

/** Perform a function application as statement, discarding the return value. */
public class PlcFuncApplStatement extends PlcStatement {
    /** Function application to use as statement. The outer performed function must have a prefix notation. */
    public PlcFuncAppl funcApplExpr;

    /**
     * Constructor of the {@link PlcFuncApplStatement} class.
     *
     * @param funcApplExpr Function application to use as statement. The outer performed function must have a prefix
     *     notation.
     * @see PlcTarget#getSupportedFuncNotations
     */
    public PlcFuncApplStatement(PlcFuncAppl funcApplExpr) {
        Assert.check(!isEmptyIntersection(PlcFuncNotation.NOT_INFIX, funcApplExpr.function.notations),
                "Supported function call notation has no prefix forms.");
        this.funcApplExpr = funcApplExpr;
    }

    @Override
    public PlcStatement copy() {
        return new PlcFuncApplStatement(funcApplExpr);
    }
}
