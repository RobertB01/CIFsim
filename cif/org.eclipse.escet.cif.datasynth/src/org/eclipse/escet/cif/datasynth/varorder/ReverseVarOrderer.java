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

import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.RelationsKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrdererHelper;

/** Variable ordering algorithm that returns the reverse order of another algorithm. */
public class ReverseVarOrderer implements VarOrderer {
    /** The algorithm to apply. */
    private final VarOrderer algorithm;

    /** The relations to use to compute metric values. */
    private final RelationsKind relationsKind;

    /**
     * Constructor for the {@link ReverseVarOrderer} class.
     *
     * @param algorithm The algorithm to apply.
     * @param relationsKind The relations to use to compute metric values.
     */
    public ReverseVarOrderer(VarOrderer algorithm, RelationsKind relationsKind) {
        this.algorithm = algorithm;
        this.relationsKind = relationsKind;
    }

    @Override
    public List<SynthesisVariable> order(VarOrdererHelper helper, List<SynthesisVariable> inputOrder,
            boolean dbgEnabled, int dbgLevel)
    {
        // Debug output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying algorithm, and reversing its result:");
        }

        // Apply the algorithm.
        List<SynthesisVariable> order = algorithm.order(helper, inputOrder, dbgEnabled, dbgLevel + 1);

        // Reverse the order.
        Collections.reverse(order);

        // Debug output after applying the algorithm.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Reversed the variable order.");
            helper.dbgMetricsForVarOrder(dbgLevel, order, "reversed", relationsKind);
        }

        // Return the resulting variable order.
        return order;
    }
}
