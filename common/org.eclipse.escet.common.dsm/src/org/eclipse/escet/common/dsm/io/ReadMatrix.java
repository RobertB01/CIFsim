//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.io;

import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.slice;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Strings.slice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.dsm.ClusterInputData;
import org.eclipse.escet.common.dsm.Label;
import org.eclipse.escet.common.java.Assert;

/** Code for reading and writing a matrix. */
public class ReadMatrix {
    /** Whitespace regular expression pattern. */
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");

    /** Matrix quoted entry regular expression pattern. */
    private static final Pattern QUOTED_ENTRY_PATTERN = Pattern.compile("\\\"[^\\\"]*\\\"");

    /** Matrix regular entry regular expression pattern. */
    private static final Pattern REGULAR_ENTRY_PATTERN = Pattern.compile("[^, ]*");

    /** Matrix entry separator regular expression pattern. */
    private static final Pattern SEPARATOR_PATTERN = Pattern.compile(",");

    /** Constructor of the static {@link ReadMatrix} class. */
    private ReadMatrix() {
        // Static class.
    }

    /**
     * Parse a line of text to its entries.
     *
     * @param line Line to parse.
     * @return The entries.
     * @throws IOException In case of a parse error.
     */
    private static List<String> parseLine(String line) throws IOException {
        List<String> entries = list();

        Matcher whitespaceMatcher = WHITESPACE_PATTERN.matcher(line);
        Matcher quotedEntryMatcher = QUOTED_ENTRY_PATTERN.matcher(line);
        Matcher regularEntryMatcher = REGULAR_ENTRY_PATTERN.matcher(line);
        Matcher separatorMatcher = SEPARATOR_PATTERN.matcher(line);

        int index = 0;
        while (true) {
            // Skip whitespace.
            if (whitespaceMatcher.find(index) && whitespaceMatcher.start() == index) {
                index += whitespaceMatcher.end() - index;
            }

            // Parse an entry.
            if (quotedEntryMatcher.find(index) && quotedEntryMatcher.start() == index) {
                String entry = slice(quotedEntryMatcher.group(), 1, -1);
                entries.add(entry);
                index += quotedEntryMatcher.end() - index;
            } else {
                Assert.check(regularEntryMatcher.find(index));
                Assert.areEqual(regularEntryMatcher.start(), index);
                String entry = regularEntryMatcher.group();
                entries.add(entry);
                index += regularEntryMatcher.end() - index;
            }

            // Skip whitespace.
            if (whitespaceMatcher.find(index) && whitespaceMatcher.start() == index) {
                index += whitespaceMatcher.end() - index;
            }

            // Parse a separator.
            if (separatorMatcher.find(index) && separatorMatcher.start() == index) {
                index += separatorMatcher.end() - index;
            } else {
                break;
            }
        }

        if (index != line.length()) {
            throw new IOException(fmt("Failed to parse matrix line at position %d.", index + 1));
        }

        return entries;
    }

    /**
     * Take the rows and columns of texts, and convert it to input data for the cluster algorithm.
     * <p>
     * Function may throw a {@link InputOutputException} if the received data cannot be parsed. Missing or negative
     * adjacency values are assumed to be {@code 0}.
     * </p>
     *
     * @param matrixLines Read input, rows of columns of texts.
     * @return The found cluster input data (adjacency values and labels).
     * @throws IOException In case of a conversion error.
     */
    static ClusterInputData convertToMatrix(List<List<String>> matrixLines) throws IOException {
        // Decide on the size of the matrix.
        // Note that rows may be one longer than columns, as row labels are mandatory, while column labels are optional.
        int matRowCount = matrixLines.size();
        int matColcount = matrixLines.stream().map(row -> row.size()).max(Math::max).orElseGet(() -> 0);

        int firstDataLine; // First line containing data.
        if (matRowCount > 1 && matRowCount == matColcount) {
            // First line has column labels.
            firstDataLine = 1;
        } else if (matRowCount >= 1 && matRowCount + 1 == matColcount) {
            // First line has data.
            firstDataLine = 0;
        } else {
            String msg = "Matrix data is not square, found %d rows and %d columns, excluding first label column.";
            throw new IOException(fmt(msg, matRowCount, matColcount - 1));
        }

        int size = matRowCount - firstDataLine;
        RealMatrix adjMat = new BlockRealMatrix(size, size);
        Label[] rowLabels = new Label[size];

        // Copy data to matrix.
        for (int row = 0; row < size; row++) {
            List<String> line = matrixLines.get(firstDataLine + row);
            rowLabels[row] = (line.size() > 0) ? new Label(line.get(0)) : new Label("");
            int col = 0;
            for (; col < size; col++) {
                if (line.size() <= col + 1) {
                    break;
                }

                String valueText = line.get(col + 1);
                double value;
                if (valueText.isBlank()) {
                    value = 0.0;
                } else {
                    try {
                        value = Double.parseDouble(valueText);
                    } catch (NumberFormatException ex) {
                        throw new IOException(fmt("Value \"%s\" is not numeric.", valueText));
                    }
                }
                if (value < 0) {
                    throw new IOException(fmt("Value \"%s\" is negative.", valueText));
                }
                if (Double.isNaN(value)) {
                    throw new IOException(fmt("Value \"%s\" is not a number.", valueText));
                }
                if (Double.isInfinite(value)) {
                    throw new IOException(fmt("Value \"%s\" is infinite.", valueText));
                }

                adjMat.setEntry(row, col, value);
            }
            for (; col < size; col++) {
                adjMat.setEntry(row, col, 0);
            }
        }

        // Check column labels, if any.
        if (firstDataLine == 1) {
            // Check top-left cell.
            List<String> firstLine = matrixLines.get(0);
            String topLeftCell = first(firstLine);
            if (!topLeftCell.isEmpty()) {
                throw new IOException(
                        fmt("The top-left cell of the matrix contains \"%s\" rather than being empty.", topLeftCell));
            }

            // Check row vs column labels.
            Label[] colLabels = slice(firstLine, 1, null).stream().map(l -> new Label(l)).toArray(l -> new Label[l]);
            Assert.areEqual(rowLabels.length, size);
            Assert.areEqual(colLabels.length, size);
            for (int i = 0; i < size; i++) {
                if (!rowLabels[i].equals(colLabels[i])) {
                    throw new IOException(
                            fmt("Row label \"%s\" is different from column label \"%s\".", rowLabels[i], colLabels[i]));
                }
            }
        }

        // Return cluster input data.
        return new ClusterInputData(adjMat, rowLabels);
    }

    /**
     * Read the matrix elements as rows and columns of texts.
     *
     * @param reader The reader to use.
     * @return Rows of columns of texts. Note that the number of columns at each line may be different. Also, no
     *     checking is performed whether the number of lines and columns match.
     * @throws IOException In case of an I/O or parse error.
     */
    static List<List<String>> readMatrixLines(BufferedReader reader) throws IOException {
        List<List<String>> matrixValues = list();
        int lineNr = 0;
        while (true) {
            lineNr++;
            String line;
            try {
                line = reader.readLine();
            } catch (IOException ex) {
                throw new IOException(fmt("Failed to read matrix line %d.", lineNr), ex);
            }
            if (line == null) {
                break; // EOF reached.
            }

            List<String> words;
            try {
                words = parseLine(line);
            } catch (IOException ex) {
                throw new IOException(fmt("Failed to parse matrix line %d.", lineNr), ex);
            }
            matrixValues.add(words);
        }
        return matrixValues;
    }

    /**
     * Read the CSV-like adjacency and label data from the file with the provided name.
     *
     * <p>
     * True CSV is a complicated format, so this code only does a subset.
     * </p>
     * <p>
     * It assumes NxN numeric (real) values, as N lines of N comma separated numbers at a line. Before the first number
     * at each row should be a label designating the name of the element of that row. Optionally, above the first line
     * of data may be a line of labels as well.
     * </p>
     * <p>
     * Example: <pre>
     * "",  "A", "B"
     * "A",  1,   0
     * "B", 0.5, 0.1
     * </pre>shows a 2x2 adjacency matrix of elements A and B with an additional first line of labels.
     * </p>
     *
     * @param filepath Path of the file to read.
     * @return The read data.
     */
    public static ClusterInputData readMatrixFile(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            List<List<String>> matrixLines = readMatrixLines(reader);
            return convertToMatrix(matrixLines);
        } catch (IOException ex) {
            throw new InputOutputException(fmt("Failed to read or interpret matrix file \"%s\".", filepath), ex);
        }
    }
}
