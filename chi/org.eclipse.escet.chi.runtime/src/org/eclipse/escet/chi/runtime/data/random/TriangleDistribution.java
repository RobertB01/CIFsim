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

package org.eclipse.escet.chi.runtime.data.random;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.ChiSimulatorException;

/**
 * Class for the triangle stochastic distribution.
 *
 * <p>
 * TRI(a, b, c), with a &lt; b &lt; c.
 * <ul>
 * <li>Mean: (a + b + c) / 3</li>
 * <li>Variance: (a * a + b * b + c * c - a * b - a * c - b * c) / 18</li>
 * </ul>
 * </p>
 */
public class TriangleDistribution extends DoubleDistribution {
    /** Internal random number generator. */
    private RandomGenerator randGen;

    /** Left-most position of the triangle. */
    private double a;

    /** Top-position, between a and c. */
    private double b;

    /** Right-most position of the triangle. */
    private double c;

    /**
     * Constructor of the {@link TriangleDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param a Left-most position of the triangle.
     * @param b Top-position, between a and c.
     * @param c Right-most position of the triangle.
     */
    public TriangleDistribution(ChiCoordinator chiCoordinator, double a, double b, double c) {
        super(chiCoordinator);
        if (a >= b) {
            String msg = fmt("The Triangle distribution expects the left bound to be less than the top, "
                    + "found %s for left and %s for top instead.", a, b);
            throw new ChiSimulatorException(msg);
        }
        if (b >= c) {
            String msg = fmt("The Triangle distribution expects the top to be less than the right bound, "
                    + "found %s for top and %s for right instead.", b, c);
            throw new ChiSimulatorException(msg);
        }

        this.a = a;
        this.b = b;
        this.c = c;
        randGen = chiCoordinator.getFreshGenerator();
    }

    @Override
    public double sample() {
        return drawTriangle(randGen, a, b, c);
    }

    /**
     * Compute a sample of the triangle distribution.
     *
     * @param randGen Random number generator to use (and update).
     * @param a Left-most position of the triangle.
     * @param b Top-position, between a and c.
     * @param c Right-most position of the triangle.
     * @return Sample value of the triangle distribution.
     */
    public static double drawTriangle(RandomGenerator randGen, double a, double b, double c) {
        double beta = (b - a) / (c - a);
        double u = randGen.draw();
        double t = (u < beta) ? Math.sqrt(beta * u) : 1 - Math.sqrt((1 - beta) * (1 - u));
        return a + (c - a) * t;
    }
}
