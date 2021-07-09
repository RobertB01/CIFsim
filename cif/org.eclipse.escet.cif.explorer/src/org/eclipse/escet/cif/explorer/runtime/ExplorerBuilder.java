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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Equation;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeReceive;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeSend;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Monitors;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Class that constructs an explorer of a specification. */
public class ExplorerBuilder {
    /** CIF specification to build an explorer for. */
    public final Specification spec;

    /** Collected automata of the specification. */
    private List<Automaton> automata = list();

    /**
     * Collected automata of the specification, with multiple potential initial locations. Is a subset of
     * {@link #automata}.
     */
    private List<Automaton> multiInitAuts = list();

    /** Collected continuous and discrete variables of the specification. */
    private List<PositionObject> variables = list();

    /**
     * Collected discrete variables of the specification, with multiple potential initial values. Is a subset of
     * {@link #variables}.
     */
    private List<DiscVariable> multiInitVars = list();

    /** Collected algebraic variables. */
    private Map<AlgVariable, CollectedAlgVariable> algVariables = map();

    /** Collected event declarations. */
    private List<Event> events = list();

    /**
     * Found state invariants ordered by location. The {@code null} location is used for invariants of components.
     *
     * <p>
     * Use {@link #addInvariants} to add invariants.
     * </p>
     */
    private Map<Location, CollectedInvariants> stateInvs = map();

    /**
     * Per event, found state/event exclusion invariants ordered by location. The {@code null} location is used for
     * invariants of components.
     *
     * <p>
     * Use {@link #addInvariants} to add invariants.
     * </p>
     */
    private Map<Event, Map<Location, CollectedInvariants>> stateEvtExclInvs = map();

    /** Found marker predicates of components. */
    private List<Expression> markeds = list();

    /**
     * Found initialization predicates ordered by location. The {@code null} location is used for initialization
     * predicates of components.
     *
     * <p>
     * Use {@link #addInitials} to add initialization predicates.
     * </p>
     */
    private Map<Location, List<Expression>> initialLocs = map();

    /**
     * Constructor of the {@link ExplorerBuilder} class.
     *
     * @param spec Specification to build an explorer for.
     */
    public ExplorerBuilder(Specification spec) {
        this.spec = spec;
    }

    /**
     * Add several invariants to the collection.
     *
     * @param loc Location containing the invariants, or {@code null} for invariants of a component.
     * @param invs Invariants to add.
     */
    private void addInvariants(Location loc, List<Invariant> invs) {
        if (invs.isEmpty()) {
            return;
        }

        // Add state invariants.
        CollectedInvariants ci = stateInvs.get(loc);
        if (ci == null) {
            ci = new CollectedInvariants();
            stateInvs.put(loc, ci);
        }
        for (Invariant inv: invs) {
            if (inv.getInvKind() != InvKind.STATE) {
                continue;
            }
            ci.add(inv);
        }

        // Add state/event exclusion invariants.
        for (Invariant inv: invs) {
            if (inv.getInvKind() == InvKind.STATE) {
                continue;
            }
            Event event = ((EventExpression)inv.getEvent()).getEvent();

            Map<Location, CollectedInvariants> el = stateEvtExclInvs.get(event);
            if (el == null) {
                el = map();
                stateEvtExclInvs.put(event, el);
            }

            CollectedInvariants ei = el.get(loc);
            if (ei == null) {
                ei = new CollectedInvariants();
                el.put(loc, ei);
            }

            ei.add(inv);
        }
    }

    /**
     * Add several initialization predicates to the collection.
     *
     * @param loc Location containing the initialization predicates, or {@code null} for initialization predicates of a
     *     component.
     * @param inits Initialization predicates to add.
     */
    private void addInitials(Location loc, List<Expression> inits) {
        if (inits.isEmpty()) {
            return;
        }
        List<Expression> exprs = initialLocs.get(loc);
        if (exprs == null) {
            exprs = copy(inits);
            initialLocs.put(loc, exprs);
            return;
        }
        exprs.addAll(inits);
    }

    /** Collect data from the specification. */
    public void collectData() {
        automata = list();
        variables = list();
        algVariables = map();
        events = list();
        stateInvs = map();
        stateEvtExclInvs = map();
        markeds = list();
        initialLocs = map();

        collectGroup(spec);
    }

    /**
     * Collect automata and variables of the given group (recursively).
     *
     * @param group Group to search.
     */
    private void collectGroup(Group group) {
        collectComponent(group, -1);
        for (Component comp: group.getComponents()) {
            if (comp instanceof Automaton) {
                automata.add((Automaton)comp);
                int autIndex = automata.size() - 1;
                collectComponent((ComplexComponent)comp, autIndex);
                continue;
            }
            Assert.check(comp instanceof Group);
            collectGroup((Group)comp);
        }
    }

    /**
     * Collect several kinds of data from a component.
     *
     * <p>
     * Algebraic variables, events, continuous variables, discrete variables, initialization and marker predicates, and
     * invariants are collected.
     * </p>
     *
     * @param comp Component to inspect (non-recursively).
     * @param autIndex Index number of the automaton if this component is in fact an automaton, else {@code -1}.
     */
    private void collectComponent(ComplexComponent comp, int autIndex) {
        Assert.check(comp.getIoDecls().isEmpty());

        for (Declaration decl: comp.getDeclarations()) {
            if (decl instanceof AlgVariable) {
                AlgVariable av = (AlgVariable)decl;
                algVariables.put(av, new CollectedAlgVariable(av, autIndex));
                continue;
            }
            if (decl instanceof Event) {
                events.add((Event)decl);
                continue;
            }

            if (decl instanceof ContVariable) {
                variables.add(decl);
                continue;
            }

            if (decl instanceof DiscVariable) {
                variables.add(decl);
                DiscVariable var = (DiscVariable)decl;
                if (var.getValue() != null && var.getValue().getValues().size() != 1) {
                    multiInitVars.add(var);
                }
                continue;
            }

            // Constant is ignored.
            // Function is ignored.
            // EnumDecl is ignored.
            // TypeDecl is ignored.
            // InputVariable is ignored.
        }

        for (Equation eq: comp.getEquations()) {
            if (eq.isDerivative()) {
                continue;
            }
            CollectedAlgVariable ca = algVariables.get(eq.getVariable());
            ca.value = eq.getValue();
        }
        addInitials(null, comp.getInitials());
        addInvariants(null, comp.getInvariants());
        markeds.addAll(comp.getMarkeds());

        // For automata, also walk through the locations, and get the
        // initialization predicates, invariants, marker predicates, and
        // equations of algebraic variables.
        if (comp instanceof Automaton) {
            Automaton aut = (Automaton)comp;

            int initCnt = 0;
            for (Location loc: aut.getLocations()) {
                if (!loc.getInitials().isEmpty()) {
                    initCnt++;
                }

                addInitials(loc, loc.getInitials());
                addInvariants(loc, loc.getInvariants());
                // Marker predicates are read directly by the state if needed.

                // Add algebraic equations.
                for (Equation eq: loc.getEquations()) {
                    if (eq.isDerivative()) {
                        continue;
                    }
                    CollectedAlgVariable ca = algVariables.get(eq.getVariable());
                    if (ca.locMap == null) {
                        ca.locMap = map();
                    }
                    ca.locMap.put(loc, eq.getValue());
                }
            }

            if (initCnt > 1) {
                // Multiple potential initial locations, or more precisely,
                // multiple locations with initialization predicates. There
                // may still be a single initial location or no initial
                // location at all, as the initialization predicates may still
                // evaluate to false.
                multiInitAuts.add(aut);
            }
        }
    }

    /**
     * Construct an explorer from the collected information.
     *
     * @param stateFactory Factory for making new states during the exploration process.
     * @return Explorer to explore the states of the specification.
     */
    public Explorer buildExplorer(AbstractStateFactory stateFactory) {
        Automaton[] auts = new Automaton[automata.size()];
        auts = automata.toArray(auts);

        PositionObject[] vars = new PositionObject[variables.size()];
        vars = variables.toArray(vars);

        return new Explorer(auts, multiInitAuts, vars, multiInitVars, algVariables, stateInvs, stateEvtExclInvs,
                markeds, initialLocs, stateFactory);
    }

    /**
     * Derive how each synchronizing event is being used.
     *
     * @param automata Automata in the specification.
     * @param stateEvtExclInvs Per event, found state/event exclusion invariants ordered by location. The {@code null}
     *     location is used for invariants of components.
     * @return Mapping of events to their usage.
     */
    public static List<EventUsage> getSynchronizingEventMap(Automaton[] automata,
            Map<Event, Map<Location, CollectedInvariants>> stateEvtExclInvs)
    {
        Set<Event> allEvents = set(); // All events used anywhere.

        // Alphabet, monitors, send, and receive events of every automaton.
        List<Set<Event>> alphabets = listc(automata.length);
        List<Set<Event>> monitors = listc(automata.length);
        List<Set<Event>> sends = listc(automata.length);
        List<Set<Event>> recvs = listc(automata.length);

        // Walk through all automata collecting the event usages, and adding
        // them to the above variables.
        for (Automaton aut: automata) {
            Set<Event> alphabet = set();
            Set<Event> monitor = set();
            Set<Event> send = set();
            Set<Event> recv = set();

            // Explicit alphabet definition.
            if (aut.getAlphabet() != null) {
                for (Expression expr: aut.getAlphabet().getEvents()) {
                    EventExpression eve = (EventExpression)expr;
                    alphabet.add(eve.getEvent());
                }
            }

            // Walk through locations, collecting sync/send/receive alphabets.
            for (Location loc: aut.getLocations()) {
                for (Edge edge: loc.getEdges()) {
                    if (edge.getEvents().isEmpty()) {
                        continue; // Tau event.
                    }
                    for (EdgeEvent ee: edge.getEvents()) {
                        Expression eventRef = ee.getEvent();
                        if (eventRef instanceof TauExpression) {
                            continue;
                        }
                        EventExpression eve = (EventExpression)eventRef;

                        Event event = eve.getEvent();
                        if (ee instanceof EdgeSend) {
                            send.add(event);
                        } else if (ee instanceof EdgeReceive) {
                            recv.add(event);
                        } else {
                            alphabet.add(event);
                        }
                    }
                }
            }

            // Monitors.
            if (aut.getMonitors() != null) {
                Monitors mons = aut.getMonitors();
                if (mons.getEvents().isEmpty()) {
                    monitor.addAll(alphabet);
                } else {
                    for (Expression expr: mons.getEvents()) {
                        EventExpression ee = (EventExpression)expr;
                        monitor.add(ee.getEvent());
                    }
                }
            }

            alphabets.add(alphabet);
            monitors.add(monitor);
            sends.add(send);
            recvs.add(recv);

            allEvents.addAll(alphabet);
            allEvents.addAll(monitor);
            allEvents.addAll(send);
            allEvents.addAll(recv);
        }

        List<EventUsage> eventUsages = listc(allEvents.size());
        for (Event event: allEvents) {
            int[] alphabetIndices = makeEventIndicesArray(alphabets, event);
            boolean[] monitorAuts = makeEventAutsArray(monitors, event);
            int[] sendIndices = makeEventIndicesArray(sends, event);
            int[] recvIndices = makeEventIndicesArray(recvs, event);
            Map<Location, CollectedInvariants> evtLocInvs;
            evtLocInvs = stateEvtExclInvs.get(event);
            if (evtLocInvs == null) {
                evtLocInvs = Collections.emptyMap();
            }
            EventUsage eu = new EventUsage(event, alphabetIndices, monitorAuts, sendIndices, recvIndices, evtLocInvs);
            eventUsages.add(eu);
        }
        return eventUsages;
    }

    /**
     * Construct an array with automaton indices that have a given event in their set.
     *
     * @param usage Event usages per automaton.
     * @param event Event to select on.
     * @return Array with indices of automata that have the given event in their set.
     */
    private static int[] makeEventIndicesArray(List<Set<Event>> usage, Event event) {
        int[] indices = new int[usage.size()];
        int last = 0;

        for (int i = 0; i < usage.size(); i++) {
            if (usage.get(i).contains(event)) {
                indices[last++] = i;
            }
        }
        return Arrays.copyOf(indices, last);
    }

    /**
     * Construct an array which automata have a given event in their set.
     *
     * @param usage Event usages per automaton.
     * @param event Event to select on.
     * @return Per automata, whether the automaton has the given event in its set.
     */
    private static boolean[] makeEventAutsArray(List<Set<Event>> usage, Event event) {
        boolean[] hasEvents = new boolean[usage.size()];

        for (int i = 0; i < usage.size(); i++) {
            hasEvents[i] = usage.get(i).contains(event);
        }
        return hasEvents;
    }
}
