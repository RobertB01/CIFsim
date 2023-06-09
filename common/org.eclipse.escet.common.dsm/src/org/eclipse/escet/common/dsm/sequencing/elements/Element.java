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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.common.dsm.sequencing.graph.Vertex;

/** An element in a DSM. */
public abstract class Element {
    /** Inputs of the element. */
    public final BitSet inputs;

    /** Outputs of the element. */
    public final BitSet outputs;

    /**
     * Constructor of the {@link Element} class.
     *
     * @param inputs Inputs of the element.
     * @param outputs Outputs of the element.
     */
    protected Element(BitSet inputs, BitSet outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    /**
     * Check whether this element uses at least one of the elements in the given set for input.
     *
     * @param elementSet Mask set with elements that should be checked for being used for input for this element.
     * @return Whether at least one of the elements in the given element set is used for input by this element.
     */
    public boolean hasInputDeps(BitSet elementSet) {
        return inputs.intersects(elementSet);
    }

    /**
     * Check whether this element uses at least one of the elements in the given set for output.
     *
     * @param elementSet Mask set with elements that should be checked for being used for output for this element.
     * @return Whether at least one of the elements in the given element set is used for output by this element.
     */
    public boolean hasOutputDeps(BitSet elementSet) {
        return outputs.intersects(elementSet);
    }

    /**
     * Set the indices in {@code indexSet} of the vertices in the element.
     *
     * @param indexSet Storage of the added indices.
     */
    public abstract void setVertexIndices(BitSet indexSet);

    /**
     * Clear the indices in {@code indexSet} of the vertices in the element.
     *
     * @param indexSet Storage of the added indices.
     */
    public abstract void clearVertexIndices(BitSet indexSet);

    /**
     * Retrieve the number of vertices in the element.
     *
     * @return The number of vertices in the element.
     */
    public abstract int getVertexCount();

    /**
     * Add the vertices of the element sequentially into the given destination.
     *
     * @param vertices Destination to append the vertices to.
     */
    public abstract void appendVertices(List<Vertex> vertices);

    /**
     * Get a string describing content of this element.
     *
     * @return The string describing content of this element.
     */
    protected abstract String getSelfNumbers();

    @Override
    public String toString() {
        String inputText = inputs.stream().mapToObj(String::valueOf).collect(Collectors.joining(","));
        String outputText = outputs.stream().mapToObj(String::valueOf).collect(Collectors.joining(","));
        return fmt("<<Element %s: [%s] -> [%s]>>", getSelfNumbers(), inputText, outputText);
    }
}
