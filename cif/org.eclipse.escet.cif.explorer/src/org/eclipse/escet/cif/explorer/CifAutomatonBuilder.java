//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.explorer;

import static org.eclipse.escet.cif.common.CifValueUtils.makeTrue;
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
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.explorer.options.AutomatonNameOption;
import org.eclipse.escet.cif.explorer.runtime.BaseState;
import org.eclipse.escet.cif.explorer.runtime.EventUsage;
import org.eclipse.escet.cif.explorer.runtime.Explorer;
import org.eclipse.escet.cif.explorer.runtime.ExplorerEdge;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Alphabet;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.common.java.Assert;

/** Class for constructing a data-less automaton with the explored states. */
public class CifAutomatonBuilder {
    /** Mapping of old events to new events. Valid during the conversion. */
    private Map<Event, Event> evtMap = null;

    /**
     * Construct a CIF specification with a data-less automaton containing the explored state space.
     *
     * @param expl Explorer containing the explored states.
     * @param oldSpec Original specification, used as template for copying the event declarations.
     * @return The constructed CIF specification.
     */
    public Specification createAutomaton(Explorer expl, Specification oldSpec) {
        evtMap = map();

        // Copy the events from the original specification.
        Specification newSpec = newSpecification();
        makeEventGroups(oldSpec, newSpec);

        // Add the explored state space.
        addAutomaton(expl, newSpec);

        evtMap = null;
        return newSpec;
    }

    /**
     * Copy the events in the group structure of the {@code oldGrp} tree, pruning the empty branches.
     *
     * @param oldGrp Existing group tree.
     * @param newGrp Resulting new group tree.
     * @return Whether anything was added to the new group.
     */
    private boolean makeEventGroups(Group oldGrp, Group newGrp) {
        boolean nonEmpty = false;

        // Construct sub-groups.
        for (Component comp: oldGrp.getComponents()) {
            if (comp instanceof Group) {
                Group oldComp = (Group)comp;
                Group subGrp = newGroup();
                subGrp.setName(oldComp.getName());
                if (makeEventGroups(oldComp, subGrp)) {
                    newGrp.getComponents().add(subGrp);
                    nonEmpty = true;
                }
            }
            if (comp instanceof Automaton) {
                Automaton oldAut = (Automaton)comp;
                Group subGrp = newGroup();
                subGrp.setName(oldAut.getName());
                if (addEvents(subGrp, oldAut.getDeclarations())) {
                    newGrp.getComponents().add(subGrp);
                    nonEmpty = true;
                }
            }
        }

        nonEmpty |= addEvents(newGrp, oldGrp.getDeclarations());
        return nonEmpty;
    }

    /**
     * Add event declarations to the group.
     *
     * @param grp Group for adding event declarations.
     * @param decls Declarations to check.
     * @return Whether an event declaration was added.
     */
    private boolean addEvents(Group grp, List<Declaration> decls) {
        boolean nonEmpty = false;
        for (Declaration decl: decls) {
            if (decl instanceof Event) {
                Event oldEvent = (Event)decl;
                Boolean ctl = oldEvent.getControllable();
                Event newEvent = newEvent(null, ctl, oldEvent.getName(), null, null);
                grp.getDeclarations().add(newEvent);
                evtMap.put(oldEvent, newEvent);
                nonEmpty = true;
            }
        }
        return nonEmpty;
    }

    /**
     * Creates the resulting statespace automaton with the given name, in the root of the supplied specification.
     *
     * @param sugName Suggested name.
     * @param spec Specification to check.
     * @return The newly created automaton.
     */
    private static Automaton createResultAutomaton(String sugName, Specification spec) {
        // Create automaton.
        Automaton aut = newAutomaton();

        // Give new automaton a unique name.
        String name = sugName;
        Set<String> names = CifScopeUtils.getSymbolNamesForScope(spec, null);
        if (names.contains(name)) {
            name = CifScopeUtils.getUniqueName(name, names, Collections.emptySet());
            warn("Resulting statespace automaton is named \"%s\" instead of \"%s\" to avoid a naming conflict.", name,
                    sugName);
        }
        aut.setName(name);

        // Add automaton to the specification.
        spec.getComponents().add(aut);
        return aut;
    }

    /**
     * Add the explored states as a flat data-less automaton to the specification.
     *
     * <p>
     * Method assumes that {@link #evtMap} contains a mapping of explorer events to newSpec events.
     * </p>
     *
     * @param expl Explorer containing the explored states.
     * @param newSpec Destination specification.
     */
    private void addAutomaton(Explorer expl, Specification newSpec) {
        String name = AutomatonNameOption.getAutomatonName("statespace");
        Automaton aut = createResultAutomaton(name, newSpec);

        // Add alphabet.
        Alphabet alphabet = newAlphabet();
        aut.setAlphabet(alphabet);
        for (EventUsage evtUse: expl.eventUsages) {
            Event evt = evtMap.get(evtUse.event);
            EventExpression ee = newEventExpression(evt, null, newBoolType());
            alphabet.getEvents().add(ee);
        }

        // Special case for no initial state.
        if (expl.states == null || expl.states.isEmpty()) {
            Location loc = newLocation();
            aut.getLocations().add(loc); // Add single non-initial location.
            return;
        }

        // Normal case, first add all states, without adding edges.
        int idx = 0;
        for (BaseState state: expl.states.values()) {
            // Verify that the state number is equal to its index. This
            // property is used while adding edges below.
            Assert.check(state.stateNumber == idx + 1);

            Location loc = newLocation();
            loc.setName(fmt("loc%d", idx + 1));
            if (state.isInitial()) {
                loc.getInitials().add(makeTrue());
            }
            if (state.isMarked()) {
                loc.getMarkeds().add(makeTrue());
            }
            aut.getLocations().add(loc);
            idx++;
        }

        // Add edges.
        Location srcLoc, dstLoc;
        idx = 0;
        for (BaseState state: expl.states.values()) {
            srcLoc = aut.getLocations().get(idx++);
            for (ExplorerEdge explEdge: state.getOutgoingEdges()) {
                dstLoc = aut.getLocations().get(explEdge.next.stateNumber - 1);

                // Construct the edge.
                Edge edge = newEdge();
                if (srcLoc != dstLoc) {
                    edge.setTarget(dstLoc);
                }
                srcLoc.getEdges().add(edge);

                // Copy the event of the edge.
                if (explEdge.event != null) {
                    Event newEvent = evtMap.get(explEdge.event);
                    EventExpression e = newEventExpression(newEvent, null, newBoolType());

                    EdgeEvent ee = newEdgeEvent();
                    ee.setEvent(e);
                    edge.getEvents().add(ee);
                }
            }
        }
    }
}
