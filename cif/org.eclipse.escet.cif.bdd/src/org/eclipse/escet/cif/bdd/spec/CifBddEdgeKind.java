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

package org.eclipse.escet.cif.bdd.spec;

/** CIF/BDD edge kind. */
public enum CifBddEdgeKind {
    /** Edge with controllable event. */
    CONTROLLABLE,

    /** Edge with uncontrollable event. Is not an edge that allows an input variable to change value. */
    UNCONTROLLABLE,

    /** Edge that allows an input variable to change value. */
    INPUT_VARIABLE;
}
