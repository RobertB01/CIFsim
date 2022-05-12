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

package org.eclipse.escet.common.raildiagrams.railroad;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.raildiagrams.util.DumpSupportFunctions.writeDumpHeaderElements;

import java.awt.Color;

import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.Arc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.VertLine;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;

/** Iteration node in the diagram. */
public class LoopNode extends DiagramElement {
    /** Child diagram in the forward part of the loop. */
    public final DiagramElement forward;

    /** Child diagram in the backward part of the loop. */
    public final DiagramElement backward;

    /** Proxy of {@link #forward} in the constraints. */
    private ProxyDiagramElement forwProxy;

    /** Proxy of {@link #backward} in the constraints. */
    private ProxyDiagramElement backwProxy;

    /**
     * Constructor of the {@link LoopNode} class.
     *
     * @param forward Child diagram in the forward part of the loop.
     * @param backward Child diagram in the backward part of the loop.
     * @param id Identifying number of the diagram element.
     */
    public LoopNode(DiagramElement forward, DiagramElement backward, int id) {
        super("loop", id);
        this.forward = forward;
        this.backward = backward;
    }

    @Override
    public void create(Configuration config, int direction) {
        int railWidth = config.getRailWidth();
        Color railColor = config.getRailColor();
        int arcSize = config.getIntValue("loop.arc-radius");
        int leftPadding = config.getIntValue("loop.padding.left");
        int rightPadding = config.getIntValue("loop.padding.right");
        int interrowPadding = config.getIntValue("loop.padding.vertical");

        // Loop elements at the left.
        VertLine leftVertLine = new VertLine(solver, "loop-left-vert", railColor, railWidth);
        TopLeftArc tlArc = new TopLeftArc(solver, "loop-tlarc", railColor, arcSize, railWidth);
        BottomLeftArc blArc = new BottomLeftArc(solver, "loop-blarc", railColor, arcSize, railWidth);
        addGraphic(leftVertLine);
        addGraphic(tlArc);
        addGraphic(blArc);
        tlArc.connectLine(solver, leftVertLine);
        blArc.connectLine(solver, leftVertLine);

        HorLine entry = new HorLine(solver, "loop-entry", railColor, railWidth);
        addGraphic(entry);
        solver.addEq(entry.left, 0, left);
        solver.addEq(entry.top, 0, connectTop);
        solver.addEq(entry.right, 0, tlArc.right);
        solver.addEq(entry.top, 0, tlArc.top);
        solver.addLe(entry.left, arcSize + leftPadding, entry.right);

        // Loop elements at the right.
        VertLine rightVertLine = new VertLine(solver, "loop-right-vert", railColor, railWidth);
        TopRightArc trArc = new TopRightArc(solver, "loop-trarc", railColor, arcSize, railWidth);
        BottomRightArc brArc = new BottomRightArc(solver, "loop-brarc", railColor, arcSize, railWidth);
        addGraphic(rightVertLine);
        addGraphic(trArc);
        addGraphic(brArc);
        trArc.connectLine(solver, rightVertLine);
        brArc.connectLine(solver, rightVertLine);

        HorLine exit = new HorLine(solver, "loop-exit", railColor, railWidth);
        addGraphic(exit);
        solver.addEq(exit.left, 0, trArc.left);
        solver.addEq(exit.top, 0, trArc.top);
        solver.addEq(exit.right, 0, right);
        solver.addEq(exit.top, 0, connectTop);
        solver.addLe(exit.left, arcSize + rightPadding, exit.right);

        forward.create(config, direction);
        backward.create(config, -direction);

        forwProxy = addDiagramElement(forward, "proxy-forward");
        backwProxy = addDiagramElement(backward, "proxy-back");
        solver.addEq(top, 0, forwProxy.top);
        solver.addLe(forwProxy.bottom, interrowPadding, backwProxy.top); // Vertical spacing between the rows.
        solver.addEq(backwProxy.bottom, 0, bottom);

        // Horizontal connections.
        HorLine forwExtend = new HorLine(solver, "forw-extend", railColor, railWidth);
        HorLine backwExtend = new HorLine(solver, "backw-extend", railColor, railWidth);
        addGraphic(forwExtend);
        addGraphic(backwExtend);

        if (direction > 0) {
            connectArcProxyHLineArc(tlArc, forwProxy, forwExtend, trArc, railWidth);
            connectArcHlineProxyArc(blArc, backwExtend, backwProxy, brArc, railWidth);
        } else {
            connectArcHlineProxyArc(tlArc, forwExtend, forwProxy, trArc, railWidth);
            connectArcProxyHLineArc(blArc, backwProxy, backwExtend, brArc, railWidth);
        }
        solver.solve("loop", config);

        boolean dumpEquations = config.getDebugSetting(DebugDisplayKind.EQUATIONS);
        boolean dumpRelCoords = config.getDebugSetting(DebugDisplayKind.REL_COORDINATES);
        if ((dumpEquations || dumpRelCoords) && config.dodbg()) {
            writeDumpHeaderElements(config, this, list(forward, backward));
            config.dbg();

            if (dumpEquations) {
                solver.dumpRelations(config);
                config.dbg();
            }
            if (dumpRelCoords) {
                dumpElementBox(config);
                config.dbg();
            }
        }
    }

    /**
     * Horizontally connect an arc, a diagram, a horizontal line, and a second arc.
     *
     * @param a First arc to connect.
     * @param p Diagram to connect.
     * @param hl Horizontal line to connect.
     * @param b Second arc to connect.
     * @param railWidth Width of the rail line.
     */
    private void connectArcProxyHLineArc(Arc a, ProxyDiagramElement p, HorLine hl, Arc b, int railWidth) {
        p.connectLeft(solver, a, railWidth);
        p.connectRight(solver, hl);
        b.connectLine(solver, hl);
    }

    /**
     * Horizontally connect an arc, a horizontal line, a diagram element, and a second arc.
     *
     * @param a First arc to connect.
     * @param hl Horizontal line to connect.
     * @param p Diagram to connect.
     * @param b Second arc to connect.
     * @param railWidth Width of the rail line.
     */
    private void connectArcHlineProxyArc(Arc a, HorLine hl, ProxyDiagramElement p, Arc b, int railWidth) {
        a.connectLine(solver, hl);
        p.connectLeft(solver, hl);
        p.connectRight(solver, b, railWidth);
    }
}
