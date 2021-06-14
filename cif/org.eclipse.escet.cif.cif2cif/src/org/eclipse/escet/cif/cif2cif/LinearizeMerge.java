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

package org.eclipse.escet.cif.cif2cif;

import static org.eclipse.escet.cif.common.CifEventUtils.filterAutomata;
import static org.eclipse.escet.cif.common.CifEventUtils.filterMonitorAuts;
import static org.eclipse.escet.cif.common.CifValueUtils.createConjunction;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAssignment;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newElifUpdate;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newIfUpdate;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.emf.EMFHelper.deepclone;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.java.Triple.triple;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.common.CifEventUtils;
import org.eclipse.escet.cif.common.CifEventUtils.Alphabets;
import org.eclipse.escet.cif.common.CifGuardUtils;
import org.eclipse.escet.cif.common.CifGuardUtils.LocRefExprCreator;
import org.eclipse.escet.cif.common.CifSortUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.ElifUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.IfUpdate;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BoolExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ElifExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.IfExpression;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.VoidType;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Triple;

/**
 * Linearization transformation that merges all edges for non-tau events, resulting in a single self loop for each
 * non-tau event. If the original automata have non-determinism, for instance due to multiple outgoing edges for a
 * single location, for the same event (excluding the "tau" event), with overlapping guards, this choice is eliminated
 * as part of this transformation. That is, in the resulting specification, the first possible transition is always
 * taken, similarly as the simulator would, assuming the simulator is configured to always choose the first edge (or
 * transition). By eliminating non-deterministic choice for non-tau events, this transformation ensures that the model
 * size of the resulting specification is near-linear compared to the model size of the original specification.
 *
 * <p>
 * Dummy updates are added to ensure that the correct updates are taken, but this is not optimized, and may result in
 * more dummy updates than necessary.
 * </p>
 *
 * <p>
 * See the {@link LinearizeBase} class for further details.
 * </p>
 */
public class LinearizeMerge extends LinearizeBase {
    @Override
    protected void createEdges(List<Automaton> auts, Automaton mergedAut, Location mergedLoc) {
        // Get all events to process (the merged alphabet).
        List<Event> events = set2list(CifEventUtils.getAlphabet(mergedAut));

        // Get alphabets.
        List<Set<Event>> syncAlphabets = listc(auts.size());
        List<Set<Event>> sendAlphabets = listc(auts.size());
        List<Set<Event>> recvAlphabets = listc(auts.size());
        List<Set<Event>> moniAlphabets = listc(auts.size());
        for (Alphabets autAlphabets: alphabets) {
            syncAlphabets.add(autAlphabets.syncAlphabet);
            sendAlphabets.add(autAlphabets.sendAlphabet);
            recvAlphabets.add(autAlphabets.recvAlphabet);
            moniAlphabets.add(autAlphabets.moniAlphabet);
        }

        // Filter automata, per event.
        List<List<Automaton>> syncAuts;
        List<List<Automaton>> sendAuts;
        List<List<Automaton>> recvAuts;
        List<Set<Automaton>> moniAuts;
        syncAuts = filterAutomata(auts, syncAlphabets, events);
        sendAuts = filterAutomata(auts, sendAlphabets, events);
        recvAuts = filterAutomata(auts, recvAlphabets, events);
        moniAuts = filterMonitorAuts(auts, moniAlphabets, events);

        // Create location reference expression creator.
        LocRefExprCreator creator = new LocRefExprCreator() {
            @Override
            public Expression create(Location loc) {
                return lpIntroducer.createLocRef(loc);
            }
        };

        // Get relevant automata per event, and merge guards.
        List<Expression> guards = listc(events.size());
        for (int i = 0; i < events.size(); i++) {
            Expression guard = CifGuardUtils.mergeGuards(syncAuts.get(i), sendAuts.get(i), recvAuts.get(i),
                    moniAuts.get(i), events.get(i), creator);
            guards.add(guard);
        }

        // Eliminate monitors from the automaton. We do this after merging of
        // the guards, to be able to take monitors into account there. For the
        // remainder of this method (especially merging of updates), it is
        // easier if we eliminate monitors, in order to avoid extra work for
        // them.
        ElimMonitors monitorElim = new ElimMonitors();
        for (Automaton aut: auts) {
            monitorElim.transform(aut);
        }

        // Add edges for each of the events.
        int i = 0;
        for (Event event: events) {
            // Get relevant automata and merged guard.
            List<Automaton> evtSyncAuts = syncAuts.get(i);
            List<Automaton> evtSendAuts = sendAuts.get(i);
            List<Automaton> evtRecvAuts = recvAuts.get(i);
            Expression guard = guards.get(i);
            i++;

            // Get merged updates.
            List<Update> updates = mergeUpdates(evtSyncAuts, evtSendAuts, evtRecvAuts, event);

            // Ignore target location, as location pointer updates already
            // handle that.

            // Create new edge.
            EventExpression eventRef = newEventExpression();
            eventRef.setEvent(event);
            eventRef.setType(newBoolType());

            EdgeEvent edgeEvent = newEdgeEvent();
            edgeEvent.setEvent(eventRef);

            Edge edge = newEdge();
            edge.getEvents().add(edgeEvent);
            edge.getGuards().add(guard);
            edge.getUpdates().addAll(updates);

            // Add new edge.
            mergedLoc.getEdges().add(edge);
        }
    }

    /**
     * Merges updates of the given automata for the edges with the given event, to create combined updates.
     *
     * @param syncAuts The original automata that synchronize over the given event, sorted in ascending order based on
     *     their absolute names (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     * @param sendAuts The original automata that send over the given event, sorted in ascending order based on their
     *     absolute names (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     * @param recvAuts The original automata that receive over the given event, sorted in ascending order based on their
     *     absolute names (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     * @param event The event for which to create a combined updates.
     * @return The combined updates.
     */
    private List<Update> mergeUpdates(List<Automaton> syncAuts, List<Automaton> sendAuts, List<Automaton> recvAuts,
            Event event)
    {
        // Is the event a channel?
        boolean isChannel = event.getType() != null;
        boolean isVoid = isChannel && (event.getType() instanceof VoidType);

        // Get send value, if applicable.
        Expression sendValue = null;
        if (isChannel && !isVoid) {
            sendValue = getSendValue(sendAuts, event);

            if (sendValue == null) {
                // If there is no sender the event will never be enabled. Since there is no value for potential
                // ReceivedExpressions, all updates are omitted. We use the non-absolute event name for this warning,
                // rather than the absolute event name. Due to previous processing during this transformation the
                // absolute name would be 'M.<something>', which makes no sense in the original specification.
                warn(fmt("Event \"%s\" does not have a 'send' edge and is never enabled.", event.getName()));
                return list();
            }
        }

        // Add updates, for all send/receive alternatives.
        List<Update> updates = list();
        List<Triple<Expression, Location, List<Update>>> triples = list();
        if (isChannel) {
            // Add for send.
            triples.clear();
            for (Automaton aut: sendAuts) {
                collectUpdates(aut, event, null, triples);
            }
            addUpdates(triples, updates);

            // Add for receive.
            triples.clear();
            for (Automaton aut: recvAuts) {
                collectUpdates(aut, event, sendValue, triples);
            }
            addUpdates(triples, updates);
        }

        // Add updates, for synchronization, per automaton.
        for (Automaton aut: syncAuts) {
            triples.clear();
            collectUpdates(aut, event, null, triples);
            addUpdates(triples, updates);
        }

        // Return combined updates.
        return updates;
    }

    /**
     * Get the 'send' value for the given event.
     *
     * @param auts The original automata that send over the given event, sorted in ascending order based on their
     *     absolute names (without escaping). See also {@link CifSortUtils#sortCifObjects}.
     * @param event The event for which to return the 'send' value. Must be a channel.
     * @return The 'send' value for the given event or {@code null} if there is no sender.
     */
    private Expression getSendValue(List<Automaton> auts, Event event) {
        // Initialize condition to value pairs.
        List<Pair<Expression, Expression>> pairs = list();

        // Get condition to value pairs from the send automata.
        for (Automaton aut: auts) {
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    for (EdgeEvent edgeEvent: edge.getEvents()) {
                        // Skip non-sends.
                        if (!(edgeEvent instanceof EdgeSend)) {
                            continue;
                        }

                        // Skip if event doesn't match.
                        Event e = CifEventUtils.getEventFromEdgeEvent(edgeEvent);
                        if (e != event) {
                            continue;
                        }

                        // Add 'loc and guards' : 'value' pair.
                        Expression lexpr = lpIntroducer.createLocRef(loc);

                        Expression guards = createConjunction(deepclone(edge.getGuards()));

                        EdgeSend sendEdge = (EdgeSend)edgeEvent;
                        Pair<Expression, Expression> pair;
                        pair = pair(createConjunction(list(lexpr, guards)), deepclone(sendEdge.getValue()));

                        pairs.add(pair);
                    }
                }
            }
        }

        // If there is no sender, there is no value to be sent.
        if (pairs.isEmpty()) {
            return null;
        }

        // If only on one edge, we have single send value.
        if (pairs.size() == 1) {
            return first(pairs).right;
        }

        // Conditional send value. Create 'if' expression.
        IfExpression rslt = newIfExpression();

        rslt.getGuards().add(first(pairs).left);
        rslt.setThen(first(pairs).right);

        for (int i = 1; i < pairs.size() - 1; i++) {
            ElifExpression elif = newElifExpression();
            rslt.getElifs().add(elif);

            elif.getGuards().add(pairs.get(i).left);
            elif.setThen(pairs.get(i).right);
        }

        rslt.setElse(last(pairs).right);

        CifType eventType = event.getType();
        rslt.setType(deepclone(eventType));

        return rslt;
    }

    /**
     * Collects the updates of the given automaton for the edges with the given event, and returns 'condition, location,
     * updates' triples.
     *
     * <p>
     * This method may be applied to automata that synchronize, send, or receive over the given event. This method
     * assumes that monitors have already been eliminated.
     * </p>
     *
     * @param aut The automaton.
     * @param event The event for which to add triples.
     * @param triples The 'condition, location, updates' list. Is extended in-place.
     * @param sendValue The 'send' value, or {@code null} if not applicable.
     */
    private void collectUpdates(Automaton aut, Event event, Expression sendValue,
            List<Triple<Expression, Location, List<Update>>> triples)
    {
        for (Location loc: aut.getLocations()) {
            for (Edge edge: loc.getEdges()) {
                // Get edge events. If empty, then 'tau', which we may ignore
                // (no special case needed).
                List<EdgeEvent> edgeEvents = edge.getEvents();

                // Process all of the edge events.
                for (EdgeEvent edgeEvent: edgeEvents) {
                    // Process only if event matches.
                    Event e = CifEventUtils.getEventFromEdgeEvent(edgeEvent);
                    if (e != event) {
                        continue;
                    }

                    // Get 'loc and guards', 'loc', 'updates' triple.
                    Expression lexpr = lpIntroducer.createLocRef(loc);

                    Expression guards = createConjunction(deepclone(edge.getGuards()));

                    List<Update> edgeUpdates;
                    edgeUpdates = replaceUpdates(edge.getUpdates(), sendValue);

                    Triple<Expression, Location, List<Update>> trip;
                    trip = triple(createConjunction(list(lexpr, guards)), loc, edgeUpdates);

                    // Add triple.
                    triples.add(trip);
                }
            }
        }
    }

    /**
     * Adds updates of the given 'condition, location, update' triples, to the given updates list.
     *
     * @param triples The 'condition, location, update' triples.
     * @param updates The updates list. Is extended in-place.
     */
    private void addUpdates(List<Triple<Expression, Location, List<Update>>> triples, List<Update> updates) {
        // If no triples, or only triples without updates, then nothing to add.
        boolean allEmpty = true;

        // Save a variable that can be updated, in case we have to construct a dummy update later. If such a variable
        // is not found, then no dummy update has to be created. Hence, there won't be problems with the variable
        // being null.
        Expression updatableVar = null;

        for (Triple<Expression, Location, List<Update>> trip: triples) {
            if (!trip.third.isEmpty()) {
                // Has an update.
                allEmpty = false;
                updatableVar = getUpdatedVarRefExpr(first(trip.third));
                break;
            }
        }
        if (allEmpty) {
            return;
        }

        // If multiple outgoing edges for the same event, and one of them has
        // no updates, add dummy updates to ensure we keep the chosen edge
        // deterministic. That is, choose the same edge as the simulator would,
        // assuming the simulator is configured to always choose the first
        // edge. Similarly, for outgoing edges within a single automaton, from
        // different locations, if only one of them has an update, we only
        // want to do the update if it is relevant. Therefore, for all empty
        // updates, add a dummy update. This also applies to sends/receives,
        // where we need the same updates as the edge of the send value.

        // Add dummy update for triples without updates.
        for (Triple<Expression, Location, List<Update>> trip: triples) {
            // Skip non-empty updates.
            List<Update> tripUpdates = trip.third;
            if (!tripUpdates.isEmpty()) {
                continue;
            }

            // Get location.
            Location loc = trip.second;

            // Add dummy update.
            Expression lexpr = lpIntroducer.createLocRef(loc);

            Assignment asgn = newAssignment();
            if (lexpr instanceof BinaryExpression) {
                // The automaton has has multiple locations and this is a 'var = lit' binary expression. Create a
                // 'var := lit' assignment, where 'var' is the location pointer.
                BinaryExpression binExpr = (BinaryExpression)lexpr;
                asgn.setAddressable(binExpr.getLeft());
                asgn.setValue(binExpr.getRight());
            } else if (lexpr instanceof BoolExpression) {
                // The automaton has exactly one location and this is a 'true' boolean expression. Create a 'var := var'
                // assignment, where 'var' is the updateable variable we found before.
                asgn.setAddressable(deepclone(updatableVar));
                asgn.setValue(deepclone(updatableVar));
            } else {
                throw new RuntimeException("Unexpected expression: " + lexpr.toString());
            }

            tripUpdates.add(asgn);
        }

        // If only on one triple, we have unconditional updates.
        if (triples.size() == 1) {
            updates.addAll(first(triples).third);
            return;
        }

        // Conditional updates. Create 'if' update.
        IfUpdate rslt = newIfUpdate();

        rslt.getGuards().add(first(triples).first);
        rslt.getThens().addAll(first(triples).third);

        for (int i = 1; i < triples.size(); i++) {
            ElifUpdate elif = newElifUpdate();
            rslt.getElifs().add(elif);

            elif.getGuards().add(triples.get(i).first);
            elif.getThens().addAll(triples.get(i).third);
        }

        updates.add(rslt);
    }

    /**
     * Returns an expression that references (a part of) a variable. For an assignment this is the addressable, for an
     * if-update this is a variable updated by the first 'then'.
     *
     * @param update The update to retrieve a variable from.
     * @return An expression that references (a part of) a variable".
     */
    private Expression getUpdatedVarRefExpr(Update update) {
        if (update instanceof Assignment) {
            Assignment ass = (Assignment)update;
            return ass.getAddressable();
        } else if (update instanceof IfUpdate) {
            IfUpdate ifUpdate = (IfUpdate)update;
            return getUpdatedVarRefExpr(first(ifUpdate.getThens()));
        } else {
            throw new RuntimeException("Unexpected update: " + update.toString());
        }
    }
}
