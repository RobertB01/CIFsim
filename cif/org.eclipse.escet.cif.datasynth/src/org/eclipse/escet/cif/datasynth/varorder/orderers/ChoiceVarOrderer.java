//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.varorder.orderers;

import java.util.BitSet;
import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.RelationsKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;
import org.eclipse.escet.cif.datasynth.varorder.metrics.VarOrderMetric;
import org.eclipse.escet.cif.datasynth.varorder.metrics.VarOrderMetricKind;
import org.eclipse.escet.common.java.Assert;

/** Variable ordering algorithm that applies multiple other algorithms, and picks the best order. */
public class ChoiceVarOrderer implements VarOrderer {
    /** The name of the choice-based algorithm, or {@code null} if no name is given. */
    private final String name;

    /** The algorithms to apply. At least two algorithms. */
    private final List<VarOrderer> algorithms;

    /** The kind of metric to use to pick the best order. */
    private final VarOrderMetricKind metricKind;

    /** The kind of relations to use to compute metric values. */
    private final RelationsKind relationsKind;

    /**
     * Constructor for the {@link ChoiceVarOrderer} class. Does not name the choice-based algorithm.
     *
     * @param algorithms The sequence of algorithms to apply. Must be at least two algorithms.
     * @param metricKind The kind of metric to use to pick the best order.
     * @param relationsKind The kind of relations to use to compute metric values.
     */
    public ChoiceVarOrderer(List<VarOrderer> algorithms, VarOrderMetricKind metricKind, RelationsKind relationsKind) {
        this(null, algorithms, metricKind, relationsKind);
    }

    /**
     * Constructor for the {@link ChoiceVarOrderer} class.
     *
     * @param name The name of the choice-based algorithm.
     * @param algorithms The sequence of algorithms to apply. Must be at least two algorithms.
     * @param metricKind The kind of metric to use to pick the best order.
     * @param relationsKind The kind of relations to use to compute metric values.
     */
    public ChoiceVarOrderer(String name, List<VarOrderer> algorithms, VarOrderMetricKind metricKind,
            RelationsKind relationsKind)
    {
        this.name = name;
        this.algorithms = algorithms;
        this.metricKind = metricKind;
        this.relationsKind = relationsKind;
        Assert.check(algorithms.size() >= 2);
    }

    @Override
    public List<SynthesisVariable> order(VarOrderHelper helper, List<SynthesisVariable> inputOrder, boolean dbgEnabled,
            int dbgLevel)
    {
        // Debug output before applying the algorithms.
        if (dbgEnabled) {
            if (name == null) {
                helper.dbg(dbgLevel, "Applying multiple algorithms, and choosing the best result:");
            } else {
                helper.dbg(dbgLevel, "Applying %s algorithm:", name);
            }
        }

        // Initialize best order (the lower the metric value the better).
        List<SynthesisVariable> bestOrder = null;
        double bestMetric = Double.POSITIVE_INFINITY;

        // Apply each algorithm.
        VarOrderMetric metric = metricKind.create();
        for (int i = 0; i < algorithms.size(); i++) {
            // Separate debug output of this algorithm from that of the previous one.
            if (i > 0 && dbgEnabled) {
                helper.dbg();
            }

            // Apply algorithm. Each algorithm is independently applied to the input variable order.
            VarOrderer algorithm = algorithms.get(i);
            List<SynthesisVariable> algoOrder = algorithm.order(helper, inputOrder, dbgEnabled, dbgLevel + 1);

            // Update best order (with lowest metric value).
            List<BitSet> hyperEdges = helper.getHyperEdges(relationsKind);
            double algoMetric = metric.computeForVarOrder(helper, algoOrder, hyperEdges);
            if (algoMetric < bestMetric) {
                bestOrder = algoOrder;
                bestMetric = algoMetric;

                if (dbgEnabled) {
                    helper.dbg(dbgLevel + 1, "Found new best variable order.");
                }
            }
        }

        // Return the best variable order.
        Assert.notNull(bestOrder);
        return bestOrder;
    }
}
