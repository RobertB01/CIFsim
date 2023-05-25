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

import org.eclipse.escet.common.java.Assert;

/** Directed connection between two vertices. */
public class Edge {
    /** The vertex number of the originating vertex of the edge. */
    public final int producingVertex;

    /** The vertex number of the destination vertex of the edge. */
    public final int consumingVertex;

    /** Whether this edge is valid for topological ordering. */
    public boolean teared = false;

    /**
     * Constructor of the {@link Edge} class.
     *
     * @param producingVertex The vertex number of the originating vertex of the edge.
     * @param consumingVertex The vertex number of the destination vertex of the edge.
     */
    public Edge(int producingVertex, int consumingVertex) {
        this.producingVertex = producingVertex;
        this.consumingVertex = consumingVertex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // As a particular edge should exist exactly once, an equal edge should not happen.
        if (other instanceof Edge edge) {
            Assert.check(producingVertex != edge.producingVertex || consumingVertex != edge.consumingVertex);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return producingVertex + 2048 * consumingVertex;
    }
}
