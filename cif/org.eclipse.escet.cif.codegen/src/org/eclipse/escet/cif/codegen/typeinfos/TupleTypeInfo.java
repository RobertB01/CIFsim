//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.metamodel.cif.expressions.TupleExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** Generic type info about the tuple type. */
public abstract class TupleTypeInfo extends ContainerTypeInfo {
    /**
     * Constructor of the {@link TupleTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param fieldTypes Type information of the fields, ordered by field index.
     */
    public TupleTypeInfo(CifType cifType, TypeInfo[] fieldTypes) {
        super(cifType, fieldTypes);
    }

    @Override
    public String makeTypeName() {
        String[] parts = new String[1 + childInfos.length];
        parts[0] = fmt("T%d", childInfos.length);
        for (int i = 0; i < childInfos.length; i++) {
            parts[i + 1] = childInfos[i].getTypeName();
        }
        return StringUtils.join(parts);
    }

    /**
     * Get the number of fields in the tuple.
     *
     * @return The number of fields in the tuple.
     */
    public int getFieldCount() {
        return childInfos.length;
    }

    /**
     * Convert the literal tuple value to the target language.
     *
     * @param expr Literal tuple expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public abstract ExprCode convertLiteral(TupleExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Modify a container in-place.
     *
     * @param container Container being modified
     * @param partCode Code to obtain the new value of the part.
     * @param index Field number that is changed.
     * @param ctxt Code context.
     * @return Code expressing modifying a container from the old container and the new part at the given index.
     */
    public abstract CodeBox modifyContainer(VariableInformation container, ExprCode partCode, int index,
            CodeContext ctxt);

    /**
     * Access a field of the tuple.
     *
     * @param childCode Code to obtain the tuple.
     * @param index Index number of the field (not normalized).
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Code expressing accessing the requested field.
     */
    public abstract ExprCode getProjectedValue(ExprCode childCode, int index, Destination dest, CodeContext ctxt);

    /**
     * Extend a value denoting a tuple of this type with a projection at a field.
     *
     * @param value Value to extend.
     * @param safe Whether the value is safe to extend with projection without adding parentheses (if in doubt, use
     *     {@code false}).
     * @param index Index of the field in the tuple.
     * @return The projected value.
     */
    public abstract String appendProjection(String value, boolean safe, int index);
}
