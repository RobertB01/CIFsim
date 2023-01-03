//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.automata.origin.Origin;
import org.eclipse.escet.cif.eventbased.automata.origin.StateOrigin;

/** Wrapper class holding a combination of locations. */
public class State {
    /** Array holding the combined locations. */
    public Location[] locs;

    /**
     * Constructor for the {@link State} class.
     *
     * @param locs Locations to wrap.
     */
    public State(Location[] locs) {
        this.locs = locs; // No copy, take the given array.
    }

    /**
     * Construct an origin for this state.
     *
     * @return An origin for this state.
     */
    public Origin getOrigin() {
        if (locs.length == 1) {
            return locs[0].origin;
        }
        return new StateOrigin(this);
    }

    /**
     * Get marked property of the state.
     *
     * @return Whether the state should be marked.
     */
    public boolean isMarked() {
        for (Location loc: locs) {
            if (!loc.marked) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof State)) {
            return false;
        }
        State oLocs = (State)other;
        if (oLocs.locs.length != locs.length) {
            return false;
        }
        for (int i = 0; i < locs.length; i++) {
            if (!oLocs.locs[i].equals(locs[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Location loc: locs) {
            h = (h << 1) ^ loc.hashCode();
        }
        return h;
    }
}
