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

package org.eclipse.escet.common.raildiagrams.railroad;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.dbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.ddbg;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.idbg;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;

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

/** Node that builds a diagram to pick one of the child nodes. */
public class ChoiceNode extends DiagramElement {
    /** Alternatives of the choice. */
    public final List<DiagramElement> alts;

    /** Proxies of the {@link #alts} elements. */
    private List<ProxyDiagramElement> altProxies;

    /**
     * Choice between alternatives.
     *
     * @param alts Alternatives to choose from.
     * @param id Identifying number of the diagram element.
     */
    public ChoiceNode(List<DiagramElement> alts, int id) {
        super(id);
        this.alts = alts;
        Assert.check(alts.size() > 1);
    }

    @SuppressWarnings("null") // False positives in the 'else', since the 'if' case initializes them first.
    @Override
    public void create(Configuration config, int direction) {
        double railWidth = config.getRailWidth();
        Color railColor = config.getRailColor();
        double arcSize = config.getRealValue("choice.arc-radius");
        double choiceVertPadding = config.getRealValue("choice.padding.vertical");

        altProxies = listc(alts.size());
        ProxyDiagramElement topElement = null;
        TopRightArc leftArcDown = null;
        TopLeftArc rightArcDown = null;

        dbg("computing choice");

        int i = 0;
        ProxyDiagramElement prevElement = null;
        for (DiagramElement alt: alts) {
            idbg();
            alt.create(config, direction);
            ddbg();
            ProxyDiagramElement altProxy = addDiagramElement(alt, fmt("alt-%d", i));
            altProxies.add(altProxy);

            // All alternatives must be within the choice node.
            solver.addLe(top, 0, altProxy.top);
            solver.addLe(altProxy.bottom, 0, bottom);

            if (prevElement == null) {
                topElement = altProxy;

                // First element, add arcs going to the other alternatives.
                solver.addEq(connectTop, 0, altProxy.connectTop);

                double minHorWidth = arcSize * 2 - railWidth; // Minimal length of the entry and exit lines.
                HorLine leftHor = new HorLine(solver, "left-hor", railColor, railWidth);
                addGraphic(leftHor);
                solver.addEq(left, 0, leftHor.left);
                solver.addEq(leftHor.right, 0, altProxy.left);
                solver.addLe(leftHor.left, minHorWidth, leftHor.right);
                solver.addEq(leftHor.top, 0, connectTop);

                HorLine rightHor = new HorLine(solver, "right-hor", railColor, railWidth);
                addGraphic(rightHor);
                solver.addEq(rightHor.left, 0, altProxy.right);
                solver.addEq(rightHor.right, 0, right);
                solver.addLe(rightHor.left, minHorWidth, rightHor.right);
                solver.addEq(connectTop, 0, rightHor.top);

                leftArcDown = new TopRightArc(solver, "leftarc-down", railColor, arcSize, railWidth);
                addGraphic(leftArcDown);
                solver.addEq(leftArcDown.left, 0, left);
                solver.addEq(leftArcDown.top, 0, connectTop);

                rightArcDown = new TopLeftArc(solver, "rightarc-down", railColor, arcSize, railWidth);
                addGraphic(rightArcDown);
                solver.addEq(rightArcDown.right, 0, right);
                solver.addEq(rightArcDown.top, 0, connectTop);
            } else {
                solver.addLe(prevElement.bottom, choiceVertPadding, altProxy.top); // Min distance to previous choice.

                VertLine leftVer = new VertLine(solver, fmt("left-ver-%d", i), railColor, railWidth);
                addGraphic(leftVer);
                leftArcDown.connectLine(solver, leftVer);
                BottomLeftArc leftConnect = new BottomLeftArc(solver, fmt("left-connect-%d", i), railColor, arcSize,
                        railWidth);
                addGraphic(leftConnect);
                leftConnect.connectLine(solver, leftVer);

                VertLine rightVer = new VertLine(solver, fmt("right-ver-%d", i), railColor, railWidth);
                addGraphic(rightVer);
                rightArcDown.connectLine(solver, rightVer);
                BottomRightArc rightConnect = new BottomRightArc(solver, fmt("right-connect-%d", i), railColor, arcSize,
                        railWidth);
                addGraphic(rightConnect);
                rightConnect.connectLine(solver, rightVer);

                HorLine hLine = new HorLine(solver, fmt("alt-hor-%d", i), railColor, railWidth);
                addGraphic(hLine);
                if (direction > 0) {
                    altProxy.connectLeft(solver, leftConnect, railWidth);
                    altProxy.connectRight(solver, hLine);
                    rightConnect.connectLine(solver, hLine);

                    solver.addEq(topElement.left, 0, altProxy.left); // Align all alts at their left side.
                } else {
                    leftConnect.connectLine(solver, hLine);
                    altProxy.connectLeft(solver, hLine);
                    altProxy.connectRight(solver, rightConnect, railWidth);

                    solver.addEq(topElement.right, 0, altProxy.right); // Align all alts at their right side.
                }
            }
            prevElement = altProxy;
            i++;
        }
        solver.solve("choice");
    }
}
