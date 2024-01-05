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

import static org.eclipse.escet.chi.runtime.data.random.BernoulliDistribution.drawBernoulli;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Stochastic binomial distribution.
 *
 * <p>
 * bin(t, p), with t the number of experiments, and p the chance of success.
 * <ul>
 * <li>Mean: t * p</li>
 * <li>Variance: t * p * (1 - p)</li>
 * </ul>
 * </p>
 */
public class BinomialDistribution extends IntegerDistribution {
    /** Internal random generator. */
    private RandomGenerator randGen;

    /** Number of experiments. */
    private int numberExperiments;

    /** Chance of success. */
    private double chance;

    /**
     * Constructor of the {@link BinomialDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param numberExperiments Number of experiments to perform.
     * @param chance Chance of success for each experiment.
     */
    public BinomialDistribution(ChiCoordinator chiCoordinator, int numberExperiments, double chance) {
        super(chiCoordinator);
        if (numberExperiments <= 0) {
            String msg = fmt("The Binomial distribution expects a non-zero positive number of experiments, "
                    + "found value %d instead.", numberExperiments);
            throw new ChiSimulatorException(msg);
        }
        if (chance < 0.0 || chance > 1.0) {
            String msg = fmt("The Binomial distribution expects a chance parameter in the range [0.0..1.0], "
                    + "found chance %s instead.", chance);
            throw new ChiSimulatorException(msg);
        }

        this.numberExperiments = numberExperiments;
        this.chance = chance;
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public int sample() {
        return drawBinomial(randGen, numberExperiments, chance);
    }

    /**
     * Compute the sampling of a binomial distribution.
     *
     * @param randGen Random generator to use (and update).
     * @param numberExperiments Number of experiments to perform.
     * @param chance Chance of success for each experiment.
     * @return Sampled value of the binomial distribution.
     */
    public static int drawBinomial(RandomGenerator randGen, int numberExperiments, double chance) {
        int count = 0;
        for (int experiment = 0; experiment < numberExperiments; experiment++) {
            if (drawBernoulli(randGen, chance)) {
                count++;
            }
        }
        return count;
    }
}
