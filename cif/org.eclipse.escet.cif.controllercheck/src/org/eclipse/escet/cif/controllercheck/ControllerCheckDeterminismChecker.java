//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck;

import static org.eclipse.escet.cif.common.CifCollectUtils.collectDiscAndInputVariables;
import static org.eclipse.escet.cif.common.CifTextUtils.getAbsName;
import static org.eclipse.escet.cif.common.CifTextUtils.getLocationText1;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.controllercheck.multivaluetrees.CifVarInfoBuilder;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.MvSpecBuilder;
import org.eclipse.escet.cif.metamodel.cif.Component;
import org.eclipse.escet.cif.metamodel.cif.Group;
import org.eclipse.escet.cif.metamodel.cif.Specification;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.cif.metamodel.cif.expressions.EventExpression;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.AppEnvData;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;

/** Controller checker determinism checker. */
public class ControllerCheckDeterminismChecker {
    /** Found problems in the specification. */
    public List<String> problems = list();

    /** The application context to use. */
    private final AppEnvData env = AppEnv.getData();

    /**
     * Check that a specification is supported.
     *
     * <p>
     * As only deterministic specifications are supported, check that the specification is deterministic.
     * </p>
     *
     * @param spec The specification to check.
     * @throws UnsupportedException If the specification is not deterministic.
     */
    public void check(Specification spec) {
        List<Declaration> variables = list();
        collectDiscAndInputVariables(spec, variables);

        // Construct a MDD tree builder.
        final int READINDEX = 0;
        final int WRITEINDEX = 1;
        CifVarInfoBuilder cifVarInfoBuilder = new CifVarInfoBuilder(1);
        cifVarInfoBuilder.addVariablesGroupOnVariable(variables);
        MvSpecBuilder builder = new MvSpecBuilder(cifVarInfoBuilder, READINDEX, WRITEINDEX);

        // Verify determinism for each location.
        verifyDeterminism(spec, builder);
        if (env.isTerminationRequested()) {
            return;
        }

        // If we have any problems, the specification is unsupported.
        Collections.sort(problems, Strings.SORTER);
        if (!problems.isEmpty()) {
            String msg = "CIF controller properties check application failed due to unsatisfied preconditions:\n - "
                    + String.join("\n - ", problems);
            throw new UnsupportedException(msg);
        }
    }

    /**
     * Verifies that a group is deterministic.
     *
     * <p>
     * A group is deterministic if all its contained automata are deterministic.
     * </p>
     *
     * @param group The group to check.
     * @param builder The builder for the MDD tree.
     */
    private void verifyDeterminism(Group group, MvSpecBuilder builder) {
        for (Component comp: group.getComponents()) {
            if (env.isTerminationRequested()) {
                return;
            }
            if (comp instanceof Automaton) {
                verifyDeterminism((Automaton)comp, builder);
                continue;
            } else if (comp instanceof Group) {
                verifyDeterminism((Group)comp, builder);
                continue;
            }

            // ComponentInst should not happen, as DefInst has been eliminated.
            throw new RuntimeException("Unexpected type of Component.");
        }
    }

    /**
     * Verifies that an automaton is deterministic.
     *
     * <p>
     * An automaton is deterministic if all its locations are deterministic.
     * </p>
     *
     * @param aut The automaton to check.
     * @param builder The builder for the MDD tree.
     */
    private void verifyDeterminism(Automaton aut, MvSpecBuilder builder) {
        for (Location loc: aut.getLocations()) {
            verifyDeterminism(loc, builder);
            if (env.isTerminationRequested()) {
                return;
            }
        }
    }

    /**
     * Verifies that a location is deterministic by not finding non-determinism for an event.
     *
     * <p>
     * An event in a location is non-deterministic if there is more than one edge for the same event enabled at the same
     * time. This typically happens with overlapping guards (e.g. x &gt; 1 and x &lt; 4).
     * </p>
     *
     * @param loc The location to check.
     * @param builder The builder for the MDD tree.
     */
    private void verifyDeterminism(Location loc, MvSpecBuilder builder) {
        Map<Event, List<List<Expression>>> edgesPredsByEvent = map();
        for (Edge edge: loc.getEdges()) {
            // If there are edges, collect the guards by event to check determinism.
            for (EdgeEvent ee: edge.getEvents()) {
                Expression eventExpr = ee.getEvent();
                Assert.check(eventExpr instanceof EventExpression);
                Event event = ((EventExpression)eventExpr).getEvent();
                if (!event.getControllable()) {
                    continue;
                }

                List<List<Expression>> edgesPreds = edgesPredsByEvent.get(event);
                if (edgesPreds == null) {
                    edgesPreds = list();
                    edgesPredsByEvent.put(event, edgesPreds);
                }
                edgesPreds.add(edge.getGuards());
            }

            if (env.isTerminationRequested()) {
                return;
            }
        }

        // Verify that the collected edges have non-overlapping guards.
        for (Entry<Event, List<List<Expression>>> entry: edgesPredsByEvent.entrySet()) {
            List<List<Expression>> edgeGuards = entry.getValue();
            if (edgeGuards.size() == 1) {
                continue;
            }

            // Convert guards to MDD tree and determine mutually exclusiveness.
            List<Node> edgeGuardNodes = listc(edgeGuards.size());
            for (List<Expression> edgeGuard: edgeGuards) {
                Node edgeGuardNode = builder.getExpressionConvertor().convert(edgeGuard).get(1);
                edgeGuardNodes.add(edgeGuardNode);
                if (env.isTerminationRequested()) {
                    return;
                }
            }

            // Check mutually exclusiveness for every pair.
            for (int i = 0; i < edgeGuardNodes.size() - 1; i++) {
                for (int j = i + 1; j < edgeGuardNodes.size(); j++) {
                    Node n = builder.tree.conjunct(edgeGuardNodes.get(i), edgeGuardNodes.get(j));
                    if (env.isTerminationRequested()) {
                        return;
                    }

                    if (n != Tree.ZERO) {
                        // Not mutually exclusive, which implies overlapping guards.
                        String msg = fmt(
                                "Unsupported %s: non-determinism detected for edge of controllable event "
                                        + "\"%s\" with overlapping guards.",
                                getLocationText1(loc), getAbsName(entry.getKey()));
                        problems.add(msg);
                    }
                }
            }
        }
    }
}
