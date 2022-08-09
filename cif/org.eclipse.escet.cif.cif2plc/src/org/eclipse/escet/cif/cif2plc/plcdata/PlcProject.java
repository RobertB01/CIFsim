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

/** PLC project. */
public class PlcProject extends PlcObject {
    /** The indentation to use for the Structured Text files. */
    public static final int INDENT = 4;

    /** The name of the PLC project. */
    public final String name;

    /** The type declarations of the project. */
    public List<PlcTypeDecl> typeDecls = list();

    /** The POUs of the project. */
    public List<PlcPou> pous = list();

    /** The configurations of the project. */
    public List<PlcConfiguration> configurations = list();

    /**
     * Constructor for the {@link PlcProject} class.
     *
     * @param name The name of the PLC project.
     */
    public PlcProject(String name) {
        this.name = name;
    }
}
