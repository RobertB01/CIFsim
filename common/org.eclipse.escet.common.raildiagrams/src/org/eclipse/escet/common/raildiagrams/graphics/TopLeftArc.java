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

/** An arc running from bottom-left to top-right. */
public class TopLeftArc extends Arc {
    /**
     * Constructor of the {@link TopLeftArc} class.
     *
     * @param solver Variable and relation storage.
     * @param prefix Name prefix of the arc line.
     * @param railColor Color of the rail in the diagram.
     * @param size Size of the arc, from center-point upto and including the line.
     * @param lineWidth Width of the arc line.
     */
    public TopLeftArc(Solver solver, String prefix, Color railColor, int size, int lineWidth) {
        super(solver, prefix, railColor, size, lineWidth);
    }

    @Override
    public void connectLine(Solver solver, HorLine line) {
        solver.addEq(right, 1, line.left);
        solver.addEq(top, 0, line.top);
    }

    @Override
    public void connectLine(Solver solver, VertLine line) {
        solver.addEq(left, 0, line.left);
        solver.addEq(bottom, 1, line.top);
    }

    @Override
    public Position2D[] getConnectPoints(int baseLeft, int baseTop, Solver solver) {
        int left = solver.getVarValue(this.left) + baseLeft;
        int right = solver.getVarValue(this.right) + baseLeft;
        int top = solver.getVarValue(this.top) + baseTop;
        int bottom = solver.getVarValue(this.bottom) + baseTop;

        Position2D[] connections = new Position2D[lineWidth * 2];
        int index = 0;
        for (int i = 0; i < lineWidth; i++) {
            connections[index] = new Position2D(left + i, bottom + 1);
            connections[index + 1] = new Position2D(right + 1, top + i);
            index += 2;
        }
        return connections;
    }
}
