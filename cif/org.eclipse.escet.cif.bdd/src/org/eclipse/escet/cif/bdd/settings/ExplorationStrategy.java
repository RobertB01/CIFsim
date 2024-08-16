//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.bdd.settings;

/** Exploration strategy for symbolic reachability computations. */
public enum ExplorationStrategy {
    /** Use the chaining strategy with a fixed edge ordering. */
    CHAINING_FIXED,

    /** Use the chaining strategy with a dynamic edge ordering as determined by the edge workset algorithm. */
    CHAINING_WORKSET,

    /** Use the saturation strategy. */
    SATURATION;
}