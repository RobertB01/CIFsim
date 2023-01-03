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

import java.util.Arrays;
import java.util.BitSet;

import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.eclipse.escet.common.java.Assert;

/** Helper functions to compute clusters in a matrix. */
public class MatrixHelper {
    /** Constructor of the static {@link MatrixHelper} class. */
    private MatrixHelper() {
        // Static class.
    }

    /**
     * Normalize the columns of the given matrix (sum becomes {@code 1}, if not all {@code 0}).
     *
     * @param m The matrix to normalize.
     * @return The resulting normalized matrix.
     */
    public static RealMatrix normalizeColumns(RealMatrix m) {
        return normalizeColumns(m, true);
    }

    /**
     * Normalize the columns of the given matrix (sum becomes {@code 1}, if not all {@code 0}).
     *
     * @param m The matrix to normalize.
     * @param copyData Whether to construct a new matrix as result. If {@code false}, the given matrix is modified.
     * @return The resulting normalized matrix.
     */
    public static RealMatrix normalizeColumns(RealMatrix m, boolean copyData) {
        RealMatrix result = (copyData) ? new BlockRealMatrix(m.getRowDimension(), m.getColumnDimension()) : m;
        for (int j = 0; j < m.getColumnDimension(); j++) {
            double[] col = m.getColumn(j);
            normalizeArray(col);
            result.setColumn(j, col);
        }
        return result;
    }

    /**
     * Normalize the rows of the given matrix (sum becomes {@code 1}, if not all {@code 0}).
     *
     * @param m The matrix to normalize.
     * @return The resulting normalized matrix.
     */
    public static RealMatrix normalizeRows(RealMatrix m) {
        return normalizeRows(m, true);
    }

    /**
     * Normalize the rows of the given matrix (sum becomes {@code 1}, if not all {@code 0}).
     *
     * @param m The matrix to normalize.
     * @param copyData Whether to construct a new matrix as result. If {@code false}, the given matrix is modified.
     * @return The resulting normalized matrix.
     */
    public static RealMatrix normalizeRows(RealMatrix m, boolean copyData) {
        RealMatrix result = (copyData) ? new BlockRealMatrix(m.getRowDimension(), m.getColumnDimension()) : m;
        for (int i = 0; i < m.getRowDimension(); i++) {
            double[] row = m.getRow(i);
            normalizeArray(row);
            result.setRow(i, row);
        }
        return result;
    }

    /**
     * Normalize the provided sequence of real values (sum becomes {@code 1} if not all {@code 0}).
     *
     * @param sequence Sequence to normalize. Is updated in-place.
     */
    public static void normalizeArray(double[] sequence) {
        double sum = Arrays.stream(sequence).sum();
        if (sum > 0) {
            for (int j = 0; j < sequence.length; j++) {
                sequence[j] /= sum;
            }
        }
    }

    /**
     * Get the maximum value in the requested column.
     *
     * @param m Matrix to inspect.
     * @param column Column index.
     * @return Maximum value in the column.
     */
    public static double getColumnMax(RealMatrix m, int column) {
        double maximum = 0;
        for (int i = 0; i < m.getRowDimension(); i++) {
            double v = m.getEntry(i, column);
            if (i == 0 || maximum < v) {
                maximum = v;
            }
        }
        return maximum;
    }

    /**
     * Prune small values of the given matrix away.
     *
     * <p>
     * Values below {@code cutValue} are cleared to {@code 0}, while retaining normalized columns.
     * </p>
     *
     * @param m Matrix to prune, is modified in-place.
     * @param cutValues Smallest value for each column in the matrix that is not cleared.
     */
    public static void prune(RealMatrix m, double[] cutValues) {
        final int numCols = m.getColumnDimension();
        final int numRows = m.getRowDimension();

        for (int j = 0; j < numCols; j++) {
            double[] col = m.getColumn(j);

            // Drop small values (less than cutValue), sum non-small values of the column.
            double sum = 0;
            for (int i = 0; i < numRows; i++) {
                if (col[i] < cutValues[j]) {
                    col[i] = 0;
                } else {
                    sum += col[i];
                }
            }
            if (sum > 0) {
                // Normalize if anything is left.
                for (int i = 0; i < numRows; i++) {
                    col[i] /= sum;
                }
            }

            // Write result back to the matrix.
            m.setColumn(j, col);
        }
    }

    /**
     * Sets the values of the main diagonal to {@code 0}. Non-square matrices are ignored.
     *
     * @param m Matrix of which to clear main diagonal, is modified in-place.
     */
    public static void clearDiagonal(RealMatrix m) {
        if (!m.isSquare()) {
            return;
        }

        for (int i = 0; i < m.getColumnDimension(); i++) {
            m.setEntry(i, i, 0);
        }
    }

    /**
     * Returns the result of exponentiating each entry of the supplied matrix by {@code d}.
     *
     * @param m Matrix to exponentiate entries for.
     * @param d Value to exponentiate all entries by.
     * @return The resulting matrix.
     */
    public static RealMatrix scalarPower(RealMatrix m, double d) {
        return scalarPower(m, d, true);
    }

    /**
     * Returns the result of exponentiating each entry of the supplied matrix by {@code d}.
     *
     * @param m Matrix to exponentiate entries for.
     * @param d Value to exponentiate all entries by.
     * @param copyData Whether to construct a new matrix as result. If {@code false}, the given matrix is modified.
     * @return The resulting matrix.
     */
    public static RealMatrix scalarPower(RealMatrix m, double d, boolean copyData) {
        final int numCols = m.getColumnDimension();
        final int numRows = m.getRowDimension();

        RealMatrix result = (copyData) ? new BlockRealMatrix(numRows, numCols) : m;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                result.setEntry(i, j, Math.pow(m.getEntry(i, j), d));
            }
        }

        return result;
    }

    /**
     * Shuffle the rows of the matrix.
     *
     * @param rowShuffle Shuffle table, each entry {@code i} contains the original index of the row.
     * @param m Matrix to shuffle.
     * @return The shuffled matrix.
     */
    public static RealMatrix shuffleRows(int[] rowShuffle, RealMatrix m) {
        final int nrows = m.getRowDimension();
        final int ncols = m.getColumnDimension();
        Assert.check(nrows == rowShuffle.length);

        RealMatrix result = new BlockRealMatrix(nrows, ncols);
        for (int i = 0; i < nrows; i++) {
            result.setRowMatrix(i, m.getRowMatrix(rowShuffle[i]));
        }
        return result;
    }

    /**
     * Shuffle the columns of the matrix.
     *
     * @param columnShuffle Shuffle table, each entry {@code i} contains the original index of the column.
     * @param m Matrix to shuffle.
     * @return The shuffled matrix.
     */
    public static RealMatrix shuffleColumns(int[] columnShuffle, RealMatrix m) {
        final int nrows = m.getRowDimension();
        final int ncols = m.getColumnDimension();
        Assert.check(ncols == columnShuffle.length);

        RealMatrix result = new BlockRealMatrix(nrows, ncols);
        for (int i = 0; i < ncols; i++) {
            result.setColumnMatrix(i, m.getColumnMatrix(columnShuffle[i]));
        }
        return result;
    }

    /**
     * Creates a matrix with all entries set to {@code 1}.
     *
     * @param nrows Number of rows.
     * @param ncols Number of columns.
     * @return Matrix with ones as entries.
     */
    public static RealMatrix ones(int nrows, int ncols) {
        RealMatrix result = new BlockRealMatrix(nrows, ncols);
        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                result.setEntry(i, j, 1);
            }
        }
        return result;
    }

    /**
     * Find indices of nonzero entries in a one-dimensional matrix.
     *
     * @param m A one-dimensional matrix.
     * @return The indices of nonzero entries.
     */
    public static BitSet find(RealMatrix m) {
        Assert.check(m.getColumnDimension() == 1 || m.getRowDimension() == 1);

        if (m.getColumnDimension() == 1) {
            return findR(m);
        } else {
            return findC(m);
        }
    }

    /**
     * Find the row indices for which the entry is nonzero.
     *
     * @param m A matrix with a single column.
     * @return The row indices with nonzero entries.
     */
    private static BitSet findR(RealMatrix m) {
        Assert.check(m.getColumnDimension() == 1);

        BitSet result = new BitSet(m.getRowDimension());
        for (int j = 0; j < m.getRowDimension(); j++) {
            if (m.getEntry(j, 0) != 0) {
                result.set(j);
            }
        }
        return result;
    }

    /**
     * Find the column indices for which the entry is nonzero.
     *
     * @param m A matrix with a single row.
     * @return The column indices with nonzero entries.
     */
    private static BitSet findC(RealMatrix m) {
        Assert.check(m.getRowDimension() == 1);

        BitSet result = new BitSet(m.getColumnDimension());
        for (int j = 0; j < m.getColumnDimension(); j++) {
            if (m.getEntry(0, j) != 0) {
                result.set(j);
            }
        }
        return result;
    }

    /**
     * Find indices of the matrix where the value equals the given value in a one-dimensional matrix.
     *
     * @param m A one-dimensional matrix.
     * @param value The value to match.
     * @return The indices for the matching values.
     */
    public static BitSet find(RealMatrix m, double value) {
        Assert.check(m.getColumnDimension() == 1 || m.getRowDimension() == 1);

        if (m.getColumnDimension() == 1) {
            return findR(m, value);
        } else {
            return findC(m, value);
        }
    }

    /**
     * Find the row indices of a single column matrix where the value equals the given value.
     *
     * @param m A matrix with a single column.
     * @param value The value to match.
     * @return The row indices for the matching values.
     */
    private static BitSet findR(RealMatrix m, double value) {
        Assert.check(m.getColumnDimension() == 1);

        BitSet result = new BitSet(m.getRowDimension());
        for (int j = 0; j < m.getRowDimension(); j++) {
            if (m.getEntry(j, 0) == value) {
                result.set(j);
            }
        }
        return result;
    }

    /**
     * Find the column indices of a single row matrix where the value equals the given value.
     *
     * @param m A matrix with a single row.
     * @param value The value to match.
     * @return The column indices for the matching values.
     */
    private static BitSet findC(RealMatrix m, double value) {
        Assert.check(m.getRowDimension() == 1);

        BitSet result = new BitSet(m.getColumnDimension());
        for (int j = 0; j < m.getColumnDimension(); j++) {
            if (m.getEntry(0, j) == value) {
                result.set(j);
            }
        }
        return result;
    }
}
