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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.dsm.ClusterInputData;
import org.eclipse.escet.common.dsm.Label;
import org.eclipse.escet.common.java.Assert;

/** Code for reading and writing a matrix. */
public class ReadMatrix {
    /** Constructor of the static {@link ReadMatrix} class. */
    private ReadMatrix() {
        // Static class.
    }

    /**
     * Match a 'word' to a matrix input line.
     *
     * @param line Text line to analyze.
     * @param start Start index for analyzing. Should be a character in the line.
     * @return {@code null} if match failed, else a match result with various relevant indices.
     */
    private static MatchResult matchWord(String line, int start) {
        Assert.check(start >= 0 && start < line.length());

        // Find first non-space at or after 'start'.
        int index = start;
        while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= line.length()) {
            return null;
        }

        // Grab the next word from the string.
        int groupStart = index;
        if (line.charAt(index) == '"') {
            // Parse quoted word.
            index++;
            while (index < line.length() && line.charAt(index) != '"') {
                index++;
            }
            if (index < line.length()) { // Don't skip non-existing closing quote.
                index++;
            }
            return new MatchResult(start, index, groupStart + 1, index);
        } else {
            // Parse non-quoted word.
            while (index < line.length()) {
                if (Character.isWhitespace(line.charAt(index)) || line.charAt(index) == ',') {
                    break;
                }
                index++;
            }
            return new MatchResult(start, index, groupStart, index);
        }
    }

    /**
     * Match a separator to a matrix input line.
     *
     * @param line Text line to analyze.
     * @param start Start index for analyzing. Should point at a character in the line.
     * @return {@code null} if match failed, else a match result with various relevant indices.
     */
    private static MatchResult matchSep(String line, int start) {
        Assert.check(start >= 0 && start < line.length());

        int index = start;
        while (index < line.length() && Character.isWhitespace(line.charAt(index))) {
            index++;
        }
        if (index >= line.length()) {
            return null;
        }

        if (line.charAt(index) == ',') {
            return new MatchResult(start, index + 1, index, index + 1);
        }
        return null;
    }

    /**
     * Read words from a line of text.
     *
     * @param line Line to read.
     * @return The read words.
     */
    private static List<String> parseLine(String line) {
        List<String> entries = list();

        int index = 0;
        while (true) {
            if (index >= line.length()) {
                break;
            }

            MatchResult matchResult = matchWord(line, index);
            if (matchResult == null) {
                break;
            }
            entries.add(line.substring(matchResult.groupStart, matchResult.groupEnd));

            index = matchResult.matchEnd;
            if (index >= line.length()) {
                break;
            }

            matchResult = matchSep(line, index);
            if (matchResult == null) {
                break;
            }
            index = matchResult.matchEnd;
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
     */
    private static ClusterInputData convertToMatrix(List<List<String>> matrixLines) {
        // Decide on the size of the matrix.
        // Note that rows are expected to be one longer than columns, since the first column is labels.
        //
        int matRowCount = matrixLines.size();
        int matColcount = 0;
        for (List<String> row: matrixLines) {
            matColcount = Math.max(matColcount, row.size());
        }

        int firstDataLine; // First line containing data.
        if (matRowCount > 1 && matRowCount == matColcount) {
            firstDataLine = 1;
        } else if (matRowCount >= 1 && matRowCount + 1 == matColcount) {
            // First line is data.
            firstDataLine = 0;
        } else {
            String msg = "Matrix data is not square, found %d rows and %d columns, excluding first label column.";
            throw new InputOutputException(fmt(msg, matRowCount, matColcount - 1));
        }

        int size = matRowCount - firstDataLine;
        RealMatrix adjMat = new BlockRealMatrix(size, size);
        Label[] labels = new Label[size];

        // Copy data to matrix.
        for (int row = 0; row < size; row++) {
            List<String> line = matrixLines.get(firstDataLine + row);
            labels[row] = (line.size() > 0) ? new Label(line.get(0)) : new Label("");
            int col = 0;
            for (; col < size; col++) {
                if (line.size() <= col + 1) {
                    break;
                }

                double value;
                try {
                    value = Math.max(0, Double.parseDouble(line.get(col + 1)));
                } catch (NumberFormatException ex) {
                    value = 0;
                }
                adjMat.setEntry(row, col, value);
            }
            for (; col < size; col++) {
                adjMat.setEntry(row, col, 0);
            }
        }

        return new ClusterInputData(adjMat, labels);
    }

    /**
     * Read the matrix elements as rows and columns of texts.
     *
     * @param inp Input stream.
     * @return Rows of columns of texts. Note that the number of columns at each line may be different. Also, no
     *     checking is performed whether the number of lines and columns match.
     * @throws InputOutputException When a line of text could not be read.
     */
    private static List<List<String>> readMatrixLines(BufferedReader inp) {
        List<List<String>> matrixValues = list();
        while (true) {
            String line;
            try {
                line = inp.readLine();
            } catch (IOException ex) {
                throw new InputOutputException("Failed to read a text line.", ex);
            }
            if (line == null) {
                break; // EOF reached.
            }

            List<String> words = parseLine(line);
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
            throw new InputOutputException(fmt("Failed to read input file \"%s\".", filepath), ex);
        }
    }
}
