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

package org.eclipse.escet.tooldef.runtime;

import org.eclipse.escet.common.java.Assert;

/**
 * Wraps a ToolDef value for proper value equality.
 *
 * @param <T> The type of the wrapped value.
 */
public class ToolDefEqWrap<T> {
    /** The value that is wrapped. */
    public final T value;

    /**
     * Constructor for the {@link ToolDefEqWrap} class.
     *
     * @param value The value to wrap.
     */
    private ToolDefEqWrap(T value) {
        this.value = value;
    }

    /**
     * Creates a wrapped value. If the given value is already wrapped, return the already wrapped value.
     *
     * @param <T> The type of the value that is to be wrapped.
     * @param value The potentially already wrapped value.
     * @return The wrapped value.
     */
    @SuppressWarnings("unchecked")
    public static <T> ToolDefEqWrap<T> wrap(T value) {
        if (value instanceof ToolDefEqWrap) {
            return (ToolDefEqWrap<T>)value;
        }
        return new ToolDefEqWrap<>(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        Assert.check(obj instanceof ToolDefEqWrap);
        ToolDefEqWrap<?> other = (ToolDefEqWrap<?>)obj;
        return ToolDefRuntimeUtils.equalValues(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return ToolDefRuntimeUtils.hashValue(value);
    }

    @Override
    public String toString() {
        return ToolDefRuntimeUtils.valueToStr(value);
    }
}
