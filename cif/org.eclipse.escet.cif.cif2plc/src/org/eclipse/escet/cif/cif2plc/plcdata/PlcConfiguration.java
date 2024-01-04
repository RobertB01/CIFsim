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

package org.eclipse.escet.cif.cif2plc.plcdata;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

/** PLC configuration. */
public class PlcConfiguration extends PlcObject {
    /** The name of the configuration. */
    public final String name;

    /** The global variable lists of the configuration. */
    public List<PlcGlobalVarList> globalVarLists = list();

    /** The resources of the configuration. */
    public List<PlcResource> resources = list();

    /**
     * Constructor for the {@link PlcConfiguration} class.
     *
     * @param name The name of the configuration.
     */
    public PlcConfiguration(String name) {
        this.name = name;
    }
}
