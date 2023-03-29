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

package org.eclipse.escet.cif.plcgen.model.statements;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcFuncAppl;

/** Perform a function application as statement, discarding the return value. */
public class PlcFuncApplStatement extends PlcStatement {
    /** Function application to use as statement. */
    public PlcFuncAppl funcApplExpr;

    /**
     * Constructor of the {@link PlcFuncApplStatement} class.
     *
     * @param funcApplExpr Function application to use as statement.
     */
    public PlcFuncApplStatement(PlcFuncAppl funcApplExpr) {
        this.funcApplExpr = funcApplExpr;
    }

    @Override
    public PlcStatement copy() {
        return new PlcFuncApplStatement(funcApplExpr);
    }
}
