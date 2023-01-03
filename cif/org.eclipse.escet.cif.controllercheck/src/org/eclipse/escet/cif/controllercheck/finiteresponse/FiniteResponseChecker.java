//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.finiteresponse;

import static org.eclipse.escet.cif.common.CifEventUtils.getAlphabet;
import static org.eclipse.escet.cif.common.CifSortUtils.sortCifObjects;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.controllercheck.PrepareChecks.READ_INDEX;
import static org.eclipse.escet.cif.controllercheck.finiteresponse.EventLoopSearch.searchEventLoops;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.controllercheck.CheckConclusion;
import org.eclipse.escet.cif.controllercheck.PrepareChecks;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.MvSpecBuilder;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.AppEnvData;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;
import org.eclipse.escet.common.multivaluetrees.VarInfo;

/** Class for checking a CIF specification has finite response. */
public class FiniteResponseChecker {
    /** The application context to use. */
    private final AppEnvData env = AppEnv.getData();

    /**
     * The controllable event set. Iteratively, this set is updated. If an event is found in the alphabet of an
     * automaton, but not in any of its potential controllable-event loops, it is removed from this set.
     */
    private Set<Event> controllableEvents;

    /**
     * Whether the controllable events have changed after the last computation of the controllable independent
     * variables.
     */
    private boolean controllableEventsChanged;

    /** Mapping between events and the variables updated by edges labeled with that event. */
    private Map<Event, Set<Declaration>> eventVarUpdate;

    /**
     * Discrete variables that are not controllable independent, i.e., their value can be updated by an edge labeled
     * with a controllable event.
     */
    private VarInfo[] nonCtrlIndependentVarsInfos;

    /** Global guard for each event. */
    private Map<Event, Node> globalGuardsByEvent;

    /** Builder for the MDD tree. */
    private MvSpecBuilder builder;

    /**
     * Performs the finite response check for a CIF specification.
     *
     * @param prepareChecks Collected CIF information to perform the finite response check.
     * @return {@code null} when the check is aborted, else the conclusion of the finite response check.
     */
    public CheckConclusion checkSystem(PrepareChecks prepareChecks) {
        List<Automaton> automata = prepareChecks.getAutomata();
        controllableEvents = copy(prepareChecks.getControllableEvents());
        if (automata.isEmpty() || controllableEvents.isEmpty()) {
            return new FiniteResponseCheckConclusion(List.of());
        }

        controllableEventsChanged = true;
        eventVarUpdate = prepareChecks.getUpdatedVariablesByEvent();
        nonCtrlIndependentVarsInfos = null;
        globalGuardsByEvent = prepareChecks.getGlobalGuardsByEvent();
        builder = prepareChecks.getBuilder();

        // Remove controllable events that are always disabled.
        Iterator<Event> evtIterator = controllableEvents.iterator();
        Event evt;
        while (evtIterator.hasNext()) {
            evt = evtIterator.next();
            Node n = globalGuardsByEvent.get(evt);
            Assert.notNull(n);

            if (n == Tree.ZERO) {
                evtIterator.remove();
            }
        }
        if (env.isTerminationRequested()) {
            return null;
        }

        // Check all automata for controllable-event loops. If an automata has a controllable event in its alphabet, but
        // not in any of its potential controllable loops, then this event is removed from the controllable-event set.
        // We keep repeating the search until the controllable-event set is not updated anymore or the set is empty.
        int oldSize;
        int iterationNumber = 1;

        do {
            dbg();
            dbg("Iteration %d.", iterationNumber);
            iterationNumber++;
            oldSize = controllableEvents.size();

            idbg();
            for (Automaton aut: automata) {
                checkAutomaton(aut);

                if (env.isTerminationRequested()) {
                    ddbg();
                    return null;
                }
            }
            ddbg();
        } while (oldSize != controllableEvents.size() && !controllableEvents.isEmpty());

        // Construct the conclusion.
        List<Event> orderedEvents = set2list(controllableEvents);
        sortCifObjects(orderedEvents);
        return new FiniteResponseCheckConclusion(orderedEvents);
    }

    /**
     * Checks an automaton on the existence of potential controllable-event loops, i.e., loops in the automaton that are
     * not controllable unconnectable. This function removes events from {@link #controllableEvents} if they occur in
     * the alphabet of the automaton, but not in any potential controllable-event loop.
     *
     * @param aut The automaton to check for potential controllable-event loops.
     */
    private void checkAutomaton(Automaton aut) {
        // Check if the automaton has any controllable events in its alphabet.
        if (isEmptyIntersection(getAlphabet(aut), controllableEvents)) {
            return;
        }

        // Find the controllable-event loops in the automata. Here we ignore guards and updates, only use location,
        // edges, and events.
        Set<EventLoop> controllableEventLoops = searchEventLoops(aut, controllableEvents, env);
        if (env.isTerminationRequested()) {
            return;
        }

        // Calculate the non-controllable independent variables. That are the variables that can be updated by
        // controllable events. We later have to abstract from these in the global guards. Variables are cached, only
        // calculate when the controllable event set has changed.
        if (controllableEventsChanged) {
            controllableEventsChanged = false;

            BitSet bits = new BitSet(builder.cifVarInfoBuilder.varInfos.size());
            for (Event evt: controllableEvents) {
                for (Declaration var: eventVarUpdate.getOrDefault(evt, set())) {
                    VarInfo varInfo = builder.cifVarInfoBuilder.getVarInfo(var, READ_INDEX);
                    bits.set(varInfo.level);
                }
            }

            nonCtrlIndependentVarsInfos = new VarInfo[bits.cardinality()];
            int nextFree = 0;
            for (int level = bits.nextSetBit(0); level >= 0; level = bits.nextSetBit(level + 1)) {
                nonCtrlIndependentVarsInfos[nextFree] = builder.cifVarInfoBuilder.varInfos.get(level);
                nextFree++;
            }
        }

        if (env.isTerminationRequested()) {
            return;
        }

        // Collect which events occur in potential controllable-event loops.
        Set<Event> eventsInPotentialControllableLoops = set();

        // Print output if controllable-event loops were found.
        if (!controllableEventLoops.isEmpty()) {
            dbg("The following events have been encountered in a controllable-event loop of automaton %s:",
                    getAbsName(aut));
            idbg();

            // Check whether the loop is controllable unconnectable. If it is not, it is a potential controllable-event
            // loop in the system. Print the result.
            for (EventLoop controllableEventLoop: controllableEventLoops) {
                if (isUnconnectable(controllableEventLoop, nonCtrlIndependentVarsInfos)) {
                    dbg("%s, which is controllable unconnectable.", controllableEventLoop.toString());
                } else {
                    dbg("%s, which is not controllable unconnectable.", controllableEventLoop.toString());
                    eventsInPotentialControllableLoops.addAll(controllableEventLoop.events);
                }

                if (env.isTerminationRequested()) {
                    ddbg();
                    return;
                }
            }
            ddbg();
        }

        // Determine which events are in the alphabet of the automaton, but not in any of its potential
        // controllable-event loops.
        Set<Event> eventsInAlphabetNotInLoop = Sets.difference(getAlphabet(aut), eventsInPotentialControllableLoops);

        // If there are controllable events that are in the alphabet of the automaton, but not in any of its potential
        // controllable-event loops, these events cannot occur in any controllable-event loops of other automata. Remove
        // these events from the controllable event set.
        controllableEventsChanged = controllableEvents.removeAll(eventsInAlphabetNotInLoop);
    }

    /**
     * Checks whether the controllable-event loop is controllable unconnectable. Controllable unconnectable is checked
     * after abstracting from the events that might change their value due to the updates of other controllable events.
     *
     * @param controllableEventLoop The loop to check to be controllable unconnectable.
     * @param nonCtrlIndependentVarsInfos The variables that are updated by controllable events, not controllable
     *     independent variables.
     * @return {@code true} if the loop is controllable unconnectable, {@code false} otherwise.
     */
    private boolean isUnconnectable(EventLoop controllableEventLoop, VarInfo[] nonCtrlIndependentVarsInfos) {
        Node n = Tree.ONE;
        for (Event evt: controllableEventLoop.events) {
            Node g = globalGuardsByEvent.get(evt);
            Node gAbstract = builder.tree.variableAbstractions(g, nonCtrlIndependentVarsInfos);
            n = builder.tree.conjunct(n, gAbstract);
            if (n == Tree.ZERO) {
                return true;
            }
        }
        return false;
    }
}
