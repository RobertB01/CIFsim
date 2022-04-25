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

import static org.eclipse.escet.common.dsm.Group.GroupType.BUS;
import static org.eclipse.escet.common.dsm.Group.GroupType.CLUSTER;
import static org.eclipse.escet.common.dsm.Group.GroupType.COLLECTION;
import static org.eclipse.escet.common.java.BitSets.bitset;

import java.util.BitSet;

import org.eclipse.escet.common.java.Assert;

/** Helper functions for DSMs. */
public class DsmHelper {
    /** Constructor of the static {@link DsmHelper} class. */
    private DsmHelper() {
        // Static class.
    }

    /**
     * Shuffle array according to a provided shuffle table.
     *
     * <p>Entry {@code i} containing {@code j} indicates that in the output on index {@code i} the value is
     * placed that is in the supplied array on index {@code j}.</p>
     *
     * @param <T> Element type of the array values.
     * @param array Array to shuffle.
     * @param shuffleTable Shuffle table, each entry {@code i} contains the original
     *      index of the node.
     * @return The shuffled labels.
     */
    public static <T> T[] shuffleArray(T[] array, int[] shuffleTable) {
        final int size = shuffleTable.length;
        Assert.check(array.length == size);

        // Clone is used the get a new generic array of the correct type without adding a new parameter to the method.
        T[] result = array.clone();
        for (int i = 0; i < size; i++) {
            result[i] = array[shuffleTable[i]];
        }
        return result;
    }

    /**
     * Get the nodes of a bus group.
     *
     * @param gr The group to analyze.
     * @return The bus nodes in the group.
     */
    public static BitSet getBusNodes(Group gr) {
        if (gr.groupType == BUS) {
            return gr.members;
        }

        if (gr.isElementary()) {
            return bitset();
        }

        BitSet result = bitset();
        for (Group child : gr.childGroups) {
            result.or(getBusNodes(child));
        }
        return result;
    }

    /**
     * Get the first group with a bus type.
     *
     * @param gr The root group to search in.
     * @return The first group with a bus type, or {@code null} if no such group is found.
     */
    public static Group getBusGroup(Group gr) {
        if (gr.groupType == BUS) {
            return gr;
        }
        if (gr.groupType == CLUSTER) {
            return null;
        }
        Assert.check(gr.groupType == COLLECTION);

        for (Group child : gr.childGroups) {
            if (child.groupType == BUS) {
                return child;
            }
        }

        // Bus group not found in this collection.
        return null;
    }

    /**
     * Get the first group with a cluster type.
     *
     * @param gr The root group to search in.
     * @return The first group with a cluster type, or {@code null} if no such group is found.
     */
    public static Group getNonbusGroup(Group gr) {
        if (gr.groupType == BUS) {
            return null;
        }
        if (gr.groupType == CLUSTER) {
            return gr;
        }
        Assert.check(gr.groupType == COLLECTION);

        for (Group child : gr.childGroups) {
            if (child.groupType == CLUSTER) {
                return child;
            }
        }

        // Non-bus group not found in this collection.
        return null;
    }
}
