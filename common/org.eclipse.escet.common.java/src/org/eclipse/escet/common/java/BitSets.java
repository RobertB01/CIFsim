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

/** Common functions around bitsets. */
public class BitSets {
    /** Constructor of the static {@link BitSets} class. */
    private BitSets() {
        // Static class.
    }

    /**
     * Construct an empty bitset with unspecified size.
     *
     * @return The created bitset.
     */
    public static BitSet bitset() {
        return new BitSet();
    }

    /**
     * Construct an empty bitset of the given size.
     *
     * @param n Requested size of the bitset.
     * @return The created bitset.
     * @throws NegativeArraySizeException If {@code n} is negative.
     */
    public static BitSet bitset(int n) {
        return new BitSet(n);
    }

    /**
     * Construct a bitset with the indicated bits set.
     *
     * @param indices Bit numbers of bits that should be set.
     * @return The created bitset.
     * @throws IndexOutOfBoundsException If a bit number is negative.
     */
    public static BitSet makeBitset(int... indices) {
        BitSet bs = bitset();
        setBits(bs, indices);
        return bs;
    }

    /**
     * Make an independent copy of the provided bitset.
     *
     * @param bs Bitset to copy.
     * @return The created independent copy.
     */
    public static BitSet copy(BitSet bs) {
        return (BitSet)bs.clone();
    }

    /**
     * Create a new bitset of the requested size, with all bits at least up-to the requested size set to {@code true}.
     *
     * @param n Requested size of the bitset.
     * @return The created bitset.
     * @throws IndexOutOfBoundsException If {@code n} is negative.
     */
    public static BitSet ones(int n) {
        BitSet bs = bitset(n);
        bs.set(0, n);
        return bs;
    }

    /**
     * Invert the provided bitset, up to bit index {@code n}.
     *
     * @param bs Bitset to invert and truncate.
     * @param n Number of bits to invert.
     * @return The inverted bitset (all bits that are not in {@code bs} up to bit number {@code n}).
     * @throws IndexOutOfBoundsException If {@code n} is negative.
     */
    public static BitSet invert(BitSet bs, int n) {
        BitSet result = ones(n);
        result.andNot(bs);
        return result;
    }

    /**
     * Get the index of the first {@code true} bit in the set.
     *
     * @param bs Bitset to check.
     * @return Index of the first {@code true} bit, or {@code -1} if there is no such bit.
     */
    public static int first(BitSet bs) {
        return bs.nextSetBit(0);
    }

    /**
     * Decide whether the bitset spans a consecutive range of {@code true} bits.
     *
     * @param bs Bitset to check.
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
     * @param bs Bitset to update.
     * @param bitNumbers Bit indices to set.
     * @throws IndexOutOfBoundsException If a bit number is negative.
     */
    public static void setBits(BitSet bs, int... bitNumbers) {
        for (int i: bitNumbers) {
            bs.set(i);
        }
    }

    /**
     * Get an array with the indices of the bitset which are set to {@code true}.
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
     * Wrapper function for iterating over set bits of a {@link BitSet}.
     *
     * @param bitSet Bitset to iterate over.
     * @return Iterable over the supplied bitset.
     */
    public static Iterable<Integer> iterateTrueBits(BitSet bitSet) {
        return new BitSetIterator(bitSet);
    }

    /**
     * Wrapper function for iterating over set bits of a {@link BitSet}.
     *
     * @param bitSet Bitset to iterate over.
     * @param fromIndex Start iterating from the first {@code true} bit at or after this index.
     * @return Iterable over the supplied bitset.
     * @throws IndexOutOfBoundsException If the specified index is negative.
     */
    public static Iterable<Integer> iterateTrueBits(BitSet bitSet, int fromIndex) {
        return new BitSetIterator(bitSet, fromIndex);
    }
}
