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

/** Interface for getting parent node and group information in sub-matrix context. */
public interface SubNode {
    /**
     * Return the first node in the parent matrix related to this node which is at least <em>startNode</em>.
     *
     * @param startNode Parent node to start the search.
     * @return First parent node at or beyond <em>startNode</em> or {@code -1} if there is no such parent node.
     */
    public int firstParentNode(int startNode);

    /**
     * Get the group related to the sub-node.
     *
     * @return Group related to the sub-node, or {@code null} if the node doesn't have a group.
     */
    public Group getGroup();
}
