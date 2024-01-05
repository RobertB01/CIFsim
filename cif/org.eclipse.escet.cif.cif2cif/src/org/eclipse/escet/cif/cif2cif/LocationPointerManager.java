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

package org.eclipse.escet.cif.cif2cif;

import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.automata.Update;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/** Location pointer manager. */
public interface LocationPointerManager {
    /**
     * Creates a CIF expression that can be used to refer to the given location, taking into account that a location
     * pointer variable has been created for the automaton that contains the location.
     *
     * @param loc The location.
     * @return The newly created expression.
     */
    public abstract Expression createLocRef(Location loc);

    /**
     * Creates a CIF update that can be used to update the location pointer variable created for the automaton that
     * contains the given location, to the value for the given location. The updates makes the given location the
     * current location.
     *
     * @param loc The location.
     * @return The newly created update.
     */
    public abstract Update createLocUpdate(Location loc);
}
