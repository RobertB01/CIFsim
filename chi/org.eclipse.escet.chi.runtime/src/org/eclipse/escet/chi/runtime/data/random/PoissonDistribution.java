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

package org.eclipse.escet.chi.runtime.data.random;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Poisson stochastic distribution class.
 *
 * <p>
 * Poisson(lambda), lambda &gt; 0
 * <ul>
 * <li>Mean: lambda</li>
 * <li>Variance: lambda</li>
 * </ul>
 * </p>
 */
public class PoissonDistribution extends IntegerDistribution {
    /** Internal random generator. */
    private RandomGenerator randGen;

    /** Mean value. */
    private double lambda;

    /**
     * Constructor of the {@link PoissonDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param lambda Mean value of the distribution.
     */
    public PoissonDistribution(ChiCoordinator chiCoordinator, double lambda) {
        super(chiCoordinator);
        if (lambda <= 0.0) {
            String msg = fmt(
                    "The Poisson distribution expects the lambda parameter to be bigger than 0, found %s instead.",
                    lambda);
            throw new ChiSimulatorException(msg);
        }

        this.lambda = lambda;
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public int sample() {
        return drawPoisson(randGen, lambda);
    }

    /**
     * Draw a sample value from a poisson distribution.
     *
     * @param randGen Random generator to use (and update).
     * @param lambda Mean value of the poisson distribution.
     * @return A sample of the poisson distribution.
     */
    public static int drawPoisson(RandomGenerator randGen, double lambda) {
        // TODO This generator is slow for larger lambda, find a faster one.

        double e = Math.exp(-lambda);
        double p = 1.0;
        int r = -1;
        while (p > e) {
            p *= randGen.drawNonzero();
            r++;
        }
        return r;
    }
}
