//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.checks.finiteresponse;

import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifSortUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.controllercheck.checks.ControllerCheckerMddBasedCheck;
import org.eclipse.escet.cif.controllercheck.mdd.CifMddSpec;
import org.eclipse.escet.cif.controllercheck.mdd.MddSpecBuilder;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.java.output.DebugNormalOutput;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;
import org.eclipse.escet.common.multivaluetrees.VarInfo;

/** Class for checking a CIF specification has finite response. */
public class FiniteResponseCheck extends ControllerCheckerMddBasedCheck<FiniteResponseCheckConclusion> {
    /** The name of the property being checked. */
    public static final String PROPERTY_NAME = "finite response";

    /**
     * Whether to print the events that appear in finite response control loops as part of printing the check
     * conclusion.
     */
    private final boolean printControlLoops;

    /**
     * Constructor for the {@link FiniteResponseCheck} class.
     *
     * @param printControlLoops Whether to print the events that appear in finite response control loops as part of
     *     printing the check conclusion.
     */
    public FiniteResponseCheck(boolean printControlLoops) {
        this.printControlLoops = printControlLoops;
    }

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
    private MddSpecBuilder builder;

    @Override
    public String getPropertyName() {
        return PROPERTY_NAME;
    }

    @Override
    public FiniteResponseCheckConclusion performCheck(CifMddSpec cifMddSpec) {
        DebugNormalOutput dbg = cifMddSpec.getDebugOutput();

        // Get information from MDD specification.
        List<Automaton> automata = cifMddSpec.getAutomata();
        Assert.check(!automata.isEmpty());
        controllableEvents = copy(cifMddSpec.getControllableEvents());

        // If no controllable events, then finite response trivially holds.
        if (controllableEvents.isEmpty()) {
            dbg.line("No controllable events. Finite response trivially holds.");
            return new FiniteResponseCheckConclusion(List.of(), printControlLoops);
        }

        // Get additional information from MDD specification.
        Termination termination = cifMddSpec.getTermination();
        controllableEventsChanged = true;
        eventVarUpdate = cifMddSpec.getUpdatedVariablesByEvent();
        nonCtrlIndependentVarsInfos = null;
        globalGuardsByEvent = cifMddSpec.getGlobalGuardsByEvent();
        builder = cifMddSpec.getBuilder();

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
        if (termination.isRequested()) {
            return null;
        }

        // Check all automata for controllable-event loops. If an automata has a controllable event in its alphabet, but
        // not in any of its potential controllable loops, then this event is removed from the controllable-event set.
        // We keep repeating the search until the controllable-event set is not updated anymore or the set is empty.
        int oldSize;
        int iterationNumber = 1;

        do {
            if (iterationNumber > 1) {
                dbg.line();
            }
            dbg.line("Iteration %d.", iterationNumber);
            iterationNumber++;
            oldSize = controllableEvents.size();

            dbg.inc();
            for (Automaton aut: automata) {
                checkAutomaton(aut, termination, dbg);

                if (termination.isRequested()) {
                    dbg.dec();
                    return null;
                }
            }
            dbg.dec();
        } while (oldSize != controllableEvents.size() && !controllableEvents.isEmpty());

        // Construct the conclusion.
        List<Event> orderedEvents = set2list(controllableEvents);
        CifSortUtils.sortCifObjects(orderedEvents);
        return new FiniteResponseCheckConclusion(orderedEvents, printControlLoops);
    }

    /**
     * Checks an automaton on the existence of potential controllable-event loops, i.e., loops in the automaton that are
     * not controllable unconnectable. This function removes events from {@link #controllableEvents} if they occur in
     * the alphabet of the automaton, but not in any potential controllable-event loop.
     *
     * @param aut The automaton to check for potential controllable-event loops.
     * @param termination Cooperative termination query function.
     * @param dbg Callback to send debug output to the user.
     */
    private void checkAutomaton(Automaton aut, Termination termination, DebugNormalOutput dbg) {
        // Check if the automaton has any controllable events in its alphabet.
        if (Sets.isEmptyIntersection(CifEventUtils.getAlphabet(aut), controllableEvents)) {
            return;
        }

        // Find the controllable-event loops in the automata. Here we ignore guards and updates, only use location,
        // edges, and events.
        Set<EventLoop> controllableEventLoops = EventLoopSearch.searchEventLoops(aut, controllableEvents, termination);
        if (termination.isRequested()) {
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
                    VarInfo varInfo = builder.cifVarInfoBuilder.getVarInfo(var, CifMddSpec.READ_INDEX);
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

        if (termination.isRequested()) {
            return;
        }

        // Collect which events occur in potential controllable-event loops.
        Set<Event> eventsInPotentialControllableLoops = set();

        // Print output if controllable-event loops were found.
        if (!controllableEventLoops.isEmpty()) {
            dbg.line("The following events have been encountered in a controllable-event loop of automaton %s:",
                    CifTextUtils.getAbsName(aut));
            dbg.inc();

            // Check whether the loop is controllable unconnectable. If it is not, it is a potential controllable-event
            // loop in the system. Print the result.
            for (EventLoop controllableEventLoop: controllableEventLoops) {
                if (isUnconnectable(controllableEventLoop, nonCtrlIndependentVarsInfos, termination)) {
                    dbg.line("%s, which is controllable unconnectable.", controllableEventLoop.toString());
                } else {
                    dbg.line("%s, which is not controllable unconnectable.", controllableEventLoop.toString());
                    eventsInPotentialControllableLoops.addAll(controllableEventLoop.events);
                }

                if (termination.isRequested()) {
                    dbg.dec();
                    return;
                }
            }
            dbg.dec();
        }

        // Determine which events are in the alphabet of the automaton, but not in any of its potential
        // controllable-event loops.
        Set<Event> eventsInAlphabetNotInLoop = Sets.difference(CifEventUtils.getAlphabet(aut),
                eventsInPotentialControllableLoops);

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
     * @param termination Cooperative termination query function.
     * @return {@code true} if the loop is controllable unconnectable, {@code false} otherwise.
     */
    private boolean isUnconnectable(EventLoop controllableEventLoop, VarInfo[] nonCtrlIndependentVarsInfos,
            Termination termination)
    {
        Node n = Tree.ONE;
        for (Event evt: controllableEventLoop.events) {
            Node g = globalGuardsByEvent.get(evt);
            Node gAbstract = builder.tree.variableAbstractions(g, nonCtrlIndependentVarsInfos);
            n = builder.tree.conjunct(n, gAbstract);
            if (n == Tree.ZERO) {
                return true;
            }
            if (termination.isRequested()) {
                return false;
            }
        }
        return false;
    }
}
