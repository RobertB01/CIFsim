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

    /**
     * Constructor of the {@link Cycle} class.
     *
     * @param edges Edges of the cycle.
     */
    public Cycle(List<Edge> edges) {
        this.edges = edges;

        vertices = new BitSet();
        for (Edge e: edges) {
            vertices.set(e.producingVertex);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Cycle otherCycle) || !vertices.equals(otherCycle.vertices)) {
            return false;
        }

        // Compute offset of 'otherCycle' for the first element of this cycle.
        int offset; // Avoid false positive Checkstyle warning.
        for (offset = 0; offset < edges.size(); offset++) {
            if (otherCycle.edges.get(offset).producingVertex != edges.get(0).producingVertex) {
                continue;
            }

            // As these elements contain simple cycles, the same vertex will not be found at any other offset.
            // Therefore, to find a match it must happen at the found offset.
            for (int index = 1; index < edges.size(); index++) {
                offset = (offset + 1) % edges.size(); // Increment offset before comparing so index and offset match.
                if (otherCycle.edges.get(offset).producingVertex != edges.get(index).producingVertex) {
                    return false;
                }
            }
            return true;
        }
        throw new AssertionError("Vertex " + edges.get(0).producingVertex + " has mysteriously disappeared.");
    }

    @Override
    public int hashCode() {
        return vertices.hashCode(); // Abstracts from the order of the vertices.
    }
}
