//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.codegen.typeinfos.TypeInfoHelper.convertBinaryExpressionPattern;

import java.util.List;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.Destination;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.box.CodeBox;

/** Base class containing all information about a type for generating code. */
public abstract class TypeInfo {
    /** The CIF type used for creating this type information object. */
    public final CifType cifType;

    /**
     * Name of this type if available, else {@code null}.
     *
     * @see #getTypeName
     * @see #makeTypeName
     */
    private String typeName = null;

    /**
     * Constructor for {@link TypeInfo} class.
     *
     * @param cifType The CIF type used for creating this type information object.
     */
    public TypeInfo(CifType cifType) {
        this.cifType = cifType;
    }

    /**
     * Retrieve the unique type name for the type represented by the type info class.
     *
     * @return The unique type name for the type.
     */
    public final String getTypeName() {
        if (typeName == null) {
            typeName = makeTypeName();
        }
        return typeName;
    }

    /**
     * Construct the unique target type identifier for the type represented by the type info class.
     *
     * <p>
     * Conversion is performed as follows:
     * <ul>
     * <li>{@code bool} = {@code "B"},</li>
     * <li>{@code int} = {@code "I"},</li>
     * <li>{@code enum} = {@code "E"} (enum name is always ignored),</li>
     * <li>{@code real} = {@code "R"},</li>
     * <li>{@code string} = {@code "S"},</li>
     * <li>{@code array} = {@code "A"} &lt;fixed size&gt; &lt;element type&gt;,</li>
     * <li>{@code tuple} = {@code "T"} &lt;number of fields&gt; &lt;each field type&gt;</li>
     * </ul>
     * </p>
     *
     * <p>
     * Possible future extensions are
     * <ul>
     * <li>Bounded integer {@code Ix_y} with lower bound {@code x} and upper bound {@code y},</li>
     * <li>Unbounded list type {@code Le} with element type {@code e},</li>
     * <li>Variable length list type {@code Lx_ye} with lower bound {@code x}, upper bound {@code y}, and element type
     * {@code e},</li>
     * <li>sets, and</li>
     * <li>dictionaries.</li>
     * </ul>
     * </p>
     *
     * @return Unique name of the represented type.
     */
    protected abstract String makeTypeName();

    /**
     * Construct the text denoting the type in the target language type system.
     *
     * @return Text denoting the given CIF type in the target language.
     * @see #generateCode
     */
    public abstract String getTargetType();

    /**
     * Generate code in the target language required for representing the type, and for performing the required
     * operations in it.
     *
     * @param ctxt Code generation context.
     */
    public abstract void generateCode(CodeContext ctxt);

    /**
     * Append an assignment statement to the code, assigning the source value to the destination.
     *
     * @param code Source code to append the assignment to.
     * @param sourceValue Value to assign.
     * @param dest Destination to copy to.
     */
    public abstract void storeValue(CodeBox code, DataValue sourceValue, Destination dest);

    /**
     * Append an variable initialization statement to the code, defining the destination, and assigning the source value
     * to it.
     *
     * @param code Source code to append the assignment to.
     * @param sourceValue Value to assign.
     * @param dest Destination to copy to.
     */
    public abstract void declareInit(CodeBox code, DataValue sourceValue, Destination dest);

    /**
     * Convert an equality test to the target language.
     *
     * @param expr Equality test expression to convert.
     * @param dest Destination to store the result if available.
     * @param ctxt Code context of the expression.
     * @return The conversion result.
     */
    public ExprCode convertEqualsExpression(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.EQUAL, ctxt), dest,
                ctxt);
    }

    /**
     * Convert an unequality test to the target language.
     *
     * @param expr Equality test expression to convert.
     * @param dest Destination to store the result if available.
     * @param ctxt Code context of the expression.
     * @return The conversion result.
     */
    public ExprCode convertUnequalsExpression(BinaryExpression expr, Destination dest, CodeContext ctxt) {
        return convertBinaryExpressionPattern(expr, getBinaryExpressionTemplate(BinaryOperator.UNEQUAL, ctxt), dest,
                ctxt);
    }

    /**
     * Retrieve the text template to use for generating an expression with the provided binary operator by the type. The
     * template contains patterns ${left-value}, ${left-ref}, ${right-value}, or ${right-ref} to replace with values or
     * references from the left or right operand.
     *
     * <p>
     * The equality operators {@link BinaryOperator#EQUAL} and {@link BinaryOperator#UNEQUAL} are always present. The
     * comparison operators {@link BinaryOperator#LESS_THAN}, {@link BinaryOperator#LESS_EQUAL},
     * {@link BinaryOperator#GREATER_EQUAL} and {@link BinaryOperator#GREATER_THAN} only exist for types that support
     * ordering.
     * </p>
     *
     * @param binOp Operator that is being generated by the caller.
     * @param ctxt Code context of the expression.
     * @return The text template for the provided operator.
     */
    public abstract String getBinaryExpressionTemplate(BinaryOperator binOp, CodeContext ctxt);

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

    /**
     * Generate range bound checks for the provided value, if necessary.
     *
     * @param lhsType Type of the left hand side.
     * @param rhsType Type of the right hand side.
     * @param rhsValue Value to check
     * @param varType Type of the left hand side.
     * @param varName Name of the assigned variable.
     * @param errorTexts Texts to output in case of a range error.
     * @param level Nesting level.
     * @param code Generated range checks on the rhsValue.
     * @param ctxt Code generation context.
     */
    public abstract void checkRange(CifType lhsType, CifType rhsType, DataValue rhsValue, CifType varType,
            String varName, List<RangeCheckErrorLevelText> errorTexts, int level, CodeBox code, CodeContext ctxt);
}
