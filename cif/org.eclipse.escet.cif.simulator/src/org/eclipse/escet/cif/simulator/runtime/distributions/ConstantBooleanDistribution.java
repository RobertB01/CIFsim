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

package org.eclipse.escet.cif.simulator.runtime.distributions;

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.boolToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

/**
 * Constant boolean stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: n/a</li>
 * <li>{@code v}: the value</li>
 * <li>Mean: {@code v}</li>
 * <li>Variance: {@code 0}</li>
 * </ul>
 *
 * <p>
 * Always returns the same value. Mostly used for debugging and testing. Also used as default value for boolean
 * stochastic distributions.
 * </p>
 */
public class ConstantBooleanDistribution extends BooleanDistribution {
    /** The constant value of the distribution. */
    private final boolean value;

    /** Constructor for the {@link ConstantBooleanDistribution} class. Uses {@code false} as the constant value. */
    public ConstantBooleanDistribution() {
        this(false);
    }

    /**
     * Constructor for the {@link ConstantBooleanDistribution} class.
     *
     * @param value The constant value of the distribution.
     */
    public ConstantBooleanDistribution(boolean value) {
        this.value = value;
    }

    @Override
    public BooleanDistribution copy() {
        return this;
    }

    @Override
    public boolean sample() {
        return value;
    }

    @Override
    public String toString() {
        return fmt("constant(%s)", boolToStr(value));
    }
}
