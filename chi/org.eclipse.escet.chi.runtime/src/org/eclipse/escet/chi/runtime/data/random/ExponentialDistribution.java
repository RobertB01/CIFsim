//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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
 * Negative exponential stochastic distribution.
 *
 * <p>
 * EXP(mean), mean &gt; 0
 * <ul>
 * <li>Mean: mean</li>
 * <li>Variance: mean * mean</li>
 * </ul>
 * </p>
 */
public class ExponentialDistribution extends DoubleDistribution {
    /** Internal random generator. */
    private RandomGenerator randGen;

    /** Mean value of the distribution. */
    private double mean;

    /**
     * Constructor of the {@link ExponentialDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param mean Mean value of the exponential distribution.
     */
    public ExponentialDistribution(ChiCoordinator chiCoordinator, double mean) {
        super(chiCoordinator);
        if (mean <= 0.0) {
            String msg = fmt("The Exponential distribution expects the mean parameter to be bigger than 0, found %s "
                    + "instead.", mean);
            throw new ChiSimulatorException(msg);
        }

        this.mean = mean;
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public double sample() {
        return drawExponential(randGen, mean);
    }

    /**
     * Draw a sample for the exponential distribution.
     *
     * @param randGen Random generator to use (and update).
     * @param mean Mean value of the exponential distribution.
     * @return Sampled value of the drawing process.
     */
    public static double drawExponential(RandomGenerator randGen, double mean) {
        return -mean * Math.log(1 - randGen.draw());
    }
}
