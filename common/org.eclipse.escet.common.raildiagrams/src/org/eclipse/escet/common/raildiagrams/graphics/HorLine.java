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
import org.eclipse.escet.common.raildiagrams.util.Position2D;

/** Horizontal line. */
public class HorLine extends Area {
    /** Color of the rail line. */
    public final Color railColor;

    /**
     * Constructor of the {@link HorLine} class.
     *
     * @param solver Variable and relation storage.
     * @param prefix Name prefix of the horizontal line.
     * @param railColor Color of the line.
     * @param lineWidth Width of the line.
     */
    public HorLine(Solver solver, String prefix, Color railColor, int lineWidth) {
        super(solver, prefix + ".hor");
        this.railColor = railColor;

        solver.addEq(top, lineWidth - 1, bottom);
    }

    @Override
    public Position2D[] getConnectPoints(int baseLeft, int baseTop, Solver solver) {
        int top = solver.getVarValue(this.top) + baseTop;
        int bottom = solver.getVarValue(this.bottom) + baseTop;
        int left = solver.getVarValue(this.left) + baseLeft;
        int right = solver.getVarValue(this.right) + baseLeft;
        int height = bottom - top + 1;

        Position2D[] connections = new Position2D[height * 2];
        int index = 0;
        for (int i = 0; i < height; i++) {
            connections[index] = new Position2D(left - 1, top + i);
            connections[index + 1] = new Position2D(right + 1, top + i);
            index += 2;
        }
        return connections;
    }
}
