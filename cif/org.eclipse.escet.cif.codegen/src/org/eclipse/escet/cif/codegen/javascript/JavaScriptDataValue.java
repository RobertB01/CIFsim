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

package org.eclipse.escet.cif.codegen.javascript;

import org.eclipse.escet.cif.codegen.DataValue;
import org.eclipse.escet.common.java.Assert;

/** A value in the JavaScript target language. */
public class JavaScriptDataValue implements DataValue {
    /** The stored code fragment to the data. */
    protected final String value;

    /**
     * Constructor for {@link JavaScriptDataValue} class.
     *
     * @param value Text denoting the returned value.
     */
    public JavaScriptDataValue(String value) {
        this.value = value;
    }

    @Override
    public String getData() {
        return value;
    }

    @Override
    public String getReference() {
        throw new RuntimeException("Reference access not supported.");
    }

    @Override
    public String toString() {
        Assert.fail("Do not print data value object!");
        return "";
    }

    @Override
    public boolean isReferenceValue() {
        return false; // Values versus references are handled entirely by the JavaScript runtime.
    }

    @Override
    public boolean canBeReferenced() {
        return false; // Values versus references are handled entirely by the JavaScript runtime.
    }
}
