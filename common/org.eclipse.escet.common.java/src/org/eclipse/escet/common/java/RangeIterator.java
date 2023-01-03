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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over integer number ranges, with configurable start and end values, as well as a configurable step size
 * (which may be negative).
 */
public class RangeIterator implements Iterator<Integer> {
    /** Current value of the iterator. */
    private int current;

    /** Step size and direction of the iterator. */
    private final int step;

    /** End value (exclusive). */
    private final int end;

    /**
     * Constructor of the {@link RangeIterator} class.
     *
     * @param start Start value returned by the iterator.
     * @param end Upper-boundary of the iterator (never returned).
     * @param step Step size to use. Negative values are allowed. If the given step size is {@code 0}, a step size of
     *     {@code 1} is used instead.
     */
    public RangeIterator(int start, int end, int step) {
        this.current = start;
        this.end = end;
        this.step = (step == 0) ? 1 : step;
    }

    @Override
    public boolean hasNext() {
        if (step > 0) {
            return current < end;
        }
        return current > end;
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        int result = current;
        current += step;
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
