//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
 * Gamma stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: Gamma(a, b)</li>
 * <li>{@code a}: the shape parameter, {@code a > 0}</li>
 * <li>{@code b}: the scale parameter, {@code b > 0}</li>
 * <li>Mean: {@code a * b}</li>
 * <li>Variance: {@code a * b^2}</li>
 * </ul>
 */
public class GammaDistribution extends RealDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The shape parameter. */
    private final double a;

    /** The scale parameter. */
    private final double b;

    /**
     * Constructor for the {@link GammaDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param a The shape parameter.
     * @param b The scale parameter.
     * @throws CifSimulatorException If {@code a <= 0} or {@code b <= 0}.
     */
    public GammaDistribution(CifRandomGenerator randGen, double a, double b) {
        this.randGen = randGen;
        this.a = a;
        this.b = b;

        if (a <= 0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The shape parameter (the first parameter) is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }

        if (b <= 0.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The scale parameter (the second parameter) is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link GammaDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private GammaDistribution(GammaDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.a = distribution.a;
        this.b = distribution.b;
    }

    @Override
    public RealDistribution copy() {
        return new GammaDistribution(this);
    }

    @Override
    public double sample() {
        return sample(randGen, a, b);
    }

    /**
     * Take a sample from the gamma stochastic distribution.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param a The shape parameter.
     * @param b The scale parameter.
     * @return The sampled value.
     */
    public static double sample(CifRandomGenerator randGen, double a, double b) {
        // Rename parameters.
        double shape = a;
        double scale = b;

        // shape < 1
        if (shape < 1.0) {
            double beta = (Math.E + shape) / Math.E;
            while (true) {
                double w = beta * randGen.draw();
                double v = randGen.draw();
                if (w < 1) {
                    double y = Math.pow(w, 1 / shape);
                    if (v <= Math.exp(-y)) {
                        return scale * y;
                    }
                } else {
                    double y = -Math.log((beta - w) / shape);
                    if (v <= Math.pow(y, shape - 1)) {
                        return scale * y;
                    }
                }
            }
        }

        // gamma(1, scale) = exponential(scale)
        if (shape == 1.0) {
            return ExponentialDistribution.sample(randGen, scale);
        }

        // Else, shape > 1.
        double alpha = 1 / Math.sqrt(2 * shape - 1);
        double beta = shape - Math.log(4.0);
        double gamma = shape + 1 / alpha;
        double delta = 1 + Math.log(4.5);
        while (true) {
            double u1 = randGen.drawNonZero();
            double v = alpha * Math.log(u1 / (1 - u1));
            double y = shape * Math.exp(v);
            double z = u1 * u1 * randGen.drawNonZero();
            double w = beta + gamma * v - y;
            if (w + delta - 4.5 * z >= 0) {
                return scale * y;
            }
            if (w >= Math.log(z)) {
                return scale * y;
            }
        }
    }

    @Override
    public String toString() {
        return fmt("gamma(%s, %s)", realToStr(a), realToStr(b));
    }
}
