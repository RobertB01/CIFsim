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

package org.eclipse.escet.cif.simulator.runtime.distributions;

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.intToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;

/**
 * Uniform integer stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: {@code DU(a, b - 1)}</li>
 * <li>{@code a}: the lower bound (inclusive), {@code a < b}</li>
 * <li>{@code b}: the upper bound (exclusive), {@code a < b}</li>
 * <li>Mean: {@code (a + b - 1) / 2}</li>
 * <li>Variance: {@code ((b - a)^2 - 1) / 12}</li>
 * </ul>
 */
public class UniformIntegerDistribution extends IntegerDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The lower bound {@code a} (inclusive). */
    private final int lower;

    /** The upper bound {@code b} (exclusive). */
    private final int upper;

    /**
     * Constructor for the {@link UniformIntegerDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param lower The lower bound {@code a} (inclusive).
     * @param upper The upper bound {@code b} (exclusive).
     * @throws CifSimulatorException If {@code lower >= upper}.
     */
    public UniformIntegerDistribution(CifRandomGenerator randGen, int lower, int upper) {
        this.randGen = randGen;
        this.lower = lower;
        this.upper = upper;

        if (lower >= upper) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The lower bound (the first parameter) is not smaller than the upper bound (the second "
                    + "parameter).";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link UniformIntegerDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private UniformIntegerDistribution(UniformIntegerDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.lower = distribution.lower;
        this.upper = distribution.upper;
    }

    @Override
    public IntegerDistribution copy() {
        return new UniformIntegerDistribution(this);
    }

    @Override
    public int sample() {
        int range = upper - lower;
        return lower + (int)(range * randGen.draw());
    }

    @Override
    public String toString() {
        return fmt("uniform(%s, %s)", intToStr(lower), intToStr(upper));
    }
}
