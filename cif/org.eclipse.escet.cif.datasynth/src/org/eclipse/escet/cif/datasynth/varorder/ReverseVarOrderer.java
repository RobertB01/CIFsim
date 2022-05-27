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

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrdererHelper;
import org.eclipse.escet.common.java.ArrayUtils;

/** Variable ordering algorithm that returns the reverse order of another algorithm. */
public class ReverseVarOrderer implements VarOrderer {
    /** The algorithm to apply. */
    private final VarOrderer algorithm;

    /**
     * Constructor for the {@link ReverseVarOrderer} class.
     *
     * @param algorithm The algorithm to apply.
     */
    public ReverseVarOrderer(VarOrderer algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public SynthesisVariable[] order(VarOrdererHelper helper, SynthesisVariable[] inputOrder, boolean dbgEnabled,
            int dbgLevel)
    {
        // Debug output before applying the algorithms.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying algorithm, and reversing its result:");
        }

        // Apply the algorithm.
        SynthesisVariable[] order = algorithm.order(helper, inputOrder, dbgEnabled, dbgLevel + 1);

        // Reverse the order.
        SynthesisVariable[] reverseOrder = ArrayUtils.reverse(order);

        // Debug output after applying the algorithms.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Reversed the variable order.");
            helper.dbgTotalSpan(dbgLevel, reverseOrder, "reversed");
        }

        // Return the resulting variable order.
        return reverseOrder;
    }
}
