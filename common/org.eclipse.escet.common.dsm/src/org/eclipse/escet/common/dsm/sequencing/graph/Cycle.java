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

import java.util.BitSet;
import java.util.List;

/** A cycle in a graph. */
public class Cycle {
    /** Edges in the cycle. */
    public final List<Edge> edges;

    /** Set of vertices of the cycle. */
    public final BitSet vertices;

    /** Index of the smallest vertex in the cycle. */
    public int smallestVertexIndex;

    /**
     * Constructor of the {@link Cycle} class.
     *
     * @param edges Edges of the cycle.
     */
    public Cycle(List<Edge> edges) {
        this.edges = edges;

        vertices = new BitSet();
        int smallestIndex = -1;
        int smallestVertex = Integer.MAX_VALUE;
        for (int edgeIndex = 0; edgeIndex < edges.size(); edgeIndex++) {
            int vertex = edges.get(edgeIndex).producingVertex;
            vertices.set(vertex);
            if (smallestVertex > vertex) {
                smallestVertex = vertex;
                smallestIndex = edgeIndex;
            }
        }
        smallestVertexIndex = smallestIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        // Are the vertices sets the same?
        // Note this abstracts from vertices order in the cycles.
        if (!(obj instanceof Cycle otherCycle) || !vertices.equals(otherCycle.vertices)) {
            return false;
        }

        // Compare the exact order of the vertices.
        // As the vertex sets of the cycles are the same, the lowest vertex is also the same and doesn't need checking.
        int thisIndex = smallestVertexIndex;
        int otherIndex = otherCycle.smallestVertexIndex;
        for (int index = 1; index < edges.size(); index++) {
            // Increment the indices first to match the 'index' counter.
            thisIndex = (thisIndex + 1) % edges.size();
            otherIndex = (otherIndex + 1) % edges.size();
            if (otherCycle.edges.get(otherIndex).producingVertex != edges.get(thisIndex).producingVertex) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return vertices.hashCode(); // Abstracts from the order of the vertices.
    }
}
