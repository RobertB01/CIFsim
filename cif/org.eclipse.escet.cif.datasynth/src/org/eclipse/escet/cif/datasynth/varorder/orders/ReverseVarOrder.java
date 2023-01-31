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

package org.eclipse.escet.cif.datasynth.varorder.orders;

import static org.eclipse.escet.common.java.Lists.reverse;
import static org.eclipse.escet.common.java.Pair.pair;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;
import org.eclipse.escet.common.java.Pair;

/** Reverse variable order. Reverses a given variable order, preserving its interleaving. */
public class ReverseVarOrder extends NonInterleavedVarOrder {
    /** The variable order to reverse. */
    private final VarOrder order;

    /**
     * Constructor for the {@link ReverseVarOrder} class.
     *
     * @param order The variable order to reverse.
     */
    public ReverseVarOrder(VarOrder order) {
        this.order = order;
    }

    @Override
    public List<Pair<SynthesisVariable, Integer>> order(VarOrderHelper helper, boolean dbgEnabled, int dbgLevel) {
        // Debug output before obtaining the variable order.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying variable order, and reversing its result:");
        }

        // Get variable order to reverse.
        List<Pair<SynthesisVariable, Integer>> orderToReverse = order.order(helper, dbgEnabled, dbgLevel + 1);

        // Debug output.
        if (dbgEnabled) {
            helper.dbg();
            helper.dbg(dbgLevel + 1, "Reversing the variable order.");
        }

        // Reverse the order.
        int maxGroupNr = orderToReverse.stream().map(p -> p.right).max(Integer::compareTo).get();
        List<Pair<SynthesisVariable, Integer>> reverseOrder = reverse(orderToReverse).stream()
                .map(p -> pair(p.left, maxGroupNr - p.right)).collect(Collectors.toList());
        return reverseOrder;
    }

    @Override
    public String toString() {
        return fmt("reverse(order=%s)", order);
    }
}
