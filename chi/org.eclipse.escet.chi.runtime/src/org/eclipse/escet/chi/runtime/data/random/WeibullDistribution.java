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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Class for the stochastic Weibull distribution.
 *
 * <p>
 * WEIB(shape, scale).
 * </p>
 */
public class WeibullDistribution extends DoubleDistribution {
    /** Second internal random number generator. */
    private RandomGenerator randGen;

    /** Shape parameter. */
    private double shape;

    /** Scale parameter. */
    private double scale;

    /**
     * Constructor of the {@link WeibullDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param shape Shape parameter of the distribution.
     * @param scale Scale parameter of the distribution.
     */
    public WeibullDistribution(ChiCoordinator chiCoordinator, double shape, double scale) {
        super(chiCoordinator);
        if (shape <= 0.0) {
            String msg = fmt(
                    "The Weibull distribution expects the shape parameter to be bigger than 0, found %s instead.",
                    shape);
            throw new ChiSimulatorException(msg);
        }
        if (scale <= 0.0) {
            String msg = fmt(
                    "The Weibull distribution expects the scale parameter to be bigger than 0, found %s instead.",
                    scale);
            throw new ChiSimulatorException(msg);
        }

        this.shape = shape;
        this.scale = scale;
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public double sample() {
        return drawWeibull(randGen, shape, scale);
    }

    /**
     * Draw a sample of a Weibull distribution.
     *
     * @param randGen Random number generator to use (and update).
     * @param shape Shape parameter.
     * @param scale Scale parameter.
     * @return Sample of the distribution.
     */
    public static double drawWeibull(RandomGenerator randGen, double shape, double scale) {
        return scale * Math.pow(-Math.log(1 - randGen.draw()), 1 / shape);
    }
}
