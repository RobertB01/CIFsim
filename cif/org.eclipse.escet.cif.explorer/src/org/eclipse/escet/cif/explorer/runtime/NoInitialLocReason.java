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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Automaton;

/** No initial state due to an automaton not having an initial location. */
public class NoInitialLocReason extends NoInitialStateReason {
    /** The automaton that does not have an initial location. */
    public final Automaton aut;

    /**
     * Constructor for the {@link NoInitialLocReason} class.
     *
     * @param aut The automaton that does not have an initial location.
     */
    public NoInitialLocReason(Automaton aut) {
        this.aut = aut;
    }

    @Override
    public String getMessage() {
        return fmt("Automaton \"%s\" has no initial location.", CifTextUtils.getAbsName(aut));
    }
}
