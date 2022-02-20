//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.util;

/** Kinds of debug information that can be displayed. */
public enum DebugDisplayKind {
    /** Display element structure. */
    STRUCTURE,

    /** Display equations. */
    EQUATIONS,

    /** Display details of solving the position equations. */
    SOLVER,

    /** Display relative coordinates. */
    REL_COORDINATES,

    /** Display absolute coordinates. */
    ABS_COORDINATES;
}
