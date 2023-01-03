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

package org.eclipse.escet.cif.eventbased.automata;

import java.util.Comparator;

/** Comparator for sorting event-based automata. */
public class AutomatonComparator implements Comparator<Automaton> {
    /** Global automaton comparator instance. */
    public static final AutomatonComparator INSTANCE = new AutomatonComparator();

    /** Constructor of the {@link AutomatonComparator} class. */
    private AutomatonComparator() {
        // Nothing to do.
    }

    @Override
    public int compare(Automaton o1, Automaton o2) {
        return org.eclipse.escet.common.java.Strings.SORTER.compare(o1.name, o2.name);
    }
}
