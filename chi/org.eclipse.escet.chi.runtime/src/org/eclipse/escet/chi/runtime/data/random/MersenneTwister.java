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

package org.eclipse.escet.chi.runtime.data.random;

/** Wrapper class around the org.apache math implementation of MT. */
public class MersenneTwister extends RandomGenerator {
    /** Mersenne Twister instance. */
    org.apache.commons.math3.random.RandomGenerator randGen;

    /**
     * Constructor of the {@link MersenneTwister} class.
     *
     * @param seed Initial seed of the MT.
     */
    public MersenneTwister(long seed) {
        randGen = new org.apache.commons.math3.random.MersenneTwister(seed);
    }

    @Override
    public double draw() {
        return randGen.nextDouble();
    }
}
