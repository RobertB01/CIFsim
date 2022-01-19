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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dodbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.raildiagrams.util.DumpSupportFunctions.writeDumpHeaderElements;

import java.awt.Color;
import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.VertLine;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;

/** Node that builds a diagram to pick one of the child nodes. */
public class ChoiceNode extends DiagramElement {
    /** Alternatives of the choice. */
    public final List<DiagramElement> alternatives;

    /** Proxies of the {@link #alternatives} elements. */
    private List<ProxyDiagramElement> altProxies;

    /**
     * Choice between alternatives.
     *
     * @param alternatives Alternatives to choose from.
     * @param id Identifying number of the diagram element.
     */
    public ChoiceNode(List<DiagramElement> alternatives, int id) {
        super("choice", id);
        this.alternatives = alternatives;
        Assert.check(alternatives.size() > 1);
    }

    @SuppressWarnings("null") // False positives in accessing topElement and lastElement.
    @Override
    public void create(Configuration config, int direction) {
        double railWidth = config.getRailWidth();
        Color railColor = config.getRailColor();
        double arcSize = config.getRealValue("choice.arc-radius");
        double choiceVertPadding = config.getRealValue("choice.padding.vertical");

        // Arc at the left down to the alternatives.
        TopRightArc leftArcDown = new TopRightArc(solver, "leftarc-down", railColor, arcSize, railWidth);
        addGraphic(leftArcDown);
        solver.addEq(leftArcDown.left, 0, left);
        solver.addEq(leftArcDown.top, 0, connectTop);

        // Vertical line down at the left. Connect arcs to the alternatives horizontally only except for
        // the bottom alternative.
        VertLine leftVer = new VertLine(solver, "left-vert", railColor, railWidth);
        addGraphic(leftVer);
        leftArcDown.connectLine(solver, leftVer);

        // Arc up from the alternatives to the horizontal exit.
        TopLeftArc rightArcDown = new TopLeftArc(solver, "rightarc-up", railColor, arcSize, railWidth);
        addGraphic(rightArcDown);
        solver.addEq(rightArcDown.right, 0, right);
        solver.addEq(rightArcDown.top, 0, connectTop);

        // Vertical line up at the right. Connect arcs from the alternatives horizontally only except for
        // the bottom alternative.
        VertLine rightVer = new VertLine(solver, "right-vert", railColor, railWidth);
        addGraphic(rightVer);
        rightArcDown.connectLine(solver, rightVer);

        // Construct alternatives.
        int i = 0;
        ProxyDiagramElement topElement = null;
        ProxyDiagramElement lastElement = null;
        BottomLeftArc lastLeftArc = null;
        BottomRightArc lastRightArc = null;
        altProxies = listc(alternatives.size());
        for (DiagramElement alt: alternatives) {
            idbg();
            alt.create(config, direction);
            ddbg();
            ProxyDiagramElement altProxy = addDiagramElement(alt, fmt("alt-%d", i));
            altProxies.add(altProxy);

            if (topElement == null) {
                topElement = altProxy; // First element.
                lastElement = altProxy; // and also currently last element.

                // Connect proxie directly to the choice entry/exits.
                HorLine leftHor = new HorLine(solver, "left-alt0", railColor, railWidth);
                addGraphic(leftHor);
                solver.addEq(connectTop, 0, leftHor.top);
                solver.addEq(left, 0, leftHor.left);
                solver.addEq(leftHor.right, 1, altProxy.left);
                solver.addEq(leftHor.top, 0, altProxy.connectTop);
                solver.addLe(leftHor.left, arcSize, leftHor.right); // Make line slightly longer than arc.

                HorLine rightHor = new HorLine(solver, "right-alt0", railColor, railWidth);
                addGraphic(rightHor);
                solver.addEq(rightHor.left, 1, altProxy.right);
                solver.addEq(rightHor.top, 0, connectTop);
                solver.addEq(rightHor.right, 0, right);
                solver.addEq(rightHor.top, 0, altProxy.connectTop);
                solver.addLe(rightHor.left, arcSize, rightHor.right); // Make line slightly longer than arc.

            } else {
                // Not first element.

                // Keep distance from predecessor.
                solver.addLe(lastElement.bottom, choiceVertPadding, altProxy.top);
                lastElement = altProxy; // New last element.

                // If 2nd alternative, connect vertically with topElement to align the latter as well.
                if (i == 1) {
                    if (direction > 0) {
                        solver.addEq(topElement.left, 0, altProxy.left);
                    } else {
                        solver.addEq(topElement.right, 0, altProxy.right);
                    }
                }

                // Construct arcs to connect to and attach them horizontally to the vertical lines at either side.
                lastLeftArc = new BottomLeftArc(solver, fmt("left-arc-%d", i), railColor, arcSize, railWidth);
                addGraphic(lastLeftArc);
                solver.addEq(lastLeftArc.left, 0, leftVer.left);

                lastRightArc = new BottomRightArc(solver, fmt("right-arc-%d", i), railColor, arcSize, railWidth);
                addGraphic(lastRightArc);
                solver.addEq(lastRightArc.right, 0, rightVer.right);

                // Connect the alternatives to the arcs.
                HorLine hLine = new HorLine(solver, fmt("alt-trail-%d", i), railColor, railWidth);
                addGraphic(hLine);
                if (direction > 0) {
                    altProxy.connectLeft(solver, lastLeftArc, railWidth);
                    altProxy.connectRight(solver, hLine);
                    lastRightArc.connectLine(solver, hLine);
                } else {
                    lastLeftArc.connectLine(solver, hLine);
                    altProxy.connectLeft(solver, hLine);
                    altProxy.connectRight(solver, lastRightArc, railWidth);
                }
            }
            i++;
        }

        // Connect first and last element to the choice node top and bottom.
        solver.addLe(top, 0, topElement.top);
        solver.addLe(lastElement.bottom, 0, bottom);

        // Connect last arcs to the vertical line vertically, to force the lines to the correct lengths.
        solver.addEq(leftVer.bottom, 1, lastLeftArc.top);
        solver.addEq(rightVer.bottom, 1, lastRightArc.top);

        boolean dumpEquations = config.getDebugSetting(DebugDisplayKind.EQUATIONS);
        if (dumpEquations && dodbg()) {
            writeDumpHeaderElements(this, alternatives);
            dbg();

            solver.dumpRelations();
            dbg();
        }
        solver.solve("choice", config);

        boolean dumpRelCoords = config.getDebugSetting(DebugDisplayKind.REL_COORDINATES);
        if (dumpRelCoords && dodbg()) {
            writeDumpHeaderElements(this, alternatives);
            dbg();

            if (dumpRelCoords) {
                dumpElementBox();
                dbg();
            }
        }
    }
}
