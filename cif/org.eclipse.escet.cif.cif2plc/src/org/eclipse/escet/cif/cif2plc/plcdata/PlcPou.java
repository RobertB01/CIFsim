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

import static org.eclipse.escet.cif.cif2plc.plcdata.PlcProject.INDENT;
import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;

/** PLC Program Organization Unit (POU). */
public class PlcPou extends PlcObject {
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

    /** The next unique number to use when creating a temporary variable. */
    private int nextTmpVarNr = 0;

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

    /**
     * Creates a fresh temporary variable and adds it to this POU. Also adds a line of code to the body of the POU to
     * assign the given value to the temporary variable, if such a value is provided.
     *
     * @param type The type of the temporary variable.
     * @param valueTxt The textual representation of the value of the temporary variable, in IEC 61131-3 syntax. May be
     *     {@code null} if the caller will set the value later on.
     * @return The name of the newly created temporary variable.
     */
    public String addTempVar(PlcType type, String valueTxt) {
        // Create and add variable.
        String varName = "tmp" + nextTmpVarNr;
        nextTmpVarNr++;
        PlcVariable var = new PlcVariable(varName, type);
        if (retType == null) {
            tempVars.add(var); // Temporary variable of the program.
        } else {
            localVars.add(var); // Local variable of the function.
        }

        // Add assignment statement to the body.
        if (valueTxt != null) {
            body.add("%s := %s;", varName, valueTxt);
        }

        // Return the name of the new temporary variable.
        return varName;
    }
}
