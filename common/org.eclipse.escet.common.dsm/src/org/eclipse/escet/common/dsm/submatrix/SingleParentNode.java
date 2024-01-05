//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm.submatrix;

import org.eclipse.escet.common.dsm.Group;

/** A single node in the parent, which is also a single sub node. */
public class SingleParentNode implements SubNode {
    /** Parent node represented by this sub-node. */
    private final int parentNode;

    /**
     * Constructor of the {@link SingleParentNode} class.
     *
     * @param parentNode Parent node represented by this sub-node.
     */
    public SingleParentNode(int parentNode) {
        this.parentNode = parentNode;
    }

    @Override
    public int firstParentNode(int startNode) {
        if (startNode >= 0 && startNode <= parentNode) {
            return parentNode;
        }
        return -1;
    }

    @Override
    public Group getGroup() {
        return null;
    }
}
