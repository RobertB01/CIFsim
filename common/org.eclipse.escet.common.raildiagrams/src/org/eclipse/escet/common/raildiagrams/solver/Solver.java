//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.solver;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dodbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;

/**
 * Solver that assigns non-negative real values to variables such that all provided relations hold.
 */
public class Solver {
    /** Allowed deviation of a value due to rounding errors. */
    public static final double EPSILON = 1e-4;

    /** Variables to assign. */
    private List<Variable> variables = list();

    /**
     * Solution of the relations, value of variable {@code variables.get(i)} is in {@code solution[i]}.
     */
    private double[] solution;

    /** Relations between variables to obey. */
    private List<VariableRelation> relations = list();

    /** Found clusters of variables linked through equality relations. */
    private List<EqualityCluster> equalityClusters;

    /** Mapping of variables to their containing equality cluster. */
    private Map<Variable, EqualityCluster> varsToCluster;

    /** Constructor of the {@link Solver} class. */
    public Solver() {
        // Nothing to do.
    }

    /**
     * Make a new variable.
     *
     * @param name Name of the new variable, may be {@code null}.
     * @return The created variable.
     */
    public Variable newVar(String name) {
        Variable v = new Variable(name, variables.size());
        variables.add(v);
        return v;
    }

    /**
     * Add a relation between two variables to the solver.
     *
     * @param relation Relation to add.
     */
    public void addRelation(VariableRelation relation) {
        relations.add(relation);
    }

    /**
     * Add equality relation {@code a + offset == b}.
     *
     * @param a First (smaller) variable in the relation.
     * @param offset Fixed differences between both variables.
     * @param b Second (bigger) variable in the relation.
     */
    public void addEq(Variable a, double offset, Variable b) {
        EqRelation eqRel = new EqRelation(a, offset, b);
        addRelation(eqRel);
    }

    /**
     * Add less-equal relation {@code a + lowBound &lt;= b}.
     *
     * @param a First (smaller) variable in the relation.
     * @param lowBound Fixed minimal differences between both variables.
     * @param b Second (bigger) variable in the relation.
     */
    public void addLe(Variable a, double lowBound, Variable b) {
        LeRelation leRel = new LeRelation(a, lowBound, b);
        addRelation(leRel);
    }

    /** Dump the variables and relations for debugging. */
    public void dumpRelations() {
        if (!dodbg()) {
            return;
        }

        dbg("Relations:");
        idbg();
        for (VariableRelation r: relations) {
            dbg("%s", r);
        }
        ddbg();
        dbg();
    }

    /**
     * Assign non-negative values to the variables such that a smaller value cannot be assigned without violating the
     * constraint relations.
     *
     * <p>
     * May only be used after all variables have been created, and all relations have been added.
     * </p>
     *
     * @param name Name of the node containing the variables and relations, for debugging purposes.
     * @param config Configuration of the program.
     */
    public void solve(String name, Configuration config) {
        boolean dumpSolving = config.getDebugSetting(DebugDisplayKind.SOLVER);

        equalityClusters = list();
        varsToCluster = map();

        // Create equality clusters, and initialize each of them.
        constructEqualityClusters();
        addSingletonEqualities();
        for (EqualityCluster cluster: equalityClusters) {
            cluster.initialize(dumpSolving);
        }
        if (dumpSolving) {
            for (EqualityCluster cluster: equalityClusters) {
                cluster.dump(name);
            }
        }

        // Build a cluster graph with less-equal relations, and verify it has no cycles.
        makeLessEqualGraph(dumpSolving);
        checkNoCycles();

        solution = assignValues(dumpSolving); // And finally, assign values to the variables.
    }

    /**
     * Get the computed value of a variable.
     *
     * <p>
     * May only be used after calling {@link #solve}.
     * </p>
     *
     * @param var Variable being queried.
     * @return Value of the queried variable.
     */
    public double getVarValue(Variable var) {
        Assert.check(var == variables.get(var.index)); // Should be a variable of the solver.
        return solution[var.index];
    }

    /** Create clusters of variables connected through equality relations. */
    private void constructEqualityClusters() {
        int numEqRels = 0;
        int numClusters = 0;
        for (VariableRelation rel: relations) {
            if (!(rel instanceof EqRelation)) {
                continue;
            }

            numEqRels++;
            EqRelation eqRel = (EqRelation)rel;
            EqualityCluster clA = varsToCluster.get(eqRel.a);
            EqualityCluster clB = varsToCluster.get(eqRel.b);
            if (clA == null) {
                if (clB == null) { // Both not in a cluster, make a new one.
                    numClusters++;
                    EqualityCluster cluster = new EqualityCluster();
                    cluster.add(eqRel.a);
                    cluster.add(eqRel.b);
                    cluster.equalities.add(eqRel);
                    equalityClusters.add(cluster);
                    varsToCluster.put(eqRel.a, cluster);
                    varsToCluster.put(eqRel.b, cluster);
                } else { // A is not part of a cluster, B is -> Add A to cluster of B.
                    clB.add(eqRel.a);
                    clB.equalities.add(eqRel);
                    varsToCluster.put(eqRel.a, clB);
                }
            } else if (clB == null) {
                clA.add(eqRel.b); // A is part of a cluster, B is not -> Add B to cluster of A.
                clA.equalities.add(eqRel);
                varsToCluster.put(eqRel.b, clA);
            } else if (clA == clB) { // Already in the same cluster.
                clA.equalities.add(eqRel);
            } else {
                // Both in different clusters, merge clA to clB.
                for (Variable va: clA.variables.keySet()) {
                    clB.add(va);
                    varsToCluster.put(va, clB);
                }
                clB.equalities.addAll(clA.equalities);
                equalityClusters.remove(clA);
                numClusters--;
                clB.equalities.add(eqRel);
            }
        }
        // Compare counts with stored results as a basic sanity check.
        Assert.check(numClusters == equalityClusters.size());
        int countedVars = 0;
        int countedEqRels = 0;
        for (EqualityCluster cluster: equalityClusters) {
            countedVars += cluster.variables.size();
            countedEqRels += cluster.equalities.size();
        }
        Assert.check(countedVars == varsToCluster.size());
        Assert.check(countedEqRels == numEqRels);
    }

    /**
     * Add equality clusters with a single variable for all variables that are not involved in an equality relation.
     */
    private void addSingletonEqualities() {
        for (Variable var: variables) {
            if (!varsToCluster.containsKey(var)) {
                EqualityCluster cluster = new EqualityCluster();
                cluster.add(var);
                equalityClusters.add(cluster);
                varsToCluster.put(var, cluster);
            }
        }
    }

    /**
     * Assign the {@link LeRelation}s to the found (and initialized) equality clusters.
     *
     * @param dumpSolving Whether to dump details of solving the position equations.
     */
    private void makeLessEqualGraph(boolean dumpSolving) {
        for (VariableRelation rel: relations) {
            if (!(rel instanceof LeRelation)) {
                continue;
            }

            LeRelation leRel = (LeRelation)rel;
            EqualityCluster clA = varsToCluster.get(leRel.a);
            EqualityCluster clB = varsToCluster.get(leRel.b);
            Assert.notNull(clA, fmt("Variable '%s' is not a variable here", leRel.a));
            Assert.notNull(clB, fmt("Variable '%s' is not a variable here", leRel.b));
            if (clA == clB) {
                // Both variables are in the same equality cluster, verify it.
                clA.checkLeRelation(leRel, dumpSolving);
                continue;
            }
            LeClusterRelation clLeRel = new LeClusterRelation(leRel, clA, clB);
            clA.remoteBiggers.add(clLeRel);
            clB.remoteSmallers.add(clLeRel);
        }
    }

    /** Verify there are no cycles in the {@link LeClusterRelation}. */
    private void checkNoCycles() {
        Set<EqualityCluster> safeClusters = setc(equalityClusters.size()); // Clusters that are cycle-free.
        List<EqualityCluster> stack = listc(equalityClusters.size()); // Exploration stack.

        // Indices of stacked clusters to their index.
        Map<EqualityCluster, Integer> stackIndices = mapc(equalityClusters.size());

        int index = 0;
        stack.add(null);
        for (EqualityCluster cluster: equalityClusters) {
            if (safeClusters.contains(cluster)) {
                continue; // Already checked.
            }

            stack.set(index, cluster);
            stackIndices.put(cluster, index);
            Integer cycleStart = findCycle(stack, stackIndices, safeClusters);
            Assert.check(cycleStart == null);
            stackIndices.remove(cluster);
            safeClusters.add(cluster);
        }
    }

    /**
     * Try to find a cycle by a DFS, exploring the graph in the 'smaller' direction.
     *
     * @param stack Clusters being explored currently. Top entry should be expanded in this call. Top is assumed not to
     *     be in the safe set.
     * @param stackIndices Mapping of clusters on the stack to their index.
     * @param safeSet Set of clusters that are known not to be part of a cycle.
     * @return Index number of a cluster that is part of a cycle.
     */
    private Integer findCycle(List<EqualityCluster> stack, Map<EqualityCluster, Integer> stackIndices,
            Set<EqualityCluster> safeSet)
    {
        // Stack grows downward, so 'top' is the last value, which is expanded here.
        EqualityCluster top = last(stack);

        // This call uses stack[index].
        int index = stack.size();
        stack.add(null);

        // For all locally smaller clusters
        for (LeClusterRelation leRel: top.remoteSmallers) {
            EqualityCluster smallerCl = leRel.smallerCluster;
            // If the cluster safe already?
            if (safeSet.contains(smallerCl)) {
                continue;
            }

            // Check for cycle.
            Integer smallerIndex = stackIndices.get(smallerCl);
            if (smallerIndex != null) {
                return smallerIndex;
            }

            // Make a call to find cycles from the descendant.
            stack.set(index, smallerCl);
            stackIndices.put(smallerCl, index);

            // Explore child cluster, and exit when a cycle is found.
            smallerIndex = findCycle(stack, stackIndices, safeSet);
            if (smallerIndex != null) {
                return smallerIndex;
            }

            stackIndices.remove(smallerCl);
            safeSet.add(smallerCl);
            // Conceptually, stack top should be null again, but it gets overwritten or
            // deleted soon.
        }
        stack.remove(index);
        return null;
    }

    /**
     * Walk along the equality clusters, and decide about variable values.
     *
     * @param dumpSolving Whether to dump details of solving the position equations.
     * @return Values assigned to the variables.
     */
    private double[] assignValues(boolean dumpSolving) {
        // Assigned values to the variables, negative value means no value has been
        // assigned yet.
        double[] varValues = new double[variables.size()];
        Arrays.fill(varValues, -10);

        // Walk over the less-equal graph, from 'small' to 'big', where a cluster can be
        // assigned
        // value when all its smaller remotes have been assigned a value already.
        // As there are no cycles this will produce the minimal answer.

        // Set of clusters that need to be assigned next.
        Set<EqualityCluster> activeClusters = set();
        for (EqualityCluster cluster: equalityClusters) {
            if (cluster.remoteSmallers.isEmpty()) {
                activeClusters.add(cluster);
            }
        }

        Set<EqualityCluster> nextActiveClusters = set(); // Active clusters of the next iteration.
        Set<EqualityCluster> assignedClusters = setc(equalityClusters.size()); // Clusters that are done.

        for (;;) {
            if (activeClusters.isEmpty()) {
                break;
            }

            nextActiveClusters.clear();
            boolean progress = false;
            for (EqualityCluster cluster: activeClusters) {
                if (cluster.remoteSmallers.isEmpty()) {
                    // First cluster in the chain.
                    double cValue = cluster.getMinimalValidC();
                    if (dumpSolving) {
                        dbg("Smallest cluster: minimal C = %f", cValue);
                    }
                    cluster.assignVariables(varValues, cValue, dumpSolving);
                    assignedClusters.add(cluster);
                    addSuccessorClusters(cluster.remoteBiggers, nextActiveClusters);
                    progress = true;
                } else if (allSmallerDone(cluster.remoteSmallers, assignedClusters)) {
                    // All predecessors done.
                    double cValue = cluster.getMinimalValidC(varValues);
                    if (dumpSolving) {
                        dbg("next cluster: minimal C = %f", cValue);
                    }
                    cluster.assignVariables(varValues, cValue, dumpSolving);
                    assignedClusters.add(cluster);
                    addSuccessorClusters(cluster.remoteBiggers, nextActiveClusters);
                    progress = true;
                } else {
                    nextActiveClusters.add(cluster); // Keep cluster active for the next iteration.
                }
            }
            Assert.check(progress);

            // Swap active and next active.
            Set<EqualityCluster> tempClusters = activeClusters;
            activeClusters = nextActiveClusters;
            nextActiveClusters = tempClusters;
        }

        // Paranoia checks.
        Assert.check(assignedClusters.size() == equalityClusters.size());

        // Keep some distance from 0 to avoid false positive from rounding errors
        for (int i = 0; i < varValues.length; i++) {
            Assert.check(varValues[i] > -1, fmt("variable %s fails, value=%f", variables.get(i), varValues[i]));
        }

        return varValues;
    }

    /**
     * Verify whether all equality clusters at the smaller side of the LE relation are resolved.
     *
     * @param remoteSmallers Equality clusters to verify having been done.
     * @param assignedClusters Clusters that have been resolved already.
     * @return Whether all remote smaller equality clusters have been resolved.
     */
    private static boolean allSmallerDone(List<LeClusterRelation> remoteSmallers,
            Set<EqualityCluster> assignedClusters)
    {
        for (LeClusterRelation leRel: remoteSmallers) {
            if (!assignedClusters.contains(leRel.smallerCluster)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Add the 'bigger' side of the provided next bigger set of equality clusters to the provided active set.
     *
     * @param nextBiggers Successor equality clusters.
     * @param actives Active clusters for which assignment is attempted.
     */
    private static void addSuccessorClusters(List<LeClusterRelation> nextBiggers, Set<EqualityCluster> actives) {
        for (LeClusterRelation nextBigger: nextBiggers) {
            actives.add(nextBigger.biggerCluster);
        }
    }
}
