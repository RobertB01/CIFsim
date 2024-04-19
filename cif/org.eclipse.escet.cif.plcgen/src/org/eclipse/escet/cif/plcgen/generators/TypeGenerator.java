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
import org.eclipse.escet.cif.plcgen.model.expressions.PlcEnumLiteral;
import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;

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
     * @param enumDecl Enumeration declaration to convert.
     * @return The PLC type generated for the enumeration.
     */
    public PlcType convertEnumDecl(EnumDecl enumDecl);

    /**
     * Get the PLC equivalent of the given CIF enumeration literal.
     *
     * @param enumLit Enumeration to convert.
     * @return The equivalent PLC value of the provided enum literal.
     */
    public PlcEnumLiteral getPlcEnumLiteral(EnumLiteral enumLit);
}
