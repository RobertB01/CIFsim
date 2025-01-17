//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

/** CIF/BDD-related statistics. */
public enum CifBddStatistics {
    /** BDD garbage collection. */
    BDD_GC_COLLECT,

    /** BDD node table resize. */
    BDD_GC_RESIZE,

    /** BDD cache. */
    BDD_PERF_CACHE,

    /** Continuous BDD performance. */
    BDD_PERF_CONT,

    /** Maximum used BDD nodes. */
    BDD_PERF_MAX_NODES,

    /** Maximum used memory. */
    MAX_MEMORY,
}
