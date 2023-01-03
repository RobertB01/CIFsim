//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

/** Information of a created outgoing edge. */
public class EdgeInfo {
    /** Destination of the edge. */
    public final int destLoc;

    /** Event of the edge. */
    public int event;

    /**
     * Constructor of the {@link EdgeInfo} class.
     *
     * @param event Event of the edge.
     * @param destLoc Destination of the edge.
     */
    public EdgeInfo(int event, int destLoc) {
        this.event = event;
        this.destLoc = destLoc;
    }
}
