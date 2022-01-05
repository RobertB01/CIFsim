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

import static org.eclipse.escet.common.raildiagrams.graphics.PaintSupport.setLineWidth;

import java.awt.Color;
import java.awt.Graphics2D;

import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.util.Position2D;

/** Vertical line. */
public class VertLine extends Area {
    /** Color of the rail line. */
    public final Color railColor;

    /**
     * Constructor of the {@link VertLine} class.
     *
     * @param solver    Variable and relation storage.
     * @param prefix    Name prefix of the vertical line.
     * @param railColor Color of the line.
     * @param lineWidth Width of the line.
     */
    public VertLine(Solver solver, String prefix, Color railColor, double lineWidth) {
        super(solver, prefix + ".vert");
        this.railColor = railColor;

        solver.addEq(left, lineWidth, right);
    }

    @Override
    public void paint(double baseLeft, double baseTop, Solver solver, Graphics2D gd) {
        double top = solver.getVarValue(this.top) + baseTop;
        double bottom = solver.getVarValue(this.bottom) + baseTop - 1;
        double left = solver.getVarValue(this.left) + baseLeft;
        double right = solver.getVarValue(this.right) + baseLeft - 1;
        double width = right - left + 1;
        double center = (right + left + 1) / 2;

        if (bottom <= top) {
            return;
        }

        gd.setColor(railColor);
        setLineWidth(gd, (int)width);
        gd.drawLine((int)center, (int)top, (int)center, (int)bottom);
    }

    @Override
    public Position2D[] getConnectPoints(double baseLeft, double baseTop, Solver solver) {
        int top = (int)(solver.getVarValue(this.top) + baseTop);
        int bottom = (int)(solver.getVarValue(this.bottom) + baseTop - 1);
        int left = (int)(solver.getVarValue(this.left) + baseLeft);
        int right = (int)(solver.getVarValue(this.right) + baseLeft - 1);
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
