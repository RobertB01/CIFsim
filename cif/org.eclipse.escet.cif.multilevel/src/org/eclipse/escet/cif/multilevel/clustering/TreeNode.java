//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.multilevel.clustering;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.BitSet;
import java.util.List;

/** Node in a multi-level synthesis tree. */
public class TreeNode {
    /** Index of the tree node. Is {@code -1} until set by {@link #linearizeTree}. */
    public int index = -1;

    /** The plant groups that are included in the tree node. */
    public final BitSet plantGroups;

    /** The requirement groups that are included in the tree node. */
    public final BitSet requirementGroups;

    /** The child nodes of the tree node. Creates a node that is initially empty. */
    public final List<TreeNode> childNodes = list();

    /** Constructor of the {@link TreeNode} class. */
    public TreeNode() {
        this(new BitSet(), new BitSet());
    }

    /**
     * Constructor of the {@link TreeNode} class. Creates a node that initially has no children.
     *
     * @param plantGroups The plant groups that are included in the tree node.
     * @param requirementGroups The requirement groups that are included in the tree node.
     */
    public TreeNode(BitSet plantGroups, BitSet requirementGroups) {
        this.plantGroups = plantGroups;
        this.requirementGroups = requirementGroups;
    }

    /**
     * Walk recursively depth-first left-to-right through the node tree rooted at this node, collect all encountered
     * nodes, and return them as a list of nodes. Also sets {@link #index} for each node in the tree.
     *
     * @return All encountered nodes in the tree rooted at this node.
     */
    public List<TreeNode> linearizeTree() {
        return linearizeTree(this, list());
    }

    /**
     * Walk recursively depth-first left-to-right through the node tree, and add all encountered nodes to the result
     * list. Also sets {@link #index} for each node in the tree.
     *
     * @param node The node to start the walk.
     * @param nodes Storage for encountered nodes, {@code node} is assumed not to be in the storage yet. Is extended
     *     in-place.
     * @return The encountered nodes including the (sub)tree rooted at {@code node}.
     */
    private static List<TreeNode> linearizeTree(TreeNode node, List<TreeNode> nodes) {
        node.index = nodes.size();
        nodes.add(node);
        for (TreeNode child: node.childNodes) {
            linearizeTree(child, nodes);
        }
        return nodes;
    }
}
