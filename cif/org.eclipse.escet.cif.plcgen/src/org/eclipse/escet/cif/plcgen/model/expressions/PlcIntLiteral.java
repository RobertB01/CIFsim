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

/** Expression class for an integer value. */
public class PlcIntLiteral extends PlcExpression {
    /** Value of the expression. */
    public final int value;

    /**
     * Constructor of the {@link PlcIntLiteral} class.
     *
     * @param value Value of the expression.
     */
    public PlcIntLiteral(int value) {
        this.value = value;
    }
}
