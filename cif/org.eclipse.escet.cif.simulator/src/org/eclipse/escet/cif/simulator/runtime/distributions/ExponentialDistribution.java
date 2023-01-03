//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;

/**
 * Negative exponential stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: {@code expo(b)}</li>
 * <li>{@code b}: the scale parameter {@code b}, {@code b > 0}</li>
 * <li>Mean: {@code b}</li>
 * <li>Variance: {@code b * b}</li>
 * </ul>
 */
public class ExponentialDistribution extends RealDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The scale parameter of the distribution. */
    private final double b;

    /**
     * Constructor for the {@link ExponentialDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param b The scale parameter of the distribution.
     * @throws CifSimulatorException If {@code b <= 0}.
     */
    public ExponentialDistribution(CifRandomGenerator randGen, double b) {
        this.randGen = randGen;
        this.b = b;

        if (b <= 0.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The scale parameter is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link ExponentialDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private ExponentialDistribution(ExponentialDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.b = distribution.b;
    }

    @Override
    public RealDistribution copy() {
        return new ExponentialDistribution(this);
    }

    @Override
    public double sample() {
        return sample(randGen, b);
    }

    /**
     * Take a sample from the negative exponential stochastic distribution.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param b The scale parameter of the distribution
     * @return The sampled value.
     */
    public static double sample(CifRandomGenerator randGen, double b) {
        return -b * Math.log(1 - randGen.draw());
    }

    @Override
    public String toString() {
        return fmt("exponential(%s)", realToStr(b));
    }
}
