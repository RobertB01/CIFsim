//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.eclipse.escet.common.dsm.sequencing.graph.Cycle;
import org.eclipse.escet.common.dsm.sequencing.graph.Edge;
import org.eclipse.escet.common.dsm.sequencing.graph.Graph;
import org.eclipse.escet.common.dsm.sequencing.graph.Vertex;
import org.eclipse.escet.common.java.DirectedGraphCycleFinder;
import org.eclipse.escet.common.java.Lists;

/** Class for finding cycles in the graph. */
public class GraphCycleFinder
        extends DirectedGraphCycleFinder<Graph, Integer, GraphCycleFinder.CycleFinderEdge, Cycle>
{
    /** Constructor of the {@link GraphCycleFinder} class. */
    public GraphCycleFinder() {
        super(null);
    }

    @Override
    protected List<Integer> getVertices(Graph graph) {
        return IntStream.range(0, graph.vertices.size()).boxed().toList();
    }

    @Override
    protected List<CycleFinderEdge> getOutgoingEdges(Graph graph, Integer vertex) {
        Vertex v = graph.vertices.get(vertex);
        return v.outputs.stream().map(CycleFinderEdge::new).toList();
    }

    @Override
    protected void addCycle(Graph g, List<CycleFinderEdge> edges, Set<Cycle> foundCycles) {
        Cycle cycle = new Cycle(edges.stream().map(e -> e.edge).collect(Lists.toList()));
        foundCycles.add(cycle);
    }

    /** Wrapper class around a graph edge to match the {@link DirectedGraphCycleFinder} class expectations. */
    public static class CycleFinderEdge extends DirectedGraphCycleFinder.GraphEdge<Integer> {
        /** The actual edge of the graph. */
        public final Edge edge;

        /**
         * Constructor of the {@link CycleFinderEdge} class.
         *
         * @param edge The actual edge of the graph.
         */
        public CycleFinderEdge(Edge edge) {
            super(edge.producingVertex, edge.consumingVertex);
            this.edge = edge;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof CycleFinderEdge cfe)) {
                return false;
            }
            return edge.equals(cfe.edge);
        }

        @Override
        public int hashCode() {
            return edge.hashCode();
        }
    }
}
