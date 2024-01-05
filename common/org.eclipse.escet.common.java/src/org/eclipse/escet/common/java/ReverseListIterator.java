//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

import java.util.Iterator;
import java.util.List;

/**
 * Iterate over a list backwards using standard {@link Iterator} contract.
 *
 * @param <T> Type of the list elements.
 */
public class ReverseListIterator<T> implements Iterator<T> {
    /** Elements to iterate over. */
    private final List<T> elements;

    /** Index of the next element to return. */
    private int nextIndex;

    /**
     * Constructor of the {@link ReverseListIterator} class.
     *
     * @param elements Elements to iterate over.
     */
    public ReverseListIterator(List<T> elements) {
        this.elements = elements;
        nextIndex = elements.size() - 1;
    }

    @Override
    public boolean hasNext() {
        return nextIndex >= 0;
    }

    @Override
    public T next() {
        return elements.get(nextIndex--);
    }
}
