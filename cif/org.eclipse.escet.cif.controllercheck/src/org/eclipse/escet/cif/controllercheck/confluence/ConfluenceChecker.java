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

package org.eclipse.escet.cif.controllercheck.confluence;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.iout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.controllercheck.CheckConclusion;
import org.eclipse.escet.cif.controllercheck.ComputeGlobalEventData;
import org.eclipse.escet.cif.controllercheck.GlobalEventGuardUpdate;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.MvSpecBuilder;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.AppEnvData;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;
import org.eclipse.escet.common.multivaluetrees.VariableReplacement;

/** Class to check confluence of the specification. */
public class ConfluenceChecker {
    /** The application context to use. */
    private final AppEnvData env = AppEnv.getData();

    /** Mapping between events and their global guard as a MDD node. Is {@code null} until computed. */
    Map<Event, GlobalEventGuardUpdate> globalEventsGuardUpdate;

    /** Variable replacements to perform after an update. */
    private VariableReplacement[] varReplacements;

    /** Builder for the MDD tree. */
    private MvSpecBuilder builder;

    /**
     * Performs the finite response check for a CIF specification.
     *
     * @param globalEventData Prepared information of the specification.
     * @return {@code null} if the check was aborted, else the conclusion about the checking process.
     */
    public CheckConclusion checkSystem(ComputeGlobalEventData globalEventData) {
        List<Automaton> automata = globalEventData.getReadOnlyAutomata();
        Set<Event> controllableEvents = globalEventData.getReadOnlyControllableEvents();

        if (automata.isEmpty() || controllableEvents.isEmpty()) {
            return new ConfluenceCheckConclusion(List.of());
        }

        // At least one automaton and one controllable event exists, other data is valid too.
        globalEventsGuardUpdate = globalEventData.getReadOnlyGlobalEventsGuardUpdate();
        varReplacements = globalEventData.createVarUpdateReplacements();
        builder = globalEventData.getBuilder();
        Tree tree = builder.tree;

        // Storage of test results.
        List<Pair<String, String>> mutualExclusives = list(); // List with pairs that are mutual exclusive.
        List<Pair<String, String>> updateEquivalents = list(); // List with pairs that are update equivalent.
        List<Pair<String, String>> failedChecks = list(); // List with pairs that failed all checks.

        // Events that should be skipped by the inner loop to avoid performing both (A, B) and (B, A) tests.
        Set<Event> skipInner = setc(globalEventsGuardUpdate.size());
        for (Entry<Event, GlobalEventGuardUpdate> entry1: globalEventsGuardUpdate.entrySet()) {
            Event event1 = entry1.getKey();
            String evt1Name = CifTextUtils.getAbsName(event1);

            skipInner.add(event1);
            for (Entry<Event, GlobalEventGuardUpdate> entry2: globalEventsGuardUpdate.entrySet()) {
                Event event2 = entry2.getKey();

                if (env.isTerminationRequested()) {
                    return null;
                }

                // Skip events already being done with the outer loop.
                if (skipInner.contains(event2)) {
                    continue;
                }

                String evt2Name = CifTextUtils.getAbsName(event2);

                GlobalEventGuardUpdate evtData1 = entry1.getValue();
                Node globalGuard1 = evtData1.getGuard();
                Node globalUpdate1 = evtData1.getUpdate();

                GlobalEventGuardUpdate evtData2 = entry2.getValue();
                Node globalGuard2 = evtData2.getGuard();
                Node globalUpdate2 = evtData2.getUpdate();

                // Check for mutual exclusiveness (never both guards are enabled at the same time).
                Node commonEnabledGuards = tree.conjunct(globalGuard1, globalGuard2);
                if (commonEnabledGuards == Tree.ZERO) {
                    mutualExclusives.add(makeSortedPair(evt1Name, evt2Name));
                    continue;
                }
                if (env.isTerminationRequested()) {
                    return null;
                }

                // Check for update equivalence (both events make the same changes).
                Node event1Done = performEdge(commonEnabledGuards, globalUpdate1);
                Node event2Done = performEdge(commonEnabledGuards, globalUpdate2);
                if (event1Done == event2Done) {
                    updateEquivalents.add(makeSortedPair(evt1Name, evt2Name));
                    continue;
                }
                if (env.isTerminationRequested()) {
                    return null;
                }

                // None of the checks holds, failed to prove confluence.
                failedChecks.add(makeSortedPair(evt1Name, evt2Name));
            }
        }
        if (env.isTerminationRequested()) {
            return null;
        }

        // Dump results.
        dumpMatches(mutualExclusives, "Mutual exclusive event pairs");
        dumpMatches(updateEquivalents, "Update equivalent event pairs");

        return new ConfluenceCheckConclusion(failedChecks);
    }

    /**
     * Perform an edge update from the {@code state}.
     *
     * @param state State to start from.
     * @param update Update to perform from the taken edge.
     * @return The state after performing the update.
     */
    private Node performEdge(Node state, Node update) {
        return builder.tree.adjacentReplacements(builder.tree.conjunct(state, update), varReplacements);
    }

    /**
     * Construct an event pair where its members are alphabetically sorted.
     *
     * @param evt1Name First name to use.
     * @param evt2Name Second name to use.
     * @return Pair with not names in alphabetical order.
     */
    private Pair<String, String> makeSortedPair(String evt1Name, String evt2Name) {
        if (evt1Name.compareTo(evt2Name) < 0) {
            return new Pair<>(evt1Name, evt2Name);
        } else {
            return new Pair<>(evt2Name, evt1Name);
        }
    }

    /**
     * Output the collection of event pairs with the stated reason.
     *
     * @param pairs Event pairs to output.
     * @param reasonText Description of the reason what the collection means.
     */
    private void dumpMatches(List<Pair<String, String>> pairs, String reasonText) {
        if (pairs.isEmpty()) {
            return;
        }

        // Sort the pairs for easier reading.
        pairs.sort(new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> p1, Pair<String, String> p2) {
                int order = p1.left.compareTo(p2.left);
                return (order != 0) ? order : p1.right.compareTo(p2.right);
            }
        });

        out();
        out(reasonText + ":");
        iout();
        out(pairs.stream().map(p -> "(" + p.left + ", " + p.right + ")").collect(Collectors.joining(", ")));
        dout();
    }
}
