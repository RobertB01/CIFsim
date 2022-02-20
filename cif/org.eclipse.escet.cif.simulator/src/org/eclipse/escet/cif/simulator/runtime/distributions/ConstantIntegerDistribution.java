//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.intToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

/**
 * Constant integer stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: n/a</li>
 * <li>{@code v}: the value</li>
 * <li>Mean: {@code v}</li>
 * <li>Variance: {@code 0}</li>
 * </ul>
 *
 * <p>
 * Always returns the same value. Mostly used for debugging and testing. Also used as default value for integer
 * stochastic distributions.
 * </p>
 */
public class ConstantIntegerDistribution extends IntegerDistribution {
    /** The constant value of the distribution. */
    private final int value;

    /** Constructor for the {@link ConstantIntegerDistribution} class. Uses {@code 0} as the constant value. */
    public ConstantIntegerDistribution() {
        this(0);
    }

    /**
     * Constructor for the {@link ConstantIntegerDistribution} class.
     *
     * @param value The constant value of the distribution.
     */
    public ConstantIntegerDistribution(int value) {
        this.value = value;
    }

    @Override
    public IntegerDistribution copy() {
        return this;
    }

    @Override
    public int sample() {
        return value;
    }

    @Override
    public String toString() {
        return fmt("constant(%s)", intToStr(value));
    }
}
