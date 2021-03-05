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

package org.eclipse.escet.cif.simulator.runtime;

/** Simulation result. */
public enum SimulationResult {
    /** Simulation ended due to initialization failure. */
    INIT_FAILED,

    /** Simulation ended in deadlock. */
    DEADLOCK,

    /** Simulation ended due to reaching the user-provided simulation end-time. */
    ENDTIME_REACHED,

    /** Simulation terminated prematurely, at the request of the user. */
    USER_TERMINATED;
}
