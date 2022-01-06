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

package org.eclipse.escet.chi.runtime.data;

import org.eclipse.escet.chi.runtime.ChiCoordinator;

/** Base class for data with a coordinator reference. */
public abstract class CoordData {
    /** Reference to the coordinator. */
    public final ChiCoordinator chiCoordinator;

    /**
     * Constructor for the {@link CoordData} class.
     *
     * @param chiCoordinator Reference to the central coordinator.
     */
    public CoordData(ChiCoordinator chiCoordinator) {
        this.chiCoordinator = chiCoordinator;
    }
}
