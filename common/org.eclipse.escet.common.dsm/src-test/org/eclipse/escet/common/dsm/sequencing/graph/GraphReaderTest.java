//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.sequencing.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.escet.common.java.Assert;
import org.junit.jupiter.api.Test;

/** Tests for loading a graph into the sequencer. */
public class GraphReaderTest {
    @Test
    @SuppressWarnings("javadoc")
    public void testEmptyGraphLoad() {
        Graph g = GraphReader.loadVertexPairs("");
        assertEquals("Graph with 0 vertices.\n", g.dumpGraph());
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testStringGraphLoad() {
        Graph g = GraphReader.loadVertexPairs("(0, 5) (0, 9), (5, 9)\n");

        // Dirty way to tear the 0 -> 9 edge.
        Assert.check(g.vertices.get(0).name.equals("0"));
        Assert.check(g.vertices.get(2).name.equals("9"));
        for (Edge e: g.vertices.get(0).outputs) {
            if (e.consumingVertex == 2) {
                e.teared = true;
                break;
            }
        }

        String expected = """
                Graph with 3 vertices.
                Vertex "0": -> "5", TEARED -> "9"
                Vertex "5": -> "9"
                Vertex "9": No outputs.
                """;
        assertEquals(expected, g.dumpGraph());
    }
}
