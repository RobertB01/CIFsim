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

package org.eclipse.escet.cif.simulator.runtime.distributions;

import org.eclipse.escet.cif.simulator.runtime.io.RuntimeToStringable;

/** Boolean stochastic distribution. */
public abstract class BooleanDistribution implements RuntimeToStringable {
    /**
     * Copies the distribution, including the state of the internal random generator(s). This operation is essentially a
     * deep clone.
     *
     * @return The copy of the distribution.
     */
    public abstract BooleanDistribution copy();

    /**
     * Take a sample from the boolean distribution.
     *
     * @return The sampled value.
     */
    public abstract boolean sample();
}
