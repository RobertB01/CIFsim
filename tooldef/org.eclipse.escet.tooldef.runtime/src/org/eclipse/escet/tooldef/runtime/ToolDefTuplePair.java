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

package org.eclipse.escet.tooldef.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

/**
 * ToolDef 2-tuple (pair).
 *
 * @param <T1> The type of the left value of the pair.
 * @param <T2> The type of the right value of the pair.
 */
public class ToolDefTuplePair<T1, T2> extends ToolDefTuple {
    /** The left value of the pair. May be {@code null}. */
    public final T1 left;

    /** The right value of the pair. May be {@code null}. */
    public final T2 right;

    /**
     * Constructor for the {@link ToolDefTuplePair} class.
     *
     * @param left The left value of the pair. May be {@code null}.
     * @param right The right value of the pair. May be {@code null}.
     */
    public ToolDefTuplePair(T1 left, T2 right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int size() {
        return 2;
    }

    @Override
    protected void collectValues(List<Object> values) {
        values.add(left);
        values.add(right);
    }

    @Override
    public Object getValue(int idx) {
        if (idx == 0) {
            return left;
        }
        if (idx == 1) {
            return right;
        }
        throw new IllegalArgumentException("Invalid idx: " + idx);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ToolDefTuplePair<?, ?>)) {
            return false;
        }
        ToolDefTuplePair<?, ?> other = (ToolDefTuplePair<?, ?>)obj;
        return ToolDefRuntimeUtils.equalValues(left, other.left) && ToolDefRuntimeUtils.equalValues(right, other.right);
    }

    @Override
    public int hashCode() {
        return ToolDefTuplePair.class.hashCode() ^ ToolDefRuntimeUtils.hashValue(left)
                ^ ToolDefRuntimeUtils.hashValue(right);
    }

    @Override
    public String toString() {
        return fmt("(%s, %s)", ToolDefRuntimeUtils.valueToStr(left), ToolDefRuntimeUtils.valueToStr(right));
    }
}
