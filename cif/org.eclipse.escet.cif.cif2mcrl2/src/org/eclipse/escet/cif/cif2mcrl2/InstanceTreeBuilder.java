//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.escet.cif.cif2mcrl2.storage.AutomatonData;
import org.eclipse.escet.cif.cif2mcrl2.storage.VariableData;
import org.eclipse.escet.cif.cif2mcrl2.tree.AutomatonProcessNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.CombinedProcessNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.CombinedTextNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.ElementaryTextNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.ProcessNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.TextNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.VariableProcessNode;
import org.eclipse.escet.common.java.Assert;

/** Class for building the instance process tree. */
public class InstanceTreeBuilder {
    /** Constructor of the {@link InstanceTreeBuilder} class. */
    private InstanceTreeBuilder() {
        // Static class.
    }

    /**
     * Build the process tree from the text node tree.
     *
     * <p>
     * All checks have already been done in {@link InstanceTreeVerifier#checkAndGetLocals}.
     * </p>
     *
     * @param node Root text node to use.
     * @param autDatas Automata to use in the tree.
     * @param sharedVars Shared variables to use in the tree.
     * @param singleUseVars Variables that may be used in the tree.
     * @return Constructed process tree.
     */
    public static ProcessNode buildProcessTree(TextNode node, List<AutomatonData> autDatas,
            Set<VariableData> sharedVars, Set<VariableData> singleUseVars)
    {
        // Build name maps for faster conversion.
        Map<String, AutomatonData> autNames = map();
        for (AutomatonData autData: autDatas) {
            autNames.put(autData.name, autData);
        }

        Map<String, VariableData> varNames = map();
        for (VariableData dv: sharedVars) {
            varNames.put(dv.name, dv);
        }
        for (VariableData dv: singleUseVars) {
            varNames.put(dv.name, dv);
        }

        return buildProcessNode("1", node, autNames, varNames);
    }

    /**
     * Convert the provided text node to a node in the process tree.
     *
     * @param name Name of the node (hierarchical name).
     * @param node Node to convert.
     * @param autNames Available automata.
     * @param varNames Available variables.
     * @return The converted node.
     */
    private static ProcessNode buildProcessNode(String name, TextNode node, Map<String, AutomatonData> autNames,
            Map<String, VariableData> varNames)
    {
        if (node instanceof ElementaryTextNode) {
            ElementaryTextNode etn = (ElementaryTextNode)node;
            AutomatonData autData = autNames.get(etn.name);
            if (autData != null) {
                return new AutomatonProcessNode(name, autData);
            }
            return new VariableProcessNode(name, varNames.get(etn.name));
        }
        Assert.check(node instanceof CombinedTextNode);
        CombinedTextNode ctn = (CombinedTextNode)node;
        List<ProcessNode> processes = listc(ctn.children.size());
        int i = 1;
        for (TextNode n: ctn.children) {
            String childName = fmt("%s.%d", name, i);
            processes.add(buildProcessNode(childName, n, autNames, varNames));
            i++;
        }
        return new CombinedProcessNode(name, processes);
    }

    /**
     * Build the default process node tree. Behavior processes at the bottom, and variable processes at the top.
     *
     * @param autDatas Collection automata to add.
     * @param sharedVars Collection variables to add.
     * @return Constructed process tree.
     */
    public static ProcessNode buildDefaultTree(List<AutomatonData> autDatas, Set<VariableData> sharedVars) {
        String name = "1";
        for (int i = 1; i < autDatas.size() + sharedVars.size(); i++) {
            name += ".1";
        }
        ProcessNode root = null;
        for (AutomatonData ad: autDatas) {
            if (root == null) {
                root = new AutomatonProcessNode(name, ad);
            } else {
                ProcessNode node = new AutomatonProcessNode(name + ".2", ad);
                root = new CombinedProcessNode(name, list(root, node));
            }
            if (name.length() > 1) {
                name = name.substring(0, name.length() - 2);
            }
        }
        for (VariableData dv: sharedVars) {
            if (root == null) {
                root = new VariableProcessNode(name, dv);
            } else {
                ProcessNode node = new VariableProcessNode(name + ".2", dv);
                root = new CombinedProcessNode(name, list(root, node));
            }
            if (name.length() > 1) {
                name = name.substring(0, name.length() - 2);
            }
        }
        return root;
    }
}
