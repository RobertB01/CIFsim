//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.varorder;

import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrdererHelper;
import org.eclipse.escet.common.java.Assert;

/** Variable ordering algorithm that applies multiple other algorithms, and picks the one with the lowest total span. */
public class ChoiceVarOrderer implements VarOrderer {
    /** The name of the choice-based algorithm, or {@code null} if no name is given. */
    private final String name;

    /** The algorithms to apply. At least two algorithms. */
    private final List<VarOrderer> algorithms;

    /**
     * Constructor for the {@link ChoiceVarOrderer} class. Does not name the choice-based algorithm.
     *
     * @param algorithms The sequence of algorithms to apply. Must be at least two algorithms.
     */
    public ChoiceVarOrderer(List<VarOrderer> algorithms) {
        this(null, algorithms);
    }

    /**
     * Constructor for the {@link ChoiceVarOrderer} class.
     *
     * @param name The name of the choice-based algorithm.
     * @param algorithms The sequence of algorithms to apply. Must be at least two algorithms.
     */
    public ChoiceVarOrderer(String name, List<VarOrderer> algorithms) {
        this.name = name;
        this.algorithms = algorithms;
        Assert.check(algorithms.size() >= 2);
    }

    @Override
    public List<SynthesisVariable> order(VarOrdererHelper helper, List<SynthesisVariable> inputOrder,
            boolean dbgEnabled, int dbgLevel)
    {
        // Debug output before applying the algorithms.
        if (dbgEnabled) {
            if (name == null) {
                helper.dbg(dbgLevel, "Applying multiple algorithms, and choosing the best result:");
            } else {
                helper.dbg(dbgLevel, "Applying %s algorithm:", name);
            }
        }

        // Initialize best order (with lowest span).
        List<SynthesisVariable> bestOrder = null;
        long bestSpan = Long.MAX_VALUE;

        // Apply each algorithm.
        for (int i = 0; i < algorithms.size(); i++) {
            // Separate debug output of this algorithm from that of the previous one.
            if (i > 0 && dbgEnabled) {
                helper.dbg();
            }

            // Apply algorithm. Each algorithm is independently applied to the input variable order.
            VarOrderer algorithm = algorithms.get(i);
            List<SynthesisVariable> algoOrder = algorithm.order(helper, inputOrder, dbgEnabled, dbgLevel + 1);

            // Update best order (with lowest span).
            long algoSpan = helper.computeTotalSpanForVarOrder(algoOrder);
            if (algoSpan < bestSpan) {
                bestOrder = algoOrder;
                bestSpan = algoSpan;

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
