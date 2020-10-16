//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.eventbased.apps.conversion;

import org.eclipse.escet.cif.metamodel.cif.Group;

/** Class for building a tree of groups. */
public class GroupTree {
    /** Parent group. */
    private final Group parent;

    /** Child name. */
    private final String name;

    /**
     * Constructor of the {@link GroupTree} class.
     *
     * @param parent Parent group.
     * @param name Child name.
     */
    public GroupTree(Group parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GroupTree)) {
            return false;
        }
        GroupTree other = (GroupTree)obj;
        return parent == other.parent && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        if (parent != null) {
            return parent.hashCode() + name.hashCode() * 3;
        }
        return name.hashCode();
    }
}
