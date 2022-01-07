//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.output.tests;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.raildiagrams.output.DebugImageOutput.BACKGROUND;
import static org.eclipse.escet.common.raildiagrams.output.DebugImageOutput.CONNECT_POINT;
import static org.eclipse.escet.common.raildiagrams.output.DebugImageOutput.SINGLE_RAIL;
import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.Arc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.VertLine;
import org.eclipse.escet.common.raildiagrams.output.DebugImageOutput;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.junit.Before;
import org.junit.Test;

/** Tests for the {@link DebugImageOutput} class. */
public class DebugImageOutputTest {
    /** If not {@code null} the directory to dump arc test images for manual inspection. */
    private final String testDumpDir = null;

    /** Output generator instance for testing. */
    private DebugImageOutput dbgOutput;

    /** Constraint solver. */
    Solver solver;

    /** */
    @Before
    public void setup() {
        dbgOutput = new DebugImageOutput();
        solver = new Solver();
    }

    /** */
    @Test
    public void testScratchColorOverride() {
        assertEquals(Color.WHITE, dbgOutput.getOverrideColor("diagram.background.color"));
        assertEquals(Color.BLACK, dbgOutput.getOverrideColor("something.else"));
    }

    /** */
    @Test
    public void testEmptyOutput() {
        dbgOutput.prepareOutputFile(64, 32, null);
        BufferedImage result = dbgOutput.getOutput();
        assertEquals(64, result.getWidth());
        assertEquals(32, result.getHeight());
        assertEquals(BACKGROUND, result.getRGB(7, 5));
    }

    /** */
    @Test
    public void testEmptyHorLine() {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        HorLine hLine = new HorLine(solver, "prefix", Color.BLACK, 1);
        solver.solve("hl", config);

        dbgOutput.addGraphic(3, 5, solver, hLine);
        BufferedImage result = dbgOutput.getOutput();
        assertEquals(CONNECT_POINT, result.getRGB(2, 5));
        assertEquals(CONNECT_POINT, result.getRGB(3, 5));
    }

    /** */
    @Test
    public void testHorLineOneDistance() {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        HorLine hLine = new HorLine(solver, "prefix", Color.BLACK, 1);
        solver.addLe(hLine.left, 1, hLine.right);
        solver.solve("hl", config);

        dbgOutput.addGraphic(3, 5, solver, hLine);
        BufferedImage result = dbgOutput.getOutput();
        assertEquals(CONNECT_POINT, result.getRGB(2, 5));
        assertEquals(SINGLE_RAIL, result.getRGB(3, 5));
        assertEquals(CONNECT_POINT, result.getRGB(4, 5));
    }

    /** */
    @Test
    public void testHorLineTwoDistance() {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        HorLine hLine = new HorLine(solver, "prefix", Color.BLACK, 1);
        solver.addLe(hLine.left, 2, hLine.right);
        solver.solve("prefix", config);

        dbgOutput.addGraphic(3, 5, solver, hLine);
        BufferedImage result = dbgOutput.getOutput();
        assertEquals(CONNECT_POINT, result.getRGB(2, 5));
        assertEquals(SINGLE_RAIL, result.getRGB(3, 5));
        assertEquals(SINGLE_RAIL, result.getRGB(4, 5));
        assertEquals(CONNECT_POINT, result.getRGB(5, 5));
    }

    /** */
    @Test
    public void testEmptyVertLine() {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        VertLine vLine = new VertLine(solver, "prefix", Color.BLACK, 1);
        solver.solve("prefix", config);

        dbgOutput.addGraphic(3, 5, solver, vLine);
        BufferedImage result = dbgOutput.getOutput();
        assertEquals(CONNECT_POINT, result.getRGB(3, 4));
        assertEquals(CONNECT_POINT, result.getRGB(3, 5));
    }

    /** */
    @Test
    public void testVertLineOneDistance() {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        VertLine vLine = new VertLine(solver, "prefix", Color.BLACK, 1);
        solver.addLe(vLine.top, 1, vLine.bottom);
        solver.solve("prefix", config);

        dbgOutput.addGraphic(3, 5, solver, vLine);
        BufferedImage result = dbgOutput.getOutput();
        assertEquals(CONNECT_POINT, result.getRGB(3, 4));
        assertEquals(SINGLE_RAIL, result.getRGB(3, 5));
        assertEquals(CONNECT_POINT, result.getRGB(3, 6));
    }

    /** */
    @Test
    public void testVertLineTwoDistance() {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        VertLine vLine = new VertLine(solver, "prefix", Color.BLACK, 1);
        solver.addLe(vLine.top, 2, vLine.bottom);
        solver.solve("prefix", config);

        dbgOutput.addGraphic(3, 5, solver, vLine);
        BufferedImage result = dbgOutput.getOutput();
        assertEquals(CONNECT_POINT, result.getRGB(3, 4));
        assertEquals(SINGLE_RAIL, result.getRGB(3, 5));
        assertEquals(SINGLE_RAIL, result.getRGB(3, 6));
        assertEquals(CONNECT_POINT, result.getRGB(3, 7));
    }

    /** */
    @Test
    public void testBottomRightArc11w1() {
        tryBottomRightArc(11, 1);
    }

    /** */
    @Test
    public void testBottomRightArc10w2() {
        tryBottomRightArc(10, 2);
    }

    /** */
    @Test
    public void testBottomRightArc14w3() {
        tryBottomRightArc(14, 3);
    }

    /** */
    @Test
    public void testBottomRightArc15w4() {
        tryBottomRightArc(15, 4);
    }

    /**
     * Verify bottom-right arc for the given size and line-width.
     *
     * @param size Size of the arc.
     * @param lineWidth Width of the line.
     */
    private void tryBottomRightArc(int size, int lineWidth) {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        final int xpos = 3;
        final int ypos = 5;

        final int inTop = ypos;
        final int inBot = ypos + size - 1;
        final int inLeft = xpos;
        final int inRight = xpos + size - 1;

        Arc arc = new BottomRightArc(solver, "arc", Color.BLACK, size, lineWidth);
        solver.solve("prefix", config);

        dbgOutput.addGraphic(xpos, ypos, solver, arc);
        BufferedImage result = dbgOutput.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestBRimg_%d_%d.png", testDumpDir, File.separator, size, lineWidth);
            dbgOutput.writeOutputFile(fname);
        }
        for (int i = 0; i < lineWidth; i++) {
            assertEquals(SINGLE_RAIL, result.getRGB(inLeft, inBot - i));
            assertEquals(SINGLE_RAIL, result.getRGB(inRight - i, inTop));
            assertEquals(CONNECT_POINT, result.getRGB(inLeft - 1, inBot - i));
            assertEquals(CONNECT_POINT, result.getRGB(inRight - i, inTop - 1));
        }
    }

    /** */
    @Test
    public void testTopLeftArc11w1() {
        tryTopLeftArc(11, 1);
    }

    /** */
    @Test
    public void testTopLeftArc10w2() {
        tryTopLeftArc(10, 2);
    }

    /** */
    @Test
    public void testTopLeftArc14w3() {
        tryTopLeftArc(14, 3);
    }

    /** */
    @Test
    public void testTopLeftArc15w4() {
        tryTopLeftArc(15, 4);
    }

    /**
     * Verify top-left arc for the given size and line-width.
     *
     * @param size Size of the arc.
     * @param lineWidth Width of the line.
     */
    private void tryTopLeftArc(int size, int lineWidth) {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        final int xpos = 3;
        final int ypos = 5;

        final int inTop = ypos;
        final int inBot = ypos + size - 1;
        final int inLeft = xpos;
        final int inRight = xpos + size - 1;

        Arc arc = new TopLeftArc(solver, "arc", Color.BLACK, size, lineWidth);
        solver.solve("prefix", config);

        dbgOutput.addGraphic(xpos, ypos, solver, arc);
        BufferedImage result = dbgOutput.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestTLimg_%d_%d.png", testDumpDir, File.separator, size, lineWidth);
            dbgOutput.writeOutputFile(fname);
        }
        for (int i = 0; i < lineWidth; i++) {
            assertEquals(SINGLE_RAIL, result.getRGB(inLeft + i, inBot));
            assertEquals(SINGLE_RAIL, result.getRGB(inRight, inTop + i));
            assertEquals(CONNECT_POINT, result.getRGB(inLeft + i, inBot + 1));
            assertEquals(CONNECT_POINT, result.getRGB(inRight + 1, inTop + i));
        }
    }

    /** */
    @Test
    public void testBottomLeftArc11w1() {
        tryBottomLeftArc(11, 1);
    }

    /** */
    @Test
    public void testBottomLeftArc10w2() {
        tryBottomLeftArc(10, 2);
    }

    /** */
    @Test
    public void testBottomLeftArc14w3() {
        tryBottomLeftArc(14, 3);
    }

    /** */
    @Test
    public void testBottomLeftArc15w4() {
        tryBottomLeftArc(15, 4);
    }

    /**
     * Verify bottom-left arc for the given size and line-width.
     *
     * @param size Size of the arc.
     * @param lineWidth Width of the line.
     */
    private void tryBottomLeftArc(int size, int lineWidth) {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        final int xpos = 3;
        final int ypos = 5;

        final int inTop = ypos;
        final int inBot = ypos + size - 1;
        final int inLeft = xpos;
        final int inRight = xpos + size - 1;

        Arc arc = new BottomLeftArc(solver, "arc", Color.BLACK, size, lineWidth);
        solver.solve("prefix", config);

        dbgOutput.addGraphic(xpos, ypos, solver, arc);
        BufferedImage result = dbgOutput.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestBLimg_%d_%d.png", testDumpDir, File.separator, size, lineWidth);
            dbgOutput.writeOutputFile(fname);
        }
        for (int i = 0; i < lineWidth; i++) {
            assertEquals(SINGLE_RAIL, result.getRGB(inLeft + i, inTop));
            assertEquals(SINGLE_RAIL, result.getRGB(inRight, inBot - i));
            assertEquals(CONNECT_POINT, result.getRGB(inLeft + i, inTop - 1));
            assertEquals(CONNECT_POINT, result.getRGB(inRight + 1, inBot - i));
        }
    }

    /** */
    @Test
    public void testTopRightArc11w1() {
        tryTopRightArc(11, 1);
    }

    /** */
    @Test
    public void testTopRightArc10w2() {
        tryTopRightArc(10, 2);
    }

    /** */
    @Test
    public void testTopRightArc14w3() {
        tryTopRightArc(14, 3);
    }

    /** */
    @Test
    public void testTopRightArc15w4() {
        tryTopRightArc(15, 4);
    }

    /**
     * Verify top-right arc for the given size and line-width.
     *
     * @param size Size of the arc.
     * @param lineWidth Width of the line.
     */
    private void tryTopRightArc(int size, int lineWidth) {
        Configuration config = new Configuration(dbgOutput);

        dbgOutput.prepareOutputFile(64, 32, null);
        final int xpos = 3;
        final int ypos = 5;

        final int inTop = ypos;
        final int inBot = ypos + size - 1;
        final int inLeft = xpos;
        final int inRight = xpos + size - 1;

        Arc arc = new TopRightArc(solver, "arc", Color.BLACK, size, lineWidth);
        solver.solve("prefix", config);

        dbgOutput.addGraphic(xpos, ypos, solver, arc);
        BufferedImage result = dbgOutput.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestTRimg_%d_%d.png", testDumpDir, File.separator, size, lineWidth);
            dbgOutput.writeOutputFile(fname);
        }
        for (int i = 0; i < lineWidth; i++) {
            assertEquals(SINGLE_RAIL, result.getRGB(inLeft, inTop + i));
            assertEquals(SINGLE_RAIL, result.getRGB(inRight - i, inBot));
            assertEquals(CONNECT_POINT, result.getRGB(inLeft - 1, inTop + i));
            assertEquals(CONNECT_POINT, result.getRGB(inRight - i, inBot + 1));
        }
    }
}
