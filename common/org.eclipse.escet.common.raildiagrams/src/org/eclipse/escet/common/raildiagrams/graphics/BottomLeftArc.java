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

import static org.eclipse.escet.common.raildiagrams.graphics.PaintSupport.drawArc;

import java.awt.Color;
import java.awt.Graphics2D;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.graphics.PaintSupport.ArcType;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.util.Position2D;

/** An arc running from top-left to bottom-right. */
public class BottomLeftArc extends Arc {
    /**
     * Constructor of the {@link TopLeftArc} class.
     *
     * @param solver Variable and relation storage.
     * @param prefix Name prefix of the arc line.
     * @param railColor Color of the rail in the diagram.
     * @param size Size of the arc, from center-point upto and including the line.
     * @param lineWidth Width of the arc line.
     */
    public BottomLeftArc(Solver solver, String prefix, Color railColor, double size, double lineWidth) {
        super(solver, prefix, railColor, size, lineWidth);
    }

    @Override
    public void connectLine(Solver solver, HorLine line) {
        solver.addEq(right, 0, line.left);
        solver.addEq(bottom, 0, line.bottom);
    }

    @Override
    public void connectLine(Solver solver, VertLine line) {
        solver.addEq(left, 0, line.left);
        solver.addEq(top, 0, line.bottom);
    }

    @Override
    public void paint(double baseLeft, double baseTop, Solver solver, Graphics2D gd) {
        double left = solver.getVarValue(this.left) + baseLeft;
        double right = solver.getVarValue(this.right) + baseLeft - 1;
        double top = solver.getVarValue(this.top) + baseTop;
        double bottom = solver.getVarValue(this.bottom) + baseTop - 1;

        double size = bottom - top + 1;
        Assert.check(Math.abs(size - (right - left + 1)) < Solver.EPSILON); // Must be a square area.
        drawArc(gd, left, top, ArcType.BL_ARC, size, lineWidth, railColor);
    }

    @Override
    public Position2D[] getConnectPoints(double baseLeft, double baseTop, Solver solver) {
        int left = (int)(solver.getVarValue(this.left) + baseLeft);
        int right = (int)(solver.getVarValue(this.right) + baseLeft - 1);
        int top = (int)(solver.getVarValue(this.top) + baseTop);
        int bottom = (int)(solver.getVarValue(this.bottom) + baseTop - 1);
        int lwidth = (int)lineWidth;

        Position2D[] connections = new Position2D[lwidth * 2];
        int index = 0;
        for (int i = 0; i < lwidth; i++) {
            connections[index] = new Position2D(left + i, top - 1);
            connections[index + 1] = new Position2D(right + 1, bottom - i);
            index += 2;
        }
        return connections;
    }
}
