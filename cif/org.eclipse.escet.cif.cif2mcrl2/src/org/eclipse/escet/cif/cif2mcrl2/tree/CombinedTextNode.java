//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.cif2mcrl2.tree;

import java.util.List;

import org.eclipse.escet.common.java.Assert;

/** Text node containing one or more child text nodes. */
public class CombinedTextNode extends TextNode {
    /** Children of this combined node. */
    public final List<TextNode> children;

    /**
     * Constructor of the {@link CombinedTextNode} class.
     *
     * @param children Children of the node.
     */
    public CombinedTextNode(List<TextNode> children) {
        this.children = children;
        Assert.check(!children.isEmpty());
    }
}
