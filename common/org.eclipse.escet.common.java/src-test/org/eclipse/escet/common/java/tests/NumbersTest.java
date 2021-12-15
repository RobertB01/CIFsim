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

package org.eclipse.escet.common.java.tests;

import static org.junit.Assert.assertEquals;

import org.eclipse.escet.common.java.Numbers;
import org.junit.Test;

/** {@link Numbers} unit tests. */
public class NumbersTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testFormatNumberInt() {
        final int MIN = Integer.MIN_VALUE;
        final int MAX = Integer.MAX_VALUE;
        assertEquals("-2,147,483,648", Numbers.formatNumber(MIN));
        assertEquals("-1,000", Numbers.formatNumber(-1000));
        assertEquals("-999", Numbers.formatNumber(-999));
        assertEquals("-1", Numbers.formatNumber(-1));
        assertEquals("0", Numbers.formatNumber(0));
        assertEquals("1", Numbers.formatNumber(1));
        assertEquals("999", Numbers.formatNumber(999));
        assertEquals("1,000", Numbers.formatNumber(1000));
        assertEquals("2,147,483,647", Numbers.formatNumber(MAX));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testFormatNumberStr() {
        final String MIN = "-2147483648";
        final String MAX = "2147483647";
        assertEquals("-2,147,483,648", Numbers.formatNumber(MIN));
        assertEquals("-1,000", Numbers.formatNumber("-1000"));
        assertEquals("-999", Numbers.formatNumber("-999"));
        assertEquals("-1", Numbers.formatNumber("-1"));
        assertEquals("0", Numbers.formatNumber("0"));
        assertEquals("1", Numbers.formatNumber("1"));
        assertEquals("999", Numbers.formatNumber("999"));
        assertEquals("1,000", Numbers.formatNumber("1000"));
        assertEquals("2,147,483,647", Numbers.formatNumber(MAX));
    }
}
