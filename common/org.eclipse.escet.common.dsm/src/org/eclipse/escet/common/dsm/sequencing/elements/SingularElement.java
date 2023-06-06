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

package org.eclipse.escet.common.dsm.sequencing.elements;

import java.util.BitSet;
import java.util.List;

import org.eclipse.escet.common.dsm.sequencing.graph.Vertex;

/** An single DSM element. */
public class SingularElement extends Element {
    /** Vertex of this element. */
    public final Vertex vertex;

    /**
     * Constructor of the {@link Element}.
     *
     * @param vertex Vertex represented by the element.
     * @param inputs Inputs of the element.
     * @param outputs Outputs of the element.
     */
    public SingularElement(Vertex vertex, BitSet inputs, BitSet outputs) {
        super(inputs, outputs);
        this.vertex = vertex;
    }

    @Override
    public void setVertexIndices(BitSet indexSet) {
        indexSet.set(vertex.number);
    }

    @Override
    public void clearVertexIndices(BitSet indexSet) {
        indexSet.clear(vertex.number);
    }

    @Override
    public int getVertexCount() {
        return 1;
    }

    @Override
    public void appendVertices(List<Vertex> vertices) {
        vertices.add(vertex);
    }

    @Override
    protected String getSelfNumbers() {
        return String.valueOf(vertex.number);
    }
}
