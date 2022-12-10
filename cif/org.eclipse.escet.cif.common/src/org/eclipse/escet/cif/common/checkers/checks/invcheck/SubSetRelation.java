//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common.checkers.checks.invcheck;

import java.util.EnumSet;

import org.eclipse.escet.common.java.Assert;

/** Values to express a relation between two (sub)sets. */
public enum SubSetRelation {
    /** Both sides have the same values. */
    EQUAL(false, false),

    /** The left side of the comparison has at least one element that is not available at the right side. */
    LEFT_LARGER(true, false),

    /** The right side of the comparison has at least one element that is not available at the left side. */
    RIGHT_LARGER(false, true),

    /** Both the left and the right side of the comparison have at least one value that the other side does not have. */
    BOTH_LARGER(true, true);

    /** Whether the left side of the comparison was larger (has at least one value that is not in the right side). */
    public final boolean leftLarger;

    /** Whether the right side of the comparison was larger (has at least one value that is not in the left side). */
    public final boolean rightLarger;

    /**
     * Constructor of the {@link SubSetRelation} enum class.
     *
     * @param leftLarger Whether the left side of the comparison was larger (has at least one value that is not in the
     *     right side).
     * @param rightLarger Whether the right side of the comparison was larger (has at least one value that is not in the
     *     left side).
     */
    private SubSetRelation(boolean leftLarger, boolean rightLarger) {
        this.leftLarger = leftLarger;
        this.rightLarger = rightLarger;
    }

    /**
     * Obtain a {@link SubSetRelation} enumeration literal from the given relation properties.
     *
     * @param leftLarger Whether the left side of the comparison was larger (has at least one value that is not in the
     *     right side).
     * @param rightLarger Whether the right side of the comparison was larger (has at least one value that is not in the
     *     left side).
     * @return The requested enumeration literal.
     */
    public static SubSetRelation getRelation(boolean leftLarger, boolean rightLarger) {
        SubSetRelation literal = values()[(leftLarger ? 1 : 0) + (rightLarger ? 2 : 0)];
        Assert.areEqual(literal.leftLarger, leftLarger);
        Assert.areEqual(literal.rightLarger, rightLarger);
        return literal;
    }

    /**
     * Compare the left side with the right side, and decide which of the sets are larger.
     *
     * @param <E> Enumeration type used in the comparison.
     * @param left Left side to compare.
     * @param right Right side to compare.
     * @return Which of the sets are larger
     */
    public static <E extends Enum<E>> SubSetRelation compare(EnumSet<E> left, EnumSet<E> right) {
        EnumSet<E> copy;
        copy = EnumSet.copyOf(left);
        copy.removeAll(right);
        boolean leftLarger = !copy.isEmpty();

        copy = EnumSet.copyOf(right);
        copy.removeAll(left);
        boolean rightLarger = !copy.isEmpty();

        return getRelation(leftLarger, rightLarger);
    }
}
