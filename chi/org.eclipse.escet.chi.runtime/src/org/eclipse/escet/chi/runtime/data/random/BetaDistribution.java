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

package org.eclipse.escet.chi.runtime.data.random;

import static org.eclipse.escet.chi.runtime.data.random.GammaDistribution.drawGamma;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Class for the stochastic Beta distribution.
 *
 * <p>
 * BETA(p, q)
 * <ul>
 * <li>Mean: p / (p + q)</li>
 * <li>Variance: p * q / ((p + q) * (p + q) * (p + q + 1))</li>
 * </ul>
 * </p>
 */
public class BetaDistribution extends DoubleDistribution {
    /** First internal random number generator. */
    private RandomGenerator randGen1;

    /** Second internal random number generator. */
    private RandomGenerator randGen2;

    /** Third internal random number generator. */
    private RandomGenerator randGen3;

    /** Fourth internal random number generator. */
    private RandomGenerator randGen4;

    /** Shape parameter p. */
    private double p;

    /** Shape parameter q. */
    private double q;

    /**
     * Constructor of the {@link BetaDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param p Shape parameter p.
     * @param q Shape parameter q.
     */
    public BetaDistribution(ChiCoordinator chiCoordinator, double p, double q) {
        super(chiCoordinator);
        if (p <= 0.0) {
            String msg = fmt("The Beta distribution expects a shape parameter p bigger than 0, found %s instead.", p);
            throw new ChiSimulatorException(msg);
        }
        if (q <= 0.0) {
            String msg = fmt("The Beta distribution expects a shape parameter q bigger than 0, found %s instead.", q);
            throw new ChiSimulatorException(msg);
        }

        this.p = p;
        this.q = q;
        randGen1 = chiCoordinator.getFreshGenerator();
        randGen2 = chiCoordinator.getFreshGenerator();
        randGen3 = chiCoordinator.getFreshGenerator();
        randGen4 = chiCoordinator.getFreshGenerator();
    }

    @Override
    public double sample() {
        return drawBeta(randGen1, randGen2, randGen3, randGen4, p, q);
    }

    /**
     * Draw a sample from a beta distribution.
     *
     * @param randGen1 First random number generator.
     * @param randGen2 Second random number generator.
     * @param randGen3 Third random number generator.
     * @param randGen4 Fourth random number generator.
     * @param p Shape parameter p.
     * @param q Shape parameter q.
     * @return Sample value of the beta distribution.
     */
    public static double drawBeta(RandomGenerator randGen1, RandomGenerator randGen2, RandomGenerator randGen3,
            RandomGenerator randGen4, double p, double q)
    {
        double g1 = drawGamma(randGen1, randGen2, p, 1);
        double g2 = drawGamma(randGen3, randGen4, q, 1);
        return g1 / (g1 + g2);
    }
}
