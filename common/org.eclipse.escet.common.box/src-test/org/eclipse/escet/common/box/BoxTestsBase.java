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

package org.eclipse.escet.common.box;

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

/** Base class for {@link Box} unit tests. */
public class BoxTestsBase {
    /** Empty string array. */
    protected static final String[] EMPTY = {};

    /**
     * Asserts that two lists of strings are equal.
     *
     * @param lines1 The first list of strings (the expected values).
     * @param lines2 The second list of strings (the actual values).
     */
    protected void assertEqualLists(List<String> lines1, List<String> lines2) {
        assertArrayEquals(lines1.toArray(EMPTY), lines2.toArray(EMPTY));
    }
}
