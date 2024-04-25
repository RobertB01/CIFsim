//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm;

import static org.eclipse.escet.common.java.Lists.listc;

import java.util.Collection;
import java.util.List;
import java.util.function.DoublePredicate;
import java.util.stream.IntStream;

import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.java.CsvUtils;
import org.eclipse.escet.common.java.Lists;

/** Data storage of a domain mapping matrix. */
public class Dmm {
    /**
     * Adjacency matrix of the nodes, {@code (i, j)} is the non-negative weight of node {@code i} to node {@code j}.
     */
    public RealMatrix adjacencies;

    /** Names of the row nodes. */
    public Label[] rowLabels;

    /** Names of the column nodes. */
    public Label[] columnLabels;

    /**
     * Constructor of the {@link Dmm} class.
     *
     * @param adjacencies Adjacency graph of the nodes, {@code (i, j)} is the non-negative weight of node {@code i} to
     *     node {@code j}.
     * @param rowLabels Names of the labels of the elements along the rows.
     * @param columnLabels Names of the labels of the elements along the columns.
     */
    public Dmm(RealMatrix adjacencies, Label[] rowLabels, Label[] columnLabels) {
        this.adjacencies = adjacencies;
        this.rowLabels = rowLabels;
        this.columnLabels = columnLabels;
    }

    /**
     * Copy constructor of the {@link Dmm} class.
     *
     * @param dmm The DMM to shallow copy.
     */
    public Dmm(Dmm dmm) {
        this.adjacencies = dmm.adjacencies;
        this.rowLabels = dmm.rowLabels;
        this.columnLabels = dmm.columnLabels;
    }

    /**
     * Select rows from the {@link #adjacencies} matrix based on values in a specified column.
     *
     * @param column Column index number to use for selection.
     * @param pred Predicate that decides what rows to include.
     * @return A stream of selected row index numbers.
     */
    public IntStream filterColumn(int column, DoublePredicate pred) {
        return IntStream.range(0, adjacencies.getRowDimension())
                .filter(r -> pred.test(adjacencies.getEntry(r, column)));
    }

    /**
     * Select columns from the {@link #adjacencies} matrix based on values in a specified row.
     *
     * @param row Row index number to use for selection.
     * @param pred Predicate that decides what columns to include.
     * @return A stream of selected column index numbers.
     */
    public IntStream filterRow(int row, DoublePredicate pred) {
        return IntStream.range(0, adjacencies.getColumnDimension())
                .filter(c -> pred.test(adjacencies.getEntry(row, c)));
    }

    /**
     * Get column labels indicated by indices in the given stream.
     *
     * @param stream Stream to process.
     * @return Labels of the selected columns.
     */
    public Collection<Label> getColumnLabels(IntStream stream) {
        return stream.mapToObj(c -> columnLabels[c]).collect(Lists.toList());
    }

    /**
     * Get row labels indicated by indices in the given stream.
     *
     * @param stream Stream to process.
     * @return Labels of the selected rows.
     */
    public Collection<Label> getRowLabels(IntStream stream) {
        return stream.mapToObj(r -> rowLabels[r]).collect(Lists.toList());
    }

    @Override
    public String toString() {
        // The DMM is considered to be text here rather than a CSV file. Use 'toString(true)' to get proper CSV file
        // text.
        return toString(false);
    }

    /**
     * Convert the DMM to a string in CSV format as specified in RFC-4180, except the line delimiters may deviate
     * depending on the platform and the 'useRfcEol' parameter value, with {@code true} forcing RFC-4180 compliance.
     *
     * @param useRfcEol If {@code true} use {@code CRLF} line delimiters between the lines as specified in the RFC-4180
     *     standard. If {@code false} use {@code \n} (LF only) as line delimiter.
     * @return The converted DMM as lines of a CSV file.
     */
    public String toString(boolean useRfcEol) {
        List<List<String>> lines = listc(1 + rowLabels.length);

        // Header with first column empty.
        List<String> lineValues = listc(1 + columnLabels.length);
        lineValues.add("");
        for (Label label: columnLabels) {
            lineValues.add(label.toString());
        }
        lines.add(lineValues);

        // All rows.
        for (int row = 0; row < rowLabels.length; row++) {
            lineValues = listc(1 + columnLabels.length);
            lineValues.add(rowLabels[row].toString());

            for (int col = 0; col < columnLabels.length; col++) {
                // Output value, but for integers omit the trailing ".0".
                double value = adjacencies.getEntry(row, col);
                if ((int)value == value) {
                    lineValues.add(String.valueOf((int)value));
                } else {
                    lineValues.add(String.valueOf(value));
                }
            }
            lines.add(lineValues);
        }
        return CsvUtils.rowsToString(lines, useRfcEol);
    }
}
