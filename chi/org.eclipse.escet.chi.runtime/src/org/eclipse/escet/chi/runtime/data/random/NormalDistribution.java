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
 * Class containing a stochastic normal distribution.
 *
 * <p>
 * N(m, v2)
 * <ul>
 * <li>Mean: m</li>
 * <li>Variance: v2</li>
 * </ul>
 * </p>
 */
public class NormalDistribution extends DoubleDistribution {
    /** Gaussian generator. */
    private GaussianGenerator gausGen;

    /** Mean value of the distribution. */
    private final double mean;

    /** Square root of the variance of the distribution. */
    private final double sqrtVariance;

    /**
     * Constructor of the {@link NormalDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param mean Mean of the distribution.
     * @param variance Variance of the distribution.
     */
    public NormalDistribution(ChiCoordinator chiCoordinator, double mean, double variance) {
        super(chiCoordinator);
        if (variance <= 0.0) {
            String msg = fmt("The Normal distribution expects the variance parameter to be bigger than 0, "
                    + "found %s instead.", variance);
            throw new ChiSimulatorException(msg);
        }

        this.mean = mean;
        this.sqrtVariance = Math.sqrt(variance);
        gausGen = chiCoordinator.getGaussianGenerator();
    }

    @Override
    public double sample() {
        return gausGen.drawGaussian(mean, sqrtVariance);
    }
}
