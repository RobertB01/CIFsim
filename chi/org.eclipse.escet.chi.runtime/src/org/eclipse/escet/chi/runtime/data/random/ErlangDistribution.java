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

package org.eclipse.escet.chi.runtime.data.random;

import static org.eclipse.escet.chi.runtime.data.random.GammaDistribution.drawGamma;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Class for a stochastic k-Erlang distribution.
 *
 * <p>
 * ERL(k, m), k a positive integer. Equivalent to Gamma(k, m/k).
 * <ul>
 * <li>Mean: m</li>
 * <li>Variance: m * m / k</li>
 * </ul>
 * </p>
 */
public class ErlangDistribution extends DoubleDistribution {
    /** First internal random number generator. */
    private RandomGenerator randGen1;

    /** Second internal random number generator. */
    private RandomGenerator randGen2;

    /** m parameter. */
    private double m;

    /** k parameter. */
    private int k;

    /**
     * Constructor for the {@link GammaDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param m The m parameter.
     * @param k The k parameter.
     */
    public ErlangDistribution(ChiCoordinator chiCoordinator, double m, int k) {
        super(chiCoordinator);
        if (m <= 0.0) {
            String msg = fmt("The Erlang distribution expects the m parameter to be bigger than 0, found %s instead.",
                    m);
            throw new ChiSimulatorException(msg);
        }
        if (k <= 0.0) {
            String msg = fmt("The Erlang distribution expects the k parameter to be bigger than 0, found %s instead.",
                    k);
            throw new ChiSimulatorException(msg);
        }

        this.m = m;
        this.k = k;
        randGen1 = chiCoordinator.getFreshGenerator();
        randGen2 = chiCoordinator.getFreshGenerator();
    }

    @Override
    public double sample() {
        return drawErlang(randGen1, randGen2, k, m);
    }

    /**
     * Compute a sample for the k-erlang distribution.
     *
     * @param randGen1 First random generator to use (and update).
     * @param randGen2 Second random generator to use (and update).
     * @param m m-parameter.
     * @param k Scale parameter.
     * @return Sample of the k-erlang distribution.
     */
    public static double drawErlang(RandomGenerator randGen1, RandomGenerator randGen2, int k, double m) {
        return drawGamma(randGen1, randGen2, k, m / k);
    }
}
