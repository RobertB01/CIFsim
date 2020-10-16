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

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;

/**
 * Geometric stochastic geometric distribution (number of failures before first success):
 * <ul>
 * <li>Book Law & Kelton: {@code geom(p)}</li>
 * <li>{@code p}: the chance of success, {@code 0 < p <= 1}</li>
 * <li>Mean: {@code (1 - p) / p}</li>
 * <li>Variance: {@code (1 - p) / (p * p)}</li>
 * </ul>
 */
public class GeometricDistribution extends IntegerDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The chance of success. */
    private final double p;

    /**
     * Constructor for the {@link GeometricDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param p The chance of success.
     * @throws CifSimulatorException If {@code p} is not in the range {@code (0 .. 1]}.
     */
    public GeometricDistribution(CifRandomGenerator randGen, double p) {
        this.randGen = randGen;
        this.p = p;

        if (p <= 0.0 || p > 1.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The chance parameter is not in the range (0.0 .. 1.0].";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link GeometricDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private GeometricDistribution(GeometricDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.p = distribution.p;
    }

    @Override
    public IntegerDistribution copy() {
        return new GeometricDistribution(this);
    }

    @Override
    public int sample() {
        double a = 1 / Math.log(1 - p);
        return (int)(a * Math.log(randGen.drawNonZero()));
    }

    @Override
    public String toString() {
        return fmt("geometric(%s)", realToStr(p));
    }
}
