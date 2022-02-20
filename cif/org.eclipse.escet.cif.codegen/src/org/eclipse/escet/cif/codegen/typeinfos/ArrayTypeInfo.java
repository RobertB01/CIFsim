//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ListExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.java.Assert;

/** Generic type info about the array type. */
public abstract class ArrayTypeInfo extends ContainerTypeInfo {
    /** Length of the array, a negative value means "don't know". */
    public final int length;

    /**
     * Constructor of the {@link ArrayTypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     * @param childTIs Type information of the elements, must have length {@code 1}.
     * @param length Length of the array.
     */
    public ArrayTypeInfo(CifType cifType, TypeInfo[] childTIs, int length) {
        super(cifType, childTIs);
        Assert.check(childTIs.length == 1);

        this.length = length;
    }

    @Override
    public int getSize() {
        return length;
    }

    @Override
    protected String makeTypeName() {
        if (length < 0) {
            return "L" + childInfos[0].getTypeName();
        } else {
            return fmt("A%d%s", length, childInfos[0].getTypeName());
        }
    }

    /**
     * Construct the text denoting the element type in the target language type system.
     *
     * @return The text of the element type of the array in the target language.
     */
    public abstract String getElementTargetType();

    /**
     * Access an element of the array.
     *
     * @param containerCode Code to obtain the array.
     * @param indexCode Code to obtain the index value.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context.
     * @return Code expressing accessing the element in the array at the requested index.
     */
    public abstract ExprCode getProjectedValue(ExprCode containerCode, ExprCode indexCode, Destination dest,
            CodeContext ctxt);

    /**
     * Modify a container in-place.
     *
     * @param container Container being modified
     * @param partCode Code to obtain the new value of the part.
     * @param indexCode Code to obtain the index value.
     * @param ctxt Code context.
     * @return Code expressing modifying a container from the old container and the new part at the given index.
     */
    public abstract CodeBox modifyContainer(VariableInformation container, ExprCode partCode, ExprCode indexCode,
            CodeContext ctxt);

    /**
     * Convert the literal array value to the target language.
     *
     * @param expr Literal array expression to convert to the target language.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public abstract ExprCode convertLiteral(ListExpression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert a 'size' standard library function call to the target language.
     *
     * @param expr Argument of the function call.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public abstract ExprCode convertSizeStdLib(Expression expr, Destination dest, CodeContext ctxt);

    /**
     * Convert an 'empty' standard library function call to the target language.
     *
     * @param expr Argument of the function call.
     * @param dest Storage destination if available, else {@code null}.
     * @param ctxt Code context
     * @return Result of the conversion.
     */
    public abstract ExprCode convertEmptyStdLib(Expression expr, Destination dest, CodeContext ctxt);
}
