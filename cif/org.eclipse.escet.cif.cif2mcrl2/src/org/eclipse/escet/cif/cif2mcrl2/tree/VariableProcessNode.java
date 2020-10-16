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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.cif2mcrl2.NameMaps;
import org.eclipse.escet.cif.cif2mcrl2.storage.VariableData;
import org.eclipse.escet.common.box.VBox;
import org.eclipse.escet.common.java.Assert;

/** Variable process node in the instance tree. */
public class VariableProcessNode extends ProcessNode {
    /** Variable represented by this node. */
    public final VariableData variable;

    /**
     * Constructor of the {@link VariableProcessNode} class.
     *
     * @param name Name of the node.
     * @param variable Variable represented by this node.
     */
    public VariableProcessNode(String name, VariableData variable) {
        super(name);
        this.variable = variable;
    }

    @Override
    protected String getCifName() {
        return "variable " + variable.name;
    }

    @Override
    public void deriveActions(Set<VariableData> localVars) {
        Assert.check(!localVars.contains(variable));

        availProcessVars = set(variable);
        valueVars = set(variable);
        eventVarUse = map();
    }

    @Override
    public void addDefinitions(NameMaps names, Set<VariableData> localVars, VBox code) {
        String varProcName = names.getVariableProcess(variable.variable);
        String varType = names.getTypeName(variable.getType());

        String varValue = variable.getHasValueAction() ? names.getVariableValue(variable.variable) : null;
        String varRead = names.getVarRead(variable.variable);
        String behRead = names.getBehRead(variable.variable);
        String varSync = names.getVarSync(variable.variable);
        String varWrite = names.getVarWrite(variable.variable);
        String behWrite = names.getBehWrite(variable.variable);

        Integer bnd;
        String sumPrefix, lower, upper;

        // If the type has range limits, add them as conditions in the
        // summation.
        bnd = variable.getLowerLimit();
        lower = (bnd == null) ? null : fmt("(m >= %d)", bnd);
        bnd = variable.getUpperLimit();
        upper = (bnd == null) ? null : fmt("(m <= %d)", bnd);

        String sumText = "sum m:" + varType;
        if (lower == null) {
            if (upper == null) {
                sumPrefix = sumText + " . true";
            } else {
                sumPrefix = sumText + " . " + upper;
            }
        } else {
            if (upper == null) {
                sumPrefix = sumText + " . " + lower;
            } else {
                sumPrefix = sumText + " . (" + lower + " && " + upper + ")";
            }
        }

        // Action declarations: [value], vread, vwrite, sync, aread, awrite : <type>;
        List<String> actions = list();
        if (varValue != null) {
            actions.add(varValue);
        }
        actions.add(varRead);
        actions.add(varWrite);
        actions.add(varSync);
        actions.add(behRead);
        actions.add(behWrite);
        String actionsStr = StringUtils.join(actions, ", ");
        code.add(fmt("act %s : %s;", actionsStr, varType));
        code.add();

        // Variable process definition.
        code.add(fmt("proc %s(v:%s) =", varProcName, varType));
        if (varValue != null) {
            code.add(fmt("  %s(v) . %s(v) +", varValue, varProcName));
        }
        code.add(fmt("  %s(v) . %s(v) +", varRead, varProcName));
        code.add(fmt("  %s -> %s(m) . %s(m) +", sumPrefix, varWrite, varProcName));
        code.add(fmt("  %s -> %s(v) | %s(m) . %s(m);", sumPrefix, varRead, varWrite, varProcName));

        code.add(); // Add empty line as separation for the next definition.
    }

    @Override
    public void addInstantiations(NameMaps names, Set<VariableData> localVars, VBox code) {
        String varProcess = names.getVariableProcess(variable.variable);
        code.add(fmt("%s(%s)", varProcess, variable.initialValue));
    }
}
