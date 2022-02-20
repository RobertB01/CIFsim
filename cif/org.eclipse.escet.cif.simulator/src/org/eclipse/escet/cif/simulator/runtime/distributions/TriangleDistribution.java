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
 * Triangle stochastic distribution:
 * <ul>
 * <li>Book Law & Kelton: {@code triang(a, c, b)}</li>
 * <li>{@code a}: the left-most position, {@code a < b}</li>
 * <li>{@code b}: the top position, {@code a < b < c}</li>
 * <li>{@code c}: the right-most position, {@code b < c}</li>
 * <li>Mean: {@code (a + b + c) / 3}</li>
 * <li>Variance: {@code (a^2 + b^2 + c^2 - a * b - a * c - b * c) / 18}</li>
 * </ul>
 */
public class TriangleDistribution extends RealDistribution {
    /** The random generator to use. Is modified in-place. */
    private final CifRandomGenerator randGen;

    /** The left-most position of the triangle ({@code a}). */
    private final double a;

    /** The top position of the triangle ({@code b}). */
    private final double b;

    /** The right-most position of the triangle ({@code c}). */
    private final double c;

    /**
     * Constructor for the {@link TriangleDistribution} class.
     *
     * @param randGen The random generator to use. Is modified in-place.
     * @param a The left-most position of the triangle ({@code a}).
     * @param b The top position ({@code b}).
     * @param c The right-most position of the triangle ({@code c}).
     * @throws CifSimulatorException If {@code a >= b} or {@code b >= c}.
     */
    public TriangleDistribution(CifRandomGenerator randGen, double a, double b, double c) {
        this.randGen = randGen;
        this.a = a;
        this.b = b;
        this.c = c;

        if (a >= b) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The left-most position (the first parameter) is not smaller than the top position "
                    + "(the second parameter).";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }

        if (b >= c) {
            String msg1 = fmt("Invalid operation: %s.", this);
            String msg2 = "The top position (the second parameter) is not smaller than the right-most position "
                    + "(the third parameter).";
            Exception e = new CifSimulatorException(msg2);
            throw new CifSimulatorException(msg1, e);
        }
    }

    /**
     * Constructor for the {@link TriangleDistribution} class.
     *
     * @param distribution The distribution to copy, including the state of the internal random generator(s). This
     *     operation is essentially a deep clone.
     */
    private TriangleDistribution(TriangleDistribution distribution) {
        this.randGen = distribution.randGen.copy();
        this.a = distribution.a;
        this.b = distribution.b;
        this.c = distribution.c;
    }

    @Override
    public RealDistribution copy() {
        return new TriangleDistribution(this);
    }

    @Override
    public double sample() {
        double beta = (b - a) / (c - a);
        double u = randGen.draw();
        double t = (u < beta) ? Math.sqrt(beta * u) : 1 - Math.sqrt((1 - beta) * (1 - u));
        return a + (c - a) * t;
    }

    @Override
    public String toString() {
        return fmt("triangle(%s, %s, %s)", realToStr(a), realToStr(b), realToStr(c));
    }
}
