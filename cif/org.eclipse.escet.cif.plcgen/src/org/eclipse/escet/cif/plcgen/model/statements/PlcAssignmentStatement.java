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

import org.eclipse.escet.cif.plcgen.model.declarations.PlcBasicVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;
import org.eclipse.escet.common.java.Assert;

/** Assignment statement to assign (part of) a variable. */
public class PlcAssignmentStatement extends PlcStatement {
    /** Variable (part) to assign to. */
    public PlcVarExpression lhs;

    /** Value to assign. */
    public PlcExpression value;

    /**
     * Constructor of the {@link PlcAssignmentStatement} class.
     *
     * @param lhsVar Variable to assign to.
     * @param value Value to assign.
     */
    public PlcAssignmentStatement(PlcBasicVariable lhsVar, PlcExpression value) {
        this(new PlcVarExpression(lhsVar), value);
    }

    /**
     * Constructor of the {@link PlcAssignmentStatement} class.
     *
     * @param lhs Variable (part) to assign to.
     * @param value Value to assign.
     */
    public PlcAssignmentStatement(PlcVarExpression lhs, PlcExpression value) {
        this.lhs = lhs;
        this.value = value;

        Assert.areEqual(lhs.type, value.type);
    }

    @Override
    public PlcAssignmentStatement copy() {
        return new PlcAssignmentStatement(lhs, value);
    }

    @Override
    public boolean isProperPlcStatement() {
        return true;
    }
}
