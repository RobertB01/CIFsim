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

package org.eclipse.escet.tooldef.runtime;

import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * ToolDef set value, with proper value equality.
 *
 * @param <E> The type of the elements of the set.
 */
public class ToolDefSet<E> implements Set<E> {
    /** The underlying storage. */
    private final Set<ToolDefEqWrap<E>> storage;

    /** Constructor for the {@link ToolDefSet} class. */
    public ToolDefSet() {
        storage = set();
    }

    /**
     * Constructor for the {@link ToolDefSet} class.
     *
     * @param initialCapacity The initial capacity of the set.
     * @throws IllegalArgumentException If the initial capacity is negative.
     */
    public ToolDefSet(int initialCapacity) {
        storage = setc(initialCapacity);
    }

    /**
     * Constructor for the {@link ToolDefSet} class.
     *
     * @param collection The collection whose elements are to be placed into this set.
     * @throws NullPointerException If the given collection is {@code null}.
     */
    public ToolDefSet(Collection<? extends E> collection) {
        storage = setc(collection.size());
        for (E elem: collection) {
            storage.add(ToolDefEqWrap.wrap(elem));
        }
    }

    /**
     * Constructor for the {@link ToolDefSet} class.
     *
     * @param set The set to wrap.
     */
    private ToolDefSet(Set<ToolDefEqWrap<E>> set) {
        storage = set;
    }

    /**
     * Wraps the given set in a {@link ToolDefSet}.
     *
     * @param <T> The type of the ToolDef values.
     * @param set The set of ToolDef values, with the values wrapped for proper value equality.
     * @return The wrapper {@link ToolDefSet}.
     */
    public static <T> ToolDefSet<T> wrap(Set<ToolDefEqWrap<T>> set) {
        return new ToolDefSet<>(set);
    }

    @Override
    public boolean add(E elem) {
        ToolDefEqWrap<E> wrappedElem = ToolDefEqWrap.wrap(elem);
        return storage.add(wrappedElem);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO: May increase performance by using bulk operation.
        boolean rslt = false;
        for (E elem: c) {
            rslt |= add(elem);
        }
        return rslt;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public boolean contains(Object elem) {
        @SuppressWarnings("unchecked")
        ToolDefEqWrap<E> wrappedElem = ToolDefEqWrap.wrap((E)elem);
        return storage.contains(wrappedElem);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object elem: c) {
            if (!contains(elem)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ToolDefSet)) {
            return false;
        }

        ToolDefSet<?> other = (ToolDefSet<?>)obj;
        if (this.size() != other.size()) {
            return false;
        }
        return containsAll(other);
    }

    @Override
    public int hashCode() {
        int rslt = ToolDefSet.class.hashCode();
        for (ToolDefEqWrap<E> wrap: storage) {
            rslt = 31 * rslt + wrap.hashCode();
        }
        return rslt;
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private final Iterator<ToolDefEqWrap<E>> iter = storage.iterator();

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public E next() {
                return iter.next().value;
            }

            @Override
            public void remove() {
                iter.remove();
            }
        };
    }

    @Override
    public boolean remove(Object elem) {
        @SuppressWarnings("unchecked")
        ToolDefEqWrap<E> wrappedElem = ToolDefEqWrap.wrap((E)elem);
        return storage.remove(wrappedElem);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO: May increase performance by using bulk operation.
        boolean rslt = false;
        for (Object elem: c) {
            rslt |= remove(elem);
        }
        return rslt;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        ToolDefSet<Object> wrappedC = new ToolDefSet<>(c.size());
        for (Object elem: c) {
            wrappedC.add(ToolDefEqWrap.wrap(elem));
        }
        return storage.retainAll(wrappedC);
    }

    @Override
    public int size() {
        return storage.size();
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
        for (ToolDefEqWrap<E> elem: storage) {
            if (txt.length() > 1) {
                txt.append(", ");
            }
            txt.append(elem.toString());
        }
        txt.append("}");
        return txt.toString();
    }
}
