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

/** Base class of a random generator. */
public abstract class RandomGenerator {
    /**
     * Draw a sample from the random generator.
     *
     * @return Sample value between 0 and 1 (exclusive upper limit).
     */
    public abstract double draw();

    /**
     * Draw a sample from the random generator.
     *
     * @return Sample value between 0 and 1 (exclusive lower and upper limits).
     */
    public double drawNonzero() {
        while (true) {
            double d = draw();
            if (d != 0.0) {
                return d;
            }
        }
    }
}
