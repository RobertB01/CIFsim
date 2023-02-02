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
import org.eclipse.escet.cif.datasynth.varorder.graph.algos.SloanNodeOrderer;
import org.eclipse.escet.cif.datasynth.varorder.helper.RelationsKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.RepresentationKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;

/**
 * Sloan profile/wavefront-reducing variable ordering heuristic.
 *
 * @see SloanNodeOrderer
 */
public class SloanVarOrderer implements VarOrderer {
    /** The relations to use to obtain the graph and to compute metric values. */
    private final RelationsKind relationsKind;

    /**
     * Constructor for the {@link SloanVarOrderer} class.
     *
     * @param relationsKind The kind of relations to use to obtain the graph and to compute metric values.
     */
    public SloanVarOrderer(RelationsKind relationsKind) {
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
            helper.dbg(dbgLevel, "Applying Sloan algorithm:");
            helper.dbg(dbgLevel + 1, "Relations: %s", VarOrderer.enumValueToParserArg(relationsKind));
            helper.dbgRepresentation(dbgLevel + 1, RepresentationKind.GRAPH, relationsKind);
            helper.dbg();
        }

        // Skip algorithm if no graph edges.
        if (graph.edgeCount() == 0) {
            if (dbgEnabled) {
                helper.dbg(dbgLevel + 1, "Skipping algorithm: no graph edges.");
            }
            return inputOrder;
        }

        // More debug output before applying the algorithm.
        if (dbgEnabled) {
            helper.dbgMetricsForVarOrder(dbgLevel + 1, inputOrder, "before", relationsKind);
        }

        // Apply algorithm.
        List<Node> order = new SloanNodeOrderer().orderNodes(graph);

        // Debug output after applying the algorithm.
        if (dbgEnabled) {
            helper.dbgMetricsForNodeOrder(dbgLevel + 1, order, "after", relationsKind);
        }

        // Return the resulting order.
        return helper.reorderForNodeOrder(order);
    }

    @Override
    public String toString() {
        return fmt("sloan(relations=%s)", VarOrderer.enumValueToParserArg(relationsKind));
    }
}
