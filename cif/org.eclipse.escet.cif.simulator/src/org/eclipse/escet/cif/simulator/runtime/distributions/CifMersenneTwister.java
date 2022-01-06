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

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.math3.random.MersenneTwister;

/** CIF compatible implementation of Mersenne Twister pseudo-random number generator. */
public class CifMersenneTwister extends MersenneTwister implements CifRandomGenerator {
    /**
     * Constructor for the {@link CifMersenneTwister} class.
     *
     * @param seed The initial seed.
     */
    public CifMersenneTwister(long seed) {
        super(seed);
    }

    @Override
    public double draw() {
        return nextDouble();
    }

    @Override
    public CifRandomGenerator copy() {
        // TODO: For better performance, Apache Common Math's MersenneTwister should have a copy constructor.
        return SerializationUtils.clone(this);
    }
}
