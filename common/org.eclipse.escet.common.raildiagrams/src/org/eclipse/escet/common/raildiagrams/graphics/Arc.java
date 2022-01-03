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

package org.eclipse.escet.common.raildiagrams.graphics;

import java.awt.Color;

import org.eclipse.escet.common.raildiagrams.solver.Solver;

/**
 * An arc connecting horizontal with vertical.
 *
 * <p>
 * Edge of the arc area is also edge of the line.
 * </p>
 */
public abstract class Arc extends Area {
    /** Line width of the arc. */
    public final double lineWidth;

    /** Color of the arc. */
    public final Color railColor;

    /**
     * Constructor of the {@link Arc} base class.
     *
     * @param solver    Variable and relation storage.
     * @param prefix    Name prefix of the arc line.
     * @param railColor Color of the rail in the diagram.
     * @param size      Size of the arc, from center-point up to and including the
     *                  line.
     * @param lineWidth Width of the arc line.
     */
    public Arc(Solver solver, String prefix, Color railColor, double size, double lineWidth) {
        super(solver, prefix);
        this.lineWidth = lineWidth;
        this.railColor = railColor;

        size = Math.max(size, lineWidth);
        solver.addEq(left, size, right);
        solver.addEq(top, size, bottom);
    }

    /**
     * Connect the arc with a horizontal line.
     *
     * @param solver Variable and relation storage.
     * @param line   Line to connect with.
     */
    public abstract void connectLine(Solver solver, HorLine line);

    /**
     * Connect the arc with a vertical line.
     *
     * @param solver Variable and relation storage.
     * @param line   Line to connect with.
     */
    public abstract void connectLine(Solver solver, VertLine line);
}
