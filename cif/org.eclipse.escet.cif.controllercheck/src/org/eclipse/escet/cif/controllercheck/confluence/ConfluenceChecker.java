//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.iout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.setc;
import static org.eclipse.escet.common.java.Strings.SORTER;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.controllercheck.CheckConclusion;
import org.eclipse.escet.cif.controllercheck.PrepareChecks;
import org.eclipse.escet.cif.controllercheck.multivaluetrees.MvSpecBuilder;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.AppEnvData;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;
import org.eclipse.escet.common.multivaluetrees.VarInfo;
import org.eclipse.escet.common.multivaluetrees.VariableReplacement;

/** Class to check confluence of the specification. */
public class ConfluenceChecker {
    /** Debug global flow of the checks, which pairs are tested, where are they matched. */
    private static final boolean DEBUG_GLOBAL = false;

    /** Whether to enable debugging output for independence checking. */
    private static final boolean DEBUG_INDENPENCE = false;

    /** Whether to enable debugging output for reversible checking. */
    private static final boolean DEBUG_REVERSIBLE = false;

    /** Whether to enable debugging output for update equivalence. */
    private static final boolean DEBUG_UPDATE_EQUIVALENCE = false;

    /** The application context to use. */
    private final AppEnvData env = AppEnv.getData();

    /** Global guard for each event. */
    private Map<Event, Node> globalGuardsByEvent;

    /** Global guarded update for each event. */
    private Map<Event, Node> globalGuardedUpdatesByEvent;

    /** Variable replacements to perform after an update. */
    private VariableReplacement[] varReplacements;

    /** Tree with original to read identity equations for all variables. */
    private Node origToReadVariablesRelations;

    /** Variables in the MDD tree that are not the original values. */
    private VarInfo[] nonOriginalVarInfos;

    /** Builder for the MDD tree. */
    private MvSpecBuilder builder;

    /**
     * Performs the confluence check for a CIF specification.
     *
     * @param prepareChecks Collected CIF information to perform the confluence check.
     * @return {@code null} if the check was aborted, else the conclusion about the checking process.
     */
    public CheckConclusion checkSystem(PrepareChecks prepareChecks) {
        List<Automaton> automata = prepareChecks.getAutomata();
        Set<Event> controllableEvents = prepareChecks.getControllableEvents();

        if (automata.isEmpty() || controllableEvents.isEmpty()) {
            return new ConfluenceCheckConclusion(List.of());
        }

        // At least one automaton and one controllable event exists, other data is valid too.
        globalGuardsByEvent = prepareChecks.getGlobalGuardsByEvent();
        globalGuardedUpdatesByEvent = prepareChecks.getGlobalGuardedUpdatesByEvent();

        // MDD data.
        varReplacements = prepareChecks.createVarUpdateReplacements();
        origToReadVariablesRelations = prepareChecks.computeOriginalToReadIdentity();
        nonOriginalVarInfos = prepareChecks.getNonOriginalVariables();
        builder = prepareChecks.getBuilder();
        Tree tree = builder.tree;

        // Storage of test results.
        List<Pair<String, String>> mutualExclusives = list(); // List with pairs that are mutual exclusive.
        List<Pair<String, String>> updateEquivalents = list(); // List with pairs that are update equivalent.
        List<Pair<String, String>> independents = list(); // List with pairs that are independent.
        List<Pair<String, String>> skippables = list(); // List with pairs that are skippable.
        List<Pair<String, String>> reversibles = list(); // List with pairs that are reversible.
        List<Pair<String, String>> cannotProves = list(); // List with pairs where confluence could not be proven.

        // Events that should be skipped by the inner loop to avoid performing both (A, B) and (B, A) tests.
        Set<Event> skipInner = setc(globalGuardsByEvent.size());

        // For all pairs of events, perform the checks.
        for (Entry<Event, Node> entry1: globalGuardsByEvent.entrySet()) {
            Event event1 = entry1.getKey();
            String evt1Name = CifTextUtils.getAbsName(event1);

            Node globalGuard1 = entry1.getValue();
            Node globalUpdate1 = globalGuardedUpdatesByEvent.get(event1);

            skipInner.add(event1);
            for (Entry<Event, Node> entry2: globalGuardsByEvent.entrySet()) {
                Event event2 = entry2.getKey();

                if (env.isTerminationRequested()) {
                    return null;
                }

                // Skip events already being done with the outer loop.
                if (skipInner.contains(event2)) {
                    continue;
                }

                String evt2Name = CifTextUtils.getAbsName(event2);
                if (DEBUG_GLOBAL) {
                    dbg("Trying event pair (" + evt1Name + ", " + evt2Name + ")...");
                }

                Node globalGuard2 = entry2.getValue();
                Node globalUpdate2 = globalGuardedUpdatesByEvent.get(event2);

                // Check for mutual exclusiveness (never both guards are enabled at the same time).
                Node commonEnabledGuards = tree.conjunct(globalGuard1, globalGuard2);
                if (commonEnabledGuards == Tree.ZERO) {
                    mutualExclusives.add(makeSortedPair(evt1Name, evt2Name));
                    if (DEBUG_GLOBAL) {
                        dbg("  -> event pair (" + evt1Name + ", " + evt2Name + ") is mutual exclusive.");
                    }
                    continue;
                }
                if (env.isTerminationRequested()) {
                    return null;
                }

                // Glue original nodes to the variables. This allows detection that different paths through the state
                // space treat variable values differently.
                commonEnabledGuards = tree.conjunct(origToReadVariablesRelations, commonEnabledGuards);

                if (env.isTerminationRequested()) {
                    return null;
                }

                // Grab the set of states where both events are enabled under the optimistic assumption that the event
                // pair matches one of the checks below.
                Node originalStates = tree.variableAbstractions(commonEnabledGuards, nonOriginalVarInfos);

                if (env.isTerminationRequested()) {
                    return null;
                }

                // Check for update equivalence (both events make the same changes).
                if (DEBUG_UPDATE_EQUIVALENCE) {
                    globalGuard1.dumpGraphLines("updateEquivalent-globalGuard1");
                    dbg();
                    globalGuard2.dumpGraphLines("updateEquivalent-globalGuard2");
                    dbg();
                    globalUpdate1.dumpGraphLines("updateEquivalent-globalUpdate1");
                    dbg();
                    globalUpdate2.dumpGraphLines("updateEquivalent-globalUupdate2");
                    dbg();
                    commonEnabledGuards.dumpGraphLines("updateEquivalence-commonEnabledGuards");
                    dbg();
                }
                Node event1Done = performEdge(commonEnabledGuards, globalUpdate1);
                Node event2Done = performEdge(commonEnabledGuards, globalUpdate2);
                if (DEBUG_UPDATE_EQUIVALENCE) {
                    event1Done.dumpGraphLines("updateEquivalent-event1Done");
                    event2Done.dumpGraphLines("updateEquivalent-event2Done");
                }
                if (event1Done != Tree.ZERO && event1Done == event2Done
                        && allStateCovered(originalStates, event1Done))
                {
                    if (DEBUG_GLOBAL) {
                        dbg("  -> event pair (" + evt1Name + ", " + evt2Name + ") is update equivalent.");
                    }
                    updateEquivalents.add(makeSortedPair(evt1Name, evt2Name));
                    continue;
                }
                if (env.isTerminationRequested()) {
                    return null;
                }

                // Check for independence (diamond shape edges leading to the same changes).
                if (DEBUG_INDENPENCE) {
                    globalGuard1.dumpGraphLines("independence-globalGuard1");
                    dbg();
                    globalGuard2.dumpGraphLines("independence-globalGuard2");
                    dbg();
                    globalUpdate1.dumpGraphLines("independence-globalUpdate1");
                    dbg();
                    globalUpdate2.dumpGraphLines("independence-globalUupdate2");
                    dbg();
                }
                //
                // First event1 then event2.
                Node event1Enabled2 = tree.conjunct(event1Done, globalGuard2);
                Node event12Done = (event1Enabled2 == Tree.ZERO) ? Tree.ZERO
                        : performEdge(event1Enabled2, globalUpdate2);
                if (DEBUG_INDENPENCE) {
                    event1Enabled2.dumpGraphLines("independence-event1enabled2");
                    event12Done.dumpGraphLines("independence-event12done");
                }
                if (env.isTerminationRequested()) {
                    return null;
                }

                // First event2 then event1.
                Node event2Enabled1 = tree.conjunct(event2Done, globalGuard1);
                Node event21Done = (event2Enabled1 == Tree.ZERO) ? Tree.ZERO
                        : performEdge(event2Enabled1, globalUpdate1);
                if (DEBUG_INDENPENCE) {
                    event2Enabled1.dumpGraphLines("independence-event2enabled1");
                    event21Done.dumpGraphLines("independence-event212done");
                }

                if (event12Done != Tree.ZERO && event12Done == event21Done
                        && allStateCovered(originalStates, event12Done))
                {
                    independents.add(makeSortedPair(evt1Name, evt2Name));
                    if (DEBUG_GLOBAL) {
                        dbg("  -> event pair (" + evt1Name + ", " + evt2Name + ") is indepenent.");
                    }
                    continue;
                }
                if (env.isTerminationRequested()) {
                    return null;
                }

                // Check for skippable (may perform a second event, but its changes are undone).
                //
                // Check skippable event2 (events 2, 1 versus event 1).
                if (event21Done != Tree.ZERO && event1Done == event21Done
                        && allStateCovered(originalStates, event1Done))
                {
                    skippables.add(makeSortedPair(evt1Name, evt2Name));
                    if (DEBUG_GLOBAL) {
                        dbg("  -> event pair (" + evt1Name + ", " + evt2Name + ") is skippable.");
                    }
                    continue;
                }
                if (env.isTerminationRequested()) {
                    return null;
                }

                // Check skippable event1 (events 1, 2 versus event 2).
                if (event12Done != Tree.ZERO && event2Done == event12Done
                        && allStateCovered(originalStates, event2Done))
                {
                    skippables.add(makeSortedPair(evt1Name, evt2Name));
                    if (DEBUG_GLOBAL) {
                        dbg("  -> event pair (" + evt1Name + ", " + evt2Name + ") is skippable.");
                    }
                    continue;
                }
                if (env.isTerminationRequested()) {
                    return null;
                }

                // Check reversible (if event2 is performed before event1 its effect is reverted by event3 after
                // event1).
                if (DEBUG_REVERSIBLE) {
                    globalGuard1.dumpGraphLines("reversible-globalGuard1");
                    dbg();
                    globalGuard2.dumpGraphLines("reversible-globalGuard2");
                    dbg();
                    globalUpdate1.dumpGraphLines("reversible-globalUpdate1");
                    dbg();
                    globalUpdate2.dumpGraphLines("reversible-globalUpdate2");
                    dbg();
                }

                boolean foundReversible = false;
                for (Entry<Event, Node> entry3: globalGuardsByEvent.entrySet()) {
                    Event event3 = entry3.getKey();
                    if (event3 == event1 || event3 == event2) {
                        continue;
                    }
                    if (env.isTerminationRequested()) {
                        return null;
                    }

                    Node globalGuard3 = entry3.getValue();
                    Node globalUpdate3 = globalGuardedUpdatesByEvent.get(event3);
                    if (DEBUG_REVERSIBLE) {
                        dbg("----");
                        dbg("Reversible (" + evt1Name + ", " + evt2Name + "), trying event3 = " + event3.getName());
                        event21Done.dumpGraphLines("reversible-event21Done");
                        event12Done.dumpGraphLines("reversible-event12Done");
                        globalGuard3.dumpGraphLines("reversible-globalGuard3");
                        globalUpdate3.dumpGraphLines("reversible-globalUpdate3");
                    }

                    // Check for reversible (events 2, 1, 3 versus event 1).
                    if (event21Done != Tree.ZERO) {
                        Node event21Enabled3 = tree.conjunct(event21Done, globalGuard3);
                        Node event213Done = (event21Enabled3 == Tree.ZERO) ? Tree.ZERO
                                : performEdge(event21Enabled3, globalUpdate3);
                        if (DEBUG_REVERSIBLE) {
                            event21Enabled3.dumpGraphLines("reversible-event21Enabled3");
                            event213Done.dumpGraphLines("reversible-event213Done");
                            event1Done.dumpGraphLines("reversible-event1Done");
                        }
                        if (event213Done != Tree.ZERO && event213Done == event1Done
                                && allStateCovered(originalStates, event1Done))
                        {
                            if (DEBUG_REVERSIBLE) {
                                dbg("reversible: event213Done != Tree.ZERO && event213Done == event1Done -> Found a match.");
                            }
                            foundReversible = true;
                            break;
                        }
                    }
                    if (env.isTerminationRequested()) {
                        return null;
                    }

                    // Check for reversible (events 1, 2, 3 versus event 2).
                    if (event12Done != Tree.ZERO) {
                        Node event12Enabled3 = tree.conjunct(event12Done, globalGuard3);
                        Node event123Done = (event12Enabled3 == Tree.ZERO) ? Tree.ZERO
                                : performEdge(event12Enabled3, globalUpdate3);
                        if (DEBUG_REVERSIBLE) {
                            event12Enabled3.dumpGraphLines("reversible-event12Enabled3");
                            event123Done.dumpGraphLines("reversible-event123Done");
                            event2Done.dumpGraphLines("reversible-event2Done");
                        }
                        if (event123Done != Tree.ZERO && event123Done == event2Done
                                && allStateCovered(originalStates, event2Done))
                        {
                            if (DEBUG_REVERSIBLE) {
                                dbg("reversible: event123Done != Tree.ZERO && event123Done == event2Done -> Found a match.");
                            }
                            foundReversible = true;
                            break;
                        }
                    }
                    if (env.isTerminationRequested()) {
                        return null;
                    }
                }
                if (foundReversible) {
                    reversibles.add(makeSortedPair(evt1Name, evt2Name));
                    if (DEBUG_GLOBAL) {
                        dbg("  -> event pair (" + evt1Name + ", " + evt2Name + ") is reversible.");
                    }
                    continue;
                }

                // None of the checks holds, failed to prove confluence.
                cannotProves.add(makeSortedPair(evt1Name, evt2Name));
                if (DEBUG_GLOBAL) {
                    dbg("  -> event pair (" + evt1Name + ", " + evt2Name + ") is CANNOT-PROVE.");
                }
            }
        }
        if (env.isTerminationRequested()) {
            return null;
        }

        // Dump results.
        dumpMatches(mutualExclusives, "Mutual exclusive event pairs");
        dumpMatches(updateEquivalents, "Update equivalent event pairs");
        dumpMatches(independents, "Independent event pairs");
        dumpMatches(skippables, "Skippable event pairs");
        dumpMatches(reversibles, "Reversible event pairs");

        return new ConfluenceCheckConclusion(cannotProves);
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
     * Verify that the result states cover all {@code originalStates}.
     *
     * @param originalStates States where the initial combined guards holds, in {@link PrepareChecks#ORIGINAL_INDEX
     *     ORIGINAL_INDEX} variables.
     * @param resultStates Common end states for one of the confluence checks, as a relation between surviving original
     *     states and the end states.
     * @return Whether the {@code resultStates} cover the entire {@code originalStates}.
     */
    private boolean allStateCovered(Node originalStates, Node resultStates) {
        // Compute the set of original states that is associated with the common result state.
        Node orignalResultStates = builder.tree.variableAbstractions(resultStates, nonOriginalVarInfos);
        return originalStates == orignalResultStates;
    }

    /**
     * Construct an event pair where its members are alphabetically sorted.
     *
     * @param evt1Name First name to use.
     * @param evt2Name Second name to use.
     * @return Pair with the names in alphabetical order.
     */
    private Pair<String, String> makeSortedPair(String evt1Name, String evt2Name) {
        if (SORTER.compare(evt1Name, evt2Name) < 0) {
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
        pairs.sort(
                Comparator.comparing((Pair<String, String> p) -> p.left, SORTER).thenComparing(p -> p.right, SORTER));

        out();
        out(reasonText + ":");
        iout();
        out(pairs.stream().map(Pair::toString).collect(Collectors.joining(", ")));
        dout();
    }
}
