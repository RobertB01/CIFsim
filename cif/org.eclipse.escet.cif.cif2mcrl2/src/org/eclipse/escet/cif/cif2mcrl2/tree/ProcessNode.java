//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2mcrl2.tree;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.escet.cif.cif2mcrl2.NameMaps;
import org.eclipse.escet.cif.cif2mcrl2.storage.EventVarUsage;
import org.eclipse.escet.cif.cif2mcrl2.storage.VarUsage;
import org.eclipse.escet.cif.cif2mcrl2.storage.VariableData;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.Event;
import org.eclipse.escet.common.box.GridBox;
import org.eclipse.escet.common.box.HBox;
import org.eclipse.escet.common.box.StreamCodeBox;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;

/** Node in the instance tree. */
public abstract class ProcessNode {
    /** Name of the node. */
    public final String name;

    /** Collection of variable processes available in this (sub-)tree. */
    public Set<VariableData> availProcessVars = null;

    /** How each event uses non-local variables. */
    public Map<Event, EventVarUsage> eventVarUse = null;

    /** Collection of variables available for querying their current value. */
    public Set<VariableData> valueVars = null;

    /**
     * Constructor of the {@link ProcessNode} class.
     *
     * @param name Name of the node.
     */
    public ProcessNode(String name) {
        this.name = name;
    }

    /**
     * Get the name of the node as CIF element, if available.
     *
     * @return Name of the node as CIF element, or {@code null} if not available.
     */
    protected String getCifName() {
        return null;
    }

    /**
     * Perform bottom-up traversal of the tree, filling {@link #availProcessVars}, {@link #valueVars}, and
     * {@link #eventVarUse} of the process nodes.
     *
     * @param localVars Variables that are not shared between automata processes.
     */
    public abstract void deriveActions(Set<VariableData> localVars);

    /**
     * Define the processes, location sorts, and actions.
     *
     * @param names Mapping of CIF elements to unique mCRL2 names.
     * @param localVars Local variables of the entire system.
     * @param code Output stream to write to.
     */
    public abstract void addDefinitions(NameMaps names, Set<VariableData> localVars, VBox code);

    /**
     * Instantiate the process tree.
     *
     * @param names Mapping of CIF elements to unique mCRL2 names.
     * @param localVars Local variables of the entire system.
     * @param code Output stream to write to.
     */
    public abstract void addInstantiations(NameMaps names, Set<VariableData> localVars, VBox code);

    /**
     * Dump the variable usage of the node. This method only writes output. Some nodes may want to override this for
     * also dumping the child nodes.
     *
     * @param code Output stream to write to.
     */
    public void dumpActions(StreamCodeBox code) {
        Map<VariableData, VarUsage> values;

        code.add("===============================================================");
        String cifName = getCifName();
        cifName = (cifName == null) ? "" : " (" + cifName + ")";
        code.add(fmt("Node %s%s", name, cifName));

        if (!availProcessVars.isEmpty()) {
            code.add();
            code.add("Available variable processes:");
            code.indent();
            for (VariableData dv: availProcessVars) {
                code.add(dv.name);
            }
            code.dedent();
        }

        if (!eventVarUse.isEmpty()) {
            code.add();
            code.add("Variable use by behavior processes ordered by event:");
            code.indent();
            for (Entry<Event, EventVarUsage> entry: eventVarUse.entrySet()) {
                code.add(CifTextUtils.getAbsName(entry.getKey()) + ":");
                code.indent();
                values = entry.getValue().varUses;
                if (values.isEmpty()) {
                    code.add("No variables from variables processes accessed.");
                } else {
                    GridBox b = new GridBox(values.entrySet().size(), 3, 0, 2);
                    int i = 0;
                    for (Entry<VariableData, VarUsage> usages: values.entrySet()) {
                        b.set(i, 0, usages.getKey().name);
                        VarUsage vu = usages.getValue();
                        HBox c = new HBox("read:", vu.readAccess.toString());
                        b.set(i, 1, c);
                        c = new HBox("write:", vu.writeAccess.toString());
                        b.set(i, 2, c);
                        i++;
                    }
                    Assert.check(i == values.entrySet().size());
                    code.add(b);
                }
                code.dedent();
            }
            code.dedent();
        }
    }
}
