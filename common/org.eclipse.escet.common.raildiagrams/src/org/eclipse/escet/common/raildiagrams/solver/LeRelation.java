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
 * Less or equal relation between two variables.
 *
 * <p>
 * The instance represents {@link #a} + {@link #lowBound} &lt;= {@link #b}.
 * </p>
 */
public class LeRelation extends VariableRelation {
    /** Variable 'a'. */
    public final Variable a;

    /** Variable 'b'. */
    public final Variable b;

    /** Lower-bound offset between the variables, at least {@link #b} - {@link #a}. */
    public final double lowBound;

    /**
     * Constructor of the {@link LeRelation} class.
     *
     * <p>
     * Instance represents equality relation {@link #a} + {@link #lowBound} &lt;= {@link #b}.
     * </p>
     *
     * @param a Variable 'a'.
     * @param lowBound Offset between the variables, is at least {@link #b} - {@link #a}.
     * @param b Variable 'b'.
     */
    public LeRelation(Variable a, double lowBound, Variable b) {
        this.a = a;
        this.b = b;
        this.lowBound = lowBound;

        Assert.check(a != b);
    }

    @Override
    public String toString() {
        if (lowBound == 0.0) {
            return fmt("Lt[%s <= %s]", a, b);
        }
        return fmt("Lt[%s + %.2f <= %s]", a, lowBound, b);
    }
}