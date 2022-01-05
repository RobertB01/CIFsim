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

package org.eclipse.escet.tooldef.runtime;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * ToolDef map value, with proper value equality.
 *
 * @param <K> The type of the keys of the map.
 * @param <V> The type of the values of the map.
 */
public class ToolDefMap<K, V> implements Map<K, V> {
    /** The underlying storage. */
    private final Map<ToolDefEqWrap<K>, ToolDefEqWrap<V>> storage;

    /** Constructor for the {@Link ToolDefMap} class. */
    public ToolDefMap() {
        storage = map();
    }

    /**
     * Constructor for the {@Link ToolDefMap} class.
     *
     * @param initialCapacity The initial capacity of the map.
     * @throws IllegalArgumentException If the initial capacity is negative.
     */
    public ToolDefMap(int initialCapacity) {
        storage = mapc(initialCapacity);
    }

    /**
     * Constructor for the {@Link ToolDefMap} class.
     *
     * @param map The map whose entries are to be placed into this map.
     * @throws NullPointerException If the given map is {@code null}.
     */
    public ToolDefMap(Map<? extends K, ? extends V> map) {
        storage = mapc(map.size());
        for (Entry<? extends K, ? extends V> entry: map.entrySet()) {
            ToolDefEqWrap<K> key = ToolDefEqWrap.wrap((K)entry.getKey());
            ToolDefEqWrap<V> value = ToolDefEqWrap.wrap((V)entry.getValue());
            storage.put(key, value);
        }
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        @SuppressWarnings("unchecked")
        ToolDefEqWrap<K> wrappedKey = ToolDefEqWrap.wrap((K)key);
        return storage.containsKey(wrappedKey);
    }

    @Override
    public boolean containsValue(Object value) {
        @SuppressWarnings("unchecked")
        ToolDefEqWrap<V> wrappedValue = ToolDefEqWrap.wrap((V)value);
        return storage.containsValue(wrappedValue);
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new ToolDefEntrySet<>(storage.entrySet());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ToolDefMap)) {
            return false;
        }

        ToolDefMap<?, ?> other = (ToolDefMap<?, ?>)obj;
        if (this.size() != other.size()) {
            return false;
        }

        Iterator<Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>>> iter;
        iter = storage.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>> entry = iter.next();
            ToolDefEqWrap<K> key = entry.getKey();
            ToolDefEqWrap<V> value = entry.getValue();
            ToolDefEqWrap<?> otherValue = other.storage.get(key);
            if (!value.equals(otherValue)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public V get(Object key) {
        @SuppressWarnings("unchecked")
        ToolDefEqWrap<K> wrappedKey = ToolDefEqWrap.wrap((K)key);
        ToolDefEqWrap<V> rslt = storage.get(wrappedKey);
        return (rslt == null) ? null : rslt.value;
    }

    @Override
    public int hashCode() {
        int rslt = ToolDefMap.class.hashCode();
        Iterator<Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>>> iter;
        iter = storage.entrySet().iterator();
        while (iter.hasNext()) {
            rslt += iter.next().hashCode();
        }
        return rslt;
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public Set<K> keySet() {
        return ToolDefSet.wrap(storage.keySet());
    }

    @Override
    public V put(K key, V value) {
        ToolDefEqWrap<K> wrappedKey = ToolDefEqWrap.wrap(key);
        ToolDefEqWrap<V> wrappedValue = ToolDefEqWrap.wrap(value);
        ToolDefEqWrap<V> rslt = storage.put(wrappedKey, wrappedValue);
        return (rslt == null) ? null : rslt.value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        // TODO: May increase performance by using bulk operation.
        for (Entry<? extends K, ? extends V> entry: m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        @SuppressWarnings("unchecked")
        ToolDefEqWrap<K> wrappedKey = ToolDefEqWrap.wrap((K)key);
        ToolDefEqWrap<V> rslt = storage.remove(wrappedKey);
        return (rslt == null) ? null : rslt.value;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("{");
        for (Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>> entry: storage.entrySet()) {
            if (txt.length() > 1) {
                txt.append(", ");
            }
            txt.append(entry.getKey().toString());
            txt.append(": ");
            txt.append(entry.getValue().toString());
        }
        txt.append("}");
        return txt.toString();
    }

    @Override
    public Collection<V> values() {
        // TODO: May increase performance by introducing a ToolDefCollection to
        // wrap the values collection instead of creating a new one.
        ToolDefList<V> rslt = new ToolDefList<>(storage.size());
        for (ToolDefEqWrap<V> wrap: storage.values()) {
            rslt.add(wrap.value);
        }
        return rslt;
    }
}
