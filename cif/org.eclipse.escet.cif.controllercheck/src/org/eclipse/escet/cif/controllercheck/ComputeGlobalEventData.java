//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck;

import static org.eclipse.escet.cif.common.CifCollectUtils.collectAutomata;
import static org.eclipse.escet.cif.common.CifCollectUtils.collectControllableEvents;
import static org.eclipse.escet.cif.common.CifCollectUtils.collectDiscAndInputVariables;
import static org.eclipse.escet.cif.common.CifEventUtils.getAlphabet;
import static org.eclipse.escet.cif.common.CifEventUtils.getEvents;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.copy;
import static org.eclipse.escet.common.java.Sets.intersection;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.controllercheck.multivaluetrees.CifVarInfoBuilder;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.MvSpecBuilder;
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
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.AppEnvData;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;

/** Compute global event guards for the finite response check. */
public class ComputeGlobalEventData {
    /** Index for denoting reading a variable. */
    private static final int READ_INDEX = 0;

    /** Index for denoting writing a variable. */
    private static final int WRITE_INDEX = 1;

    /** The application context to use. */
    private final AppEnvData env = AppEnv.getData();

    /** Automata of the specification. */
    private List<Automaton> automata = list();

    /** Discrete and input variables of the specification. */
    private List<Declaration> variables = list();

    /**
     * Mapping between events and the variables updated by edges labeled with that event. Is {@code null} until
     * computed, see {@link #collectEventVarUpdate}.
     */
    private Map<Event, Set<Declaration>> eventVarUpdate;

    /** The controllable event set with at least one edge that can be enabled. */
    private Set<Event> controllableEvents = set();

    /**
     * Mapping from events and their global guard and update as a MDD node. Is {@code null} until computed.
     */
    private Map<Event, GlobalEventGuardUpdate> globalEventsGuardUpdate = null;

    /** Builder for the MDD tree. */
    private MvSpecBuilder builder;

    /**
     * Perform analysis of the specification, extract relevant data, and compute global guards and updates for the
     * controllable events.
     *
     * @param spec Specification to analyze.
     * @return Whether the computation finished. The computation only does not finish when the user aborts the
     *     computation prematurely.
     */
    public boolean compute(Specification spec) {
        // Collect automata and controllable events.
        automata = collectAutomata(spec, list());
        controllableEvents = collectControllableEvents(spec, set());
        if (automata.isEmpty() || controllableEvents.isEmpty()) {
            // Finite response trivially holds.
            return true;
        }

        // Collect variables.
        variables = collectDiscAndInputVariables(spec, list());
        if (env.isTerminationRequested()) {
            return false;
        }

        // Compute which variables are updated by each event.
        collectEventVarUpdate();
        if (env.isTerminationRequested()) {
            return false;
        }

        // Construct the MDD tree class.
        CifVarInfoBuilder cifVarInfoBuilder = new CifVarInfoBuilder(2);
        cifVarInfoBuilder.addVariablesGroupOnVariable(variables);
        builder = new MvSpecBuilder(cifVarInfoBuilder, READ_INDEX, WRITE_INDEX);
        if (env.isTerminationRequested()) {
            return false;
        }

        // Compute global guards for the controllable events.
        if (!collectGlobalGuards(controllableEvents)) {
            return false;
        }

        return !env.isTerminationRequested();
    }

    /**
     * Construct the mapping between events and the variables that are updated by edges labeled by that event.
     */
    private void collectEventVarUpdate() {
        eventVarUpdate = map();
        for (Automaton aut: automata) {
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    for (Update update: edge.getUpdates()) {
                        Assert.check(update instanceof Assignment); // 'If' updates should have been eliminated.
                        Assignment assignment = (Assignment)update;
                        collectEventsAddressable(assignment.getAddressable(), getEvents(edge), eventVarUpdate);
                    }
                }
            }
        }
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
        // Partial assignments and multi-assignments should have been eliminated.
        Assert.check(addressable instanceof DiscVariableExpression);
        DiscVariable adressedVar = ((DiscVariableExpression)addressable).getVariable();

        // Add the 'event updates variable' information in the map.
        for (Event evt: events) {
            Set<Declaration> vars = eventVarUpdate.getOrDefault(evt, set());
            vars.add(adressedVar);
            eventVarUpdate.put(evt, vars);
        }
    }

    /**
     * Compute global model guard for each provided event.
     *
     * @param events Set of events to compute a model guard.
     * @return Whether the computation was finished. It only doesn't finish if the user aborts the computation.
     */
    private boolean collectGlobalGuards(Set<Event> events) {
        Tree tree = builder.tree;

        // Global condition on taking an edge with the event.
        globalEventsGuardUpdate = mapc(events.size());
        for (Event event: events) {
            globalEventsGuardUpdate.put(event, new GlobalEventGuardUpdate(event));
        }

        // Compute the global guard for enabling each event for all automata.
        for (Automaton aut: automata) {
            Set<Event> eventsOfAut = intersection(getAlphabet(aut), events);

            // Global guard for each event in one automaton only, as a disjunction between edges.
            Map<Event, Node> autGuards = makeEventMap(eventsOfAut, Tree.ZERO);

            // Within an automaton, for each event constructs a disjunction over its edges.
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    // Filter on relevant events.
                    Set<Event> eventsOfEdge = intersection(getEvents(edge), eventsOfAut);
                    if (eventsOfEdge.isEmpty()) {
                        continue;
                    }

                    // Compute guard and update of the edge.
                    Node guard = computeGuard(edge);

                    // Add the guard and guarded update as alternative to the relevant events of the edge.
                    for (Event evt: eventsOfEdge) {
                        Node autGuard = autGuards.get(evt);
                        autGuards.put(evt, tree.disjunct(autGuard, guard));
                    }

                    // Abort computation if the user requests it.
                    if (env.isTerminationRequested()) {
                        return false;
                    }
                }
            }

            // At global level, guards and updates of each event must synchronize between participating automata.
            for (Entry<Event, Node> guardEntry: autGuards.entrySet()) {
                Event event = guardEntry.getKey();
                GlobalEventGuardUpdate evtData = globalEventsGuardUpdate.get(event);
                evtData.update(guardEntry.getValue(), tree);
            }
        }

        // Disable events that are completely unused.
        for (GlobalEventGuardUpdate evtdata: globalEventsGuardUpdate.values()) {
            if (!evtdata.isInitialized()) {
                evtdata.update(Tree.ZERO, tree);
            }
        }

        return true;
    }

    /**
     * Convert the guard of an edge to an MDD relation.
     *
     * @param edge Edge to use.
     * @return The guard as an MDD tree.
     */
    private Node computeGuard(Edge edge) {
        Node guard = Tree.ONE;
        for (Expression grd: edge.getGuards()) {
            Node node = builder.getExpressionConvertor().convert(grd).get(1);
            guard = builder.tree.conjunct(guard, node);
        }
        return guard;
    }

    /**
     * Construct an initialized event map for all given events.
     *
     * @param events Events that should be available in the new map.
     * @param node Node to initialize the values of the map.
     * @return The initialized map.
     */
    private static Map<Event, Node> makeEventMap(Set<Event> events, Node node) {
        Map<Event, Node> eventMap = mapc(events.size());
        for (Event evt: events) {
            eventMap.put(evt, node);
        }
        return eventMap;
    }

    /**
     * Get a read-only collection of the automata in the specification.
     *
     * @return The found automata in the specification, value may not be modified.
     */
    public List<Automaton> getReadOnlyAutomata() {
        return Collections.unmodifiableList(automata);
    }

    /**
     * Get the collection of the controllable events in the specification.
     *
     * @return A shallow copy of the found controllable events in the specification, collection may be modified.
     */
    public Set<Event> getShallowCopiedControllableEvents() {
        return copy(controllableEvents);
    }

    /**
     * Get a read-only copy of the sets variables that are updated by an event.
     *
     * <p>
     * May only be accessed when there is at least one automaton and at least one controllable event.
     * </p>
     *
     * @return For each event the set updated variables.
     */
    public Map<Event, Set<Declaration>> getReadOnlyEventVarUpate() {
        return Collections.unmodifiableMap(eventVarUpdate);
    }

    /**
     * Get a read-only collection of the computed global guard and update for all controllable events in the specification.
     *
     * <p>
     * May only be accessed when there is at least one automaton and at least one controllable event.
     * </p>
     *
     * @return The computed global guard and update for all controllable events in the specification.
     */
    public Map<Event, GlobalEventGuardUpdate> getReadOnlyGlobalEventsGuardUpdate() {
        return Collections.unmodifiableMap(globalEventsGuardUpdate);
    }

    /**
     * Get the MDD builder.
     *
     * <p>
     * May only be accessed when there is at least one automaton and at least one controllable event.
     * </p>
     *
     * @return The MDD builder.
     */
    public MvSpecBuilder getBuilder() {
        return builder;
    }
}
