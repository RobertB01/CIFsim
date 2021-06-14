//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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

import java.awt.Graphics2D;

import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.solver.Variable;

/** An area in a diagram. */
public abstract class Area {
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
        top = solver.newVar(prefix + ".top");
        bottom = solver.newVar(prefix + ".bottom");
        left = solver.newVar(prefix + ".left");
        right = solver.newVar(prefix + ".right");

        solver.addLe(left, 0, right);
        solver.addLe(top, 0, bottom);
    }

    /**
     * Paint the graphic to the graphics output stream.
     *
     * @param baseLeft Left-most position available for use.
     * @param baseTop Top-most position available for use.
     * @param solver Solver containing values for all variables.
     * @param gd Graphics stream handle.
     */
    public abstract void paint(double baseLeft, double baseTop, Solver solver, Graphics2D gd);
}
