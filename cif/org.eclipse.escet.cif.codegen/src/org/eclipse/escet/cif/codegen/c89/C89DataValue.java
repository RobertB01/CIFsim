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

package org.eclipse.escet.cif.codegen.c89;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.str;

import org.eclipse.escet.cif.codegen.CodeContext;
import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.ExprCode;
import org.eclipse.escet.cif.codegen.assignments.VariableInformation;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.common.box.CodeBox;

/** A data value in the C89 target language. */
public class C89DataValue implements DataValue {
    /** Kind of value represented by 'value'. */
    private ValueKind valueKind;

    /** Text of the value. How to interpret the text depends on the value of 'valueKind'. */
    private String value;

    /**
     * Constructor for {@link C89DataValue} class.
     *
     * @param value Text of the value.
     * @param valueKind Kind of value represented by 'value'.
     */
    protected C89DataValue(String value, ValueKind valueKind) {
        this.value = value;
        this.valueKind = valueKind;
    }

    @Override
    public String getData() {
        switch (valueKind) {
            case DATA:
            case LITERAL:
            case COMPUTED:
                return value;

            case POINTER:
                return "*(" + value + ")";
        }
        throw new RuntimeException("Unexpected kind of value: " + str(valueKind));
    }

    @Override
    public String getReference() {
        switch (valueKind) {
            case DATA:
                return "&(" + value + ")";

            case POINTER:
                return value;

            case LITERAL:
            case COMPUTED:
                throw new RuntimeException("Cannot construct a pointer to a literal");
        }
        throw new RuntimeException("Unexpected kind of value: " + str(valueKind));
    }

    @Override
    public boolean isReferenceValue() {
        return valueKind.equals(ValueKind.POINTER);
    }

    @Override
    public boolean canBeReferenced() {
        switch (valueKind) {
            case DATA:
            case POINTER:
                return true;

            case LITERAL:
            case COMPUTED:
                return false;
        }
        throw new RuntimeException("Unexpected kind of value: " + str(valueKind));
    }

    /**
     * Can the value be safely used more than once?
     *
     * @return Whether the value can be used again without causing unwanted side effects.
     */
    public boolean canBeReUsed() {
        return !valueKind.equals(ValueKind.COMPUTED);
    }

    /** Kind of value represented by the 'value' text. */
    protected static enum ValueKind {
        /** Stored data. */
        DATA,

        /** Pointer to stored data. */
        POINTER,

        /** Literal value without address. */
        LITERAL,

        /** Computed value without address. */
        COMPUTED,
    }

    /**
     * Construct a value that is a literal value, which cannot have a pointer to it.
     *
     * @param value Value denoting the literal.
     * @return The constructed data value.
     * @see #makeReference
     */
    public static C89DataValue makeLiteral(String value) {
        return new C89DataValue(value, ValueKind.LITERAL);
    }

    /**
     * Construct a value that is a computed value, which cannot have a pointer to it.
     *
     * @param value Value denoting the literal.
     * @return The constructed data value.
     * @see #makeReference
     */
    public static C89DataValue makeComputed(String value) {
        return new C89DataValue(value, ValueKind.COMPUTED);
    }

    /**
     * Construct a value that is the data itself (and not a pointer to it).
     *
     * @param value Value denoting the reference.
     * @return The constructed data value.
     * @see #makeReference
     */
    public static C89DataValue makeValue(String value) {
        return new C89DataValue(value, ValueKind.DATA);
    }

    /**
     * Construct a value that is a pointer to the data.
     *
     * @param value Value denoting the pointer.
     * @return The constructed data value.
     * @see #makeValue
     */
    public static C89DataValue makeReference(String value) {
        return new C89DataValue(value, ValueKind.POINTER);
    }

    /**
     * Make a data value referencable.
     *
     * @param value Value to make referencable.
     * @param valueTi Type of the value.
     * @param ctxt Code generation context.
     * @param box Storage of generated code (if any).
     * @return Text denoting the reference in the C89 target language.
     */
    public static String constructReference(DataValue value, TypeInfo valueTi, CodeContext ctxt, CodeBox box) {
        if (value.canBeReferenced()) {
            return value.getReference();
        }

        // Not a reference, store the value, then use the stored value instead.
        VariableInformation varInfo = ctxt.makeTempVariable(valueTi, "deref_store");
        box.add("%s %s = %s;", valueTi.getTargetType(), varInfo.targetName, value.getData());
        return "&" + varInfo.targetName;
    }

    /**
     * Make a data value referencable.
     *
     * @param value Value to make referencable.
     * @param valueTi Type of the value.
     * @param ctxt Code generation context.
     * @param exprCode Storage of generated code (if any).
     * @return Text denoting the reference in the C89 target language.
     */
    public static String constructReference(DataValue value, TypeInfo valueTi, CodeContext ctxt, ExprCode exprCode) {
        if (value.canBeReferenced()) {
            return value.getReference(); // Avoid creating a code box if data value is referencable already.
        }

        CodeBox code = ctxt.makeCodeBox();
        String result = constructReference(value, valueTi, ctxt, code);
        if (!code.isEmpty()) {
            exprCode.add(code);
        }
        return result;
    }

    @Override
    public String toString() {
        throw new RuntimeException(fmt("Do not try to print a C89 data value (contains \"%s\"", value));
    }
}
