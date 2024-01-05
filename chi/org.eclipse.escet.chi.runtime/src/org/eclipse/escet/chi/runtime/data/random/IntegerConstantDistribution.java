//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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
 * Class for an integer 'stochastic' constant distribution.
 *
 * <p>
 * Always returns the same value; useful for debugging and testing. Also used as default value for stochastic
 * distributions.
 * </p>
 */
public class IntegerConstantDistribution extends IntegerDistribution {
    /** Constant value of the distribution. */
    private int value;

    /**
     * Default constructor for the {@link IntegerConstantDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     */
    public IntegerConstantDistribution(ChiCoordinator chiCoordinator) {
        this(chiCoordinator, 0);
    }

    /**
     * Constructor for the {@link IntegerConstantDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     * @param value Value to return when sampling.
     */
    public IntegerConstantDistribution(ChiCoordinator chiCoordinator, int value) {
        super(chiCoordinator);
        this.value = value;
    }

    @Override
    public int sample() {
        return value;
    }
}
