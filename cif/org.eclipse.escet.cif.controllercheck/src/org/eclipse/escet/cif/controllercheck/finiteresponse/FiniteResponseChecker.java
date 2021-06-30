//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.common.CifCollectUtils.collectAutomata;
import static org.eclipse.escet.cif.common.CifCollectUtils.collectDiscAndInputVariables;
import static org.eclipse.escet.cif.common.CifEventUtils.getAlphabet;
import static org.eclipse.escet.cif.common.CifEventUtils.getEvents;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifValueUtils.createConjunction;
import static org.eclipse.escet.cif.common.CifValueUtils.createDisjunction;
import static org.eclipse.escet.cif.controllercheck.finiteresponse.EventLoopSearch.searchEventLoops;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.intersection;
import static org.eclipse.escet.common.java.Sets.isEmptyIntersection;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.controllercheck.multivaluetrees.CifVarInfoBuilder;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.MvSpecBuilder;
import org.eclipse.escet.cif.controllercheck.options.PrintOutputOption;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;
import org.eclipse.escet.common.multivaluetrees.VarInfo;

/** Class for checking a CIF specification has finite response. */
public class FiniteResponseChecker {
    /** Automata of the specification. */
    private List<Automaton> automata = list();

    /** Discrete and input Variables of the specification. */
    private List<Declaration> variables = list();

    /**
     * The controllable event set. Iteratively, this set is updated. If an event is found in the alphabet of an
     * automaton, but not in any of its potential controllable-event loops, it is removed from this set.
     */
    private Set<Event> controllableEvents = set();

    /**
     * Whether the controllable events have changed after the last computation of the controllable independent
     * variables.
     */
    private boolean controllableEventsChanged = true;

    /**
     * Mapping between events and the variables updated by edges labeled with that event. IS {@code null} until
     * computed, see {@link #collectEventVarUpdate}.
     */
    private Map<Event, Set<Declaration>> eventVarUpdate;

    /**
     * Discrete variables that are not controllable independent, i.e., their value can be updated by an edge labeled
     * with a controllable event.
     */

    /**
     * Mapping between events and their global guard as a MDD node. Is {@code null} until computed, see
     * {@link #collectGlobalGuards}.
     */
    private Map<Event, Node> globalGuards;

    /** Builder for the MDD tree. */
    private MvSpecBuilder builder;

    /**
     * Performs the finite response checker for a CIF specification.
     *
     * @param spec The specification to check for finite response.
     * @return {@code true} if the specification has finite response, {@code false} if finite response cannot be
     *     guaranteed.
     */
    public boolean checkSystem(Specification spec) {
        collectAutomata(spec, automata);
        collectDiscAndInputVariables(spec, variables);
        collectControllableEvents(spec, controllableEvents);
        eventVarUpdate = collectEventVarUpdate();

        if (automata.isEmpty()) {
            OutputProvider.out("The specification has finite response.");
            OutputProvider.out("Warning: the specification contains 0 automata.");
            return true;
        }

        if (controllableEvents.isEmpty()) {
            OutputProvider.out("The specification has finite response.");
            OutputProvider.out("Warning: the specification contains 0 forcible events.");
            return true;
        }

        // Construct the MDD tree.
        final int readIndex = 0;
        final int writeIndex = 1;
        CifVarInfoBuilder cifVarInfoBuilder = new CifVarInfoBuilder(2);
        cifVarInfoBuilder.addVariablesGroupOnVariable(variables);
        builder = new MvSpecBuilder(cifVarInfoBuilder, readIndex, writeIndex);

        // Get the global guards in the tree.
        globalGuards = collectGlobalGuards(controllableEvents);

        // Check all automata for controllable-event loops. If an automata has a controllable event in its alphabet, but
        // not in any of its potential controllable loops, then this event is removed from the controllable-event set.
        // We keep repeating the search until the controllable-event set is not updated anymore or the set is empty.
        int oldSize;
        int iterationNumber = 1;

        do {
            OutputProvider.dbg(fmt("Iteration %d.", iterationNumber));
            iterationNumber++;
            oldSize = controllableEvents.size();

            for (Automaton aut: automata) {
                checkAutomaton(aut);
            }
        } while (oldSize != controllableEvents.size() && !controllableEvents.isEmpty());

        if (!controllableEvents.isEmpty()) {
            OutputProvider.out("The specification does not have finite response, at least one forcible-event "
                    + "loop was found.");

            if (PrintOutputOption.print()) {
                OutputProvider.out("The following events might still occur in a forcible loop.");
                for (Event event: controllableEvents) {
                    OutputProvider.out(getAbsName(event) + ", ");
                }
            }
        } else {
            OutputProvider.out("The specification has finite response.");
        }

        return controllableEvents.isEmpty();
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
        // edges, and events..
        Set<EventLoop> controllableEventLoops = searchEventLoops(aut, controllableEvents);

        // Print the results.
        if (!controllableEventLoops.isEmpty()) {
            OutputProvider.dbg();
            OutputProvider.dbg(fmt("The following events have initially been encountered in a controllable-event loop "
                    + "of automaton %s", getAbsName(aut)));
            for (EventLoop eventLoop: controllableEventLoops) {
                OutputProvider.dbg("* " + eventLoop.toString());
            }
            OutputProvider.dbg(); // Print empty line.
        }

        // Calculate the non controllable independent variables. As we later have to abstract from these in the
        // global guards. Variables are cached, only calculate when the controllable event set has changed.
        VarInfo[] nonCtrlIndependentVarsInfos = null;
        if (controllableEventsChanged) {
            controllableEventsChanged = false;

            BitSet bits = new BitSet(builder.cifVarInfoBuilder.varInfos.size());
            for (Event evt: controllableEvents) {
                for (Declaration var: eventVarUpdate.getOrDefault(evt, set())) {
                    VarInfo varInfo = builder.cifVarInfoBuilder.getVarInfo(var, 0);
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

        // Collect which events occur in potential controllable-event loops.
        Set<Event> eventsInPotentialControllableLoops = set();

        // Check for if the loop is controllable unconnectable, if not, it is a potential controllable-event loop in the
        // system.
        for (EventLoop controllableEventLoop: controllableEventLoops) {
            if (isUnconnectable(controllableEventLoop, nonCtrlIndependentVarsInfos)) {
                OutputProvider.dbg(fmt("%s is forcible-unconnectable", controllableEventLoop.toString()));
            } else {
                OutputProvider.dbg(fmt("Warning: %s is not forcible-unconnectable", controllableEventLoop.toString()));
                eventsInPotentialControllableLoops.addAll(controllableEventLoop.events);
            }
        }

        // Determine which events are in the alphabet of the automaton, but not in any of its potential
        // controllable-event loops.
        Set<Event> eventsInAlphabetNotInLoop = Sets.difference(getAlphabet(aut), eventsInPotentialControllableLoops);

        // Remove the events that are in the alphabet of the automaton, but not in any of its potential
        // controllable-event loops from the controllable event set.
        controllableEventsChanged = controllableEvents.removeAll(eventsInAlphabetNotInLoop);
    }

    /**
     * Checks whether the controllable-event loop is controllable unconnectable. Controllable unconnectable is checked
     * after abstracting from the events that are might change their value due to other controllable events.
     *
     * @param controllableEventLoop The loop to check to be controllable unconnectable.
     * @param nonCtrlIndependentVarsInfos The variables that are updated by forcible events, not forcible independent
     *     variables.
     * @return {@code true} if the loop is forcible unconnectable, {@code false} otherwise.
     */
    private boolean isUnconnectable(EventLoop controllableEventLoop, VarInfo[] nonCtrlIndependentVarsInfos) {
        Node n = Tree.ONE;
        for (Event evt: controllableEventLoop.events) {
            Node g = globalGuards.get(evt);
            Node gAbstract = builder.tree.variableAbstractions(g, nonCtrlIndependentVarsInfos);
            n = builder.tree.conjunct(n, gAbstract);
            if (n == Tree.ZERO) {
                return true;
            }
        }
        return false;
    }

    /**
     * Constructs the mapping between events and the variables that are updated by edges labeled by that event.
     *
     * @return The constructed mapping.
     */
    private Map<Event, Set<Declaration>> collectEventVarUpdate() {
        Map<Event, Set<Declaration>> eventVarUpdate = map();
        for (Automaton aut: automata) {
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    for (Update update: edge.getUpdates()) {
                        if (update instanceof Assignment) {
                            Assignment assignment = (Assignment)update;
                            collectEventsAddressable(assignment.getAddressable(), getEvents(edge), eventVarUpdate);
                        } else {
                            // 'If' updates should have been eliminated.
                            throw new AssertionError(fmt("Unexpected update encountered: '%s'.", update.toString()));
                        }
                    }
                }
            }
        }
        return eventVarUpdate;
    }

    /**
     * Collects the relations between events and the variable from an addressable.
     *
     * @param addressable The addressable to collect, may only be a discrete variable.
     * @param events The events that are labeled on the edge with this addressable.
     * @param eventVarUpdate The map in which to save the 'event updates variable' information.
     */
    private void collectEventsAddressable(Expression addressable, Set<Event> events,
            Map<Event, Set<Declaration>> eventVarUpdate)
    {
        if (addressable instanceof DiscVariableExpression) {
            DiscVariable adressedVar = ((DiscVariableExpression)addressable).getVariable();

            // Add the 'event updates variable' information in the map.
            for (Event evt: events) {
                Set<Declaration> vars = eventVarUpdate.getOrDefault(evt, set());
                vars.add(adressedVar);
                eventVarUpdate.put(evt, vars);
            }
        } else {
            // Partial assignments and multi-assignments should be eliminated.
            throw new AssertionError(fmt("Unexpected addressable encountered: '%s'.", addressable.toString()));
        }
    }

    /**
     * Constructs a mapping between events and their global guards as a MDD node. The event is enabled in the
     * specification if and only if its global guard evaluates to {@code true}. Multiple guards on a single edge are
     * combined with 'and'. Guards for an event that is labeled on more than one edge in an automaton are combined with
     * 'or'.
     *
     * @param events The events for which to collect the global guards.
     * @return A mapping between events and their global guards as a MDD node.
     */
    public Map<Event, Node> collectGlobalGuards(Set<Event> events) {
        // An event is enabled in the specification if all of the global guard expressions evaluate to 'true'.
        Map<Event, List<Expression>> eventGlobalGuards = map();

        for (Automaton aut: automata) {
            // An event is enabled in an automaton if any of the automaton guard expressions evaluate to 'true'.
            Map<Event, List<Expression>> eventAutGuards = map();
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    Set<Event> intersection = intersection(getEvents(edge), events);
                    if (intersection.isEmpty() || edge.getGuards().isEmpty()) {
                        continue;
                    }

                    // An edge with a guard, labeled with an event from the supplied set has been found. The edge is
                    // enabled if all expression evaluate to 'true'. Therefore, the guards are combined via 'and'.
                    for (Event evt: intersection) {
                        List<Expression> automGuards = eventAutGuards.getOrDefault(evt, list());
                        automGuards.add(createConjunction(deepclone(edge.getGuards())));
                        eventAutGuards.put(evt, automGuards);
                    }
                }
            }

            // An event is enabled in an automaton if at least one edge with that event is enabled. Hence,
            // the automaton guards are combined via 'or'.
            for (Entry<Event, List<Expression>> entry: eventAutGuards.entrySet()) {
                List<Expression> globalGuards = eventGlobalGuards.getOrDefault(entry.getKey(), list());
                globalGuards.add(createDisjunction(entry.getValue()));
                eventGlobalGuards.put(entry.getKey(), globalGuards);
            }
        }

        Map<Event, Node> eventNode = mapc(events.size());
        for (Event event: events) {
            List<Expression> globalGuards = eventGlobalGuards.getOrDefault(event, Collections.emptyList());
            Node nodeGuard = globalGuards.isEmpty() ? Tree.ONE
                    : builder.getPredicateConvertor().convert(globalGuards).get(1);
            eventNode.put(event, nodeGuard);
        }
        return eventNode;
    }

    /**
     * Collect the controllable events declared in the given component (recursively).
     *
     * @param comp The component.
     * @param ctrlEvents The controllable events collected so far. Is modified in-place.
     */
    public static void collectControllableEvents(ComplexComponent comp, Set<Event> ctrlEvents) {
        // Collect locally.
        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof Event && ((Event)decl).getControllable()) {
                ctrlEvents.add((Event)decl);
            }
        }

        // Collect recursively.
        if (comp instanceof Group) {
            for (Component child: ((Group)comp).getComponents()) {
                collectControllableEvents((ComplexComponent)child, ctrlEvents);
            }
        }
    }
}
