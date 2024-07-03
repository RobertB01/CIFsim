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

/** Direction in which to apply an edge. */
public enum CifBddEdgeApplyDirection {
    /** Apply edge backward. */
    BACKWARD("backward"),

    /** Apply edge forward. */
    FORWARD("forward");

    /** The description. */
    public final String description;

    /**
     * Constructor of the {@link CifBddEdgeApplyDirection} enum.
     *
     * @param description The description.
     */
    private CifBddEdgeApplyDirection(String description) {
        this.description = description;
    }
}
