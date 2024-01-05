//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.runtime.data;

import static org.eclipse.escet.chi.runtime.IoFunctions.readReal;
import static org.eclipse.escet.chi.runtime.IoFunctions.writeReal;
import static org.eclipse.escet.common.java.Assert.check;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Iterator;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;
import org.eclipse.escet.chi.runtime.data.io.ChiFileHandle;
import org.eclipse.escet.chi.runtime.data.io.ChiWriteMemoryFile;

/** Runtime representation of a Chi matrix object. */
public class ChiMatrix {
    /** Number of rows. */
    public final int numRows;

    /** Number of columns. */
    public final int numColumns;

    /**
     * Array holding the matrix data.
     *
     * <p>
     * Data is organized row-major, that is, value (r,c) is at {@code c + numColumns * r}.
     * </p>
     */
    private double[] data;

    /**
     * Constructor for the {@link ChiMatrix} class.
     *
     * @param numRows Number of rows in the matrix.
     * @param numColumns Number of columns in the matrix.
     */
    public ChiMatrix(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        check(this.numRows > 0);
        check(this.numColumns > 0);
        this.data = new double[numRows * numColumns];
    }

    /**
     * Copy constructor of the {@link ChiMatrix} class.
     *
     * @param orig Matrix to copy.
     * @param deepCopy Perform a deep copy (has no effect).
     */
    public ChiMatrix(ChiMatrix orig, boolean deepCopy) {
        this.numRows = orig.numRows;
        this.numColumns = orig.numColumns;
        this.data = new double[orig.data.length];
        System.arraycopy(orig.data, 0, data, 0, orig.data.length);
    }

    /**
     * Retrieve a value from the matrix.
     *
     * @param r Row number of the value to retrieve.
     * @param c Column number of the value to retrieve.
     * @return Value at the specified position.
     */
    public double get(int r, int c) {
        check(r >= 0 && r < numRows);
        check(c >= 0 && c < numColumns);
        return data[c + r * numColumns];
    }

    /**
     * Set a value in the matrix.
     *
     * <p>
     * Note that setting a value does not handle sharing of objects.
     * </p>
     *
     * @param r Row number of the value to set.
     * @param c Column number of the value to set.
     * @param val New value.
     */
    public void set(int r, int c, double val) {
        check(r >= 0 && r < numRows);
        check(c >= 0 && c < numColumns);
        data[c + r * numColumns] = val;
    }

    /**
     * Load a list into the matrix. Matrix must have 1 row and the same number of columns as the length of the list.
     *
     * @param src List to load into the matrix.
     */
    public void loadList(IndexableDeque<Double> src) {
        if (numRows != 1) {
            String msg = fmt("Cannot cast to a matrix with %d rows (only 1 row is supported).", numRows);
            throw new ChiSimulatorException(msg);
        }
        if (numColumns != src.size()) {
            String msg = fmt("Cannot cast a list with %d values to a matrix with %d columns.", src.size(), numColumns);
            throw new ChiSimulatorException(msg);
        }
        Iterator<Double> iter = src.iterator();
        for (int c = 0; c < numColumns; c++) {
            set(0, c, iter.next());
        }
    }

    /**
     * Read a matrix from the input stream.
     *
     * @param coord Chi coordinator (not used).
     * @param numRows Number of rows of the matrix.
     * @param numColumns Number of columns of the matrix.
     * @param stream Input stream
     * @return Read matrix.
     */
    public static ChiMatrix read(ChiCoordinator coord, int numRows, int numColumns, ChiFileHandle stream) {
        ChiMatrix mat = new ChiMatrix(numRows, numColumns);
        stream.expectCharacter('[');
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                mat.set(r, c, readReal(stream));
                if (c + 1 < numColumns) {
                    stream.expectCharacter(',');
                }
            }
            if (r + 1 < numRows) {
                stream.expectCharacter(';');
            }
        }
        stream.expectCharacter(']');
        return mat;
    }

    /**
     * Write a matrix to a stream.
     *
     * @param stream Stream to write to.
     */
    public void write(ChiFileHandle stream) {
        stream.write("[");
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                writeReal(stream, get(r, c));
                if (c + 1 < numColumns) {
                    stream.write(", ");
                }
            }
            if (r + 1 < numRows) {
                stream.write("; ");
            }
        }
        stream.write("]");
    }

    /**
     * Convert a matrix to a string representation.
     *
     * @return String representation of the matrix.
     */
    @Override
    public String toString() {
        ChiWriteMemoryFile mem = new ChiWriteMemoryFile();
        write(mem);
        return mem.getData();
    }

    @Override
    public int hashCode() {
        int hash = 67 + numRows * 13 + numColumns * 23;
        for (int i = 0; i < data.length; i++) {
            hash += Double.doubleToLongBits(data[i]) * (i % 16);
        }
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ChiMatrix)) {
            return false;
        }
        ChiMatrix cm = (ChiMatrix)other;
        if (cm.numRows != numRows || cm.numColumns != numColumns) {
            return false;
        }
        for (int i = 0; i < data.length; i++) {
            if (cm.data[i] != data[i]) {
                return false;
            }
        }
        return true;
    }
}
