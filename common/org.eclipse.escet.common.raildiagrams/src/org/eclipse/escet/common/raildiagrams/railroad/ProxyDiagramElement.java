//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.railroad;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.Arc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.output.OutputTarget;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.solver.Variable;

/** Abstract representative of a sub-diagram. */
public class ProxyDiagramElement {
    /** The real diagram element. */
    public final DiagramElement child;

    /** Variable holding the X coordinate of the left side of the diagram element. */
    public final Variable left;

    /** Variable holding the X coordinate of the right side of the diagram element. */
    public final Variable right;

    /** Variable holding the Y coordinate of the top side of the diagram element. */
    public final Variable top;

    /** Variable holding the Y coordinate of the bottom side of the diagram element. */
    public final Variable bottom;

    /** Variable holding the Y coordinate of the top side of the diagram element rail connection point. */
    public final Variable connectTop;

    /**
     * Constructor of the {@link ProxyDiagramElement} class.
     *
     * @param child The child diagram element.
     * @param elemName Name of the element in the parent element.
     * @param solver Constraint storage and solver.
     */
    public ProxyDiagramElement(DiagramElement child, String elemName, Solver solver) {
        this.child = child;

        // Query size and connect position from the child element.
        Solver childSolver = child.solver;
        int childTop = childSolver.getVarValue(child.top);
        int width = childSolver.getVarValue(child.right) - childSolver.getVarValue(child.left) + 1;
        int height = childSolver.getVarValue(child.bottom) - childTop + 1;
        Assert.check(width > 0);
        Assert.check(height > 0);
        int connectOffset = childSolver.getVarValue(child.connectTop) - childTop;
        Assert.check(connectOffset >= 0);
        Assert.check(connectOffset < height);

        // Create variables and set constraints.
        left = solver.newVar(elemName + ".left");
        right = solver.newVar(elemName + ".right");
        top = solver.newVar(elemName + ".top");
        bottom = solver.newVar(elemName + ".bottom");
        connectTop = solver.newVar(elemName + ".connectTop");
        solver.addEq(left, width - 1, right);
        solver.addEq(top, height - 1, bottom);
        solver.addEq(top, connectOffset, connectTop);
    }

    /**
     * Connect a horizontal line to the left entry of the element.
     *
     * @param solver Constraint storage and solver.
     * @param hline Line to connect.
     */
    public void connectLeft(Solver solver, HorLine hline) {
        solver.addEq(left, -1, hline.right);
        solver.addEq(connectTop, 0, hline.top);
    }

    /**
     * Connect a {@link TopLeftArc} or {@link BottomLeftArc} to the left entry of the element.
     *
     * @param solver Constraint storage and solver.
     * @param arc Arc to connect.
     * @param railWidth Width of the rail line.
     */
    public void connectLeft(Solver solver, Arc arc, int railWidth) {
        if (arc instanceof TopLeftArc) {
            solver.addEq(left, -1, arc.right);
            solver.addEq(connectTop, 0, arc.top);
        } else if (arc instanceof BottomLeftArc) {
            solver.addEq(left, -1, arc.right);
            solver.addEq(connectTop, railWidth - 1, arc.bottom);
        } else {
            Assert.fail(fmt("Cannot connect arc '%s' with the proxy element.", arc));
        }
    }

    /**
     * Connect a horizontal line to the right exit of the element.
     *
     * @param solver Constraint storage and solver.
     * @param hline Line to connect.
     */
    public void connectRight(Solver solver, HorLine hline) {
        solver.addEq(right, 1, hline.left);
        solver.addEq(connectTop, 0, hline.top);
    }

    /**
     * Connect a {@link TopRightArc} or {@link BottomRightArc} to the right exit of the element.
     *
     * @param solver Constraint storage and solver.
     * @param arc Arc to connect.
     * @param railWidth Width of the rail line.
     */
    public void connectRight(Solver solver, Arc arc, int railWidth) {
        if (arc instanceof TopRightArc) {
            solver.addEq(right, 1, arc.left);
            solver.addEq(connectTop, 0, arc.top);
        } else if (arc instanceof BottomRightArc) {
            solver.addEq(right, 1, arc.left);
            solver.addEq(connectTop, railWidth - 1, arc.bottom);
        } else {
            Assert.fail(fmt("Cannot connect arc '%s' with the proxy element.", arc));
        }
    }

    /**
     * Paint the proxy by painting the child node.
     *
     * @param left Coordinate of the left edge.
     * @param top Coordinate of the top edge.
     * @param outputTarget Diagram to write.
     * @param dumpAbsCoords Whether to dump the absolute coordinates of the elements for debugging.
     * @param config Configuration to use.
     */
    public void paint(int left, int top, OutputTarget outputTarget, boolean dumpAbsCoords, Configuration config) {
        child.paint(left, top, outputTarget, dumpAbsCoords, config);
    }
}
