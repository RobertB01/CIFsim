//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.datasynth.bdd.BddUtils.bddToStr;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.warn;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.datasynth.bdd.BddUtils;
import org.eclipse.escet.cif.datasynth.options.BddSimplify;
import org.eclipse.escet.cif.datasynth.options.BddSimplifyOption;
import org.eclipse.escet.cif.datasynth.options.ForwardReachOption;
import org.eclipse.escet.cif.datasynth.spec.SynthesisAutomaton;
import org.eclipse.escet.cif.datasynth.spec.SynthesisDiscVariable;
import org.eclipse.escet.cif.datasynth.spec.SynthesisEdge;
import org.eclipse.escet.cif.datasynth.spec.SynthesisVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

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
     * @param aut The automaton on which to perform synthesis. Is updated to represent the result of synthesis.
     * @param dbgEnabled Whether debug output is enabled.
     * @param doTiming Whether to collect timing statistics.
     * @param timing The timing statistics data. Is modified in-place.
     */
    public static void synthesize(SynthesisAutomaton aut, boolean dbgEnabled, boolean doTiming,
            CifDataSynthesisTiming timing)
    {
        // Algorithm is based on the following paper: Lucien Ouedraogo, Ratnesh Kumar, Robi Malik, and Knut Åkesson:
        // Nonblocking and Safe Control of Discrete-Event Systems Modeled as Extended Finite Automata, IEEE Transactions
        // on Automation Science and Engineering, Volume 8, Issue 3, Pages 560-569, July 2011.

        // Configuration.
        boolean doForward = ForwardReachOption.isEnabled();

        // Pre synthesis.
        if (doTiming) {
            timing.preSynth.start();
        }
        try {
            // Check uncontrolled system, and print debug information.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            checkUncontrolledSystem(aut, dbgEnabled);

            // Apply requirements.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            applyStateInvs(aut, dbgEnabled);

            if (aut.env.isTerminationRequested()) {
                return;
            }
            applyVarRanges(aut, dbgEnabled);

            if (aut.env.isTerminationRequested()) {
                return;
            }
            applyStateEvtExcls(aut, dbgEnabled);

            // Prepare for actual synthesis. Allow applying edges from here on.
            for (SynthesisEdge edge: aut.edges) {
                if (aut.env.isTerminationRequested()) {
                    return;
                }
                edge.initApply(doForward);
            }
        } finally {
            if (doTiming) {
                timing.preSynth.stop();
            }
        }

        if (aut.env.isTerminationRequested()) {
            return;
        }

        // Perform actual synthesis, using fixed point calculations.
        if (doTiming) {
            timing.main.start();
        }
        try {
            if (aut.env.isTerminationRequested()) {
                return;
            }
            synthesizeFixedPoints(aut, doForward, dbgEnabled, doTiming, timing);
        } finally {
            if (doTiming) {
                timing.main.stop();
            }
        }

        if (aut.env.isTerminationRequested()) {
            return;
        }

        // Post synthesis.
        if (doTiming) {
            timing.postSynth.start();
        }
        try {
            // Determine controlled system guards.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            determineCtrlSysGuards(aut, dbgEnabled);

            // Done with actual synthesis. May no longer apply edges from here on.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            for (SynthesisEdge edge: aut.edges) {
                edge.cleanupApply();
            }

            // Result of synthesis is in the automaton.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            if (dbgEnabled) {
                dbg();
                dbg("Final synthesis result:");
                dbg(aut.toString(1));
            }

            // Check whether initial state present.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            boolean emptySup = !checkInitStatePresent(aut, doForward, dbgEnabled);

            // Debug output: number of states in controlled system.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            printNumberStates(aut, emptySup, doForward, dbgEnabled);

            // Determine the output of synthesis (1/2).
            if (aut.env.isTerminationRequested()) {
                return;
            }
            determineOutputInitial(aut, dbgEnabled);

            // Fail if supervisor is empty.
            if (emptySup) {
                throw new InvalidInputException("Empty supervisor.");
            }

            // Determine the output of synthesis (2/2).
            if (aut.env.isTerminationRequested()) {
                return;
            }
            determineOutputGuards(aut, dbgEnabled);

            // Separate debug output from what is to come.
            if (dbgEnabled) {
                dbg();
            }
        } finally {
            if (doTiming) {
                timing.postSynth.stop();
            }
        }

        if (aut.env.isTerminationRequested()) {
            return;
        }
    }

    /**
     * Checks the uncontrolled system for problems with initialization, marking, and state invariants. Also prints
     * related debug information.
     *
     * @param aut The automaton on which to perform synthesis.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void checkUncontrolledSystem(SynthesisAutomaton aut, boolean dbgEnabled) {
        // Debug state invariants (predicates) of the components.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            for (BDD pred: aut.invsComps) {
                dbg("Invariant (component state invariant):   %s", bddToStr(pred, aut));
            }
            dbg("Invariant (components state invariant):  %s", bddToStr(aut.invComps, aut));
        }

        // Debug state invariants (predicates) of the locations of the automata.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: aut.invsLocs) {
                dbg("Invariant (location state invariant):    %s", bddToStr(pred, aut));
            }
            dbg("Invariant (locations state invariant):   %s", bddToStr(aut.invLocs, aut));
        }

        // Debug state invariant (predicate) of the system.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg("Invariant (system state invariant):      %s", bddToStr(aut.inv, aut));
        }

        // Warn if no state in system, due to state invariants.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (aut.inv.isZero()) {
            warn("The uncontrolled system has no states (taking into account only the state invariants).");
        }

        // Debug initialization predicates of the discrete variables.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            for (int i = 0; i < aut.variables.length; i++) {
                SynthesisVariable var = aut.variables[i];
                if (!(var instanceof SynthesisDiscVariable)) {
                    continue;
                }

                String nr = String.valueOf(i);
                dbg("Initial   (discrete variable %s):%s%s", nr, Strings.spaces(10 - nr.length()),
                        bddToStr(aut.initialsVars.get(i), aut));
            }

            dbg("Initial   (discrete variables):          %s", bddToStr(aut.initialVars, aut));
        }

        // Debug initialization predicates of the components.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: aut.initialsComps) {
                dbg("Initial   (component init predicate):    %s", bddToStr(pred, aut));
            }
            dbg("Initial   (components init predicate):   %s", bddToStr(aut.initialComps, aut));
        }

        // Debug initialization predicates of the locations of the automata.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: aut.initialsLocs) {
                dbg("Initial   (aut/locs init predicate):     %s", bddToStr(pred, aut));
            }
            dbg("Initial   (auts/locs init predicate):    %s", bddToStr(aut.initialLocs, aut));
        }

        // Debug initialization predicate of the uncontrolled system.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg("Initial   (uncontrolled system):         %s", bddToStr(aut.initialUnctrl, aut));
        }

        // Debug combined initialization and state invariants of the uncontrolled system.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg("Initial   (system, combined init/inv):   %s", bddToStr(aut.initialInv, aut));
        }

        // Warn if no initial state in uncontrolled system.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (aut.initialUnctrl.isZero()) {
            warn("The uncontrolled system has no initial state (taking into account only initialization).");
        }

        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (!aut.initialUnctrl.isZero() && !aut.inv.isZero() && aut.initialInv.isZero()) {
            warn("The uncontrolled system has no initial state (taking into account both initialization and state "
                    + "invariants).");
        }

        // Debug marker predicates of the components.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            for (BDD pred: aut.markedsComps) {
                dbg("Marked    (component marker predicate):  %s", bddToStr(pred, aut));
            }
            dbg("Marked    (components marker predicate): %s", bddToStr(aut.markedComps, aut));
        }

        // Debug marker predicates of the locations of the automata.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            for (BDD pred: aut.markedsLocs) {
                dbg("Marked    (aut/locs marker predicate):   %s", bddToStr(pred, aut));
            }
            dbg("Marked    (auts/locs marker predicate):  %s", bddToStr(aut.markedLocs, aut));
        }

        // Debug marker predicate of the uncontrolled system.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg("Marked    (uncontrolled system):         %s", bddToStr(aut.marked, aut));
        }

        // Debug combined initialization and marking of the uncontrolled system.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg("Marked    (system, combined marked/inv): %s", bddToStr(aut.markedInv, aut));
        }

        // Warn if no marked state in uncontrolled system.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (aut.marked.isZero()) {
            warn("The uncontrolled system has no marked state (taking into account only marking).");
        }

        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (!aut.marked.isZero() && !aut.inv.isZero() && aut.markedInv.isZero()) {
            warn("The uncontrolled system has no marked state (taking into "
                    + "account both marking and state invariants).");
        }

        // Debug state/event exclusions requirements.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            dbg("State/event exclusion requirements:");
            if (aut.stateEvtExclLists.isEmpty()) {
                dbg("  None");
            }
            for (Entry<Event, List<BDD>> entry: aut.stateEvtExclLists.entrySet()) {
                dbg("  Event \"%s\" needs:", CifTextUtils.getAbsName(entry.getKey()));
                for (BDD pred: entry.getValue()) {
                    dbg("    %s", bddToStr(pred, aut));
                }
            }
        }

        // Debug automaton.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            dbg("Uncontrolled system:");
            dbg(aut.toString(1));
        }

        // Free no longer needed predicates.
        for (BDD bdd: aut.invsComps) {
            bdd.free();
        }
        for (BDD bdd: aut.invsLocs) {
            bdd.free();
        }
        aut.invComps.free();
        aut.invLocs.free();

        for (BDD bdd: aut.initialsVars) {
            if (bdd != null) {
                bdd.free();
            }
        }
        for (BDD bdd: aut.initialsComps) {
            bdd.free();
        }
        for (BDD bdd: aut.initialsLocs) {
            bdd.free();
        }
        aut.initialVars.free();
        aut.initialComps.free();
        aut.initialLocs.free();
        aut.initialInv.free();

        for (BDD bdd: aut.markedsComps) {
            bdd.free();
        }
        for (BDD bdd: aut.markedsLocs) {
            bdd.free();
        }
        aut.markedComps.free();
        aut.markedLocs.free();
        aut.markedInv.free();

        for (List<BDD> preds: aut.stateEvtExclLists.values()) {
            for (BDD pred: preds) {
                pred.free();
            }
        }

        aut.invsComps = null;
        aut.invComps = null;
        aut.invsLocs = null;
        aut.invLocs = null;

        aut.initialsVars = null;
        aut.initialVars = null;
        aut.initialsComps = null;
        aut.initialComps = null;
        aut.initialsLocs = null;
        aut.initialLocs = null;
        aut.initialInv = null;

        aut.markedsComps = null;
        aut.markedComps = null;
        aut.markedsLocs = null;
        aut.markedLocs = null;
        aut.markedInv = null;

        aut.stateEvtExclLists = null;
    }

    /**
     * Initializes the controlled-behavior predicate, to the invariants, as preprocessing step for synthesis. The idea
     * is that a state is only in the controlled system if the state invariant holds.
     *
     * @param aut The automaton on which to perform synthesis. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void applyStateInvs(SynthesisAutomaton aut, boolean dbgEnabled) {
        if (aut.env.isTerminationRequested()) {
            return;
        }
        aut.ctrlBeh = aut.inv.id();

        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            dbg("Initialized controlled-behavior predicate using invariants: %s.", bddToStr(aut.ctrlBeh, aut));
        }
    }

    /**
     * Ensure that variables stay within their range, i.e. ensure no out of bounds values are ever used, as
     * preprocessing step for synthesis. This is done by enforcing that variables stay within their
     * {@link SynthesisVariable#domain domains}.
     *
     * <p>
     * As an example, consider a variable 'x' of type 'int[0..2]'. Two BDD variables are used to represent the integer
     * CIF variable. The two BDD variables can represent 2^2=4 values, i.e. they can represent values of type
     * 'int[0..3]'. Value '3' can be represented, but is not a valid value of the variable, and should thus not be used.
     * The predicate that represents the value range is {@code 0 <= x <= 2}. This predicate is added to the
     * controlled-behavior predicate.
     * </p>
     *
     * @param aut The automaton on which to perform synthesis. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void applyVarRanges(SynthesisAutomaton aut, boolean dbgEnabled) {
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            dbg("Extending controlled-behavior predicate using variable ranges.");
        }

        boolean firstDbg = true;
        boolean changed = false;
        for (SynthesisVariable var: aut.variables) {
            if (aut.env.isTerminationRequested()) {
                return;
            }

            // Compute out of range predicate.
            BDD range = BddUtils.getVarDomain(var, false, aut.factory);

            // Update controlled-behavior predicate.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            BDD newCtrlBeh = aut.ctrlBeh.and(range);
            if (aut.env.isTerminationRequested()) {
                return;
            }

            if (aut.ctrlBeh.equals(newCtrlBeh)) {
                newCtrlBeh.free();
                range.free();
            } else {
                if (aut.env.isTerminationRequested()) {
                    return;
                }
                if (dbgEnabled) {
                    if (firstDbg) {
                        firstDbg = false;
                        dbg();
                    }
                    dbg("Controlled behavior: %s -> %s [range: %s, variable: %s].", bddToStr(aut.ctrlBeh, aut),
                            bddToStr(newCtrlBeh, aut), bddToStr(range, aut), var.toString(0, ""));
                }
                range.free();
                aut.ctrlBeh.free();
                aut.ctrlBeh = newCtrlBeh;
                changed = true;
            }
        }

        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled && changed) {
            dbg();
            dbg("Extended controlled-behavior predicate using variable ranges: %s.", bddToStr(aut.ctrlBeh, aut));
        }
    }

    /**
     * Applies the state/event exclusion invariants, as preprocessing step for synthesis.
     *
     * @param aut The automaton on which to perform synthesis. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void applyStateEvtExcls(SynthesisAutomaton aut, boolean dbgEnabled) {
        // Update guards and controlled-behavior predicate, to ensure that transitions not allowed by the state/event
        // exclusion invariants, are blocked.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            dbg("Restricting behavior using state/event exclusion requirements.");
        }

        boolean firstDbg = true;
        boolean changed = false;
        boolean guardChanged = false;
        for (SynthesisEdge edge: aut.edges) {
            // Get additional condition for the edge. Skip if none.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            BDD req = aut.stateEvtExcls.get(edge.event);
            if (req == null) {
                continue;
            }

            // Enforce the additional condition.
            if (edge.event.getControllable()) {
                // For controllable events, we can simply restrict the guard.
                BDD newGuard = edge.guard.and(req);

                if (edge.guard.equals(newGuard)) {
                    newGuard.free();
                } else {
                    if (aut.env.isTerminationRequested()) {
                        return;
                    }
                    if (dbgEnabled) {
                        if (firstDbg) {
                            firstDbg = false;
                            dbg();
                        }
                        dbg("Edge %s: guard: %s -> %s [requirement: %s].", edge.toString(0, ""),
                                bddToStr(edge.guard, aut), bddToStr(newGuard, aut), bddToStr(req, aut));
                    }
                    edge.guard.free();
                    edge.guard = newGuard;
                    changed = true;
                    guardChanged = true;
                }
            } else {
                // For uncontrollable events, update the controlled-behavior predicate. If the guard of the edge holds
                // (event enabled in the plant), and the requirement condition doesn't hold (event disabled by the
                // requirements), the edge may not be taken.
                //
                // reqBad = guard && !req
                // reqGood = !(guard && !req) = !guard || req = guard => req
                //
                // Only good states in controlled behavior. So restrict controlled behavior with 'reqGood'.
                BDD reqGood = edge.guard.imp(req);
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                BDD newCtrlBeh = aut.ctrlBeh.id().andWith(reqGood);
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                if (aut.ctrlBeh.equals(newCtrlBeh)) {
                    newCtrlBeh.free();
                } else {
                    if (aut.env.isTerminationRequested()) {
                        return;
                    }
                    if (dbgEnabled) {
                        if (firstDbg) {
                            firstDbg = false;
                            dbg();
                        }
                        dbg("Controlled behavior: %s -> %s [requirement: %s, edge: %s].", bddToStr(aut.ctrlBeh, aut),
                                bddToStr(newCtrlBeh, aut), bddToStr(req, aut), edge.toString(0, ""));
                    }
                    aut.ctrlBeh.free();
                    aut.ctrlBeh = newCtrlBeh;
                    changed = true;
                }
            }
        }

        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled && changed) {
            dbg();
            dbg("Restricted behavior using state/event exclusion requirements:");
            dbg(aut.toString(1, guardChanged));
        }

        // Free no longer needed predicates.
        for (BDD bdd: aut.stateEvtExcls.values()) {
            bdd.free();
        }
        aut.stateEvtExcls = null;
    }

    /**
     * Performs the actual synthesis algorithm from the paper, calculating various fixed points.
     *
     * @param aut The automaton on which to perform synthesis. Is modified in-place.
     * @param doForward Whether to do forward reachability during synthesis.
     * @param dbgEnabled Whether debug output is enabled.
     * @param doTiming Whether to collect timing statistics.
     * @param timing The timing statistics data. Is modified in-place.
     */
    private static void synthesizeFixedPoints(SynthesisAutomaton aut, boolean doForward, boolean dbgEnabled,
            boolean doTiming, CifDataSynthesisTiming timing)
    {
        // We know that:
        // - Each round, we perform the same operations.
        // - Each operation is a fixed point calculation.
        // - All the operations take a controlled behavior and produce a potentially changed controlled behavior.
        // - All other data that is used is constant. For instance, marking and initialization predicates of the
        // original model are used, and they don't change during the main loop (this method). The used guards and
        // update relations also don't change during the main loop (this method), etc.
        //
        // This means that:
        // - If we have 'n' operations, and the previous 'n - 1' operations didn't change the controlled behavior,
        // then the controlled behavior was last changed by the next operation that we will perform.
        // - The controlled behavior came from that 'next' operation.
        // - It was not changed by all the other operations since the last iteration of the loop.
        // - The operation is a fixed point as all other operations.
        // - Putting the result of a fixed point into that same fixed point again means the input is also the output.
        // - Thus, the 'next' operation will not have an effect on the controlled behavior.
        // - Similarly, all next operations won't have an affect. We have thus reached a fixed point for this entire
        // main loop (this method), and are done with synthesis.
        //
        // Some additional things to consider:
        // - This applies to any 'n - 1' previous operations, regardless of whether the next operation is the first
        // operation of the loop, the last operation of the loop, or an operation in between.
        // - We do need to go through all the operations of the loop at least once.

        // Count the number of reachability operations in the loop;
        int reachabilityCount = 0;
        reachabilityCount++; // Backward reach of marking.
        reachabilityCount++; // Backward uncontrollable reach of bad states.
        if (doForward) { // Forward reach of initialization.
            reachabilityCount++;
        }

        // Get the number of reachability operations that need to be stable before we can stop synthesis.
        int stableCount = reachabilityCount - 1;

        // Perform synthesis.
        int round = 0;
        int unchanged = 0;
        while (true) {
            // Next round.
            round++;
            if (aut.env.isTerminationRequested()) {
                return;
            }

            if (dbgEnabled) {
                dbg();
                dbg("Round %d: started.", round);
            }

            // Compute non-blocking predicate from marking (fixed point).
            BDD nonBlock;
            if (doTiming) {
                timing.mainBwMarked.start();
            }
            try {
                nonBlock = reachability(aut.marked.id(), false, // bad
                        false, // forward
                        true, // ctrl
                        true, // unctrl
                        aut.ctrlBeh, aut, dbgEnabled, "backward controlled-behavior", "marker",
                        "current/previous controlled-behavior", round);
            } finally {
                if (doTiming) {
                    timing.mainBwMarked.stop();
                }
            }

            if (aut.env.isTerminationRequested()) {
                return;
            }

            // Detect change in controlled behavior.
            if (aut.ctrlBeh.equals(nonBlock)) {
                nonBlock.free();
                unchanged++;
            } else {
                if (dbgEnabled) {
                    dbg("Controlled behavior: %s -> %s.", bddToStr(aut.ctrlBeh, aut), bddToStr(nonBlock, aut));
                }
                aut.ctrlBeh.free();
                aut.ctrlBeh = nonBlock;
                unchanged = 0;
            }

            // Detect fixed point for main loop.
            if (aut.ctrlBeh.isZero()) {
                if (dbgEnabled) {
                    dbg();
                    dbg("Round %d: finished, all states are bad.", round);
                }
                break;
            }
            if (round > 1 && unchanged >= stableCount) {
                if (dbgEnabled) {
                    dbg();
                    dbg("Round %d: finished, controlled behavior is stable.", round);
                }
                break;
            }
            if (unchanged == 0) {
                BDD init = aut.initialUnctrl.and(aut.ctrlBeh);
                boolean noInit = init.isZero();
                init.free();
                if (noInit) {
                    if (dbgEnabled) {
                        dbg();
                        dbg("Round %d: finished, no initialization possible.", round);
                    }
                    break;
                }
            }
            if (aut.env.isTerminationRequested()) {
                return;
            }

            // Compute bad-state predicate from blocking (fixed point).
            BDD badState = aut.ctrlBeh.not();
            if (aut.env.isTerminationRequested()) {
                return;
            }

            if (doTiming) {
                timing.mainBwBadState.start();
            }
            try {
                badState = reachability(badState, true, // bad
                        false, // forward
                        false, // ctrl
                        true, // unctrl
                        null, aut, dbgEnabled, "backward uncontrolled bad-state",
                        "current/previous controlled behavior", null, round);
            } finally {
                if (doTiming) {
                    timing.mainBwBadState.stop();
                }
            }

            if (aut.env.isTerminationRequested()) {
                return;
            }

            BDD newCtrlBeh = badState.not();
            badState.free();
            if (aut.env.isTerminationRequested()) {
                return;
            }

            // Detect change in controlled behavior.
            if (aut.ctrlBeh.equals(newCtrlBeh)) {
                newCtrlBeh.free();
                unchanged++;
            } else {
                if (dbgEnabled) {
                    dbg("Controlled behavior: %s -> %s.", bddToStr(aut.ctrlBeh, aut), bddToStr(newCtrlBeh, aut));
                }
                aut.ctrlBeh.free();
                aut.ctrlBeh = newCtrlBeh;
                unchanged = 0;
            }

            // Optional forward reachability.
            if (doForward) {
                // Detect fixed point for main loop.
                if (aut.ctrlBeh.isZero()) {
                    if (dbgEnabled) {
                        dbg();
                        dbg("Round %d: finished, all states are bad.", round);
                    }
                    break;
                }
                if (round > 1 && unchanged >= stableCount) {
                    if (dbgEnabled) {
                        dbg();
                        dbg("Round %d: finished, controlled behavior is stable.", round);
                    }
                    break;
                }
                if (unchanged == 0) {
                    BDD init = aut.initialUnctrl.and(aut.ctrlBeh);
                    boolean noInit = init.isZero();
                    init.free();
                    if (noInit) {
                        if (dbgEnabled) {
                            dbg();
                            dbg("Round %d: finished, no initialization possible.", round);
                        }
                        break;
                    }
                }
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                // Compute controlled-behavior predicate from initialization of the uncontrolled system (fixed point).
                if (doTiming) {
                    timing.mainFwInit.start();
                }
                try {
                    newCtrlBeh = reachability(aut.initialUnctrl.id(), false, // bad
                            true, // forward
                            true, // ctrl
                            true, // unctrl
                            aut.ctrlBeh, aut, dbgEnabled, "forward controlled-behavior", "initialization",
                            "current/previous controlled-behavior", round);
                } finally {
                    if (doTiming) {
                        timing.mainFwInit.stop();
                    }
                }

                if (aut.env.isTerminationRequested()) {
                    return;
                }

                // Detect change in controlled behavior.
                if (aut.ctrlBeh.equals(newCtrlBeh)) {
                    newCtrlBeh.free();
                    unchanged++;
                } else {
                    if (dbgEnabled) {
                        dbg("Controlled behavior: %s -> %s.", bddToStr(aut.ctrlBeh, aut), bddToStr(newCtrlBeh, aut));
                    }
                    aut.ctrlBeh.free();
                    aut.ctrlBeh = newCtrlBeh;
                    unchanged = 0;
                }
            }

            // Detect fixed point for main loop.
            if (aut.ctrlBeh.isZero()) {
                if (dbgEnabled) {
                    dbg();
                    dbg("Round %d: finished, all states are bad.", round);
                }
                break;
            }
            if (unchanged >= stableCount) {
                if (dbgEnabled) {
                    dbg();
                    dbg("Round %d: finished, controlled behavior is stable.", round);
                }
                break;
            }
            if (!doForward && unchanged == 0) {
                BDD init = aut.initialUnctrl.and(aut.ctrlBeh);
                boolean noInit = init.isZero();
                init.free();
                if (noInit) {
                    if (dbgEnabled) {
                        dbg();
                        dbg("Round %d: finished, no initialization possible.", round);
                    }
                    break;
                }
            }
            if (aut.env.isTerminationRequested()) {
                return;
            }

            // Finished round.
            if (dbgEnabled) {
                dbg();
                dbg("Round %d: finished, need another round.", round);
            }
        }
    }

    /**
     * Performs forward or backward reachability until a fixed point is reached.
     *
     * @param pred The predicate to which to apply the reachability. This predicate is {@link BDD#free freed} by this
     *     method.
     * @param bad Whether the given predicate represents bad states ({@code true}) or good states ({@code false}).
     * @param forward Whether to apply forward reachability ({@code true}) or backward reachability ({@code false}).
     * @param ctrl Whether to include edges with controllable events in the reachability.
     * @param unctrl Whether to include edges with uncontrollable events in the reachability.
     * @param restriction The predicate that indicates the upper bound on the reached states. That is, during
     *     reachability no states may be reached outside these states. May be {@code null} to not impose a restriction,
     *     which is semantically equivalent to providing 'true'.
     * @param aut The synthesis automaton.
     * @param dbgEnabled Whether debug output is enabled.
     * @param predName The name of the given predicate, for debug output. Must be in lower case.
     * @param initName The name of the initial value of the given predicate, for debug output. Must be in lower case.
     * @param restrictionName The name of the restriction predicate, for debug output. Must be in lower case. Must be
     *     {@code null} if no restriction predicate is provided.
     * @param round The 1-based round number of the main synthesis algorithm, for debug output.
     * @return The fixed point result of the reachability computation, or {@code null} if the application is terminated.
     */
    private static BDD reachability(BDD pred, boolean bad, boolean forward, boolean ctrl, boolean unctrl,
            BDD restriction, SynthesisAutomaton aut, boolean dbgEnabled, String predName, String initName,
            String restrictionName, int round)
    {
        // Print debug output.
        if (dbgEnabled) {
            dbg();
            dbg("Round %d: computing %s predicate.", round, predName);
            dbg("%s: %s [%s predicate]", Strings.makeInitialUppercase(predName), bddToStr(pred, aut), initName);
        }

        // Initialization.
        boolean changed = false;

        // Restrict predicate.
        if (restriction != null) {
            BDD restrictedPred = pred.and(restriction);
            if (aut.env.isTerminationRequested()) {
                return null;
            }

            if (pred.equals(restrictedPred)) {
                restrictedPred.free();
            } else {
                if (aut.env.isTerminationRequested()) {
                    return null;
                }
                if (dbgEnabled) {
                    Assert.notNull(restrictionName);
                    dbg("%s: %s -> %s [restricted to %s predicate: %s]", Strings.makeInitialUppercase(predName),
                            bddToStr(pred, aut), bddToStr(restrictedPred, aut), restrictionName,
                            bddToStr(restriction, aut));
                }
                pred.free();
                pred = restrictedPred;
                changed = true;
            }
        }

        // Prepare edges for being applied.
        for (SynthesisEdge edge: aut.edges) {
            edge.preApply(forward, restriction);
        }

        // Compute fixed point.
        int iter = 0;
        while (true) {
            // Print iteration, for debugging.
            iter++;
            if (dbgEnabled) {
                dbg("%s reachability: iteration %d.", (forward ? "Forward" : "Backward"), iter);
            }

            // Store current/old predicate, to detect fixed point later on.
            if (aut.env.isTerminationRequested()) {
                return null;
            }
            BDD oldPred = pred.id();

            // Push through all edges.
            for (SynthesisEdge edge: aut.edges) {
                // Skip edges if requested.
                if (!ctrl && edge.event.getControllable()) {
                    continue;
                }
                if (!unctrl && !edge.event.getControllable()) {
                    continue;
                }
                if (aut.env.isTerminationRequested()) {
                    return null;
                }

                // Apply edge.
                BDD updPred = pred.id();
                updPred = edge.apply(updPred, bad, forward, restriction);
                if (aut.env.isTerminationRequested()) {
                    return null;
                }

                // Extend reachable states.
                BDD newPred = pred.id().orWith(updPred);
                if (aut.env.isTerminationRequested()) {
                    return null;
                }

                // Detect change.
                if (pred.equals(newPred)) {
                    newPred.free();
                    continue;
                } else {
                    if (aut.env.isTerminationRequested()) {
                        return null;
                    }
                    if (dbgEnabled) {
                        String restrTxt;
                        if (restriction == null) {
                            restrTxt = "";
                        } else {
                            Assert.notNull(restrictionName);
                            restrTxt = fmt(", restricted to %s predicate: %s", restrictionName,
                                    bddToStr(restriction, aut));
                        }
                        dbg("%s: %s -> %s [%s reach with edge: %s%s]", Strings.makeInitialUppercase(predName),
                                bddToStr(pred, aut), bddToStr(newPred, aut), (forward ? "forward" : "backward"),
                                edge.toString(0, ""), restrTxt);
                    }
                    pred.free();
                    pred = newPred;
                    changed = true;
                }
            }

            // Detect fixed point.
            boolean fixedPoint = pred.equals(oldPred);
            oldPred.free();
            if (fixedPoint) {
                break;
            }
        }

        // Cleanup edges for being applied.
        for (SynthesisEdge edge: aut.edges) {
            edge.postApply(forward);
        }

        // Fixed point reached.
        if (aut.env.isTerminationRequested()) {
            return null;
        }
        if (dbgEnabled && changed) {
            dbg("%s: %s [fixed point].", Strings.makeInitialUppercase(predName), bddToStr(pred, aut));
        }
        return pred;
    }

    /**
     * Determines the guards of the controlled system, for each linearized edge.
     *
     * @param aut The synthesis result.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void determineCtrlSysGuards(SynthesisAutomaton aut, boolean dbgEnabled) {
        // Compute guards of edges with controllable events.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            dbg("Computing controlled system guards.");
        }

        boolean guardUpdated = false;
        for (SynthesisEdge edge: aut.edges) {
            if (!edge.event.getControllable()) {
                continue;
            }
            if (aut.env.isTerminationRequested()) {
                return;
            }

            BDD updPred = aut.ctrlBeh.id();
            edge.preApply(false, null);
            updPred = edge.apply(updPred, false, false, null);
            edge.postApply(false);
            edge.cleanupApply();
            if (aut.env.isTerminationRequested()) {
                return;
            }

            BDD newGuard = edge.guard.id().andWith(updPred);
            if (aut.env.isTerminationRequested()) {
                return;
            }

            if (edge.guard.equals(newGuard)) {
                newGuard.free();
            } else {
                if (aut.env.isTerminationRequested()) {
                    return;
                }
                if (dbgEnabled) {
                    if (!guardUpdated) {
                        dbg();
                    }
                    dbg("Edge %s: guard: %s -> %s.", edge.toString(0, ""), bddToStr(edge.guard, aut),
                            bddToStr(newGuard, aut));
                }
                edge.guard.free();
                edge.guard = newGuard;
                guardUpdated = true;
            }
        }
    }

    /**
     * Checks the synthesis result, to see whether an initial state is still present. Also determines the initialization
     * predicate of the controlled system ({@link SynthesisAutomaton#initialCtrl}).
     *
     * @param aut The synthesis result.
     * @param doForward Whether to do forward reachability during synthesis.
     * @param dbgEnabled Whether debug output is enabled.
     * @return Whether an initial state exists in the controlled system ({@code true}) or the supervisor is empty
     *     ({@code false}).
     */
    private static boolean checkInitStatePresent(SynthesisAutomaton aut, boolean doForward, boolean dbgEnabled) {
        // Get initialization predicate for controlled system.
        BDD initialUnctrl = aut.initialUnctrl;
        aut.initialCtrl = initialUnctrl.and(aut.ctrlBeh);
        if (aut.env.isTerminationRequested()) {
            return true;
        }

        // Check for empty supervisor (no initial state).
        boolean emptySup = aut.initialCtrl.isZero();
        return !emptySup;
    }

    /**
     * Prints the number of states in the controlled system, as debug output.
     *
     * @param aut The synthesis automaton.
     * @param emptySup whether the supervisor is empty.
     * @param doForward Whether to do forward reachability during synthesis.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void printNumberStates(SynthesisAutomaton aut, boolean emptySup, boolean doForward,
            boolean dbgEnabled)
    {
        // Only print debug output if enabled.
        if (!dbgEnabled) {
            return;
        }

        // Get number of states in controlled system.
        double nr;
        if (emptySup) {
            nr = 0;
        } else if (aut.variables.length == 0) {
            Assert.check(aut.ctrlBeh.isZero() || aut.ctrlBeh.isOne());
            nr = aut.ctrlBeh.isOne() ? 1 : 0;
        } else {
            nr = aut.ctrlBeh.satCount(aut.varSetOld);
        }
        Assert.check(emptySup || nr > 0);

        // Print debug output.
        boolean isExact = emptySup || doForward;
        dbg();
        dbg("Controlled system:                     %s %,.0f state%s.", (isExact ? "exactly" : "at most"), nr,
                nr == 1 ? "" : "s");
    }

    /**
     * Determines the synthesis output, related to initialization, that is to end up in the resulting CIF model.
     *
     * @param aut The synthesis result.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void determineOutputInitial(SynthesisAutomaton aut, boolean dbgEnabled) {
        // Print some debug output.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg();
            dbg("Initial (synthesis result):            %s", bddToStr(aut.ctrlBeh, aut));
            dbg("Initial (uncontrolled system):         %s", bddToStr(aut.initialUnctrl, aut));
            dbg("Initial (controlled system):           %s", bddToStr(aut.initialCtrl, aut));
        }

        // Initialization and options.
        EnumSet<BddSimplify> simplifications = BddSimplifyOption.getSimplifications();
        boolean dbgPrinted = false;

        // What initialization was allowed in the uncontrolled system, but is no longer allowed in the controlled
        // system, as thus has been removed as allowed initialization? The inverse of that is what the supervisor adds
        // as additional initialization restriction on top of the uncontrolled system.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        BDD initialUnctrl = aut.initialUnctrl;
        BDD initialCtrl = aut.initialCtrl;

        BDD initialRemoved = initialUnctrl.id().andWith(initialCtrl.not());
        if (aut.env.isTerminationRequested()) {
            return;
        }

        BDD initialAdded = initialRemoved.not();

        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (dbgEnabled) {
            dbg("Initial (removed by supervisor):       %s", bddToStr(initialRemoved, aut));
            dbg("Initial (added by supervisor):         %s", bddToStr(initialAdded, aut));
        }

        // Determine initialization predicate. The initialization predicate of the controlled system is used, if it is
        // at all restricted with respect to the uncontrolled system.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        aut.initialOutput = null;
        if (!initialRemoved.isZero()) {
            aut.initialOutput = initialCtrl.id();

            // If requested, the controlled system initialization predicate is simplified under the assumption of the
            // uncontrolled system initialization predicate, to obtain the additional initialization restrictions
            // introduced by the controller.
            if (simplifications.contains(BddSimplify.INITIAL_UNCTRL)) {
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                BDD newInitial = aut.initialOutput.simplify(initialUnctrl);
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                if (dbgEnabled && !aut.initialOutput.equals(newInitial)) {
                    if (!dbgPrinted) {
                        dbgPrinted = true;
                        dbg();
                        dbg("Simplification of controlled system under the assumption of the uncontrolled system:");
                    }
                    dbg("  Initial: %s -> %s [assume %s].", bddToStr(aut.initialOutput, aut), bddToStr(newInitial, aut),
                            bddToStr(initialUnctrl, aut));
                }
                aut.initialOutput.free();
                aut.initialOutput = newInitial;
            }
        }

        if (aut.env.isTerminationRequested()) {
            return;
        }
        initialCtrl.free();
        initialUnctrl.free();
        initialRemoved.free();
        initialAdded.free();
    }

    /**
     * Determines the synthesis output, related to guards of controllable events, that is to end up in the resulting CIF
     * model.
     *
     * <p>
     * It takes the controlled system guards of the linearized edges with controllable events, and merges them for each
     * event. It then applies simplification as requested.
     * </p>
     *
     * @param aut The synthesis result.
     * @param dbgEnabled Whether debug output is enabled.
     */
    private static void determineOutputGuards(SynthesisAutomaton aut, boolean dbgEnabled) {
        // Initialize global controlled system guards to 'false', for all controllable events.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        Map<Event, BDD> ctrlGuards = mapc(aut.controllables.size());
        for (Event controllable: aut.controllables) {
            ctrlGuards.put(controllable, aut.factory.zero());
        }

        // Compute global controlled system guards, for all controllable events. This is done by combining the guards of
        // all edges, per event.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        for (SynthesisEdge synthEdge: aut.edges) {
            // Skip edges with uncontrollable events, as those events are not in the alphabet (the supervisor can't
            // restrict them).
            if (!synthEdge.event.getControllable()) {
                continue;
            }
            if (aut.env.isTerminationRequested()) {
                return;
            }

            // Get current guards.
            BDD ctrl = ctrlGuards.get(synthEdge.event);

            // Update guards. Frees the guards of the edge.
            ctrl = ctrl.orWith(synthEdge.guard);
            if (aut.env.isTerminationRequested()) {
                return;
            }

            // Store updated guard.
            ctrlGuards.put(synthEdge.event, ctrl);
        }

        // Get simplifications to perform.
        EnumSet<BddSimplify> simplifications = BddSimplifyOption.getSimplifications();
        List<String> assumptionTxts = list();

        // Initialize assumptions to 'true', for all controllable events.
        Map<Event, BDD> assumptions = mapc(aut.controllables.size());
        for (Event controllable: aut.controllables) {
            assumptions.put(controllable, aut.factory.one());
        }

        // If requested, simplify output guards assuming the uncontrolled system guard. This results in the additional
        // restrictions introduced by the controller with respect to the plants (i.e. uncontrolled system), instead of
        // the full controlled system guard. Simplification is best effort.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_PLANTS)) {
            assumptionTxts.add("plants");

            // Initialize global uncontrolled system guards to 'false', for all controllable events.
            Map<Event, BDD> unctrlGuards = mapc(aut.controllables.size());
            for (Event controllable: aut.controllables) {
                unctrlGuards.put(controllable, aut.factory.zero());
            }

            // Compute global uncontrolled system guards, for all controllable events. This is done by combining the
            // guards of all edges, per event.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            for (SynthesisEdge synthEdge: aut.edges) {
                // Skip edges with uncontrollable events, as those events are not in the alphabet (the supervisor can't
                // restrict them).
                if (!synthEdge.event.getControllable()) {
                    continue;
                }
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                // Get current guards.
                BDD unctrl = unctrlGuards.get(synthEdge.event);

                // Update guards. Frees the guards of the edge.
                unctrl = unctrl.orWith(synthEdge.origGuard);
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                // Store updated guard.
                unctrlGuards.put(synthEdge.event, unctrl);
            }

            // Add guards to the assumptions.
            for (Event controllable: aut.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = unctrlGuards.get(controllable);
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }

        // If requested, simplify output guards assuming the state/event exclusion requirement invariants derived from
        // the requirement automata. This results in the additional restrictions introduced by the controller with
        // respect to those requirements, instead of the full controlled system guard. Simplification is best effort.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_REQ_AUTS)) {
            assumptionTxts.add("requirement automata");

            for (Event controllable: aut.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = aut.stateEvtExclsReqAuts.get(controllable);
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }
        aut.stateEvtExclsReqAuts = null;

        // If requested, simplify output guards assuming the state/event exclusion requirement invariants from the input
        // specification. This results in the additional restrictions introduced by the controller with respect to those
        // requirements, instead of the full controlled system guard. Simplification is best effort.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_SE_EXCL_REQ_INVS)) {
            assumptionTxts.add("state/event exclusion requirement invariants");

            for (Event controllable: aut.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = aut.stateEvtExclsReqInvs.get(controllable);
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }
        aut.stateEvtExclsReqInvs = null;

        // If requested, simplify output guards assuming the state requirement invariants from the input specification.
        // This results in the additional restrictions introduced by the controller with respect to those requirements,
        // instead of the full controlled system guard. Simplification is best effort.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_STATE_REQ_INVS)) {
            assumptionTxts.add("state requirement invariants");

            for (Event controllable: aut.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = aut.inv.id();
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }
        aut.inv.free();
        aut.inv = null;

        // If requested, simplify output guards assuming the controlled behavior as computed by synthesis.
        // Initialization is restricted to ensure the system starts within the controlled behavior. Each guard ensures
        // the system remains in the controlled behavior. We may assume before a transition, we are in the controlled
        // behavior. We can thus simplify guards using this assumption. Simplification is best effort.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        if (simplifications.contains(BddSimplify.GUARDS_CTRL_BEH)) {
            assumptionTxts.add("controlled behavior");

            for (Event controllable: aut.controllables) {
                BDD assumption = assumptions.get(controllable);
                BDD extra = aut.ctrlBeh.id();
                if (aut.env.isTerminationRequested()) {
                    return;
                }

                assumption = assumption.andWith(extra);
                assumptions.put(controllable, assumption);
            }
        }

        // Initialize output guards.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        aut.outputGuards = ctrlGuards;

        // If no assumptions, we are done.
        if (assumptionTxts.isEmpty()) {
            return;
        }

        // Perform the simplification using all the collected assumptions.
        if (aut.env.isTerminationRequested()) {
            return;
        }
        String assumptionsTxt = combineAssumptionTexts(assumptionTxts);
        simplifyOutputGuards(aut, dbgEnabled, assumptions, assumptionsTxt);
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
     * Simplify the {@link SynthesisAutomaton#outputGuards output guards}.
     *
     * @param aut The synthesis result. Is modified in-place.
     * @param dbgEnabled Whether debug output is enabled.
     * @param assumptions Per controllable event, the assumption to use. All assumptions are {@link BDD#free freed}
     *     after use.
     * @param assumptionsTxt Text describing the assumptions that are used, for debugging output.
     */
    private static void simplifyOutputGuards(SynthesisAutomaton aut, boolean dbgEnabled, Map<Event, BDD> assumptions,
            String assumptionsTxt)
    {
        boolean dbgPrinted = false;
        for (Event controllable: aut.controllables) {
            if (aut.env.isTerminationRequested()) {
                return;
            }

            // Get current guard and assumption.
            BDD guard = aut.outputGuards.get(controllable);
            BDD assumption = assumptions.get(controllable);

            // Simplify.
            BDD newGuard = guard.simplify(assumption);
            if (aut.env.isTerminationRequested()) {
                return;
            }

            aut.outputGuards.put(controllable, newGuard);

            // If it had an effect, print some debug info.
            if (dbgEnabled && !guard.equals(newGuard)) {
                if (!dbgPrinted) {
                    dbgPrinted = true;
                    dbg();
                    dbg("Simplification of controlled system under the assumption of the %s:", assumptionsTxt);
                }
                dbg("  Event %s: guard: %s -> %s [assume %s].", CifTextUtils.getAbsName(controllable),
                        bddToStr(guard, aut), bddToStr(newGuard, aut), bddToStr(assumption, aut));
            }

            // Free no longer needed predicates.
            if (aut.env.isTerminationRequested()) {
                return;
            }
            assumption.free();
            guard.free();
        }
    }
}
