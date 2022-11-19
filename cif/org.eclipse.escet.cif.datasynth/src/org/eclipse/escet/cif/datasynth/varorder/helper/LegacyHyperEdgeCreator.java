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

package org.eclipse.escet.cif.datasynth.varorder.helper;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.datasynth.spec.SynthesisDiscVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisInputVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisLocPtrVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Assignment;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.TauExpression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** Automatic variable ordering hyper-edge creator. */
class LegacyHyperEdgeCreator {
    /** The synthesis variables. */
    private List<SynthesisVariable> variables;

    /** The hyper-edges created so far. */
    private List<BitSet> hyperEdges = list();

    /** Mapping from events to the CIF variable objects to put on the hyper-edge for that event. */
    private Map<Event, Set<PositionObject>> eventHyperEdges = map();

    /**
     * Creates and returns hyper-edges for the given CIF specification.
     *
     * @param spec The CIF specification.
     * @param variables The synthesis variables.
     * @return The hyper-edges.
     */
    List<BitSet> getHyperEdges(Specification spec, List<SynthesisVariable> variables) {
        // Initialization.
        this.variables = variables;
        this.hyperEdges = list();
        this.eventHyperEdges = map();

        // Create hyper-edges.
        addHyperEdges(spec);
        for (Set<PositionObject> vars: eventHyperEdges.values()) {
            addHyperEdge(vars);
        }

        // Cleanup.
        List<BitSet> rslt = hyperEdges;
        this.eventHyperEdges = null;
        this.variables = null;
        this.hyperEdges = null;

        // Return the hyperedges.
        return rslt;
    }

    /**
     * Add hyper-edges for the given component, recursively.
     *
     * @param comp The component.
     */
    private void addHyperEdges(ComplexComponent comp) {
        // Add a hyper-edge per invariant of the component.
        for (Invariant inv: comp.getInvariants()) {
            Expression pred = inv.getPredicate();
            VariableCollector varCollector = new VariableCollector();
            Set<PositionObject> vars = set();
            varCollector.collectCifVarObjs(pred, vars);
            addHyperEdge(vars);
        }

        // Add hyper-edges for CIF automata.
        if (comp instanceof Automaton) {
            Automaton aut = (Automaton)comp;
            for (Location loc: aut.getLocations()) {
                // Add a hyper-edge per invariant of the location.
                for (Invariant inv: comp.getInvariants()) {
                    Expression pred = inv.getPredicate();
                    VariableCollector varCollector = new VariableCollector();
                    Set<PositionObject> vars = set();
                    varCollector.collectCifVarObjs(pred, vars);
                    addHyperEdge(vars);
                }

                // Add hyper-edges for the edges of the CIF automaton.
                for (Edge edge: loc.getEdges()) {
                    addHyperEdges(aut, edge);
                }
            }
        }

        // Recursively add for child components.
        if (comp instanceof Group) {
            Group group = (Group)comp;
            for (Component child: group.getComponents()) {
                addHyperEdges((ComplexComponent)child);
            }
        }
    }

    /**
     * Add hyper edges for the given edge of a CIF automaton.
     *
     * @param aut The CIF automaton that contains the edge.
     * @param edge The edge.
     */
    private void addHyperEdges(Automaton aut, Edge edge) {
        // Add hyper-edge for each comparison in the guards.
        for (Expression guard: edge.getGuards()) {
            ComparisonCollector cmpCollector = new ComparisonCollector();
            List<BinaryExpression> cmps = cmpCollector.collectComparisons(guard);
            for (BinaryExpression cmp: cmps) {
                VariableCollector varCollector = new VariableCollector();
                Set<PositionObject> vars = set();
                varCollector.collectCifVarObjs(cmp.getLeft(), vars);
                varCollector.collectCifVarObjs(cmp.getRight(), vars);
                addHyperEdge(vars);
            }
        }

        // Add hyper-edges for updates.
        addHyperEdges(edge.getUpdates());

        // Collect information for hyper-edges to create for each event.
        Automaton lpAut = (aut.getLocations().size() < 2) ? null : aut;
        for (EdgeEvent edgeEvent: edge.getEvents()) {
            // Skip 'tau' events.
            Expression eventRef = edgeEvent.getEvent();
            if (eventRef instanceof TauExpression) {
                continue;
            }

            // Get variable objects already collected for the event.
            Event event = ((EventExpression)eventRef).getEvent();
            Set<PositionObject> vars = eventHyperEdges.get(event);
            if (vars == null) {
                vars = set();
                eventHyperEdges.put(event, vars);
            }

            // Add variable objects from guards and updates.
            VariableCollector varCollector = new VariableCollector();
            for (Expression guard: edge.getGuards()) {
                varCollector.collectCifVarObjs(guard, vars);
            }
            for (Update update: edge.getUpdates()) {
                varCollector.collectCifVarObjs(update, vars);
            }

            // Add location pointer variable (if applicable), as source location is always in guard, and target
            // location is assigned to the location pointer (for all but self loop edges).
            if (lpAut != null) {
                vars.add(lpAut);
            }
        }
    }

    /**
     * Add hyper edges for the given updates of a CIF automaton edge.
     *
     * @param updates The updates.
     */
    private void addHyperEdges(List<Update> updates) {
        for (Update update: updates) {
            // Skip all but assignments. Precondition checked elsewhere.
            if (!(update instanceof Assignment)) {
                continue;
            }
            Assignment asgn = (Assignment)update;

            // Add hyperedge per assignment.
            VariableCollector varCollector = new VariableCollector();
            Set<PositionObject> vars = set();
            varCollector.collectCifVarObjs(asgn.getAddressable(), vars);
            varCollector.collectCifVarObjs(asgn.getValue(), vars);
            addHyperEdge(vars);
        }
    }

    /**
     * Add a hyper-edge for the given CIF variable objects. Creating and adding a hyper-edge is skipped if no CIF
     * variable objects are provided.
     *
     * @param vars The CIF variable objects.
     */
    private void addHyperEdge(Set<PositionObject> vars) {
        // Skip creation of hyper-edges without any variables.
        if (vars.isEmpty()) {
            return;
        }

        // Create bit set.
        BitSet hyperEdge = new BitSet(variables.size());
        for (PositionObject var: vars) {
            int matchIdx = -1;
            for (int i = 0; i < variables.size(); i++) {
                // Get synthesis variable.
                SynthesisVariable synthVar = variables.get(i);
                Assert.notNull(synthVar == null);

                // Check for matching variable.
                boolean match = false;
                if (synthVar instanceof SynthesisDiscVariable) {
                    SynthesisDiscVariable sdv = (SynthesisDiscVariable)synthVar;
                    if (sdv.var == var) {
                        match = true;
                    }
                } else if (synthVar instanceof SynthesisInputVariable) {
                    SynthesisInputVariable siv = (SynthesisInputVariable)synthVar;
                    if (siv.var == var) {
                        match = true;
                    }
                } else if (synthVar instanceof SynthesisLocPtrVariable) {
                    SynthesisLocPtrVariable slpv = (SynthesisLocPtrVariable)synthVar;
                    if (slpv.aut == var) {
                        match = true;
                    }
                } else {
                    throw new RuntimeException("Unknown synthesis variable: " + synthVar);
                }

                // Done if match found.
                if (match) {
                    matchIdx = i;
                    break;
                }
            }

            // Must have found a match.
            Assert.check(matchIdx >= 0);
            hyperEdge.set(matchIdx);
        }

        // Add hyper-edge.
        hyperEdges.add(hyperEdge);
    }
}
