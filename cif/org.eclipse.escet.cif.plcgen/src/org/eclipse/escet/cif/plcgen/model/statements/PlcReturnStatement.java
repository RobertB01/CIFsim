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

import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;

/**
 * Statement to optionally return a value to the caller, and end execution its the code block.
 *
 * <p>
 * In the PLC this functionality are two separate statements, they are merged as they occur together frequently.
 * </p>
 */
public class PlcReturnStatement extends PlcStatement {
    /** Value to return to the caller, may be {@code null}. */
    public PlcExpression returnValue;

    /**
     * Constructor of the {@link PlcReturnStatement} class.
     *
     * @param returnValue Value to return to the caller, may be {@code null}.
     */
    public PlcReturnStatement(PlcExpression returnValue) {
        this.returnValue = returnValue;
    }
}
