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
 * Stochastic geometric distribution (number of failures before first success).
 *
 * <p>
 * geom(p), with p the chance of success, 0 &lt; p &lt;= 1.
 * <ul>
 * <li>Mean: (1 - p) / p</li>
 * <li>Variance: (1 - p) / (p * p)</li>
 * </ul>
 * </p>
 */
public class GeometricDistribution extends IntegerDistribution {
    /** Internal random generator. */
    private RandomGenerator randGen;

    /** Chance of success. */
    private double chance;

    /**
     * Constructor of the {@link GeometricDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param chance Chance of success.
     */
    public GeometricDistribution(ChiCoordinator chiCoordinator, double chance) {
        super(chiCoordinator);
        if (chance <= 0.0 || chance > 1.0) {
            String msg = fmt("The Geometric distribution expects the chance parameter to be in the range (0.0..1.0], "
                    + "found value %s instead.", chance);
            throw new ChiSimulatorException(msg);
        }

        this.chance = chance;
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public int sample() {
        return drawGeometric(randGen, chance);
    }

    /**
     * Compute the sample value of a geometric distribution.
     *
     * @param randGen Random generator to use (and update).
     * @param chance Chance of success for each experiment.
     * @return Sampled value of the geometric distribution.
     */
    public int drawGeometric(RandomGenerator randGen, double chance) {
        double a = 1 / Math.log(1 - chance);
        return (int)(a * Math.log(randGen.drawNonzero()));
    }
}
