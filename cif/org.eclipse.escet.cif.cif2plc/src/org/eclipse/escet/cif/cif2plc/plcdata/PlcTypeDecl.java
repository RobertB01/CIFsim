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

import org.eclipse.escet.common.box.Box;
import org.eclipse.escet.common.box.CodeBox;
import org.eclipse.escet.common.box.HBox;
import org.eclipse.escet.common.box.MemoryCodeBox;

/** PLC type declaration. */
public class PlcTypeDecl extends PlcObject {
    /** The name of the type declaration. */
    public final String name;

    /** The type of the type declaration. */
    public final PlcType type;

    /**
     * Constructor for the {@link PlcTypeDecl} class.
     *
     * @param name The name of the type declaration.
     * @param type The type of the type declaration.
     */
    public PlcTypeDecl(String name, PlcType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Box toBox() {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("TYPE %s:", name);
        c.indent();
        c.add(new HBox(type.toBox(), ";"));
        c.dedent();
        c.add("END_TYPE");
        return c;
    }

    @Override
    public Box toBoxS7() {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("TYPE %s:", name);
        c.indent();
        c.add(new HBox(type.toBoxS7(), ";"));
        c.dedent();
        c.add("END_TYPE");
        return c;
    }

    /**
     * Converts the type declaration to a textual representation in IEC 61131-3 syntax. The output is TwinCAT specific,
     * in that it implements a workaround for a bug in TwinCAT, where structs may in type declarations may not be
     * terminated with a semicolon.
     *
     * @return The type declaration in a textual representation in IEC 61131-3 syntax, compatible with TwinCAT.
     */
    public String toStringTwinCat() {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("TYPE %s:", name);
        c.indent();
        if (type instanceof PlcStructType) {
            // Special TwinCAT workaround.
            c.add(type);
        } else {
            c.add(new HBox(type, ";"));
        }
        c.dedent();
        c.add("END_TYPE");
        return c.toString();
    }
}
