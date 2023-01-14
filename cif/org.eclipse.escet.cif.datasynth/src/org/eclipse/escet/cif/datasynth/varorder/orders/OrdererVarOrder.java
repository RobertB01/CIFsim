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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;

import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.datasynth.varorder.graph.Graph;
import org.eclipse.escet.cif.datasynth.varorder.helper.RelationsKind;
import org.eclipse.escet.cif.datasynth.varorder.helper.VarOrderHelper;
import org.eclipse.escet.cif.datasynth.varorder.orderers.VarOrderer;
import org.eclipse.escet.common.java.Pair;

/**
 * Variable order produced by applying a ordering algorithm to a given initial variable order. No interleaving is used.
 */
public class OrdererVarOrder extends NonInterleavedVarOrder {
    /** The initial variable order. */
    private final VarOrder initialOrder;

    /** The variable ordering algorithm. */
    private final VarOrderer orderer;

    /**
     * Constructor for the {@link OrdererVarOrder} class.
     *
     * @param initialOrder The initial variable order.
     * @param orderer The variable ordering algorithm.
     */
    public OrdererVarOrder(VarOrder initialOrder, VarOrderer orderer) {
        this.initialOrder = initialOrder;
        this.orderer = orderer;
    }

    @Override
    public List<Pair<SynthesisVariable, Integer>> order(VarOrderHelper helper, boolean dbgEnabled, int dbgLevel) {
        // Get variables of the initial variable order, without interleaving.
        List<SynthesisVariable> initialVariables = initialOrder.order(helper, dbgEnabled, dbgLevel).stream()
                .map(p -> p.left).collect(Collectors.toList());

        // Apply algorithm.
        dbg("  Number of hyper-edges: %,d", hyperEdgeCount);
        dbg("  Number of graph edges: %,d", graphEdgeCount);

        // Only apply a variable ordering algorithm if there are hyper-edges and graph edges, to ensures that variable
        // relations exist for improving the variable order. It also avoids division by zero issues.
        List<SynthesisVariable> curOrder = Arrays.asList(synthAut.variables);
        VarOrderHelper helper = new VarOrderHelper(spec, curOrder);
        List<BitSet> hyperEdges = helper.getHyperEdges(RelationsKind.CONFIGURED);
        Graph graph = helper.getGraph(RelationsKind.CONFIGURED);
        long hyperEdgeCount = hyperEdges.size();
        long graphEdgeCount = graph.edgeCount();
        if (hyperEdgeCount == 0) {
            if (dbgEnabled) {
                dbg();
                dbg("Skipping automatic variable ordering: no hyper-edges.");
                dbg();
            }
            return;
        }
        if (graphEdgeCount == 0) {
            if (dbgEnabled) {
                dbg();
                dbg("Skipping automatic variable ordering: no graph edges.");
                dbg();
            }
            return;
        }

        // Apply variable ordering algorithm.
        List<SynthesisVariable> orderedVariables = orderer.order(helper, initialVariables, dbgEnabled, dbgLevel);

        // Return non-interleaved variable order.
        return getNonInterleavedOrder(orderedVariables);
    }
}
