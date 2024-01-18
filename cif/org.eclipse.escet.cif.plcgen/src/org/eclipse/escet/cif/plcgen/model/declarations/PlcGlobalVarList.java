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

    /** Kind of global variable list. */
    public final PlcVarListKind listKind;

    /** The variables of the global variable list. */
    public List<PlcVariable> variables = list();

    /**
     * Constructor for the {@link PlcGlobalVarList} class.
     *
     * @param name The name of the global variable list.
     * @param listKind Kind of global variables.
     */
    public PlcGlobalVarList(String name, PlcVarListKind listKind) {
        this.name = name;
        this.listKind = listKind;
    }

    /** Available kinds of global variable lists. */
    public static enum PlcVarListKind {
        /** Global variable list contains constants. */
        CONSTANTS,

        /** Global variable list contains normal variables. */
        GENERIC,

        /** Global variable list contains input or output variables. */
        INPUT_OUTPUT,

        /** Global variable list contains timers. */
        TIMERS
    }
}
