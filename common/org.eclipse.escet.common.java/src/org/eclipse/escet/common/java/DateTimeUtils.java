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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/** Helper class with date/time related helper methods. */
public class DateTimeUtils {
    /** Constructor for the {@link DateTimeUtils} class. */
    private DateTimeUtils() {
        // Static class.
    }

    /**
     * Formats the date/time. The used date/time format is a {@link SimpleDateFormat} with
     * {@code "yyyy-MM-dd HH:mm:ss.SSS"} pattern, and {@link Locale#US} locale.
     *
     * @param dateTime The date/time to format.
     * @param utc Whether to format as UTC time zone ({@code true}), or as the users current local time zone
     *     ({@code false}).
     * @return The formatted date/time.
     */
    public static String formatDateTime(Date dateTime, boolean utc) {
        return formatDateTime(dateTime, utc, false);
    }

    /**
     * Formats the date/time. The used date/time format is a {@link SimpleDateFormat} with
     * {@code "yyyy-MM-dd HH:mm:ss.SSS"} or {@code "yyyy-MM-dd HH:mm:ss.SSS z (Z)"} pattern, and {@link Locale#US}
     * locale.
     *
     * @param dateTime The date/time to format.
     * @param utc Whether to format as UTC time zone ({@code true}), or as the users current local time zone
     *     ({@code false}).
     * @param includeTimeZone Whether to include the time zone information in the result.
     * @return The formatted date/time.
     */
    public static String formatDateTime(Date dateTime, boolean utc, boolean includeTimeZone) {
        String fmt = "yyyy-MM-dd HH:mm:ss.SSS";
        if (includeTimeZone) {
            fmt += " z (Z)";
        }
        DateFormat df = new SimpleDateFormat(fmt, Locale.US);
        if (utc) {
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return df.format(dateTime);
    }

    /**
     * Formats the date/time. The used date/time format is a {@link SimpleDateFormat} with
     * {@code "yyyy-MM-dd HH:mm:ss.SSS"} pattern, and {@link Locale#US} locale.
     *
     * @param dateTime The date/time to format, as milliseconds since epoch.
     * @param utc Whether to format as UTC time zone ({@code true}), or as the users current local time zone
     *     ({@code false}).
     * @return The formatted date/time.
     */
    public static String formatDateTime(long dateTime, boolean utc) {
        return formatDateTime(dateTime, utc, false);
    }

    /**
     * Formats the date/time. The used date/time format is a {@link SimpleDateFormat} with
     * {@code "yyyy-MM-dd HH:mm:ss.SSS"} or {@code "yyyy-MM-dd HH:mm:ss.SSS z (Z)"} pattern, and {@link Locale#US}
     * locale.
     *
     * @param dateTime The date/time to format, as milliseconds since epoch.
     * @param utc Whether to format as UTC time zone ({@code true}), or as the users current local time zone
     *     ({@code false}).
     * @param includeTimeZone Whether to include the time zone information in the result.
     * @return The formatted date/time.
     */
    public static String formatDateTime(long dateTime, boolean utc, boolean includeTimeZone) {
        return formatDateTime(new Date(dateTime), utc, includeTimeZone);
    }

    /**
     * Returns the duration in a readable form, as in: {@code "XX h XX m XX s XX ms"}.
     *
     * @param duration The duration in milliseconds.
     * @param omitZero Whether to omit zero values. If duration is zero however, the milliseconds are always shown.
     * @return the duration in a readable form.
     */
    public static String durationToString(long duration, boolean omitZero) {
        long x = duration;
        long ms = x % 1000;
        x = x / 1000;
        long s = x % 60;
        x = x / 60;
        long m = x % 60;
        x = x / 60;
        long h = x;
        StringBuilder rslt = new StringBuilder();
        if (h != 0 || !omitZero) {
            rslt.append(h + "h");
        }
        if (m != 0 || !omitZero) {
            if (rslt.length() > 0) {
                rslt.append(" ");
            }
            rslt.append(m + "m");
        }
        if (s != 0 || !omitZero) {
            if (rslt.length() > 0) {
                rslt.append(" ");
            }
            rslt.append(s + "s");
        }
        if (ms != 0 || !omitZero || duration == 0) {
            if (rslt.length() > 0) {
                rslt.append(" ");
            }
            rslt.append(ms + "ms");
        }
        return rslt.toString();
    }

    /**
     * Converts a time or duration in nanoseconds to milliseconds.
     *
     * @param nanoTime The time or duration in nanoseconds.
     * @return The time or duration in miliseconds.
     */
    public static long nanoTimeToMillis(long nanoTime) {
        return (long)(nanoTime / 1e6);
    }
}
