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

package org.eclipse.escet.cif.eventbased.apps.conversion;

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAlphabet;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newGroup;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newSpecification;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.eventbased.automata.OutgoingEdgeIterator;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;

/** Convert an event-based automaton back to a CIF metamodel instance. */
public class ConvertFromEventBased {
    /** Unique suffix number for creating unique state names. */
    private int number = 1;

    /** Mapping of converted locations. */
    Map<org.eclipse.escet.cif.eventbased.automata.Location, Location> locMap;

    /**
     * Convert event-based automaton to a CIF specification.
     *
     * @param aut Automaton to convert.
     * @param resultName Name of the resulting automaton.
     * @return CIF specification containing the converted automaton and its events.
     */
    public Specification convertAutomaton(org.eclipse.escet.cif.eventbased.automata.Automaton aut, String resultName) {
        Specification spec = newSpecification();
        // Convert and add events.
        Map<org.eclipse.escet.cif.eventbased.automata.Event, Event> eventMap;
        eventMap = convertEvents(aut.alphabet, spec);

        // Convert the event-based automaton.
        Automaton cifAut = newAutomaton();
        SupKind kind = null; // Dummy initialization.
        switch (aut.kind) {
            case PLANT:
                kind = SupKind.PLANT;
                break;
            case REQUIREMENT:
                kind = SupKind.REQUIREMENT;
                break;
            case SUPERVISOR:
                kind = SupKind.SUPERVISOR;
                break;
            case UNKNOWN:
                kind = SupKind.NONE;
                break;
        }
        cifAut.setKind(kind);

        Set<String> names = CifScopeUtils.getSymbolNamesForScope(spec, null);
        if (names.contains(resultName)) {
            resultName = CifScopeUtils.getUniqueName(resultName, names, names);
        }
        cifAut.setName(resultName);
        spec.getComponents().add(cifAut);

        // Add the alphabet to the automaton.
        Alphabet alphabet = newAlphabet();
        for (org.eclipse.escet.cif.eventbased.automata.Event evt: aut.alphabet) {
            EventExpression ee = newEventExpression(eventMap.get(evt), null, newBoolType());
            alphabet.getEvents().add(ee);
        }
        cifAut.setAlphabet(alphabet);

        // No locations, make a valid CIF specification for this
        // case (single location without 'initial true' predicate).
        // In addition, it may be useful to warn the user about the useless
        // result in the application wrapper code.
        if (aut.locations == null) {
            Location cifSrc = newLocation();
            cifAut.getLocations().add(cifSrc);
            return spec;
        }

        // Add locations and edges.
        number = 1;
        locMap = mapc(aut.size());
        for (org.eclipse.escet.cif.eventbased.automata.Location loc: aut) {
            // Find or create the location.
            Location cifSrc = getLocation(loc, aut.initial, cifAut);

            OutgoingEdgeIterator edgeIter = loc.getOutgoing();
            for (org.eclipse.escet.cif.eventbased.automata.Edge edge: edgeIter) {
                Location cifDest = null;
                if (loc != edge.dstLoc) {
                    cifDest = getLocation(edge.dstLoc, aut.initial, cifAut);
                }

                Event event = eventMap.get(edge.event);
                EdgeEvent edv;
                Expression ee = newEventExpression(event, null, newBoolType());
                edv = newEdgeEvent(ee, null);
                Edge cifEdge = newEdge(list(edv), null, null, cifDest, null, false);
                cifSrc.getEdges().add(cifEdge);
            }
        }
        return spec;
    }

    /**
     * Convert the alphabet of the event-based result to event declarations in CIF.
     *
     * @param alphabet Alphabet of the event-based computation result.
     * @param spec CIF specification to store the event declarations.
     * @return Mapping of event-based events to the CIF declarations.
     */
    private Map<org.eclipse.escet.cif.eventbased.automata.Event, Event>
            convertEvents(Set<org.eclipse.escet.cif.eventbased.automata.Event> alphabet, Specification spec)
    {
        Map<org.eclipse.escet.cif.eventbased.automata.Event, Event> eventMap;
        eventMap = mapc(alphabet.size());

        Map<GroupTree, Group> groupTree = map();
        for (org.eclipse.escet.cif.eventbased.automata.Event evt: alphabet) {
            Group current = spec;
            String name = evt.name;
            // Split the event name on '.', and make each level a group.
            int i = 0;
            while (true) {
                int j = name.indexOf('.', i);
                if (j == -1) {
                    break;
                }
                GroupTree tree = new GroupTree(current, name.substring(i, j));
                Group child = groupTree.get(tree);
                if (child == null) {
                    child = newGroup();
                    child.setName(name.substring(i, j));
                    groupTree.put(tree, child);
                    current.getComponents().add(child);
                }
                current = child;
                i = j + 1;
            }

            Event event;
            switch (evt.contr) {
                case CONTR_EVENT:
                    event = newEvent(true, name.substring(i), null, null);
                    break;

                case PLAIN_EVENT:
                    event = newEvent(null, name.substring(i), null, null);
                    break;

                case UNCONTR_EVENT:
                    event = newEvent(false, name.substring(i), null, null);
                    break;

                default:
                    Assert.fail("Unexpected kind of controllability");
                    event = null; // Not reached.
            }
            eventMap.put(evt, event);
            current.getDeclarations().add(event);
        }
        return eventMap;
    }

    /**
     * Retrieve or create a CIF location for event-based location 'loc'.
     *
     * @param loc Location in the event-based synthesis tools.
     * @param initLoc Initial location in the event-based automaton.
     * @param cifAut CIF automaton containing the CIF locations.
     * @return The retrieved or created CIF location representing 'loc'.
     */
    private Location getLocation(org.eclipse.escet.cif.eventbased.automata.Location loc,
            org.eclipse.escet.cif.eventbased.automata.Location initLoc, Automaton cifAut)
    {
        Location cifLoc = locMap.get(loc);
        if (cifLoc != null) {
            return cifLoc;
        }

        String name = "s" + Integer.toString(number);
        number++;

        cifLoc = newLocation();
        cifLoc.setName(name);
        if (loc == initLoc) {
            cifLoc.getInitials().add(CifValueUtils.makeTrue());
        }
        if (loc.marked) {
            cifLoc.getMarkeds().add(CifValueUtils.makeTrue());
        }
        cifAut.getLocations().add(cifLoc);
        locMap.put(loc, cifLoc);
        return cifLoc;
    }
}
