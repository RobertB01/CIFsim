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

import static org.eclipse.escet.common.java.BitSets.copy;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.common.dsm.sequencing.elements.CollectionElement;
import org.eclipse.escet.common.dsm.sequencing.elements.Element;
import org.eclipse.escet.common.dsm.sequencing.elements.SingularElement;
import org.eclipse.escet.common.dsm.sequencing.graph.Cycle;
import org.eclipse.escet.common.dsm.sequencing.graph.Edge;
import org.eclipse.escet.common.dsm.sequencing.graph.Graph;
import org.eclipse.escet.common.dsm.sequencing.graph.Vertex;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSetIterator;

/** Class for sequencing a graph or DSM. */
public class Sequencer {
    /** Constructor of the {@link Sequencer} class. */
    private Sequencer() {
        // Static class.
    }

    /**
     * Perform sequencing of the provided graph.
     *
     * @param g Graph to sequence.
     * @param collections If not {@code null}, the list is in-place extended with collections of vertex indices for each
     *     strongly connected component in the graph.
     * @return The vertices in sequenced order.
     */
    public static List<Vertex> sequenceGraph(Graph g, List<BitSet> collections) {
        // Find simple cycles (cycles that don't cross themselves).
        GraphCycleFinder cycleFinder = new GraphCycleFinder();
        Set<Cycle> foundCycles = cycleFinder.findSimpleCycles(g);

        // Group the found cycles in collections of related cycles (cycles that share at least one vertex).
        List<List<Cycle>> cycleCollections = makeCycleCollections(foundCycles);

        // Break cycles of each collection with a tearing edges heuristic, and store the collections in collection
        // elements as the first step in sequencing the graph.
        List<Element> elements = list(); // Storage for the created collection elements.
        BitSet storedVertices = new BitSet(); // Vertices of all stored elements.
        for (List<Cycle> collection: cycleCollections) {
            // Break all cycles in the collection.
            tearCycles(collection);

            // To prepare for topological sorting, convert collections to collection elements, and store them.
            CollectionElement collectionElement = makeCollectionElement(collection, g.vertices);
            collectionElement.setVertexIndices(storedVertices);
            elements.add(collectionElement);
        }

        // The second step in sequencing the graph is to walk over the vertices of the graph, and create a singular
        // element for each of the remaining vertices. Since these vertices are not involved in a cycle, they don't have
        // cyclic dependencies.
        int vertexIndex = 0;
        List<Vertex> graphVertices = g.vertices;
        for (Vertex vertex: graphVertices) {
            if (!storedVertices.get(vertexIndex)) { // Vertex is not stored as element yet.
                storedVertices.set(vertexIndex);
                BitSet inputs = new BitSet(graphVertices.size());
                for (Edge e: vertex.inputs) {
                    inputs.set(e.producingVertex);
                }
                BitSet outputs = new BitSet(graphVertices.size());
                for (Edge e: vertex.outputs) {
                    outputs.set(e.consumingVertex);
                }
                SingularElement element = new SingularElement(vertex, inputs, outputs);
                elements.add(element);
            }
            vertexIndex++;
        }

        // Sequence the entire graph.
        //
        // There are no cycles here, since all their vertices are hidden inside the collection elements. The vertices
        // inside each collection element have already been ordered internally and can be copied as-is into the final
        // vertices list.

        // Do a sanity check.
        int numVertices = 0;
        BitSet vertices = new BitSet();
        for (Element elem: elements) {
            elem.setVertexIndices(vertices);
            numVertices += elem.getVertexCount();
        }
        Assert.check(numVertices == g.vertices.size()); // Check all graph vertices have been added.
        Assert.check(numVertices == vertices.cardinality());

        // Order the elements.
        Element[] orderedElements = new Element[elements.size()];
        orderElements(elements.toArray(new Element[0]), vertices, orderedElements);

        // Expand the ordered elements to their vertices.
        List<Vertex> result = listc(numVertices);
        for (Element elem: orderedElements) {
            if (collections != null && elem instanceof CollectionElement ce) {
                BitSet verticesSet = new BitSet();
                collections.add(verticesSet);
                for (SingularElement element: ce.containedElements) {
                    verticesSet.set(element.vertex.number);
                }
            }
            elem.appendVertices(result);
        }
        return result;
    }

    /**
     * Construct zero or more collections of related cycles.
     *
     * <p>
     * A collection contains all cycles that directly or indirectly (via other cycles) share at least one vertex. In
     * other words, collections are disjoint partitions of cycles.
     * </p>
     *
     * <p>
     * Each vertex in a cycle is used in exactly one collection. If two cycles share a vertex they are related,
     * and thus both cycles must be in the same collection.
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
                // - First vertex picks set X as the 'cycleCollection'.
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

    /**
     * Tear all cycles in the given collection by heuristically cutting a (hopefully small) number of edges.
     *
     * <p>
     * Heuristic has been published in: A. Kusiak and J.Wang, "Efficient organizing of design activities", International
     * Journal of Production Research, volume 31, issue 4, pages 753-769, 1993,
     * doi:<a href="https://doi.org/10.1080/00207549308956755">10.1080/00207549308956755</a>.
     * </p>
     *
     * <p>
     * The paper has no pseudo-code. It explains the general idea, and then shows the decisions that are made in the
     * sequencing process for a number of example graphs, with the tearing decisions (they name it "triangularization")
     * at edge-selection detail level.
     * </p>
     *
     * <p>
     * This implementation swaps the tearing and topological sort steps, but these are independent steps and thus the
     * swap does not affect the result.
     * </p>
     *
     * <p>
     * This code assumes that all cycles share their {@link Edge} instances. This allows toggling the
     * {@link Edge#teared} flag for all cycles with that edge with a single assignment.
     * </p>
     *
     * @param collection Collection of cycles to tear.
     */
    @SuppressWarnings("null")
    static void tearCycles(List<Cycle> collection) {
        Map<Edge, BitSet> edgeCounts = map(); // Edges with indices of cycles that have it.

        // Store all edges in the map.
        int cycleNum = 0;
        for (Cycle cycle: collection) {
            for (Edge edge: cycle.edges) {
                Assert.check(!edge.teared); // Assumption: Edges are not teared already.
                BitSet cyclesWithEdge = edgeCounts.computeIfAbsent(edge, e -> new BitSet(collection.size()));
                cyclesWithEdge.set(cycleNum); // Add the cycle to the edge.
            }
            cycleNum++;
        }

        // Repeatedly find a good edge to cut.
        BitSet tearedCycles = new BitSet(collection.size()); // Contains indices of cycles that have been teared.
        while (tearedCycles.cardinality() < collection.size()) {
            // Some cycles have not been teared yet.

            // The heuristic is to tear an edge that is used by as many non-teared cycles as possible.
            Edge bestEdge = null; // Best edge to tear.
            BitSet edgeCycles = null; // Non-teared cycles that are affected if 'bestEdge' is teared.
            int bestEdgeCount = -1; // Number of uses of 'bestEdge'.
            Iterator<Entry<Edge, BitSet>> iter = edgeCounts.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Edge, BitSet> entry = iter.next();
                BitSet cyclesWithEdge = entry.getValue();
                cyclesWithEdge.andNot(tearedCycles); // Remove already teared cycles from the 'edgeCounts' entry.

                int count = cyclesWithEdge .cardinality();
                if (count == 0) { // Cutting this edge is not going to tear any cycle, remove it completely.
                    iter.remove();
                    continue;
                }
                // Update the best result if appropriate.
                if (count > bestEdgeCount) {
                    bestEdgeCount = count;
                    bestEdge = entry.getKey();
                    edgeCycles = entry.getValue();
                }
            }
            Assert.check(bestEdgeCount > 0);

            // Tear the best edge, and update what cycles have been teared.
            bestEdge.teared = true;
            tearedCycles.or(edgeCycles);
        }
    }

    /**
     * Construct a collection element from the collection of cycles.
     *
     * @param collection Collection to convert.
     * @param graphVertices Vertices of the graph.
     * @return The constructed collection element.
     */
    private static CollectionElement makeCollectionElement(List<Cycle> collection, List<Vertex> graphVertices) {
        // The created collection element has external input and output dependencies only (the vertices in the
        // collection are not dependencies). Each vertex inside is also converted to an element. Those elements have
        // non-teared input and output dependencies between the internal elements only.
        //
        // In this way, the collection element can be ordered with respect to the entire graph, while the internal
        // vertices can be ordered within the collection.

        // Get the vertex indices of all vertices in the cycle.
        BitSet containedVertices = new BitSet();
        for (Cycle cycle: collection) {
            containedVertices.or(cycle.vertices);
        }

        // For each vertex in the collection, add its external dependencies to collection inputs and outputs. Also build
        // non-teared inputs and outputs of each internal vertex element over the internal elements only, so the
        // contained elements can be ordered.
        BitSet collectionInputs = new BitSet(); // Inputs of the collection element.
        BitSet collectionOutputs = new BitSet(); // Outputs of the collection element.

        SingularElement[] containedElements = new SingularElement[containedVertices.cardinality()];
        int nextFreeContained = 0;

        for (int vertexIndex: new BitSetIterator(containedVertices)) {
            Vertex vertex = graphVertices.get(vertexIndex);

            // Process input dependencies of the vertex.
            BitSet nonTearedInputs = new BitSet();
            for (Edge e: vertex.inputs) {
                collectionInputs.set(e.producingVertex);
                nonTearedInputs.set(e.producingVertex, !e.teared);
            }

            // Process output dependencies of the vertex.
            BitSet nonTearedOutputs = new BitSet();
            for (Edge e: vertex.outputs) {
                collectionOutputs.set(e.consumingVertex);
                nonTearedOutputs.set(e.consumingVertex, !e.teared);
            }

            // Restrict the inputs and outputs of the internal element, and store it.
            nonTearedInputs.and(containedVertices);
            nonTearedOutputs.and(containedVertices);
            containedElements[nextFreeContained] = new SingularElement(vertex, nonTearedInputs, nonTearedOutputs);
            nextFreeContained++;
        }

        // Order internal elements.
        SingularElement[] orderedElements = new SingularElement[containedElements.length];
        orderElements(containedElements, containedVertices, orderedElements);

        // Exclude the contained vertices from the input and output of the collection element, and construct the
        // collection element with the ordered internal elements.
        collectionInputs.andNot(containedVertices);
        collectionOutputs.andNot(containedVertices);
        return new CollectionElement(Arrays.asList(orderedElements), collectionInputs, collectionOutputs);
    }

    /**
     * Order elements such that the vertices of an element only depend on vertices of an earlier element in the result.
     * Note that this function assumes existence of such an order.
     *
     * @param <E> Type of the elements.
     * @param elements Elements to order. Array is destroyed during the call.
     * @param vertices The vertices contained by the given elements.
     * @param destination Ordered output. Is modified in-place. Must have the same length as the number of elements to order.
     */
    private static <E extends Element> void orderElements(E[] elements, BitSet vertices, E[] destination) {
        Assert.check(elements.length == destination.length);

        int firstEmpty = 0; // Lowest entry in 'destination' that has not been assigned.
        int lastEmpty = destination.length - 1; // Highest entry in 'destination' that has not been assigned.
        BitSet predecessors = copy(vertices); // Vertex numbers *not* added before 'firstEmpty'.
        BitSet successors = copy(vertices); // Vertex numbers *not* added after 'lastEmpty'.

        // Elements are moved from 'elements' to 'destination'.
        //
        // The 'destination' is split into 3 parts. A prefix part near the start of the array up-to and excluding the
        // 'firstEmpty' index. Similarly, there is a suffix part near the end of the array just after 'lastEmpty' up-to
        // the end of the array. In between the prefix and suffix parts is empty space that becomes used when the prefix
        // or the suffix part grows.
        //
        // The input 'elements' array contains unordered elements that must be moved to the destination.
        //
        // An element can be appended to the destination prefix part if all input vertices of that element have been
        // moved to the prefix. Similarly, an element can be prepended to the destination suffix part if all output
        // vertices of that element have been moved to the suffix. If both moves can be performed one of them is
        // arbitrarily chosen.
        // After a move, the gap in 'elements' is filled by moving last element in it into that gap.
        //
        // The function assumes there are no cycles in the vertex dependencies, thus each iteration one or more elements
        // move until all elements are in 'destination'.
        int lastElement = elements.length - 1; // Last used index in 'elements'.
        while (lastElement >= 0) {
            boolean progress = false;

            // Find and move one or more elements to the destination.
            int elmIdx = 0;
            while (elmIdx <= lastElement) {
                // Loop terminates because each case below either decreases 'lastElement' or increases 'elmIdx'.
                if (!elements[elmIdx].hasInputDeps(predecessors)) {
                    // All available elements containing vertices needed by this element have already been moved to the
                    // front of 'destination'. Append this element.
                    destination[firstEmpty] = elements[elmIdx];
                    firstEmpty++;
                    elements[elmIdx].clearVertexIndices(predecessors);
                    progress = true;

                    // Eliminate the gap at 'elmIdx' by moving the last element into it.
                    if (elmIdx != lastElement) {
                        elements[elmIdx] = elements[lastElement];
                    }
                    elements[lastElement] = null;
                    lastElement--;
                } else if (!elements[elmIdx].hasOutputDeps(successors)) {
                    // All available elements containing vertices that need this element have already been moved to the
                    // end of 'destination'. Prepend this element.
                    destination[lastEmpty] = elements[elmIdx];
                    lastEmpty--;
                    elements[elmIdx].clearVertexIndices(successors);
                    progress = true;

                    // Eliminate the gap at 'elmIdx' by moving the last element into it.
                    if (elmIdx != lastElement) {
                        elements[elmIdx] = elements[lastElement];
                    }
                    elements[lastElement] = null;
                    lastElement--;
                } else {
                    elmIdx++; // Element cannot be moved, keep it for the next iteration.
                }
            }
            Assert.check(progress); // Something must be copied each iteration.
        }
        Assert.check(lastEmpty + 1 == firstEmpty); // Nothing should remain in the 'elements'.
    }
}
