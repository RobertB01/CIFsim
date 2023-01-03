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

package org.eclipse.escet.common.java.removablelist;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.escet.common.java.Assert;

/**
 * List where elements can remove themselves from, quickly. In addition, appending new elements, and element access
 * through the list is quick (due to {@link ArrayList} properties).
 *
 * <p>
 * In case of removal, the index position of the removed element is filled with the last element of the list. The
 * position of the elements is thus not fixed between removal of an element.
 * </p>
 *
 * @param <T> Type of the elements of the list.
 */
public class RemovableList<T extends RemovableElement<T>> {
    /** Elements stored in the list. */
    private List<T> elements = list();

    /**
     * Get an element by index.
     *
     * @param index Index number of the element to retrieve.
     * @return The retrieved element.
     */
    public T get(int index) {
        return elements.get(index);
    }

    /**
     * Get an element from the list if available else return {@code null}. A returned element is removed from the list.
     *
     * @return The removed element from the list, or {@code null}.
     */
    public T poll() {
        if (elements.isEmpty()) {
            return null;
        }

        int lastIndex = elements.size() - 1;
        T last = elements.get(lastIndex);
        Assert.check(last.getListIndex() == lastIndex);
        last.clearListIndex();
        elements.remove(lastIndex);
        return last;
    }

    /**
     * Get the number of elements in the list.
     *
     * @return Length of the list.
     */
    public int size() {
        return elements.size();
    }

    /**
     * Check whether the list is empty.
     *
     * @return Whether the list contains no elements.
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /** Remove all elements from the list. */
    public void clear() {
        for (T elm: elements) {
            elm.clearListIndex();
        }
        elements.clear();
    }

    /**
     * An element wants to be removed from the list.
     *
     * @param index Index of the element in the list.
     * @return The removed element.
     */
    T elementRemove(int index) {
        T removed = elements.get(index);
        if (index < elements.size() - 1) {
            T last = elements.get(elements.size() - 1);
            elements.set(index, last);
            last.setIndex(index);
        }
        elements.remove(elements.size() - 1);
        return removed;
    }

    /**
     * Append an element to the list.
     *
     * @param element Element to append.
     * @return Index of the added element.
     */
    public int add(T element) {
        int index = elements.size();
        elements.add(element);
        element.setListIndex(this, index);
        return index;
    }
}
