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
 * Lognormal stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: {@code LN(a, b)}</li>
 * <li>{@code a}: the mean value</li>
 * <li>{@code b}: the variance, {@code b > 0}</li>
 * <li>Mean: {@code exp(a + (b / 2))}</li>
 * <li>Variance: {@code exp(2 * a + b) * (exp(b) - 1)}</li>
 * </ul>
 */
public class LogNormalDistribution extends NormalDistribution {
    /**
     * Constructor for the {@link LogNormalDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param mean The mean value of the distribution.
     * @param variance The variance of the distribution.
     * @throws CifSimulatorException If {@code variance <= 0}.
     */
    public LogNormalDistribution(CifRandomGenerator randGen, double mean, double variance) {
        // We check the variance via a function, to ensure we check it here
        // first, before we check it in the normal distribution. This is to
        // ensure we get the error message for this distribution, and not for
        // the normal distribution.
        super(randGen, mean, checkVariance(mean, variance));
    }

    /**
     * Checks the variance.
     *
     * @param mean The mean value of the distribution.
     * @param variance The variance of the distribution.
     * @return The variance of the distribution.
     * @throws CifSimulatorException If {@code variance <= 0}.
     */
    private static double checkVariance(double mean, double variance) {
        if (variance <= 0.0) {
            String msg1 = fmt("Invalid operation: lognormal(%s, %s).", realToStr(mean), realToStr(variance));
            String msg2 = "The variance parameter (the second parameter) is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }

        return variance;
    }

    /**
     * Constructor for the {@link LogNormalDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private LogNormalDistribution(LogNormalDistribution distribution) {
        super(distribution);
    }

    @Override
    public RealDistribution copy() {
        return new LogNormalDistribution(this);
    }

    @Override
    public double sample() {
        return Math.exp(super.sample());
    }

    @Override
    public String toString() {
        return fmt("lognormal(%s, %s)", realToStr(mean), realToStr(variance));
    }
}
