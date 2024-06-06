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

import org.eclipse.escet.cif.plcgen.model.types.PlcEnumType;

/** Expression class for an enumeration literal. */
public class PlcEnumLiteral extends PlcExpression {
    /** Value of the literal. */
    public final String value;

    /**
     * Constructor of the {@link PlcEnumLiteral} class.
     *
     * @param value Value of the literal.
     * @param type Type of the literal.
     */
    public PlcEnumLiteral(String value, PlcEnumType type) {
        super(type);
        this.value = value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof PlcEnumLiteral otherLiteral && value.equals(otherLiteral.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "PlcEnumLiteral(\"" + value + "\")";
    }
}
