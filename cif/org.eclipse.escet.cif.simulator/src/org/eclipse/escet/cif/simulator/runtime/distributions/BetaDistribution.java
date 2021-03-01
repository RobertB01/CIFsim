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

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;

/**
 * Beta stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: {@code B(a, b)}</li>
 * <li>{@code a}: the shape parameter {@code a}, {@code a > 0}</li>
 * <li>{@code b}: the shape parameter {@code b}, {@code b > 0}</li>
 * <li>Mean: {@code a / (a + b)}</li>
 * <li>Variance: {@code a * b / ((a + b)^2 * (a + b + 1))}</li>
 * </ul>
 */
public class BetaDistribution extends RealDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The shape parameter {@code a}. */
    private final double a;

    /** The shape parameter {@code b}. */
    private final double b;

    /**
     * Constructor for the {@link BetaDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param a The shape parameter {@code a}.
     * @param b The shape parameter {@code b}.
     * @throws CifSimulatorException If {@code a <= 0} or {@code b <= 0}.
     */
    public BetaDistribution(CifRandomGenerator randGen, double a, double b) {
        this.randGen = randGen;
        this.a = a;
        this.b = b;

        if (a <= 0.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The first shape parameter is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }

        if (b <= 0.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The second shape parameter is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link BetaDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private BetaDistribution(BetaDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.a = distribution.a;
        this.b = distribution.b;
    }

    @Override
    public RealDistribution copy() {
        return new BetaDistribution(this);
    }

    @Override
    public double sample() {
        double g1 = GammaDistribution.sample(randGen, a, 1);
        double g2 = GammaDistribution.sample(randGen, b, 1);
        return g1 / (g1 + g2);
    }

    @Override
    public String toString() {
        return fmt("beta(%s, %s)", realToStr(a), realToStr(b));
    }
}
