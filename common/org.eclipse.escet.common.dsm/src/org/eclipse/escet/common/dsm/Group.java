//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm;

import static org.eclipse.escet.common.java.BitSets.bitset;

import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.java.Assert;

/**
 * Class for storing nested groups (subsets) of nodes.
 *
 * <p>
 * Elementary groups are constructed with {@link #Group(GroupType groupType, BitSet members)} while groups containing
 * other groups are constructed with {@link #Group(GroupType groupType, BitSet localNodes, List childGroups)}. In the
 * latter case, child groups may not overlap.
 * </p>
 *
 * <p>
 * The group does not enforce any inherent member properties other than non-overlapping at the same level. In
 * particular, there may be holes in a group and/or between groups, and a group does not need to be a single block of
 * consecutive nodes.
 * </p>
 */
public class Group {
    /** Kind of group. */
    public final GroupType groupType;

    /**
     * Member nodes of the group and its children (should not be modified).
     *
     * <p>
     * Union of the child groups and the local nodes.
     * </p>
     */
    public final BitSet members;

    /** Local nodes of the group, may be {@code null}. */
    public final BitSet localNodes;

    /** Child groups (cannot be modified). */
    public final List<Group> childGroups;

    /** First node number of this group after ordering the groups, else non-positive. */
    private int shuffledBase = -1;

    /**
     * Constructor for an elementary {@link Group} class.
     *
     * @param groupType Kind of group.
     * @param localNodes Local nodes that belong to the group, may be {@code null}.
     */
    public Group(GroupType groupType, BitSet localNodes) {
        this(groupType, localNodes, Collections.emptyList());
    }

    /**
     * Constructor for a combined {@link Group} class.
     *
     * @param groupType Kind of group.
     * @param localNodes Local nodes that belong to the group, may be {@code null}.
     * @param childGroups Child groups that belong to this group.
     */
    public Group(GroupType groupType, BitSet localNodes, List<Group> childGroups) {
        this.groupType = groupType;
        this.localNodes = localNodes;
        this.childGroups = childGroups;

        this.members = bitset();
        if (localNodes != null) {
            this.members.or(localNodes);
        }
        for (Group childGrp: childGroups) {
            this.members.or(childGrp.members);
        }
    }

    /** Dump the group onto the debug output stream. */
    public void dbgDump() {
        dbgDump("    ");
    }

    /**
     * Dump the group onto the debug output stream.
     *
     * @param indent The amount of indentation to use as prefix.
     */
    public void dbgDump(String indent) {
        if (!OutputProvider.dodbg()) {
            return;
        }

        OutputProvider.dbg("%s- Grouptype %s", indent, groupType.name());
        OutputProvider.dbg("%s  Local nodes: %s", indent, (localNodes == null) ? "<none>" : localNodes.toString());
        for (Group child: childGroups) {
            child.dbgDump(indent + "    ");
        }
    }

    /**
     * Assign the index of the first node of the group after settling its place in the nodes.
     *
     * @param base Base index number of this group.
     */
    public void setShuffledBase(int base) {
        Assert.check(base >= 0);
        shuffledBase = base;
    }

    /**
     * Get the index of the first node of this group. Returns {@code -1} if the base position has not been settled yet.
     *
     * @return Index of the first node after shuffling it to the right spot.
     */
    public int getShuffledBase() {
        return shuffledBase;
    }

    /**
     * Get the number of nodes covered by this group after settling its position in the nodes.
     *
     * @return {@code -1} if the position has not yet been settled, else the number of nodes covered by this group.
     */
    public int getShuffledSize() {
        if (shuffledBase < 0) {
            return -1;
        }
        return members.cardinality();
    }

    /**
     * Return whether this is a leaf group.
     *
     * @return {@code true} if the group is an elementary group, {@code false} if it is a composition of child groups.
     */
    public boolean isElementary() {
        return childGroups.isEmpty();
    }

    /** Available kinds of groups. */
    public static enum GroupType {
        /** Group forms a bus. */
        BUS,

        /** Group forms a cluster. */
        CLUSTER,

        /** Group has no relation. */
        COLLECTION,
    }
}
