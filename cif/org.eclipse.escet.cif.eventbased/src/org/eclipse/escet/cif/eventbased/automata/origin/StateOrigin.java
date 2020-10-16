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

package org.eclipse.escet.cif.eventbased.automata.origin;

import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.builders.State;
import org.eclipse.escet.common.java.Assert;

/** Origin is a state. */
public class StateOrigin extends Origin {
    /** State representing the location. */
    public final State state;

    /**
     * Constructor of the {@link StateOrigin} class.
     *
     * @param state State representing the location.
     */
    public StateOrigin(State state) {
        this.state = state;
    }

    @Override
    public void toString(StringBuilder sb, int flags) {
        Assert.check((flags & (ALLOW_STATE | ADD_PREFIX)) == (ALLOW_STATE | ADD_PREFIX));
        sb.append("state ");

        boolean first = true;
        for (Location loc: state.locs) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            loc.origin.toString(sb, flags & ALLOW_CIF_LOCATION);
        }
    }
}
