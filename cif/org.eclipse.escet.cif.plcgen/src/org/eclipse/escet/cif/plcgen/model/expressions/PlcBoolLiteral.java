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

import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;

/** Expression class for a boolean value. */
public class PlcBoolLiteral extends PlcExpression {
    /** Value of the expression. */
    public final boolean value;

    /**
     * Constructor of the {@link PlcBoolLiteral} class.
     *
     * @param value Value of the expression.
     */
    public PlcBoolLiteral(boolean value) {
        super(PlcElementaryType.BOOL_TYPE);
        this.value = value;
    }
}
