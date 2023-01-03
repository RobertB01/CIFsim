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

package org.eclipse.escet.cif.eventbased.analysis;

import java.util.List;

/** Storage of name information of an automaton in the synthesis analysis dump. */
public class AutomatonNamesInfo {
    /** Name of the automaton. */
    public final String autName;

    /** Names of the locations (ordered by their number, first entry is the initial location). */
    public final List<String> locNames;

    /**
     * Constructor of the {@link AutomatonNamesInfo} class.
     *
     * @param autName Name of the automaton.
     * @param locNames Names of the locations (ordered by their number, first entry is the initial location).
     */
    public AutomatonNamesInfo(String autName, List<String> locNames) {
        this.autName = autName;
        this.locNames = locNames;
    }
}
