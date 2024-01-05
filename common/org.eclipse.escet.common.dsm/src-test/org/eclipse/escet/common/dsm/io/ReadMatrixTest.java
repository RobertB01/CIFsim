//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import org.eclipse.escet.common.dsm.ClusterInput;
import org.eclipse.escet.common.java.exceptions.InputOutputException;
import org.junit.jupiter.api.Test;

/** Tests for the {@link ReadMatrix} class. */
public class ReadMatrixTest {
    @SuppressWarnings("javadoc")
    @Test
    public void testReadMatrixLines() {
        String matrix = ", A,\"BB, \", C  \nA, 0,\"\", 1\nBB,   0.1,,1e-4";
        List<List<String>> expectedLines = list(list("", "A", "BB,", "C"), list("A", "0", "", "1"),
                list("BB", "0.1", "", "1e-4"));
        BufferedReader reader = new BufferedReader(new StringReader(matrix));
        List<List<String>> actualLines = ReadMatrix.readMatrixLines(reader);
        assertEquals(expectedLines, actualLines);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithColumnLabels() {
        List<List<String>> lines = list(list("", "A", "B"), list("A", "0", "1"), list("B", "0.1", "1e-4"));
        ClusterInput custerInput = ReadMatrix.convertToMatrix(lines, null);
        assertEquals(2, custerInput.labels.length);
        assertEquals("A", custerInput.labels[0].toString());
        assertEquals("B", custerInput.labels[1].toString());
        assertEquals(2, custerInput.adjacencies.getRowDimension());
        assertEquals(2, custerInput.adjacencies.getColumnDimension());
        assertEquals(0.0, custerInput.adjacencies.getEntry(0, 0), 0.0);
        assertEquals(1.0, custerInput.adjacencies.getEntry(0, 1), 0.0);
        assertEquals(0.1, custerInput.adjacencies.getEntry(1, 0), 0.0);
        assertEquals(1e-4, custerInput.adjacencies.getEntry(1, 1), 0.0);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithoutColumnLabels() {
        List<List<String>> lines = list(list("A", "0", "1"), list("B", "0.1", "1e-4"));
        ClusterInput custerInput = ReadMatrix.convertToMatrix(lines, null);
        assertEquals(2, custerInput.labels.length);
        assertEquals("A", custerInput.labels[0].toString());
        assertEquals("B", custerInput.labels[1].toString());
        assertEquals(2, custerInput.adjacencies.getRowDimension());
        assertEquals(2, custerInput.adjacencies.getColumnDimension());
        assertEquals(0.0, custerInput.adjacencies.getEntry(0, 0), 0.0);
        assertEquals(1.0, custerInput.adjacencies.getEntry(0, 1), 0.0);
        assertEquals(0.1, custerInput.adjacencies.getEntry(1, 0), 0.0);
        assertEquals(1e-4, custerInput.adjacencies.getEntry(1, 1), 0.0);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixJagged() {
        List<List<String>> lines = list(list("A", "0", "1", "", "3"), list("B"), list("C", "4"), list("D", "5", "6"));
        ClusterInput custerInput = ReadMatrix.convertToMatrix(lines, null);
        assertEquals(4, custerInput.labels.length);
        assertEquals("A", custerInput.labels[0].toString());
        assertEquals("B", custerInput.labels[1].toString());
        assertEquals("C", custerInput.labels[2].toString());
        assertEquals("D", custerInput.labels[3].toString());
        assertEquals(4, custerInput.adjacencies.getRowDimension());
        assertEquals(4, custerInput.adjacencies.getColumnDimension());
        assertEquals(0.0, custerInput.adjacencies.getEntry(0, 0), 0.0);
        assertEquals(1.0, custerInput.adjacencies.getEntry(0, 1), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(0, 2), 0.0);
        assertEquals(3.0, custerInput.adjacencies.getEntry(0, 3), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(1, 0), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(1, 1), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(1, 2), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(1, 3), 0.0);
        assertEquals(4.0, custerInput.adjacencies.getEntry(2, 0), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(2, 1), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(2, 2), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(2, 3), 0.0);
        assertEquals(5.0, custerInput.adjacencies.getEntry(3, 0), 0.0);
        assertEquals(6.0, custerInput.adjacencies.getEntry(3, 1), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(3, 2), 0.0);
        assertEquals(0.0, custerInput.adjacencies.getEntry(3, 3), 0.0);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithoutColumnLabelsMissingRow() {
        List<List<String>> lines = list(list("A", "0", "1"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithoutColumnLabelsMissingColumn() {
        List<List<String>> lines = list(list("A", "0"), list("B", "1"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithColumnLabelsMissingRow() {
        List<List<String>> lines = list(list("", "A", "B"), list("A", "0", "1"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithColumnLabelsMissingColumn() {
        List<List<String>> lines = list(list("", "A"), list("A", "0"), list("B", "1"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixNonNumericValue() {
        List<List<String>> lines = list(list("A", "x"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixNegativeValue() {
        List<List<String>> lines = list(list("A", "-1"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixNaNValue() {
        List<List<String>> lines = list(list("A", "NaN"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixInfiniteValue() {
        List<List<String>> lines = list(list("A", "Infinity"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixTopLeftNotEmpty() {
        List<List<String>> lines = list(list("x", "A"), list("A", "0"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixRowColumnLabelMismatch() {
        List<List<String>> lines = list(list("", "A"), list("B", "0"));
        assertThrows(InputOutputException.class, () -> ReadMatrix.convertToMatrix(lines, null));
    }
}
