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
import org.eclipse.escet.cif.datasynth.varorder.graph.Graph;
import org.eclipse.escet.cif.datasynth.varorder.graph.Node;
import org.eclipse.escet.cif.datasynth.varorder.graph.algos.WeightedCuthillMcKeeNodeOrderer;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrdererHelper;

/**
 * Weighted Cuthill-McKee bandwidth-reducing variable ordering heuristic.
 *
 * @see WeightedCuthillMcKeeNodeOrderer
 */
public class WeightedCuthillMcKeeVarOrderer implements VarOrderer {
    @Override
    public SynthesisVariable[] order(VarOrdererHelper helper, SynthesisVariable[] inputOrder, boolean dbgEnabled,
            int dbgLevel)
    {
        // Get graph.
        Graph graph = helper.getGraph();

        // Debug output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying Weighted Cuthill-McKee algorithm.");
            helper.dbgTotalSpan(dbgLevel, inputOrder, "before");
        }

        // Apply algorithm.
        List<Node> order = new WeightedCuthillMcKeeNodeOrderer().orderNodes(graph);

        // Debug output after applying the algorithm.
        if (dbgEnabled) {
            helper.dbgTotalSpan(dbgLevel, order, "after");
        }

        // Return the resulting order.
        return helper.reorder(order);
    }
}
