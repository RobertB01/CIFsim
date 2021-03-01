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

/** Information about a removed edge. */
public class RemovedEdgeInfo {
    /** Source location of the removed edge. */
    public final int from;

    /** Destination of the removed edge. See {@link #toIsAutomaton}. */
    public final int to;

    /** Event of the removed edge. */
    public final int event;

    /**
     * If set, the {@link #to} value is an automaton number that disabled the edge. If unset, the {@link #to} value is a
     * location number that got removed.
     */
    public final boolean toIsAutomaton;

    /**
     * Constructor of the {@link RemovedEdgeInfo} class.
     *
     * @param from Source location of the removed edge.
     * @param to Destination of the removed edge. See {@link #toIsAutomaton}.
     * @param event Event of the removed edge.
     * @param toIsAutomaton Whether the 'to' field is an automaton index. See {@link #toIsAutomaton}.
     */
    public RemovedEdgeInfo(int from, int to, int event, boolean toIsAutomaton) {
        this.from = from;
        this.to = to;
        this.event = event;
        this.toIsAutomaton = toIsAutomaton;
    }
}
