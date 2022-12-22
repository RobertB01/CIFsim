//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrdererHelper;
import org.eclipse.escet.common.java.Assert;

/**
 * Variable ordering algorithm that applies multiple other algorithms sequentially, each to the result of the previous
 * algorithm.
 */
public class SequentialVarOrderer implements VarOrderer {
    /** The sequence of algorithms to apply, in order. */
    private final List<VarOrderer> algorithms;

    /**
     * Constructor for the {@link SequentialVarOrderer} class.
     *
     * @param algorithms The sequence of algorithms to apply, in order. Must be at least two algorithms.
     */
    public SequentialVarOrderer(List<VarOrderer> algorithms) {
        this.algorithms = algorithms;
        Assert.check(algorithms.size() >= 2);
    }

    @Override
    public List<SynthesisVariable> order(VarOrdererHelper helper, List<SynthesisVariable> inputOrder,
            boolean dbgEnabled, int dbgLevel)
    {
        // Debug output before applying the algorithms.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying %d algorithms, sequentially:", algorithms.size());
        }

        // Initialize variable order to the input variable order.
        List<SynthesisVariable> order = inputOrder;

        // Apply each algorithm, in order, to the result of the previous algorithm.
        for (int i = 0; i < algorithms.size(); i++) {
            // Separate debug output of this algorithm from that of the previous one.
            if (i > 0 && dbgEnabled) {
                helper.dbg();
            }

            // Apply algorithm and update the current order.
            VarOrderer algorithm = algorithms.get(i);
            order = algorithm.order(helper, order, dbgEnabled, dbgLevel + 1);
        }

        // Return the resulting variable order.
        return order;
    }
}
