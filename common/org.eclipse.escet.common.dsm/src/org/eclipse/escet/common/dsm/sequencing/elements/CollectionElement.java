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

package org.eclipse.escet.common.dsm.sequencing.elements;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.common.dsm.sequencing.graph.Vertex;

/** A collection of related vertices as a DSM element. */
public class CollectionElement extends Element {
    /** Cycle elements in the collection. */
    public final List<SingularElement> containedElements;

    /**
     * Constructor of the {@link CollectionElement} class.
     *
     * @param containedElements Singular elements in the collection.
     * @param inputs Inputs of the element.
     * @param outputs Outputs of the element.
     */
    public CollectionElement(List<SingularElement> containedElements, BitSet inputs, BitSet outputs) {
        super(inputs, outputs);
        this.containedElements = containedElements;
    }

    @Override
    public void setVertexIndices(BitSet indexSet) {
        for (SingularElement singular: containedElements) {
            singular.setVertexIndices(indexSet);
        }
    }

    @Override
    public void clearVertexIndices(BitSet indexSet) {
        for (SingularElement singular: containedElements) {
            singular.clearVertexIndices(indexSet);
        }
    }

    @Override
    public int getVertexCount() {
        return containedElements.size();
    }

    @Override
    public void appendVertices(List<Vertex> vertices) {
        for (SingularElement singular: containedElements) {
            singular.appendVertices(vertices);
        }
    }

    @Override
    protected String getSelfNumbers() {
        return containedElements.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
