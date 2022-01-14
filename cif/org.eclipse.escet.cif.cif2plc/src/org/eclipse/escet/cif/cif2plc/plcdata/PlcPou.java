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

import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_1200;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputType.S7_1500;
import static org.eclipse.escet.cif.cif2plc.options.PlcOutputTypeOption.getPlcOutputType;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcPouType.FUNCTION;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcPouType.PROGRAM;
import static org.eclipse.escet.cif.cif2plc.plcdata.PlcProject.INDENT;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;

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

    @Override
    public Box toBox() {
        CodeBox c = headerToBox();
        c.add();
        c.add(body);
        c.add("END_%s", pouType);
        return c;
    }

    /**
     * Converts the header of the POU to IEC 61131-3 syntax. The header includes the POU type, name, return type, and
     * variables, but not the body or the final closing keyword.
     *
     * @return The header of the POU in IEC 61131-3 syntax.
     */
    public CodeBox headerToBox() {
        CodeBox c = new MemoryCodeBox(INDENT);
        String retTypeTxt = (retType == null) ? "" : fmt(": %s", retType);
        c.add("%s %s%s", pouType, name, retTypeTxt);
        if (!inputVars.isEmpty()) {
            c.add("VAR_INPUT");
            c.indent();
            for (PlcVariable var: inputVars) {
                c.add(var);
            }
            c.dedent();
            c.add("END_VAR");
        }
        if (!outputVars.isEmpty()) {
            c.add("VAR_OUTPUT");
            c.indent();
            for (PlcVariable var: outputVars) {
                c.add(var);
            }
            c.dedent();
            c.add("END_VAR");
        }
        if (!localVars.isEmpty()) {
            c.add("VAR");
            c.indent();
            for (PlcVariable var: localVars) {
                c.add(var);
            }
            c.dedent();
            c.add("END_VAR");
        }
        if (!tempVars.isEmpty()) {
            c.add("VAR_TEMP");
            c.indent();
            for (PlcVariable var: tempVars) {
                c.add(var);
            }
            c.dedent();
            c.add("END_VAR");
        }
        return c;
    }

    @Override
    public Box toBoxS7() {
        CodeBox c = new MemoryCodeBox(INDENT);

        // Is optimized block access supported? Only supported for S7-1200 and S7-1500. It optimizes data storage and
        // performance.
        boolean optimizedBlockAccess = getPlcOutputType() == S7_1200 || getPlcOutputType() == S7_1500;

        // Get the POU text, either FUNCTION for functions, or ORGANIZATION_BLOCK for the main program.
        String pouTypeText;
        switch (pouType) {
            case FUNCTION:
                pouTypeText = "FUNCTION";
                break;
            case PROGRAM:
                pouTypeText = "ORGANIZATION_BLOCK";
                break;

            default:
                throw new RuntimeException("Unknown pou type: " + pouType);
        }

        // Write header. The header includes the POU type, name and return type.
        String retTypeTxt = (retType == null) ? "" : fmt(": %s", retType);
        c.add("%s %s%s", pouTypeText, name, retTypeTxt);
        c.add("{ S7_Optimized_Access := '%b' }", optimizedBlockAccess);
        c.indent();

        // Write the input variables.
        if (!inputVars.isEmpty()) {
            c.add("VAR_INPUT");
            c.indent();
            for (PlcVariable var: inputVars) {
                c.add("%s: %s;", var.name, var.type);
            }
            c.dedent();
            c.add("END_VAR");
        }

        // Write the local variables.
        if (!localVars.isEmpty()) {
            if (pouType == FUNCTION) {
                // S7 doesn't have local variables in functions. Instead, all variables are temporary. They do the same
                // but are named differently. That is, after exiting the function, the values are lost. This is similar
                // to how CIF and TwinCAT handle local variables in functions.
                c.add("VAR_TEMP");
                c.indent();
                for (PlcVariable var: localVars) {
                    c.add("%s: %s;", var.name, var.type);
                }
                c.dedent();
                c.add("END_VAR");
            }
            // else: Different from functions, local variables of programs are persistent and are written to a DB file.
        }

        // Write the temporary variables.
        if (!tempVars.isEmpty() || !outputVars.isEmpty()) {
            // Functions shouldn't have variables declared as temporary. As all variables are temporary.
            Assert.check(pouType != FUNCTION);

            c.add("VAR_TEMP");
            c.indent();
            for (PlcVariable var: tempVars) {
                c.add("%s: %s;", var.name, var.type);
            }
            for (PlcVariable var: outputVars) {
                // There should only be two output variables, timerValue0 and timerValue1. These are part of the main
                // program. In S7 the main program cannot have output variables. Hence, we add them as temporary
                // variables. Functions should not have output variables.
                Assert.check(pouType == PROGRAM);
                Assert.check(outputVars.size() == 2);
                c.add("%s: %s;", var.name, var.type);
            }
            c.dedent();
            c.add("END_VAR");
        }

        // Write the program body.
        c.dedent();
        c.add();
        c.add("BEGIN");

        c.indent();
        c.add(body);

        // Close POU.
        c.dedent();
        c.add("END_%s", pouTypeText);

        return c;
    }
}
