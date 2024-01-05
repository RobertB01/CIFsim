//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.automata;

/** Edge of an automaton. */
public class Edge {
    /** Originating location of the edge. */
    public final Location srcLoc;

    /** Destination location of the edge. */
    public final Location dstLoc;

    /** Event associated with this edge. */
    public final Event event;

    /**
     * Next outgoing edge for the {@link #srcLoc}. Is {@code null} if this edge is the last outgoing edge of
     * {@link #srcLoc}.
     */
    public Edge nextOutgoing;

    /**
     * Next incoming edge for the {@link #dstLoc}. Is {@code null} if this edge is the last incoming edge of
     * {@link #dstLoc}.
     */
    public Edge nextIncoming;

    /**
     * Constructor of the {@link Edge} class.
     *
     * <p>
     * Made private to avoid false positives about unused objects (that is, {@code new Edge(e, src, dst);} gives a false
     * positive on an object created but not used). Use {@link #addEdge} instead.
     * </p>
     *
     * @param event Event of the edge.
     * @param srcLoc Source location of the edge.
     * @param dstLoc Destination location of the edge.
     */
    private Edge(Event event, Location srcLoc, Location dstLoc) {
        this.event = event;
        this.srcLoc = srcLoc;
        this.dstLoc = dstLoc;

        srcLoc.addOutgoingEdge(this);
        dstLoc.addIncomingEdge(this);
    }

    /** Remove the edge from the automaton. */
    public void remove() {
        this.srcLoc.removeOutgoingEdge(this);
        this.dstLoc.removeIncomingEdge(this);
    }

    /**
     * Create an edge between two locations.
     *
     * @param event Event of the edge.
     * @param srcLoc Source location of the edge.
     * @param dstLoc Destination location of the edge.
     * @return The created edge. It is unlikely you ever need it, but it avoids false "unused object" warnings.
     */
    public static Edge addEdge(Event event, Location srcLoc, Location dstLoc) {
        return new Edge(event, srcLoc, dstLoc);
    }
}
