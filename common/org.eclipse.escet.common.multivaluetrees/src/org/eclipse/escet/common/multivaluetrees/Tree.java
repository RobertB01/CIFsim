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

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import java.util.Queue;
import java.util.WeakHashMap;

import org.eclipse.escet.common.java.Assert;

/**
 * Trees of multi-value nodes in increasing levels from the roots.
 *
 * <p>
 * A node is associated with a particular use of a variable, e.g. 'current value' or 'next value'. The tree code uses
 * the {@link VarInfo} class to link variables to nodes.
 * </p>
 */
public class Tree {
    /** False node constant. */
    public static final Node ZERO;

    /** True node constant. */
    public static final Node ONE;

    /** Node table, with automagic removal of unused nodes. */
    private final WeakHashMap<Node, WeakReference<Node>> nodeTable = new WeakHashMap<>();

    static {
        Node[] noChilds = new Node[0];
        ZERO = new Node(new VarInfo(Node.ZERO_LEVEL, null, 0, 0, 0), noChilds);
        ONE = new Node(new VarInfo(Node.ONE_LEVEL, null, 0, 0, 0), noChilds);
    }

    /**
     * Convert a variable with its child nodes to a new node in the tree.
     *
     * @param varInfo Variable used in the node.
     * @param childs Child nodes of the variable.
     * @return The (probably new) root node.
     */
    private Node addNode(VarInfo varInfo, Node[] childs) {
        // Check whether a new node is needed.
        Node child = childs[0];
        for (int i = 1; i < childs.length; i++) {
            if (child != childs[i]) {
                // At least two different nodes, make a new node!
                Node node = new Node(varInfo, childs);
                WeakReference<Node> value = nodeTable.get(node);
                if (value == null) {
                    nodeTable.put(node, new WeakReference<>(node));
                    return node;
                }
                node = value.get();
                Assert.notNull(node);
                return node;
            }
        }

        // All children are equal, node represents 'true && child'. Use child instead.
        return child;
    }

    /**
     * Convert a tree in (somewhat) human-readable form to a string for debugging purposes.
     *
     * @param n Tree to dump.
     * @return Textual representation of the graph.
     */
    public String dumpGraph(Node n) {
        StringBuilder sb = new StringBuilder();

        int freeNum = 1;
        Map<Node, String> foundNodes = map();
        Queue<Node> notPrinted = new ArrayDeque<>();

        foundNodes.put(n, String.valueOf(freeNum++));
        foundNodes.put(Tree.ONE, "*TRUE*");
        foundNodes.put(Tree.ZERO, "*FALSE*");
        notPrinted.add(n);

        while (!notPrinted.isEmpty()) {
            Node a = notPrinted.poll();
            String name = foundNodes.get(a);
            if (a == Tree.ONE || a == Tree.ZERO) {
                sb.append(fmt("%s\n", name));
                continue;
            }
            sb.append(fmt("%s (%s):", name, a.varInfo.toString()));
            for (Node c: a.childs) {
                name = foundNodes.get(c);
                if (name == null) {
                    name = String.valueOf(freeNum++);
                    foundNodes.put(c, name);
                    notPrinted.add(c);
                }
                sb.append(' ');
                sb.append(name);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /**
     * Build equality condition for a variable for a given value of the variable.
     *
     * @param varInfo Variable to use in the equality.
     * @param value Desired value of the variable in the tree.
     * @return Tree with the constructed condition (that is, a single node).
     */
    public Node buildEqualityValue(VarInfo varInfo, int value) {
        return buildEqualityIndex(varInfo, value - varInfo.lower);
    }

    /**
     * Build equality condition for a variable for a given value index of the variable.
     *
     * @param varInfo Variable to use in the equality.
     * @param index Desired value index of the variable in the tree.
     * @return Tree with the constructed condition (that is, a single node).
     */
    public Node buildEqualityIndex(VarInfo varInfo, int index) {
        return buildEqualityIndex(varInfo, index, Tree.ONE);
    }

    /**
     * Build equality condition for a variable for a given value at a given index on top of an existing tree.
     *
     * @param varInfo Variable to use in the equality.
     * @param index Desired value index of the variable in the tree.
     * @param sub Existing tree to build the equality on.
     * @return Tree with the constructed condition (that is, a single node).
     */
    public Node buildEqualityIndex(VarInfo varInfo, int index, Node sub) {
        Assert.check(sub == Tree.ZERO || sub == Tree.ONE || sub.varInfo.level > varInfo.level);

        Node[] childs = new Node[varInfo.length];
        Arrays.fill(childs, Tree.ZERO);
        childs[index] = sub;
        return addNode(varInfo, childs);
    }

    /**
     * Compute the conjunction of trees 'a' and 'b'.
     *
     * @param a First tree to merge.
     * @param b Second tree to merge.
     * @return The result tree after the merge.
     */
    public Node conjunct(Node a, Node b) {
        if (a == b) {
            return a;
        }
        if (a == Tree.ZERO || b == Tree.ZERO) {
            return Tree.ZERO;
        }
        if (a == Tree.ONE) {
            return b;
        }
        if (b == Tree.ONE) {
            return a;
        }

        int aLevel = a.varInfo.level;
        int bLevel = b.varInfo.level;
        if (aLevel == bLevel) {
            return addNode(a.varInfo, conjunctChilds(a.childs, b.childs));
        } else if (aLevel < bLevel) {
            return addNode(a.varInfo, conjunctChilds(a.childs, b));
        } else {
            return addNode(b.varInfo, conjunctChilds(b.childs, a)); // Conjunction is commutative.
        }
    }

    /**
     * Apply conjunction on each child in 'aChilds' and corresponding entry in 'bChilds'.
     *
     * @param aChilds Child nodes of one side of the conjunction.
     * @param bChilds Child nodes of the other side of the conjunction.
     * @return Computed child nodes.
     */
    private Node[] conjunctChilds(Node[] aChilds, Node[] bChilds) {
        Node[] childs = new Node[aChilds.length];
        for (int i = 0; i < aChilds.length; i++) {
            if (childs[i] != null) {
                continue; // Skip computing deduced results.
            }

            // Compute the i-th child.
            Node n = conjunct(aChilds[i], bChilds[i]);
            childs[i] = n;

            // If left side and right side are equal, the conjunction will be the same.
            // Apply to avoid computing it again.
            for (int j = i + 1; j < aChilds.length; j++) {
                if (aChilds[j] == aChilds[i] && bChilds[j] == bChilds[i]) {
                    childs[j] = n;
                }
            }
        }
        return childs;
    }

    /**
     * Apply conjunction on each child in 'aChilds' and 'bNode'.
     *
     * @param aChilds Child nodes of one side of the conjunction.
     * @param bNode Other side of the conjunction for all 'aChilds'.
     * @return Computed child nodes.
     */
    private Node[] conjunctChilds(Node[] aChilds, Node bNode) {
        Node[] childs = new Node[aChilds.length];
        for (int i = 0; i < aChilds.length; i++) {
            if (childs[i] != null) {
                continue; // Skip computing deduced results.
            }

            // Compute the i-th child.
            Node n = conjunct(aChilds[i], bNode);
            childs[i] = n;

            // Since all conjunctions use the same 'bNode', if 'aChilds[j] == aChilds[i]', then the conjunction
            // result for the j-th child will be equal to 'n' as well. Apply to avoid computing it again.
            for (int j = i + 1; j < aChilds.length; j++) {
                if (aChilds[j] == aChilds[i]) {
                    childs[j] = n;
                }
            }
        }
        return childs;
    }

    /**
     * Compute the conjunction of several trees.
     *
     * @param nodes Trees to merge in conjunction.
     * @return The result tree after the merge.
     */
    public Node multiConjunct(Node... nodes) {
        Assert.check(nodes.length > 0);
        // Try to reduce the computation by skipping nodes that don't add anything.
        BitSet skipNodes = new BitSet(nodes.length);
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == Tree.ZERO) {
                return nodes[i];
            }
            if (nodes[i] == Tree.ONE) { // X & true == X.
                skipNodes.set(i);
                continue;
            }

            // Check if node[i] is already done previously.
            // TODO: Quite expensive for long ranges.
            for (int j = 0; j < i; j++) {
                if (nodes[j] == nodes[i]) {
                    skipNodes.set(i); // Seen already, and X & X == X
                    break;
                }
            }
        }

        // Perform the computation, skipping entries previously marked as not useful.
        Node result = nodes[0]; // May be marked for being ONE, but that's handled in the 'conjunct' function.
        for (int i = 1; i < nodes.length; i++) {
            if (skipNodes.get(i)) {
                continue;
            }
            result = conjunct(result, nodes[i]);
        }
        return result;
    }

    /**
     * Compute the disjunction of trees 'a' and 'b'.
     *
     * @param a First tree to merge.
     * @param b Second tree to merge.
     * @return The result tree after the merge.
     */
    public Node disjunct(Node a, Node b) {
        if (a == b) {
            return a;
        }
        if (a == Tree.ZERO) {
            return b;
        }
        if (b == Tree.ZERO) {
            return a;
        }
        if (a == Tree.ONE) {
            return a;
        }
        if (b == Tree.ONE) {
            return b;
        }

        int aLevel = a.varInfo.level;
        int bLevel = b.varInfo.level;
        if (aLevel == bLevel) {
            return addNode(a.varInfo, disjunctChilds(a.childs, b.childs));
        } else if (aLevel < bLevel) {
            return addNode(a.varInfo, disjunctChilds(a.childs, b));
        } else {
            return addNode(b.varInfo, disjunctChilds(b.childs, a)); // Disjunction is commutative.
        }
    }

    /**
     * Apply disjunction on each child in 'aChilds' and corresponding entry in 'bChilds'.
     *
     * @param aChilds Child nodes of one side of the disjunction.
     * @param bChilds Child nodes of the other side of the disjunction.
     * @return Computed child nodes.
     */
    private Node[] disjunctChilds(Node[] aChilds, Node[] bChilds) {
        Node[] childs = new Node[aChilds.length];
        for (int i = 0; i < aChilds.length; i++) {
            if (childs[i] != null) {
                continue; // Skip computing deduced results.
            }

            // Compute the i-th child.
            Node n = disjunct(aChilds[i], bChilds[i]);
            childs[i] = n;

            // If left side and right side are equal, the disjunction will be the same.
            // Apply to avoid computing it again.
            for (int j = i + 1; j < aChilds.length; j++) {
                if (aChilds[j] == aChilds[i] && bChilds[j] == bChilds[i]) {
                    childs[j] = n;
                }
            }
        }
        return childs;
    }

    /**
     * Apply disjunction on each child in 'aChilds' and 'bNode'.
     *
     * @param aChilds Child nodes of one side of the disjunction.
     * @param bNode Other side of the disjunction for all 'aChilds'.
     * @return Computed child nodes.
     */
    private Node[] disjunctChilds(Node[] aChilds, Node bNode) {
        Node[] childs = new Node[aChilds.length];
        for (int i = 0; i < aChilds.length; i++) {
            if (childs[i] != null) {
                continue; // Skip computing deduced results.
            }

            // Compute the i-th child.
            Node n = disjunct(aChilds[i], bNode);
            childs[i] = n;

            // Since all disjunctions use the same 'bNode', if 'aChilds[j] == aChilds[i]', then the disjunction
            // result for the j-th child will be equal to 'n' as well. Apply to avoid computing it again.
            for (int j = i + 1; j < aChilds.length; j++) {
                if (aChilds[j] == aChilds[i]) {
                    childs[j] = n;
                }
            }
        }
        return childs;
    }

    /**
     * Compute the disjunction of several trees.
     *
     * @param nodes Trees to merge in disjunction.
     * @return The result tree after the merge.
     */
    public Node multiDisjunct(Node... nodes) {
        Assert.check(nodes.length > 0);
        // Try to reduce the computation by skipping nodes that don't add anything.
        BitSet skipNodes = new BitSet(nodes.length);
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == Tree.ONE) {
                return nodes[i];
            }
            if (nodes[i] == Tree.ZERO) { // X | false == X.
                skipNodes.set(i);
                continue;
            }

            // Check if node[i] is already done previously.
            // TODO: Quite expensive for long ranges.
            for (int j = 0; j < i; j++) {
                if (nodes[j] == nodes[i]) {
                    skipNodes.set(i); // Seen already, and X | X == X
                    break;
                }
            }
        }

        // Perform the computation, skipping entries previously marked as not useful.
        Node result = nodes[0]; // May be marked for being ZERO, but that's handled in the 'disjunct' function.
        for (int i = 1; i < nodes.length; i++) {
            if (skipNodes.get(i)) {
                continue;
            }

            result = disjunct(result, nodes[i]);
        }
        return result;
    }

    /**
     * Perform the invert (NOT) operator on a tree.
     *
     * @param n Root node to invert.
     * @return The inverted tree.
     */
    public Node invert(Node n) {
        if (n == Tree.ONE) {
            return Tree.ZERO;
        }
        if (n == Tree.ZERO) {
            return Tree.ONE;
        }

        Node[] childs = new Node[n.childs.length];
        for (int i = 0; i < childs.length; i++) {
            childs[i] = invert(n.childs[i]);
        }

        return addNode(n.varInfo, childs);
    }

    /**
     * Perform a variable abstraction on a tree, where the node is replaced by a disjunction over its children.
     *
     * @param n Root of the tree to modify.
     * @param abstractions Variable abstractions to perform. Must be ordered on increasing level in the tree.
     * @return The result tree after abstraction.
     */
    public Node variableAbstractions(Node n, VarInfo[] abstractions) {
        if (abstractions.length == 0) {
            return n;
        }

        // Verify that the abstractions are sorted on increasing level.
        int prev = -1;
        for (VarInfo abstraction: abstractions) {
            Assert.check(abstraction.level > prev);
            prev = abstraction.level;
        }

        return abstractVariables(n, 0, abstractions);
    }

    /**
     * Perform a variable abstraction on a tree, where the node is replaced by a disjunction over its children.
     *
     * @param n Root of the tree.
     * @param abstractionIndex Index in 'abstractions', pointing to the next abstraction to perform. If the parameter is
     *     beyond the end of the array, all abstractions have been done.
     * @param abstractions Variable abstractions to perform. Ordered on increasing level in the tree.
     * @return The result root after abstraction.
     */
    private Node abstractVariables(Node n, int abstractionIndex, VarInfo[] abstractions) {
        if (abstractionIndex >= abstractions.length) {
            return n; // Beyond the last abstraction.
        }

        if (n == Tree.ZERO || n == Tree.ONE) {
            return n;
        }

        int level = n.varInfo.level;
        while (level > abstractions[abstractionIndex].level) {
            // Below the levels for abstractions[abstractionIndex], continue to the next abstraction.
            abstractionIndex++;
            if (abstractionIndex == abstractions.length) {
                return n; // Reached the end of abstractions.
            }
        }

        // Process its children
        Node[] childs = new Node[n.childs.length];
        for (int i = 0; i < n.childs.length; i++) {
            childs[i] = abstractVariables(n.childs[i], abstractionIndex, abstractions);
        }

        if (level < abstractions[abstractionIndex].level) { // Above variable to abstract.
            return addNode(n.varInfo, childs);
        } else { // Abstract this variable by replacing it with the disjunction of its children.
            return multiDisjunct(childs);
        }
    }

    /**
     * Denote that a particular variable has a given value and is not needed in the tree any more. Computes the
     * sub-expression for that case and eliminates that variable from the expression as well.
     *
     * <p>
     * TODO: This function performs two different things, which may be useful to split somewhen.
     * </p>
     *
     * @param n Node to modify for the assignment.
     * @param varInfo Variable to assign.
     * @param index Value index of the variable.
     * @return Sub-expression that holds for that value, excluding the variable and its value.
     */
    public Node assign(Node n, VarInfo varInfo, int index) {
        Assert.check(index >= 0 && index < varInfo.length);

        if (n == Tree.ZERO || n == Tree.ONE) {
            return n;
        }

        if (n.varInfo.level > varInfo.level) {
            return n;
        }

        if (n.varInfo.level == varInfo.level) {
            return n.childs[index];
        }

        // Above assigned variable.
        Node[] childs = new Node[n.childs.length];
        for (int i = 0; i < childs.length; i++) {
            childs[i] = assign(n.childs[i], varInfo, index);
        }

        return addNode(n.varInfo, childs);
    }

    /**
     * Perform all variable replacements in the tree, where variables in the replacement must be at adjacent levels.
     *
     * <p>
     * Conceptually, 'replace (x, x+)' with 'x' the old variable and 'x+' the new variable means that any existing node
     * of 'x' in the tree is eliminated, and a new node for 'x' is created at the same time. The sub-trees to use for
     * the new 'x' node are obtained from the 'x+' node. The latter nodes are also eliminated.
     * </p>
     *
     * @param n Root of the tree to modify.
     * @param replacements Variable replacements to perform. Must be ordered on increasing level in the tree. Each
     *     replacement must have its variables at adjacent levels. Replacements may not overlap.
     * @return The rewritten tree.
     */
    public Node adjacentReplacements(Node n, VariableReplacement[] replacements) {
        if (replacements.length == 0) {
            return n;
        }

        // Verify that the replacements are sorted on increasing level.
        int prev = -2;
        for (VariableReplacement replacement: replacements) {
            Assert.check(replacement.topLevel >= prev + 2);
            prev = replacement.topLevel;
        }
        return replaceAdjacents(n, 0, replacements);
    }

    /**
     * Recursively replace adjacent variable levels in the tree.
     *
     * @param n Root of the tree.
     * @param replaceIndex Index in 'replacements', pointing to the next replacement to perform. If the parameter is
     *     beyond the end of the array, all replacements have been done.
     * @param replacements Replacements to perform, ordered on increasing level in the tree.
     * @return The rewritten root.
     */
    private Node replaceAdjacents(Node n, int replaceIndex, VariableReplacement[] replacements) {
        // Path in the tree:
        // |
        // |
        // [ old var ] VariableReplacement[0]
        // [ new var ]
        // |
        // |
        // [ new var ] VariableReplacement[1]
        // [ old var ]
        // |
        // |
        // 0/1
        //
        if (replaceIndex >= replacements.length) {
            return n; // Beyond the last replacement.
        }

        if (n == Tree.ZERO || n == Tree.ONE) {
            return n; // Path is finished even if we are not.
        }

        int level = n.varInfo.level;
        while (level > replacements[replaceIndex].topLevel + 1) {
            // Below the levels for replacement[replaceIndex], none of it exists apparently, continue to the
            // next replacement.
            replaceIndex++;
            if (replaceIndex == replacements.length) {
                return n; // Reached the end of replacements.
            }
        }
        // Current conditions:
        // - replaceIndex < replacement.length (still variable replacements available to process).
        // - level <= replacements[replaceIndex].topLevel + 1 (at or above the next replacement).

        if (level < replacements[replaceIndex].topLevel) {
            // Above it, rebuild the node.
            Node[] childs = new Node[n.childs.length];
            for (int i = 0; i < n.childs.length; i++) {
                childs[i] = replaceAdjacents(n.childs[i], replaceIndex, replacements);
            }
            return addNode(n.varInfo, childs);
        }

        VariableReplacement vr = replacements[replaceIndex];
        if (level == vr.topLevel) {
            if (vr.isOldAtTop()) {
                // Old variable here, new variable at the child if it exists.
                Node[] childs = new Node[vr.oldVar.length];
                for (int i = 0; i < vr.oldVar.length; i++) {
                    // build new oldVar for index i by disjuncting all new vars for index i.
                    Node sub = Tree.ZERO;
                    for (Node c: n.childs) {
                        if (c == Tree.ONE || c == Tree.ZERO || c.varInfo.level > level + 1) {
                            // 'new' var level doesn't exist.
                            c = replaceAdjacents(c, replaceIndex + 1, replacements);
                        } else {
                            c = replaceAdjacents(c.childs[i], replaceIndex + 1, replacements);
                        }
                        sub = disjunct(sub, c);
                    }
                    childs[i] = sub;
                }
                return addNode(vr.oldVar, childs);
            } else {
                // New variable here, old variable at the child if it exists.
                Node[] childs = new Node[vr.oldVar.length];
                for (int i = 0; i < vr.oldVar.length; i++) {
                    // build new oldVar for index i
                    Node c = n.childs[i];
                    Node sub;
                    if (c == Tree.ONE || c == Tree.ZERO || c.varInfo.level > level + 1) {
                        // c is below old Var level.
                        sub = replaceAdjacents(c, replaceIndex + 1, replacements);
                    } else {
                        // c is old variable, eliminate it.
                        sub = Tree.ZERO;
                        for (Node subC: c.childs) {
                            subC = replaceAdjacents(subC, replaceIndex + 1, replacements);
                            sub = disjunct(sub, subC);
                        }
                    }
                    childs[i] = sub;
                }
                return addNode(vr.oldVar, childs);
            }
        }

        // level == replacements[replaceIndex].topLevel + 1 (topLevel apparently doesn't exist).
        Assert.check(level == vr.topLevel + 1);
        if (vr.isOldAtTop()) {
            // Only 'new' variable exists, relabel it as 'old'.
            Node[] childs = new Node[vr.oldVar.length];
            for (int i = 0; i < vr.oldVar.length; i++) {
                childs[i] = replaceAdjacents(n.childs[i], replaceIndex + 1, replacements);
            }
            return addNode(vr.oldVar, childs);
        } else {
            // Only 'old' variable exists, eliminate as 'new' variable is not relevant.
            Node result = Tree.ZERO;
            for (Node c: n.childs) {
                Node sub = replaceAdjacents(c, replaceIndex + 1, replacements);
                result = disjunct(result, sub);
            }
            return result;
        }
    }

    /**
     * Replace a variable by another variable with the same number of children, rebuilding the latter if it already
     * exists.
     *
     * <p>
     * The use-case for this function is to move updated variants like 'a+' back to their original 'a' variable.
     * </p>
     *
     * <p>
     * This method is highly generic, oldVar and newVar can be anywhere in the tree. The simpler case is when both
     * variables are on directly adjacent levels. That version can also handle several such variable replacements at the
     * same time. See {@link #adjacentReplacements} for details.
     * </p>
     *
     * @param n Expression to manipulate.
     * @param oldVar Original variable.
     * @param newVar Variable holding the update.
     * @return Result expression, with 'oldVar' holding the expressions that were in 'newVar', and removed 'newVar'.
     */
    public Node replace(Node n, VarInfo oldVar, VarInfo newVar) {
        // Conceptually, it copies the expression for each possible value
        // of the replaced variable:
        //
        // result = false
        // for value in domain(variable):
        // .. // Select a value of the new variable.
        // .. sub = assign(a, newVar, value)
        // .. // Eliminate the old variable in the sub-expression.
        // .. sub2 = disjunct([assign(sub, oldVar, val) for val in domain(variable)])
        // .. // Build a new sub-expression wit the same value for the old variable
        // .. sub3 = conjunct(sub2, buildEqualityValue(oldVar, value))
        // .. // and merge it into the overall tree.
        // .. result = disjunct([result, sub3)
        // return result
        //
        // In reality, all things are done at the same time. Two cases are
        // distinguished. Either the old variable is above the new variable in
        // the tree, or vice versa. Each case is treated separately.
        int upperLevel = Math.min(oldVar.level, newVar.level);
        int bottomLevel = Math.max(oldVar.level, newVar.level);
        if (upperLevel == oldVar.level) {
            return replaceOldNew(n, upperLevel, bottomLevel, oldVar);
        } else {
            return replaceNewOld(n, upperLevel, bottomLevel, oldVar);
        }
    }

    /**
     * Perform a general variable replacement in a tree from the new variable to the old variable. The old variable is
     * above the new variable in the tree.
     *
     * @param n Node to rebuild, performing the replacement.
     * @param oldLevel Level of the old variable in the tree.
     * @param newLevel Level of the new variable in the tree.
     * @param oldVar Variable information of the old variable.
     * @return The rebuild tree.
     */
    private Node replaceOldNew(Node n, int oldLevel, int newLevel, VarInfo oldVar) {
        // Picture of a path in the tree:
        // root
        // |
        // | --> case A
        // oldLevel --> case B
        // |
        // | --> case C
        // newLevel --> case D
        // |
        // | ..> case E
        // 0/1 --> case F
        //
        // Cases are distinguished and dealt with depending on the level of the node.

        // case F
        if (n == Tree.ONE || n == Tree.ZERO) {
            return n;
        }

        int level = n.varInfo.level;
        if (level > newLevel) {
            return n; // case E
        }

        if (level < oldLevel) { // case A
            Node[] childs = new Node[n.childs.length];
            for (int i = 0; i < n.childs.length; i++) {
                childs[i] = replaceOldNew(n.childs[i], oldLevel, newLevel, oldVar);
            }
            return addNode(n.varInfo, childs);
        }
        if (level == newLevel) { // case D
            // Very lucky, found the new variable while looking for the old one.
            // Copy the children, and done!
            Node[] childs = new Node[n.childs.length];
            System.arraycopy(n.childs, 0, childs, 0, n.childs.length);
            return addNode(oldVar, childs);
        }

        // level >= oldLevel && level < new_level, construct a fresh oldVar node,
        // and query the sub-expression for each value of the variable.
        Node[] childs = new Node[oldVar.length];
        if (level > oldLevel) { // case C
            for (int i = 0; i < oldVar.length; i++) {
                childs[i] = replaceOldNewValue(n, newLevel, i);
            }
            return addNode(oldVar, childs);
        }
        // case B, at oldLevel. This node must be eliminated. Ask the children instead.
        for (int i = 0; i < oldVar.length; i++) {
            Node subExpr = Tree.ZERO;
            for (Node c: n.childs) {
                subExpr = disjunct(subExpr, replaceOldNewValue(c, newLevel, i));
            }
            childs[i] = subExpr;
        }
        return addNode(oldVar, childs);
    }

    /**
     * Rebuild the bottom of the tree for the old variable above the new variable case, for a value of the new variable.
     *
     * @param n Node to rebuild.
     * @param newLevel Node level of the variable to eliminate in the tree.
     * @param newValue Value of the new variable to use in the elimination.
     * @return The rebuild tree.
     */
    private Node replaceOldNewValue(Node n, int newLevel, int newValue) {
        // Picture of a path in the tree for this function:
        //
        // |
        // | --> case C
        // newLevel --> case D
        // |
        // | ..> case E
        // 0/1 --> case F
        //
        // Only the bottom part of the path remains, where a specific index must
        // be picked at newLevel.

        // case F
        if (n == Tree.ONE || n == Tree.ZERO) {
            return n;
        }

        int level = n.varInfo.level;
        if (level > newLevel) {
            return n; // case E
        }

        if (level < newLevel) { // case C
            Node[] childs = new Node[n.childs.length];
            for (int i = 0; i < n.childs.length; i++) {
                childs[i] = replaceOldNewValue(n.childs[i], newLevel, newValue);
            }
            return addNode(n.varInfo, childs);
        }
        return n.childs[newValue]; // case D
    }

    /**
     * Perform a general variable replacement in a tree from the new variable to the old variable. The new variable is
     * above the old variable in the tree.
     *
     * @param n Node to rebuild, performing the replacement.
     * @param newLevel Level of the new variable in the tree.
     * @param oldLevel Level of the old variable in the tree.
     * @param oldVar Variable information of the old variable.
     * @return The rebuild tree.
     */
    private Node replaceNewOld(Node n, int newLevel, int oldLevel, VarInfo oldVar) {
        // Picture of a path in the tree:
        // root
        // |
        // | --> case A
        // newLevel --> case B
        // |
        // | --> case C
        // oldLevel --> case D
        // |
        // | ..> case E
        // 0/1 --> case F
        //
        // Cases are distinguished and dealt with depending on the level of the node.
        // Pretty much the same as the replaceOldNew function, except that old and
        // new level are swapped in the tree.

        // case F
        if (n == Tree.ONE || n == Tree.ZERO) {
            return n;
        }

        int level = n.varInfo.level;
        if (level > oldLevel) {
            return n; // case E
        }

        if (level < newLevel) { // case A
            Node[] childs = new Node[n.childs.length];
            for (int i = 0; i < n.childs.length; i++) {
                childs[i] = replaceNewOld(n.childs[i], newLevel, oldLevel, oldVar);
            }
            return addNode(n.varInfo, childs);
        }

        if (level == oldLevel) { // case D
            // There is no newVar in this path, its value is irrelevant apparently.
            // Merge childs of the old variable and done.
            Node result = Tree.ZERO;
            for (Node c: n.childs) {
                result = disjunct(result, c);
            }
            return result;
        }

        if (level == newLevel) { // case B
            // Eliminate the new variable, pushing the selected value down to
            // construct the old variable.
            Node result = Tree.ZERO;
            for (int i = 0; i < n.childs.length; i++) {
                Node sub = replaceNewOldValue(n.childs[i], oldLevel, oldVar, i);
                result = disjunct(result, sub);
            }
            return result;
        }

        // case C remaining.
        // There is no new level in the path, the oldVar should eliminate itself.
        // Denote that by passing the special value -1 down.
        return replaceNewOldValue(n, oldLevel, null, -1);
    }

    /**
     * Rebuild the bottom of the tree for the new variable above the old variable case, for a value of the old variable.
     *
     * @param n Node to rebuild.
     * @param oldLevel Node level of the variable to re-construct in the tree (if applicable).
     * @param oldVar Variable to re-construct in the tree (if applicable).
     * @param newValue Value of the old variable, or {@code -1} to denote that the old variable should not be
     *     constructed.
     * @return The rebuild tree.
     */
    private Node replaceNewOldValue(Node n, int oldLevel, VarInfo oldVar, int newValue) {
        // Picture of a path in the tree for this function:
        //
        // |
        // | --> case C
        // oldLevel --> case D
        // |
        // | ..> case E
        // 0/1 --> case F
        //
        // Only the bottom part of the path remains, where a specific index must
        // be constructed at oldLevel (except with the special index -1).

        // case F
        if (n == Tree.ONE) {
            if (newValue < 0) {
                return n; // Special new value indicating the variable is irrelevant.
            }
            return buildEqualityIndex(oldVar, newValue, n);
        }
        if (n == Tree.ZERO) {
            return n; // oldVar yields false for all its values here.
        }

        int level = n.varInfo.level;
        if (level > oldLevel) { // case E
            if (newValue < 0) {
                return n; // Special new value indicating the variable is irrelevant.
            }
            return buildEqualityIndex(oldVar, newValue, n);
        }

        if (level < oldLevel) { // case C
            Node[] childs = new Node[n.childs.length];
            for (int i = 0; i < n.childs.length; i++) {
                childs[i] = replaceNewOldValue(n.childs[i], oldLevel, oldVar, newValue);
            }
            return addNode(n.varInfo, childs);
        }

        // case D, at oldLevel.
        // Eliminate the node.
        Node result = Tree.ZERO;
        for (Node c: n.childs) {
            result = disjunct(result, c); // c is either case E or F.
        }

        // Build a new node at oldLevel if required.
        if (newValue < 0) {
            return result; // Special new value indicating the variable is irrelevant.
        }
        return buildEqualityIndex(oldVar, newValue, result);
    }
}
