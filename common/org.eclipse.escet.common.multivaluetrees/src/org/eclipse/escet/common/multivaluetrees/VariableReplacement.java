//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.java.Assert;

/**
 * Description of a variable replacement in a multi-value tree.
 *
 * <p>
 * Recommended usage is to make a {@code TreeSet<VariableReplacement>} and insert the created replacements in it. When
 * all replacements have been collected, convert it to an array, and the result is properly sorted for use in
 * {@link Tree#adjacentReplacements}.
 * </p>
 *
 * <p>
 * Check the {@link Tree} documentation how to retrieve {@link VarInfo} objects.
 * </p>
 */
public class VariableReplacement implements Comparable<VariableReplacement> {
    /** Variable to rebuild from 'newVar'. */
    public final VarInfo oldVar;

    /** Variable to use for rebuilding 'oldVar', must be eliminated from the tree. */
    public final VarInfo newVar;

    /** Level of the node that is encountered first for this replacement (the other level is at 'topLevel + 1'). */
    public final int topLevel;

    /**
     * Constructor of the {@link VariableReplacement} class.
     *
     * @param oldVar Variable to rebuild from 'newVar'.
     * @param newVar Variable to use for rebuilding 'oldVar', must be eliminated from the tree.
     */
    public VariableReplacement(VarInfo oldVar, VarInfo newVar) {
        Assert.check(oldVar.length == newVar.length);

        this.oldVar = oldVar;
        this.newVar = newVar;
        topLevel = Math.min(oldVar.level, newVar.level);

        int otherLevel = Math.max(oldVar.level, newVar.level);
        Assert.check(topLevel + 1 == otherLevel);
    }

    /**
     * Is the old variable at the top relative to the new variable?
     *
     * @return Whether the old variable is closer to the top of the tree.
     */
    public boolean isOldAtTop() {
        return topLevel == oldVar.level;
    }

    @Override
    public int compareTo(VariableReplacement other) {
        if (oldVar.level > other.oldVar.level) {
            return 1;
        }
        if (oldVar.level < other.oldVar.level) {
            return -1;
        }

        if (newVar.level > other.newVar.level) {
            return 1;
        }
        if (newVar.level < other.newVar.level) {
            return -1;
        }
        return 0;
    }
}
