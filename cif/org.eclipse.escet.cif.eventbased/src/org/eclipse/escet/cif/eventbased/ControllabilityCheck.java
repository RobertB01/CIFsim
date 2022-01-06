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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonHelper;
import org.eclipse.escet.cif.eventbased.automata.EventAtLocation;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.builders.AutomatonBuilder;
import org.eclipse.escet.cif.eventbased.builders.State;
import org.eclipse.escet.cif.eventbased.builders.StateEdges;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;

/**
 * Check whether an automaton is a proper supervisor for a set of plants.
 *
 * <p>
 * A proper supervisor may not disable edges with an uncontrollable event of the plant. In addition, it may be useful to
 * know which edges with a controllable event are disabled.
 * </p>
 *
 * <p>
 * The implementation explores the states of a synchronous product of the supervisor and the plant automata, noting
 * which events get disabled. The disabled events are stored for later processing.
 * </p>
 */
public class ControllabilityCheck {
    /** Constructor of the {@link ControllabilityCheck} class. */
    private ControllabilityCheck() {
        // Static class.
    }

    /**
     * Check whether the controllability check can be performed with the given automata.
     *
     * @param auts Automata to use in the controllability check.
     */
    public static void controllabilityCheckPreCheck(List<Automaton> auts) {
        Automaton sup = null;
        boolean seenPlant = false;

        for (Automaton aut: auts) {
            // Record and check automaton kind.
            switch (aut.kind) {
                case PLANT:
                    // Only need to verify existence of at least one plant.
                    seenPlant = true;
                    break;

                case SUPERVISOR:
                    if (sup != null) {
                        String msg = fmt(
                                "Unsupported supervisor \"%s\": only one supervisor allowed, "
                                        + "and automaton \"%s\" is already selected as supervisor.",
                                aut.name, sup.name);
                        throw new InvalidInputException(msg);
                    }
                    sup = aut;
                    break;

                case REQUIREMENT:
                case UNKNOWN: {
                    String msg = fmt("Unsupported automaton \"%s\": only plants and a supervisor are allowed "
                            + "for the controllability check.", aut.name);
                    throw new InvalidInputException(msg);
                }

                default:
                    throw new AssertionError("Unexpected automaton kind.");
            }

            AutomatonHelper.reportNonDeterministic(aut);
        }
        if (!seenPlant) {
            String msg = "No plant automata found in the input for the controllability check.";
            throw new InvalidInputException(msg);
        }
        if (sup == null) {
            String msg = "No supervisor automaton found in the input for the controllability check.";
            throw new InvalidInputException(msg);
        }
    }

    /**
     * Perform controllability check. It is assumed that the input survived execution of
     * {@link #controllabilityCheckPreCheck}.
     *
     * @param auts Automata to check for controllability.
     * @return Non-empty list of disabled uncontrollable events, or non-empty list of disabled controllable events (if
     *     no uncontrollable event was disabled), or {@code null} if no event was disabled at all.
     */
    public static List<EventAtLocation> controllabilityCheck(List<Automaton> auts) {
        Automaton sup = null;
        List<Automaton> plants = list();
        for (Automaton aut: auts) {
            switch (aut.kind) {
                case PLANT:
                    plants.add(aut);
                    break;

                case SUPERVISOR:
                    sup = aut;
                    break;

                default:
                    throw new AssertionError("Unexpected automaton kind.");
            }
        }
        return controllabilityCheck(sup, plants);
    }

    /**
     * Get disabled events of the combined plants and supervisor state. The location of the latter is at index
     * {@code plants.size()} of the returned states.
     *
     * @param sup Supervisor automaton.
     * @param plants Participating plants.
     * @return Non-empty list of disabled uncontrollable events, or non-empty list of disabled controllable events (if
     *     no uncontrollable event was disabled), or {@code null} if no event was disabled at all.
     */
    public static List<EventAtLocation> controllabilityCheck(Automaton sup, List<Automaton> plants) {
        List<EventAtLocation> disableds = getDisabledEvents(sup, plants);

        List<EventAtLocation> uncontrolleds = list();
        List<EventAtLocation> controlleds = list();
        for (EventAtLocation de: disableds) {
            if (de.evt.isControllable()) {
                controlleds.add(de);
            } else {
                uncontrolleds.add(de);
            }
        }
        if (uncontrolleds.isEmpty()) {
            if (controlleds.isEmpty()) {
                return null;
            }
            return controlleds;
        }
        return uncontrolleds;
    }

    /**
     * Find events in the plants that are disabled by the supervisor.
     *
     * <p>
     * Works for deterministic automata only.
     * </p>
     *
     * @param sup Supervisor automaton.
     * @param plants Participating plants.
     * @return Events disabled in the combined plant/supervisor state.
     */
    public static List<EventAtLocation> getDisabledEvents(Automaton sup, List<Automaton> plants) {
        List<Automaton> automs = listc(plants.size() + 1);
        automs.addAll(plants);
        automs.add(sup);

        List<EventAtLocation> disableds = list();
        AutomatonBuilder builder = new AutomatonBuilder(automs);
        for (State srcState: builder) {
            Location srcLoc = builder.getLocation(srcState);
            builder.edgeBuilder.setupStateEdges(srcState);
            for (StateEdges se: builder.edgeBuilder.getStateEdges()) {
                int idx = se.disabledIndex();
                if (idx >= 0) { // Event is disabled.
                    if (idx < plants.size()) {
                        continue;
                    }
                    disableds.add(new EventAtLocation(srcLoc, se.event));
                } else { // Event is enabled, expand to the next state.
                    for (State destState: se) {
                        builder.getLocation(destState);
                    }
                }
            }
        }
        return disableds;
    }
}
