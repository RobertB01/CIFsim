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

package org.eclipse.escet.cif.eventbased.analysis;

/** Information about an added edge. */
public class AddedEdgeInfo {
    /** Source location of the new edge. */
    public final int from;

    /** Destination of the new edge. */
    public final int to;

    /** Event of the new edge. */
    public final int event;

    /**
     * Constructor of the {@link AddedEdgeInfo} class.
     *
     * @param from Source location of the new edge.
     * @param to Destination of the new edge.
     * @param event Event of the new edge.
     */
    public AddedEdgeInfo(int from, int to, int event) {
        this.from = from;
        this.to = to;
        this.event = event;
    }
}
