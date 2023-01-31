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

import java.util.List;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.graph.Graph;
import org.eclipse.escet.cif.datasynth.varorder.graph.Node;
import org.eclipse.escet.cif.datasynth.varorder.graph.algos.PseudoPeripheralNodeFinder;
import org.eclipse.escet.cif.datasynth.varorder.graph.algos.PseudoPeripheralNodeFinderKind;
import org.eclipse.escet.cif.datasynth.varorder.graph.algos.WeightedCuthillMcKeeNodeOrderer;
import org.eclipse.escet.cif.datasynth.varorder.helper.RelationsKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;

/**
 * Weighted Cuthill-McKee bandwidth-reducing variable ordering heuristic.
 *
 * @see WeightedCuthillMcKeeNodeOrderer
 */
public class WeightedCuthillMcKeeVarOrderer implements VarOrderer {
    /** The kind of pseudo-peripheral node finder to use. */
    private final PseudoPeripheralNodeFinderKind nodeFinderKind;

    /** The kind of relations to use to obtain the graph and to compute metric values. */
    private final RelationsKind relationsKind;

    /**
     * Constructor for the {@link WeightedCuthillMcKeeVarOrderer} class.
     *
     * @param nodeFinderKind The kind of pseudo-peripheral node finder to use.
     * @param relationsKind The kind of relations to use to obtain the graph and to compute metric values.
     */
    public WeightedCuthillMcKeeVarOrderer(PseudoPeripheralNodeFinderKind nodeFinderKind, RelationsKind relationsKind) {
        this.nodeFinderKind = nodeFinderKind;
        this.relationsKind = relationsKind;
    }

    @Override
    public List<SynthesisVariable> order(VarOrderHelper helper, List<SynthesisVariable> inputOrder, boolean dbgEnabled,
            int dbgLevel)
    {
        // Get graph.
        Graph graph = helper.getGraph(relationsKind);

        // Debug output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbg(dbgLevel, "Applying Weighted Cuthill-McKee algorithm:");
            helper.dbg(dbgLevel + 1, "Node finder: %s", VarOrderer.enumValueToParserArg(nodeFinderKind));
            helper.dbg(dbgLevel + 1, "Relations: %s", VarOrderer.enumValueToParserArg(relationsKind));
            helper.dbg();
            helper.dbgMetricsForVarOrder(dbgLevel + 1, inputOrder, "before", relationsKind);
        }

        // Apply algorithm.
        PseudoPeripheralNodeFinder nodeFinder = nodeFinderKind.create();
        List<Node> order = new WeightedCuthillMcKeeNodeOrderer(nodeFinder).orderNodes(graph);

        // Debug output after applying the algorithm.
        if (dbgEnabled) {
            helper.dbgMetricsForNodeOrder(dbgLevel + 1, order, "after", relationsKind);
        }

        // Return the resulting order.
        return helper.reorderForNodeOrder(order);
    }

    @Override
    public String toString() {
        return fmt("weighted-cm(node-finder=%s, relations=%s)", VarOrderer.enumValueToParserArg(nodeFinderKind),
                VarOrderer.enumValueToParserArg(relationsKind));
    }
}
