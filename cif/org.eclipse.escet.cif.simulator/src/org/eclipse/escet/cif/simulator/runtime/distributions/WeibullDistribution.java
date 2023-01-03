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

import static org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath.realToStr;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.runtime.CifSimulatorException;

/**
 * Weibull stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: {@code Weibull(a, b)}</li>
 * <li>{@code a}: the shape parameter, {@code a > 0}</li>
 * <li>{@code b}: the scale parameter, {@code b > 0}</li>
 * <li>Mean: {@code (b / a) * F(1 / a)}</li>
 * <li>Variance: {@code (b^2 / a) * (2 * F(2 / a) - (F(1 / a)^2 / a))}</li>
 * </ul>
 */
public class WeibullDistribution extends RealDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The shape parameter {@code a}. */
    private final double shape;

    /** The scale parameter {@code b}. */
    private final double scale;

    /**
     * Constructor for the {@link WeibullDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param shape The shape parameter {@code a}.
     * @param scale The scale parameter {@code b}.
     * @throws CifSimulatorException If {@code shape <= 0} or {@code scale <= 0}.
     */
    public WeibullDistribution(CifRandomGenerator randGen, double shape, double scale) {
        this.randGen = randGen;
        this.shape = shape;
        this.scale = scale;

        if (shape <= 0.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The shape parameter (the first parameter) is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }

        if (scale <= 0.0) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The scale parameter (the second parameter) is not positive.";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link WeibullDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private WeibullDistribution(WeibullDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.shape = distribution.shape;
        this.scale = distribution.scale;
    }

    @Override
    public RealDistribution copy() {
        return new WeibullDistribution(this);
    }

    @Override
    public double sample() {
        return scale * Math.pow(-Math.log(1 - randGen.draw()), 1 / shape);
    }

    @Override
    public String toString() {
        return fmt("weibull(%s, %s)", realToStr(shape), realToStr(scale));
    }
}
