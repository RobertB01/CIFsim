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

package org.eclipse.escet.cif.datasynth.varorder.orderers;

import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.graph.Graph;
import org.eclipse.escet.cif.datasynth.varorder.graph.Node;
import org.eclipse.escet.cif.datasynth.varorder.graph.algos.PseudoPeripheralNodeFinder;
import org.eclipse.escet.cif.datasynth.varorder.graph.algos.WeightedCuthillMcKeeNodeOrderer;
import org.eclipse.escet.cif.datasynth.varorder.helper.RelationsKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrdererHelper;

/**
 * Weighted Cuthill-McKee bandwidth-reducing variable ordering heuristic.
 *
 * @see WeightedCuthillMcKeeNodeOrderer
 */
public class WeightedCuthillMcKeeVarOrderer implements VarOrderer {
    /** The pseudo-peripheral node finder to use. */
    private final PseudoPeripheralNodeFinder nodeFinder;

    /** The relations to use to obtain the graph and to compute metric values. */
    private final RelationsKind relationsKind;

    /**
     * Constructor for the {@link WeightedCuthillMcKeeVarOrderer} class.
     *
     * @param nodeFinder The pseudo-peripheral node finder to use.
     * @param relationsKind The relations to use to obtain the graph and to compute metric values.
     */
    public WeightedCuthillMcKeeVarOrderer(PseudoPeripheralNodeFinder nodeFinder, RelationsKind relationsKind) {
        this.nodeFinder = nodeFinder;
        this.relationsKind = relationsKind;
    }

    @Override
    public List<SynthesisVariable> order(VarOrdererHelper helper, List<SynthesisVariable> inputOrder,
            boolean dbgEnabled, int dbgLevel)
    {
        // Get graph.
        Graph graph = helper.getGraph(relationsKind);

        // Debug output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying Weighted Cuthill-McKee algorithm.");
            helper.dbgMetricsForVarOrder(dbgLevel, inputOrder, "before", relationsKind);
        }

        // Apply algorithm.
        List<Node> order = new WeightedCuthillMcKeeNodeOrderer(nodeFinder).orderNodes(graph);

        // Debug output after applying the algorithm.
        if (dbgEnabled) {
            helper.dbgMetricsForNodeOrder(dbgLevel, order, "after", relationsKind);
        }

        // Return the resulting order.
        return helper.reorderForNodeOrder(order);
    }
}
