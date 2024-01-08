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

package org.eclipse.escet.cif.datasynth.settings;

/** BDD hyper-edge creation algorithm. */
public enum BddHyperEdgeAlgo {
    /** Use the legacy hyper-edge creation algorithm. */
    LEGACY,

    /** Use the linearized hyper-edge creation algorithm. */
    LINEARIZED,

    /**
     * Use the linearized hyper-edge creation algorithm for the FORCE and sliding window algorithms, and the legacy
     * hyper-edge creation algorithm otherwise.
     */
    DEFAULT;
}
