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

package org.eclipse.escet.cif.datasynth.varorder.orderers;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.BitSet;
import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.RelationsKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;
import org.eclipse.escet.cif.datasynth.varorder.metrics.VarOrderMetric;
import org.eclipse.escet.cif.datasynth.varorder.metrics.VarOrderMetricKind;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.PermuteUtils;

/** Sliding window algorithm variable ordering heuristic. */
public class SlidingWindowVarOrderer implements VarOrderer {
    /** The maximum length of the window. Is in the range [1..12]. */
    private final int maxLen;

    /** The kind of metric to use to pick the best order. */
    private final VarOrderMetricKind metricKind;

    /** The kind of relations to use to compute metric values. */
    private final RelationsKind relationsKind;

    /**
     * Constructor for the {@link SlidingWindowVarOrderer} class.
     *
     * @param maxLen The maximum length of the window. Must be in the range [1..12].
     * @param metricKind The kind of metric to use to pick the best order.
     * @param relationsKind The kind of relations to use to compute metric values.
     */
    public SlidingWindowVarOrderer(int maxLen, VarOrderMetricKind metricKind, RelationsKind relationsKind) {
        this.maxLen = maxLen;
        this.metricKind = metricKind;
        this.relationsKind = relationsKind;
        Assert.check(maxLen >= 1);
        Assert.check(maxLen <= 12);
    }

    @Override
    public List<SynthesisVariable> order(VarOrderHelper helper, List<SynthesisVariable> inputOrder, boolean dbgEnabled,
            int dbgLevel)
    {
        // Get variable count.
        int varCnt = inputOrder.size();

        // Debug output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying sliding window algorithm.");
        }

        // Determine window length.
        int length = Math.min(maxLen, varCnt);
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Window length: %,d", length);
        }

        // Initialize current indices and metric value.
        VarOrderMetric metric = metricKind.create();
        List<BitSet> hyperEdges = helper.getHyperEdges(relationsKind);
        int[] curIndices = helper.getNewIndicesForVarOrder(inputOrder);
        double curMetricValue = metric.computeForNewIndices(curIndices, hyperEdges);
        if (dbgEnabled) {
            helper.dbgMetricsForNewIndices(dbgLevel, curIndices, "before", relationsKind);
        }

        // Process all windows.
        int[] window = new int[length];
        int permCnt = PermuteUtils.factorial(window.length);
        int[][] windowPerms = new int[permCnt][window.length];
        for (int offset = 0; offset <= (varCnt - length); offset++) {
            // Fill window and all permutations.
            System.arraycopy(curIndices, offset, window, 0, length);
            PermuteUtils.permute(window, windowPerms);

            // Compute metric value for each order.
            int bestIdx = -1;
            int[] windowIndices = curIndices.clone();
            for (int i = 0; i < windowPerms.length; i++) {
                int[] windowPerm = windowPerms[i];
                System.arraycopy(windowPerm, 0, windowIndices, offset, length);
                double windowMetricValue = metric.computeForNewIndices(windowIndices, hyperEdges);
                if (windowMetricValue < curMetricValue) { // Check for better order (with lower metric value).
                    curMetricValue = windowMetricValue;
                    bestIdx = i;
                }
            }

            // Update order if improved by this window (has lower metric value).
            if (bestIdx >= 0) {
                System.arraycopy(windowPerms[bestIdx], 0, curIndices, offset, length);

                if (dbgEnabled) {
                    helper.dbgMetricsForNewIndices(dbgLevel, curIndices,
                            fmt("window %d..%d", offset, offset + length - 1), relationsKind);
                }
            }
        }

        // Debug output after applying the algorithm.
        if (dbgEnabled) {
            helper.dbgMetricsForNewIndices(dbgLevel, curIndices, "after", relationsKind);
        }

        // Return the resulting order.
        return helper.reorderForNewIndices(curIndices);
    }

    @Override
    public String toString() {
        return fmt("slidwin(size=%d, metric=%s, relations=%s)", maxLen, VarOrderer.enumValueToParserArg(metricKind),
                VarOrderer.enumValueToParserArg(relationsKind));
    }
}
