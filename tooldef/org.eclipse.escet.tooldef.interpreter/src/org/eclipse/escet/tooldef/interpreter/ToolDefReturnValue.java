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

package org.eclipse.escet.tooldef.interpreter;

/** Result of a 'return' statement. */
public class ToolDefReturnValue {
    /** The return value. May be {@code null} if a {@code null} value is returned. */
    public final Object value;

    /**
     * Constructor for the {@link ToolDefReturnValue} class.
     *
     * @param value The return value. May be {@code null} if a {@code null} value is returned.
     */
    public ToolDefReturnValue(Object value) {
        this.value = value;
    }
}
