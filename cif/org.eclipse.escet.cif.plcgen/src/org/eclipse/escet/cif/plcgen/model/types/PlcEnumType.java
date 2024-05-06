//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.types;

import java.util.List;

import org.eclipse.escet.cif.plcgen.model.declarations.PlcDeclaredType;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcEnumLiteral;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.common.java.Lists;

/** PLC enum type. */
public class PlcEnumType extends PlcType implements PlcDeclaredType {
    /** Name of the enum type. */
    public final String typeName;

    /** The literals of the enum type. */
    public final List<PlcEnumLiteral> literals;

    /**
     * Constructor for the {@link PlcEnumType} class.
     *
     * @param typeName Name of the enum type.
     * @param literalNames The names of the iterals of the enum type.
     */
    public PlcEnumType(String typeName, List<String> literalNames) {
        this.typeName = typeName;
        this.literals = literalNames.stream().map(name -> new PlcEnumLiteral(name, this)).collect(Lists.toList());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PlcEnumType enumType)) {
            return false;
        }
        return typeName.equals(enumType.typeName) && literals.equals(enumType.literals);
    }

    @Override
    public int hashCode() {
        return typeName.hashCode() + 19 * literals.hashCode();
    }

    /**
     * Get an enum literal by its index.
     *
     * @param literalIndex Index of the enum literal to provide.
     * @return The requested enum literal.
     */
    public PlcExpression getLiteral(int literalIndex) {
        return literals.get(literalIndex);
    }
}
