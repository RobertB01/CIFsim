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

import static org.eclipse.escet.common.java.Lists.listc;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Iterator that iterates over all the possible combinations of the elements of the sub-lists, where each resulting item
 * has one element from each of the sub-lists. In other words, the iterator can be used to iterate over all items of the
 * product of the sub-lists.
 *
 * <p>
 * The elements of the result items, are guaranteed to be in the same order as the sub-lists (from the input) from which
 * the elements originate.
 * </p>
 *
 * <p>
 * The elements of the last sub-list are iterated first, one by one. Once the last sub-list is depleted, the before last
 * sub-list advances by one element, and the last sub-list is once again iterated from the start. This continues until
 * the first sub-list is depleted.
 * </p>
 *
 * <p>
 * <b>Example</b>
 * </p>
 * <p>
 * For instance, consider input:
 *
 * <pre>[[1, 2, 3], [4], [5, 6]]</pre>
 *
 * It is transformed to:
 *
 * <pre>[[1, 4, 5], [1, 4, 6], [2, 4, 5], [2, 4, 6], [3, 4, 5], [3, 4, 6]]</pre>
 *
 * The resulting sub-lists are returned one by one.
 * </p>
 *
 * @param <T> The elements of the lists of lists (for both the input and the output).
 */
public class ListProductIterator<T> implements Iterator<List<T>> {
    /**
     * The indices of the input sub-lists to use to obtain the elements for the next iteration result. Is modified
     * in-place.
     */
    private final int[] indices;

    /** The input data. */
    private final List<List<T>> data;

    /** Whether the input is depleted. */
    private boolean depleted;

    /**
     * Constructor for the {@link ListProductIterator} class.
     *
     * @param data The input data.
     */
    public ListProductIterator(List<List<T>> data) {
        // Store input data.
        this.data = data;

        // Initialize all indices to zero.
        indices = new int[data.size()];

        // If there is no input, or we have an empty sub-list, the input is
        // already depleted.
        if (data.isEmpty()) {
            depleted = true;
        } else {
            for (List<T> subList: data) {
                if (subList.isEmpty()) {
                    depleted = true;
                    break;
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !depleted;
    }

    @Override
    public List<T> next() {
        // If already depleted, this method may not be invoked.
        if (depleted) {
            throw new NoSuchElementException();
        }

        // Get the 'current' result.
        List<T> rslt = current();

        // Advance to the next result.
        for (int i = indices.length - 1; i >= 0; i--) {
            // Increment index.
            indices[i]++;

            // Check for index overflow.
            if (indices[i] == data.get(i).size()) {
                // Index overflow. Reset to zero, and move to previous index.
                indices[i] = 0;
            } else {
                // No overflow. We are done advancing.
                return rslt;
            }
        }

        // Overflow on first index. Iterator is depleted.
        depleted = true;
        return rslt;
    }

    /**
     * Returns the current iteration result. Must not be called if iterator is depleted.
     *
     * @return The current iteration result.
     */
    private List<T> current() {
        List<T> rslt = listc(indices.length);
        for (int i = 0; i < indices.length; i++) {
            rslt.add(data.get(i).get(indices[i]));
        }
        return rslt;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
