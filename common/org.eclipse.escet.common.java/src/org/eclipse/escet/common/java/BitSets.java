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

/** Common functions around bit sets. */
public class BitSets {
    /** Constructor of the static {@link BitSets} class. */
    private BitSets() {
        // Static class.
    }

    /**
     * Construct an empty bit set with unspecified size.
     *
     * @return The created bit set.
     */
    public static BitSet bitset() {
        return new BitSet();
    }

    /**
     * Construct an empty bit set of the given size (may allocate more).
     *
     * @param n Requested size of the bit set.
     * @return The created bit set.
     */
    public static BitSet bitset(int n) {
        return new BitSet(n);
    }

    /**
     * Construct a bit set with the indicated bits set.
     *
     * @param indices Bit numbers of bits that should be set.
     * @return The created bit set.
     */
    public static BitSet makeBitset(int... indices) {
        BitSet bs = bitset();
        setBits(bs, indices);
        return bs;
    }

    /**
     * Make an independent copy of the provided bit set.
     *
     * @param bs Bit set to copy.
     * @return The created independent copy.
     */
    public static BitSet copy(BitSet bs) {
        return (BitSet)bs.clone();
    }

    /**
     * Create a new bit set of the requested size (may allocate more), with all bits up-to the requested size set
     * to {@code false}.
     *
     * @param n Requested size of the bit set.
     * @return The created bit set.
     */
    public static BitSet zeroes(int n) {
        return bitset(n);
    }

    /**
     * Create a new bit set of the requested size (may allocate more), with all bits at least up-to the requested
     * size set to {@code true}.
     *
     * @param n Requested size of the bit set.
     * @return The created bit set.
     */
    public static BitSet ones(int n) {
        BitSet bs = bitset(n);
        bs.set(0, n);
        return bs;
    }

    /**
     * Invert the provided bitset, up to <em>n</em> bits.
     *
     * @param bs Bit set to invert.
     * @param n Number of bits to invert.
     * @return The inverted bit set (all bits that are not in <em>b</em>s up to bit number <em>n</em>).
     */
    public static BitSet invert(BitSet bs, int n) {
        BitSet result = ones(n);
        result.andNot(bs);
        return result;
    }

    /**
     * Get the index of the first {@code true} bit in the set.
     *
     * @param bs Bit set to check.
     * @return Index of the first {@code true} bit, or {@code -1} if there is no such bit.
     */
    public static int first(BitSet bs) {
        return bs.nextSetBit(0);
    }

    /**
     * Decide whether the bit set spans a consecutive range of {@code true} bits.
     *
     * @param bs Bit set to check.
     * @return Whether all {@code true} bits are consecutively ordered in the set.
     */
    public static boolean isConsecutive(BitSet bs) {
        int first = first(bs);
        if (first < 0) {
            return true; // Empty set is trivially consecutive.
        }
        int nextZero = bs.nextClearBit(first + 1);
        return nextZero == first + bs.cardinality();
    }

    /**
     * Set all bits indicated by the bit numbers.
     *
     * @param bs Bit set to update.
     * @param bitNumbers Bit indices to set.
     */
    public static void setBits(BitSet bs, int... bitNumbers) {
        for (int i: bitNumbers) {
            bs.set(i);
        }
    }

    /**
     * Get an array with the indices of the bitset which are set to true.
     *
     * @param bs The bitset.
     * @return The array with indices.
     */
    public static int[] getTrueBits(BitSet bs) {
        int[] result = new int[bs.cardinality()];
        int free = 0;

        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
            result[free] = i;
            free++;

            if (i == Integer.MAX_VALUE) {
                break; // Or (i + 1) would overflow.
            }
        }

        return result;
    }

    /**
     * Get an array with the indices of the bitset which are set to true starting at a given position.
     *
     * @param bs The bitset.
     * @param start The start position.
     * @return The array with indices.
     */
    public static int[] getTrueBits(BitSet bs, int start) {
        int[] result = new int[bs.get(0, start).cardinality()];
        int free = 0;

        for (int i = bs.nextSetBit(start); i >= 0; i = bs.nextSetBit(i + 1)) {
            result[free] = i;
            free++;

            if (i == Integer.MAX_VALUE) {
                break; // Or (i + 1) would overflow.
            }
        }

        return result;
    }

    /**
     * Wrapper function for iterating over set bits of a {@link BitSet}.
     *
     * @param bitSet Bit set to iterate over.
     * @return Iterable over the supplied bit set.
     */
    public static Iterable<Integer> iterateTrueBits(BitSet bitSet) {
        return new BitSetIterator(bitSet);
    }

    /**
     * Wrapper function for iterating over set bits of a {@link BitSet}.
     *
     * @param bitSet Bit set to iterate over.
     * @param fromIndex Start iterating from the first true bit on or after this index.
     * @return Iterable over the supplied bit set.
     * @throws IndexOutOfBoundsException if the specified index is negative.
     */
    public static Iterable<Integer> iterateTrueBits(BitSet bitSet, int fromIndex) {
        return new BitSetIterator(bitSet, fromIndex);
    }
}
