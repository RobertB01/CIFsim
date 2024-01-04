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

package org.eclipse.escet.cif.plcgen.model.declarations;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

/** PLC global variable list. */
public class PlcGlobalVarList {
    /** The name of the global variable list. */
    public final String name;

    /** Whether the variable list contains constants ({@code true}) or variables ({@code false}). */
    public final boolean constants;

    /** The variables of the global variable list. */
    public List<PlcVariable> variables = list();

    /**
     * Constructor for the {@link PlcGlobalVarList} class.
     *
     * @param name The name of the global variable list.
     * @param constants Whether the variable list contains constants ({@code true}) or variables ({@code false}).
     */
    public PlcGlobalVarList(String name, boolean constants) {
        this.name = name;
        this.constants = constants;
    }
}
