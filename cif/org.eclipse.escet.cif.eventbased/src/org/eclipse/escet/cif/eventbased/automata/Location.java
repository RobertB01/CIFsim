//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.origin.Origin;
import org.eclipse.escet.common.java.Assert;

/** Location of an automaton. */
public class Location {
    /** Incoming edges, {@code null} if no incoming edges at this location. */
    public Edge incomingEdges = null;

    /** Outgoing edges, {@code null} if no outgoing edges at this location. */
    public Edge outgoingEdges = null;

    /** Reference to the previous location of the automaton, {@code null} if this is the first location. */
    public Location prevLoc = null;

    /** Reference to the next location of the automaton, {@code null} if this is the last location. */
    public Location nextLoc = null;

    /** Is the location a marked location? */
    public boolean marked = false;

    /** Origin of the location. May be {@code null} denoting lack of a user-readable location name. */
    public final Origin origin;

    /**
     * Constructor of the {@link Location} class.
     *
     * @param aut Automaton owning the location.
     * @param origin Origin of the location. May be {@code null} if origin is not used.
     */
    public Location(Automaton aut, Origin origin) {
        this.origin = origin;
        aut.registerLocation(this);
    }

    /**
     * Remove this location from the locations of its automaton.
     *
     * <p>
     * The location should not be the initial location of the automaton.
     * </p>
     *
     * <p>
     * Do not call directly, use {@link Automaton#removeLocation} instead.
     * </p>
     */
    public void removeInternal() {
        removeAllIncomingEdges();
        removeAllOutgoingEdges();
        Assert.check(incomingEdges == null);
        Assert.check(outgoingEdges == null);
        if (prevLoc != null) {
            prevLoc.nextLoc = nextLoc;
        }
        if (nextLoc != null) {
            nextLoc.prevLoc = prevLoc;
        }
    }

    /**
     * Add an outgoing edge.
     *
     * @param edge Outgoing edge to add.
     */
    public void addOutgoingEdge(Edge edge) {
        Assert.check(edge.nextOutgoing == null);
        edge.nextOutgoing = outgoingEdges;
        outgoingEdges = edge;
    }

    /**
     * Add an incoming edge.
     *
     * @param edge Incoming edge to add.
     */
    public void addIncomingEdge(Edge edge) {
        Assert.check(edge.nextIncoming == null);
        edge.nextIncoming = incomingEdges;
        incomingEdges = edge;
    }

    /**
     * Remove all outgoing edges of this location from the automaton.
     *
     * <p>
     * TODO: By using a double linked list for edge source and destination, removal performance can be improved.
     * </p>
     *
     * <p>
     * TODO: A more complicated solution could be to maintain insertion order in edge lists, which would allow traversal
     * of the outgoing edge list to find the next incoming edge at a location to remove.
     * </p>
     */
    public void removeAllOutgoingEdges() {
        // Construct a map with locations to update.
        Map<Location, Set<Edge>> splitEdges = map();
        Edge e = outgoingEdges;
        while (e != null) {
            Set<Edge> se = splitEdges.get(e.dstLoc);
            if (se == null) {
                se = set();
                splitEdges.put(e.dstLoc, se);
            }
            se.add(e);
            e = e.nextOutgoing;
        }
        // Update the incoming edges of each affected location.
        for (Entry<Location, Set<Edge>> locEdges: splitEdges.entrySet()) {
            locEdges.getKey().removeIncomingEdges(locEdges.getValue());
        }
        outgoingEdges = null;
    }

    /**
     * Remove all incoming edges in the set from this location.
     *
     * @param edges Edges to remove.
     */
    private void removeIncomingEdges(Set<Edge> edges) {
        Edge curEdge = incomingEdges;
        Edge newEdge = null; // Points to the last new edge.
        while (curEdge != null && !edges.isEmpty()) {
            if (!edges.remove(curEdge)) {
                // curEdge needs to stay, append it to the new edge list.
                if (newEdge == null) {
                    incomingEdges = curEdge;
                } else {
                    newEdge.nextIncoming = curEdge;
                }
                newEdge = curEdge;
            } // else the edge has to go, do nothing with curEdge
            curEdge = curEdge.nextIncoming;
        }
        // Connect remaining edges to the new list.
        if (newEdge == null) {
            incomingEdges = curEdge;
        } else {
            newEdge.nextIncoming = curEdge;
        }
    }

    /**
     * Remove all incoming edges of this location from the automaton.
     *
     * <p>
     * TODO: By using a double linked list for edge source and destination, removal performance can be improved.
     * </p>
     *
     * <p>
     * TODO: A more complicated solution could be to maintain insertion order in edge lists, which would allow traversal
     * of the incoming edge list to find the next outgoing edge at a location to remove.
     * </p>
     */
    public void removeAllIncomingEdges() {
        // Construct a map of affected locations.
        Map<Location, Set<Edge>> splitEdges = map();
        Edge e = incomingEdges;
        while (e != null) {
            Set<Edge> se = splitEdges.get(e.srcLoc);
            if (se == null) {
                se = set();
                splitEdges.put(e.srcLoc, se);
            }
            se.add(e);
            e = e.nextIncoming;
        }
        // Update the outgoing edges of each affected location.
        for (Entry<Location, Set<Edge>> locEdges: splitEdges.entrySet()) {
            locEdges.getKey().removeOutgoingEdges(locEdges.getValue());
        }
        incomingEdges = null;
    }

    /**
     * Remove all outgoing edges in the set from this location.
     *
     * @param edges Edges to remove.
     */
    private void removeOutgoingEdges(Set<Edge> edges) {
        Edge curEdge = outgoingEdges;
        Edge newEdge = null; // Points to the last new edge.
        while (curEdge != null && !edges.isEmpty()) {
            if (!edges.remove(curEdge)) {
                // curEdge needs to stay, append it to the new edge list.
                if (newEdge == null) {
                    outgoingEdges = curEdge;
                } else {
                    newEdge.nextOutgoing = curEdge;
                }
                newEdge = curEdge;
            } // else the edge has to go, do nothing with curEdge
            curEdge = curEdge.nextOutgoing;
        }
        // Connect remaining edges to the new list.
        if (newEdge == null) {
            outgoingEdges = curEdge;
        } else {
            newEdge.nextOutgoing = curEdge;
        }
    }

    /**
     * Remove an incoming edge.
     *
     * @param edge Incoming edge to remove.
     */
    public void removeIncomingEdge(Edge edge) {
        Edge prev = null;
        Edge e = incomingEdges;
        while (e != edge) {
            prev = e;
            e = e.nextIncoming; // NPE means e not in the incoming list.
        }

        if (prev == null) {
            incomingEdges = e.nextIncoming;
        } else {
            prev.nextIncoming = e.nextIncoming;
        }
        e.nextIncoming = null;
    }

    /**
     * Remove an outgoing edge.
     *
     * @param edge Outgoing edge to remove.
     */
    public void removeOutgoingEdge(Edge edge) {
        Edge prev = null;
        Edge e = outgoingEdges;
        while (e != edge) {
            prev = e;
            e = e.nextOutgoing; // NPE means e not in the outgoing list.
        }

        if (prev == null) {
            outgoingEdges = e.nextOutgoing;
        } else {
            prev.nextOutgoing = e.nextOutgoing;
        }
        e.nextOutgoing = null;
    }

    /**
     * Iterate over all outgoing edges of this location.
     *
     * @return Iterator to iterate over all outgoing edges.
     */
    public OutgoingEdgeIterator getOutgoing() {
        return getOutgoing(null);
    }

    /**
     * Iterate over all incoming edges of this location.
     *
     * @return Iterator to iterate over all incoming edges.
     */
    public IncomingEdgeIterator getIncoming() {
        return getIncoming(null);
    }

    /**
     * Iterate over all outgoing edges of this location with a given event.
     *
     * @param evt Event to select edges on.
     * @return Iterator to iterate over all outgoing edges with a given event.
     */
    public OutgoingEdgeIterator getOutgoing(Event evt) {
        return new OutgoingEdgeIterator(this, evt);
    }

    /**
     * Iterate over all incoming edges of this location with a given event.
     *
     * @param evt Event to select edges on.
     * @return Iterator to iterate over all incoming edges with a given event.
     */
    public IncomingEdgeIterator getIncoming(Event evt) {
        return new IncomingEdgeIterator(this, evt);
    }

    @Override
    public String toString() {
        return origin.toString(); // origin should not be null if we get here.
    }
}
