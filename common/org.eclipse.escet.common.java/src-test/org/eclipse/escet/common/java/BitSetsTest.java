//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Lists.list;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

/** Tests for the {@link BitSets} class. */
public class BitSetsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testToBitSetEmpty() {
        assertEquals("{}", IntStream.of().boxed().collect(BitSets.toBitSet()).toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testToBitSetRange() {
        assertEquals("{0, 1, 2, 3, 4}", IntStream.range(0, 5).boxed().collect(BitSets.toBitSet()).toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testToBitSetSubset() {
        assertEquals("{1, 3}",
                IntStream.range(0, 5).filter(i -> i % 2 != 0).boxed().collect(BitSets.toBitSet()).toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testToBitSetUnordered() {
        assertEquals("{4, 7, 9}",
                list(7, 4, 9).stream().mapToInt(i -> i).boxed().collect(BitSets.toBitSet()).toString());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testToBitSetParallel() {
        assertEquals(BitSets.ones(10_000), IntStream.range(0, 10_000).parallel().boxed().collect(BitSets.toBitSet()));
        assertEquals(BitSets.ones(10_000), IntStream.range(0, 10_000).boxed().parallel().collect(BitSets.toBitSet()));
    }
}
