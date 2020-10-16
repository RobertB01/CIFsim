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

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.intToStr;
import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;

/**
 * Binomial stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: {@code bin(t, p)}</li>
 * <li>{@code t}: the number of experiments, {@code t > 0}</li>
 * <li>{@code p}: the chance of success, {@code 0 <= p <= 1}</li>
 * <li>Mean: {@code t * p}</li>
 * <li>Variance: {@code t * p * (1 - p)}</li>
 * </ul>
 */
public class BinomialDistribution extends IntegerDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The chance of success. */
    private final double p;

    /** The number of experiments. */
    private final int t;

    /**
     * Constructor for the {@link BinomialDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param p Chance of success for each experiment.
     * @param t Number of experiments to perform.
     * @throws CifSimulatorException If {@code t <= 0} or {@code p} not in the range {@code [0 .. 1]}.
     */
    public BinomialDistribution(CifRandomGenerator randGen, double p, int t) {
        this.randGen = randGen;
        this.p = p;
        this.t = t;

        if (p < 0.0 || p > 1.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The chance parameter (the first parameter) is not in the range [0.0 .. 1.0].";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }

        if (t <= 0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The number of experiments (the second parameter) is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link BinomialDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private BinomialDistribution(BinomialDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.p = distribution.p;
        this.t = distribution.t;
    }

    @Override
    public IntegerDistribution copy() {
        return new BinomialDistribution(this);
    }

    @Override
    public int sample() {
        int count = 0;
        for (int experiment = 0; experiment < t; experiment++) {
            if (BernoulliDistribution.sample(randGen, p)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return fmt("binomial(%s, %s)", realToStr(p), intToStr(t));
    }
}
