//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output.print;

/** The kind of a transition for a runtime print declaration. */
public enum PrintTransitionKind {
    /** The virtual 'initial' transition. */
    INITIAL,

    /** The virtual 'final' transition. */
    FINAL,

    /** An event transition. */
    EVENT,

    /** A time transition. */
    TIME;
}
