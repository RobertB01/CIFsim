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

package org.eclipse.escet.cif.simulator.input.trace;

/** Trace input time mode. Used as value of the 'time' option. */
public enum TimeMode {
    /** No time passage. */
    OFF,

    /** Implicit time passage. */
    IMPLICIT,

    /** Explicit time passage. */
    EXPLICIT;
}
