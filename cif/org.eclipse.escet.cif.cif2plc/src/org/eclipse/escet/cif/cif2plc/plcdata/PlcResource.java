//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

    @Override
    public Box toBox() {
        // We only support a single resource for now, so the 'name' is not
        // included in the box representation, to avoid having to specify a
        // resource type name.
        CodeBox c = new MemoryCodeBox(INDENT);
        for (PlcGlobalVarList globalVarList: globalVarLists) {
            if (globalVarList.variables.isEmpty()) {
                continue;
            }
            c.add(globalVarList.toBox());
        }
        for (PlcTask task: tasks) {
            c.add(task.toBox());
        }
        for (PlcPouInstance pouInstance: pouInstances) {
            c.add(pouInstance.toBox(null));
        }
        for (PlcTask task: tasks) {
            for (PlcPouInstance pouInstance: task.pouInstances) {
                c.add(pouInstance.toBox(task.name));
            }
        }
        return c;
    }
}
