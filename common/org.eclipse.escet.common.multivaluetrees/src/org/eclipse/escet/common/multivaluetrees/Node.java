//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.multivaluetrees;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Map;

import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;

/** Node in the multi-value tree. */
public class Node {
    /** Level of the {@link Tree#ONE} terminator node. */
    public static final int ONE_LEVEL = -2;

    /** Level of the {@link Tree#ZERO} terminator node. */
    public static final int ZERO_LEVEL = -1;

    /** Variable represented by this node. */
    public final VarInfo varInfo;

    /** Child nodes. */
    public final Node[] childs;

    /**
     * Constructor of the {@link Node} class.
     *
     * <p>
     * <b>Warning</b>: Don't use this constructor directly. Instead, use {@link Tree#addNode} to keep the nodes managed.
     * </p>
     *
     * @param varInfo Variable represented by this node.
     * @param childs Child nodes.
     */
    Node(VarInfo varInfo, Node[] childs) {
        this.varInfo = varInfo;
        this.childs = childs;

        Assert.check(varInfo.length == childs.length);
    }

    /**
     * Dump the graph rooted at the node as sequence of lines.
     *
     * @param nodeName Name of the node for human readability.
     */
    public void dumpGraphLines(String nodeName) {
        // 'lines' contains a line of text for each node, nodes '0' and '1' can be printed but have no line of text.
        List<String> lines = list(null, null);
        Map<Node, String> mappedNodes = map();
        mappedNodes.put(Tree.ZERO, ".");
        mappedNodes.put(Tree.ONE, "T");

        // Walk the graph, and output the named root, and the visited nodes.
        String rootNodeName = constructGraphLines(mappedNodes, lines);
        OutputProvider.dbg("%s: node %s", nodeName, rootNodeName);
        OutputProvider.idbg();
        for (String line: lines) {
            if (line != null) {
                OutputProvider.dbg(line);
            }
        }
        OutputProvider.ddbg();
    }

    /**
     * Recursively construct a description of the graph of the tree rooted at self.
     *
     * <p>
     * Each node will output a line of itself only one time, and refer to it on the next calls.
     * </p>
     *
     * <p>
     * Use {@link #dumpGraphLines} as starting point for dumping a graph of a node.
     * </p>
     *
     * @param mappedNodes Already visited nodes with their name.
     * @param lines Lines of output with the description of the graph.
     * @return Name of the node itself.
     */
    private String constructGraphLines(Map<Node, String> mappedNodes, List<String> lines) {
        // Check if self was printed already, if so, return.
        String myName = mappedNodes.get(this);
        if (myName != null) {
            return myName;
        }

        // Construct information to dump for the node.
        int myLine = lines.size();
        lines.add(null);

        // Setup the name for self, and add it to the mapped nodes.
        // Note that this can be delayed, as the graph has no cycles.
        myName = String.valueOf(myLine);
        mappedNodes.put(this, myName);

        // Find names of the children.
        List<String> childNodeNames = listc(varInfo.length);
        for (Node child: childs) {
            childNodeNames.add(child.constructGraphLines(mappedNodes, lines));
        }

        // Create a line of text for self and insert it in the lines.
        StringBuilder sb = new StringBuilder();
        sb.append(myName);
        sb.append("=");
        sb.append(varInfo.toString());
        sb.append(", children=[");
        for (int i = 0; i < childNodeNames.size(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            // For larger nodes, also add the node value.
            if (varInfo.length > 6) {
                sb.append(i + varInfo.lower);
                sb.append("=");
            }
            sb.append(childNodeNames.get(i));
        }
        sb.append("]");
        lines.set(myLine, sb.toString());

        return myName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (!(other instanceof Node)) {
            return false;
        }
        Node otherNode = (Node)other;
        if (varInfo != otherNode.varInfo) {
            return false;
        }
        if (childs.length != otherNode.childs.length) {
            return false;
        }

        // Compare each child. Uses identity as equivalence notion, under the assumption that
        // children are unique already.
        for (int i = 0; i < childs.length; i++) {
            if (childs[i] != otherNode.childs[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = varInfo.level;
        if (childs != null) {
            for (Node c: childs) {
                h = h * 3 + c.varInfo.level;
            }
        }
        return h;
    }

    @Override
    public String toString() {
        if (varInfo.level == Node.ONE_LEVEL) {
            return "*TRUE*";
        }

        if (varInfo.level == Node.ZERO_LEVEL) {
            return "*FALSE*";
        }
        return fmt("Node(level=%d, var=%s)", varInfo.level, varInfo);
    }
}
