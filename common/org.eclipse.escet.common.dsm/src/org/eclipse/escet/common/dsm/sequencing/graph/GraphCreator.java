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

/** Interface for constructing a graph. */
public interface GraphCreator {
    /** Prepare for constructing a new graph, must be the first method called in the interface. */
    void setupCreation();

    /**
     * Add a directed edge between two vertices in the graph. There can be only one edge between a given source and
     * target vertex (no parallel edges).
     *
     * @param sourceVertexName Source vertex of the edge. The name does not have to be provided to {@link #addVertex}
     *     beforehand.
     * @param targetVertexName Target vertex of the edge. The name does not have to be provided to {@link #addVertex}
     *     beforehand.
     */
    void addEdge(String sourceVertexName, String targetVertexName);

    /**
     * Add a vertex to the graph with the given name. Providing the same name multiple times is allowed and has no
     * effect, only one vertex with that name is created.
     *
     * @param vertexName Name of the vertex.
     */
    void addVertex(String vertexName);

    /** Finish and cleanup creating the graph, must be the last method called in the interface. */
    void finishCreation();
}
