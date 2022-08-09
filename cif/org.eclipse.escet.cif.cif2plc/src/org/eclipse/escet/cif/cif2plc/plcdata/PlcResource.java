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

package org.eclipse.escet.cif.cif2plc.plcdata;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

/** PLC resource. */
public class PlcResource extends PlcObject {
    /** The name of the resource. */
    public final String name;

    /** The global variable lists of the resource. */
    public final List<PlcGlobalVarList> globalVarLists = list();

    /** The tasks of the resource. */
    public List<PlcTask> tasks = list();

    /** The POU instances of the resource. */
    public List<PlcPouInstance> pouInstances = list();

    /**
     * Constructor for the {@link PlcResource} class.
     *
     * @param name The name of the resource.
     */
    public PlcResource(String name) {
        this.name = name;
    }
}
