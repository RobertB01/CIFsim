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

package org.eclipse.escet.common.dsm;

import org.apache.commons.math3.linear.RealMatrix;

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
     * @param rowLabels Names of the labels of the elements along the rows
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Header with first column empty.
        sb.append("\"\"");
        for (Label label: columnLabels) {
            sb.append(",");
            String labelText = (label == null) ? "null" : label.toString();
            sb.append("\"" + labelText + "\"");
        }
        sb.append(System.lineSeparator());

        // All rows.
        for (int row = 0; row < rowLabels.length; row++) {
            sb.append("\"" + rowLabels[row].toString() + "\"");
            for (int col = 0; col < columnLabels.length; col++) {
                sb.append(",");
                // Output value, but for integers omit the trailing ".0".
                double value = adjacencies.getEntry(row, col);
                if ((int)value == value) {
                    sb.append((int)value);
                } else {
                    sb.append(value);
                }
            }
            // Skip %n at last line.
            if (row < rowLabels.length - 1) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
