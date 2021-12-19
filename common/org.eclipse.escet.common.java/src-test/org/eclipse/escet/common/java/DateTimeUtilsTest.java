//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.DateTimeUtils.durationToString;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Unit tests for the {@link DateTimeUtils} class. */
public class DateTimeUtilsTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testDurationToString() {
        int s = 1000;
        int m = s * 60;
        int h = m * 60;
        int t3456 = 3 * h + 4 * m + 5 * s + 6;

        // Don't omit zeros.
        assertEquals("0h 0m 0s 0ms", durationToString(0, false));
        assertEquals("0h 0m 0s 1ms", durationToString(1, false));
        assertEquals("0h 0m 0s 2ms", durationToString(2, false));

        assertEquals("0h 0m 0s 999ms", durationToString(s - 1, false));
        assertEquals("0h 0m 1s 0ms", durationToString(s, false));
        assertEquals("0h 0m 1s 1ms", durationToString(s + 1, false));
        assertEquals("0h 0m 2s 0ms", durationToString(2 * s, false));

        assertEquals("0h 0m 59s 999ms", durationToString(m - 1, false));
        assertEquals("0h 1m 0s 0ms", durationToString(m, false));
        assertEquals("0h 1m 0s 1ms", durationToString(m + 1, false));
        assertEquals("0h 2m 0s 0ms", durationToString(2 * m, false));

        assertEquals("0h 59m 59s 999ms", durationToString(h - 1, false));
        assertEquals("1h 0m 0s 0ms", durationToString(h, false));
        assertEquals("1h 0m 0s 1ms", durationToString(h + 1, false));
        assertEquals("2h 0m 0s 0ms", durationToString(2 * h, false));

        assertEquals("3h 4m 5s 6ms", durationToString(t3456, false));

        // Omit zeros.
        assertEquals("0ms", durationToString(0, true));
        assertEquals("1ms", durationToString(1, true));
        assertEquals("2ms", durationToString(2, true));

        assertEquals("999ms", durationToString(s - 1, true));
        assertEquals("1s", durationToString(s, true));
        assertEquals("1s 1ms", durationToString(s + 1, true));
        assertEquals("2s", durationToString(2 * s, true));

        assertEquals("59s 999ms", durationToString(m - 1, true));
        assertEquals("1m", durationToString(m, true));
        assertEquals("1m 1ms", durationToString(m + 1, true));
        assertEquals("2m", durationToString(2 * m, true));

        assertEquals("59m 59s 999ms", durationToString(h - 1, true));
        assertEquals("1h", durationToString(h, true));
        assertEquals("1h 1ms", durationToString(h + 1, true));
        assertEquals("2h", durationToString(2 * h, true));

        assertEquals("3h 4m 5s 6ms", durationToString(t3456, true));
    }
}
