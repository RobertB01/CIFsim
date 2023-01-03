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

package org.eclipse.escet.cif.common;

import org.eclipse.escet.cif.metamodel.cif.automata.Edge;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;

/** CIF edge utility methods. */
public class CifEdgeUtils {
    /** Constructor for the {@link CifEdgeUtils} class. */
    private CifEdgeUtils() {
        // Static class.
    }

    /**
     * Returns the source location of an edge.
     *
     * @param edge The edge.
     * @return The source location.
     */
    public static Location getSource(Edge edge) {
        return (Location)edge.eContainer();
    }

    /**
     * Returns the target location of an edge. For self loops without an explicit target location, the implicit target
     * location is returned.
     *
     * @param edge The edge.
     * @return The target location.
     */
    public static Location getTarget(Edge edge) {
        Location target = edge.getTarget();
        if (target != null) {
            return target;
        }
        return getSource(edge);
    }

    /**
     * Is the given edge a self loop?
     *
     * @param edge The edge.
     * @return {@code true} if the edge is a self loop, {@code false} otherwise.
     */
    public static boolean isSelfLoop(Edge edge) {
        // If no target location, then it is by definition a self loop.
        if (edge.getTarget() == null) {
            return true;
        }

        // Compare source/target locations.
        Location source = (Location)edge.eContainer();
        return source == edge.getTarget();
    }
}
