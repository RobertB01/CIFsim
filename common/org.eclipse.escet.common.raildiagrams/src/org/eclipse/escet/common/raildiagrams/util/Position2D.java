//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.util;

import static org.eclipse.escet.common.java.Strings.fmt;

/** Class for storing a position. */
public class Position2D {
    /** Horizontal position. */
    public final double x;

    /** Vertical position. */
    public final double y;

    /**
     * Constructor of the {@link Position2D} class.
     *
     * @param x Horizontal position.
     * @param y Vertical position.
     */
    public Position2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return fmt("Position2D(%.1f, %.1f)", x, y);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Position2D)) {
            return false;
        }
        Position2D otherPos = (Position2D)other;
        return otherPos.x == x && otherPos.y == y;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) + 5 * Double.hashCode(y);
    }
}
