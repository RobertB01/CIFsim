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

import java.util.List;

/** Vertex in a graph. */
public class Vertex {
    /** Index number of the vertex in {@link Graph#vertices}. */
    public final int number;

    /** Name of the vertex. */
    public final String name;

    /** Edges from producing vertices to the vertex. */
    public final List<Edge> inputs;

    /** Edges from the vertex to consuming vertices. */
    public final List<Edge> outputs;

    /**
     * Constructor of the {@link Vertex} class.
     *
     * @param number Number of the vertex.
     * @param name Name of the vertex.
     * @param inputs Edges from producing vertices to the vertex.
     * @param outputs Edges from the vertex to consuming vertices.
     */
    public Vertex(int number, String name, List<Edge> inputs, List<Edge> outputs) {
        this.number = number;
        this.name = name;
        this.inputs = inputs;
        this.outputs = outputs;
    }
}
