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

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;

/**
 * Normal stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: {@code N(a, b)}</li>
 * <li>{@code a}: the mean value</li>
 * <li>{@code b}: the variance, {@code b > 0}</li>
 * <li>Mean: {@code a}</li>
 * <li>Variance: {@code b}</li>
 * </ul>
 */
public class NormalDistribution extends RealDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The mean value of the distribution. */
    protected final double mean;

    /** The variance of the distribution. */
    protected final double variance;

    /** The square root of the variance of the distribution. */
    protected final double sqrtVariance;

    /** The cached value from the sample generation process. */
    private double cachedValue;

    /** The cached {@code y} value. */
    private double cachedY;

    /** Is the cache filled? Only if the value is {@code true}, are {@link #cachedValue} and {@link #cachedY} valid. */
    private boolean cacheFilled = false;

    /**
     * Constructor for the {@link NormalDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param mean The mean value of the distribution.
     * @param variance The variance of the distribution.
     * @throws CifSimulatorException If {@code variance <= 0}.
     */
    public NormalDistribution(CifRandomGenerator randGen, double mean, double variance) {
        this.randGen = randGen;
        this.mean = mean;
        this.variance = variance;
        this.sqrtVariance = Math.sqrt(variance);

        if (variance <= 0.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The variance parameter (the second parameter) is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link NormalDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    protected NormalDistribution(NormalDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.mean = distribution.mean;
        this.variance = distribution.variance;
        this.sqrtVariance = distribution.sqrtVariance;
        this.cachedValue = distribution.cachedValue;
        this.cachedY = distribution.cachedY;
        this.cacheFilled = distribution.cacheFilled;
    }

    @Override
    public RealDistribution copy() {
        return new NormalDistribution(this);
    }

    @Override
    public double sample() {
        // Use cache, if available.
        if (cacheFilled) {
            cacheFilled = false;
            return mean + sqrtVariance * cachedValue * cachedY;
        }

        // Compute new values, and cache one of them.
        double s1;
        double s2;
        double w;
        do {
            s1 = 2 * randGen.draw() - 1;
            s2 = 2 * randGen.draw() - 1;
            w = s1 * s1 + s2 * s2;
        } while (w >= 1.0 || w == 0.0);

        cachedY = Math.sqrt(-2 * Math.log(w) / w);
        cachedValue = s2;
        cacheFilled = true;

        return mean + sqrtVariance * s1 * cachedY;
    }

    @Override
    public String toString() {
        return fmt("normal(%s, %s)", realToStr(mean), realToStr(variance));
    }
}
