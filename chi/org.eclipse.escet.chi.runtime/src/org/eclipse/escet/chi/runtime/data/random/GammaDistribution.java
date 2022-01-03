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

package org.eclipse.escet.chi.runtime.data.random;

import static org.eclipse.escet.chi.runtime.data.random.ExponentialDistribution.drawExponential;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Stochastic gamma distribution.
 *
 * <p>
 * Gamma(a, b)
 * <ul>
 * <li>Mean: a * b</li>
 * <li>Variance: a * b * b</li>
 * </ul>
 * </p>
 */
public class GammaDistribution extends DoubleDistribution {
    /** First internal random number generator. */
    private RandomGenerator randGen1;

    /** Second internal random number generator. */
    private RandomGenerator randGen2;

    /** Shape parameter. */
    private double shape;

    /** Scale parameter. */
    private double scale;

    /**
     * Constructor for the {@link GammaDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param shape Shape parameter.
     * @param scale Scale parameter.
     */
    public GammaDistribution(ChiCoordinator chiCoordinator, double shape, double scale) {
        super(chiCoordinator);
        if (shape <= 0.0) {
            String msg = fmt(
                    "The Gamma distribution expects the shape parameter to be bigger than 0, found %s instead.", shape);
            throw new ChiSimulatorException(msg);
        }
        if (scale <= 0.0) {
            String msg = fmt(
                    "The Gamma distribution expects the scale parameter to be bigger than 0, found %s instead.", scale);
            throw new ChiSimulatorException(msg);
        }

        this.shape = shape;
        this.scale = scale;
        randGen1 = chiCoordinator.getFreshGenerator();
        randGen2 = chiCoordinator.getFreshGenerator();
    }

    @Override
    public double sample() {
        return drawGamma(randGen1, randGen2, shape, scale);
    }

    /**
     * Compute a sample for the gamma distribution.
     *
     * @param randGen1 First random generator to use (and update).
     * @param randGen2 Second random generator to use (and update).
     * @param shape Shape parameter.
     * @param scale Scale parameter.
     * @return Sample of the gamma distribution.
     */
    public static double drawGamma(RandomGenerator randGen1, RandomGenerator randGen2, double shape, double scale) {
        // shape < 1
        if (shape < 1.0) {
            double beta = (Math.E + shape) / Math.E;
            while (true) {
                double w = beta * randGen1.draw();
                double v = randGen2.draw();
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

        // Gamma(1, scale) = EXP(scale)
        if (shape == 1.0) {
            return drawExponential(randGen1, scale);
        }

        // Else, shape > 1
        double alpha = 1 / Math.sqrt(2 * shape - 1);
        double beta = shape - Math.log(4.0);
        double gamma = shape + 1 / alpha;
        double delta = 1 + Math.log(4.5);
        while (true) {
            double u1 = randGen1.drawNonzero();
            double v = alpha * Math.log(u1 / (1 - u1));
            double y = shape * Math.exp(v);
            double z = u1 * u1 * randGen2.drawNonzero();
            double w = beta + gamma * v - y;
            if (w + delta - 4.5 * z >= 0) {
                return scale * y;
            }
            if (w >= Math.log(z)) {
                return scale * y;
            }
        }
    }
}
