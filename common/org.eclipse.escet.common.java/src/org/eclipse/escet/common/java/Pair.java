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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Objects;

/**
 * Generic pair (2-tuple) class.
 *
 * @param <T1> The type of the left value of the pair.
 * @param <T2> The type of the right value of the pair.
 *
 * @see Triple
 */
public class Pair<T1, T2> {
    /** The left value of the pair. May be {@code null}. */
    public final T1 left;

    /** The right value of the pair. May be {@code null}. */
    public final T2 right;

    /**
     * Constructor for the {@link Pair} class. It is highly recommended to use the {@link #pair} method instead, as it
     * leads to simpler and shorter code.
     *
     * @param left The left value of the pair. May be {@code null}.
     * @param right The right value of the pair. May be {@code null}.
     */
    public Pair(T1 left, T2 right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Static constructor for the {@link Pair} class.
     *
     * @param <TL> The type of the left value of the pair.
     * @param <TR> The type of the right value of the pair.
     * @param left The left value of the pair. May be {@code null}.
     * @param right The right value of the pair. May be {@code null}.
     * @return The newly constructed pair.
     */
    public static <TL, TR> Pair<TL, TR> pair(TL left, TR right) {
        return new Pair<>(left, right);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(left) ^ Objects.hashCode(right);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pair<?, ?>)) {
            return false;
        }
        Pair<?, ?> other = (Pair<?, ?>)obj;
        return Objects.equals(left, other.left) && Objects.equals(right, other.right);
    }

    @Override
    public String toString() {
        return fmt("(%s, %s)", left, right);
    }
}
