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

package org.eclipse.escet.cif.eventbased.equivalence;

import org.eclipse.escet.cif.eventbased.automata.Location;

/** Location in a block. */
public class BlockLocation {
    /** Represented location. */
    public final Location loc;

    /** Index number of the automaton of the location. */
    public final int autNumber;

    /** Path length of the location from the initial state. */
    public final int depth;

    /** Index number of the block that it is part of. */
    public int blockNumber;

    /**
     * Constructor of the {@link BlockLocation} class.
     *
     * @param loc Represented location.
     * @param autNumber Index number of the automaton of the location.
     * @param depth Path length of the location from the initial state.
     * @param blockNumber Index number of the block that it is part of.
     */
    public BlockLocation(Location loc, int autNumber, int depth, int blockNumber) {
        this.loc = loc;
        this.autNumber = autNumber;
        this.depth = depth;
        this.blockNumber = blockNumber;
    }
}
