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

package org.eclipse.escet.cif.simulator.runtime.distributions;

/**
 * 'Random' stochastic distribution. Provides direct access to the underlying random number generator:
 * <ul>
 * <li>Book Law & Kelton: {@code U(0, 1)}</li>
 * <li>Lower bound is inclusive, upper bounds is exclusive</li>
 * <li>Mean: {@code 0.5}</li>
 * <li>Variance: {@code 1 / 12}</li>
 * </ul>
 */
public class RandomDistribution extends RealDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /**
     * Constructor for the {@link RandomDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     */
    public RandomDistribution(CifRandomGenerator randGen) {
        this.randGen = randGen;
    }

    /**
     * Constructor for the {@link RandomDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private RandomDistribution(RandomDistribution distribution) {
        this.randGen = distribution.randGen.copy();
    }

    @Override
    public RealDistribution copy() {
        return new RandomDistribution(this);
    }

    @Override
    public double sample() {
        return randGen.draw();
    }

    @Override
    public String toString() {
        return "random()";
    }
}
