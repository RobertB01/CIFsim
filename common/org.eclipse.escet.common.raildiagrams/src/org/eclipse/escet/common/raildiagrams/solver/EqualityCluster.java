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
import static org.eclipse.escet.common.java.Lists.copy;
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;

/**
 * Set of variables that are equal to each other, modulo some offset.
 *
 * <p>
 * To avoid wasting computational effort, first variables are added using {@link #add}. When that phase is done,
 * relations are added, leading to computing relative offsets of all variables to the common variable. The caller should
 * ensure the set provided relations only contain variables where both sides are already in the cluster, and that the
 * set is complete, eventually linking all variables to the shared common variable.
 * </p>
 */
public class EqualityCluster {
    /**
     * Variables in the cluster, with offsets between the variable and some arbitrary common variable that may or may
     * not be part of the cluster.
     *
     * <p>
     * For each variable {@code v}, {@code v + variables.get(v)} reaches the common variable.
     * </p>
     */
    public Map<Variable, Double> variables;

    /**
     * Equality relations between variables of the cluster.
     *
     * <p>
     * Managed by external code, used during the call to {@link Solver#constructEqualityClusters}.
     * </p>
     */
    public List<EqRelation> equalities;

    /**
     * List of cluster less-equal relations where this has the bigger value 'b' in the relation.
     */
    public List<LeClusterRelation> remoteSmallers = list();

    /**
     * List of cluster less-equal relations where this has the smaller value 'a' in the relation.
     */
    public List<LeClusterRelation> remoteBiggers = list();

    /** Constructor of the {@link EqualityCluster} class. */
    public EqualityCluster() {
        variables = map();
        equalities = list();
    }

    /**
     * Add a variable to the cluster.
     *
     * @param var Variable to add.
     */
    public void add(Variable var) {
        Assert.check(!variables.containsKey(var));
        variables.put(var, null);
    }

    /**
     * Compute offsets of all variables from the equality relations.
     *
     * <p>
     * The caller should ensure the set provided relations only contain variables where both sides are already in the
     * cluster, and that the set is complete, eventually linking all variables to the shared common variable.
     * </p>
     *
     * @param dumpSolving Whether to dump details of solving the position equations.
     */
    public void initialize(boolean dumpSolving) {
        // Singleton equality cluster have no equations to initialize from.
        if (equalities.isEmpty()) {
            Assert.check(variables.size() == 1);
            for (Entry<Variable, Double> entry: variables.entrySet()) {
                entry.setValue(0.0);
                break;
            }
            return;
        }

        // Clusters with 2 or more variables (1 equality implies at least 2 variables)
        // get initialized by picking one variable as the base (0 distance to the common C value)
        // and recursively computing distance of all other variables from it.
        List<EqRelation> curList = copy(equalities);
        List<EqRelation> nextList = listc(curList.size());
        boolean first = true;

        // Use the relations to couple an uninitialized variable to an already initialized variable through its
        // relation.
        // In worst case, the loop has quadratic complexity, but the clusters are assumed to be small.
        while (!curList.isEmpty()) {
            nextList.clear();
            for (EqRelation eqRel: curList) {
                if (first) {
                    // A + offsetA = C <-> A = C - offsetA
                    // B + offsetB = C <-> B = C - offsetB
                    //
                    // A + eqRel.offset = B
                    // . . . . <-> C - offsetA + eqRel.offset = C - offsetB
                    // . . . . <-> eqRel.offset - offsetA = -offsetB
                    // offsetB = 0
                    // . . . . <-> eqRel.offset - offsetA = 0
                    // . . . . <-> eqRel.offset = offsetA
                    double offsetB = 0;
                    double offsetA = eqRel.offset;
                    variables.put(eqRel.a, offsetA);
                    variables.put(eqRel.b, offsetB);
                    Assert.check(Math.abs(-offsetA + eqRel.offset + offsetB) < Solver.EPSILON);
                    if (dumpSolving && dodbg()) {
                        dbg();
                        dbg("%s:", eqRel);
                        idbg();
                        dbg("init-eq, first: %s = %.1f, %s = %.1f", eqRel.a, 100 - offsetA, eqRel.b, 100 - offsetB);
                        ddbg();
                    }
                    first = false;
                    // eqRel completely done.
                    continue;
                }
                // Regular processing.
                Double offsetA = variables.get(eqRel.a);
                Double offsetB = variables.get(eqRel.b);
                if (offsetA == null) {
                    if (offsetB == null) {
                        nextList.add(eqRel); // Keep the entry for the next iteration.
                        continue;
                    } else {
                        // EqRel: A + eqRel.offset = B
                        // Cluster: A + offsetA = C
                        // . . . . .B + offsetB = C
                        //
                        // A + offsetA = A
                        // <-> A = C - offsetA
                        // B + offsetB = C
                        // . . . <-> B = C - offsetB
                        // A + eqRel.offset = B
                        // . . . <-> C - offsetA + eqRel.offset = C - offsetB
                        // . . . <-> eqRel.offset - offsetA == -offsetB
                        // . . . <-> -offsetA = -offsetB - eqRel.offset
                        // . . . <-> offsetA = -(-offsetB - eqRel.offset)
                        // = offsetB + eqRel.offset
                        offsetA = offsetB + eqRel.offset;
                        variables.put(eqRel.a, offsetA);
                        Assert.check(Math.abs(-offsetA + eqRel.offset + offsetB) < Solver.EPSILON);
                        if (dumpSolving && dodbg()) {
                            dbg();
                            dbg("[add-A] %s:", eqRel);
                            idbg();
                            dbg("init-eq, new a: %s = %1f, %s = %.1f", eqRel.a, 100 - offsetA, eqRel.b, 100 - offsetB);
                            ddbg();
                        }
                        continue;
                    }
                } else {
                    if (offsetB == null) {
                        // EqRel: A + eqRel.offset = B
                        // Cluster: A + offsetA = C
                        // . . . . .B + offsetB = C
                        //
                        // A + offsetA = C
                        // . . . <-> A = C - offsetA
                        // B + offsetB = C
                        // . . . <-> B = C - offsetB
                        // A + eqRel.offset = B
                        // . . . <-> C - offsetA + eqRel.offset = C - offsetB
                        // . . . <-> eqRel.offset - offsetA = -offsetB
                        // . . . <-> offsetB = offsetA - eqRel.offset
                        offsetB = offsetA - eqRel.offset;
                        variables.put(eqRel.b, offsetB);
                        Assert.check(Math.abs(-offsetA + eqRel.offset + offsetB) < Solver.EPSILON);
                        if (dumpSolving && dodbg()) {
                            dbg();
                            dbg("[add-B] %s:", eqRel);
                            idbg();
                            dbg("init-eq, new b: %s = %1f, %s = %.1f", eqRel.a, 100 - offsetA, eqRel.b, 100 - offsetB);
                            ddbg();
                        }
                        continue;
                    } else {
                        // Both are here. There are cycles in the equality relations.
                        if (dumpSolving) {
                            dumpEqualityCycle(eqRel);
                        }

                        Assert.check(Math.abs(-offsetA + eqRel.offset + offsetB) < Solver.EPSILON,
                                fmt("eqRel=%s, offsetA=%f, offsetB=%f", eqRel, offsetA, offsetB));
                        Assert.check(Math.abs(offsetA - offsetB) < Solver.EPSILON,
                                fmt("eqRel=%s, offsetA=%f, offsetB=%f", eqRel, offsetA, offsetB));
                        // eqRel completely done, drop it.
                        continue;
                    }
                }
            }
            Assert.check(nextList.size() < curList.size()); // There must be progress!

            // Swap curList and nextList for processing the skipped relations in the next iteration.
            List<EqRelation> tmpList = curList;
            curList = nextList;
            nextList = tmpList;
        }
    }

    /**
     * Dump the data in the equality cluster.
     *
     * @param name Name of the node performing a solve.
     */
    public void dump(String name) {
        if (!dodbg()) {
            return;
        }

        dbg("Equality cluster of %s:", name);
        idbg();
        for (Entry<Variable, Double> entry: variables.entrySet()) {
            dbg("C == %s + %s", entry.getKey(), entry.getValue());
        }
        ddbg();
    }

    /**
     * Dump an equality dependency cycle to the debug output.
     *
     * @param eqRel Equality relation that introduced a cycle.
     */
    private void dumpEqualityCycle(EqRelation eqRel) {
        List<Variable> addedVars = list();
        Set<EqRelation> addedRels = set();
        Map<Variable, EqRelation> relByVar = map();
        addedVars.add(eqRel.a);
        int idx = findCycle(addedVars, addedRels, relByVar);
        if (idx >= 0) {
            dbg("Cycle with %s:", addedVars.get(idx));
            idbg();
            for (; idx < addedVars.size(); idx++) {
                Variable w = addedVars.get(idx);
                EqRelation r = relByVar.get(w);
                dbg("%s due to %s", w, r != null ? r : "-");
            }
            ddbg();
        }
    }

    /**
     * Find a cycle in the relation dependencies.
     *
     * @param addedVars Added variables.
     * @param addedRels Added relations.
     * @param relByVar Relations ordered by variables.
     * @return Index of a variable in a cycle, or {@code -1} if no cycle was found.
     */
    private int findCycle(List<Variable> addedVars, Set<EqRelation> addedRels, Map<Variable, EqRelation> relByVar) {
        Variable v = last(addedVars);
        for (EqRelation eqRel: equalities) {
            if (eqRel.a != v && eqRel.b != v) {
                continue;
            }
            if (addedRels.contains(eqRel)) {
                continue;
            }
            Variable w = (eqRel.a == v) ? eqRel.b : eqRel.a;
            int idx = addedVars.indexOf(w);
            if (idx >= 0) {
                return idx;
            }

            addedRels.add(eqRel);
            addedVars.add(w);
            relByVar.put(w, eqRel);
            idx = findCycle(addedVars, addedRels, relByVar);
            if (idx >= 0) {
                return idx;
            }
            addedVars.remove(addedVars.size() - 1);
            addedRels.remove(eqRel);
            relByVar.remove(w);
        }
        return -1;
    }

    /**
     * Verify that the less-equal relation holds between two variables in this equality cluster.
     *
     * <p>
     * May only be used after the cluster has been initialized.
     * </p>
     *
     * @param leRel Less-equal relation to check.
     * @param dumpSolving Whether to dump details of solving the position equations.
     */
    public void checkLeRelation(LeRelation leRel, boolean dumpSolving) {
        if (dumpSolving && dodbg()) {
            dbg("checkLEReleation: %s", leRel);
        }
        double offsetA = variables.get(leRel.a);
        double offsetB = variables.get(leRel.b);
        // A + offsetA = C
        // B + offsetB = C
        // A + leRel.lowBound <= B
        //
        // A + leRel.lowBound <= B
        // . . . <-> C - offsetA + leRel.lowBound <= C - offsetB
        // . . . <-> -offsetA + leRel.lowBound <= -offsetB
        // . . . <-> leRel.lowBound - offsetA <= -offsetB
        double leftSide = leRel.lowBound - offsetA;
        double rightSide = -offsetB;
        Assert.check(leftSide <= rightSide + Solver.EPSILON, fmt("Failed relation: %s", leRel));
    }

    /**
     * Assign all variables of the equality cluster from a computed value of the common C value.
     *
     * @param varValues Storage of variable assignments.
     * @param cValue The common C value to use for assigning variables.
     * @param dumpSolving Whether to dump details of solving the position equations.
     */
    public void assignVariables(double[] varValues, double cValue, boolean dumpSolving) {
        for (Entry<Variable, Double> entry: variables.entrySet()) {
            double varValue = cValue - entry.getValue();
            Assert.check(varValue > -Solver.EPSILON); // Variable should be non-negative.

            Variable var = entry.getKey();
            int varIndex = var.index;
            Assert.check(varValues[varIndex] <= -1 || Math.abs(varValues[varIndex] - varValue) < Solver.EPSILON);
            varValues[varIndex] = varValue;
        }
        // Dump the variables in increasing value.
        if (dumpSolving && dodbg()) {
            List<Variable> sortedVars = listc(variables.size());
            for (Variable v: variables.keySet()) {
                sortedVars.add(v);
            }
            Collections.sort(sortedVars, new VarValueComparer(varValues));
            idbg();
            for (Variable v: sortedVars) {
                dbg("%s = %f", v, varValues[v.index]);
            }
            ddbg();
        }
    }

    /** Comparator for sorting variables in increasing order. */
    private class VarValueComparer implements Comparator<Variable> {
        /** Values of the variables. */
        private final double[] varValues;

        /**
         * Constructor of the {@link VarValueComparer} class.
         *
         * @param varValues Values of the variables.
         */
        public VarValueComparer(double[] varValues) {
            this.varValues = varValues;
        }

        @Override
        public int compare(Variable v1, Variable v2) {
            double val1 = varValues[v1.index];
            double val2 = varValues[v2.index];
            if (val1 < val2) {
                return -1;
            }
            if (val1 == val2) {
                return 0;
            }
            return 1;
        }
    }

    /**
     * Compute the smallest common C value such that all variables are non-negative.
     *
     * @return Smallest C value such that all variables become non-negative.
     */
    public double getMinimalValidC() {
        Assert.check(remoteSmallers.isEmpty());

        // For all variables, it holds that v + offset_v == c, with v >= 0.
        // c must be 'positive enough' to allow for all offsets in the cluster, thus c == max offset_i.
        double maxOffset = 0;
        boolean first = true;
        for (double offset: variables.values()) {
            if (first || maxOffset < offset) {
                maxOffset = offset;
            }
            first = false;
        }
        Assert.check(!first);
        return maxOffset;
    }

    /**
     * Compute the minimal valid C value for this cluster from the variable assignments derived in the preceding smaller
     * clusters.
     *
     * @param varValues Assigned variables.
     * @return Minimal valid C.
     */
    public double getMinimalValidC(double[] varValues) {
        Assert.check(!remoteSmallers.isEmpty());

        double minC = 0;
        boolean first = true;
        for (LeClusterRelation leClusterRel: remoteSmallers) {
            LeRelation leRel = leClusterRel.leRelation;
            // Compute smallest valid C value for this leRelation.
            double biggerRelValue = varValues[leRel.a.index] + leRel.lowBound;
            double cRelValue = biggerRelValue + variables.get(leRel.b);

            if (first || cRelValue > minC) {
                minC = cRelValue;
            }
            first = false;
        }
        Assert.check(!first);
        return minC;
    }
}
