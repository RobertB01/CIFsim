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

package org.eclipse.escet.common.box;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.java.Strings;

/** Box that combines sub-boxes in a grid (or matrix). This class does not support jagged grids. */
public class GridBox extends Box {
    /** Cells of the grid. Indexed using: {@code cells[rowIdx][colIdx]}. */
    private final Box[][] cells;

    /** Number of rows of the grid. */
    private final int numRows;

    /** Number of columns of the grid. */
    private final int numCols;

    /** Amount of space between columns. */
    private final int colSpace;

    /** Amount of space between rows. */
    private final int rowSpace;

    /**
     * Constructor for the {@link GridBox} class, without space between rows and columns.
     *
     * @param numRows Number of rows of the grid.
     * @param numCols Number of columns of the grid.
     */
    public GridBox(int numRows, int numCols) {
        this(numRows, numCols, 0, 0);
    }

    /**
     * Constructor for the {@link GridBox} class.
     *
     * @param numRows Number of rows of the grid.
     * @param numCols Number of columns of the grid.
     * @param rowSpace Amount of space between rows.
     * @param colSpace Amount of space between columns.
     */
    public GridBox(int numRows, int numCols, int rowSpace, int colSpace) {
        cells = new Box[numRows][numCols];
        this.numRows = numRows;
        this.numCols = numCols;
        this.rowSpace = rowSpace;
        this.colSpace = colSpace;
    }

    /**
     * Get a box for a position in the grid.
     *
     * @param row 0-based index of the row of the box.
     * @param col 0-based index of the column of the box.
     * @return The box at the cell, or {@code null} if not yet set.
     */
    public Box get(int row, int col) {
        return cells[row][col];
    }

    /**
     * Put a box into a position in the grid.
     *
     * @param row 0-based index of the row of the box.
     * @param col 0-based index of the column of the box.
     * @param box The box to put in.
     */
    public void set(int row, int col, Box box) {
        cells[row][col] = box;
    }

    /**
     * Put a text into a position in the grid.
     *
     * @param row 0-based index of the row of the box.
     * @param col 0-based index of the column of the box.
     * @param txt The text to put in. It must not contain new line characters.
     */
    public void set(int row, int col, String txt) {
        set(row, col, new TextBox(txt));
    }

    @Override
    public List<String> getLines() {
        // Optimization for no rows.
        if (numRows == 0) {
            return list();
        }

        // Get layout data.
        GridBoxLayout layout = computeLayout();

        // Initialize all lines.
        StringBuilder[] buffers = new StringBuilder[layout.totalHeight];
        for (int lineIdx = 0; lineIdx < layout.totalHeight; lineIdx++) {
            buffers[lineIdx] = new StringBuilder(layout.totalWidth);
        }

        // Add each column.
        String colSpaceText = Strings.spaces(colSpace);
        for (int colIdx = 0; colIdx < numCols; colIdx++) {
            int lineIdx = 0;
            for (int rowIdx = 0; rowIdx < numRows; rowIdx++) {
                if (rowIdx > 0) {
                    lineIdx += rowSpace;
                }

                int count = 0;

                String[] cellLines = layout.txts[rowIdx][colIdx];
                if (cellLines != null) {
                    for (String line: cellLines) {
                        if (colIdx > 0) {
                            buffers[lineIdx].append(colSpaceText);
                        }
                        line = StringUtils.rightPad(line, layout.widths[colIdx]);
                        buffers[lineIdx].append(line);
                        lineIdx++;
                        count++;
                    }
                }

                if (count < layout.heights[rowIdx]) {
                    String line = Strings.spaces(layout.widths[colIdx]);
                    if (colIdx > 0) {
                        line = colSpaceText + line;
                    }
                    while (count < layout.heights[rowIdx]) {
                        buffers[lineIdx].append(line);
                        lineIdx++;
                        count++;
                    }
                }
            }
        }

        // Strip trailing whitespace and compute result.
        List<String> lines = list();
        for (int idx = 0; idx < layout.totalHeight; idx++) {
            lines.add(StringUtils.stripEnd(buffers[idx].toString(), " "));
        }

        // Return the final lines.
        return lines;
    }

    /**
     * Computes the layout data for the grid box.
     *
     * @return The layout data for the grid box.
     */
    public GridBoxLayout computeLayout() {
        // Initialize layout.
        GridBoxLayout layout = new GridBoxLayout();
        layout.numCols = numCols;
        layout.numRows = numRows;
        layout.widths = new int[numCols];
        layout.heights = new int[numRows];
        layout.txts = new String[numRows][numCols][];

        // Convert cells to text, and compute maximum column/row width/height.
        for (int rowIdx = 0; rowIdx < numRows; rowIdx++) {
            for (int colIdx = 0; colIdx < numCols; colIdx++) {
                // Get lines for cell.
                Box box = cells[rowIdx][colIdx];
                String[] cellLines = null;
                if (box != null) {
                    List<String> lines = box.getLines();
                    if (!lines.isEmpty()) {
                        cellLines = new String[lines.size()];
                        lines.toArray(cellLines);
                        layout.txts[rowIdx][colIdx] = cellLines;
                    }
                }

                // Update maximums.
                if (cellLines != null) {
                    // Update maximum height of row.
                    layout.heights[rowIdx] = Math.max(layout.heights[rowIdx], cellLines.length);

                    // Update maximum width of column.
                    int cellWidth = 0;
                    for (String line: cellLines) {
                        cellWidth = Math.max(cellWidth, line.length());
                    }
                    layout.widths[colIdx] = Math.max(layout.widths[colIdx], cellWidth);
                }
            }
        }

        // Compute total height.
        for (int rowIdx = 0; rowIdx < numRows; rowIdx++) {
            if (rowIdx > 0) {
                layout.totalHeight += rowSpace;
            }
            layout.totalHeight += layout.heights[rowIdx];
        }

        // Compute total width.
        for (int colIdx = 0; colIdx < numCols; colIdx++) {
            if (colIdx > 0) {
                layout.totalWidth += colSpace;
            }
            layout.totalWidth += layout.widths[colIdx];
        }

        // Return the computed layout data.
        return layout;
    }

    /** Layout data computed for a grid box. */
    public static class GridBoxLayout {
        /** The number of rows. */
        public int numRows;

        /** The number of columns. */
        public int numCols;

        /** The width of each column. */
        public int[] widths;

        /** The height of each row. */
        public int[] heights;

        /** The total width of all columns, including space between columns. */
        public int totalWidth;

        /** The total height of all row, including space between rows. */
        public int totalHeight;

        /**
         * For each row, for each column, the lines of text of that cell. The lines may be {@code null} in case there is
         * no text in the cell.
         */
        public String[][][] txts;
    }
}
