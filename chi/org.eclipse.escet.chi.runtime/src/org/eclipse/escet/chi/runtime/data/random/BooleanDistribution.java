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

import org.eclipse.escet.chi.runtime.ChiCoordinator;
import org.eclipse.escet.chi.runtime.data.CoordData;

/** Stochastic boolean distribution base class. */
public abstract class BooleanDistribution extends CoordData {
    /**
     * Constructor for the {@link BooleanDistribution} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     */
    public BooleanDistribution(ChiCoordinator chiCoordinator) {
        super(chiCoordinator);
    }

    /**
     * Take a sample of a boolean distribution.
     *
     * @return Sampled value.
     */
    public abstract boolean sample();
}
