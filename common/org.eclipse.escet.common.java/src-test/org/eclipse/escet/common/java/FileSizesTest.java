//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Unit tests for the methods of the {@link FileSizes} class. */
public class FileSizesTest {
    /** Test file size extended format (non-SI). */
    @Test
    public void testFormatFileSizeEx() {
        assertEquals("0 bytes (~0 B)", FileSizes.formatFileSizeEx(0));
        assertEquals("1 bytes (~1 B)", FileSizes.formatFileSizeEx(1));
        assertEquals("1,000 bytes (~0.977 KiB)", FileSizes.formatFileSizeEx(1_000));
        assertEquals("1,234,567 bytes (~1.177 MiB)", FileSizes.formatFileSizeEx(1_234_567));
        assertEquals("9,223,372,036,854,775,807 bytes (~8 EiB)", FileSizes.formatFileSizeEx(Long.MAX_VALUE));
    }

    /** Test file size format (SI). */
    @Test
    public void testFormatFileSizeSi() {
        assertEquals("0 B", FileSizes.formatFileSize(0, true));
        assertEquals("1 B", FileSizes.formatFileSize(1, true));
        assertEquals("999 B", FileSizes.formatFileSize(999, true));

        assertEquals("1 kB", FileSizes.formatFileSize(1_000, true));
        assertEquals("1.001 kB", FileSizes.formatFileSize(1_001, true));
        assertEquals("1.999 kB", FileSizes.formatFileSize(1_999, true));
        assertEquals("2 kB", FileSizes.formatFileSize(2_000, true));
        assertEquals("9.999 kB", FileSizes.formatFileSize(9_999, true));
        assertEquals("10 kB", FileSizes.formatFileSize(10_000, true));
        assertEquals("999.999 kB", FileSizes.formatFileSize(999_999, true));

        assertEquals("1 MB", FileSizes.formatFileSize(1_000_000, true));
        assertEquals("1 MB", FileSizes.formatFileSize(1_000_499, true));
        assertEquals("1.001 MB", FileSizes.formatFileSize(1_000_500, true));
        assertEquals("1.001 MB", FileSizes.formatFileSize(1_000_999, true));
        assertEquals("1.001 MB", FileSizes.formatFileSize(1_001_000, true));
        assertEquals("1.001 MB", FileSizes.formatFileSize(1_001_001, true));
        assertEquals("1.001 MB", FileSizes.formatFileSize(1_001_499, true));
        assertEquals("1.002 MB", FileSizes.formatFileSize(1_001_500, true));

        assertEquals("1 GB", FileSizes.formatFileSize(1_000_000_000L, true));
        assertEquals("1 GB", FileSizes.formatFileSize(1_000_499_999L, true));
        assertEquals("1.001 GB", FileSizes.formatFileSize(1_000_500_000L, true));
        assertEquals("1.001 GB", FileSizes.formatFileSize(1_000_999_999L, true));
        assertEquals("1.001 GB", FileSizes.formatFileSize(1_001_000_000L, true));
        assertEquals("1.001 GB", FileSizes.formatFileSize(1_001_000_001L, true));
        assertEquals("1.001 GB", FileSizes.formatFileSize(1_001_499_999L, true));
        assertEquals("1.002 GB", FileSizes.formatFileSize(1_001_500_000L, true));

        assertEquals("1 TB", FileSizes.formatFileSize(1_000_000_000_000L, true));
        assertEquals("1 TB", FileSizes.formatFileSize(1_000_499_999_999L, true));
        assertEquals("1.001 TB", FileSizes.formatFileSize(1_000_500_000_000L, true));
        assertEquals("1.001 TB", FileSizes.formatFileSize(1_000_999_999_999L, true));
        assertEquals("1.001 TB", FileSizes.formatFileSize(1_001_000_000_000L, true));
        assertEquals("1.001 TB", FileSizes.formatFileSize(1_001_000_000_001L, true));
        assertEquals("1.001 TB", FileSizes.formatFileSize(1_001_499_999_999L, true));
        assertEquals("1.002 TB", FileSizes.formatFileSize(1_001_500_000_000L, true));

        assertEquals("1 PB", FileSizes.formatFileSize(1_000_000_000_000_000L, true));
        assertEquals("1 PB", FileSizes.formatFileSize(1_000_499_999_999_999L, true));
        assertEquals("1.001 PB", FileSizes.formatFileSize(1_000_500_000_000_000L, true));
        assertEquals("1.001 PB", FileSizes.formatFileSize(1_000_999_999_999_999L, true));
        assertEquals("1.001 PB", FileSizes.formatFileSize(1_001_000_000_000_000L, true));
        assertEquals("1.001 PB", FileSizes.formatFileSize(1_001_000_000_000_001L, true));
        assertEquals("1.001 PB", FileSizes.formatFileSize(1_001_499_999_999_999L, true));
        assertEquals("1.002 PB", FileSizes.formatFileSize(1_001_500_000_000_000L, true));

        assertEquals("1 EB", FileSizes.formatFileSize(1_000_000_000_000_000_000L, true));
        assertEquals("1 EB", FileSizes.formatFileSize(1_000_499_999_999_999_999L, true));
        assertEquals("1.001 EB", FileSizes.formatFileSize(1_000_500_000_000_000_000L, true));
        assertEquals("1.001 EB", FileSizes.formatFileSize(1_000_999_999_999_999_999L, true));
        assertEquals("1.001 EB", FileSizes.formatFileSize(1_001_000_000_000_000_000L, true));
        assertEquals("1.001 EB", FileSizes.formatFileSize(1_001_000_000_000_000_001L, true));
        assertEquals("1.001 EB", FileSizes.formatFileSize(1_001_499_999_999_999_999L, true));
        assertEquals("1.002 EB", FileSizes.formatFileSize(1_001_500_000_000_000_000L, true));
        assertEquals("9.223 EB", FileSizes.formatFileSize(Long.MAX_VALUE, true));
    }

    /** Test file size format (non-SI). */
    @Test
    public void testFormatFileSizeNonSi() {
        assertEquals("0 B", FileSizes.formatFileSize(0, false));
        assertEquals("1 B", FileSizes.formatFileSize(1, false));
        assertEquals("999 B", FileSizes.formatFileSize(1_000 - 1, false));

        assertEquals("0.977 KiB", FileSizes.formatFileSize(1_000, false));
        assertEquals("0.999 KiB", FileSizes.formatFileSize(1_024 - 1, false));

        assertEquals("1 KiB", FileSizes.formatFileSize(1_024, false));
        assertEquals("1.001 KiB", FileSizes.formatFileSize(1_024 + 1, false));
        assertEquals("1.002 KiB", FileSizes.formatFileSize(1_024 + 2, false));
        assertEquals("1.003 KiB", FileSizes.formatFileSize(1_024 + 3, false));
        assertEquals("1.999 KiB", FileSizes.formatFileSize(2 * 1_024 - 1, false));
        assertEquals("2 KiB", FileSizes.formatFileSize(2 * 1_024, false));
        assertEquals("9.999 KiB", FileSizes.formatFileSize(10 * 1_024 - 1, false));
        assertEquals("10 KiB", FileSizes.formatFileSize(10 * 1_024, false));
        assertEquals("999.999 KiB", FileSizes.formatFileSize(1_000 * 1_024 - 1, false));

        assertEquals("0.977 MiB", FileSizes.formatFileSize(1_000 * 1_024, false));
        assertEquals("0.999 MiB", FileSizes.formatFileSize((1_024 - 1) * 1_024, false));

        assertEquals("1 MiB", FileSizes.formatFileSize(1_024 * 1_024, false));
        assertEquals("1 MiB", FileSizes.formatFileSize(1_024 * 1_024 + 524, false));
        assertEquals("1.001 MiB", FileSizes.formatFileSize(1_024 * 1_024 + 525, false));
        assertEquals("1.001 MiB", FileSizes.formatFileSize((1_024 + 1) * 1_024 + 548, false));
        assertEquals("1.002 MiB", FileSizes.formatFileSize((1_024 + 1) * 1_024 + 549, false));
        assertEquals("999.999 MiB", FileSizes
                .formatFileSize((long)((999 * 1024 * 1024) + (999 * 1024 * 1.024) + (499 * 1.024 * 1.024)), false));
        assertEquals("999.999 MiB", FileSizes
                .formatFileSize((long)((999 * 1024 * 1024) + (999 * 1024 * 1.024) + (499 * 1.024 * 1.024) + 1), false));

        assertEquals("0.977 GiB", FileSizes
                .formatFileSize((long)((999 * 1024 * 1024) + (999 * 1024 * 1.024) + (499 * 1.024 * 1.024) + 2), false));

        assertEquals("1 GiB", FileSizes.formatFileSize(1_024L * 1_024L * 1_024L, false));
        assertEquals("1 TiB", FileSizes.formatFileSize(1_024L * 1_024L * 1_024L * 1_024L, false));
        assertEquals("1 PiB", FileSizes.formatFileSize(1_024L * 1_024L * 1_024L * 1_024L * 1_024L, false));
        assertEquals("1 EiB", FileSizes.formatFileSize(1_024L * 1_024L * 1_024L * 1_024L * 1_024L * 1_024L, false));

        assertEquals("8 EiB", FileSizes.formatFileSize(Long.MAX_VALUE, false));
    }
}
