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

package org.eclipse.escet.cif.simulator.runtime.distributions;

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.intToStr;
import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;

/**
 * Erlang stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: m-Erlang(b)</li>
 * <li>{@code m}: the shape parameter, {@code m > 0}</li>
 * <li>{@code b}: the scale parameter, {@code b > 0}</li>
 * <li>Mean: {@code m * b}</li>
 * <li>Variance: {@code m * b^2}</li>
 * </ul>
 */
public class ErlangDistribution extends RealDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The shape parameter. */
    private final int m;

    /** The scale parameter. */
    private final double b;

    /**
     * Constructor for the {@link ErlangDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param m The shape parameter.
     * @param b The scale parameter.
     * @throws CifSimulatorException If {@code m <= 0} or {@code b <= 0}.
     */
    public ErlangDistribution(CifRandomGenerator randGen, int m, double b) {
        this.randGen = randGen;
        this.m = m;
        this.b = b;

        if (m <= 0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The shape parameter (the first parameter) is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }

        if (b <= 0.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The scale parameter (the second parameter) is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link ErlangDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private ErlangDistribution(ErlangDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.m = distribution.m;
        this.b = distribution.b;
    }

    @Override
    public RealDistribution copy() {
        return new ErlangDistribution(this);
    }

    @Override
    public double sample() {
        return GammaDistribution.sample(randGen, m, b);
    }

    @Override
    public String toString() {
        return fmt("erlang(%s, %s)", intToStr(m), realToStr(b));
    }
}
