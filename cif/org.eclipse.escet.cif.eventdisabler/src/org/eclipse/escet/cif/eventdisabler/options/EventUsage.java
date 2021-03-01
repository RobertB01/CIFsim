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

package org.eclipse.escet.cif.eventdisabler.options;

/** How should the supplied events be used? That is, which events should be disabled? */
public enum EventUsage {
    /** Disable all supplied events, regardless of the alphabet of the input specification. */
    DISABLE,

    /** Disable all supplied events, that are not in the alphabet of the input specification. */
    ALPHABET;
}
