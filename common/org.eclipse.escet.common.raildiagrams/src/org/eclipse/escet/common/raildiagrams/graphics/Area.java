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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;

import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.solver.Variable;
import org.eclipse.escet.common.raildiagrams.util.Position2D;

/** An area in a diagram. */
public abstract class Area {
    /** Name of the area. */
    public final String prefix;

    /** Position of the top of the area. */
    public final Variable top;

    /** Position of the bottom of the area. */
    public final Variable bottom;

    /** Position of the left of the area. */
    public final Variable left;

    /** Position of the right of the area. */
    public final Variable right;

    /**
     * Constructor of the {@link Area} class.
     *
     * @param solver Variable and relation storage.
     * @param prefix Name prefix for the variables.
     */
    public Area(Solver solver, String prefix) {
        this.prefix = prefix;
        top = solver.newVar(prefix + ".top");
        bottom = solver.newVar(prefix + ".bottom");
        left = solver.newVar(prefix + ".left");
        right = solver.newVar(prefix + ".right");

        solver.addLe(left, 0, right);
        solver.addLe(top, 0, bottom);
    }

    /**
     * Dump coordinates of the area to debug output.
     *
     * @param solver Solver storing the relative positions.
     * @param xOffset Horizontal offset of the element in the picture.
     * @param yOffset Vertical offset of the element in the picture.
     */
    public void dump(Solver solver, double xOffset, double yOffset) {
        dbg("%s: x[%.1f--%.1f], y[%.1f--%.1f]", prefix, xOffset + solver.getVarValue(left),
                xOffset + solver.getVarValue(right), yOffset + solver.getVarValue(top),
                yOffset + solver.getVarValue(bottom));
    }

    /**
     * Return a list of positions where other elements are expected to connect.
     *
     * @param baseLeft Left-most X coordinate available for the graphic.
     * @param baseTop Top-most Y coordinate available for the graphic.
     * @param solver Solver containing values for all variables.
     * @return Positions expected to be used by other elements.
     */
    public abstract Position2D[] getConnectPoints(double baseLeft, double baseTop, Solver solver);
}
