//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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
 * Poisson stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: {@code P(r)}</li>
 * <li>{@code r}: the rate, {@code r > 0}</li>
 * <li>Mean: {@code r}</li>
 * <li>Variance: {@code r}</li>
 * </ul>
 */
public class PoissonDistribution extends IntegerDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The rate of the distribution. */
    private final double r;

    /**
     * Constructor for the {@link PoissonDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param r The rate of the distribution.
     * @throws CifSimulatorException If {@code r <= 0.0}.
     */
    public PoissonDistribution(CifRandomGenerator randGen, double r) {
        this.randGen = randGen;
        this.r = r;

        if (r <= 0.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The rate parameter is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link PoissonDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private PoissonDistribution(PoissonDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.r = distribution.r;
    }

    @Override
    public IntegerDistribution copy() {
        return new PoissonDistribution(this);
    }

    @Override
    public int sample() {
        // NOTE: This generator is slow for larger 'r': find a faster one.

        double e = Math.exp(-r);
        double p = 1.0;
        int r = -1;
        while (p > e) {
            p *= randGen.drawNonZero();
            r++;
        }
        return r;
    }

    @Override
    public String toString() {
        return fmt("poisson(%s)", realToStr(r));
    }
}
