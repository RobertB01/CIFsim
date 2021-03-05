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

import org.eclipse.escet.chi.runtime.ChiCoordinator;

/**
 * Direct access to the underlying random number generator.
 *
 * <p>
 * U(0.0, 1.0), exclusive upper bound.
 * <ul>
 * <li>Mean: 0.5</li>
 * <li>Variance: 1/12</li>
 * </ul>
 * </p>
 */
public class RandomDistribution extends DoubleDistribution {
    /** Internal random generator. */
    private RandomGenerator randGen;

    /**
     * Constructor of the {@link RandomDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     */
    public RandomDistribution(ChiCoordinator chiCoordinator) {
        super(chiCoordinator);
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public double sample() {
        return drawRandom(randGen);
    }

    /**
     * Draw a sample for the random distribution.
     *
     * @param randGen Random generator to use (and update).
     * @return Sampled value of the drawing process.
     */
    public static double drawRandom(RandomGenerator randGen) {
        return randGen.draw();
    }
}
