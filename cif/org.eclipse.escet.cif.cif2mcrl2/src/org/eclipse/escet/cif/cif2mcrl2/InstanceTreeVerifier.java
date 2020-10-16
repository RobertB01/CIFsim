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

package org.eclipse.escet.cif.cif2mcrl2;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.cif.cif2mcrl2.storage.AutomatonData;
import org.eclipse.escet.cif.cif2mcrl2.storage.VariableData;
import org.eclipse.escet.cif.cif2mcrl2.tree.AutomatonProcessNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.CombinedProcessNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.CombinedTextNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.ElementaryTextNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.ProcessNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.TextNode;
import org.eclipse.escet.cif.cif2mcrl2.tree.VariableProcessNode;
import org.eclipse.escet.common.app.framework.exceptions.UnsupportedException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/** Verify whether the instance tree is correct. */
public class InstanceTreeVerifier {
    /** Constructor of the {@link InstanceTreeVerifier} class. */
    private InstanceTreeVerifier() {
        // Static class.
    }

    /**
     * Check the tree of instance names on correctness. Also get the variables to treat as local variables.
     *
     * @param root Root text node.
     * @param autDatas Automata that should be used in the tree.
     * @param sharedVars Shared variables that should be used in the tree.
     * @param singleUseVars Variables that are used in one automaton only.
     * @return Variables local to a mCRL2 behavior processes.
     */
    public static Set<VariableData> checkAndGetLocals(TextNode root, List<AutomatonData> autDatas,
            Set<VariableData> sharedVars, Set<VariableData> singleUseVars)
    {
        // Prepare a usage map of all names.
        Map<String, Boolean> useMap = map();
        for (AutomatonData autData: autDatas) {
            useMap.put(autData.name, false);
        }
        for (VariableData dv: sharedVars) {
            useMap.put(dv.name, false);
        }
        for (VariableData dv: singleUseVars) {
            useMap.put(dv.name, false);
        }
        Assert.check(useMap.size() == autDatas.size() + sharedVars.size() + singleUseVars.size());

        // Check the tree for usage of the names.
        List<String> problems = checkNode(root, useMap);

        // Verify that required names are indeed used.
        for (AutomatonData autData: autDatas) {
            if (!useMap.get(autData.name)) {
                String msg = fmt("Automaton \"%s\" is missing in the instance tree text.", autData.name);
                problems.add(msg);
            }
        }
        for (VariableData dv: sharedVars) {
            if (!useMap.get(dv.name)) {
                String msg = fmt("Discrete variable \"%s\" is missing in the instance tree text.", dv.name);
                problems.add(msg);
            }
        }

        // Report fatal problems, if any exist.
        if (!problems.isEmpty()) {
            Collections.sort(problems, Strings.SORTER);
            String msg = "CIF to mCRL2 transformation failed due to missing automata or discrete variables:\n - "
                    + StringUtils.join(problems, "\n - ");
            throw new UnsupportedException(msg);
        }

        // Inform about used local variables, and construct the remaining local
        // variables.
        Set<VariableData> localVars = set();
        for (VariableData dv: singleUseVars) {
            if (useMap.get(dv.name)) {
                out(fmt("INFO: Discrete variable \"%s\" is instantiated as variable process, but could be a local "
                        + "variable of an automaton process instead.", dv.name));
            } else {
                localVars.add(dv);
            }
        }
        return localVars;
    }

    /**
     * Check usage of names in the sub-tree.
     *
     * @param node Sub-tree to inspect.
     * @param useMap Available names mapped to usage information.
     * @return Problems found in the sub-tree.
     */
    private static List<String> checkNode(TextNode node, Map<String, Boolean> useMap) {
        if (node instanceof ElementaryTextNode) {
            ElementaryTextNode etn = (ElementaryTextNode)node;
            Boolean used = useMap.get(etn.name);
            if (used == null) {
                return list(fmt("Name \"%s\" used in the instance tree text is not an absolute name of an automaton "
                        + "or discrete variable.", etn.name));
            }
            if (used) {
                return list(fmt("Name \"%s\" is included multiple times in the instance tree option text.", etn.name));
            }
            useMap.put(etn.name, true);
            return list();
        }
        Assert.check(node instanceof CombinedTextNode);
        CombinedTextNode ctn = (CombinedTextNode)node;
        List<String> problems = list();
        for (TextNode n: ctn.children) {
            problems.addAll(checkNode(n, useMap));
        }
        return problems;
    }

    /**
     * Check the shape of the process tree, bottom nodes should contain parallel compositions of automata, to the top a
     * chain with a single variable process at each level.
     *
     * @param root Root of the tree to check
     */
    public static void checkProcessTreeShape(ProcessNode root) {
        if (root instanceof AutomatonProcessNode) {
            return; // Tree is a single automaton -> ok.
        }
        if (root instanceof VariableProcessNode) {
            return; // Tree is a single variable -> ok.
        }

        Assert.check(root instanceof CombinedProcessNode);
        checkProcessTreeShape((CombinedProcessNode)root, false);
    }

    /**
     * Check the shape of the process tree, bottom nodes should contain parallel compositions of automata, to the top a
     * chain with a single variable process at each level.
     *
     * <p>
     * The general shape of the tree should be <em>"{@code [var-node] || [remainder]}"</em> or
     * <em>"{@code [remainder] || [var-node]}"</em> where {@code [remainder]} is then again one of the previous forms,
     * or an arbitrary collection of parallel executing automata without {@code [var-node]}s.
     * </p>
     *
     * @param root Root of the (sub)tree to check.
     * @param seenBehaviour Whether anything else than a parallel composition with a single variable has been
     *     encountered while recursing down.
     */
    private static void checkProcessTreeShape(CombinedProcessNode root, boolean seenBehaviour) {
        if (!seenBehaviour && root.children.size() == 2) {
            // At the "top of the tree" part, one child should be a variable node, the
            // second child should be a combined node, or an automaton node.

            // Assign the nodes, trying to make 'ch1' a variable.
            ProcessNode ch1 = root.children.get(0);
            ProcessNode ch2 = root.children.get(1);
            if (!(ch1 instanceof VariableProcessNode)) {
                // Fist assignment did not work, swapping them is the only remaining option.
                ch1 = root.children.get(1);
                ch2 = root.children.get(0);
            }

            if (ch1 instanceof VariableProcessNode) {
                if (ch2 instanceof VariableProcessNode) {
                    // ch1 == variable, ch2 == variable
                    String msg = "Unsupported tree shape. Instance tree has no behavior, please add one or more "
                            + "processes.";
                    throw new UnsupportedException(msg);
                } else if (ch2 instanceof AutomatonProcessNode) {
                    // ch1 == variable, ch2 == behavior
                    return; // Variable and an automaton -> ok
                } else {
                    // ch1 == variable, ch2 == parallel composition
                    Assert.check(ch2 instanceof CombinedProcessNode);
                    checkProcessTreeShape((CombinedProcessNode)ch2, false);
                    return;
                }
            } // else there does not seem to be a variable here, drop into the behavior part.
        } // else seenBehavior holds or > 2 children, drop into behavior part.

        // At the 'bottom-part' of the tree, should only encounter parallel composition or
        // behavior process nodes.

        // Check for no variables here.
        for (ProcessNode pn: root.children) {
            if (pn instanceof VariableProcessNode) {
                VariableProcessNode vpn = (VariableProcessNode)pn;
                String msg = fmt("Unsupported tree shape: Variable \"%s\" should be above all automata in the instance "
                        + "tree and be the only direct child in the parallel composition.", vpn.name);
                throw new UnsupportedException(msg);
            }
        }

        // Recurse down over combined process nodes.
        for (ProcessNode pn: root.children) {
            if (pn instanceof CombinedProcessNode) {
                checkProcessTreeShape((CombinedProcessNode)pn, true);
            } else {
                Assert.check(pn instanceof AutomatonProcessNode);
            }
        }
        return;
    }
}
