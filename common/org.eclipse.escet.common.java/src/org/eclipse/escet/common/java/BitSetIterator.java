//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import java.util.BitSet;
import java.util.Iterator;

/** Iterator class for {@link BitSet}. */
public class BitSetIterator implements Iterable<Integer>, Iterator<Integer> {
    /** Bit set to iterate over. */
    private BitSet bitSet;

    /** Next bit index to return. */
    private int nextIndex;

    /**
     * Constructor of the {@link BitSetIterator} class.
     *
     * @param bitSet Bit set to iterate over.
     */
    public BitSetIterator(BitSet bitSet) {
        this(bitSet, 0);
    }

    /**
     * Constructor of the {@link BitSetIterator} class.
     *
     * @param bitSet Bit set to iterate over.
     * @param fromIndex Start iterating from the first true bit at or after this index.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     */
    public BitSetIterator(BitSet bitSet, int fromIndex) {
        this.bitSet = bitSet;
        nextIndex = bitSet.nextSetBit(fromIndex);
    }

    @Override
    public Iterator<Integer> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return nextIndex >= 0;
    }

    @Override
    public Integer next() {
        int retValue = nextIndex;

        // Java doesn't mind "Integer.MAX_VALUE + 1" below, but BitSet#nextSetBit crashes on negative indices.
        if (nextIndex == Integer.MAX_VALUE) {
            nextIndex = -1;
        } else {
            nextIndex = bitSet.nextSetBit(nextIndex + 1);
        }

        return retValue;
    }
}
