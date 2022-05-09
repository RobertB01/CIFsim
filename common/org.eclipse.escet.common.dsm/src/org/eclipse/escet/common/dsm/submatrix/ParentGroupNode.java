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

package org.eclipse.escet.common.dsm.submatrix;

import org.eclipse.escet.common.dsm.Group;

/** A previously found group in the parent represented by a single node. */
public class ParentGroupNode implements SubNode {
    /** Group of parent nodes represented by this sub-node. */
    private final Group group;

    /**
     * Sub node representing a group of parent nodes.
     *
     * @param parentGroup Group of parent nodes represented by this sub-node.
     */
    public ParentGroupNode(Group parentGroup) {
        this.group = parentGroup;
    }

    @Override
    public int firstParentNode(int startNode) {
        return group.members.nextSetBit(startNode);
    }

    @Override
    public Group getGroup() {
        return group;
    }
}
