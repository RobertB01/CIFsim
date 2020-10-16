//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.partitions;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;

/**
 * Refine provided initial partition V<sub>0</sub> containing locations of an automaton.
 *
 * <p>
 * The partition set V<sub>0</sub> is iteratively further refined until fixed point is reached. The elementary step is
 * performed by function f(U, σ) = {x∊X | (∃u,u'∊(Σ-Σ')<sup>*</sup>) ξ(x,uσu')∩U≠∅}, with U⊆X and σ∊Σ'. (The function
 * returns a set of states x, where each state has a path to a state in U, with exactly one visible event.)
 * </p>
 *
 * <p>
 * From a computation point of view, start with set U and expand the set to all states backwards reachable with
 * unobservable events (Σ-Σ')<sup>*</sup>. From this set get the set of predecessor states using event σ, and expand
 * this set again to all states backward reachable with unobservable events. The latter set is the answer of f(U, σ).
 * </p>
 *
 * <p>
 * Function g performs function f on a partition set (power set) V⊆2<sup>X</sup>, g(V) = {f(U,σ) | U∊V ⋀ σ∊Σ'} ⋃ V. It
 * extends the given partition set with result sets of function f, for every partition and every observable event.
 * </p>
 *
 * <p>
 * The new result is then normalized again to a partition set with functions h and q. Function h splits the sets in
 * subsets h(V) = {U⊆X | (∃S,T∊V) U = S-T} - {∅}. After reaching fixed point (h<sup>∞</sup>(V)), q drops all super sets:
 * q(V) = {U∊V | (∀P∊V) P⊆U ⇒ P=U} (the sets U, where you cannot find a smaller subset P from V).
 * </p>
 *
 * <p>
 * The above sequence (q ∘ h<sup>∞</sup> ∘ g) (that is, q(h<sup>∞</sup>(g(V)) ) is performed until fixed point is
 * reached. That is (q ∘ h<sup>∞</sup> ∘ g)<sup>r</sup>(V<sub>0</sub>) = (q ∘ h<sup>∞</sup> ∘
 * g)<sup>r+1</sup>(V<sub>0</sub>). This is known as the Ω function, Ω(Σ', V<sub>0</sub>) = (q ∘ h<sup>∞</sup> ∘
 * g)<sup>r</sup>(V<sub>0</sub>).
 * </p>
 *
 * <p>
 * In the implementation, refinement of the initial partitions V is done by iteratively computing f(U, σ) for every
 * partition U∊V and observable event σ∊Σ'. If a set in V partly overlaps with a result from f(U, σ), split that set in
 * two (namely U∩f(U,σ), and U-f(U,σ)). For the new sets, also compute f(U, σ) for every observable event σ. When fixed
 * point is reached (that is, no partly overlapping of partitions in V occurs any more with function results from
 * f(U,σ)), partitioning Ω(Σ', V<sub>0</sub>) has been computed.
 * </p>
 *
 * <p>
 * After computing a function result S = f(U,σ), states from a partition that are also in S have to move to a new
 * partition. The solution is to maintain a mapping from old partitions to new partitions while processing the states in
 * S, and just move all states of S according to that partition mapping. If all states from an existing partition were
 * moved, nothing has happened (all states are now in an equal new partition). If only a part of a partition got moved,
 * the remaining states are still in the old partition, and the moved states are in the new partition, ie the partition
 * got split in two according to U∩S and U-S.
 * </p>
 *
 * <p>
 * The mapping of old partition to new partition can be stored in the old partition. After all states have been moved,
 * partial moves means that both the old and the new partition contains some states. Both partitions need to have
 * function f applied again. If all states were moved to the new partition (that is, the old partition is now empty),
 * the new partition should be applied to f again only if the old partition needed to be applied.
 * </p>
 */
public class PartitionRefinement {
    /**
     * Mapping of automaton locations to locations in a partition.
     *
     * <p>
     * TODO: This is a fixed map (once set up, keys and values are never added, deleted, or changed). For better
     * performance, it would be useful to move this information into the location itself. That implies that locations in
     * the automaton may be of a different (derived) class, that is, you get a generic LOC type parameter. Edges,
     * automata, and all other algorithmic code is then affected.
     * </p>
     *
     * <p>
     * There are a few variations on this idea (for example generic data field in a location), but they all have major
     * impact on code complexity due to generics.
     * </p>
     *
     * <p>
     * The other solution, namely down-casting, has costs in constructing a variant automaton, and still performs class
     * checking during the down-cast.
     * </p>
     *
     * <p>
     * Given the huge impact of any of these alternatives and the perceived small increase in performance, the mapping
     * is the currently preferred solution.
     * </p>
     */
    public Map<Location, PartitionLocation> locationMapping;

    /** Set of observable events. */
    public final Set<Event> observableEvents;

    /** Set of non-observable events. */
    public final Set<Event> nonObservableEvents;

    /** Partitions that need to be (re)computed with f(U, σ). */
    public Partition computePartitions = null;

    /**
     * Constructor of the {@link PartitionRefinement} class.
     *
     * @param observableEvents The observable events.
     * @param nonObservableEvents The non-observable events.
     * @param partitions Initial partitioning of locations.
     */
    public PartitionRefinement(Set<Event> observableEvents, Set<Event> nonObservableEvents,
            Set<Set<Location>> partitions)
    {
        int size = 0;
        for (Set<Location> sl: partitions) {
            size += sl.size();
        }

        locationMapping = mapc(size);
        for (Set<Location> sl: partitions) {
            Partition part = new Partition();
            for (Location loc: sl) {
                PartitionLocation pl = new PartitionLocation(loc);
                pl.partition = part;
                part.addLocation(pl);
                locationMapping.put(loc, pl);
            }
            addCompute(part);
        }
        this.observableEvents = observableEvents;
        this.nonObservableEvents = nonObservableEvents;
    }

    /** Refine the initial partitioning until fixed point is reached. */
    public void refinePartitions() {
        Partition currentPartition;
        while (computePartitions != null) {
            currentPartition = computePartitions;
            removeCompute(currentPartition);
            Set<Location> expandedSet = f1(currentPartition);
            Set<Location> fResult = set();
            for (Event evt: observableEvents) {
                f2(expandedSet, evt, fResult);
                currentPartition = addSet(fResult, currentPartition);
                if (currentPartition == null) {
                    break;
                }
            }
        }
    }

    /**
     * First computation step of function f, from partition U, expand over non-observable events. Since this set is the
     * same for every second step of the function (until the partition becomes invalid), it is a separate step in the
     * calculation.
     *
     * @param currentPartition Partition U to start from.
     * @return Backward expanded set of locations from U over non-observable events.
     */
    protected Set<Location> f1(Partition currentPartition) {
        Set<Location> expandSet = set();
        Queue<Location> notDone = new ArrayDeque<>(100);

        PartitionLocation ploc = currentPartition.locations;
        while (ploc != null) {
            expandSet.add(ploc.loc);
            notDone.add(ploc.loc);

            ploc = ploc.nextPLoc;
        }
        return AutomatonHelper.expandStatesBackward(expandSet, notDone, nonObservableEvents);
    }

    /**
     * Second computation step of function f. Perform a backward transition over an edge with a 'visibleEvent' event,
     * and then expand the collection further backwards through edges with non-observable events.
     *
     * @param expandedSet Computation result of the first step of function f. Must not be modified.
     * @param visibleEvent Visible event to perform first in the second step.
     * @param result Resulting set of locations. Modified in-place, also returned as result. (Done this way to re-use
     *     the same set and avoid too many set creations and expansions.)
     * @return The set of locations backward reachable from 'expandSet' by first performing 'visibleEvent' and then zero
     *     or more non-observable events.
     */
    protected Set<Location> f2(Set<Location> expandedSet, Event visibleEvent, Set<Location> result) {
        result.clear();
        Queue<Location> notDone = new ArrayDeque<>(100);
        for (Location loc: expandedSet) {
            for (Edge e: loc.getIncoming(visibleEvent)) {
                if (result.add(e.srcLoc)) {
                    notDone.add(e.srcLoc);
                }
            }
        }
        if (result.isEmpty()) {
            return result;
        }
        return AutomatonHelper.expandStatesBackward(result, notDone, nonObservableEvents);
    }

    /**
     * Mark a partition in need of (re)computing f(U, σ) on it.
     *
     * @param part Partition to add.
     */
    public void addCompute(Partition part) {
        Assert.check(!part.inComputeList);
        part.inComputeList = true;

        part.prevCompute = null;
        part.nextCompute = computePartitions;
        if (computePartitions != null) {
            computePartitions.prevCompute = part;
        }
        computePartitions = part;
    }

    /**
     * Remove a partition from the list of partitions that need (re)computing.
     *
     * @param part Partition to remove.
     */
    public void removeCompute(Partition part) {
        Assert.check(part.inComputeList);
        part.inComputeList = false;

        if (part.prevCompute != null) {
            part.prevCompute.nextCompute = part.nextCompute;
        }
        if (part.nextCompute != null) {
            part.nextCompute.prevCompute = part.prevCompute;
        }
        if (computePartitions == part) {
            computePartitions = part.nextCompute;
        }
    }

    /**
     * First part of merging new locations into the partitions. It moves locations in the new set to new partitions,
     * returning a reference to a single linked list of affected old partitions.
     *
     * <p>
     * Separate for testing only, for production, use {@link #addSet(Set, Partition)} instead.
     * </p>
     *
     * @param locs New set of locations to move to new partitions.
     * @return Affected partitions.
     */
    public final Partition moveLocations(Set<Location> locs) {
        Partition affected = null;
        // Move locations from the set to new partitions.
        for (Location loc: locs) {
            PartitionLocation ploc = locationMapping.get(loc);
            Partition part = ploc.partition;
            part.removeLocation(ploc);
            if (part.newPartition != null) {
                // Simple case, it is already known where locations move to.
                part.newPartition.addLocation(ploc);
                ploc.partition = part.newPartition;
                continue;
            }
            // Make a new destination partition and move the location.
            // 'compute' references are updated later.
            Partition newPart = new Partition();
            part.newPartition = newPart;
            newPart.addLocation(ploc);
            ploc.partition = newPart;
            // 'compute' references are updated later, so keep a reference
            // to the old partition.
            part.nextAffected = affected;
            affected = part;
        }
        return affected;
    }

    /**
     * Second part of merging new locations into the partitions. It cleans the old partitions, and sets (re)computing
     * flags as needed.
     *
     * <p>
     * Separate for testing only, for production, use {@link #addSet(Set, Partition)} instead.
     * </p>
     *
     * @param affected Old partitions that have been changed during moving of locations.
     * @param currentPartition Partition used to compute the new set. The location movements may have affected the
     *     partition being re-computed, so it may need to be updated too.
     * @return Updated current partition, or {@code null} if it has become invalid.
     */
    public final Partition mergeLocations(Partition affected, Partition currentPartition) {
        // Clean up old partitions.
        while (affected != null) {
            if (affected.locations == null) {
                if (currentPartition == affected) {
                    // All locations got moved, just continue computing with
                    // the new partition.
                    currentPartition = affected.newPartition;
                }

                // If old partition needed to be re-computed, the new one needs
                // to be re-computed too.
                if (affected.inComputeList) {
                    removeCompute(affected);
                    addCompute(affected.newPartition);
                }
                affected.newPartition = null; // Not really needed.
            } else {
                // Partly move, keep both partitions.
                if (currentPartition == affected) {
                    // No useful way to continue the computation with the
                    // old partition.
                    currentPartition = null;
                }

                if (!affected.inComputeList) {
                    addCompute(affected);
                }
                addCompute(affected.newPartition);
                affected.newPartition = null;
            }
            // Go to the next old partition.
            Partition nextPart = affected.nextAffected;
            affected.nextAffected = null; // Not really needed.
            affected = nextPart;
        }
        return currentPartition;
    }

    /**
     * Perform q(h<sup>∞</sup>(locs)).
     *
     * <p>
     * Apply a new function result from f(U, σ) to the partitions, splitting the partitions if needed, and updating
     * which partitions need computing. In the process, the 'current partition' (the partition U used to derive the
     * function result) may move, or become invalid.
     * </p>
     *
     * @param locs New set of locations to apply to the partitions.
     * @param currentPartition Partition used to compute the new set.
     * @return Updated current partition, or {@code null} if it has become invalid.
     */
    public final Partition addSet(Set<Location> locs, Partition currentPartition) {
        Partition affected = moveLocations(locs);
        currentPartition = mergeLocations(affected, currentPartition);
        return currentPartition;
    }

    /**
     * Wrapper function to test this class. It builds an instance, performs one set merge/split operation, and returns
     * the result.
     *
     * @param existing Existing partitions.
     * @param newSet New set of locations to merge into the partitions.
     * @return Resulting set of partitions.
     */
    public static Set<Set<Location>> addSet(Set<Set<Location>> existing, Set<Location> newSet) {
        // observableEvents and nonObservableEvents are not used in this test.
        PartitionRefinement oc = new PartitionRefinement(null, null, existing);
        oc.addSet(newSet, null);
        return oc.getPartitions();
    }

    /**
     * Reconstruction of the available partitions. This method is used during testing only.
     *
     * @return The partitions stored in {@link #locationMapping}.
     */
    public Set<Set<Location>> getPartitions() {
        // From locations and the location mapping, reconstruct the partitions.
        Map<Partition, Set<Location>> partitionMap = map();
        for (Entry<Location, PartitionLocation> lpe: locationMapping.entrySet()) {
            Partition p = lpe.getValue().partition;
            Set<Location> locs = partitionMap.get(p);
            if (locs == null) {
                locs = set();
                partitionMap.put(p, locs);
            }
            locs.add(lpe.getKey());
        }

        // Put the found partitions in a set.
        Set<Set<Location>> partitions = set();
        for (Set<Location> locs: partitionMap.values()) {
            partitions.add(locs);
        }
        return partitions;
    }

    /**
     * Compute fixed point of H. This implementation is straight from the math description, and very expensive to
     * compute. It is used as base reference implementation.
     *
     * @param superLocs Input super set of locations.
     * @return Resulting super set of locations.
     */
    public static Set<Set<Location>> hInfinite(Set<Set<Location>> superLocs) {
        for (;;) {
            Set<Set<Location>> result = set();
            // Compute "result = H(superLocs)"
            for (Set<Location> locs1: superLocs) {
                for (Set<Location> locs2: superLocs) {
                    Set<Location> locs3 = Sets.copy(locs1);
                    locs3.removeAll(locs2);
                    if (!locs3.isEmpty()) {
                        result.add(locs3);
                    }
                }
            }
            if (result.equals(superLocs)) {
                return result;
            }
            superLocs = result;
        }
    }

    /**
     * Compute Q function (find smallest available sets). This implementation is straight from the math description, and
     * very expensive to compute. It is used as base reference implementation.
     *
     * @param superLocs Input super set of locations.
     * @return Resulting super set of locations.
     */
    public static Set<Set<Location>> q(Set<Set<Location>> superLocs) {
        Set<Set<Location>> result = set();
        for (Set<Location> locs: superLocs) {
            boolean allowed = true;
            for (Set<Location> locs2: superLocs) {
                if (!locs.containsAll(locs2)) {
                    continue;
                }
                if (locs.equals(locs2)) {
                    continue;
                }
                allowed = false;
                break;
            }
            if (allowed) {
                result.add(locs);
            }
        }
        return result;
    }
}
