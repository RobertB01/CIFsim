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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.helper.RelationsKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.RepresentationKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;

/** Variable ordering algorithm that returns the reverse order of another algorithm. */
public class ReverseVarOrderer implements VarOrderer {
    /** The algorithm to apply. */
    private final VarOrderer algorithm;

    /** The kind of relations to use to compute metric values. */
    private final RelationsKind relationsKind;

    /**
     * Constructor for the {@link ReverseVarOrderer} class.
     *
     * @param algorithm The algorithm to apply.
     * @param relationsKind The kind of relations to use to compute metric values.
     */
    public ReverseVarOrderer(VarOrderer algorithm, RelationsKind relationsKind) {
        this.algorithm = algorithm;
        this.relationsKind = relationsKind;
    }

    @Override
    public List<SynthesisVariable> order(VarOrderHelper helper, List<SynthesisVariable> inputOrder, boolean dbgEnabled,
            int dbgLevel)
    {
        // Debug output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying algorithm, and reversing its result:");
            helper.dbg(dbgLevel + 1, "Relations: %s", VarOrderer.enumValueToParserArg(relationsKind));
            helper.dbgRepresentation(dbgLevel + 1, RepresentationKind.HYPER_EDGES, relationsKind);
            helper.dbg();
        }

        // Skip algorithm if no hyper-edges.
        List<BitSet> hyperEdges = helper.getHyperEdges(relationsKind);
        if (hyperEdges.isEmpty()) {
            if (dbgEnabled) {
                helper.dbg(dbgLevel + 1, "Skipping algorithm: no hyper-edges.");
            }
            return inputOrder;
        }

        // More output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbgMetricsForVarOrder(dbgLevel + 1, inputOrder, "before", relationsKind);
            helper.dbg();
        }

        // Apply the algorithm.
        List<SynthesisVariable> order = algorithm.order(helper, inputOrder, dbgEnabled, dbgLevel + 1);

        // Debug output after applying the algorithm.
        if (dbgEnabled) {
            helper.dbg();
            helper.dbgMetricsForVarOrder(dbgLevel + 1, order, "after", relationsKind);
        }

        // Reverse the order.
        Collections.reverse(order);

        // Debug output after reversing the variable order.
        if (dbgEnabled) {
            helper.dbgMetricsForVarOrder(dbgLevel + 1, order, "reversed", relationsKind);
        }

        // Return the resulting variable order.
        return order;
    }

    @Override
    public String toString() {
        return fmt("reverse(relations=%s, orderer=%s)", VarOrderer.enumValueToParserArg(relationsKind),
                algorithm.toString());
    }
}
