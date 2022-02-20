//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import java.util.Iterator;

/** Class to iterate over incoming edges of a location. */
public class IncomingEdgeIterator implements Iterator<Edge>, Iterable<Edge> {
    /** Event to match in the search, or {@code null} to return every edge. */
    public final Event event;

    /**
     * If not {@code null}, edge to return on the next call to {@link #next}. If {@code null}, all edges have been
     * processed but the iterator code has not yet been notified of this fact.
     */
    private Edge edge;

    /**
     * Constructor of the {@link IncomingEdgeIterator} class.
     *
     * @param loc Location with the incoming edges.
     * @param evt Event to select edges, or {@code null} to return every edge.
     */
    public IncomingEdgeIterator(Location loc, Event evt) {
        event = evt;
        edge = loc.incomingEdges;
        while (true) {
            if (event == null || edge == null || edge.event == event) {
                break;
            }
            edge = edge.nextIncoming;
        }
    }

    @Override
    public Edge next() {
        Edge e = edge;
        while (true) {
            edge = edge.nextIncoming;
            if (event == null || edge == null || edge.event == event) {
                break;
            }
        }
        return e;
    }

    @Override
    public boolean hasNext() {
        return edge != null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Edge> iterator() {
        return this;
    }
}
