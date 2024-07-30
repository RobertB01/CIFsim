//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.mdd;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.checkers.CifCheckNoCompDefInst;
import org.eclipse.escet.cif.checkers.CifCheckViolations;
import org.eclipse.escet.cif.common.CifCollectUtils;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Termination;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;

/**
 * CIF check that disallows non-determinism for controllable events, for MDD-based checks of the controller properties
 * checker.
 *
 * <p>
 * Automata that have locations with multiple outgoing edges for the same controllable event, with overlapping guards
 * (e.g. {@code x > 1} and {@code x < 4}), are considered violations of this check.
 * </p>
 *
 * <p>
 * This check may lead to false positives, as checks are an over-approximation and guard overlap may be detected for
 * unreachable states.
 * </p>
 */
public class MddDeterminismCheck extends CifCheckNoCompDefInst {
    /** Cooperative termination query function. */
    private final Termination termination;

    /** The MDD tree builder to use, or {@code null} if not yet available. */
    private MddSpecBuilder builder;

    /**
     * Constructor for the {@link MddDeterminismCheck} class.
     *
     * @param termination Cooperative termination query function.
     */
    MddDeterminismCheck(Termination termination) {
        this.termination = termination;
    }

    @Override
    protected void preprocessSpecification(Specification spec, CifCheckViolations violations) {
        // Get discrete and input variables.
        List<Declaration> variables = list();
        CifCollectUtils.collectDiscAndInputVariables(spec, variables);

        // Construct an MDD tree builder.
        final int READINDEX = 0;
        final int WRITEINDEX = 1;
        MddCifVarInfoBuilder cifVarInfoBuilder = new MddCifVarInfoBuilder(1);
        cifVarInfoBuilder.addVariablesGroupOnVariable(variables);
        builder = new MddSpecBuilder(cifVarInfoBuilder, READINDEX, WRITEINDEX);
    }

    @Override
    protected void preprocessLocation(Location loc, CifCheckViolations violations) {
        // Per controllable event, collect guards of the edges.
        Map<Event, List<List<Expression>>> edgesPredsByEvent = map();
        for (Edge edge: loc.getEdges()) {
            for (EdgeEvent ee: edge.getEvents()) {
                if (termination.isRequested()) {
                    return;
                }

                // Skip if not a controllable event.
                Expression eventExpr = ee.getEvent();
                Assert.check(eventExpr instanceof EventExpression);
                Event event = ((EventExpression)eventExpr).getEvent();
                if (!event.getControllable()) {
                    continue;
                }

                // Collect guards.
                List<List<Expression>> edgesPreds = edgesPredsByEvent.computeIfAbsent(event, e -> list());
                edgesPreds.add(edge.getGuards());
            }
        }

        // Verify that the guards of the different edges do not overlap.
        EVENT:
        for (Entry<Event, List<List<Expression>>> entry: edgesPredsByEvent.entrySet()) {
            if (termination.isRequested()) {
                return;
            }

            // Trivially OK if only (guards of) one edge.
            List<List<Expression>> edgesGuards = entry.getValue();
            if (edgesGuards.size() == 1) {
                continue;
            }

            // Convert guards to MDD tree and determine mutually exclusiveness.
            List<Node> edgesGuardsNodes = listc(edgesGuards.size());
            for (List<Expression> edgeGuards: edgesGuards) {
                Node edgeGuardsNode = builder.getExpressionConvertor().convert(edgeGuards).get(1);
                edgesGuardsNodes.add(edgeGuardsNode);
                if (termination.isRequested()) {
                    return;
                }
            }

            // Check mutually exclusiveness for every pair.
            Event event = entry.getKey();
            for (int i = 0; i < edgesGuardsNodes.size() - 1; i++) {
                for (int j = i + 1; j < edgesGuardsNodes.size(); j++) {
                    Node overlap = builder.tree.conjunct(edgesGuardsNodes.get(i), edgesGuardsNodes.get(j));
                    if (termination.isRequested()) {
                        return;
                    }

                    if (overlap != Tree.ZERO) {
                        // Not mutually exclusive, which implies overlapping guards.
                        violations.add(loc, "Non-deterministic edges with overlapping guards within a location, "
                                + "for controllable event \"%s\"", CifTextUtils.getAbsName(event));

                        // Once we have a violation for the event, we can continue to the next one.
                        continue EVENT;
                    }
                }
            }
        }
    }
}
