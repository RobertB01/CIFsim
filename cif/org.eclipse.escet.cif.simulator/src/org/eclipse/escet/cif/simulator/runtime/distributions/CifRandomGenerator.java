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

/** Random number generator. */
public interface CifRandomGenerator {
    /**
     * Take a sample from the random number generator.
     *
     * @return The sampled value, in the range [0..1).
     */
    public double draw();

    /**
     * Take a sample from the random number generator.
     *
     * @return The sampled value, in the range (0..1).
     */
    public default double drawNonZero() {
        while (true) {
            double d = draw();
            if (d != 0.0) {
                return d;
            }
        }
    }

    /**
     * Copies the random number generator, including the internal state. This operation is essentially a deep clone.
     *
     * @return The copy of the random number generator.
     */
    public CifRandomGenerator copy();
}
