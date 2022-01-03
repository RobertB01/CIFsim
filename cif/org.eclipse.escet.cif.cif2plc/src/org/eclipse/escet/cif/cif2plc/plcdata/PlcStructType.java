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

/** PLC struct type. */
public class PlcStructType extends PlcType {
    /** The fields of the struct type. */
    public List<PlcVariable> fields = list();

    @Override
    public Box toBox() {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("STRUCT");
        c.indent();
        for (PlcVariable field: fields) {
            c.add(field);
        }
        c.dedent();
        c.add("END_STRUCT");
        return c;
    }
}
