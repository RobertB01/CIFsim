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
    /** RFC-4180 line separator. */
    private static final String CRLF = "\r\n";

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

    @Override
    public String toString() {
        // The DMM is considered to be text here rather than a CSV file. Use 'toString(true)' to get proper CSV file
        // text.
        return toString(false);
    }

    /**
     * Convert the DMM to a string in CSV format as specified in RFC-4180, except the line delimiters may deviate
     * depending on the platform and the 'useRfcEol' parameter value, {@code true} forces RFC-4180 compliance.
     *
     * @param useRfcEol If {@code true} use {@code CRLF} line delimiters between the lines as specified in the RFC-4180
     *      document. If {@code false} use {@code \n} (LF only) as line delimiter.
     * @return The converted DMM as lines of a CSV file.
     */
    public String toString(boolean useRfcEol) {
        StringBuilder sb = new StringBuilder();
        String eolSequence = useRfcEol ? CRLF : "\n";

        // Header with first column empty.
        sb.append("\"\"");
        for (Label label: columnLabels) {
            sb.append(",");
            sb.append("\"" + label.toString().replace("\"", "\"\"") + "\""); // RFC-4180 " -> "" escaping.
        }
        sb.append(eolSequence);

        // All rows.
        for (int row = 0; row < rowLabels.length; row++) {
            sb.append("\"" + rowLabels[row].toString().replace("\"", "\"\"") + "\""); // RFC-4180 " -> "" escaping.
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
            // Skip newline at last line (is allowed by RFC-4180).
            if (row < rowLabels.length - 1) {
                sb.append(eolSequence);
            }
        }
        return sb.toString();
    }
}
