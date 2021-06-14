//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.solver;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.java.Assert;

/**
 * Equality relation between two variables.
 *
 * <p>
 * The instance represents {@link #a} + {@link #offset} == {@link #b}.
 * </p>
 */
public class EqRelation extends VariableRelation {
    /** Variable 'a'. */
    public final Variable a;

    /** Variable 'b'. */
    public final Variable b;

    /** Offset between the variables, {@link #b} - {@link #a}. */
    public final double offset;

    /**
     * Constructor of the {@link EqRelation} class.
     *
     * <p>
     * Instance represents equality relation {@link #a} + {@link #offset} == {@link #b}.
     * </p>
     *
     * @param a Variable 'a'.
     * @param offset Offset between the variables, {@link #b} - {@link #a}.
     * @param b Variable 'b'.
     */
    public EqRelation(Variable a, double offset, Variable b) {
        this.a = a;
        this.b = b;
        this.offset = offset;

        Assert.check(a != b);
    }

    @Override
    public String toString() {
        if (offset == 0.0) {
            return fmt("Eq[%s == %s]", a, b);
        }
        return fmt("Eq[%s + %.2f == %s]", a, offset, b);
    }
}
