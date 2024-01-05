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

package org.eclipse.escet.cif.eventbased;

import java.util.List;

import org.eclipse.escet.cif.eventbased.automata.Automaton;
import org.eclipse.escet.cif.eventbased.automata.AutomatonKind;
import org.eclipse.escet.cif.eventbased.automata.Edge;
import org.eclipse.escet.cif.eventbased.automata.Location;
import org.eclipse.escet.cif.eventbased.builders.AutomatonBuilder;
import org.eclipse.escet.cif.eventbased.builders.State;
import org.eclipse.escet.cif.eventbased.builders.StateEdges;
import org.eclipse.escet.common.java.exceptions.InvalidModelException;

/**
 * Compute the synchronous product of two or more automata.
 *
 * <p>
 * The alphabet of the result is the merged alphabets of all source automata.
 * </p>
 *
 * <p>
 * A location in the result is a combination of locations from the source automata (one location from each automaton). A
 * location is marked when all its associated source locations are marked. The initial location of the result is the
 * combination of initial locations in the source automata.
 * </p>
 *
 * <p>
 * An edge exists in the result when an edge with the same event exists in every source location (and the destination
 * location is the combined destinations).
 * </p>
 */
public class SynchronousProduct {
    /** Constructor for the {@link SynchronousProduct} class. */
    private SynchronousProduct() {
        // Static class.
    }

    /**
     * Compute the synchronous product automata from its source automata.
     *
     * @param automs Source automata.
     * @return Synchronous product result automaton.
     */
    public static Automaton product(List<Automaton> automs) {
        if (automs.isEmpty()) {
            String msg = "Cannot compute the product of 0 automata.";
            throw new InvalidModelException(msg);
        }

        // Compute kind of the result.
        AutomatonKind akind = null;
        for (Automaton a: automs) {
            if (akind == null) {
                akind = a.kind;
            } else if (!akind.equals(a.kind)) {
                akind = AutomatonKind.UNKNOWN;
                break;
            }
        }
        // Compute the product.
        AutomatonBuilder builder = new AutomatonBuilder(automs);
        for (State srcState: builder) {
            Location srcLoc = builder.getLocation(srcState);
            builder.edgeBuilder.setupStateEdges(srcState);
            for (StateEdges stateEdges: builder.edgeBuilder.getStateEdges()) {
                for (State destState: stateEdges) {
                    Location destLoc = builder.getLocation(destState);
                    Edge.addEdge(stateEdges.event, srcLoc, destLoc);
                }
            }
        }
        builder.destAut.kind = akind;
        return builder.destAut;
    }
}
