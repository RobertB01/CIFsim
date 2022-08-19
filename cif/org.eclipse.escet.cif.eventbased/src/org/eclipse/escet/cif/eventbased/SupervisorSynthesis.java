//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased;

import static org.eclipse.escet.cif.eventbased.automata.AutomatonHelper.findDisjunctGroups;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.cif.eventbased.analysis.SynthesisDumpInterface;
import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
import org.eclipse.escet.cif.eventbased.automata.AutomatonKind;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Event;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.builders.AutomatonBuilder;
import org.eclipse.escet.cif.eventbased.builders.State;
import org.eclipse.escet.cif.eventbased.builders.StateEdges;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidModelException;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Strings;

/**
 * Event-based supervisor synthesis.
 *
 * <p>
 * Compute synchronous product of plant and requirements. Remove all states from the product that are not co-reachable,
 * or where the plant can perform an uncontrollable event, but the product cannot (due to not being allowed by the
 * requirements or because it leads to a non-existing state). Removing a state may thus trigger removal of (some of) its
 * predecessor states.
 * </p>
 *
 * <p>
 * The implementation performs three phases. The first phase is computing the product, where a location that disables an
 * uncontrollable event due to a requirement is marked as bad state.
 * </p>
 *
 * <p>
 * The second phase is iterative. The first step in each iteration starts with removing the known bad states.
 * Recursively, it also removes their predecessors if they were connected through an edge with an uncontrollable event,
 * or if they were non-marked, and have no down-stream edges after removal of the bad states. The second step in each
 * iteration is finding the non-coreachable states (after deletion). If there are any such states, the phase performs
 * another iteration.
 * </p>
 *
 * <p>
 * If the automaton is not empty after the second phase (which implies the initial location is coreachable), the
 * non-reachable locations are computed and removed in the third phase. This does not cause disabling of outgoing edges
 * with uncontrollable events from reachable locations, that is, no new bad states are created here.
 * </p>
 */
public class SupervisorSynthesis {
    /** Constructor of the {@link SupervisorSynthesis} class. */
    private SupervisorSynthesis() {
        // Static class.
    }

    /**
     * Perform checks whether the synthesis can be performed.
     *
     * <p>
     * Note that some checks produce warning messages rather than rejecting the input. Such checks are technically not a
     * requirement for performing synthesis. but if such a check fails (and produces a warning), the synthesis result is
     * unlikely to be useful.
     * </p>
     *
     * @param automs Automata to use for synthesis.
     * @param warnDisjunct Whether to emit warnings when disjunct groups of automata are found.
     * @param warnEmpty Whether to emit warnings when automata with an empty alphabet are found.
     * @param warnDeadlock Whether to emit warnings when marked deadlock locations are found.
     * @param warnSingleUse Whether to emit warnings when controllable events are used in exactly one automaton.
     */
    public static void synthesisPreCheck(List<Automaton> automs, boolean warnDisjunct, boolean warnEmpty,
            boolean warnDeadlock, boolean warnSingleUse)
    {
        List<Automaton> plants = list();
        List<Automaton> reqs = list();
        boolean unmarked = false;
        int warnCount = 0; // Number of printed warnings.

        for (Automaton aut: automs) {
            switch (aut.kind) {
                case PLANT:
                    plants.add(aut);
                    break;

                case REQUIREMENT:
                    reqs.add(aut);
                    break;

                default: {
                    String msg = fmt("Unsupported automaton \"%s\": automaton is neither a plant nor a requirement.",
                            aut.name);
                    throw new InvalidInputException(msg);
                }
            }

            // Check that the automaton is deterministic.
            AutomatonHelper.reportNonDeterministic(aut);

            // Warn for lack of marked locations.
            boolean autMarked = aut.hasMarkedLoc();
            if (!autMarked) {
                String msg = "Automaton \"" + aut.name + "\" has no marked location, supervisor will be empty.";
                OutputProvider.warn(msg);
                warnCount++;
                unmarked = true;
            }

            // Warn for non-trim.
            if (autMarked && !AutomatonHelper.trimCheck(aut)) {
                String msg = "Automaton \"" + aut.name + "\" is not trim.";
                OutputProvider.warn(msg);
                warnCount++;
            }
        }

        if (plants.isEmpty()) {
            String msg = "Supervisor synthesis needs at least one plant automaton.";
            throw new InvalidInputException(msg);
        }
        if (reqs.isEmpty()) {
            String msg = "Supervisor synthesis needs at least one requirement automaton.";
            throw new InvalidInputException(msg);
        }
        if (unmarked) {
            String msg = "Supervisor is empty (no marker states).";
            throw new InvalidModelException(msg);
        }

        // Check plant vs requirement alphabet.
        Set<Event> alphabet = set();
        for (Automaton req: reqs) {
            alphabet.addAll(req.alphabet);
        }
        for (Automaton plant: plants) {
            alphabet.removeAll(plant.alphabet);
        }
        if (!alphabet.isEmpty()) {
            List<String> eventNames = listc(alphabet.size());
            for (Event event: alphabet) {
                eventNames.add("\"" + event.name + "\"");
            }
            String msg = fmt(
                    "Event%s %s %s in the alphabet of the requirements, but not in the alphabet of the plants.",
                    (alphabet.size() == 1) ? "" : "s", String.join(", ", eventNames),
                    (alphabet.size() == 1) ? "is" : "are");
            throw new InvalidModelException(msg);
        }

        // Extra non-fatal checks for things that are probably not right.

        if (warnEmpty || warnDeadlock) {
            for (Automaton aut: automs) {
                // Automaton should have a non-empty alphabet.
                if (aut.alphabet.isEmpty()) {
                    if (warnEmpty) {
                        String msg = fmt("The alphabet of automaton \"%s\" is empty.", aut.name);
                        OutputProvider.warn(msg);
                        warnCount++;
                    }
                }

                if (warnDeadlock) {
                    // Non-marked deadlock locations have been warned above as
                    // non-trim, perhaps there are marked deadlock locations?
                    for (Location loc: aut) {
                        if (!loc.marked) {
                            continue;
                        }
                        if (loc.outgoingEdges != null) {
                            continue;
                        }

                        String msg = loc.toString() + " is a marked deadlock location.";
                        msg = StringUtils.capitalize(msg);
                        OutputProvider.warn(msg);
                        warnCount++;
                    }
                }
            }
        }

        // Find and report controllable events that are used in only one automaton.
        if (warnSingleUse) {
            Map<Event, Automaton> usesEvent = map(); // For every event, an automaton that uses it.
            Set<Event> sharedEvents = set();
            for (Automaton aut: automs) {
                for (Event evt: aut.alphabet) {
                    if (!evt.isControllable()) {
                        continue; // Ignore uncontrollable events.
                    }
                    Automaton unique = usesEvent.get(evt);
                    if (unique == null) {
                        usesEvent.put(evt, aut); // New event.
                    } else {
                        sharedEvents.add(evt); // Event seen before.
                    }
                }
            }

            for (Entry<Event, Automaton> entry: usesEvent.entrySet()) {
                Event evt = entry.getKey();
                if (sharedEvents.contains(evt)) {
                    continue;
                }

                String msg = fmt("Controllable event \"%s\" is only used by automaton \"%s\".", evt.name,
                        entry.getValue().name);
                OutputProvider.warn(msg);
                warnCount++;
            }
        }

        if (warnDisjunct) {
            // Try to find a group of automata with a disjunct union of their
            // alphabet with respect to the union of the alphabets of the other
            // automata. Such a disjunct sub-set should not exist, as it means
            // the system in fact consists of two or more independent parts.
            List<List<Automaton>> groups = findDisjunctGroups(automs);
            if (groups.size() > 1) {
                List<String> mesg = listc(groups.size() + 1);
                String msg = fmt("Found %d disjunct groups of automata that do not share events between the groups:",
                        groups.size());
                mesg.add(msg);
                int number = 1;
                for (List<Automaton> group: groups) {
                    mesg.add(constructGroupLine(number, group));
                    number++;
                }
                OutputProvider.warn(String.join("\n", mesg));
                warnCount++;
            }
        }

        // Output number of reported warnings as an easy check whether a
        // new warning appeared (warnings generated by the type checker are
        // not counted).
        if (warnCount == 1) {
            OutputProvider.out("Reported 1 synthesis warning.");
        } else if (warnCount > 0) {
            OutputProvider.out(fmt("Reported %d synthesis warnings.", warnCount));
        }
    }

    /**
     * Generate a line of warning text describing a group of disjunct automata.
     *
     * @param number Number of the group.
     * @param group Automata in this group.
     * @return Produced line of text.
     */
    private static String constructGroupLine(int number, List<Automaton> group) {
        List<String> names = listc(group.size());
        for (Automaton aut: group) {
            names.add(fmt("\"%s\"", aut.name));
        }

        Collections.sort(names, Strings.SORTER);
        String line = (names.size() == 1) ? "automaton" : "automata";
        line = fmt(" - Group %d consists of %s %s.", number, line, String.join(", ", names));
        return line;
    }

    /**
     * Perform supervisor synthesis on the provided automata.
     *
     * @param automs Automata to combine for supervisor synthesis.
     * @param synDump Object dumping information about the performed synthesis.
     * @return Supervisor automaton.
     */
    public static Automaton synthesis(List<Automaton> automs, SynthesisDumpInterface synDump) {
        List<Automaton> plants = list();
        List<Automaton> requirements = list();
        for (Automaton aut: automs) {
            switch (aut.kind) {
                case PLANT:
                    plants.add(aut);
                    break;

                case REQUIREMENT:
                    requirements.add(aut);
                    break;

                default:
                    throw new AssertionError("Unexpected automaton kind.");
            }
        }
        ArrayDeque<Location> badStates = new ArrayDeque<>(1000);
        Automaton aut;

        OutputProvider.dbg("Starting synthesis...");
        aut = makeProductWithBadStates(plants, requirements, badStates, synDump);

        if (OutputProvider.dodbg()) {
            OutputProvider.dbg("Pruning non-coreachables (%s)...", AutomatonHelper.getAutStatistics(aut));
        }
        pruneNonCoreachables(aut, badStates, synDump);

        if (OutputProvider.dodbg()) {
            OutputProvider.dbg("Pruning non-reachables (%s)...", AutomatonHelper.getAutStatistics(aut));
        }
        AutomatonHelper.removeNonReachables(aut, synDump);

        if (OutputProvider.dodbg()) {
            OutputProvider.dbg("Synthesis finished (%s).", AutomatonHelper.getAutStatistics(aut));
        }

        aut.kind = AutomatonKind.SUPERVISOR;
        return aut;
    }

    /**
     * Compute the synchronous product of the automata (plants and requirements). Bad states are saved in 'badStates',
     * left in the product, but not expanded further to reduce the number of states to cleanup later.
     *
     * @param plants Plant automata.
     * @param reqs Requirement automata.
     * @param badStates Filled with states to delete from the product during the call, with a deterministic order of
     *     deletion.
     * @param synDump Object dumping information about the performed synthesis.
     * @return The constructed automaton.
     */
    private static Automaton makeProductWithBadStates(List<Automaton> plants, List<Automaton> reqs,
            ArrayDeque<Location> badStates, SynthesisDumpInterface synDump)
    {
        int numPlants = plants.size();
        plants.addAll(reqs);
        synDump.storeAutomata(plants, numPlants);

        badStates.clear(); // Not really needed, but safe.

        // Plants at [0..numPlants-1], requirements at [numPlants..numAuts-1].
        AutomatonBuilder builder = new AutomatonBuilder(plants);

        expandStates:
        for (State srcState: builder) {
            // First iteration, test whether an uncontrollable event is blocked
            // by a requirement.
            Location srcLoc = builder.getLocation(srcState);
            synDump.newLocation(srcState, srcLoc);
            // TODO: If we do the uncontrollable events first, this loop can stop earlier.
            builder.edgeBuilder.setupStateEdges(srcState);
            for (StateEdges resEdges: builder.edgeBuilder.getStateEdges()) {
                int disabledIndex = resEdges.disabledIndex();
                if (disabledIndex >= 0) {
                    synDump.disabledEvent(srcLoc, resEdges.event, disabledIndex);
                }
                if (disabledIndex >= numPlants) {
                    if (!resEdges.event.isControllable()) {
                        // Uncontrollable event disabled by a requirement!
                        // State should be removed.
                        badStates.add(srcLoc);
                        continue expandStates; // Done with this state.
                    }
                }
            }
            // All fine, construct the new edges.
            for (StateEdges resEdges: builder.edgeBuilder.getStateEdges()) {
                // TODO: A loop is somewhat overkill for a single iteration.
                for (State destState: resEdges) {
                    Location destLoc = builder.getLocation(destState);
                    Edge.addEdge(resEdges.event, srcLoc, destLoc);
                }
            }
        }

        // Write edges to the dump file. Doing it as a second phase ensures that
        // all locations have been written already.
        if (!synDump.isFake()) {
            for (Location loc: builder.destAut) {
                for (Edge edge: loc.getOutgoing()) {
                    synDump.newEdge(edge.event, edge.srcLoc, edge.dstLoc);
                }
            }
        }

        return builder.destAut;
    }

    /**
     * Iteratively remove non-coreachable states.
     *
     * <p>
     * TODO: Location removal in an automaton is currently quite expensive (it constructs maps with neighboring
     * locations and edges). Performance of this phase can be improved by making location removal faster.
     * </p>
     *
     * @param aut Automaton to prune.
     * @param toDelete Bad locations in 'aut', found while computing the product. These location need to be removed.
     * @param synDump Object dumping information about the performed synthesis.
     */
    private static void pruneNonCoreachables(Automaton aut, Queue<Location> toDelete, SynthesisDumpInterface synDump) {
        if (aut.isEmpty()) {
            return;
        }

        // Same locations as toDelete, but faster in checking membership.
        Set<Location> badStates = new LinkedHashSet<>(toDelete);

        Set<Location> marked = set();
        Set<Location> preds = set();
        while (true) {
            // 1. Remove bad states and non-coreachable states.
            // Also remove predecessors if allowed.
            while (!toDelete.isEmpty()) {
                Location loc = toDelete.remove();
                preds.clear(); // set of predecessors to consider from 'loc'
                for (Edge e: loc.getIncoming()) {
                    if (e.srcLoc == loc) {
                        continue; // Ignore self loops.
                    }
                    synDump.removedDestination(e.srcLoc, e.event, loc);
                    if (e.event.isControllable()) {
                        // Check later whether the source location became
                        // non-coreachable for non-marked locations.
                        if (!e.srcLoc.marked) {
                            preds.add(e.srcLoc);
                        }
                    } else {
                        // Any edge with a non-controllable event is bad.
                        if (badStates.add(e.srcLoc)) {
                            toDelete.add(e.srcLoc);
                        }
                    }
                }

                if (aut.initial == loc) {
                    // Removing the initial state: Throw away everything!
                    aut.clear();
                    return;
                }
                aut.removeLocation(loc); // Drop bad location and edges.

                // Add predecessors that have become non-coreachable.
                for (Location loc2: preds) {
                    if (loc2.outgoingEdges == null) {
                        if (badStates.add(loc2)) {
                            synDump.blockingLocation(loc2);
                            toDelete.add(loc2);
                        }
                    }
                }
            }
            // toDelete is empty here.
            badStates.clear(); // Also drop the removed locations from the set.

            // 2. Find non-marked non-coreachable states, and put them in
            // 'toDelete/badStates'

            int badLocCount = AutomatonHelper.getNonCoreachableCount(aut, marked);
            if (badLocCount == 0) {
                return; // All locations are coreachable.
            }

            // Get the non-coreachables.
            for (Location loc = aut.locations; badLocCount > 0; loc = loc.nextLoc) {
                if (!marked.contains(loc)) {
                    toDelete.add(loc);
                    synDump.nonCoreachableLocation(loc);
                    badStates.add(loc);
                    badLocCount--;
                }
            }
            marked.clear();

            // Remove the found bad states in the next iteration.
        }
    }
}
