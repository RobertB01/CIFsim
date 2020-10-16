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

package org.eclipse.escet.chi.runtime.data.random;

import org.eclipse.escet.chi.runtime.ChiCoordinator;

/**
 * Class for a real number 'stochastic' constant distribution.
 *
 * <p>
 * Always returns the same value; useful for debugging and testing. Also used as default value for stochastic
 * distributions.
 * </p>
 */
public class DoubleConstantDistribution extends DoubleDistribution {
    /** Constant value of the distribution. */
    private double value;

    /**
     * Default constructor for the {@link DoubleConstantDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     */
    public DoubleConstantDistribution(ChiCoordinator chiCoordinator) {
        this(chiCoordinator, 0);
    }

    /**
     * Constructor for the {@link DoubleConstantDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param value Value to return when sampling.
     */
    public DoubleConstantDistribution(ChiCoordinator chiCoordinator, double value) {
        super(chiCoordinator);
        this.value = value;
    }

    @Override
    public double sample() {
        return value;
    }
}
