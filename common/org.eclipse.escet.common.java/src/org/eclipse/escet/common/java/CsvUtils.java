//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import java.io.CharArrayReader;
import java.util.List;

/** Utility functions for handling CSV data. */
public class CsvUtils {
    /** Constructor of the {@link CsvUtils} class. */
    private CsvUtils() {
        // Static class.
    }

    /**
     * Construct a CSV parser that will parse the given text.
     *
     * @param text Text to parse.
     * @return The constructed CSV parser.
     */
    public static CsvParser makeParser(String text) {
        return new CsvParser(new CharArrayReader(text.toCharArray()));
    }

    /**
     * Get the number of rows in the CSV text lines.
     *
     * @param rows Rows of the CSV file.
     * @return Number of rows in the provided lines, or {@code 0} if there are no lines. Note that no lines or no
     *     columns cannot be expressed in CSV.
     */
    public static int getNumRows(List<List<String>> rows) {
        return rows.size();
    }

    /**
     * Get the number of columns in the CSV text lines.
     *
     * <p>
     * The function assumes all rows in the supplied lines have the same number of columns. To check that property, use
     * {@link #isRectangular}.
     * </p>
     *
     * @param rows Rows of the CSV file.
     * @return Number of columns in the provided lines, or {@code 0} if there are no lines or no columns. Note that no
     *     lines or no columns cannot be expressed in CSV.
     */
    public static int getNumColumns(List<List<String>> rows) {
        return rows.isEmpty() ? 0 : rows.get(0).size();
    }

    /**
     * Check that the supplied lines all have the same number of columns.
     *
     * @param rows Rows of the CSV file.
     * @return Whether the supplied rows all have the same number of columns. Note that zero rows also counts as a
     *     rectangular shape even though that cannot be expressed in the CSV format.
     * @see #isCsvFormat
     */
    public static boolean isRectangular(List<List<String>> rows) {
        if (rows.isEmpty()) {
            return true;
        }
        int numColumns = rows.get(0).size();
        for (int i = 1; i < rows.size(); i++) {
            if (rows.get(i).size() != numColumns) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check that the supplied rows form a proper CSV file.
     *
     * @param rows Rows of the CSV file.
     * @return Whether the given rows form a proper CSV file. (At least one row and one column, and all rows have the
     *     same number of columns.)
     */
    public static boolean isCsvFormat(List<List<String>> rows) {
        return getNumRows(rows) > 0 && getNumColumns(rows) > 0 && isRectangular(rows);
    }

    /**
     * Convert the provided rows to a string in CSV format as specified in RFC-4180, except the used line delimiters is
     * only the LF character instead of the CRLF character sequence stated in the RFC-4180 standard.
     *
     * <p>
     * To obtain a fully RFC-4180 standard compliant string, use {@link #rowsToString(List, boolean)}.
     * </p>
     *
     * @param rows Rows of the CSV file.
     * @return The converted rows as a single string in CSV format for writing to an output stream. Note that
     *     {@link CsvParser} will accept the produced string as a valid CSV file.
     */
    public static String rowsToString(List<List<String>> rows) {
        return rowsToString(rows, false);
    }

    /**
     * Convert the provided rows to a string in CSV format as specified in RFC-4180, except the line delimiters may
     * deviate depending on the 'useRfcEol' parameter value, with {@code true} producing RFC-4180 standard compliant
     * output.
     *
     * @param rows Rows of the CSV file.
     * @param useRfcEol If {@code true} use {@code CRLF} line delimiters between the lines as specified in the RFC-4180
     *     standard. If {@code false} use {@code LF} as line delimiter.
     * @return The converted rows as a single string in CSV format for writing to an output stream. Note that
     *     {@link CsvParser} will accept the produced string as a valid CSV file for either value of the
     *     {@code useRfcEol} flag.
     */
    public static String rowsToString(List<List<String>> rows, boolean useRfcEol) {
        StringBuilder sb = new StringBuilder();
        String eolSequence = useRfcEol ? "\r\n" : "\n"; // CRLF respectively LF in the RFC-4180 CSV grammar.

        boolean firstRow = true;
        for (List<String> columns: rows) {
            if (firstRow) {
                firstRow = false;
            } else {
                sb.append(eolSequence);
            }

            boolean firstColumn = true;
            for (String column: columns) {
                if (firstColumn) {
                    firstColumn = false;
                } else {
                    sb.append(",");
                }
                sb.append("\"" + column.replace("\"", "\"\"") + "\""); // RFC-4180 " -> "" escaping.
            }
        }
        // Skip newline sequence after the last line as allowed by RFC-4180.

        return sb.toString();
    }
}
