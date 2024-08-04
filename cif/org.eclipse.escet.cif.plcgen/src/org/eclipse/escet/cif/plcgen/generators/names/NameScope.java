//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators.names;

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

/** A scope with a collection of used names. */
public class NameScope {
    /** Lower case cleaned names that already are in use in this scope. */
    private final Set<String> usedNames = set();

    /**
     * Is the given cleaned lower case name already used in the scope?
     *
     * @param loweredCleanedName Cleaned lower case name to test for being used.
     * @return Whether the give name is already in use in the scope.
     */
    public boolean isNameUsed(String loweredCleanedName) {
        return usedNames.contains(loweredCleanedName);
    }

    /**
     * Add a cleaned lower case name to the collection of used names.
     *
     * @param loweredCleanedName Name to add.
     */
    public void addName(String loweredCleanedName) {
        usedNames.add(loweredCleanedName);
    }
}
