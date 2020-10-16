//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.builders;

import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.Location;

/**
 * Helper class to construct a new automaton from a combination of source automata.
 *
 * <p>
 * Its {@link #iterator} method provides access to unexplored source state combinations.
 * </p>
 */
public class AutomatonBuilder extends StateExplorer<Location> {
    /** Destination automaton. */
    public final Automaton destAut;

    /**
     * Constructor for the {@link AutomatonBuilder} class.
     *
     * @param automs Originating automata.
     */
    public AutomatonBuilder(List<Automaton> automs) {
        super(automs);

        destAut = new Automaton(destAlphabet);

        // Mark the combined initial state as the first unfinished state.
        State initial = makeInitialState();
        if (initial != null) {
            destAut.setInitial(getLocation(initial));
        }
    }

    /**
     * Get the location associated with the source state.
     *
     * @param state Combined locations of the source automata.
     * @return Location of the destination automaton.
     */
    public Location getLocation(State state) {
        Location loc = getValue(state);
        if (loc != null) {
            return loc;
        }
        loc = new Location(destAut, state.getOrigin());
        loc.marked = state.isMarked();
        addValue(state, loc, false);
        return loc;
    }
}
