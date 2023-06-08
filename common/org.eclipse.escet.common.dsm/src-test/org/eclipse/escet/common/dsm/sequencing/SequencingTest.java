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

package org.eclipse.escet.common.dsm.sequencing;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.junit.Assert.assertEquals;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import org.eclipse.escet.common.dsm.sequencing.graph.Graph;
import org.eclipse.escet.common.dsm.sequencing.graph.Vertex;
import org.eclipse.escet.common.java.BitSetIterator;
import org.junit.Test;

/** Test sequencing of graphs. */
@SuppressWarnings("javadoc")
public class SequencingTest {
    /**
     * Sequence the provided graph and print the result, including indications of the strongly connected components.
     *
     * @param pairs Pairs to load as a graph.
     * @return The generated output.
     */
    private static String testSequencing(String pairs) {
        Graph g = Sequencer.loadVertexPairs(pairs);
        List<BitSet> collections = list();
        List<Vertex> vertices = Sequencer.sequenceGraph(g, collections);

        List<Integer> vertexPositions = listc(vertices.size());
        while (vertexPositions.size() < vertices.size()) {
            vertexPositions.add(null);
        }
        StringBuilder sb = new StringBuilder();
        int pos = 0;
        for (Vertex v: vertices) {
            if (pos > 0) {
                sb.append(" ");
                pos++;
            }
            vertexPositions.set(v.number, pos);
            sb.append(v.name);
            pos += v.name.length();
        }
        sb.append("\n");
        for (BitSet collection: collections) {
            List<Integer> positions = listc(collection.size());
            for (int vi: new BitSetIterator(collection)) {
                positions.add(vertexPositions.get(vi));
            }
            Collections.sort(positions);
            int curpos = 0;
            for (int p: positions) {
                while (curpos < p) {
                    sb.append(" ");
                    curpos++;
                }
                sb.append("*");
                curpos++;
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Test
    public void sequentialGraphTest() {
        String graphPairs = "(1, 2), (2, 3), (2, 4), (3, 4)";
        String result = testSequencing(graphPairs);
        String expected = "1 2 3 4\n";
        assertEquals(expected, result);
    }

    @Test
    public void simpleCycleGraphTest() {
        String graphPairs = "(1, 2), (2, 3), (3, 2), (3, 4)";
        String result = testSequencing(graphPairs);
        String expected = "1 3 2 4\n"
                        + "  * *\n";
        assertEquals(expected, result);
    }

    @Test
    public void kusiakWangGraphTest() {
        // Source: A. Kusiak and J.Wang, "Efficient organizing of design activities", International
        // Journal of Production Research, volume volume 31, issue 4, pages 753-769, 1993,
        // doi:<a href="https://doi.org/10.1080/00207549308956755">10.1080/00207549308956755</a>
        //
        // Example 1
        String graphPairs = """
                (1, 8) (1, 12)
                (2, 3) (2, 6) (2, 7) (2, 10) (2, 11)
                (3, 4) (3, 9) (3, 10) (3, 11)
                (4, 8)
                (5, 4)
                (6, 4) (6, 5) (6, 9) (6, 10)
                (8, 5)
                (9, 8) (9, 12)
                (10, 9) (10, 12)
                (11, 7) (11, 8) (11, 10) (11, 12)
                (12, 4) (12, 6) (12, 10)
                """;
        String result = testSequencing(graphPairs);
        String expected = "1 2 3 11 7 6 10 9 12 5 4 8\n"
                        + "           * *  * *\n"
                        + "                     * * *\n";
        assertEquals(expected, result);
    }
}
