//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2020, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.dsm;

/** Bus detection algorithms. */
public enum BusDetectionAlgorithms {
    /** No bus detection. */
    NO_BUS,

    /** The fixed-point algorithm of Wilschut et al. */
    FIX_POINT,

    /** Selecting the top k nodes. */
    TOP_K,
}
