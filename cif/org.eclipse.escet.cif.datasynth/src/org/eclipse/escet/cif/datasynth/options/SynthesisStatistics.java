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

package org.eclipse.escet.cif.datasynth.options;

/** Synthesis statistics. */
public enum SynthesisStatistics {
    /** Timing. */
    TIMING,

    /** BDD garbage collection. */
    BDD_GC,

    /** BDD node table resize. */
    BDD_RESIZE,

    /** Maximum used BDD nodes. */
    BDD_MAX_NODES,

    /** Continuous BDD node usage. */
    BDD_CONTINUOUS_NODES,

    /** BDD cache. */
    BDD_CACHE;
}
