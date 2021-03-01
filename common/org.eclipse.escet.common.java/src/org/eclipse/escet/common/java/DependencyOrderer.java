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

package org.eclipse.escet.common.java;

import static java.util.Collections.EMPTY_LIST;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Lists.set2list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class orders objects, making sure that objects that depend on other objects are ordered after the objects on
 * which they depend. In other words, the objects are ordered such that the dependencies of each object are earlier in
 * the result than the object itself.
 *
 * <p>
 * A common use case is declarations, where (other) declarations can use each other. For instance, constants may be
 * defined in terms of other constants. This class can be used to order (sort) the constants to make sure we have a
 * proper order, in which we can calculate their values.
 * </p>
 *
 * <p>
 * Usage:
 * <ul>
 * <li>Instantiate a derived class (actual implementation).</li>
 * <li>Add the objects to order using {@link #addObject}.</li>
 * <li>Compute and obtain the ordering using {@link #computeOrder}. There is an overload that allows passing additional
 * unordered objects.</li>
 * <li>Check the result for {@code null} (cycle found, no ordering exists).</li>
 * <li>If cycles exist, one of them can be retrieved by calling {@link #getCycle}.</li>
 * <li>Don't re-use the instance.</li>
 * </ul>
 * </p>
 *
 * @param <T> Type of objects being ordered.
 */
public abstract class DependencyOrderer<T> {
    /**
     * Mapping from objects to their direct dependencies. For objects that map to {@code null}, the dependencies are not
     * yet known. Entire mapping is {@code null} if no objects have been added so far.
     */
    private Map<T, Dependencies<T>> unorderedObjects = null;

    /**
     * Add an object to be ordered. Re-adding an object that was already previously added has no effect.
     *
     * @param obj Object to add.
     */
    public void addObject(T obj) {
        if (unorderedObjects == null) {
            unorderedObjects = map();
        }
        unorderedObjects.put(obj, null);
    }

    /**
     * Adds the given objects, and computes an order between them, such that the dependencies of each object are earlier
     * in the returned list than the object itself.
     *
     * <p>
     * The unordered objects to consider for ordering are the objects already added via the {@link #addObject} method,
     * as well as the additionally provided objects. All dependencies of the unordered objects that are themselves not
     * unordered objects, are ignored.
     * </p>
     *
     * @param objects Additional unordered objects. Duplicates with respect to the already added objects are silently
     *     discarded.
     * @return The ordered objects, or {@code null} if a cycle in the dependencies is detected (and thus no ordering
     *     exists).
     */
    public List<T> computeOrder(Collection<T> objects) {
        return computeOrder(objects, true);
    }

    /**
     * Adds the given objects, and computes an order between them, such that the dependencies of each object are earlier
     * in the returned list than the object itself.
     *
     * <p>
     * The unordered objects to consider for ordering are the objects already added via the {@link #addObject} method,
     * as well as the additionally provided objects. The dependencies of the unordered objects that are themselves not
     * unordered objects, may either be ignored, or be added as additional unordered objects to be ordered as well.
     * </p>
     *
     * @param objects Additional unordered objects. Duplicates with respect to the already added objects are silently
     *     discarded.
     * @param restrict Whether to restrict the direct dependencies of unordered objects to the set of unordered objects,
     *     ignoring any additional unordered objects ({@code true}), or add those additional unordered objects as
     *     objects to be ordered ({@code false}).
     * @return The ordered objects, or {@code null} if a cycle in the dependencies is detected (and thus no ordering
     *     exists).
     */
    public List<T> computeOrder(Collection<T> objects, boolean restrict) {
        if (unorderedObjects == null) {
            int size = objects.size();
            unorderedObjects = mapc(size);
        }
        for (T object: objects) {
            unorderedObjects.put(object, null);
        }
        return computeOrder(restrict);
    }

    /**
     * Computes an order between the objects, such that the dependencies of each object are earlier in the returned list
     * than the object itself.
     *
     * <p>
     * The unordered objects to consider for ordering are the objects already added via the {@link #addObject} method.
     * All dependencies of the unordered objects that are themselves not unordered objects, are ignored.
     * </p>
     *
     * @return The ordered objects, or {@code null} if a cycle in the dependencies is detected (and thus no ordering
     *     exists).
     */
    public List<T> computeOrder() {
        return computeOrder(true);
    }

    /**
     * Computes an order between the objects, such that the dependencies of each object are earlier in the returned list
     * than the object itself.
     *
     * <p>
     * The unordered objects to consider for ordering are the objects already added via the {@link #addObject} method.
     * The dependencies of the unordered objects that are themselves not unordered objects, may either be ignored, or be
     * added as additional unordered objects to be ordered as well.
     * </p>
     *
     * @param restrict Whether to restrict the direct dependencies of unordered objects to the set of unordered objects,
     *     ignoring any additional unordered objects ({@code true}), or add those additional unordered objects as
     *     objects to be ordered ({@code false}).
     * @return The ordered objects, or {@code null} if a cycle in the dependencies is detected (and thus no ordering
     *     exists).
     */
    public List<T> computeOrder(boolean restrict) {
        // If no objects added, then result is trivial.
        if (unorderedObjects == null) {
            return EMPTY_LIST;
        }

        // Set up direct dependencies administration.
        setDirectDependencies(restrict);

        // Compute the order.
        List<T> orderedObjects = listc(unorderedObjects.size());
        while (!unorderedObjects.isEmpty()) {
            boolean progress = false;

            // Try to order another unordered object.
            Iterator<Entry<T, Dependencies<T>>> iter;
            iter = unorderedObjects.entrySet().iterator();
            while (iter.hasNext()) {
                // Get next unordered object.
                Entry<T, Dependencies<T>> entry = iter.next();
                T object = entry.getKey();
                Dependencies<T> dependencies = entry.getValue();

                // Update its index to the current set of ordered objects.
                dependencies.updateIndex(orderedObjects);

                // Can this entry be considered 'ordered'?
                if (dependencies.isEmpty()) {
                    orderedObjects.add(object);
                    iter.remove();
                    progress = true;
                }
            }

            // Cycle detected. No ordering exists.
            if (!progress) {
                return null;
            }
        }

        // Order successfully computed.
        return orderedObjects;
    }

    /**
     * After {@link #computeOrder} indicated a cycle exists, this method finds one cycle.
     *
     * @return Sequence of objects that depend on each other (element {@code i} requires element {@code i+1} modulo the
     *     length of the list).
     */
    public List<T> getCycle() {
        Assert.check(unorderedObjects != null && !unorderedObjects.isEmpty());

        List<T> dependencyChain = list();
        Map<T, Integer> chainIndex = map();
        while (true) {
            // Start with a random element, and follow the chain.
            T obj = unorderedObjects.keySet().iterator().next();
            int cycleStart = cycleTest(obj, dependencyChain, chainIndex);
            if (cycleStart >= 0) {
                return dependencyChain.subList(cycleStart, dependencyChain.size());
            }
        }
    }

    /**
     * Test the object for being part of a cycle by following its dependency chains.
     *
     * @param obj Object to test for being part of a cycle. (Must exist in the {@link #unorderedObjects}).
     * @param dependencyChain Sequence of objects that are tested for having a cycle in their dependencies.
     * @param chainIndex Index map of objects in 'dependencyChain' to their index value in the sequence.
     * @return Starting index of a cycle in 'dependencyChain', or a negative value if no cycle found.
     */
    private int cycleTest(T obj, List<T> dependencyChain, Map<T, Integer> chainIndex) {
        // If object already in the dependency chain, we have found a cycle.
        Integer cycleIndex = chainIndex.get(obj);
        if (cycleIndex != null) {
            return cycleIndex;
        }

        // Add the obj to the chain.
        int idx = dependencyChain.size();
        dependencyChain.add(obj);
        chainIndex.put(obj, idx);

        // Test its dependencies.
        Dependencies<T> deps = unorderedObjects.get(obj);
        for (T dep: deps.directDependencies) {
            if (!unorderedObjects.containsKey(dep)) {
                continue;
            }
            int cycleStart = cycleTest(dep, dependencyChain, chainIndex);
            if (cycleStart >= 0) {
                return cycleStart;
            }
        }

        // obj is not part of a cycle, remove it.
        dependencyChain.remove(idx);
        chainIndex.remove(obj);

        // Remove the object after establishing it is not part of any cycle,
        // to prevent it from being tested again.
        unorderedObjects.remove(obj);
        return -1;
    }

    /**
     * Set the direct dependencies for the collection of unordered objects.
     *
     * @param restrict Whether to restrict the direct dependencies of unordered objects to the set of unordered objects,
     *     ignoring any additional unordered objects ({@code true}), or add those additional unordered objects as
     *     objects to be ordered ({@code false}).
     */
    private void setDirectDependencies(boolean restrict) {
        if (restrict) {
            for (T obj: unorderedObjects.keySet()) {
                // Find direct dependencies.
                Set<T> directDeps = findDirectDependencies(obj);

                // Restrict the set of dependencies to the unordered objects.
                directDeps.retainAll(unorderedObjects.keySet());

                // Store the direct dependencies.
                unorderedObjects.put(obj, new Dependencies<>(directDeps));
            }
        } else {
            List<T> objs = set2list(unorderedObjects.keySet());
            for (int i = 0; i < objs.size(); i++) {
                T obj = objs.get(i);

                // Find direct dependencies.
                Set<T> directDeps = findDirectDependencies(obj);

                // Add new dependencies, so that they are ordered as well.
                for (T dep: directDeps) {
                    if (unorderedObjects.containsKey(dep)) {
                        continue;
                    }
                    unorderedObjects.put(dep, null);
                    objs.add(dep);
                }

                // Store the direct dependencies.
                unorderedObjects.put(obj, new Dependencies<>(directDeps));
            }
        }
    }

    /**
     * Find the direct dependencies of an object.
     *
     * @param obj Object to examine.
     * @return The direct dependencies of the given object. The result may be restricted by the unordered orderer being
     *     ordered. Note that the set, if not empty, must be mutable, as it is modified in-place by the dependency
     *     orderer.
     */
    protected abstract Set<T> findDirectDependencies(T obj);

    /**
     * Class for storing direct dependencies of an object, and the internal administration for deciding the order.
     *
     * @param <T> Type of objects being ordered.
     */
    private static class Dependencies<T> {
        /** Direct dependent objects that have not been ordered yet. */
        public Set<T> directDependencies;

        /**
         * Index in the ordered objects that indicates how far the {@link #directDependencies} have been updated.
         *
         * <p>
         * The index value itself has not been done yet.
         * </p>
         */
        private int currentIndex = 0;

        /**
         * Constructor for the {@link DependencyOrderer.Dependencies} class.
         *
         * @param directDependencies The direct dependencies that were found.
         */
        public Dependencies(Set<T> directDependencies) {
            this.directDependencies = directDependencies;
        }

        /**
         * Update the index of the ordered dependencies.
         *
         * @param orderedObjects Ordered objects found so far.
         */
        public void updateIndex(List<T> orderedObjects) {
            while (currentIndex < orderedObjects.size()) {
                directDependencies.remove(orderedObjects.get(currentIndex));
                currentIndex++;
            }
        }

        /**
         * Is the set of direct dependencies empty? That is, have all dependencies been ordered?
         *
         * @return {@code true} if all dependencies have been ordered, {@code false} otherwise.
         */
        public boolean isEmpty() {
            return directDependencies.isEmpty();
        }
    }
}
