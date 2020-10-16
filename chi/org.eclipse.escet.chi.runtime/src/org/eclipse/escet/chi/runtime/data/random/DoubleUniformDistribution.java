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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Class representing a real uniform distribution.
 *
 * <p>
 * U(a, b), a &lt; b, exclusive upper bound.
 * <ul>
 * <li>Mean: (a + b) / 2</li>
 * <li>Variance:(b - a)**2 / 12</li>
 * </ul>
 * </p>
 */
public class DoubleUniformDistribution extends DoubleDistribution {
    /** Internal random generator. */
    private RandomGenerator randGen;

    /** Low bound (inclusive). */
    private double low;

    /** High bound (exclusive). */
    private double high;

    /**
     * Constructor of the {@link DoubleUniformDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param low Low bound of the distribution (inclusive).
     * @param high High bound of the distribution (exclusive).
     */
    public DoubleUniformDistribution(ChiCoordinator chiCoordinator, double low, double high) {
        super(chiCoordinator);
        if (low >= high) {
            String msg = fmt("The real Uniform distribution expects the lower bound to be less than the higher bound, "
                    + "found interval [%s..%s) instead.", low, high);
            throw new ChiSimulatorException(msg);
        }

        this.low = low;
        this.high = high;
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public double sample() {
        return drawDoubleUniform(randGen, low, high);
    }

    /**
     * Draw a sample of the real uniform distribution.
     *
     * @param randGen Random generator to use (and update).
     * @param low Low bound (inclusive).
     * @param up High bound (exclusive).
     * @return Sampled value of the real uniform distribution.
     */
    public static double drawDoubleUniform(RandomGenerator randGen, double low, double up) {
        double range = up - low;
        return low + range * randGen.draw();
    }
}
