//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.settings;

/** The way that state requirement invariants are enforced. */
public enum StateReqInvEnforceMode {
    /** Enforce all of them via the controlled behavior. */
    ALL_CTRL_BEH,

    /**
     * Decide per edge how to enforce them, enforcing them via edge guards for edges with controllable events, and
     * enforcing them via the controlled behavior for edges with uncontrollable events.
     */
    PER_EDGE;
}
