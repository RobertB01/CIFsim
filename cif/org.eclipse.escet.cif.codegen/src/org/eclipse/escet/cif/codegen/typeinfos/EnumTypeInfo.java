//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.codegen.typeinfos;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.metamodel.cif.expressions.EnumLiteralExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.EnumType;
import org.eclipse.escet.common.box.CodeBox;

/**
 * Type information about the enumeration type/
 *
 * <p>
 * Note: In a linearized mode, there is only one enumeration type. In such a case, the derived class may decide not to
 * use the {@link #enumType} field.
 * </p>
 */
public abstract class EnumTypeInfo extends TypeInfo {
    /** The enumeration type represented by the type information, possibly {@code null}. */
    protected final EnumType enumType;

    /**
     * Constructor of the {@link EnumTypeInfo} class.
     *
     * @param enumType The enumeration type represented by the type information.
     */
    public EnumTypeInfo(EnumType enumType) {
        super(enumType);
        this.enumType = enumType;
    }

    @Override
    protected String makeTypeName() {
        return "E";
    }

    /**
     * Convert an enum literal expression to the target language.
     *
     * @param expr Expression to convert.
     * @param dest Destination to write the value to if available, else {@code null}.
     * @param ctxt Code generation context.
     * @return Code fragment representing the value.
     */
    public abstract ExprCode convertEnumLiteral(EnumLiteralExpression expr, Destination dest, CodeContext ctxt);

    @Override
    public void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType, String varName,
            List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt)
    {
        // Nothing to do, range is always valid.
    }
}
