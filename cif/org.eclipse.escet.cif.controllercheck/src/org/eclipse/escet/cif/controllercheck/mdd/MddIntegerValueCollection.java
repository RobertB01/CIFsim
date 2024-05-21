//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.mdd;

import static org.eclipse.escet.common.java.Maps.map;
import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.Map;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.multivaluetrees.Node;
import org.eclipse.escet.common.multivaluetrees.Tree;

/** Class holding a collection of integer result values and the set of MDD nodes that produce them. */
public class MddIntegerValueCollection {
    /** Values and their MDD nodes. */
    public final Map<Integer, Node> valueNodes;

    /** Constructor of the {@link MddIntegerValueCollection} class with default number of entries in the collection. */
    public MddIntegerValueCollection() {
        valueNodes = map();
    }

    /**
     * Constructor of the {@link MddIntegerValueCollection} class with specified number of entries in the collection.
     *
     * @param numValues Expected number of values in the collection.
     */
    public MddIntegerValueCollection(int numValues) {
        valueNodes = mapc(numValues);
    }

    /**
     * Add a value and a node to the collection.
     *
     * @param tree MDD nodes store.
     * @param value Result value associated with 'node'.
     * @param node MDD node expressing the conditions when the value is reached.
     */
    public void addValue(Tree tree, Integer value, Node node) {
        Node n = valueNodes.get(value);
        if (n != null) {
            node = tree.disjunct(node, n);
        }
        valueNodes.put(value, node);
    }

    /**
     * Wrapper to query an MDD node associated with a value from the collection.
     *
     * @param value Value to query.
     * @return Associated MDD node of the queried value. Is {@code null} if the value does not exist in the collection.
     */
    public Node get(Integer value) {
        return valueNodes.get(value);
    }

    /**
     * Wrapper to query an MDD node associated with a value from the collection that is known to exist.
     *
     * @param value Value to query.
     * @return Associated MDD node of the queried value. Will never return {@code null}.
     */
    public Node getExist(Integer value) {
        Node n = get(value);
        Assert.notNull(n);
        return n;
    }

    /**
     * Get the number of values of the collection.
     *
     * @return Number of values in the collection.
     */
    public int size() {
        return valueNodes.size();
    }
}
