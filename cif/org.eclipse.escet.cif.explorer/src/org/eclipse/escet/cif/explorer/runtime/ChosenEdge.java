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

package org.eclipse.escet.cif.explorer.runtime;

import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.EdgeEvent;

/** Edge chosen for the event. */
public class ChosenEdge {
    /** Participating edge. */
    public final Edge edge;

    /** Participating event of the edge, {@code null} for tau events. */
    public final EdgeEvent edgeEvent;

    /**
     * Precise definition of the participating edge and event in a transition.
     *
     * @param edge Participating edge.
     * @param edgeEvent Participating event within the edge. Is {@code null} for tau events.
     */
    public ChosenEdge(Edge edge, EdgeEvent edgeEvent) {
        this.edge = edge;
        this.edgeEvent = edgeEvent;
    }
}
