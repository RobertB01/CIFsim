//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2023 Contributors to the Eclipse Foundation
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
import static org.eclipse.escet.common.java.Lists.slice;
import static org.eclipse.escet.common.java.Lists.toList;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.dsm.ClusterInputData;
import org.eclipse.escet.common.dsm.Label;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.CsvParser;
import org.eclipse.escet.common.java.CsvParser.CsvParseError;

/** Code for reading and writing a matrix. */
public class ReadMatrix {
    /** Constructor of the static {@link ReadMatrix} class. */
    private ReadMatrix() {
        // Static class.
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
     * @throws InputOutputException In case of a conversion error.
     */
    static ClusterInputData convertToMatrix(List<List<String>> matrixLines) {
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
            throw new InputOutputException(fmt(msg, matRowCount, matColcount - 1));
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
                        throw new InputOutputException(fmt("Value \"%s\" is not numeric.", valueText));
                    }
                }
                if (value < 0) {
                    throw new InputOutputException(fmt("Value \"%s\" is negative.", valueText));
                }
                if (Double.isNaN(value)) {
                    throw new InputOutputException(fmt("Value \"%s\" is not a number.", valueText));
                }
                if (Double.isInfinite(value)) {
                    throw new InputOutputException(fmt("Value \"%s\" is infinite.", valueText));
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
                throw new InputOutputException(
                        fmt("The top-left cell of the matrix contains \"%s\" rather than being empty.", topLeftCell));
            }

            // Check row vs column labels.
            Label[] colLabels = slice(firstLine, 1, null).stream().map(l -> new Label(l)).toArray(l -> new Label[l]);
            Assert.areEqual(rowLabels.length, size);
            Assert.areEqual(colLabels.length, size);
            for (int i = 0; i < size; i++) {
                if (!rowLabels[i].equals(colLabels[i])) {
                    throw new InputOutputException(
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
     * @return Rows of columns of texts. No checking is performed whether the number of lines and columns match.
     * @throws CsvParseError In case of an I/O or parse error.
     */
    static List<List<String>> readMatrixLines(BufferedReader reader) {
        // Parse the CSV text.
        CsvParser csvParser = new CsvParser(reader);
        List<List<String>> lines = csvParser.parse();

        // Remove leading and trailing whitespace in the CSV file.
        Function<List<String>, List<String>> trimmer = (line) -> line.stream().map(String::trim).collect(toList());
        return lines.stream().map(line -> trimmer.apply(line)).collect(toList());
    }

    /**
     * Read the CSV-like adjacency and label data from the file with the provided name.
     *
     * <p>
     * A CSV file in RFC-4180 format is required. However, leading and trailing whitespace of fields is ignored. Also,
     * we assume NxN numeric (real) values, as N lines of N comma separated numbers at a line. Before the first number
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
     * @throws InputOutputException In case of an I/O error, or the file is not in the right format.
     */
    public static ClusterInputData readMatrixFile(String filepath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            List<List<String>> matrixLines = readMatrixLines(reader);
            return convertToMatrix(matrixLines);
        } catch (IOException | CsvParseError ex) {
            throw new InputOutputException(fmt("Failed to read matrix file \"%s\".", filepath), ex);
        }
    }
}
