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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/** {@link Map}s helper methods. */
public class Maps {
    /** Constructor for the {@link Maps} class. */
    private Maps() {
        // Static class.
    }

    /**
     * Puts a pair into the map (in-place), and returns the map, to allow for chaining.
     *
     * @param <TK> The type of the keys of the map.
     * @param <TV> The type of the values of the map.
     * @param map The input map, to be modified in-place.
     * @param key The key of the pair to put into the map.
     * @param value The value of the pair to put into the map.
     * @return The input map.
     */
    public static <TK, TV> Map<TK, TV> put(Map<TK, TV> map, TK key, TV value) {
        map.put(key, value);
        return map;
    }

    /**
     * Creates an empty {@link LinkedHashMap} of the given type.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @return The newly created {@link LinkedHashMap}.
     */
    public static <K, V> Map<K, V> map() {
        return new LinkedHashMap<>();
    }

    /**
     * Creates an empty {@link LinkedHashMap} with the given initial capacity.
     *
     * @param <K> The type of the keys of the map.
     * @param <V> The type of the values of the map.
     * @param initialCapacity The initial capacity of the map.
     * @return The newly created {@link LinkedHashMap}.
     * @throws IllegalArgumentException If the initial capacity is negative.
     */
    public static <K, V> Map<K, V> mapc(int initialCapacity) {
        return new LinkedHashMap<>(initialCapacity);
    }

    /**
     * Returns a shallow copy of the given map.
     *
     * @param <KR> The type of the keys of the resulting map.
     * @param <KI> The type of the keys of the input map.
     * @param <VR> The type of the values of the resulting map.
     * @param <VI> The type of the values of the input map.
     * @param m The map to copy.
     *
     * @return A shallow copy of the given map.
     */
    public static <KR, KI extends KR, VR, VI extends VR> LinkedHashMap<KR, VR> copy(Map<KI, VI> m) {
        return new LinkedHashMap<>(m);
    }

    /**
     * Inverts a map such that a key-value pair becomes a value-key pair.
     *
     * <p>
     * It only supports inverting maps where the values in the map are unique (i.e. a one-to-one mapping or a
     * bijection), so no pairs are lost. Furthermore, all keys and values of the map must be non-{@code null}.
     * </p>
     *
     * @param <V> The type of the values of the map.
     * @param <K> The type of the keys of the map.
     * @param map The map to invert.
     * @return The inverted map.
     */
    public static <V, K> Map<V, K> invert(Map<K, V> map) {
        Map<V, K> inv = mapc(map.size());
        for (Entry<K, V> entry: map.entrySet()) {
            // Assert that the key and value are not 'null', otherwise the return value of 'put' is ambiguous.
            Assert.notNull(entry.getKey());
            Assert.notNull(entry.getValue());

            K prevValue = inv.put(entry.getValue(), entry.getKey());

            // For a one-to-one mapping, the previous value returned by 'put' should always be 'null'.
            Assert.check(prevValue == null, "Cannot invert the map, as it is not a one-to-one map.");
        }
        return inv;
    }
}
