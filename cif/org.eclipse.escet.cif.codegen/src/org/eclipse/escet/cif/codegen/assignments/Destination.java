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

package org.eclipse.escet.cif.codegen.assignments;

import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.cif.codegen.typeinfos.TypeInfo;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;

/** Destination for a typed value in a target language. */
public class Destination implements DataValue {
    /** Empty code box to use as dummy for passing code. */
    private static final CodeBox EMPTY_BOX = new MemoryCodeBox();

    /**
     * Code to execute before the {@link #getData} or the {@link #getReference} return value becomes valid. May be
     * {@code null}.
     */
    private CodeBox preCode;

    /** Text denoting the value or reference to store into. */
    private final DataValue dataValue;

    /** Type information about the destination. */
    private final TypeInfo typeInfo;

    /**
     * Constructor of the {@link Destination} class.
     *
     * @param preCode Code to execute before the text becomes valid. May be {@code null}.
     * @param typeInfo Type information of the destination.
     * @param dataValue Data to assign to.
     */
    public Destination(CodeBox preCode, TypeInfo typeInfo, DataValue dataValue) {
        this.preCode = preCode;
        this.dataValue = dataValue;
        this.typeInfo = typeInfo;
    }

    /**
     * Get the code to execute before the destination becomes reachable.
     *
     * @return Code to perform before the {@link #getData} or {@link #getReference} value becomes valid. May be empty.
     */
    public CodeBox getCode() {
        if (preCode == null) {
            return EMPTY_BOX;
        }
        return preCode;
    }

    @Override
    public String getData() {
        return dataValue.getData();
    }

    @Override
    public String getReference() {
        return dataValue.getReference();
    }

    @Override
    public boolean isReferenceValue() {
        return dataValue.isReferenceValue();
    }

    @Override
    public boolean canBeReferenced() {
        return true; // Should be a full data value, can always be referenced.
    }

    /**
     * Retrieve the type information of the destination.
     *
     * @return The type information about the destination.
     */
    public TypeInfo getTypeInfo() {
        return typeInfo;
    }
}
