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

/** A cycle in the graph. */
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
        for (int offset = 0; offset < edges.size(); offset++) {
            if (otherCycle.edges.get(offset).producingVertex != edges.get(0).producingVertex) {
                continue;
            }

            // As these elements contain simple cycles, the same vertex will not be found at any other offset.
            // Therefore, to find a match it must happen at the found offset.

            // Avoid check-style rule about not changing the iterator variable by stupid programmers so they can have
            // the risk of using the wrong variable instead. Quite an improvement!
            int theOffset = offset;

            for (int index = 1; index < edges.size(); index++) {
                theOffset = (theOffset == edges.size() - 1) ? 0 : (theOffset + 1);
                if (otherCycle.edges.get(theOffset).producingVertex != edges.get(index).producingVertex) {
                    return false;
                }
            }
            return true;
        }
        throw new AssertionError("Vertex " + edges.get(0).producingVertex + " has mysteriously disappeared.");
    }

    @Override
    public int hashCode() {
        return vertices.hashCode();
    }
}
