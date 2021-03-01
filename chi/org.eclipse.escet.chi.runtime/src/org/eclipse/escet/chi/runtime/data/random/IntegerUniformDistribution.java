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
 * Class representing an integer uniform distribution.
 *
 * <p>
 * U(a, b), a &lt; b, exclusive upper bound.
 * <ul>
 * <li>Mean: (a + b - 1) / 2</li>
 * <li>Variance:((b - a)**2 - 1) / 12</li>
 * </ul>
 * </p>
 */
public class IntegerUniformDistribution extends IntegerDistribution {
    /** Internal random generator. */
    private RandomGenerator randGen;

    /** Low bound (inclusive). */
    private int low;

    /** High bound (exclusive). */
    private int high;

    /**
     * Constructor of the {@link IntegerUniformDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param low Low bound of the distribution (inclusive).
     * @param high High bound of the distribution (exclusive).
     */
    public IntegerUniformDistribution(ChiCoordinator chiCoordinator, int low, int high) {
        super(chiCoordinator);
        if (low >= high) {
            String msg = fmt(
                    "The integer Uniform distribution expects the lower bound to be less than the higher bound, "
                            + "found interval [%s..%s) intead.",
                    low, high);
            throw new ChiSimulatorException(msg);
        }

        this.low = low;
        this.high = high;
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public int sample() {
        return drawIntegerUniform(randGen, low, high);
    }

    /**
     * Draw a sample of the integer uniform distribution.
     *
     * @param randGen Random generator to use (and update).
     * @param low Low bound (inclusive).
     * @param up High bound (exclusive).
     * @return Sampled value of the integer uniform distribution.
     */
    public static int drawIntegerUniform(RandomGenerator randGen, int low, int up) {
        int range = up - low;
        return low + (int)(range * randGen.draw());
    }
}
