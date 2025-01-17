//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.bdd.workset.dependencies;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.bdd.spec.CifBddEdge;
import org.eclipse.escet.cif.bdd.spec.CifBddEdgeApplyDirection;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;

import com.github.javabdd.BDD;

/** BDD-based edge dependency set creator. */
public class BddBasedEdgeDependencySetCreator implements EdgeDependencySetCreator {
    @Override
    public void createAndStore(CifBddSpec cifBddSpec, boolean forwardEnabled) {
        // Compute which events may potentially follow which other events.
        int edgeCnt = cifBddSpec.edges.size();
        Map<Event, Set<Event>> followEvents = mapc(edgeCnt); // For each event, which events can follow it.
        for (CifBddEdge precedingEdge: cifBddSpec.orderedEdgesForward) {
            Event precedingEvent = precedingEdge.event;

            // Compute the states that can potentially be reached by this edge.
            BDD precedingEdgeReachableStates = precedingEdge.apply( //
                    cifBddSpec.factory.one(), // Apply edge to 'true' predicate.
                    CifBddEdgeApplyDirection.FORWARD, // Forward reachability.
                    null // No restriction.
            );

            // Compute which events may potentially follow the event of this edge.
            for (CifBddEdge followingEdge: cifBddSpec.orderedEdgesForward) {
                Event followingEvent = followingEdge.event;
                if (precedingEvent == followingEvent) {
                    continue; // Save computations by skipping self-dependencies (workset algorithm does not need them).
                }

                // Compute whether these events may potentially follow each other.
                BDD enabled = precedingEdgeReachableStates.and(followingEdge.guard);
                if (!enabled.isZero()) {
                    // Second edge can potentially follow the first edge.
                    followEvents.computeIfAbsent(precedingEvent, e -> set()).add(followingEvent);
                }
                enabled.free();
            }

            // Cleanup.
            precedingEdgeReachableStates.free();
        }

        // Compute and store the edge dependency sets, based on the event follows relation.
        cifBddSpec.worksetDependenciesBackward = create(followEvents, cifBddSpec.orderedEdgesBackward, false);
        if (forwardEnabled) {
            cifBddSpec.worksetDependenciesForward = create(followEvents, cifBddSpec.orderedEdgesForward, true);
        }
    }

    /**
     * Create the edge dependency sets, based on the event follows relation.
     *
     * @param followEvents The event follows relation. May be modified in-place.
     * @param edges The edges for which to create the dependency sets.
     * @param forward Whether to create the forward ({@code true}) or backward ({@code false}) dependency sets.
     * @return The workset dependency sets, one per edge, in the order of the edges as they are given.
     */
    private List<BitSet> create(Map<Event, Set<Event>> followEvents, List<CifBddEdge> edges, boolean forward) {
        // Consider each edge.
        List<BitSet> dependencies = listc(edges.size());
        for (CifBddEdge edge1: edges) {
            BitSet dependencies1 = new BitSet(edges.size());

            // Consider each other edge.
            for (int i = 0; i < edges.size(); i++) {
                CifBddEdge edge2 = edges.get(i);

                // Add dependency based on event follows relation.
                boolean isDependency;
                if (forward) {
                    isDependency = followEvents.computeIfAbsent(edge1.event, e -> set()).contains(edge2.event);
                } else {
                    isDependency = followEvents.computeIfAbsent(edge2.event, e -> set()).contains(edge1.event);
                }
                if (isDependency) {
                    dependencies1.set(i);
                }
            }

            // Add dependency set.
            dependencies.add(dependencies1);
        }
        return dependencies;
    }
}
