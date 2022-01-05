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
import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.raildiagrams.util.DumpSupportFunctions.getElementName;
import static org.eclipse.escet.common.raildiagrams.util.DumpSupportFunctions.writeDumpHeaderElements;

import java.awt.Color;
import java.util.List;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.Arc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.VertLine;
import org.eclipse.escet.common.raildiagrams.util.DebugDisplayKind;

/** Node for a sequence of child nodes. */
public class SequenceNode extends DiagramElement {
    /** Elements of the sequence, stored as a list of rows. */
    public final List<SequenceRow> rows;

    /**
     * Constructor of the SequenceNode.java class.
     *
     * @param rows Rows of the sequence.
     * @param id Identifying number of the diagram element.
     */
    public SequenceNode(List<SequenceRow> rows, int id) {
        super("sequence", id);
        this.rows = rows;

        // Initialize the {@link SequencRow#hasNextRow} field.
        SequenceRow lastRow = last(rows);
        for (SequenceRow row: rows) {
            row.hasNextRow = (row != lastRow);
        }
    }

    @SuppressWarnings("null") // False positives with 'verticalLines', as 'i' is larger than one on multiple rows.
    @Override
    public void create(Configuration config, int direction) {
        double railWidth = config.getRailWidth();
        Color railColor = config.getRailColor();
        double firstRowLeadPadding = config.getRealValue("sequence.padding.first-row.prefix");
        double otherRowLeadPadding = config.getRealValue("sequence.padding.other-row.prefix");
        double paddingSuffix = config.getRealValue("sequence.padding.row.suffix");
        double paddingInterrow = config.getRealValue("sequence.padding.interrow");
        double arcRadius = config.getRealValue("sequence.arc-radius");

        // Construct the constraints.
        HorLine connect = null; // Line passed between rows, connecting them.
        List<VertLine> verticalLines = (rows.size() < 2) ? null : listc(rows.size() - 1); // Vertical lines after rows.
        int i = 0;
        for (SequenceRow row: rows) {
            // Solve the child elements of the row.
            String rowNumStr = fmt("%d", i);
            row.create(config, direction, this, rowNumStr);

            // Build the sequence graphics.
            if (direction > 0) {
                connect = (i == 0)
                        ? makeLeftRightFirstRow(row, railColor, railWidth, firstRowLeadPadding, paddingInterrow / 2,
                                arcRadius, verticalLines)
                        : makeLeftRightNextRow(connect, row, railColor, railWidth, otherRowLeadPadding,
                                paddingInterrow / 2, arcRadius, rowNumStr, verticalLines);
            } else {
                connect = (i == 0)
                        ? makeRightLeftFirstRow(row, railColor, railWidth, firstRowLeadPadding, paddingInterrow / 2,
                                arcRadius, verticalLines)
                        : makeRightLeftNextRow(connect, row, railColor, railWidth, otherRowLeadPadding,
                                paddingInterrow / 2, arcRadius, rowNumStr, verticalLines);
            }

            i++;
        }

        SequenceRow lastRow = last(rows);
        Assert.check(!lastRow.hasNextRow); // Last row has no connect line to the next row.
        solver.addLe(lastRow.bottom, 0, bottom);

        // Horizontal line at the end of the sequence. Attach it to the last row.
        HorLine hEnd = new HorLine(solver, "exit-connect", railColor, railWidth);
        addGraphic(hEnd);
        if (direction > 0) {
            solver.addEq(lastRow.right, 0, hEnd.left);
        } else {
            solver.addEq(lastRow.left, 0, hEnd.right);
        }
        solver.addEq(lastRow.connect, 0, hEnd.top);

        if (i == 1) { // One row, simply connect to the edge of the sequence node.
            if (direction > 0) {
                solver.addEq(hEnd.right, 0, right);
            } else {
                solver.addEq(hEnd.left, 0, left);
            }
            solver.addEq(hEnd.top, 0, connectTop);
        } else { // More than one row, make a line upward to the top.
            Arc bottomUp; // Arc at the bottom of the 'up' line.
            Arc topUp; // Arc at the top of the 'up' line.
            if (direction > 0) {
                bottomUp = new BottomRightArc(solver, "end-up", railColor, arcRadius, railWidth);
                topUp = new TopLeftArc(solver, "connect-exit", railColor, arcRadius, railWidth);
            } else {
                bottomUp = new BottomLeftArc(solver, "end-up", railColor, arcRadius, railWidth);
                topUp = new TopRightArc(solver, "connect-exit", railColor, arcRadius, railWidth);
            }
            addGraphics(bottomUp, topUp);

            bottomUp.connectLine(solver, hEnd);
            VertLine upLine = new VertLine(solver, "up-line", railColor, railWidth);
            addGraphic(upLine);
            bottomUp.connectLine(solver, upLine);
            topUp.connectLine(solver, upLine);

            // Connect the top arc to the exit, enforce padding between the vertical line and
            // the vertical lines between rows.
            if (direction > 0) {
                solver.addEq(topUp.right, 0, right);

                for (VertLine rowVert: verticalLines) {
                    solver.addLe(rowVert.right, paddingSuffix, upLine.left);
                }
            } else {
                solver.addEq(topUp.left, 0, left);

                for (VertLine rowVert: verticalLines) {
                    solver.addLe(upLine.right, paddingSuffix, rowVert.left);
                }
            }
            solver.addEq(topUp.top, 0, connectTop);
        }

        solver.solve("sequence", config);

        boolean dumpEquations = config.getDebugSetting(DebugDisplayKind.EQUATIONS);
        boolean dumpRelCoords = config.getDebugSetting(DebugDisplayKind.REL_COORDINATES);
        if ((dumpEquations || dumpRelCoords) && dodbg()) {
            writeDumpHeaderElements(this, null);
            idbg();
            boolean first = true;
            for (SequenceRow row: rows) {
                if (first) {
                    first = false;
                } else {
                    dbg();
                }
                for (DiagramElement element: row.elements) {
                    dbg("Child %s", getElementName(element));
                }
            }
            ddbg();

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
     * Construct the first row for flow from left to right.
     *
     * @param row Row to convert.
     * @param railColor Color of the rail.
     * @param railWidth Width of the rail.
     * @param leadPadding Amount of padding left of the row.
     * @param vertPadding Amount of vertical padding between the bottom of the row and the connection line.
     * @param arcSize Size of the bends.
     * @param verticalLines Storage of vertical lines between rows, expanded in-place.
     * @return Line connecting to the next row if needed else {@code null}.
     */
    private HorLine makeLeftRightFirstRow(SequenceRow row, Color railColor, double railWidth, double leadPadding,
            double vertPadding, double arcSize, List<VertLine> verticalLines)
    {
        // Attach the left side.
        if (leadPadding > 0) {
            HorLine hline = new HorLine(solver, "first-lead", railColor, railWidth);
            addGraphic(hline);

            solver.addEq(left, 0, hline.left);
            solver.addEq(hline.top, 0, connectTop);

            solver.addEq(hline.left, leadPadding, hline.right);

            solver.addEq(row.left, 0, hline.right);
            solver.addEq(row.connect, 0, connectTop);
        } else {
            solver.addEq(left, 0, row.left);
            solver.addEq(connectTop, 0, row.connect);
        }

        return makeLeftRightConnect(row, railColor, railWidth, vertPadding, arcSize, verticalLines);
    }

    /**
     * Construct a connection line from the right end of the row if needed.
     *
     * @param row Row to connect from.
     * @param railColor Color of the rail.
     * @param railWidth Width of the rail.
     * @param vertPadding Amount of vertical padding between the bottom of the row and the connection line.
     * @param arcSize Size of the bends.
     * @param verticalLines Storage of vertical lines between rows, expanded in-place.
     * @return Line connecting to the next row if needed else {@code null}.
     */
    private HorLine makeLeftRightConnect(SequenceRow row, Color railColor, double railWidth, double vertPadding,
            double arcSize, List<VertLine> verticalLines)
    {
        // Add vertical line downward for the next row if needed.
        if (row.hasNextRow) {
            HorLine endLine = new HorLine(solver, "first-trail", railColor, railWidth);
            VertLine vline = new VertLine(solver, "first-down", railColor, railWidth);
            verticalLines.add(vline);
            TopRightArc trArc = new TopRightArc(solver, "end-first", railColor, arcSize, railWidth);
            BottomRightArc brArc = new BottomRightArc(solver, "first-brArc", railColor, arcSize, railWidth);
            HorLine connectLine = new HorLine(solver, "connect-line", railColor, railWidth);
            addGraphics(endLine, vline, trArc, brArc, connectLine);

            row.getRightProxy().connectRight(solver, endLine);
            trArc.connectLine(solver, endLine);
            trArc.connectLine(solver, vline);
            brArc.connectLine(solver, vline);
            brArc.connectLine(solver, connectLine);
            solver.addLe(row.bottom, vertPadding, connectLine.top);
            return connectLine;
        }
        return null;
    }

    /**
     * Construct the first row for flow from right to left.
     *
     * @param row Row to convert.
     * @param railColor Color of the rail.
     * @param railWidth Width of the rail.
     * @param leadPadding Amount of padding right of the row.
     * @param vertPadding Amount of vertical padding between the bottom of the row and the connection line.
     * @param arcSize Size of the bends.
     * @param verticalLines Storage of vertical lines between rows, expanded in-place.
     * @return Line connecting to the next row if needed else {@code null}.
     */
    private HorLine makeRightLeftFirstRow(SequenceRow row, Color railColor, double railWidth, double leadPadding,
            double vertPadding, double arcSize, List<VertLine> verticalLines)
    {
        // Attach the right side.
        if (leadPadding > 0) {
            HorLine hline = new HorLine(solver, "first-lead", railColor, railWidth);
            addGraphic(hline);

            solver.addEq(right, 0, hline.right);
            solver.addEq(hline.top, 0, connectTop);

            solver.addEq(hline.left, leadPadding, hline.right);

            solver.addEq(row.right, 0, hline.left);
            solver.addEq(row.connect, 0, connectTop);
        } else {
            solver.addEq(right, 0, row.right);
            solver.addEq(connectTop, 0, row.connect);
        }
        return makeRightLeftConnect(row, railColor, railWidth, vertPadding, arcSize, verticalLines);
    }

    /**
     * Construct a connection line from the left end of the row if needed.
     *
     * @param row Row to connect from.
     * @param railColor Color of the rail.
     * @param railWidth Width of the rail.
     * @param vertPadding Amount of vertical padding between the bottom of the row and the connection line.
     * @param arcSize Size of the bends.
     * @param verticalLines Storage of vertical lines between rows, expanded in-place.
     * @return Line connecting to the next row if needed else {@code null}.
     */
    private HorLine makeRightLeftConnect(SequenceRow row, Color railColor, double railWidth, double vertPadding,
            double arcSize, List<VertLine> verticalLines)
    {
        if (row.hasNextRow) {
            HorLine endLine = new HorLine(solver, "first-trail", railColor, railWidth);
            VertLine vline = new VertLine(solver, "first-down", railColor, railWidth);
            verticalLines.add(vline);
            TopLeftArc tlArc = new TopLeftArc(solver, "end-first", railColor, arcSize, railWidth);
            BottomLeftArc blArc = new BottomLeftArc(solver, "first-brArc", railColor, arcSize, railWidth);
            HorLine connectLine = new HorLine(solver, "connect-line", railColor, railWidth);
            addGraphics(endLine, vline, tlArc, blArc, connectLine);

            row.getLeftProxy().connectLeft(solver, endLine);
            tlArc.connectLine(solver, endLine);
            tlArc.connectLine(solver, vline);
            blArc.connectLine(solver, vline);
            blArc.connectLine(solver, connectLine);
            solver.addLe(row.bottom, vertPadding, connectLine.top);
            return connectLine;
        }
        return null;
    }

    /**
     * Construct a second or later row for flow from left to right.
     *
     * @param connection Horizontal line above the sequence from the previous row.
     * @param row Row to convert.
     * @param railColor Color of the rail.
     * @param railWidth Width of the rail.
     * @param leadPadding Amount of padding left of the row.
     * @param vertPadding Amount of vertical padding between the bottom of the row and the connection line.
     * @param arcSize Size of the bends.
     * @param rowNumStr Text containing the row number.
     * @param verticalLines Storage of vertical lines between rows, expanded in-place.
     * @return Line connecting to the next row if needed else {@code null}.
     */
    private HorLine makeLeftRightNextRow(HorLine connection, SequenceRow row, Color railColor, double railWidth,
            double leadPadding, double vertPadding, double arcSize, String rowNumStr, List<VertLine> verticalLines)
    {
        // Connect from the previous row through the provided connecting line.
        solver.addLe(connection.bottom, vertPadding, row.top);
        TopLeftArc tlArc = new TopLeftArc(solver, "tl-connect-" + rowNumStr, railColor, arcSize, railWidth);
        tlArc.connectLine(solver, connection);
        VertLine prevline = new VertLine(solver, "lead-vert-" + rowNumStr, railColor, railWidth);
        solver.addEq(left, leadPadding, prevline.left);
        tlArc.connectLine(solver, prevline);
        BottomLeftArc blArc = new BottomLeftArc(solver, "bl-connect-" + rowNumStr, railColor, arcSize, railWidth);
        blArc.connectLine(solver, prevline);
        addGraphics(tlArc, prevline, blArc);

        solver.addEq(row.left, 0, blArc.right);
        solver.addEq(row.connect, railWidth, blArc.bottom);

        return makeLeftRightConnect(row, railColor, railWidth, vertPadding, arcSize, verticalLines);
    }

    /**
     * Construct a second or later row for flow from right to left.
     *
     * @param connection Horizontal line above the sequence from the previous row.
     * @param row Row to convert.
     * @param railColor Color of the rail.
     * @param railWidth Width of the rail.
     * @param leadPadding Amount of padding right of the row.
     * @param vertPadding Amount of vertical padding between the bottom of the row and the connection line.
     * @param arcSize Size of the bends.
     * @param rowNumStr Text containing the row number.
     * @param verticalLines Storage of vertical lines between rows, expanded in-place.
     * @return Line connecting to the next row if needed else {@code null}.
     */
    private HorLine makeRightLeftNextRow(HorLine connection, SequenceRow row, Color railColor, double railWidth,
            double leadPadding, double vertPadding, double arcSize, String rowNumStr, List<VertLine> verticalLines)
    {
        // Connect from the previous row through the provided connecting line.
        solver.addLe(connection.bottom, vertPadding, row.top);

        TopRightArc trArc = new TopRightArc(solver, "tr-connect-" + rowNumStr, railColor, arcSize, railWidth);
        trArc.connectLine(solver, connection);
        VertLine prevline = new VertLine(solver, "lead-vert-" + rowNumStr, railColor, railWidth);
        solver.addEq(prevline.right, leadPadding, right);
        trArc.connectLine(solver, prevline);
        BottomRightArc brArc = new BottomRightArc(solver, "br-connect-" + rowNumStr, railColor, arcSize, railWidth);
        brArc.connectLine(solver, prevline);
        addGraphics(trArc, prevline, brArc);

        solver.addEq(row.right, 0, brArc.left);
        solver.addEq(row.connect, railWidth, brArc.bottom);

        return makeRightLeftConnect(row, railColor, railWidth, vertPadding, arcSize, verticalLines);
    }
}
