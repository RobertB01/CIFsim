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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * ToolDef list value, with proper value equality.
 *
 * @param <E> The type of the elements of the list.
 */
public class ToolDefList<E> implements List<E> {
    /** The underlying storage. */
    private final List<ToolDefEqWrap<E>> storage;

    /** Constructor for the {@Link ToolDefList} class. */
    public ToolDefList() {
        storage = list();
    }

    /**
     * Constructor for the {@Link ToolDefList} class.
     *
     * @param initialCapacity The initial capacity of the list.
     * @throws IllegalArgumentException If the initial capacity is negative.
     */
    public ToolDefList(int initialCapacity) {
        storage = listc(initialCapacity);
    }

    /**
     * Constructor for the {@Link ToolDefList} class.
     *
     * @param collection The collection whose elements are to be placed into this list.
     * @throws NullPointerException If the given collection is {@code null}.
     */
    public ToolDefList(Collection<? extends E> collection) {
        storage = listc(collection.size());
        for (E elem: collection) {
            storage.add(ToolDefEqWrap.wrap(elem));
        }
    }

    /**
     * Constructor for the {@Link ToolDefList} class.
     *
     * @param list The list to wrap.
     */
    private ToolDefList(List<ToolDefEqWrap<E>> list) {
        storage = list;
    }

    /**
     * Wraps the given set in a {@link ToolDefList}.
     *
     * @param <T> The type of the ToolDef values.
     * @param list The list of ToolDef values, with the values wrapped for proper value equality.
     * @return The wrapper {@link ToolDefList}.
     */
    public static <T> ToolDefList<T> wrap(List<ToolDefEqWrap<T>> list) {
        return new ToolDefList<>(list);
    }

    @Override
    public boolean add(E elem) {
        ToolDefEqWrap<E> wrappedElem = ToolDefEqWrap.wrap(elem);
        return storage.add(wrappedElem);
    }

    @Override
    public void add(int index, E element) {
        storage.add(index, ToolDefEqWrap.wrap(element));
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO: May increase performance by using bulk operation.
        for (E elem: c) {
            add(elem);
        }
        return !c.isEmpty();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // TODO: May increase performance by using bulk operation.
        for (E elem: c) {
            add(index, elem);
            index++;
        }
        return !c.isEmpty();
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
        if (!(obj instanceof ToolDefList)) {
            return false;
        }

        ToolDefList<?> other = (ToolDefList<?>)obj;
        if (this.size() != other.size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            ToolDefEqWrap<E> wrapThis = this.storage.get(i);
            ToolDefEqWrap<?> wrapOther = other.storage.get(i);
            if (!wrapThis.equals(wrapOther)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public E get(int index) {
        return storage.get(index).value;
    }

    @Override
    public int hashCode() {
        int rslt = ToolDefList.class.hashCode();
        for (ToolDefEqWrap<E> wrap: storage) {
            rslt = 31 * rslt + wrap.hashCode();
        }
        return rslt;
    }

    @Override
    public int indexOf(Object o) {
        return storage.indexOf(ToolDefEqWrap.wrap(o));
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
    public int lastIndexOf(Object o) {
        return storage.lastIndexOf(ToolDefEqWrap.wrap(o));
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        final ListIterator<ToolDefEqWrap<E>> iter = storage.listIterator(index);

        return new ListIterator<>() {
            @Override
            public void add(E e) {
                iter.add(ToolDefEqWrap.wrap(e));
            }

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public boolean hasPrevious() {
                return iter.hasPrevious();
            }

            @Override
            public E next() {
                return iter.next().value;
            }

            @Override
            public int nextIndex() {
                return iter.nextIndex();
            }

            @Override
            public E previous() {
                return iter.previous().value;
            }

            @Override
            public int previousIndex() {
                return iter.previousIndex();
            }

            @Override
            public void remove() {
                iter.remove();
            }

            @Override
            public void set(E e) {
                iter.set(ToolDefEqWrap.wrap(e));
            }
        };
    }

    @Override
    public E remove(int index) {
        ToolDefEqWrap<E> rslt = storage.remove(index);
        return rslt.value;
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
            while (contains(elem)) {
                rslt = true;
                remove(elem);
            }
        }
        return rslt;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        ToolDefList<Object> wrappedC = new ToolDefList<>(c.size());
        for (Object elem: c) {
            wrappedC.add(ToolDefEqWrap.wrap(elem));
        }
        return storage.retainAll(wrappedC);
    }

    @Override
    public E set(int index, E element) {
        ToolDefEqWrap<E> rslt = storage.set(index, ToolDefEqWrap.wrap(element));
        return rslt.value;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return ToolDefList.wrap(storage.subList(fromIndex, toIndex));
    }

    @Override
    public Object[] toArray() {
        Object[] rslt = new Object[storage.size()];
        for (int i = 0; i < storage.size(); i++) {
            rslt[i] = storage.get(i).value;
        }
        return rslt;
    }

    @Override
    public <T> T[] toArray(T[] array) {
        // Check supported.
        if (array.length != storage.size()) {
            throw new UnsupportedOperationException();
        }

        // Do actual conversion to array.
        Class<?> elemType = array.getClass().getComponentType();
        @SuppressWarnings("unchecked")
        E[] elems = (E[])Array.newInstance(elemType, storage.size());
        for (int i = 0; i < storage.size(); i++) {
            elems[i] = storage.get(i).value;
        }

        // Do type conversion.
        @SuppressWarnings("unchecked")
        T[] rslt = (T[])elems;
        return rslt;
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("[");
        for (ToolDefEqWrap<E> elem: storage) {
            if (txt.length() > 1) {
                txt.append(", ");
            }
            txt.append(elem.toString());
        }
        txt.append("]");
        return txt.toString();
    }
}
