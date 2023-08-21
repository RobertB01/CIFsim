//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

/** Node in the multi-level synthesis tree. */
public class TreeNode {
    /**
     * Index of the tree node.
     *
     * <p>
     * A user may attach any desired meaning to this variable and change it at any time. The {@link #linearizeTree}
     * function sets it to the index of the returned list where the node is stored.
     * </p>
     */
    public int index;

    /** Plant-groups that are included in the tree node. */
    public final BitSet plantGroups;

    /** Requirement-groups that are included in the tree node. */
    public final BitSet requirementGroups;

    /** Child nodes. */
    public final List<TreeNode> childNodes;

    /**
     * Constructor of the {@link TreeNode} class.
     *
     * @param plantGroups Plant-groups that are included in the tree node.
     * @param requirementGroups Requirement-groups that are included in the tree node.
     */
    public TreeNode(BitSet plantGroups, BitSet requirementGroups) {
        this(plantGroups, requirementGroups, List.of());
    }

    /**
     * Constructor of the {@link TreeNode} class.
     *
     * @param plantGroups Plant-groups that are included in the tree node.
     * @param requirementGroups Requirement-groups that are included in the tree node.
     * @param childNodes Child nodes.
     */
    public TreeNode(BitSet plantGroups, BitSet requirementGroups, List<TreeNode> childNodes) {
        this.plantGroups = plantGroups;
        this.requirementGroups = requirementGroups;
        this.childNodes = childNodes;
    }

    /**
     * Walk recursively depth-first left to right through the node tree rooted at the given node, collect all
     * encountered nodes, and return them as a list of nodes. Also sets {@link #index} for each node in the tree.
     *
     * @param root Root node to start the walk.
     * @return All encountered nodes in the tree rooted at {@code root}.
     */
    public static List<TreeNode> linearizeTree(TreeNode root) {
        return linearizeTree(root, list());
    }

    /**
     * Walk recursively depth-first left to right through the node tree, and add all encountered nodes to the result
     * list. Also sets {@link #index} for each node in the tree.
     *
     * @param node Node to start the walk.
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
