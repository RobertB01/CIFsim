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
import static org.eclipse.escet.common.app.framework.output.OutputProvider.dodbg;
import static org.eclipse.escet.common.raildiagrams.util.DumpSupportFunctions.writeDumpHeaderElements;

import java.awt.Color;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.config.FontData;
import org.eclipse.escet.common.raildiagrams.config.NameKind;
import org.eclipse.escet.common.raildiagrams.config.TextSizeOffset;
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TextArea;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.VertLine;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;
import org.eclipse.escet.common.raildiagrams.util.Size2D;

/** A node referring to another diagram. */
public class NamedNode extends DiagramElement {
    /**
     * Name of the node to refer to, {@code null} if {@link #text} is not
     * {@code null}.
     */
    public final String name;

    /** Text of the node, {@code null} if {@link #name} is not {@code null}. */
    public final String text;

    /**
     * Constructor of the {@link NamedNode} class.
     *
     * @param name Name of the node to refer to.
     * @param id   Identifying number of the diagram element.
     */
    public NamedNode(String name, int id) {
        this(name, null, id);
    }

    /**
     * Constructor of the {@link NamedNode} class.
     *
     * <p>
     * One of the parameters must be {@code null} and one of the parameters must be
     * non-{@code null}.
     * </p>
     *
     * @param name Name of the node to refer to.
     * @param text Text of the node.
     * @param id   Identifying number of the diagram element.
     */
    public NamedNode(String name, String text, int id) {
        super("name", id);
        this.name = name;
        this.text = text;

        Assert.check((name != null && text == null) || (name == null && text != null));
    }

    @Override
    public void create(Configuration config, int direction) {
        NameKind nameKind = (name == null) ? NameKind.TERMINAL : config.getNameKind(name);
        String text = (name == null) ? this.text : config.getNameText(name, nameKind);

        double railwidth = config.getRailWidth();
        Color railColor = config.getRailColor();
        double horPadding = config.getRealValue(nameKind.configPrefix + ".name.padding.horizontal");
        double vertPadding = config.getRealValue(nameKind.configPrefix + ".name.padding.vertical");
        double entryLength = config.getRealValue("name.rail.entry.width");
        double exitLength = config.getRealValue("name.rail.exit.width");
        double cornerRadius = config.getCornerRadius(nameKind);
        double boxLineWidth = config.getBoxLineWidth(nameKind);
        Color boxColor = config.getBoxColor(nameKind);

        // Construct the text graphic itself.
        FontData font = config.getFont(nameKind);
        Color textColor = config.getTextColor(nameKind);
        TextSizeOffset textSizeOffset = config.getTextSizeOffset(text, nameKind);
        Size2D textSize = textSizeOffset.size;
        TextArea textArea = new TextArea(solver, "named-text", text, textColor, font, textSizeOffset.offset, textSize);
        addGraphic(textArea);
        solver.addEq(textArea.top, textSize.height / 2 - railwidth / 2, connectTop);

        // Compute minimal needed padding in both directions around the text such that
        // text area does
        // not conflict with arc area.
        // Simple approach is to keep both areas fully disjunct, but that makes a named
        // node large
        // as the corner radius increases. The solution below allows for some overlap in
        // the unused
        // arc area.
        double minPadding; // Minimal amount of needed padding due to possibly rounded corners.
        if (cornerRadius <= 0) {
            minPadding = 1; // Give the user a lot of control.
        } else {
            // For rounded corners, free space is limited by the innermost arc, which is
            // 'railwidth'
            // distance from the outside of the arc.
            //
            // Maximum usable distance along the box edges from the center point.
            // Subtracting 3 to ensure
            // some (small) empty space between text and the arc even if it uses its corner.
            double freeAmount = Math.max(Math.sqrt(2) * 0.5 * (cornerRadius - railwidth), 0) - 3;
            minPadding = cornerRadius - freeAmount;
        }
        vertPadding = Math.max(vertPadding, minPadding);
        horPadding = Math.max(horPadding, minPadding);

        // Create the 4 lines around the name, and give them to the proper distance from
        // the name.
        HorLine topLine = new HorLine(solver, "name-top-line", boxColor, boxLineWidth);
        HorLine bottomLine = new HorLine(solver, "name-bottom-line", boxColor, boxLineWidth);
        VertLine leftLine = new VertLine(solver, "name-left-line", boxColor, boxLineWidth);
        VertLine rightLine = new VertLine(solver, "name-right-line", boxColor, boxLineWidth);
        addGraphics(topLine, bottomLine, leftLine, rightLine);

        solver.addLe(topLine.bottom, vertPadding, textArea.top);
        solver.addLe(textArea.bottom, vertPadding, bottomLine.top);
        solver.addLe(leftLine.right, horPadding, textArea.left);
        solver.addLe(textArea.right, horPadding, rightLine.left);

        if (cornerRadius <= 0) {
            // No rounded corners.
            connectHorVert(topLine, leftLine, true, true);
            connectHorVert(topLine, rightLine, false, true);
            connectHorVert(bottomLine, leftLine, true, false);
            connectHorVert(bottomLine, rightLine, false, false);
        } else {
            // With rounded corner. Padding around the text was already adjusted to prevent
            // collisions.
            TopLeftArc tlArc = new TopLeftArc(solver, "name-tlarc", boxColor, cornerRadius, boxLineWidth);
            tlArc.connectLine(solver, topLine);
            tlArc.connectLine(solver, leftLine);
            TopRightArc trArc = new TopRightArc(solver, "name-trarc", boxColor, cornerRadius, boxLineWidth);
            trArc.connectLine(solver, topLine);
            trArc.connectLine(solver, rightLine);
            BottomRightArc brArc = new BottomRightArc(solver, "name-brarc", boxColor, cornerRadius, boxLineWidth);
            brArc.connectLine(solver, rightLine);
            brArc.connectLine(solver, bottomLine);
            BottomLeftArc blArc = new BottomLeftArc(solver, "name-blarc", boxColor, cornerRadius, boxLineWidth);
            blArc.connectLine(solver, bottomLine);
            blArc.connectLine(solver, leftLine);

            addGraphics(tlArc, trArc, blArc, brArc);
        }

        // Entry and exit connections.
        HorLine entryLine = new HorLine(solver, "name-entry", railColor, railwidth);
        solver.addEq(entryLine.left, entryLength, entryLine.right);
        solver.addEq(leftLine.left, 0, entryLine.right);
        addGraphic(entryLine);

        HorLine exitLine = new HorLine(solver, "name-entry", railColor, railwidth);
        solver.addEq(exitLine.left, exitLength, exitLine.right);
        solver.addEq(rightLine.right, 0, exitLine.left);
        addGraphics(exitLine);

        // Diagram element connections.
        solver.addEq(entryLine.top, 0, connectTop);
        solver.addEq(exitLine.top, 0, connectTop);
        solver.addEq(left, 0, entryLine.left);
        solver.addEq(right, 0, exitLine.right);
        solver.addEq(top, 0, topLine.top);
        solver.addEq(bottom, 0, bottomLine.bottom);

        solver.solve("named-node", config);

        boolean dumpEquations = config.getDebugSetting(DebugDisplayKind.EQUATIONS);
        boolean dumpRelCoords = config.getDebugSetting(DebugDisplayKind.REL_COORDINATES);
        if ((dumpEquations || dumpRelCoords) && dodbg()) {
            writeDumpHeaderElements(this, null);
            dbg();

            if (dumpEquations) {
                solver.dumpRelations();
                dbg();
            }
            if (dumpRelCoords) {
                dumpElementBox();
                dbg();
            }
        }
    }

    /**
     * Connect an end-point of a horizontal line with an end-point of a vertical
     * line.
     *
     * @param hl          Horizontal line to connect.
     * @param vl          Vertical line to connect.
     * @param connectLeft If set, connect the left side of both lines, else the
     *                    right side.
     * @param connectTop  If set, connect the top side of both lines, else the
     *                    bottom side.
     */
    private void connectHorVert(HorLine hl, VertLine vl, boolean connectLeft, boolean connectTop) {
        if (connectLeft) {
            solver.addEq(hl.left, 0, vl.left);
        } else {
            solver.addEq(hl.right, 0, vl.right);
        }

        if (connectTop) {
            solver.addEq(hl.top, 0, vl.top);
        } else {
            solver.addEq(hl.bottom, 0, vl.bottom);
        }
    }
}
