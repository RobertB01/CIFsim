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

package org.eclipse.escet.cif.bdd.utils;

import static org.eclipse.escet.cif.bdd.utils.BddUtils.bddToStr;
import static org.eclipse.escet.common.java.BitSets.copy;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.eclipse.escet.cif.bdd.settings.ExplorationStrategy;
import org.eclipse.escet.cif.bdd.spec.CifBddEdge;
import org.eclipse.escet.cif.bdd.spec.CifBddEdgeApplyDirection;
import org.eclipse.escet.cif.bdd.spec.CifBddEdgeKind;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.bdd.workset.pruners.MaxCardinalityEdgePruner;
import org.eclipse.escet.cif.bdd.workset.pruners.RewardBasedEdgePruner;
import org.eclipse.escet.cif.bdd.workset.pruners.SequentialEdgePruner;
import org.eclipse.escet.cif.bdd.workset.selectors.EdgeSelector;
import org.eclipse.escet.cif.bdd.workset.selectors.FirstEdgeSelector;
import org.eclipse.escet.cif.bdd.workset.selectors.PruningEdgeSelector;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSets;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;

import com.github.javabdd.BDD;
import com.github.javabdd.BDDVarSet;

/** CIF/BDD reachability computations. */
public class CifBddReachability {
    /** The CIF/BDD specification. */
    private final CifBddSpec cifBddSpec;

    /** The name of the predicate on which to apply the reachability, for debug output. Must be in lower case. */
    private final String predName;

    /**
     * The name of the initial value of the predicate on which to apply the reachability, for debug output. Must be in
     * lower case.
     */
    private final String initName;

    /**
     * The name of the restriction predicate, for debug output. Must be in lower case. May be {@code null} if no
     * restriction predicate will be used.
     */
    private final String restrictionName;

    /**
     * The predicate that indicates the upper bound on the reached states. That is, during reachability no states may be
     * reached outside these states. May be {@code null} to not impose a restriction, which is semantically equivalent
     * to providing 'true'.
     */
    private final BDD restriction;

    /**
     * The direction of the reachability computation, i.e., the direction in which to apply edges during the
     * reachability computation.
     */
    private final CifBddEdgeApplyDirection direction;

    /** The kinds of edges to apply during the reachability computation. */
    private final Set<CifBddEdgeKind> edgeKinds;

    /** Whether debug output is enabled. */
    private final boolean dbgEnabled;

    /** The instance number to use for saturation. */
    private Integer saturationInstance;

    /**
     * Constructor for the {@link CifBddReachability} class.
     *
     * @param cifBddSpec The CIF/BDD specification.
     * @param predName The name of the given predicate, for debug output. Must be in lower case.
     * @param initName The name of the initial value of the given predicate, for debug output. Must be in lower case.
     * @param restrictionName The name of the restriction predicate, for debug output. Must be in lower case. May be
     *     {@code null} if no restriction predicate will be used.
     * @param restriction The predicate that indicates the upper bound on the reached states. That is, during
     *     reachability no states may be reached outside these states. May be {@code null} to not impose a restriction,
     *     which is semantically equivalent to providing 'true'.
     * @param direction The direction of the reachability computation, i.e., the direction in which to apply edges
     *     during the reachability computation.
     * @param edgeKinds The kinds of edges to apply during the reachability computation.
     * @param dbgEnabled Whether debug output is enabled.
     */
    public CifBddReachability(CifBddSpec cifBddSpec, String predName, String initName, String restrictionName,
            BDD restriction, CifBddEdgeApplyDirection direction, Set<CifBddEdgeKind> edgeKinds,
            boolean dbgEnabled)
    {
        Assert.areEqual(restrictionName == null, restriction == null);
        this.cifBddSpec = cifBddSpec;
        this.predName = predName;
        this.initName = initName;
        this.restrictionName = restrictionName;
        this.restriction = restriction;
        this.direction = direction;
        this.edgeKinds = edgeKinds;
        this.dbgEnabled = dbgEnabled;
        this.saturationInstance = null;
    }

    /**
     * Set the instance number to use for saturation. This number must be unique for the list of edges with which saturation
     * is performed, and will be used internally for caching purposes.
     *
     * @param instance The instance number.
     */
    public void setSaturationInstance(int instance) {
        this.saturationInstance = instance;
    }

    /**
     * Performs forward or backward reachability until a fixed point is reached.
     *
     * @param pred The predicate to which to apply the reachability. This predicate is {@link BDD#free freed} by this
     *     method.
     * @return The fixed point result of the reachability computation, or {@code null} if termination was requested.
     */
    public BDD performReachability(BDD pred) {
        // Print debug output.
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("%s: %s [%s predicate]", Strings.makeInitialUppercase(predName),
                    bddToStr(pred, cifBddSpec), initName);
        }

        // Initialization.
        boolean changed = false;

        // Restrict predicate.
        if (restriction != null) {
            BDD restrictedPred = pred.and(restriction);
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }

            if (pred.equals(restrictedPred)) {
                restrictedPred.free();
            } else {
                if (dbgEnabled) {
                    Assert.notNull(restrictionName);
                    cifBddSpec.settings.getDebugOutput().line("%s: %s -> %s [restricted to %s predicate: %s]",
                            Strings.makeInitialUppercase(predName), bddToStr(pred, cifBddSpec),
                            bddToStr(restrictedPred, cifBddSpec), restrictionName, bddToStr(restriction, cifBddSpec));
                }
                pred.free();
                pred = restrictedPred;
                changed = true;
            }
        }

        // Determine the edges to be applied.
        List<CifBddEdge> orderedEdges = (direction == CifBddEdgeApplyDirection.FORWARD) ? cifBddSpec.orderedEdgesForward
                : cifBddSpec.orderedEdgesBackward;
        Predicate<CifBddEdge> edgeShouldBeApplied = e -> edgeKinds.contains(e.getEdgeKind());
        List<CifBddEdge> edgesToApply = orderedEdges.stream().filter(edgeShouldBeApplied).toList();

        // Apply edges until we get a fixed point.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return null;
        }

        ExplorationStrategy strategy = cifBddSpec.settings.getExplorationStrategy();

        Pair<BDD, Boolean> reachabilityResult = switch (strategy) {
            case CHAINING_FIXED -> performReachabilityFixedOrder(pred, edgesToApply);
            case CHAINING_WORKSET -> {
                BitSet edgesToApplyMask = IntStream.range(0, orderedEdges.size())
                        .filter(i -> edgeShouldBeApplied.test(orderedEdges.get(i))).boxed().collect(BitSets.toBitSet());
                yield performReachabilityWorkset(pred, orderedEdges, edgesToApplyMask);
            }
            case SATURATION -> performReachabilitySaturation(pred, edgesToApply);
            default -> throw new RuntimeException("Unknown exploration strategy: " + strategy);
        };

        if (reachabilityResult == null || cifBddSpec.settings.getTermination().isRequested()) {
            return null;
        }
        pred = reachabilityResult.left;
        changed |= reachabilityResult.right;

        // Fixed point reached. Inform the user.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return null;
        }
        if (dbgEnabled && changed) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("%s: %s [fixed point].", Strings.makeInitialUppercase(predName),
                    bddToStr(pred, cifBddSpec));
        }
        return pred;
    }

    /**
     * Performs forward or backward reachability until a fixed point is reached, by applying the edges using the workset
     * algorithm.
     *
     * @param pred The predicate to which to apply the reachability. This predicate should not be used after this
     *     method, as it may have been {@link BDD#free freed} by this method. Instead, continue with the predicate
     *     returned by this method as part of the return value.
     * @param edges The CIF/BDD edges.
     * @param edgesMask The edges to apply, as a mask on the edges, indicating for each edge whether it should be
     *     applied.
     * @return The fixed point result of the reachability computation, together with an indication of whether the
     *     predicate was changed as a result of the reachability computation. Instead of a pair, {@code null} is
     *     returned if termination was requested.
     */
    private Pair<BDD, Boolean> performReachabilityWorkset(BDD pred, List<CifBddEdge> edges, BitSet edgesMask) {
        boolean changed = false;
        List<BitSet> dependencies = (direction == CifBddEdgeApplyDirection.FORWARD)
                ? cifBddSpec.worksetDependenciesForward : cifBddSpec.worksetDependenciesBackward;
        EdgeSelector edgeSelector = new PruningEdgeSelector(
                new SequentialEdgePruner(new MaxCardinalityEdgePruner(dependencies),
                        new RewardBasedEdgePruner(edges.size(), 1, -1)),
                new FirstEdgeSelector());
        BitSet workset = copy(edgesMask);
        while (!workset.isEmpty()) {
            // Select the next edge to apply.
            int edgeIdx = edgeSelector.select(workset);
            CifBddEdge edge = edges.get(edgeIdx);

            // Repeatedly apply the selected edge, until it no longer has an effect.
            boolean changedByEdge = false;
            while (true) {
                // Apply selected edge.
                BDD updPred = pred.id();
                updPred = edge.apply(updPred, direction, restriction);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return null;
                }

                // Extend reachable states.
                BDD newPred = pred.id().orWith(updPred);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return null;
                }

                // Detect no change (fixed point reached for the selected edge).
                if (pred.equals(newPred)) {
                    newPred.free();
                    break;
                }

                // The predicate changed: at least one new state was reached. Inform the user.
                if (dbgEnabled) {
                    if (!changed) {
                        cifBddSpec.settings.getDebugOutput().line();
                    }
                    String restrTxt;
                    if (restriction == null) {
                        restrTxt = "";
                    } else {
                        Assert.notNull(restrictionName);
                        restrTxt = fmt(", restricted to %s predicate: %s", restrictionName,
                                bddToStr(restriction, cifBddSpec));
                    }
                    cifBddSpec.settings.getDebugOutput().line("%s: %s -> %s [%s reach with edge: %s%s]",
                            Strings.makeInitialUppercase(predName), bddToStr(pred, cifBddSpec),
                            bddToStr(newPred, cifBddSpec), direction.description, edge.toString(""), restrTxt);
                }

                // Update the administration.
                pred.free();
                pred = newPred;
                changed = true;
                changedByEdge = true;
            }

            // Update the workset.
            if (changedByEdge) {
                // Add the dependent edges of the edge that was just applied, as these may have become enabled.
                // However, only add those dependent edges that are to be applied at all.
                BitSet dependents = copy(dependencies.get(edgeIdx));
                dependents.and(edgesMask);
                workset.or(dependents);
            }
            // A fixed point was reached for the selected edge, so it can be removed from the workset. Note that by
            // removing this edge after adding its dependencies, it does not matter whether an edge is a dependency of
            // itself or not.
            workset.clear(edgeIdx);

            // Update the edge selector, based on the effect of applying the selected edge.
            edgeSelector.update(edgeIdx, changedByEdge);
        }
        return pair(pred, changed);
    }

    /**
     * Performs forward or backward reachability until a fixed point is reached, by applying the edges in a fixed order.
     *
     * @param pred The predicate to which to apply the reachability. This predicate should not be used after this
     *     method, as it may have been {@link BDD#free freed} by this method. Instead, continue with the predicate
     *     returned by this method as part of the return value.
     * @param edges The CIF/BDD edges to apply.
     * @return The fixed point result of the reachability computation, together with an indication of whether the
     *     predicate was changed as a result of the reachability computation. Instead of a pair, {@code null} is
     *     returned if termination was requested.
     */
    private Pair<BDD, Boolean> performReachabilityFixedOrder(BDD pred, List<CifBddEdge> edges) {
        boolean changed = false;
        int iter = 0;
        int remainingEdges = edges.size(); // Number of edges to apply without change to get the fixed point.
        while (remainingEdges > 0) {
            // Print iteration, for debugging.
            iter++;
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("%s reachability iteration %d:",
                        Strings.makeInitialUppercase(direction.description), iter);
                cifBddSpec.settings.getDebugOutput().inc();
            }

            // Push through all edges.
            boolean iterChanged = false;
            for (CifBddEdge edge: edges) {
                // Apply edge.
                BDD updPred = pred.id();
                updPred = edge.apply(updPred, direction, restriction);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    return null;
                }

                // Extend reachable states.
                BDD newPred = pred.id().orWith(updPred);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    return null;
                }

                // Detect change.
                if (pred.equals(newPred)) {
                    // No change.
                    newPred.free();
                    remainingEdges--;
                    if (remainingEdges == 0) {
                        break; // Fixed point reached.
                    }
                } else {
                    // The predicate changed: at least one new state was reached. Inform the user.
                    if (dbgEnabled) {
                        String restrTxt;
                        if (restriction == null) {
                            restrTxt = "";
                        } else {
                            Assert.notNull(restrictionName);
                            restrTxt = fmt(", restricted to %s predicate: %s", restrictionName,
                                    bddToStr(restriction, cifBddSpec));
                        }
                        cifBddSpec.settings.getDebugOutput().line("%s: %s -> %s [%s reach with edge: %s%s]",
                                Strings.makeInitialUppercase(predName), bddToStr(pred, cifBddSpec),
                                bddToStr(newPred, cifBddSpec), direction.description, edge.toString(""), restrTxt);
                    }

                    // Update the administration.
                    pred.free();
                    pred = newPred;
                    changed = true;
                    iterChanged = true;
                    remainingEdges = edges.size(); // Change found, reset the counter.
                }
            }

            // Iteration completed.
            if (dbgEnabled) {
                if (!iterChanged) {
                    cifBddSpec.settings.getDebugOutput().line("No change this iteration.");
                }
                cifBddSpec.settings.getDebugOutput().dec();
            }
        }
        return pair(pred, changed);
    }

    /**
     * Computes the set of reachable states from the given predicate by using the saturation strategy. The use of
     * saturation requires that a saturation instance has been configured using {@link #setSaturationInstance}.
     *
     * @param pred The predicate to which to apply the reachability. This predicate should not be used after this
     *     method, as it may have been {@link BDD#free freed} by this method. Instead, continue with the predicate
     *     returned by this method as part of the return value.
     * @param edges The CIF/BDD edges to apply.
     * @return The fixed point result of the reachability computation, together with an indication of whether the
     *     predicate was changed as a result of the reachability computation. Instead of a pair, {@code null} is
     *     returned if termination was requested.
     */
    private Pair<BDD, Boolean> performReachabilitySaturation(BDD pred, List<CifBddEdge> edges) {
        Assert.notNull(saturationInstance, "Expected a saturation instance number to have been configured.");

        // Sort the list of (distinct) edges as required by the saturation algorithm.
        Map<CifBddEdge, BDD> edgeSupport = edges.stream().distinct()
                .collect(Collectors.toMap(edge -> edge, edge -> edge.updateGuardSupport.toBDD()));

        List<CifBddEdge> sortedEdges = edges.stream()
                .sorted(Comparator.comparing(edge -> edgeSupport.get(edge).level()))
                .toList();

        edgeSupport.values().forEach(BDD::free);

        if (cifBddSpec.settings.getTermination().isRequested()) {
            return null;
        }

        // Obtain the BDDs of the transition relations and their variable support sets.
        List<BDD> sortedRelations = sortedEdges.stream().map(edge -> edge.updateGuard).toList();
        List<BDDVarSet> sortedVars = sortedEdges.stream().map(edge -> edge.updateGuardSupport).toList();

        if (cifBddSpec.settings.getTermination().isRequested()) {
            return null;
        }

        // Compute all reachable states using saturation.
        BDD result;

        if (direction == CifBddEdgeApplyDirection.FORWARD) {
            if (restriction == null) {
                result = pred.saturationForward(sortedRelations, sortedVars, saturationInstance);
            } else {
                result = pred.boundedSaturationForward(restriction, sortedRelations, sortedVars, saturationInstance);
            }
        } else {
            if (restriction == null) {
                result = pred.saturationBackward(sortedRelations, sortedVars, saturationInstance);
            } else {
                result = pred.boundedSaturationBackward(restriction, sortedRelations, sortedVars, saturationInstance);
            }
        }

        // Determine whether any additional reachable states have been found.
        boolean changed = !pred.equals(result);
        pred.free();

        return Pair.pair(result, changed);
    }
}
