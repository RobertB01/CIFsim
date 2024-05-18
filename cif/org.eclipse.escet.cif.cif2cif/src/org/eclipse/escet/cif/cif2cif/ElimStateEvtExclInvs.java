//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newAutomaton;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBinaryExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newBoolType;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdge;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEdgeEvent;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newEventExpression;
import static org.eclipse.escet.cif.metamodel.java.CifConstructors.newLocation;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.escet.cif.common.CifGuardUtils;
import org.eclipse.escet.cif.common.CifScopeUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.ComplexComponent;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.InvKind;
import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.SupKind;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.BinaryOperator;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/**
 * In-place transformation that replaces state/event exclusion invariants by automata with self loops.
 *
 * <p>
 * Precondition: Specifications with component definitions/instantiations are currently not supported.
 * </p>
 *
 * <p>
 * For named state/event exclusion invariants in groups (including the root of the specification), their corresponding
 * automata are created within those same groups. For unnamed state/event exclusion invariants in groups (including the
 * root of the specification), one new automaton is created per supervisory kind. For state/event exclusion invariants
 * in automata, the new automata are created as siblings of the original automaton.
 * </p>
 *
 * <p>
 * This transformation introduces location reference expressions, for state/event exclusion invariants specified in
 * locations of automata with more than one location. To eliminate them, apply the {@link ElimLocRefExprs}
 * transformation after this transformation.
 * </p>
 *
 * <p>
 * This transformation may generate boolean implication and inversion predicates. These are not simplified. To simply
 * them, apply the {@link SimplifyValues} transformation after this transformation.
 * </p>
 *
 * <p>
 * The annotations of unnamed state/event exclusion invariants are removed. The annotations of named state/event
 * exclusion invariants are moved to the corresponding newly created automata.
 * </p>
 *
 * @see ElimLocRefExprs
 * @see SimplifyValues
 */
public class ElimStateEvtExclInvs implements CifToCifTransformation {
    @Override
    public void transform(Specification spec) {
        // Check no component definition/instantiation precondition.
        if (CifScopeUtils.hasCompDefInst(spec)) {
            String msg = "Eliminating state/event exclusion invariants for a CIF specification with component "
                    + "definitions is currently not supported.";
            throw new CifToCifPreconditionException(msg);
        }

        // Perform actual transformation.
        elimStateEvtExclInvs(spec);
    }

    /**
     * Eliminates state/event exclusion invariants from the given component, recursively.
     *
     * @param comp The component.
     */
    private void elimStateEvtExclInvs(ComplexComponent comp) {
        // Get state/event exclusion invariants of the component.
        List<Invariant> compInvs = filterInvs(comp.getInvariants());

        // Get state/event exclusion invariants of the locations, for automata.
        List<Invariant> locInvs = list();
        if (comp instanceof Automaton) {
            Automaton aut = (Automaton)comp;
            for (Location loc: aut.getLocations()) {
                List<Invariant> invs = filterInvs(loc.getInvariants());
                modifyLocInvs(aut, invs);
                locInvs.addAll(invs);
            }
        }

        // Combine invariants.
        List<Invariant> invs = concat(compInvs, locInvs);

        // Get named and unnamed invariants.
        List<Invariant> namedInvs = invs.stream().filter(inv -> inv.getName() != null).toList();
        List<Invariant> unnamedInvs = invs.stream().filter(inv -> inv.getName() == null).toList();

        // Create an automaton for each named state/event exclusion invariant.
        for (Invariant inv: namedInvs) {
            createAutomaton(comp, list(inv));
        }

        // If we have any unnamed state/event exclusion invariants, create an automaton per supervisory kind.
        if (!invs.isEmpty()) {
            List<List<Invariant>> supKindsInvs = partitionPerSupKind(unnamedInvs);
            for (List<Invariant> supKindInvs: supKindsInvs) {
                createAutomaton(comp, supKindInvs);
            }
        }

        // Remove the state/event exclusion invariants from the model.
        removeInvs(comp.getInvariants());

        if (comp instanceof Automaton) {
            Automaton aut = (Automaton)comp;
            for (Location loc: aut.getLocations()) {
                removeInvs(loc.getInvariants());
            }
        }

        // Apply recursively, for groups.
        if (comp instanceof Group) {
            // Copy list of child components, as siblings may be added by the elimination.
            List<Component> children = copy(((Group)comp).getComponents());
            for (Component child: children) {
                elimStateEvtExclInvs((ComplexComponent)child);
            }
        }
    }

    /**
     * Filters invariants to only state/event exclusion invariants. That is, state invariants are filtered out.
     *
     * @param invs The invariants.
     * @return The state/event exclusion invariants.
     */
    private List<Invariant> filterInvs(List<Invariant> invs) {
        List<Invariant> filtered = list();
        for (Invariant inv: invs) {
            if (inv.getInvKind() != InvKind.STATE) {
                filtered.add(inv);
            }
        }
        return filtered;
    }

    /**
     * Modifies invariants from locations to make their predicates conditional on the location. Only modifies them if
     * the automaton has at least two locations, as for automata with a single location, the location is always the
     * current location.
     *
     * @param aut The automaton.
     * @param invs The invariants from a location of the automaton. Modified in-place.
     */
    private void modifyLocInvs(Automaton aut, List<Invariant> invs) {
        // If only one location, no need to make conditional on the location.
        if (aut.getLocations().size() == 1) {
            return;
        }

        // Make conditional on the location. At least two locations, so they all have a name.
        for (Invariant inv: invs) {
            // Replace 'pred' by 'loc => pred'.
            Location loc = (Location)inv.eContainer();
            Expression locRef = CifGuardUtils.LocRefExprCreator.DEFAULT.create(loc);
            Expression invPred = inv.getPredicate();
            BinaryExpression pred = newBinaryExpression();
            pred.setOperator(BinaryOperator.IMPLICATION);
            pred.setLeft(locRef);
            pred.setRight(invPred);
            pred.setType(newBoolType());
            inv.setPredicate(pred);
        }
    }

    /**
     * Partitions invariants per supervisory kind.
     *
     * @param invs The invariants.
     * @return The invariants, per supervisory kind.
     */
    private List<List<Invariant>> partitionPerSupKind(List<Invariant> invs) {
        // Construct mapping from supervisory kinds to the invariants with that supervisory kind.
        Map<SupKind, List<Invariant>> invMap = map();
        for (Invariant inv: invs) {
            List<Invariant> kindInvs = invMap.get(inv.getSupKind());
            if (kindInvs == null) {
                kindInvs = list();
                invMap.put(inv.getSupKind(), kindInvs);
            }
            kindInvs.add(inv);
        }

        // Return lists of invariants with the same supervisory kinds.
        return new ArrayList<>(invMap.values());
    }

    /**
     * Creates an automaton with self loops for the given state/exclusion invariants.
     *
     * @param comp The component from which the invariants originate, or the automaton that contains the location from
     *     which the invariants originate. If it is the specification or a group, a new automaton is created within it.
     *     If it is an automaton, a new automaton is created as its sibling.
     * @param invs The invariants. There must be at least one invariant. If the invariant is named, it must be the only
     *     invariant. Otherwise, all the invariants must have the same supervisory kind.
     */
    private void createAutomaton(ComplexComponent comp, List<Invariant> invs) {
        // Get information about the invariants.
        boolean invsHaveName = first(invs).getName() != null;
        SupKind supKind = first(invs).getSupKind();

        // Check preconditions.
        Assert.implies(invsHaveName, invs.size() == 1);
        Assert.implies(!invsHaveName, invs.stream().allMatch(inv -> inv.getSupKind() == first(invs).getSupKind()));

        // Get parent group, or specification itself for the root.
        PositionObject parent = (comp instanceof Group) ? comp : CifScopeUtils.getScope(comp);
        Group group = (Group)parent;

        // Determine proposed automaton name.
        String name;
        if (invsHaveName && comp instanceof Group) {
            // Use name of the invariant.
            name = first(invs).getName();
        } else if (invsHaveName) {
            // Use name of the invariant, prefixed with name of the automaton.
            name = CifTextUtils.getName(comp) + first(invs).getName();
        } else {
            // Use the name of the component, the supervisory kind, and a fixed text.
            name = (comp instanceof Group) ? "" : CifTextUtils.getName(comp);
            if (supKind != SupKind.NONE) {
                String txt = supKind.getName().toLowerCase(Locale.US);
                txt = StringUtils.capitalize(txt);
                name += txt;
            }
            name += "StateEvtExcls";
        }

        // Avoid naming conflicts.
        if (invsHaveName && comp instanceof Group) {
            // Invariants in groups already have a unique name.
        } else {
            // Invariants in an automaton become siblings of the automaton, so they may conflict within that scope.
            // Automata for unnamed invariants get a new name, which may also conflict.
            Set<String> usedNames = CifScopeUtils.getSymbolNamesForScope(parent, null);
            if (usedNames.contains(name)) {
                name = CifScopeUtils.getUniqueName(name, usedNames, Collections.emptySet());
            }
        }

        // Create and add automaton.
        Automaton aut = newAutomaton();
        group.getComponents().add(aut);
        aut.setName(name);
        aut.setKind(supKind);

        // Move annotations of named invariant to the automaton.
        if (invsHaveName) {
            aut.getAnnotations().addAll(first(invs).getAnnotations());
        }

        // Add location to the automaton.
        Location loc = newLocation();
        aut.getLocations().add(loc);
        loc.getInitials().add(CifValueUtils.makeTrue());
        loc.getMarkeds().add(CifValueUtils.makeTrue());

        // Get mapping from events to invariants.
        Map<Event, List<Invariant>> evtMap = map();
        for (Invariant inv: invs) {
            Event event = ((EventExpression)inv.getEvent()).getEvent();
            List<Invariant> evtInvs = evtMap.get(event);
            if (evtInvs == null) {
                evtInvs = list();
                evtMap.put(event, evtInvs);
            }
            evtInvs.add(inv);
        }

        // Add edges to the automaton, per event. We need to combine the guards for each event, to ensure 'and'
        // semantics, as different edges for the same event have 'or' semantics.
        for (Entry<Event, List<Invariant>> entry: evtMap.entrySet()) {
            // Get event and invariants.
            Event event = entry.getKey();
            List<Invariant> evtInvs = entry.getValue();

            // Get guards.
            List<Expression> guards = listc(evtInvs.size());
            for (Invariant inv: evtInvs) {
                Expression guard = inv.getPredicate();
                if (inv.getInvKind() == InvKind.EVENT_DISABLES) {
                    guard = CifValueUtils.makeInverse(guard);
                }
                Assert.notNull(guard);
                guards.add(guard);
            }

            // Add edge.
            Edge edge = newEdge();
            loc.getEdges().add(edge);
            edge.getGuards().addAll(guards);

            EventExpression evtRef = newEventExpression();
            evtRef.setEvent(event);
            evtRef.setType(newBoolType());

            EdgeEvent edgeEvent = newEdgeEvent();
            edge.getEvents().add(edgeEvent);
            edgeEvent.setEvent(evtRef);
        }
    }

    /**
     * Removes state/event exclusion invariants from the model.
     *
     * @param invs Invariants contained somewhere in the model. The state/event exclusion invariants are removed from
     *     it, in-place.
     */
    private void removeInvs(EList<Invariant> invs) {
        Iterator<Invariant> invsIter = invs.iterator();
        while (invsIter.hasNext()) {
            Invariant inv = invsIter.next();
            if (inv.getInvKind() != InvKind.STATE) {
                invsIter.remove();
            }
        }
    }
}
