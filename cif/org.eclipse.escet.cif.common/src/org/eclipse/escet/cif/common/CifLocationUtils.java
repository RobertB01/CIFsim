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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifEventUtils.getEventFromEdgeEvent;
import static org.eclipse.escet.cif.common.CifValueUtils.isTriviallyFalse;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.AlgVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ComponentExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.ContVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.DiscVariableExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.expressions.LocationExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.SelfExpression;

/** CIF location utility methods. */
public class CifLocationUtils {
    /** Constructor for the {@link CifLocationUtils} class. */
    private CifLocationUtils() {
        // Static class.
    }

    /**
     * Returns the automaton that contains the given location.
     *
     * @param loc The location.
     * @return The automaton.
     */
    public static Automaton getAutomaton(Location loc) {
        return (Automaton)loc.eContainer();
    }

    /**
     * Returns the name of the location, or {@code "*"} in case it has no name (is the only location of its automaton).
     *
     * @param loc The location.
     * @return The name of the location, or {@code "*"}.
     */
    public static String getName(Location loc) {
        String name = loc.getName();
        return (name == null) ? "*" : name;
    }

    /**
     * Returns the locations of the given automaton that could be initial locations. Since initialization could be
     * restricted by initialization predicates outside of the locations, we don't know for sure whether the returned
     * locations are truly initial states.
     *
     * <p>
     * This overload does not work for initialization predicates in locations, with variables that have cycles.
     * </p>
     *
     * @param aut The automaton for which to obtain the locations.
     * @return The locations that could be initial locations.
     */
    public static Set<Location> getPossibleInitialLocs(Automaton aut) {
        return getPossibleInitialLocs(aut, true);
    }

    /**
     * Returns the locations of the given automaton that could be initial locations. Since initialization could be
     * restricted by initialization predicates outside of the locations, we don't know for sure whether the returned
     * locations are truly initial states.
     *
     * <p>
     * This overload works for initialization predicates in locations, with objects that have cycles. If
     * {@code checkRefs} is {@code false}, objects that can have cycles are assumed to not have a single value. Such
     * initialization predicates as a whole are considered not to have a single value, and are thus not evaluated.
     * </p>
     *
     * @param aut The automaton for which to obtain the locations.
     * @param checkRefs Whether objects that can have a cycle may be checked for having a single value, and evaluated if
     *     they have a single value.
     * @return The locations that could be initial locations.
     */
    public static Set<Location> getPossibleInitialLocs(Automaton aut, boolean checkRefs) {
        Set<Location> rslt = set();

        LOC_LOOP:
        for (Location loc: aut.getLocations()) {
            // Skip locations with trivially false initial predicates. If no
            // such predicates, the default is 'false'.
            List<Expression> initials = loc.getInitials();
            if (initials.isEmpty()) {
                continue;
            }

            // Check each of the initialization predicates. If one of them is
            // 'false', the entire feature is 'false'.
            PRED_LOOP:
            for (Expression initial: initials) {
                // Check for objects that can have a cycle. If found, skip this
                // initialization predicate.
                if (!checkRefs) {
                    List<Expression> refExprs = list();
                    CifScopeUtils.collectRefExprs(initial, refExprs);
                    for (Expression refExpr: refExprs) {
                        if (refExpr instanceof DiscVariableExpression || refExpr instanceof ContVariableExpression
                                || refExpr instanceof AlgVariableExpression || refExpr instanceof LocationExpression
                                || refExpr instanceof ComponentExpression || refExpr instanceof SelfExpression)
                        {
                            continue PRED_LOOP;
                        }
                    }
                }

                // If trivially false, then not an initial location.
                if (isTriviallyFalse(initial, true, true)) {
                    continue LOC_LOOP;
                }
            }

            // This could be an initial state. We don't know for sure, as the
            // initial predicates is not be statically true/false, or it
            // contains references to objects that we may not check. As such,
            // it may be restricted somewhere else in the specification, or we
            // simply can't determine the value.
            rslt.add(loc);
        }

        return rslt;
    }

    /**
     * Get the edges associated with a given event from a given location.
     *
     * @param loc Location to search.
     * @param evt Event to match, {@code null} means 'tau' event.
     * @return Edges with the given event from the given location.
     */
    public static List<Edge> getEdges(Location loc, Event evt) {
        List<Edge> edges = list();
        for (Edge edge: loc.getEdges()) {
            if (evt == null && edge.getEvents().isEmpty()) {
                edges.add(edge); // Add tau event.
                continue;
            }

            for (EdgeEvent edgeEvent: edge.getEvents()) {
                if (getEventFromEdgeEvent(edgeEvent) == evt) {
                    edges.add(edge);
                }
            }
        }
        return edges;
    }

    /**
     * Get the successor locations associated with a given event from a given location.
     *
     * @param loc Location to search.
     * @param evt Event to match, {@code null} means 'tau' event.
     * @return Successor locations after traversing an edge with the given event from the given location.
     */
    public static Set<Location> getNextLocations(Location loc, Event evt) {
        List<Edge> edges = getEdges(loc, evt);
        Set<Location> nextLocs = setc(edges.size());
        for (Edge edge: edges) {
            nextLocs.add((edge.getTarget() == null) ? loc : edge.getTarget());
        }
        return nextLocs;
    }

    /**
     * Get the successor locations associated with a given event from a given set of locations.
     *
     * @param locs Locations to search.
     * @param evt Event to match, {@code null} means 'tau' event.
     * @return Successor locations after traversing an edge with the given event from the given locations.
     */
    public static Set<Location> getNextLocations(Set<Location> locs, Event evt) {
        Set<Location> nextLocs = set();
        for (Location loc: locs) {
            nextLocs.addAll(getNextLocations(loc, evt));
        }
        return nextLocs;
    }
}
