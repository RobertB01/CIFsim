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

/** Expression class for a floating point value. */
public class PlcRealLiteral extends PlcExpression {
    /** Value of the expression. */
    public final String value;

    /**
     * Constructor of the {@link PlcRealLiteral} class.
     *
     * @param value Value of the expression.
     */
    public PlcRealLiteral(String value) {
        this.value = value;
    }
}
