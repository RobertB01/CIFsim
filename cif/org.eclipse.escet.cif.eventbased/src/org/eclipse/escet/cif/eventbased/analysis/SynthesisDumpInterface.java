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

package org.eclipse.escet.cif.eventbased.analysis;

import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.builders.State;

/** Dumper of what edges and states get removed during synthesis. */
public interface SynthesisDumpInterface {
    /**
     * Whether the object is faking to create a synthesis dump file.
     *
     * <p>
     * Call is intended to detect fake back-ends, so generation calls can be avoided, even though they are very cheap
     * with a fake back-end.
     * </p>
     *
     * @return {@code true} if no file is being created, else {@code false}.
     */
    public abstract boolean isFake();

    /** Close the output stream. */
    public abstract void close();

    /**
     * Store information about the automata in the synthesis.
     *
     * @param auts Automata being synthesized.
     * @param numPlants Number of plant automata in the synthesis, stored as the first part of the list. The second part
     *     contains the requirement automata.
     */
    public abstract void storeAutomata(List<Automaton> auts, int numPlants);

    /**
     * Register a new location that represents the given state.
     *
     * @param state State represented by the new location.
     * @param loc The new location.
     */
    public abstract void newLocation(State state, Location loc);

    /**
     * Register a new edge.
     *
     * @param event Event of the new edge.
     * @param srcLoc Start location of the new edge.
     * @param dstLoc Destination location of the new edge.
     */
    public abstract void newEdge(Event event, Location srcLoc, Location dstLoc);

    /**
     * Outgoing edge is disabled from a location due to the synchronous product.
     *
     * @param loc Location that does not get an edge with the given event.
     * @param evt Event at the removed edge.
     * @param autIndex Index of the automaton that caused removal.
     */
    public abstract void disabledEvent(Location loc, Event evt, int autIndex);

    /**
     * An edge of a location was removed due to removal of the destination state.
     *
     * <p>
     * If the event is uncontrollable, the removal causes the location to become bad.
     * </p>
     *
     * @param loc Location with the removed edge.
     * @param evt Event at the removed edge.
     * @param dst Destination location of the edge.
     */
    public abstract void removedDestination(Location loc, Event evt, Location dst);

    /**
     * Non-marked location has no outgoing edges any more, and has thus become non-coreachable.
     *
     * <p>
     * As a result, the location becomes bad.
     * </p>
     *
     * @param loc Location that lost all its outgoing edges.
     */
    public abstract void blockingLocation(Location loc);

    /**
     * The location is not coreachable.
     *
     * @param loc Location that is not coreachable.
     */
    public abstract void nonCoreachableLocation(Location loc);

    /**
     * The location is not reachable.
     *
     * @param loc Location that is not reachable.
     */
    public abstract void nonReachableLocation(Location loc);
}
