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

package org.eclipse.escet.chi.runtime.data.random;

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.data.CoordData;

/** Stochastic integer distribution base class. */
public abstract class IntegerDistribution extends CoordData {
    /**
     * Constructor for the {@link BooleanDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     */
    public IntegerDistribution(ChiCoordinator chiCoordinator) {
        super(chiCoordinator);
    }

    /**
     * Take a sample of an integer distribution.
     *
     * @return Sampled value.
     */
    public abstract int sample();
}
