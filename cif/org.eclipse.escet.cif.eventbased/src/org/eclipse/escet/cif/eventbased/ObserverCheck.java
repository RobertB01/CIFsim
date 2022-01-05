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

package org.eclipse.escet.cif.eventbased;

import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.partitions.PartitionLocation;
import org.eclipse.escet.cif.eventbased.partitions.PartitionRefinement;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/**
 * Check whether an automaton is a proper L-observer.
 *
 * <p>
 * Σ is the set of events, Σ' is the set of observable events. Automaton G = (X, Σ, ξ, x<sub>0</sub>, X<sub>m</sub>) is
 * said to be an L-observer. The question is whether G is a proper L-observer for projection P.
 * </p>
 *
 * <p>
 * To decide, split the states X of G into a partition set V<sub>0</sub>. With U={x∊X | x∊X<sub>M</sub> ⋁
 * (∃u∊(Σ-Σ')<sup>*</sup>) ξ(x,u)∩X<sub>M</sub>≠∅}, the initial partitions of states of automaton G is V<sub>0</sub> =
 * {U, X-U}. (U is the set of marker states, and states backward reachable from the marker states by performing
 * invisible events only, X-U are all other states of automaton G).
 * </p>
 *
 * <p>
 * The observer check is to compute the set of events A = {σ∊Σ | (∃S,T∊Ω(Σ', V<sub>0</sub>): S≠T) (t∊T) (s∊S) s=ξ(t,σ)},
 * that is, the set of events where you move between two different partitions. Automaton G is an observer for observable
 * events Σ' when A⊆Σ'. (In words, when you can only move between partitions with a visible event.)
 * </p>
 *
 * <p>
 * The implementation checks the opposite, namely whether you can move to a different partition with an unobservable
 * event (this causes the condition A⊆Σ' to fail, and proofs directly that the property fails to hold).
 * </p>
 *
 * <p>
 * Computing Ω(Σ', V<sub>0</sub>) is done by starting with the power set V = V<sub>0</sub> (with a set of states that
 * can reach a marker state with unobservable events, and a set of states that cannot reach any marker state by doing
 * only unobservable events). Then, iteratively compute f(U, σ) for every partition U∊V and observable event σ∊Σ'. If a
 * set in V partly overlaps with a result from f(U, σ), split that set in two (namely U∩f(U,σ), and U-f(U,σ)). For the
 * new sets, also compute f(U, σ) for every observable event σ. When fixed point is reached (that is, no partly
 * overlapping of partitions in V occurs any more with function results from f(U,σ)), partitions Ω(Σ', V<sub>0</sub>)
 * have been computed. The observer check is then to check whether you can move to a different partition with an
 * unobservable event (which causes the condition A⊆Σ' to fail).
 * </p>
 */
public class ObserverCheck extends PartitionRefinement {
    /**
     * Constructor of the {@link ObserverCheck} class.
     *
     * @param observableEvents The observable events.
     * @param nonObservableEvents The non-observable events.
     * @param partitions Initial partitioning of locations.
     */
    private ObserverCheck(Set<Event> observableEvents, Set<Event> nonObservableEvents, Set<Set<Location>> partitions) {
        super(observableEvents, nonObservableEvents, partitions);
    }

    /**
     * Compute which events break the observer property from the current partitioning.
     *
     * @return The collection non-observable events crossing a partition border.
     */
    public Set<Event> getBadEvents() {
        Set<Event> badEvents = set();
        for (PartitionLocation pl: locationMapping.values()) {
            Location loc = pl.loc;
            for (Edge e: loc.getOutgoing()) {
                if (!nonObservableEvents.contains(e.event)) {
                    continue;
                }
                if (locationMapping.get(e.dstLoc).partition == pl.partition) {
                    continue;
                }
                badEvents.add(e.event);
            }
        }
        return badEvents;
    }

    /**
     * Perform observer check calculation for the given automaton and set of observable events.
     *
     * @param aut Observer automaton G.
     * @param observables Set of event that can be observed.
     * @return Set of bad events (non-observable events that cross partition borders).
     */
    public static Set<Event> doObserverCheck(Automaton aut, Set<Event> observables) {
        Set<Event> nonObservables = set();
        for (Event evt: aut.alphabet) {
            if (!observables.contains(evt)) {
                nonObservables.add(evt);
            }
        }
        if (nonObservables.isEmpty()) {
            String msg = "Non-observable event set is empty, observer check always holds.";
            OutputProvider.warn(msg);
            return set();
        }

        // This check is robust against adding observable events that are
        // not in the alphabet of 'aut'.
        if (nonObservables.size() == aut.alphabet.size()) {
            // The are corner cases where the property holds, but it
            // seems unlikely that these cases are of interest to a user.
            String msg = "Observable event set is empty.";
            throw new InvalidInputException(msg);
        }

        Set<Location> marked = set();
        Queue<Location> notDone = new ArrayDeque<>(100);
        int size = 0;
        for (Location loc = aut.locations; loc != null; loc = loc.nextLoc) {
            if (loc.marked) {
                marked.add(loc);
                notDone.add(loc);
            }
            size++;
        }
        AutomatonHelper.expandStatesBackward(marked, notDone, nonObservables);
        if (marked.isEmpty()) {
            String msg = "No marked locations found (cannot create initial partitioning).";
            throw new InvalidInputException(msg);
        }
        Set<Location> unmarked = setc(size - marked.size());
        for (Location loc = aut.locations; loc != null; loc = loc.nextLoc) {
            if (!marked.contains(loc)) {
                unmarked.add(loc);
            }
        }
        if (unmarked.isEmpty()) {
            String msg = "No unmarked locations found (all locations can reach a marker state through a path with "
                    + "non-observable events).";
            throw new InvalidInputException(msg);
        }

        Set<Set<Location>> partitions = setc(2);
        partitions.add(marked);
        partitions.add(unmarked);
        ObserverCheck oc = new ObserverCheck(observables, nonObservables, partitions);
        oc.refinePartitions();
        return oc.getBadEvents();
    }
}
