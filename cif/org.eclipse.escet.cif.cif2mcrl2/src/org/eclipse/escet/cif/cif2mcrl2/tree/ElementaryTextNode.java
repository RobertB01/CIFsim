//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

/** Leaf node in the text node tree. */
public class ElementaryTextNode extends TextNode {
    /** Name stored in the leaf. */
    public final String name;

    /**
     * Constructor of the {@link ElementaryTextNode} class.
     *
     * @param name Name to store.
     */
    public ElementaryTextNode(String name) {
        this.name = name;
    }
}
