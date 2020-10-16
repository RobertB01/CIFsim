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

import org.eclipse.escet.chi.runtime.ChiCoordinator;

/**
 * Generator for Gaussian function values for the purpose of drawing samples from normal and lognormal distributions.
 */
public class GaussianGenerator {
    /** First random number generator. */
    private RandomGenerator randGen1;

    /** Second random number generator. */
    private RandomGenerator randGen2;

    /** Cached value from the sample generation process. */
    private double cachedValue;

    /** Cached y value. */
    private double cachedY;

    /**
     * Flag whether {@link #cachedValue} and {@link #cachedY} are valid. If {@code true} they are valid, otherwise they
     * are not valid.
     */
    private boolean cacheFilled;

    /**
     * Constructor of the {@link GaussianGenerator} class.
     *
     * @param chiCoordinator Coordinator object of the simulation.
     */
    public GaussianGenerator(ChiCoordinator chiCoordinator) {
        randGen1 = chiCoordinator.getFreshGenerator();
        randGen2 = chiCoordinator.getFreshGenerator();
        cacheFilled = false;
    }

    /**
     * Draw a value from the Gaussian function.
     *
     * @param mean Mean value.
     * @param sqrtVariance Square root of the variance.
     * @return Drawn sample.
     */
    public double drawGaussian(double mean, double sqrtVariance) {
        if (cacheFilled) {
            cacheFilled = false;
            return mean + sqrtVariance * cachedValue * cachedY;
        }
        double s1;
        double s2;
        double w;
        do {
            s1 = 2 * randGen1.draw() - 1;
            s2 = 2 * randGen2.draw() - 1;
            w = s1 * s1 + s2 * s2;
        } while (w >= 1.0 || w == 0.0);
        cachedY = Math.sqrt(-2 * Math.log(w) / w);
        cachedValue = s2;
        cacheFilled = true;
        return mean + sqrtVariance * s1 * cachedY;
    }
}
