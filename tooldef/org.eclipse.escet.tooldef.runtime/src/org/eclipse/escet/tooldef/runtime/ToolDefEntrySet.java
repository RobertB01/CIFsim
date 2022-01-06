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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * ToolDef map entry set, with proper value equality. It is a read-only set.
 *
 * @param <K> The type of the keys of the map entries.
 * @param <V> The type of the values of the map entries.
 */
public class ToolDefEntrySet<K, V> implements Set<Entry<K, V>> {
    /** The entries. */
    private final Set<Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>>> entries;

    /**
     * Constructor for the {@link ToolDefEntrySet} class.
     *
     * @param entries The entries set of a {@link ToolDefMap}, obtained via {@link ToolDefMap#entrySet}.
     */
    public ToolDefEntrySet(Set<Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>>> entries) {
        this.entries = entries;
    }

    @Override
    public boolean add(Entry<K, V> e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends Entry<K, V>> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ToolDefEntrySet)) {
            return false;
        }

        ToolDefEntrySet<?, ?> other = (ToolDefEntrySet<?, ?>)obj;
        if (this.size() != other.size()) {
            return false;
        }
        return entries.containsAll(other.entries);
    }

    @Override
    public int hashCode() {
        int rslt = ToolDefEntrySet.class.hashCode();
        for (Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>> entry: entries) {
            rslt = 31 * rslt + entry.hashCode();
        }
        return rslt;
    }

    @Override
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        final Iterator<Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>>> iter;
        iter = entries.iterator();

        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Entry<K, V> next() {
                final Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>> entry;
                entry = iter.next();

                return new Entry<>() {
                    @Override
                    public boolean equals(Object obj) {
                        if (this == obj) {
                            return true;
                        }
                        if (obj == null) {
                            return false;
                        }
                        if (!(obj instanceof Map.Entry)) {
                            return false;
                        }
                        Entry<?, ?> other = (Entry<?, ?>)obj;
                        return ToolDefRuntimeUtils.equalValues(entry.getKey(), other.getKey())
                                && ToolDefRuntimeUtils.equalValues(entry.getValue(), other.getValue());
                    }

                    @Override
                    public K getKey() {
                        return entry.getKey().value;
                    }

                    @Override
                    public V getValue() {
                        return entry.getValue().value;
                    }

                    @Override
                    public int hashCode() {
                        return ToolDefRuntimeUtils.hashValue(entry.getKey())
                                ^ ToolDefRuntimeUtils.hashValue(entry.getValue());
                    }

                    @Override
                    public V setValue(V value) {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public String toString() {
                        return fmt("%s: %s", entry.getKey().toString(), entry.getValue().toString());
                    }
                };
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("{");
        for (Entry<ToolDefEqWrap<K>, ToolDefEqWrap<V>> entry: entries) {
            if (txt.length() > 1) {
                txt.append(", ");
            }
            txt.append("(");
            txt.append(entry.getKey().toString());
            txt.append(": ");
            txt.append(entry.getValue().toString());
            txt.append(")");
        }
        txt.append("}");
        return txt.toString();
    }
}
