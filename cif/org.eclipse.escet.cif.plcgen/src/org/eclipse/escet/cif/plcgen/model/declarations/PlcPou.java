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

import static org.eclipse.escet.cif.plcgen.model.declarations.PlcProject.INDENT;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.cif.plcgen.model.types.PlcType;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;

/** PLC Program Organization Unit (POU). */
public class PlcPou {
    /** The name of the POU. */
    public final String name;

    /** The type of the POU. */
    public final PlcPouType pouType;

    /** The return type for function, {@code null} for programs. */
    public final PlcType retType;

    /** The input variables of the POU. */
    public List<PlcVariable> inputVars = list();

    /** The output variables of the POU. */
    public List<PlcVariable> outputVars = list();

    /** The local persistent variables of the POU. */
    public List<PlcVariable> localVars = list();

    /** The local temporary (non-persistent) variables of the POU. */
    public List<PlcVariable> tempVars = list();

    /** Body of the POU in IEC 61131-3 Structured Text syntax. */
    public CodeBox body = new MemoryCodeBox(INDENT);

    /**
     * Constructor for the {@link PlcPou} class.
     *
     * @param name The name of the POU.
     * @param pouType The type of the POU.
     * @param retType The return type for function, {@code null} for programs.
     */
    public PlcPou(String name, PlcPouType pouType, PlcType retType) {
        this.name = name;
        this.pouType = pouType;
        this.retType = retType;
    }
}
