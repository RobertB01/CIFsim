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

package org.eclipse.escet.cif.plcgen.generators;

import org.eclipse.escet.cif.plcgen.model.types.PlcStructType;
import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumDecl;
import org.eclipse.escet.cif.metamodel.cif.declarations.EnumLiteral;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcEnumLiteral;

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
     * Get the underlying structure type from the associated declaration type used in the generators.
     *
     * @param type Declaration type of the structure type being queried.
     * @return The underlying structure type.
     */
    public PlcStructType getStructureType(PlcType type);

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
