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

package org.eclipse.escet.cif.plcgen.generators;

import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.TupleType;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.plcgen.targets.PlcTarget;

/** Code generation interface for a {@link DefaultTypeGenerator}. */
public interface TypeGenerator {
    /**
     * Convert a CIF type to a PLC type.
     *
     * @param type CIF type to convert.
     * @return The associated PLC type.
     */
    public PlcType convertType(CifType type);

    /**
     * Get the structure type from the associated CIF tuple type.
     *
     * @param tupleType CIF tuple type to convert.
     * @return The structure type associated with the given CIF tuple type.
     */
    public PlcStructType convertTupleType(TupleType tupleType);

    /**
     * Convert a CIF enumeration declaration to a named PLC enumeration.
     *
     * <p>
     * Conversion should follow the {@link PlcTarget#getActualEnumerationsConversion} setting.
     * </p>
     *
     * @param enumDecl Enumeration declaration to convert.
     */
    public void convertEnumDecl(EnumDecl enumDecl);

    /**
     * Convert a CIF enumeration literal to a value.
     *
     * <p>
     * Conversion should follow the {@link PlcTarget#getActualEnumerationsConversion} setting.
     * </p>
     *
     * @param enumLit Enumeration literal to convert.
     * @return The converted value.
     */
    public PlcExpression convertEnumLiteral(EnumLiteral enumLit);
}
