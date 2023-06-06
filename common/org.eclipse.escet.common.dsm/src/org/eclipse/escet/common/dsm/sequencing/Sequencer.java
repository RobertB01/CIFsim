//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.sequencing;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.trimLeft;
import static org.eclipse.escet.common.java.Strings.trimRight;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.escet.common.dsm.sequencing.graph.Cycle;
import org.eclipse.escet.common.dsm.sequencing.graph.Graph;
import org.eclipse.escet.common.dsm.sequencing.graph.GraphCreator;
import org.eclipse.escet.common.java.BitSetIterator;

/** Class for sequencing a graph or DSM. */
public class Sequencer {
    /** Constructor of the {@link Sequencer} class. */
    private Sequencer() {
        // Static class.
    }

    /** Pattern for matching a {@code (vertex-name, vertex-name)} pair. */
    private static final Pattern PAIR_PATTERN = Pattern.compile("[(]([^,)]+),([^)]+)[)]");

    /**
     * Load a file containing {@code (<name1>, <name2>)} directed edges of a graph.
     *
     * @param fpath The file path to load.
     * @return The loaded graph.
     */
    public static Graph loadVertexPairs(Path fpath) {
        Graph g = new Graph();
        GraphCreator creator = g.getGraphCreator();
        creator.setupCreation();

        try {
            Files.lines(fpath).forEachOrdered(line -> { addVertexPairs(creator, line); });
        } catch (IOException ex) {
            throw new RuntimeException("Could not read file \"" + fpath + "\".", ex);
        }

        creator.finishCreation();
        return g;
    }

    /**
     * Create a graph from a line with a sequence of directed edges, each of the form {@code (<name1>, <name2>)}.
     *
     * @param pairs Line of text with the edge pairs.
     * @return The loaded graph.
     */
    public static Graph loadVertexPairs(String pairs) {
        Graph g = new Graph();
        GraphCreator creator = g.getGraphCreator();
        creator.setupCreation();
        addVertexPairs(creator, pairs);
        creator.finishCreation();
        return g;
    }

    /**
     * Parse a line of text with a sequence of directed edges, each of the form {@code (<name1>, <name2>)}, and add the
     * edges to the graph.
     *
     * @param creator Storage for the found edges.
     * @param line Line of text to parse.
     */
    private static void addVertexPairs(GraphCreator creator, String line) {
        Matcher m = PAIR_PATTERN.matcher(line);
        while (m.find()) {
            String sourceName = trimLeft(trimRight(m.group(1)));
            String targetName = trimLeft(trimRight(m.group(2)));
            creator.addEdge(sourceName, targetName);
        }
    }

    /**
     * Perform Sequencing ofthe provided graph.
     *
     * @param g Graph to sequence.
     * TODO Return a result.
     */
    public static void sequenceGraph(Graph g) {
        // Find simple cycles (cycles that don't cross themselves).
        GraphCycleFinder cycleFinder = new GraphCycleFinder();
        Set<Cycle> foundCycles = cycleFinder.findSimpleCycles(g);

        // Group the found cycles in collections of related cycles (cycles that share at least one vertex).
        List<List<Cycle>> cycleCollections = makeCycleCollections(foundCycles);
    }

    /**
     * Construct zero or more collections of related cycles.
     *
     * <p>
     * A cycle is related to another cycle if they share at least one vertex. In addition, all cycles are directly or
     * indirectly linked to each other, you cannot partition the collection into 2 sets such that there is no relation
     * between any pair of cycles from different sets.
     * </p>
     *
     * <p>
     * This in turn implies that each vertex is related to at most one collection of related cycles.
     * </p>
     *
     * @param cycles Cycles to organize into collections.
     * @return The found collections.
     */
    static List<List<Cycle>> makeCycleCollections(Set<Cycle> cycles) {
        // Invariant: Each vertex points to the set of related cycles that have it.
        Map<Integer, Set<Cycle>> collectionMap = map();

        for (Cycle cycle: cycles) {
            // The set of related cycles for vertices of 'cycle'. The collection gets selected or expanded while
            // iterating over the vertices of the cycle.
            Set<Cycle> cycleCollection = null;

            // Try to find already existing collections of cycles that also relate to a vertex of 'cycle'.
            for (int cycleVertex: new BitSetIterator(cycle.vertices)) {
                Set<Cycle> existingCollection = collectionMap.get(cycleVertex);
                if (existingCollection == null || existingCollection == cycleCollection) {
                    // The vertex is either not in any existing collection, or it is already in the 'cycleCollection'
                    // due to a previous iteration. In both cases, nothing needs to be done as 'cycle' will be added
                    // to 'cycleCollection' after the search.
                    continue;
                }

                // Situation: 'existingCollection' is a different set that is related to the new 'cycle'. A merge must
                // happen.

                // No 'cycleCollection' yet, so pick 'existingCollection' as set to add the cycle to.
                if (cycleCollection == null) {
                    cycleCollection = existingCollection;
                    continue;
                }

                // We have both an 'existingCollection' and a 'cycleCollection'. Was the 'existingCollection' copied
                // already for another vertex in 'cycle'?
                //
                // One occurrence of this is:
                // - First vertex picks set X as the cycleCollection.
                // - Second vertex finds set Y as related, and Y gets copied into X (see below).
                // - Third vertex finds set Y as related. Copying is allowed but not needed.
                //
                // Check by testing existence of one cycle from the existing collection.
                if (cycleCollection.contains(existingCollection.iterator().next())) {
                    continue;
                }

                // The 'existingCollection' has a new relation. Merge both collections while minimizing copying.
                //
                // This may invalidate some of the vertex keys in 'collectionMap', but we don't care about those keys
                // anymore as all their cycles are already included in the 'cycleCollection' result of 'cycle'. Vertex
                // keys get repaired after the loop.
                if (existingCollection.size() < cycleCollection.size()) {
                    cycleCollection.addAll(existingCollection);
                } else {
                    existingCollection.addAll(cycleCollection);
                    cycleCollection = existingCollection;
                }
            }

            // Add 'cycle' to the 'cycleCollection'.
            if (cycleCollection == null) {
                // If 'cycleCollection' is still 'null' at this point, 'cycle' is completely disjunct and should become
                // a collection by itself.
                cycleCollection = set();
            }
            cycleCollection.add(cycle); // Add 'cycle' itself as well to its collection.

            // At this point we have a 'cycleCollection' for 'cycle', possibly while borrowing or changing other
            // collections. However all cycles in those touched collections have ended up in 'cycleCollection'. The
            // 'collectionMap' invariant is restored now by making the other copied or modified collections unreachable.
            for (Cycle c: cycleCollection) {
                for (int cVertex: new BitSetIterator(c.vertices)) {
                    collectionMap.put(cVertex, cycleCollection); // Point all vertices of the new collection to it.
                }
            }
        }

        // Grab all surviving collections from 'collectionMap'. As each key points to a set, there are a lot of
        // duplicate values which needs to be cleaned up.
        Set<Set<Cycle>> uniqueCollections = set();
        List<List<Cycle>> result = list();
        for (Set<Cycle> collection: collectionMap.values()) {
            if (uniqueCollections.add(collection)) {
                result.add(set2list(collection));
            }
        }
        return result;
    }
}
