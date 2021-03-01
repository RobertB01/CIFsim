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
import org.eclipse.escet.common.java.Assert;

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

    @Override
    public Box toBox() {
        CodeBox c = new MemoryCodeBox(INDENT);
        c.add("CONFIGURATION %s", name);
        c.indent();
        for (PlcGlobalVarList globalVarList: globalVarLists) {
            if (globalVarList.variables.isEmpty()) {
                continue;
            }
            c.add(globalVarList.toBox());
        }

        // Ensure one resource. At least is required. More resources, means
        // we have to use 'RESOURCE <name> ON <type>' syntax, and we don't
        // want to specify the <type>.
        Assert.check(resources.size() <= 1);
        for (PlcResource resource: resources) {
            c.add(resource.toBox());
        }

        c.dedent();
        c.add("END_CONFIGURATION");
        return c;
    }
}
