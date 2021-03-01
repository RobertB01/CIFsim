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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/** Helper class with file size related functionality. */
public class FileSizes {
    /** International System of Units (SI) units, i.e. powers of ten units, for different scale file sizes. */
    private static final String[] SI_NAMES = {"B", "kB", "MB", "GB", "TB", "PB", "EB"};

    /** IEEE 1541-2002 standard units, i.e. powers of two units, for different scale file sizes. */
    private static final String[] IEEE_1541_2002_NAMES = {"B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB"};

    /** Maximum scale for file size units. */
    private static final int MAX_SCALE = SI_NAMES.length;

    /** Constructor for the {@link FileSizes} class. */
    private FileSizes() {
        // Static class.
    }

    /**
     * Formats a file size in a human readable representation, both as the actual number of bytes, as well as in a more
     * readable unit, according to the IEEE 1541-2002 standard.
     *
     * @param size The file size in bytes.
     * @return The human readable representation of the file size.
     */
    public static String formatFileSizeEx(long size) {
        return fmt("%s bytes (~%s)", new DecimalFormat("###,###,###,###,###,###,##0").format(size),
                formatFileSize(size, false));
    }

    /**
     * Formats a file size in a human readable representation, in a human readable unit.
     *
     * @param size The file size in bytes.
     * @param si Whether to use the International System of Units (SI) units ({@code true}), which uses powers of ten,
     *     or to use the IEEE 1541-2002 standard ({@code false}), which uses powers of two.
     * @return The human readable representation of the file size.
     */
    public static String formatFileSize(long size, boolean si) {
        BigDecimal unit = si ? new BigDecimal(1000) : new BigDecimal(1024);
        BigDecimal value = new BigDecimal(size);
        int scale = 0;
        while (value.compareTo(unit) >= 0) {
            value = value.divide(unit, MAX_SCALE * 3, RoundingMode.HALF_UP);
            scale++;
        }
        value = value.divide(BigDecimal.ONE, 3, RoundingMode.HALF_UP);
        if (value.compareTo(new BigDecimal(1000)) >= 0) {
            value = value.divide(unit, 3, RoundingMode.HALF_UP);
            scale++;
        }
        StringBuilder result = new StringBuilder();
        result.append(new DecimalFormat("##0.###").format(value));
        result.append(' ');
        result.append((si ? SI_NAMES : IEEE_1541_2002_NAMES)[scale]);
        return result.toString();
    }
}
