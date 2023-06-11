//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.eclipse.escet.common.dsm.ClusterInputData;
import org.junit.jupiter.api.Test;

/** Tests for the {@link ReadMatrix} class. */
public class ReadMatrixTest {
    @SuppressWarnings("javadoc")
    @Test
    public void testReadMatrixLines() throws IOException {
        String matrix = ", A, \"BB, \" , C \t \nA, 0, \"\", 1\nBB, \t  0.1,,1e-4";
        List<List<String>> expectedLines = list(list("", "A", "BB, ", "C"), list("A", "0", "", "1"),
                list("BB", "0.1", "", "1e-4"));
        BufferedReader reader = new BufferedReader(new StringReader(matrix));
        List<List<String>> actualLines = ReadMatrix.readMatrixLines(reader);
        assertEquals(expectedLines, actualLines);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testReadMatrixLinesMissingSeparator() {
        String matrix = "A B";
        BufferedReader reader = new BufferedReader(new StringReader(matrix));
        assertThrows(IOException.class, () -> ReadMatrix.readMatrixLines(reader));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithColumnLabels() throws IOException {
        List<List<String>> lines = list(list("", "A", "B"), list("A", "0", "1"), list("B", "0.1", "1e-4"));
        ClusterInputData data = ReadMatrix.convertToMatrix(lines);
        assertEquals(2, data.labels.length);
        assertEquals("A", data.labels[0].toString());
        assertEquals("B", data.labels[1].toString());
        assertEquals(2, data.adjacencies.getRowDimension());
        assertEquals(2, data.adjacencies.getColumnDimension());
        assertEquals(0.0, data.adjacencies.getEntry(0, 0), 0.0);
        assertEquals(1.0, data.adjacencies.getEntry(0, 1), 0.0);
        assertEquals(0.1, data.adjacencies.getEntry(1, 0), 0.0);
        assertEquals(1e-4, data.adjacencies.getEntry(1, 1), 0.0);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithoutColumnLabels() throws IOException {
        List<List<String>> lines = list(list("A", "0", "1"), list("B", "0.1", "1e-4"));
        ClusterInputData data = ReadMatrix.convertToMatrix(lines);
        assertEquals(2, data.labels.length);
        assertEquals("A", data.labels[0].toString());
        assertEquals("B", data.labels[1].toString());
        assertEquals(2, data.adjacencies.getRowDimension());
        assertEquals(2, data.adjacencies.getColumnDimension());
        assertEquals(0.0, data.adjacencies.getEntry(0, 0), 0.0);
        assertEquals(1.0, data.adjacencies.getEntry(0, 1), 0.0);
        assertEquals(0.1, data.adjacencies.getEntry(1, 0), 0.0);
        assertEquals(1e-4, data.adjacencies.getEntry(1, 1), 0.0);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixJagged() throws IOException {
        List<List<String>> lines = list(list("A", "0", "1", "", "3"), list("B"), list("C", "4"), list("D", "5", "6"));
        ClusterInputData data = ReadMatrix.convertToMatrix(lines);
        assertEquals(4, data.labels.length);
        assertEquals("A", data.labels[0].toString());
        assertEquals("B", data.labels[1].toString());
        assertEquals("C", data.labels[2].toString());
        assertEquals("D", data.labels[3].toString());
        assertEquals(4, data.adjacencies.getRowDimension());
        assertEquals(4, data.adjacencies.getColumnDimension());
        assertEquals(0.0, data.adjacencies.getEntry(0, 0), 0.0);
        assertEquals(1.0, data.adjacencies.getEntry(0, 1), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(0, 2), 0.0);
        assertEquals(3.0, data.adjacencies.getEntry(0, 3), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(1, 0), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(1, 1), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(1, 2), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(1, 3), 0.0);
        assertEquals(4.0, data.adjacencies.getEntry(2, 0), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(2, 1), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(2, 2), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(2, 3), 0.0);
        assertEquals(5.0, data.adjacencies.getEntry(3, 0), 0.0);
        assertEquals(6.0, data.adjacencies.getEntry(3, 1), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(3, 2), 0.0);
        assertEquals(0.0, data.adjacencies.getEntry(3, 3), 0.0);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithoutColumnLabelsMissingRow() {
        List<List<String>> lines = list(list("A", "0", "1"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithoutColumnLabelsMissingColumn() {
        List<List<String>> lines = list(list("A", "0"), list("B", "1"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithColumnLabelsMissingRow() {
        List<List<String>> lines = list(list("", "A", "B"), list("A", "0", "1"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixWithColumnLabelsMissingColumn() {
        List<List<String>> lines = list(list("", "A"), list("A", "0"), list("B", "1"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixNonNumericValue() {
        List<List<String>> lines = list(list("A", "x"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixNegativeValue() {
        List<List<String>> lines = list(list("A", "-1"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixNaNValue() {
        List<List<String>> lines = list(list("A", "NaN"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixInfiniteValue() {
        List<List<String>> lines = list(list("A", "Infinity"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixTopLeftNotEmpty() {
        List<List<String>> lines = list(list("x", "A"), list("A", "0"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testConvertToMatrixRowColumnLabelMismatch() {
        List<List<String>> lines = list(list("", "A"), list("B", "0"));
        assertThrows(IOException.class, () -> ReadMatrix.convertToMatrix(lines));
    }
}
