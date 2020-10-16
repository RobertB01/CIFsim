//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.runtime;

/** State of a simulation (single model or xper run). */
public enum SimulationState {
    /** It does not exist. */
    NONE,

    /** It is created but not running. */
    STARTING,

    /** It is running. */
    RUNNING,

    /** It is finished. */
    FINISHED,
}
