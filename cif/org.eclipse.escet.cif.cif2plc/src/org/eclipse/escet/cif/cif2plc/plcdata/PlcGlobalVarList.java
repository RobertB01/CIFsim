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

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.MemoryCodeBox;
import org.eclipse.escet.common.java.Assert;

/** PLC global variable list. */
public class PlcGlobalVarList extends PlcObject {
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

    @Override
    public Box toBox() {
        Assert.check(!variables.isEmpty()); // Empty VAR_GLOBAL is illegal.
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("VAR_GLOBAL%s // %s", constants ? " CONSTANT" : "", name);
        c.indent();
        for (PlcVariable variable: variables) {
            c.add(variable);
        }
        c.dedent();
        c.add("END_VAR");
        return c;
    }

    @Override
    public Box toBoxS7() {
        // S7 uses a 'tag table' for PLC I/O and constants. We write the tag table as an .xml file for easy importing in
        // TIA Portal.
        CodeBox c = new MemoryCodeBox(INDENT);

        // The header.
        c.add("<?xml version='1.0' encoding='utf-8'?>");
        c.add("<Tagtable name='%s'>", name);
        c.indent();

        // The variables, either constants or input variables. 'type', 'value', 'name' and 'address' shouldn't contain
        // XML characters that need escaping (&, <, >, ' or "). We also can't have values with string type.
        if (constants) {
            for (PlcVariable constant: variables) {
                c.add("<Constant type='%s' remark='' value='%s'>%s</Constant>", constant.type, constant.value,
                        constant.name);
            }
        } else {
            for (PlcVariable var: variables) {
                c.add("<Tag type='%s' hmiVisible='True' hmiWriteable='False' hmiAccessible='True' retain='False' "
                        + "remark='' addr='%s'>%s</Tag>", var.type, var.address, var.name);
            }
        }
        c.dedent();

        // Close tag table.
        c.add("</Tagtable>");

        return c;
    }
}
