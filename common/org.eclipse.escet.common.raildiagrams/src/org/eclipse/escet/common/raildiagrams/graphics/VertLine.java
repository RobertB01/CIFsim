//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

/** Vertical line. */
public class VertLine extends Area {
    /** Color of the rail line. */
    public final Color railColor;

    /**
     * Constructor of the {@link VertLine} class.
     *
     * @param solver Variable and relation storage.
     * @param prefix Name prefix of the vertical line.
     * @param railColor Color of the line.
     * @param lineWidth Width of the line.
     */
    public VertLine(Solver solver, String prefix, Color railColor, int lineWidth) {
        super(solver, prefix + ".vert");
        this.railColor = railColor;

        solver.addEq(left, lineWidth - 1, right);
    }

    @Override
    public Position2D[] getConnectPoints(int baseLeft, int baseTop, Solver solver) {
        int top = solver.getVarValue(this.top) + baseTop;
        int bottom = solver.getVarValue(this.bottom) + baseTop;
        int left = solver.getVarValue(this.left) + baseLeft;
        int right = solver.getVarValue(this.right) + baseLeft;
        int width = right - left + 1;

        Position2D[] connections = new Position2D[width * 2];
        int index = 0;
        for (int i = 0; i < width; i++) {
            connections[index] = new Position2D(left + i, top - 1);
            connections[index + 1] = new Position2D(left + i, bottom + 1);
            index += 2;
        }
        return connections;
    }
}
