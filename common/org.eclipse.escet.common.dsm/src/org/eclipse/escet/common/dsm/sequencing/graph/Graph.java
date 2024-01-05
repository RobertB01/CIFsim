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

package org.eclipse.escet.common.dsm.sequencing.graph;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;

/**
 * Graph with vertices and edges.
 *
 * <p>
 * Vertices are numbered and stored in a list indexed on that number for an easy and fast mapping between numbers and
 * vertices. They have a list of input and output edges. Note that output of a vertex is input for the next vertex so
 * edge information is essentially duplicated for easier reasoning.
 * </p>
 *
 * <p>
 * Edges use vertex numbers and have a {@link Edge#teared} property that is used for breaking cycles. For this reason it
 * is essential that an edge between two vertices exists exactly once, and is shared between the connected vertices.
 * This simplifies tearing of edges.
 * </p>
 */
public class Graph implements GraphCreator {
    /** Vertices of the graph. */
    public final List<Vertex> vertices = list();

    /** Map for referring to vertices by name. Exists only while the graph is being created. */
    private Map<String, Integer> vertexNames;

    /** Created edges by source and target vertex numbers. Exists only while the graph is being created. */
    private Map<Integer, Map<Integer, Edge>> edgeMap;

    /**
     * Get the object for creating the graph.
     *
     * @return The object that creates the graph.
     */
    public GraphCreator getGraphCreator() {
        return this;
    }

    /**
     * Convert the stored graph to text, for debugging and analysis.
     *
     * @return The stored graph in textual form.
     */
    public String dumpGraph() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ");
        sb.append(vertices.size());
        sb.append(" vertices.\n");
        for (Vertex vertex: vertices) {
            sb.append("Vertex \"");
            sb.append(vertex.name);
            sb.append("\":");
            if (vertex.outputs.isEmpty()) {
                sb.append(" No outputs.\n");
            } else {
                boolean first = true;
                for (Edge output: vertex.outputs) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(",");
                    }

                    if (output.teared) {
                        sb.append(" TEARED -> ");
                    } else {
                        sb.append(" -> ");
                    }
                    sb.append("\"" + vertices.get(output.consumingVertex).name + "\"");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public void setupCreation() {
        vertexNames = map();
        edgeMap = map();
        vertices.clear();
    }

    @Override
    public void addEdge(String sourceVertexName, String targetVertexName) {
        Vertex sourceVertex = getVertexByName(sourceVertexName);
        Vertex targetVertex = getVertexByName(targetVertexName);

        // Find or create the edge.
        Map<Integer, Edge> targetMap = edgeMap.computeIfAbsent(sourceVertex.number, srcVertex -> map());
        Edge edge = targetMap.computeIfAbsent(targetVertex.number,
                destVertex -> new Edge(sourceVertex.number, targetVertex.number));

        // Hook the new edge into the graph.
        sourceVertex.outputs.add(edge);
        targetVertex.inputs.add(edge);
    }

    @Override
    public void addVertex(String vertexName) {
        getVertexByName(vertexName);
    }

    /**
     * Find or create a vertex in the graph by the given name. Should only be used while creating a graph.
     *
     * @param vertexName Name of the vertex to find or create.
     * @return The found or created vertex.
     */
    private Vertex getVertexByName(String vertexName) {
        Integer vertNumber = vertexNames.get(vertexName);
        if (vertNumber != null) {
            return vertices.get(vertNumber);
        }
        int number = vertices.size();
        vertexNames.put(vertexName, number);

        Vertex vertex = new Vertex(number, vertexName, list(), list());
        vertices.add(vertex);
        return vertex;
    }

    @Override
    public void finishCreation() {
        vertexNames = null;
        edgeMap = null;
    }
}
