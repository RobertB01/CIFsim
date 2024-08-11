//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth;

import static org.eclipse.escet.cif.bdd.utils.BddUtils.bddToStr;
import static org.eclipse.escet.cif.datasynth.settings.FixedPointComputation.CTRL;
import static org.eclipse.escet.cif.datasynth.settings.FixedPointComputation.REACH;
import static org.eclipse.escet.common.java.Lists.concat;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Sets.setc;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.eclipse.escet.cif.bdd.spec.CifBddDiscVariable;
import org.eclipse.escet.cif.bdd.spec.CifBddEdge;
import org.eclipse.escet.cif.bdd.spec.CifBddEdgeApplyDirection;
import org.eclipse.escet.cif.bdd.spec.CifBddEdgeKind;
import org.eclipse.escet.cif.bdd.spec.CifBddSpec;
import org.eclipse.escet.cif.bdd.spec.CifBddVariable;
import org.eclipse.escet.cif.bdd.utils.BddUtils;
import org.eclipse.escet.cif.bdd.utils.CifBddApplyPlantInvariants;
import org.eclipse.escet.cif.bdd.utils.CifBddReachability;
import org.eclipse.escet.cif.bdd.workset.dependencies.BddBasedEdgeDependencySetCreator;
import org.eclipse.escet.cif.bdd.workset.dependencies.EdgeDependencySetCreator;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.datasynth.settings.BddSimplify;
import org.eclipse.escet.cif.datasynth.settings.CifDataSynthesisSettings;
import org.eclipse.escet.cif.datasynth.settings.FixedPointComputation;
import org.eclipse.escet.cif.datasynth.settings.StateReqInvEnforceMode;
import org.eclipse.escet.cif.datasynth.settings.SynthesisStatistics;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.BitSets;
import org.eclipse.escet.common.java.Sets;
import org.eclipse.escet.common.java.Stopwatch;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;

import com.github.javabdd.BDD;

/** CIF data-based supervisory controller synthesis. */
public class CifDataSynthesis {
    /** Constructor for the {@link CifDataSynthesis} class. */
    private CifDataSynthesis() {
        // Static class.
    }

    /**
     * Performs data-based supervisory controller synthesis.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis. Is updated to represent the result of
     *     synthesis.
     * @param settings The settings to use.
     * @param timing The timing statistics data. Is modified in-place.
     * @return The synthesis result, or {@code null} in case termination was requested.
     */
    public static CifDataSynthesisResult synthesize(CifBddSpec cifBddSpec, CifDataSynthesisSettings settings,
            CifDataSynthesisTiming timing)
    {
        // Algorithm is based on the following paper: Lucien Ouedraogo, Ratnesh Kumar, Robi Malik, and Knut Åkesson:
        // Nonblocking and Safe Control of Discrete-Event Systems Modeled as Extended Finite Automata, IEEE Transactions
        // on Automation Science and Engineering, Volume 8, Issue 3, Pages 560-569, July 2011.

        // Initialization.
        CifDataSynthesisResult synthResult = new CifDataSynthesisResult(cifBddSpec, settings);
        boolean doTiming = synthResult.settings.getSynthesisStatistics().contains(SynthesisStatistics.TIMING);
        boolean doForward = synthResult.settings.getDoForwardReach();
        boolean dbgEnabled = cifBddSpec.settings.getDebugOutput().isEnabled();
        Set<Event> disabledEvents = null;

        // Pre synthesis.
        if (doTiming) {
            timing.preSynth.start();
        }
        try {
            // Check system, and print debug information.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Showing input and checking for potential problems:");
                cifBddSpec.settings.getDebugOutput().inc();
            }
            checkSystem(cifBddSpec, synthResult, dbgEnabled);
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }

            // Print debug information on edge guard restrictions for preventing runtime errors.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            if (dbgEnabled) {
                printDebugRuntimeErrorPrevention(cifBddSpec, synthResult);
            }

            // Apply state/event exclusion plant invariants.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            CifBddApplyPlantInvariants.applyStateEvtExclPlantsInvs(cifBddSpec, "uncontrolled system",
                    () -> synthResult.getCtrlBehText(0, cifBddSpec.settings.getIndentAmount()), dbgEnabled);

            // Initialize applying edges.
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Initializing edges for being applied.");
            }
            for (CifBddEdge edge: cifBddSpec.edges) {
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return null;
                }
                edge.initApply();
            }

            // Apply state plant invariants if there are any.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            CifBddApplyPlantInvariants.applyStatePlantInvs(cifBddSpec, "uncontrolled system",
                    () -> synthResult.getCtrlBehText(0, cifBddSpec.settings.getIndentAmount()), dbgEnabled);

            // Initialize controlled behavior.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Initializing controlled behavior:");
                cifBddSpec.settings.getDebugOutput().inc();
            }

            synthResult.ctrlBeh = cifBddSpec.factory.one();
            synthResult.initialCtrl = cifBddSpec.initialPlantInv.id();

            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line("Controlled-behavior predicate: %s.",
                        bddToStr(synthResult.ctrlBeh, cifBddSpec));
                cifBddSpec.settings.getDebugOutput().line("Controlled-initialization predicate: %s.",
                        bddToStr(synthResult.initialCtrl, cifBddSpec));
                cifBddSpec.settings.getDebugOutput().dec();
            }

            // Apply requirements.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            applyStateReqInvs(cifBddSpec, synthResult, dbgEnabled);

            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            applyVarRanges(cifBddSpec, synthResult, dbgEnabled);

            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            applyStateEvtExclReqs(cifBddSpec, synthResult, dbgEnabled);

            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            applyRuntimeErrorReqs(cifBddSpec, synthResult, dbgEnabled);

            // Re-initialize applying edges after applying the state plant invariants, state requirement invariants
            // (depending on settings), and state/event exclusion requirement invariants.
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Re-initializing edges for being applied.");
            }
            for (CifBddEdge edge: cifBddSpec.edges) {
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return null;
                }
                edge.reinitApply();
            }

            // Check edges.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            if (synthResult.settings.getDoNeverEnabledEventsWarn()) {
                disabledEvents = checkInputEdges(cifBddSpec, dbgEnabled);
            }

            // Prepare workset algorithm, if enabled.
            if (cifBddSpec.settings.getDoUseEdgeWorksetAlgo()) {
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return null;
                }
                prepareWorksetAlgorithm(cifBddSpec, synthResult.settings.getDoForwardReach(), dbgEnabled);
            }
        } finally {
            if (doTiming) {
                timing.preSynth.stop();
            }
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            return null;
        }

        // Perform actual synthesis, using fixed point calculations.
        if (doTiming) {
            timing.main.start();
        }
        try {
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            synthesizeFixedPoints(cifBddSpec, synthResult, doForward, dbgEnabled, doTiming, timing);
            cifBddSpec.marked.free();
            cifBddSpec.marked = null;
        } finally {
            if (doTiming) {
                timing.main.stop();
            }
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            return null;
        }

        // Post synthesis.
        if (doTiming) {
            timing.postSynth.start();
        }
        try {
            // Determine controlled system guards.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            determineCtrlSysGuards(cifBddSpec, synthResult, dbgEnabled);

            // Done with actual synthesis. May no longer apply edges from here on.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Cleaning up edges for being applied.");
            }
            for (CifBddEdge edge: cifBddSpec.edges) {
                edge.cleanupApply();
            }

            // At this point, the final controlled system behavior is in the synthesis result.

            // Print the final controlled system behavior as debug output.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Final synthesis result:");
                cifBddSpec.settings.getDebugOutput()
                        .line(synthResult.getCtrlBehText(1, cifBddSpec.settings.getIndentAmount()));
                if (!cifBddSpec.edges.isEmpty()) {
                    for (String line: cifBddSpec.getEdgesText(2)) {
                        cifBddSpec.settings.getDebugOutput().line(line);
                    }
                }
            }

            // Determine controlled system initialization predicate and check whether an initial state is present, or
            // the supervisor is empty.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            determineCtrlSysInit(cifBddSpec, synthResult, dbgEnabled);
            boolean emptySup = !checkInitStatePresent(synthResult);

            // Statistics: number of states in controlled system.
            if (settings.getSynthesisStatistics().contains(SynthesisStatistics.CTRL_SYS_STATES)) {
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return null;
                }
                printNumberStates(cifBddSpec, synthResult, emptySup, doForward, dbgEnabled);
            }

            // Determine the output of synthesis (1/2).
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            determineOutputInitial(cifBddSpec, synthResult, dbgEnabled);

            // Fail if supervisor is empty.
            if (emptySup) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().line();
                }
                throw new InvalidInputException("Empty supervisor.");
            }

            // Determine the guards for the controllable events.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            Map<Event, BDD> ctrlGuards = determineGuards(cifBddSpec, cifBddSpec.controllables, false);

            // Check edges.
            if (synthResult.settings.getDoNeverEnabledEventsWarn()) {
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return null;
                }
                checkOutputEdges(cifBddSpec, disabledEvents, synthResult, ctrlGuards, dbgEnabled);
            }

            // Determine the output of synthesis (2/2).
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }
            determineOutputGuards(cifBddSpec, synthResult, ctrlGuards, dbgEnabled);

            // Separate debug output from what is to come.
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
            }
        } finally {
            if (doTiming) {
                timing.postSynth.stop();
            }
        }

        // Return the synthesis result.
        return synthResult;
    }

    /**
     * Checks the system for problems with initialization, marking, and state invariants. Also prints related debug
     * information.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis. Is modified in-place.
     * @param synthResult The synthesis result.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void checkSystem(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult, boolean dbgEnabled) {
        // Debug state plant invariants (predicates) of the components.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: cifBddSpec.plantInvsComps) {
                cifBddSpec.settings.getDebugOutput().line("Invariant (component state plant invariant): %s",
                        bddToStr(pred, cifBddSpec));
            }
            cifBddSpec.settings.getDebugOutput().line("Invariant (components state plant inv):      %s",
                    bddToStr(cifBddSpec.plantInvComps, cifBddSpec));
        }

        // Debug state plant invariants (predicates) of the locations of the automata.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: cifBddSpec.plantInvsLocs) {
                cifBddSpec.settings.getDebugOutput().line("Invariant (location state plant invariant):  %s",
                        bddToStr(pred, cifBddSpec));
            }
            cifBddSpec.settings.getDebugOutput().line("Invariant (locations state plant invariant): %s",
                    bddToStr(cifBddSpec.plantInvLocs, cifBddSpec));
        }

        // Debug state plant invariant (predicate) of the system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("Invariant (system state plant invariant):    %s",
                    bddToStr(cifBddSpec.plantInv, cifBddSpec));
        }

        // Warn if no state in system, due to state plant invariants.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (cifBddSpec.plantInv.isZero()) {
            cifBddSpec.settings.getWarnOutput().line(
                    "The uncontrolled system has no states (taking into account only the state plant invariants).");
        }

        // Debug state requirement invariants (predicates) of the components.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            for (BDD pred: cifBddSpec.reqInvsComps) {
                cifBddSpec.settings.getDebugOutput().line("Invariant (component state req invariant):   %s",
                        bddToStr(pred, cifBddSpec));
            }
            cifBddSpec.settings.getDebugOutput().line("Invariant (components state req invariant):  %s",
                    bddToStr(cifBddSpec.reqInvComps, cifBddSpec));
        }

        // Debug state requirement invariants (predicates) of the locations of the automata.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: cifBddSpec.reqInvsLocs) {
                cifBddSpec.settings.getDebugOutput().line("Invariant (location state req invariant):    %s",
                        bddToStr(pred, cifBddSpec));
            }
            cifBddSpec.settings.getDebugOutput().line("Invariant (locations state req invariant):   %s",
                    bddToStr(cifBddSpec.reqInvLocs, cifBddSpec));
        }

        // Debug state requirement invariant (predicate) of the system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("Invariant (system state req invariant):      %s",
                    bddToStr(cifBddSpec.reqInv, cifBddSpec));
        }

        // Warn if no state in system, due to state requirement invariants.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (cifBddSpec.reqInv.isZero()) {
            cifBddSpec.settings.getWarnOutput().line(
                    "The controlled system has no states (taking into account only the state requirement invariants).");
        }

        // Debug initialization predicates of the discrete variables.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            for (int i = 0; i < cifBddSpec.variables.length; i++) {
                CifBddVariable var = cifBddSpec.variables[i];
                if (!(var instanceof CifBddDiscVariable)) {
                    continue;
                }

                String nr = String.valueOf(i);
                cifBddSpec.settings.getDebugOutput().line("Initial   (discrete variable %s):%s%s", nr,
                        Strings.spaces(14 - nr.length()), bddToStr(cifBddSpec.initialsVars.get(i), cifBddSpec));
            }

            cifBddSpec.settings.getDebugOutput().line("Initial   (discrete variables):              %s",
                    bddToStr(cifBddSpec.initialVars, cifBddSpec));
        }

        // Debug initialization predicates of the components.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: cifBddSpec.initialsComps) {
                cifBddSpec.settings.getDebugOutput().line("Initial   (component init predicate):        %s",
                        bddToStr(pred, cifBddSpec));
            }
            cifBddSpec.settings.getDebugOutput().line("Initial   (components init predicate):       %s",
                    bddToStr(cifBddSpec.initialComps, cifBddSpec));
        }

        // Debug initialization predicates of the locations of the automata.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: cifBddSpec.initialsLocs) {
                cifBddSpec.settings.getDebugOutput().line("Initial   (aut/locs init predicate):         %s",
                        bddToStr(pred, cifBddSpec));
            }
            cifBddSpec.settings.getDebugOutput().line("Initial   (auts/locs init predicate):        %s",
                    bddToStr(cifBddSpec.initialLocs, cifBddSpec));
        }

        // Debug initialization predicate of the uncontrolled system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("Initial   (uncontrolled system):             %s",
                    bddToStr(cifBddSpec.initial, cifBddSpec));
        }

        // Debug combined initialization and state plant invariants of the system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("Initial   (system, combined init/plant inv): %s",
                    bddToStr(cifBddSpec.initialPlantInv, cifBddSpec));
        }

        // Debug combined initialization and state invariants of the system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("Initial   (system, combined init/state inv): %s",
                    bddToStr(cifBddSpec.initialInv, cifBddSpec));
        }

        // Warn if no initial state in uncontrolled system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (cifBddSpec.initial.isZero()) {
            cifBddSpec.settings.getWarnOutput()
                    .line("The uncontrolled system has no initial state (taking into account only initialization).");
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (!cifBddSpec.initial.isZero() && !cifBddSpec.plantInv.isZero() && cifBddSpec.initialPlantInv.isZero()) {
            cifBddSpec.settings.getWarnOutput().line("The uncontrolled system has no initial state (taking into "
                    + "account only initialization and state plant invariants).");
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (!cifBddSpec.initialPlantInv.isZero() && !cifBddSpec.initial.isZero() && !cifBddSpec.plantInv.isZero()
                && !cifBddSpec.reqInv.isZero() && cifBddSpec.initialInv.isZero())
        {
            cifBddSpec.settings.getWarnOutput().line("The controlled system has no initial state (taking into account "
                    + "both initialization and state invariants).");
        }

        // Debug marker predicates of the components.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            for (BDD pred: cifBddSpec.markedsComps) {
                cifBddSpec.settings.getDebugOutput().line("Marked    (component marker predicate):      %s",
                        bddToStr(pred, cifBddSpec));
            }
            cifBddSpec.settings.getDebugOutput().line("Marked    (components marker predicate):     %s",
                    bddToStr(cifBddSpec.markedComps, cifBddSpec));
        }

        // Debug marker predicates of the locations of the automata.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: cifBddSpec.markedsLocs) {
                cifBddSpec.settings.getDebugOutput().line("Marked    (aut/locs marker predicate):       %s",
                        bddToStr(pred, cifBddSpec));
            }
            cifBddSpec.settings.getDebugOutput().line("Marked    (auts/locs marker predicate):      %s",
                    bddToStr(cifBddSpec.markedLocs, cifBddSpec));
        }

        // Debug marker predicate of the uncontrolled system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("Marked    (uncontrolled system):             %s",
                    bddToStr(cifBddSpec.marked, cifBddSpec));
        }

        // Debug combined marking and state plant invariants of the system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("Marked    (system, combined mark/plant inv): %s",
                    bddToStr(cifBddSpec.markedPlantInv, cifBddSpec));
        }

        // Debug combined marking and state invariants of the system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("Marked    (system, combined mark/state inv): %s",
                    bddToStr(cifBddSpec.markedInv, cifBddSpec));
        }

        // Warn if no marked state in uncontrolled system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (cifBddSpec.marked.isZero()) {
            cifBddSpec.settings.getWarnOutput()
                    .line("The uncontrolled system has no marked state (taking into account only marking).");
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (!cifBddSpec.marked.isZero() && !cifBddSpec.plantInv.isZero() && cifBddSpec.markedPlantInv.isZero()) {
            cifBddSpec.settings.getWarnOutput().line("The uncontrolled system has no marked state (taking into account "
                    + "only marking and state plant invariants).");
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (!cifBddSpec.markedPlantInv.isZero() && !cifBddSpec.marked.isZero() && !cifBddSpec.plantInv.isZero()
                && !cifBddSpec.reqInv.isZero() && cifBddSpec.markedInv.isZero())
        {
            cifBddSpec.settings.getWarnOutput().line("The controlled system has no marked state (taking into account "
                    + "both marking and state invariants).");
        }

        // Debug state/event exclusion plants.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("State/event exclusion plants:");
            cifBddSpec.settings.getDebugOutput().inc();
            if (cifBddSpec.stateEvtExclPlantLists.values().stream().flatMap(x -> x.stream()).findAny().isEmpty()) {
                cifBddSpec.settings.getDebugOutput().line("None");
            }
            for (Entry<Event, List<BDD>> entry: cifBddSpec.stateEvtExclPlantLists.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    continue;
                }
                cifBddSpec.settings.getDebugOutput().line("Event \"%s\" needs:",
                        CifTextUtils.getAbsName(entry.getKey()));
                cifBddSpec.settings.getDebugOutput().inc();
                for (BDD pred: entry.getValue()) {
                    cifBddSpec.settings.getDebugOutput().line(bddToStr(pred, cifBddSpec));
                }
                cifBddSpec.settings.getDebugOutput().dec();
            }
            cifBddSpec.settings.getDebugOutput().dec();
        }

        // Debug state/event exclusion requirements.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("State/event exclusion requirements:");
            cifBddSpec.settings.getDebugOutput().inc();
            if (cifBddSpec.stateEvtExclReqLists.values().stream().flatMap(x -> x.stream()).findAny().isEmpty()) {
                cifBddSpec.settings.getDebugOutput().line("None");
            }
            for (Entry<Event, List<BDD>> entry: cifBddSpec.stateEvtExclReqLists.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    continue;
                }
                cifBddSpec.settings.getDebugOutput().line("Event \"%s\" needs:",
                        CifTextUtils.getAbsName(entry.getKey()));
                cifBddSpec.settings.getDebugOutput().inc();
                for (BDD pred: entry.getValue()) {
                    cifBddSpec.settings.getDebugOutput().line(bddToStr(pred, cifBddSpec));
                }
                cifBddSpec.settings.getDebugOutput().dec();
            }
            cifBddSpec.settings.getDebugOutput().dec();
        }

        // Debug uncontrolled system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            if (cifBddSpec.stateEvtExclPlantLists.values().stream().flatMap(x -> x.stream()).findAny().isEmpty()) {
                cifBddSpec.settings.getDebugOutput().line("Uncontrolled system:");
            } else {
                cifBddSpec.settings.getDebugOutput()
                        .line("Uncontrolled system (state/event exclusion plants not applied yet):");
            }
            cifBddSpec.settings.getDebugOutput()
                    .line(synthResult.getCtrlBehText(1, cifBddSpec.settings.getIndentAmount()));
            if (!cifBddSpec.edges.isEmpty()) {
                for (String line: cifBddSpec.getEdgesText(2, true)) {
                    cifBddSpec.settings.getDebugOutput().line(line);
                }
            }
        }

        // Free no longer needed predicates.
        boolean freeReqsInvsCompsAndLocs = synthResult.settings
                .getStateReqInvEnforceMode() == StateReqInvEnforceMode.ALL_CTRL_BEH;
        cifBddSpec.freeIntermediateBDDs(freeReqsInvsCompsAndLocs);
    }

    /**
     * Print debug output for having restricted edge guards to prevent runtime errors, during the conversion from CIF to
     * BDD. This method should only be invoked if debug output is enabled.
     *
     * @param cifBddSpec The CIF/BDD specification.
     * @param synthResult The synthesis result.
     */
    private static void printDebugRuntimeErrorPrevention(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult) {
        List<CifBddEdge> restrictedEdges = cifBddSpec.edges.stream().filter(e -> !e.origGuard.equals(e.guard))
                .toList();

        cifBddSpec.settings.getDebugOutput().line();
        cifBddSpec.settings.getDebugOutput().line("Restricting edge guards to prevent runtime errors:");
        cifBddSpec.settings.getDebugOutput().inc();
        if (restrictedEdges.isEmpty()) {
            cifBddSpec.settings.getDebugOutput().line("No guards changed.");
        } else {
            for (CifBddEdge restrictedEdge: restrictedEdges) {
                cifBddSpec.settings.getDebugOutput().line(restrictedEdge.toString(0, 0, "Edge: "));
            }

            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Uncontrolled system:");
            cifBddSpec.settings.getDebugOutput().inc();
            cifBddSpec.settings.getDebugOutput().line(synthResult.getCtrlBehText(0, 0));
            cifBddSpec.settings.getDebugOutput().inc();
            for (String line: cifBddSpec.getEdgesText(0)) {
                cifBddSpec.settings.getDebugOutput().line(line);
            }
            cifBddSpec.settings.getDebugOutput().dec();
            cifBddSpec.settings.getDebugOutput().dec();
        }
        cifBddSpec.settings.getDebugOutput().dec();
    }

    /**
     * Applies the state requirement invariants, as preprocessing step for synthesis.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis. Is modified in-place.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void applyStateReqInvs(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            boolean dbgEnabled)
    {
        // Apply the state requirement invariants.
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Restricting behavior using state requirements:");
            cifBddSpec.settings.getDebugOutput().inc();
        }

        switch (synthResult.settings.getStateReqInvEnforceMode()) {
            case ALL_CTRL_BEH: {
                // Add the invariants to the controlled-behavior predicate. This ensures that a state is only in the
                // controlled system if the state requirement invariants hold.
                BDD newCtrlBeh = synthResult.ctrlBeh.and(cifBddSpec.reqInv);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    return;
                }

                if (synthResult.ctrlBeh.equals(newCtrlBeh)) {
                    newCtrlBeh.free();
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().line("Controlled behavior not changed.");
                    }
                } else {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().line(
                                "Controlled behavior: %s -> %s [state requirements: %s].",
                                bddToStr(synthResult.ctrlBeh, cifBddSpec), bddToStr(newCtrlBeh, cifBddSpec),
                                bddToStr(cifBddSpec.reqInv, cifBddSpec));
                    }
                    synthResult.ctrlBeh.free();
                    synthResult.ctrlBeh = newCtrlBeh;
                }
                break;
            }
            case PER_EDGE: {
                // Apply state requirement invariants, per edge.
                List<BDD> reqInvs = concat(cifBddSpec.reqInvsComps, cifBddSpec.reqInvsLocs);
                Function<CifBddEdge, Stream<BDD>> reqsPerEdge = edge -> reqInvs.stream().map(reqInv -> {
                    // Apply the requirement invariant backwards through the edge, to obtain a requirement condition
                    // that must hold for the edge to be taken. This condition further restricts the edge to prevent
                    // transitioning to a state that violates the requirement invariant.
                    BDD updPred = reqInv.id();
                    updPred = edge.apply(updPred, // pred
                            CifBddEdgeApplyDirection.BACKWARD, // backward
                            null); // restriction

                    // If trivial, we have the final predicate.
                    if (updPred.isOne()) {
                        return updPred;
                    }

                    // If termination is requested, we won't do any extra work on the predicate, as it won't be used.
                    if (cifBddSpec.settings.getTermination().isRequested()) {
                        return updPred;
                    }

                    // Compute 'guard and reqInv => updPred'. If the backwards applied state requirement invariant is
                    // already implied by the current guard and state requirement invariant in the source state, we
                    // don't need to strengthen the guard. In that case, replace the predicate by 'true', to ensure it
                    // gets ignored when it is applied.
                    BDD guardAndReqInv = edge.guard.and(reqInv);
                    BDD implication = guardAndReqInv.imp(updPred);
                    boolean skip = implication.isOne();
                    guardAndReqInv.free();
                    implication.free();
                    if (skip) {
                        updPred.free();
                        updPred = cifBddSpec.factory.one();
                    }

                    // Return the predicate to apply per edge.
                    return updPred;
                });
                applyReqsPerEdge(cifBddSpec, synthResult, reqsPerEdge, true, dbgEnabled, "state");

                // Restrict the initialization predicate of the controlled system, allowing only states that satisfy
                // the state requirement invariants.
                BDD newInitialCtrl = synthResult.initialCtrl.and(cifBddSpec.reqInv);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    return;
                }

                if (synthResult.initialCtrl.equals(newInitialCtrl)) {
                    newInitialCtrl.free();
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().line("Controlled-initialization not changed.");
                    }
                } else {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().line(
                                "Controlled initialization: %s -> %s [state requirements: %s].",
                                bddToStr(synthResult.initialCtrl, cifBddSpec), bddToStr(newInitialCtrl, cifBddSpec),
                                bddToStr(cifBddSpec.reqInv, cifBddSpec));
                    }
                    synthResult.initialCtrl.free();
                    synthResult.initialCtrl = newInitialCtrl;
                }

                // Cleanup.
                for (BDD bdd: cifBddSpec.reqInvsComps) {
                    bdd.free();
                }
                for (BDD bdd: cifBddSpec.reqInvsLocs) {
                    bdd.free();
                }
                cifBddSpec.reqInvsComps = null;
                cifBddSpec.reqInvsLocs = null;

                break;
            }
            default:
                throw new RuntimeException("Unknown mode: " + synthResult.settings.getStateReqInvEnforceMode());
        }

        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().dec();
        }
    }

    /**
     * Ensure that variables stay within their range, i.e. ensure no out of bounds values are ever used, as
     * preprocessing step for synthesis. This is done by enforcing that variables stay within their
     * {@link CifBddVariable#domain domains}.
     *
     * <p>
     * As an example, consider a variable 'x' of type 'int[0..2]'. Two BDD variables are used to represent the integer
     * CIF variable. The two BDD variables can represent 2^2=4 values, i.e. they can represent values of type
     * 'int[0..3]'. Value '3' can be represented, but is not a valid value of the variable, and should thus not be used.
     * The predicate that represents the value range is {@code 0 <= x <= 2}. This predicate is added to the
     * controlled-behavior predicate.
     * </p>
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void applyVarRanges(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult, boolean dbgEnabled) {
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Extending controlled-behavior predicate using variable ranges:");
            cifBddSpec.settings.getDebugOutput().inc();
        }

        boolean changed = false;
        for (CifBddVariable var: cifBddSpec.variables) {
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }

            // Compute out of range predicate.
            BDD range = BddUtils.getVarDomain(var, false, cifBddSpec.factory);
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }

            // Update controlled-behavior predicate.
            BDD newCtrlBeh = synthResult.ctrlBeh.and(range);
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }

            if (synthResult.ctrlBeh.equals(newCtrlBeh)) {
                newCtrlBeh.free();
                range.free();
            } else {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().line(
                            "Controlled behavior: %s -> %s [range: %s, variable: %s].",
                            bddToStr(synthResult.ctrlBeh, cifBddSpec), bddToStr(newCtrlBeh, cifBddSpec),
                            bddToStr(range, cifBddSpec), var.toString(0, cifBddSpec.settings.getIndentAmount(), ""));
                }
                range.free();
                synthResult.ctrlBeh.free();
                synthResult.ctrlBeh = newCtrlBeh;
                changed = true;
            }
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }
            return;
        }
        if (dbgEnabled) {
            if (changed) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line(
                        "Extended controlled-behavior predicate using variable ranges: %s.",
                        bddToStr(synthResult.ctrlBeh, cifBddSpec));
            } else {
                cifBddSpec.settings.getDebugOutput().line("Controlled behavior not changed.");
            }
            cifBddSpec.settings.getDebugOutput().dec();
        }
    }

    /**
     * Applies the state/event exclusion requirement invariants, as preprocessing step for synthesis.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis. Is modified in-place.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void applyStateEvtExclReqs(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            boolean dbgEnabled)
    {
        // Update guards and controlled-behavior predicate, to ensure that transitions not allowed by the state/event
        // exclusion requirement invariants, are blocked.
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Restricting behavior using state/event exclusion requirements:");
            cifBddSpec.settings.getDebugOutput().inc();
        }

        // Apply state/event exclusion requirement invariants, per edge.
        Function<CifBddEdge, Stream<BDD>> reqsPerEdge = edge -> {
            BDD req = cifBddSpec.stateEvtExclReqs.get(edge.event);
            return (req == null) ? Stream.empty() : Stream.of(req);
        };
        applyReqsPerEdge(cifBddSpec, synthResult, reqsPerEdge, false, dbgEnabled, "state/event exclusion");
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().dec();
        }

        // Free no longer needed predicates.
        for (BDD bdd: cifBddSpec.stateEvtExclReqs.values()) {
            bdd.free();
        }
        cifBddSpec.stateEvtExclReqs = null;
    }

    /**
     * Applies the implicit requirements to prevent runtime errors for edges with uncontrollable events. That is, for
     * edges with uncontrollable events, if applying the edge would lead to runtime errors, this can't be prevented by
     * the supervisor, so the source states are bad. We put the negation of those bad states into the
     * controlled-behavior predicate, as a preprocessing step for synthesis.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void applyRuntimeErrorReqs(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            boolean dbgEnabled)
    {
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput()
                    .line("Restricting behavior using implicit runtime error requirements:");
            cifBddSpec.settings.getDebugOutput().inc();
        }

        // For every edge with an uncontrollable event, restrict the controlled-behavior predicate by disallowing states
        // where the original edge guard holds while also a runtime error would occur when applying that edge. Note that
        // for controllable events this doesn't hold, since we are not allowed to prevent the source states of such
        // edges, but instead must prevent runtime errors by preventing the transitions. And this is prevented in both
        // forward and backward searches since the edge guards disallow the edge to be taken from runtime error states.
        boolean changed = false;
        for (CifBddEdge edge: cifBddSpec.edges) {
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }

            if (!edge.event.getControllable()) {
                BDD guardError = edge.origGuard.and(edge.error);
                BDD guardErrorNot = guardError.not();
                guardError.free();
                BDD newCtrlBeh = synthResult.ctrlBeh.and(guardErrorNot);

                if (!newCtrlBeh.equals(synthResult.ctrlBeh)) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().line(
                                "Controlled behavior: %s -> %s [runtime error requirement (event: %s): %s].",
                                bddToStr(synthResult.ctrlBeh, cifBddSpec), bddToStr(newCtrlBeh, cifBddSpec),
                                edge.event.getName(), bddToStr(guardErrorNot, cifBddSpec));
                    }
                    changed = true;
                }

                guardErrorNot.free();
                synthResult.ctrlBeh.free();
                synthResult.ctrlBeh = newCtrlBeh;
            }
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }
            return;
        }
        if (dbgEnabled) {
            if (changed) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line(
                        "Restricted behavior using implicit runtime error requirements: %s.",
                        bddToStr(synthResult.ctrlBeh, cifBddSpec));
            } else {
                cifBddSpec.settings.getDebugOutput().line("Controlled behavior not changed.");
            }
            cifBddSpec.settings.getDebugOutput().dec();
        }
    }

    /**
     * Apply requirements, per edge.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param reqsPerEdge Function that gives per edge a stream of requirements to apply.
     * @param freeReqs Whether to free the requirements after they are applied ({@code true}) or not ({@code false}).
     * @param dbgEnabled Whether debug output is enabled.
     * @param dbgDescription Description of the kind of requirements that are applied.
     */
    private static void applyReqsPerEdge(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            Function<CifBddEdge, Stream<BDD>> reqsPerEdge, boolean freeReqs, boolean dbgEnabled, String dbgDescription)
    {
        boolean changed = false;
        boolean guardChanged = false;
        for (CifBddEdge edge: cifBddSpec.edges) {
            // Get requirements for the edge.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }
            Stream<BDD> reqsStream = reqsPerEdge.apply(edge);
            Iterable<BDD> reqsIterable = () -> reqsStream.iterator();

            // Process each requirement.
            for (BDD req: reqsIterable) {
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return;
                }

                // Skip trivially true requirements.
                if (req.isOne()) {
                    continue;
                }

                // Enforce the additional condition.
                if (edge.event.getControllable()) {
                    // For controllable events, we can simply restrict the guard.
                    BDD newGuard = edge.guard.and(req);
                    if (cifBddSpec.settings.getTermination().isRequested()) {
                        return;
                    }

                    if (edge.guard.equals(newGuard)) {
                        newGuard.free();
                    } else {
                        if (dbgEnabled) {
                            cifBddSpec.settings.getDebugOutput().line("Edge %s: guard: %s -> %s [%s requirement: %s].",
                                    edge.toString(0, cifBddSpec.settings.getIndentAmount(), ""),
                                    bddToStr(edge.guard, cifBddSpec), bddToStr(newGuard, cifBddSpec), dbgDescription,
                                    bddToStr(req, cifBddSpec));
                        }
                        edge.guard.free();
                        edge.guard = newGuard;
                        changed = true;
                        guardChanged = true;
                    }
                } else {
                    // For uncontrollable events, update the controlled-behavior predicate. If the guard of the edge
                    // holds (event enabled in the plant), and the requirement condition doesn't hold (event disabled
                    // by the requirement), the edge may not be taken.
                    //
                    // reqBad = guard && !req
                    // reqGood = !(guard && !req) = !guard || req = guard => req
                    //
                    // Only good states in controlled behavior. So restrict controlled behavior with 'reqGood'.
                    BDD reqGood = edge.guard.imp(req);
                    if (cifBddSpec.settings.getTermination().isRequested()) {
                        return;
                    }

                    BDD newCtrlBeh = synthResult.ctrlBeh.id().andWith(reqGood);
                    if (cifBddSpec.settings.getTermination().isRequested()) {
                        return;
                    }

                    if (synthResult.ctrlBeh.equals(newCtrlBeh)) {
                        newCtrlBeh.free();
                    } else {
                        if (dbgEnabled) {
                            cifBddSpec.settings.getDebugOutput().line(
                                    "Controlled behavior: %s -> %s [%s requirement: %s, edge: %s].",
                                    bddToStr(synthResult.ctrlBeh, cifBddSpec), bddToStr(newCtrlBeh, cifBddSpec),
                                    dbgDescription, bddToStr(req, cifBddSpec),
                                    edge.toString(0, cifBddSpec.settings.getIndentAmount(), ""));
                        }
                        synthResult.ctrlBeh.free();
                        synthResult.ctrlBeh = newCtrlBeh;
                        changed = true;
                    }
                }

                // Free requirement predicate, if requested.
                if (freeReqs) {
                    req.free();
                }
            }
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (dbgEnabled) {
            if (changed) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Restricted behavior using %s requirements:", dbgDescription);
                cifBddSpec.settings.getDebugOutput().inc();
                cifBddSpec.settings.getDebugOutput()
                        .line(synthResult.getCtrlBehText(0, cifBddSpec.settings.getIndentAmount()));
                if (guardChanged && !cifBddSpec.edges.isEmpty()) {
                    cifBddSpec.settings.getDebugOutput().inc();
                    for (String line: cifBddSpec.getEdgesText(0)) {
                        cifBddSpec.settings.getDebugOutput().line(line);
                    }
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                cifBddSpec.settings.getDebugOutput().dec();
            } else {
                cifBddSpec.settings.getDebugOutput().line("Guards and controlled behavior not changed.");
            }
        }
    }

    /**
     * Checks the system for problems with events that are never enabled in the input specification. Saves the disabled
     * events.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     * @return The events that are disabled before synthesis. May be incomplete if termination is requested.
     */
    private static Set<Event> checkInputEdges(CifBddSpec cifBddSpec, boolean dbgEnabled) {
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Checking pre-synthesis for events that are never enabled.");
        }

        Set<Event> disabledEvents = setc(cifBddSpec.alphabet.size());
        for (Event event: cifBddSpec.alphabet) {
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return disabledEvents;
            }

            // Skip events for input variables as they have no edges.
            if (cifBddSpec.inputVarEvents.contains(event)) {
                continue;
            }

            // Skip events that are in the alphabet, but never on an edge as these are globally disabled. Note, the type
            // checker reports these already.
            if (cifBddSpec.eventEdges.get(event) == null) {
                disabledEvents.add(event);
                continue;
            }

            // Check whether the combined state/event exclusion plants are 'false'.
            if (cifBddSpec.stateEvtExclPlants.get(event).isZero()) {
                cifBddSpec.settings.getWarnOutput()
                        .line("Event \"%s\" is never enabled in the input specification, taking into account only "
                                + "state/event exclusion plants.", CifTextUtils.getAbsName(event));
                disabledEvents.add(event);
                continue;
            }

            // Check whether the combined state/event exclusion requirements are 'false'.
            if (event.getControllable() && cifBddSpec.stateEvtExclsReqInvs.get(event).isZero()) {
                cifBddSpec.settings.getWarnOutput()
                        .line("Event \"%s\" is never enabled in the input specification, taking into account only "
                                + "state/event exclusion requirements.", CifTextUtils.getAbsName(event));
                disabledEvents.add(event);
                continue;
            }

            // Check whether the guards on edges of automata are all 'false'. There might be multiple edges for an
            // event.
            if (cifBddSpec.eventEdges.get(event).stream().filter(edge -> !edge.origGuard.isZero()).count() == 0) {
                cifBddSpec.settings.getWarnOutput()
                        .line("Event \"%s\" is never enabled in the input specification, taking into account only "
                                + "automaton guards.", CifTextUtils.getAbsName(event));
                disabledEvents.add(event);
                continue;
            }

            // Check whether the guards on edges of automata combined with invariants are all 'false'. There might be
            // multiple edges for an event. State/event exclusion plant invariants are included in the edge guards.
            // State/event exclusion requirement invariants are included in the edge guards for controllable events.
            // State plant invariants and state requirement invariants are sometimes included in the edge guard
            // (it may depend on settings, and on whether the edge guard was strengthened). To simplify the
            // implementation and make it
            // more consistent regardless, we always include the state invariants again.
            boolean alwaysDisabled = true;
            for (CifBddEdge edge: cifBddSpec.eventEdges.get(event)) {
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return disabledEvents;
                }

                BDD enabledExpression = edge.guard.and(cifBddSpec.reqInv);
                enabledExpression = enabledExpression.andWith(cifBddSpec.plantInv.id());
                if (!enabledExpression.isZero()) {
                    enabledExpression.free();
                    alwaysDisabled = false;
                    break;
                }
            }

            if (alwaysDisabled) {
                cifBddSpec.settings.getWarnOutput()
                        .line("Event \"%s\" is never enabled in the input specification, taking into account automaton "
                                + "guards, prevention of runtime errors, and invariants.",
                                CifTextUtils.getAbsName(event));
                disabledEvents.add(event);
                continue;
            }
        }

        // Return the events that are disabled before synthesis.
        return disabledEvents;
    }

    /**
     * Prepare edge workset algorithm.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis. Is modified in-place.
     * @param forwardEnabled Whether to perform forward reachability during synthesis ({@code true}) or omit it
     *     ({@code false}).
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void prepareWorksetAlgorithm(CifBddSpec cifBddSpec, boolean forwardEnabled, boolean dbgEnabled) {
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Preparing workset algorithm:");
            cifBddSpec.settings.getDebugOutput().inc();
        }

        // Compute the dependency sets for all edges, and store them in the CIF/BDD specification.
        EdgeDependencySetCreator creator = new BddBasedEdgeDependencySetCreator();
        creator.createAndStore(cifBddSpec, forwardEnabled);

        // Print dependency sets as debug output.
        if (dbgEnabled) {
            int edgeCnt = cifBddSpec.worksetDependenciesBackward.size();
            if (edgeCnt == 0) {
                cifBddSpec.settings.getDebugOutput().line("No edges.");
            } else {
                if (forwardEnabled) {
                    cifBddSpec.settings.getDebugOutput().line("Edge workset algorithm forward dependencies:");
                    GridBox box = new GridBox(edgeCnt, 4, 0, 1);
                    for (int i = 0; i < edgeCnt; i++) {
                        BitSet bitset = cifBddSpec.worksetDependenciesForward.get(i);
                        box.set(i, 0, "-");
                        box.set(i, 1, Integer.toString(i + 1) + ":");
                        box.set(i, 2, CifTextUtils.getAbsName(cifBddSpec.orderedEdgesForward.get(i).event));
                        box.set(i, 3, BitSets.bitsetToStr(bitset, edgeCnt));
                    }
                    for (String line: box.getLines()) {
                        cifBddSpec.settings.getDebugOutput().line(line);
                    }
                }

                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Edge workset algorithm backward dependencies:");
                GridBox box = new GridBox(edgeCnt, 4, 0, 1);
                for (int i = 0; i < edgeCnt; i++) {
                    BitSet bitset = cifBddSpec.worksetDependenciesBackward.get(i);
                    box.set(i, 0, "-");
                    box.set(i, 1, Integer.toString(i + 1) + ":");
                    box.set(i, 2, CifTextUtils.getAbsName(cifBddSpec.orderedEdgesBackward.get(i).event));
                    box.set(i, 3, BitSets.bitsetToStr(bitset, edgeCnt));
                }
                for (String line: box.getLines()) {
                    cifBddSpec.settings.getDebugOutput().line(line);
                }
            }
            cifBddSpec.settings.getDebugOutput().dec();
        }
    }

    /**
     * Performs the actual synthesis algorithm from the paper, calculating various fixed points.
     *
     * @param cifBddSpec The CIF/BDD specification on which to perform synthesis.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param doForward Whether to do forward reachability during synthesis.
     * @param dbgEnabled Whether debug output is enabled.
     * @param doTiming Whether to collect timing statistics.
     * @param timing The timing statistics data. Is modified in-place.
     */
    private static void synthesizeFixedPoints(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            boolean doForward, boolean dbgEnabled, boolean doTiming, CifDataSynthesisTiming timing)
    {
        // We know that:
        // - Each round, we perform the same computations, in the same order.
        // - Each computation is a fixed-point reachability computation.
        // - All the computations take a controlled behavior and produce a potentially changed controlled behavior.
        // - All other data that is used is constant. For instance, marking and initialization predicates of the
        // original model are used, and they don't change during the fixed-point computations. The used guards and
        // update relations also don't change during the fixed-point computations, etc.
        //
        // To ensure a proper fixed-point result for synthesis:
        // - We need to perform all the computations at least once.
        // - We can stop as soon as the controlled behavior remains stable for all the computations.
        //
        // Therefore:
        // - We keep track of the number of consecutive computations that produced a stable result.
        // - If a computation does not change the controlled behavior, we can increment the number by one.
        // - If a computation changed the controlled behavior, we need to reconsider the other computations again.
        // However, the computation itself is a fixed-point computation, and thus won't change the controlled behavior
        // when applied again. Hence, that computation is the first computation after the change that produced a stable
        // result, and the number can be set to one.
        // - As soon as the number of consecutive computations that produced a stable result equals the total number of
        // computations to perform, we have produced a stable result for all the computations.
        //
        // Note that:
        // - We will perform each computation at least once, as we need that many increments to reach the termination
        // condition.
        // - We stop as soon as the controlled behavior is stable, regardless of whether we have completed the current
        // round of performing the computations, since there is no need to finish the round if the controlled behavior
        // is already stable.

        // Get the fixed-point reachability computations to perform, in the order to perform them.
        List<FixedPointComputation> computationsInOrder = synthResult.settings
                .getFixedPointComputationsOrder().computations;
        if (!doForward) {
            computationsInOrder = computationsInOrder.stream().filter(c -> c != REACH).toList();
        }
        int numberOfComputations = computationsInOrder.size();

        // Perform synthesis.
        int round = 0;
        int stableCount = 0;
        FIXED_POINT_LOOP:
        while (true) {
            // Next round.
            round++;
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }

            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Synthesis round %d:", round);
                cifBddSpec.settings.getDebugOutput().inc();
            }

            // Perform the fixed-point reachability computations of the round.
            boolean firstComputationInRound = true;
            for (FixedPointComputation fixedPointComputation: computationsInOrder) {
                // Get predicate from which to start the fixed-point reachability computation.
                BDD startPred = switch (fixedPointComputation) {
                    case NONBLOCK -> cifBddSpec.marked.id();
                    case CTRL -> synthResult.ctrlBeh.not();
                    case REACH -> synthResult.initialCtrl.id();
                };
                if (fixedPointComputation == CTRL && cifBddSpec.settings.getTermination().isRequested()) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    return;
                }

                // Configure fixed-point reachability computation.
                String predName; // Name of the predicate to compute.
                String initName; // Name of the initial value of the predicate.
                String restrictionName; // Name of the restriction predicate, if applicable.
                BDD restriction; // The restriction predicate, if applicable.
                CifBddEdgeApplyDirection direction; //  The direction of the reachability computation..
                Set<CifBddEdgeKind> edgeKinds; // Kinds of edges to apply.
                switch (fixedPointComputation) {
                    case NONBLOCK:
                        predName = "backward controlled-behavior";
                        initName = "marker";
                        restrictionName = "current/previous controlled-behavior";
                        restriction = synthResult.ctrlBeh;
                        direction = CifBddEdgeApplyDirection.BACKWARD;
                        edgeKinds = EnumSet.allOf(CifBddEdgeKind.class);
                        break;
                    case CTRL:
                        predName = "backward uncontrolled bad-state";
                        initName = "current/previous controlled behavior";
                        restrictionName = null;
                        restriction = null;
                        direction = CifBddEdgeApplyDirection.BACKWARD;
                        edgeKinds = EnumSet.of(CifBddEdgeKind.UNCONTROLLABLE, CifBddEdgeKind.INPUT_VARIABLE);
                        break;
                    case REACH:
                        predName = "forward controlled-behavior";
                        initName = "initialization";
                        restrictionName = "current/previous controlled-behavior";
                        restriction = synthResult.ctrlBeh;
                        direction = CifBddEdgeApplyDirection.FORWARD;
                        edgeKinds = EnumSet.allOf(CifBddEdgeKind.class);
                        break;
                    default:
                        throw new RuntimeException("Unknown fixed-point computation: " + fixedPointComputation);
                }

                // Start timing the fixed-point reachability computation.
                if (doTiming) {
                    Stopwatch stopwatch = switch (fixedPointComputation) {
                        case NONBLOCK -> timing.mainBwMarked;
                        case CTRL -> timing.mainBwBadState;
                        case REACH -> timing.mainFwInit;
                    };
                    stopwatch.start();
                }

                // Debug output.
                if (dbgEnabled) {
                    if (firstComputationInRound) {
                        firstComputationInRound = false;
                    } else {
                        cifBddSpec.settings.getDebugOutput().line();
                    }
                    cifBddSpec.settings.getDebugOutput().line("Computing %s predicate:", predName);
                    cifBddSpec.settings.getDebugOutput().inc();
                }

                // Perform the fixed-point reachability computation.
                BDD reachabilityResult;
                try {
                    CifBddReachability reachability = new CifBddReachability(cifBddSpec, predName, initName,
                            restrictionName, restriction, direction, edgeKinds, dbgEnabled);
                    reachabilityResult = reachability.performReachability(startPred);
                } finally {
                    // Stop timing the fixed-point reachability computation.
                    if (doTiming) {
                        Stopwatch stopwatch = switch (fixedPointComputation) {
                            case NONBLOCK -> timing.mainBwMarked;
                            case CTRL -> timing.mainBwBadState;
                            case REACH -> timing.mainFwInit;
                        };
                        stopwatch.stop();
                    }
                }

                if (cifBddSpec.settings.getTermination().isRequested()) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().dec();
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    return;
                }

                // Get new controlled behavior.
                BDD newCtrlBeh;
                switch (fixedPointComputation) {
                    case NONBLOCK:
                    case REACH:
                        newCtrlBeh = reachabilityResult;
                        break;
                    case CTRL:
                        newCtrlBeh = reachabilityResult.not();
                        reachabilityResult.free();
                        if (cifBddSpec.settings.getTermination().isRequested()) {
                            if (dbgEnabled) {
                                cifBddSpec.settings.getDebugOutput().dec();
                                cifBddSpec.settings.getDebugOutput().dec();
                            }
                            return;
                        }
                        break;
                    default:
                        throw new RuntimeException("Unknown fixed-point computation: " + fixedPointComputation);
                }

                // Detect change in controlled behavior.
                boolean unchanged = synthResult.ctrlBeh.equals(newCtrlBeh);
                boolean changed = !unchanged;
                if (unchanged) {
                    newCtrlBeh.free();
                    stableCount++;
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().line();
                        cifBddSpec.settings.getDebugOutput().line("Controlled behavior not changed.");
                    }
                } else {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().line();
                        cifBddSpec.settings.getDebugOutput().line("Controlled behavior: %s -> %s.",
                                bddToStr(synthResult.ctrlBeh, cifBddSpec), bddToStr(newCtrlBeh, cifBddSpec));
                    }
                    synthResult.ctrlBeh.free();
                    synthResult.ctrlBeh = newCtrlBeh;
                    stableCount = 1;
                }
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }

                // Detect a fixed point for all fixed-point computations (as far as they are not disabled by settings):

                // 1) Check for empty controlled behavior.
                BDD ctrlStates = synthResult.ctrlBeh.and(cifBddSpec.plantInv);
                boolean noCtrlStates = ctrlStates.isZero();
                ctrlStates.free();
                if (noCtrlStates) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().line();
                        cifBddSpec.settings.getDebugOutput().line("Finished: all states are bad.");
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    break FIXED_POINT_LOOP;
                }
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    return;
                }

                // 2) Check for controlled behavior being stable, after having performed all computations at least once.
                if (stableCount == numberOfComputations) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().line();
                        cifBddSpec.settings.getDebugOutput().line("Finished: controlled behavior is stable.");
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    break FIXED_POINT_LOOP;
                }

                // 3) Check for no initial states left, if the controlled behavior changed. There is no need to check
                // this for forward reachability, as it starts from the initial states, and if there are no initial
                // states, then the controlled behavior is empty and a fixed point was detected above already.
                if (changed && fixedPointComputation != REACH) {
                    BDD init = synthResult.initialCtrl.and(synthResult.ctrlBeh);
                    boolean noInit = init.isZero();
                    init.free();
                    if (noInit) {
                        if (dbgEnabled) {
                            cifBddSpec.settings.getDebugOutput().line();
                            cifBddSpec.settings.getDebugOutput().line("Finished: no initialization possible.");
                            cifBddSpec.settings.getDebugOutput().dec();
                        }
                        break FIXED_POINT_LOOP;
                    }
                    if (cifBddSpec.settings.getTermination().isRequested()) {
                        if (dbgEnabled) {
                            cifBddSpec.settings.getDebugOutput().dec();
                        }
                        return;
                    }
                }
            }

            // Finished round.
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().line();
                cifBddSpec.settings.getDebugOutput().line("Need another round.", round);
                cifBddSpec.settings.getDebugOutput().dec();
            }
        }
    }

    /**
     * Determines the guards of the controlled system, for each linearized edge.
     *
     * @param cifBddSpec The CIF/BDD specification on which synthesis was performed. Is modified in-place.
     * @param synthResult The synthesis result.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void determineCtrlSysGuards(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            boolean dbgEnabled)
    {
        // Compute guards of edges with controllable events.
        //
        // By applying edges with controllable events backwards, we also apply their error predicates, which are
        // embedded in the guards of the edges. This ensures that synthesis prevents runtime errors for edges with
        // controllable events, by putting extra restrictions in the controlled system guards.
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Computing final controlled system guards:");
            cifBddSpec.settings.getDebugOutput().inc();
        }

        boolean guardUpdated = false;
        for (CifBddEdge edge: cifBddSpec.edges) {
            if (!edge.event.getControllable()) {
                continue;
            }
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }

            BDD updPred = synthResult.ctrlBeh.id();
            updPred = edge.apply(updPred, CifBddEdgeApplyDirection.BACKWARD, null);
            edge.cleanupApply();
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }

            BDD newGuard = edge.guard.id().andWith(updPred);
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }

            if (edge.guard.equals(newGuard)) {
                newGuard.free();
            } else {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().line("Edge %s: guard: %s -> %s.",
                            edge.toString(0, cifBddSpec.settings.getIndentAmount(), ""),
                            bddToStr(edge.guard, cifBddSpec), bddToStr(newGuard, cifBddSpec));
                }
                edge.guard.free();
                edge.guard = newGuard;
                guardUpdated = true;
            }
        }

        if (dbgEnabled) {
            if (!guardUpdated) {
                cifBddSpec.settings.getDebugOutput().line("No guards changed.");
            }
            cifBddSpec.settings.getDebugOutput().dec();
        }
    }

    /**
     * Determine the initialization predicate of the controlled system, updating the initialization predicate of the
     * controlled system as computed so far ({@link CifDataSynthesisResult#initialCtrl}) with the controlled behavior.
     *
     * @param cifBddSpec The CIF/BDD specification on which synthesis was performed. Is modified in-place.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void determineCtrlSysInit(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            boolean dbgEnabled)
    {
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Computing initialization predicate of the controlled system.");
        }

        // Update initialization predicate for controlled system with the controlled behavior.
        synthResult.initialCtrl = synthResult.initialCtrl.andWith(synthResult.ctrlBeh.id());

        // Free no longer needed predicates.
        cifBddSpec.initialPlantInv.free();
        cifBddSpec.initialPlantInv = null;
    }

    /**
     * Check the synthesis result, to see whether an initial state is still present.
     *
     * @param synthResult The synthesis result.
     * @return Whether an initial state exists in the controlled system ({@code true}) or the supervisor is empty
     *     ({@code false}).
     */
    private static boolean checkInitStatePresent(CifDataSynthesisResult synthResult) {
        // Check for empty supervisor (no initial state).
        boolean emptySup = synthResult.initialCtrl.isZero();
        return !emptySup;
    }

    /**
     * Prints the number of states in the controlled system, as normal output, separating it from the debug output.
     *
     * @param cifBddSpec The CIF/BDD specification on which synthesis was performed.
     * @param synthResult The synthesis result.
     * @param emptySup Whether the supervisor is empty.
     * @param doForward Whether to do forward reachability during synthesis.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void printNumberStates(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult, boolean emptySup,
            boolean doForward, boolean dbgEnabled)
    {
        // Get number of states in controlled system.
        double nr;
        if (emptySup) {
            nr = 0;
        } else if (cifBddSpec.variables.length == 0) {
            Assert.check(synthResult.ctrlBeh.isZero() || synthResult.ctrlBeh.isOne());
            nr = synthResult.ctrlBeh.isOne() ? 1 : 0;
        } else {
            nr = synthResult.ctrlBeh.satCount(cifBddSpec.varSetOld);
        }
        Assert.check(emptySup || nr > 0);

        // Print controlled system states statistics.
        boolean isExact = emptySup || doForward;
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
        }
        synthResult.settings.getNormalOutput().line("Controlled system: %s %,.0f state%s.",
                (isExact ? "exactly" : "at most"), nr, nr == 1 ? "" : "s");
    }

    /**
     * Determines the synthesis output, related to initialization, that is to end up in the resulting CIF model.
     *
     * @param cifBddSpec The CIF/BDD specification on which synthesis was performed. Is modified in-place.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void determineOutputInitial(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            boolean dbgEnabled)
    {
        // Print some debug output.
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Determining initialization predicate for output model:");
            cifBddSpec.settings.getDebugOutput().inc();
            cifBddSpec.settings.getDebugOutput().line("Initial (synthesis result):            %s",
                    bddToStr(synthResult.ctrlBeh, cifBddSpec));
            cifBddSpec.settings.getDebugOutput().line("Initial (uncontrolled system):         %s",
                    bddToStr(cifBddSpec.initial, cifBddSpec));
            cifBddSpec.settings.getDebugOutput().line("Initial (controlled system):           %s",
                    bddToStr(synthResult.initialCtrl, cifBddSpec));
        }
        if (cifBddSpec.settings.getTermination().isRequested()) {
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }
            return;
        }

        // What initialization was allowed in the uncontrolled system, but is no longer allowed in the controlled
        // system, as thus has been removed as allowed initialization? The inverse of that is what the supervisor adds
        // as additional initialization restriction on top of the uncontrolled system.
        BDD initialRemoved = cifBddSpec.initial.id().andWith(synthResult.initialCtrl.not());
        if (cifBddSpec.settings.getTermination().isRequested()) {
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }
            return;
        }

        BDD initialAdded = initialRemoved.not();
        if (cifBddSpec.settings.getTermination().isRequested()) {
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }
            return;
        }

        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line("Initial (removed by supervisor):       %s",
                    bddToStr(initialRemoved, cifBddSpec));
            cifBddSpec.settings.getDebugOutput().line("Initial (added by supervisor):         %s",
                    bddToStr(initialAdded, cifBddSpec));
        }
        if (cifBddSpec.settings.getTermination().isRequested()) {
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }
            return;
        }

        // Determine initialization predicate. The initialization predicate of the controlled system is used, if it is
        // at all restricted with respect to the uncontrolled system.
        EnumSet<BddSimplify> simplifications = synthResult.settings.getBddSimplifications();
        List<String> assumptionTxts = list();

        if (!initialRemoved.isZero()) {
            synthResult.initialOutput = synthResult.initialCtrl.id();
            BDD assumption = cifBddSpec.factory.one();

            // If requested, the controlled system initialization predicate is simplified under the assumption of the
            // uncontrolled system initialization predicate, to obtain the additional initialization restrictions
            // introduced by the controller with respect to the uncontrolled system initialization predicate.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }
            if (simplifications.contains(BddSimplify.INITIAL_UNCTRL)) {
                assumptionTxts.add("uncontrolled system initialization predicates");

                BDD extra = cifBddSpec.initial.id();
                assumption = assumption.andWith(extra);
            }

            // If requested, the controlled system initialization predicate is simplified under the assumption of the
            // state plant invariants, to obtain the additional initialization restrictions introduced by the
            // controller with respect to the state plant invariants.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                return;
            }
            if (simplifications.contains(BddSimplify.INITIAL_STATE_PLANT_INVS)) {
                assumptionTxts.add("state plant invariants");

                BDD extra = cifBddSpec.plantInv.id();
                assumption = assumption.andWith(extra);
            }

            // Perform simplification if there are assumptions.
            if (!assumptionTxts.isEmpty()) {
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    return;
                }

                String assumptionsTxt = combineAssumptionTexts(assumptionTxts);
                if (dbgEnabled) {
                    cifBddSpec.settings.getDebugOutput().line();
                    cifBddSpec.settings.getDebugOutput().line("Simplifying of controlled system initialization "
                            + "predicate under the assumption of the %s:", assumptionsTxt);
                    cifBddSpec.settings.getDebugOutput().inc();
                }

                BDD newInitial = synthResult.initialOutput.simplify(assumption);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    if (dbgEnabled) {
                        cifBddSpec.settings.getDebugOutput().dec();
                        cifBddSpec.settings.getDebugOutput().dec();
                    }
                    return;
                }

                if (dbgEnabled) {
                    if (synthResult.initialOutput.equals(newInitial)) {
                        cifBddSpec.settings.getDebugOutput().line("Predicate not changed.");
                    } else {
                        cifBddSpec.settings.getDebugOutput().line("Initial: %s -> %s [assume %s].",
                                bddToStr(synthResult.initialOutput, cifBddSpec), bddToStr(newInitial, cifBddSpec),
                                bddToStr(assumption, cifBddSpec));
                    }
                    cifBddSpec.settings.getDebugOutput().dec();
                }
                synthResult.initialOutput.free();
                synthResult.initialOutput = newInitial;
            }

            assumption.free();
        }

        if (cifBddSpec.settings.getTermination().isRequested()) {
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }
            return;
        }

        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Initial (output model):                %s",
                    (synthResult.initialOutput == null) ? "n/a" : bddToStr(synthResult.initialOutput, cifBddSpec));
            cifBddSpec.settings.getDebugOutput().dec();
        }

        // Free no longer needed predicates.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            if (dbgEnabled) {
                cifBddSpec.settings.getDebugOutput().dec();
            }
            return;
        }

        synthResult.initialCtrl.free();
        cifBddSpec.initial.free();
        initialRemoved.free();
        initialAdded.free();

        synthResult.initialCtrl = null;
        cifBddSpec.initial = null;
    }

    /**
     * Computes the global guards for the given events. This is done by combining the guards of all edges, per event.
     *
     * <p>
     * The guard BDDs on the edges are consumed.
     * </p>
     *
     * @param cifBddSpec The CIF/BDD specification on which synthesis was performed.
     * @param events The events for which to compute the guards.
     * @param useOrigGuards Whether to use the original guard or the current guard on the CIF/BDD edge.
     * @return The guards.
     */
    private static Map<Event, BDD> determineGuards(CifBddSpec cifBddSpec, Set<Event> events, boolean useOrigGuards) {
        Map<Event, BDD> guards = mapc(events.size());

        // Initialize guards to 'false'.
        for (Event event: events) {
            guards.put(event, cifBddSpec.factory.zero());
        }

        // Compute linearized guards. This is done by combining the guards of all edges, per event.
        for (CifBddEdge cifBddEdge: cifBddSpec.edges) {
            // Skip the edges for other events.
            if (!events.contains(cifBddEdge.event)) {
                continue;
            }
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return null;
            }

            // Get current guard.
            BDD guard = guards.get(cifBddEdge.event);

            // Update guard. Frees the guard of the edge.
            guard = useOrigGuards ? guard.orWith(cifBddEdge.origGuard) : guard.orWith(cifBddEdge.guard);

            // Store updated guard.
            guards.put(cifBddEdge.event, guard);
        }

        return guards;
    }

    /**
     * Checks the system for problems with events that are disabled in the global controlled system.
     *
     * <p>
     * Does not report problems for events that were already disabled before synthesis.
     * </p>
     *
     * @param cifBddSpec The CIF/BDD specification on which synthesis was performed.
     * @param disabledEvents The events that are disabled before synthesis.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param ctrlGuards The guards in the controlled system for the controllable events to check.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void checkOutputEdges(CifBddSpec cifBddSpec, Set<Event> disabledEvents,
            CifDataSynthesisResult synthResult, Map<Event, BDD> ctrlGuards, boolean dbgEnabled)
    {
        if (dbgEnabled) {
            cifBddSpec.settings.getDebugOutput().line();
            cifBddSpec.settings.getDebugOutput().line("Checking post-synthesis for events that are never enabled.");
        }

        // Determine the guards for the uncontrollable events.
        Set<Event> uncontrollables = Sets.difference(cifBddSpec.alphabet, cifBddSpec.controllables,
                cifBddSpec.inputVarEvents);
        Map<Event, BDD> unctrlGuards = determineGuards(cifBddSpec, uncontrollables, false);

        // Warn for controllable events never enabled in the controlled system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        warnEventsDisabled(cifBddSpec, disabledEvents, synthResult, ctrlGuards);

        // Warn for uncontrollable events never enabled in the controlled system.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        warnEventsDisabled(cifBddSpec, disabledEvents, synthResult, unctrlGuards);

        // Free no longer needed predicates.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        for (BDD bdd: unctrlGuards.values()) {
            bdd.free();
        }
    }

    /**
     * Warns for events that are disabled in the global controlled system.
     *
     * <p>
     * Does not report problems for events that were already disabled before synthesis.
     * </p>
     *
     * @param cifBddSpec The CIF/BDD specification on which synthesis was performed.
     * @param disabledEvents The events that are disabled before synthesis.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param guards The guards in the controlled system for the events.
     */
    private static void warnEventsDisabled(CifBddSpec cifBddSpec, Set<Event> disabledEvents,
            CifDataSynthesisResult synthResult, Map<Event, BDD> guards)
    {
        // Calculate controlled state space.
        BDD ctrlBehPlantInv = synthResult.ctrlBeh.and(cifBddSpec.plantInv);

        // Check all events.
        for (Event event: guards.keySet()) {
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }

            // Determine when the event is enabled in controlled statespace.
            BDD ctrlBehGuard = guards.get(event).and(ctrlBehPlantInv);

            // Warn for events that are never enabled.
            if (ctrlBehGuard.isZero() && !disabledEvents.contains(event)) {
                cifBddSpec.settings.getWarnOutput().line("Event \"%s\" is disabled in the controlled system.",
                        CifTextUtils.getAbsName(event));
                disabledEvents.add(event);
                continue;
            }
            ctrlBehGuard.free();
        }
        ctrlBehPlantInv.free();
    }

    /**
     * Determines the synthesis output, related to guards of controllable events, that is to end up in the resulting CIF
     * model.
     *
     * <p>
     * It takes the controlled system guards of the controllable events. It then applies simplification as requested.
     * </p>
     *
     * @param cifBddSpec The CIF/BDD specification on which synthesis was performed. Is modified in-place.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param ctrlGuards The guards in the controlled system for the controllable events.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void determineOutputGuards(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            Map<Event, BDD> ctrlGuards, boolean dbgEnabled)
    {
        // Get simplifications to perform.
        EnumSet<BddSimplify> simplifications = synthResult.settings.getBddSimplifications();
        List<String> assumptionTxts = list();

        // Initialize assumptions to 'true', for all controllable events.
        Map<Event, BDD> assumptions = mapc(cifBddSpec.controllables.size());
        for (Event controllable: cifBddSpec.controllables) {
            assumptions.put(controllable, cifBddSpec.factory.one());
        }

        // If requested, simplify output guards assuming the uncontrolled system guard. This results in the additional
        // restrictions introduced by the controller with respect to the plants (i.e. uncontrolled system), instead of
        // the full controlled system guard. Simplification is best effort.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_PLANTS)) {
            assumptionTxts.add("plants");

            // Compute the global uncontrolled system guards, for all controllable events.
            Map<Event, BDD> unctrlGuards = determineGuards(cifBddSpec, cifBddSpec.controllables, true);

            // Add guards to the assumptions.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }
            for (Event controllable: cifBddSpec.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = unctrlGuards.get(controllable);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }

        // If requested, simplify output guards assuming the state/event exclusion requirement invariants derived from
        // the requirement automata. This results in the additional restrictions introduced by the controller with
        // respect to those requirements, instead of the full controlled system guard. Simplification is best effort.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_REQ_AUTS)) {
            assumptionTxts.add("requirement automata");

            for (Event controllable: cifBddSpec.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = cifBddSpec.stateEvtExclsReqAuts.get(controllable);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }
        cifBddSpec.stateEvtExclsReqAuts = null;

        // If requested, simplify output guards assuming the state/event exclusion plant invariants from the input
        // specification. This results in the additional restrictions introduced by the controller with respect to these
        // plants, instead of the full controlled system guard. Simplification is best effort.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_SE_EXCL_PLANT_INVS)) {
            assumptionTxts.add("state/event exclusion plant invariants");

            for (Event controllable: cifBddSpec.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = cifBddSpec.stateEvtExclPlants.get(controllable);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }
        cifBddSpec.stateEvtExclPlants = null;

        // If requested, simplify output guards assuming the state/event exclusion requirement invariants from the input
        // specification. This results in the additional restrictions introduced by the controller with respect to those
        // requirements, instead of the full controlled system guard. Simplification is best effort.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_SE_EXCL_REQ_INVS)) {
            assumptionTxts.add("state/event exclusion requirement invariants");

            for (Event controllable: cifBddSpec.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = cifBddSpec.stateEvtExclsReqInvs.get(controllable);
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }
        cifBddSpec.stateEvtExclsReqInvs = null;

        // If requested, simplify output guards assuming the state plant invariants from the input specification.
        // This results in the additional restrictions introduced by the controller with respect to those plants,
        // instead of the full controlled system guard. Simplification is best effort.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_STATE_PLANT_INVS)) {
            assumptionTxts.add("state plant invariants");

            for (Event controllable: cifBddSpec.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = cifBddSpec.plantInv.id();
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }
        cifBddSpec.plantInv.free();
        cifBddSpec.plantInv = null;

        // If requested, simplify output guards assuming the state requirement invariants from the input specification.
        // This results in the additional restrictions introduced by the controller with respect to those requirements,
        // instead of the full controlled system guard. Simplification is best effort.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_STATE_REQ_INVS)) {
            assumptionTxts.add("state requirement invariants");

            for (Event controllable: cifBddSpec.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = cifBddSpec.reqInv.id();
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }
        cifBddSpec.reqInv.free();
        cifBddSpec.reqInv = null;

        // If requested, simplify output guards assuming the controlled behavior as computed by synthesis.
        // Initialization is restricted to ensure the system starts within the controlled behavior. Each guard ensures
        // the system remains in the controlled behavior. We may assume before a transition, we are in the controlled
        // behavior. We can thus simplify guards using this assumption. Simplification is best effort.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_CTRL_BEH)) {
            assumptionTxts.add("controlled behavior");

            for (Event controllable: cifBddSpec.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = synthResult.ctrlBeh.id();
                if (cifBddSpec.settings.getTermination().isRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }
        synthResult.ctrlBeh.free();
        synthResult.ctrlBeh = null;

        // Initialize output guards.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        synthResult.outputGuards = ctrlGuards;

        // If no assumptions, we are done.
        if (assumptionTxts.isEmpty()) {
            return;
        }

        // Perform the simplification using all the collected assumptions.
        if (cifBddSpec.settings.getTermination().isRequested()) {
            return;
        }
        String assumptionsTxt = combineAssumptionTexts(assumptionTxts);
        simplifyOutputGuards(cifBddSpec, synthResult, dbgEnabled, assumptions, assumptionsTxt);
    }

    /**
     * Combines assumption texts. Separates entries with commas, and puts an 'and' before the last entry. Only uses
     * commas if more than two items are present.
     *
     * @param texts The texts.
     * @return The combined text.
     */
    private static String combineAssumptionTexts(List<String> texts) {
        if (texts.size() == 0) {
            return "";
        }
        if (texts.size() == 1) {
            return texts.get(0);
        }

        StringBuilder txt = new StringBuilder();
        for (int i = 0; i < texts.size(); i++) {
            if (i > 0) {
                if (texts.size() > 2) {
                    txt.append(",");
                }
                txt.append(" ");
            }
            if (i == texts.size() - 1) {
                txt.append("and ");
            }
            txt.append(texts.get(i));
        }
        return txt.toString();
    }

    /**
     * Simplify the {@link CifDataSynthesisResult#outputGuards output guards}.
     *
     * @param cifBddSpec The CIF/BDD specification on which synthesis was performed.
     * @param synthResult The synthesis result. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     * @param assumptions Per controllable event, the assumption to use. All assumptions are {@link BDD#free freed}
     *     after use.
     * @param assumptionsTxt Text describing the assumptions that are used, for debugging output.
     */
    private static void simplifyOutputGuards(CifBddSpec cifBddSpec, CifDataSynthesisResult synthResult,
            boolean dbgEnabled, Map<Event, BDD> assumptions, String assumptionsTxt)
    {
        boolean dbgPrinted = false;
        for (Event controllable: cifBddSpec.controllables) {
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }

            // Get current guard and assumption.
            BDD guard = synthResult.outputGuards.get(controllable);
            BDD assumption = assumptions.get(controllable);

            // Simplify.
            BDD newGuard;
            if (assumption.isZero() && guard.isZero()) {
                // Special case for events that are assumed to be never enabled, the supervisor does not restrict them.
                newGuard = cifBddSpec.factory.one();
            } else {
                newGuard = guard.simplify(assumption);
            }
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }

            synthResult.outputGuards.put(controllable, newGuard);

            // If it had an effect, print some debug info.
            if (dbgEnabled && !guard.equals(newGuard)) {
                if (!dbgPrinted) {
                    dbgPrinted = true;
                    cifBddSpec.settings.getDebugOutput().line();
                    cifBddSpec.settings.getDebugOutput().line(
                            "Simplification of controlled system under the assumption of the %s:", assumptionsTxt);
                }
                cifBddSpec.settings.getDebugOutput().inc();
                cifBddSpec.settings.getDebugOutput().line("Event %s: guard: %s -> %s [assume %s].",
                        CifTextUtils.getAbsName(controllable), bddToStr(guard, cifBddSpec),
                        bddToStr(newGuard, cifBddSpec), bddToStr(assumption, cifBddSpec));
                cifBddSpec.settings.getDebugOutput().dec();
            }

            // Free no longer needed predicates.
            if (cifBddSpec.settings.getTermination().isRequested()) {
                return;
            }
            assumption.free();
            guard.free();
        }
    }
}
