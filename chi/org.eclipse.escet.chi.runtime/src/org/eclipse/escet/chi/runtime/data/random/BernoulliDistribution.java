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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Stochastic Bernoulli distribution.
 *
 * <p>
 * Bernoulli(p), with p the chance of success.
 * <ul>
 * <li>Mean: p</li>
 * <li>Variance: p * (1 - p)</li>
 * </ul>
 * </p>
 */
public class BernoulliDistribution extends BooleanDistribution {
    /** Internal random generator. */
    private RandomGenerator randGen;

    /** Chance of success. */
    private double chance;

    /**
     * Constructor of the {@link BernoulliDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param chance Chance of success.
     */
    public BernoulliDistribution(ChiCoordinator chiCoordinator, double chance) {
        super(chiCoordinator);
        if (chance < 0.0 || chance > 1.0) {
            String msg = fmt("The Bernoulli distribution expects the chance parameter to be in the range [0.0..1.0], "
                    + "found chance %s instead.", chance);
            throw new ChiSimulatorException(msg);
        }

        this.chance = chance;
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public boolean sample() {
        return drawBernoulli(randGen, chance);
    }

    /**
     * Compute the result of sampling a Bernoulli distribution.
     *
     * @param randGen Random generator to use (and update).
     * @param chance Chance of success.
     * @return Sampled value of the drawing process.
     */
    public static boolean drawBernoulli(RandomGenerator randGen, double chance) {
        return randGen.draw() < chance;
    }
}
