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

package org.eclipse.escet.common.java;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Objects;

/**
 * Generic triple (3-tuple) class.
 *
 * @param <T1> The type of the first value of the triple.
 * @param <T2> The type of the second value of the triple.
 * @param <T3> The type of the third value of the triple.
 *
 * @see Pair
 */
public class Triple<T1, T2, T3> {
    /** The first value of the triple. May be {@code null}. */
    public final T1 first;

    /** The second value of the triple. May be {@code null}. */
    public final T2 second;

    /** The third value of the triple. May be {@code null}. */
    public final T3 third;

    /**
     * Constructor for the {@link Triple} class. It is highly recommended to use the {@link #triple} method instead, as
     * it leads to simpler and shorter code.
     *
     * @param first The first value of the triple. May be {@code null}.
     * @param second The second value of the triple. May be {@code null}.
     * @param third The third value of the triple. May be {@code null}.
     */
    public Triple(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Static constructor for the {@link Triple} class.
     *
     * @param <T1> The type of the first value of the triple.
     * @param <T2> The type of the second value of the triple.
     * @param <T3> The type of the third value of the triple.
     * @param first The first value of the triple. May be {@code null}.
     * @param second The second value of the triple. May be {@code null}.
     * @param third The third value of the triple. May be {@code null}.
     * @return The newly constructed triple.
     */
    public static <T1, T2, T3> Triple<T1, T2, T3> triple(T1 first, T2 second, T3 third) {
        return new Triple<>(first, second, third);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(first) ^ Objects.hashCode(second) ^ Objects.hashCode(third);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Triple<?, ?, ?>)) {
            return false;
        }
        Triple<?, ?, ?> other = (Triple<?, ?, ?>)obj;
        return Objects.equals(first, other.first) && Objects.equals(second, other.second)
                && Objects.equals(third, other.third);
    }

    @Override
    public String toString() {
        return fmt("(%s, %s, %s)", first, second, third);
    }
}
