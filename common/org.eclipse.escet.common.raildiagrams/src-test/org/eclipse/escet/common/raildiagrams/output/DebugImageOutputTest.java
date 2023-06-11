//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.output;

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.raildiagrams.output.DebugImageOutput.BACKGROUND;
import static org.eclipse.escet.common.raildiagrams.output.DebugImageOutput.CONNECT_POINT;
import static org.eclipse.escet.common.raildiagrams.output.DebugImageOutput.SINGLE_RAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.Arc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.VertLine;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for the {@link DebugImageOutput} class. */
public class DebugImageOutputTest {
    /** If not {@code null} the directory to dump arc test images for manual inspection. */
    private final String testDumpDir = null;

    /** The debug logger to use. */
    private final Consumer<String> debugLogger = null;

    /** Output generator instance for testing. */
    private ImageOutput output;

    /** Constraint solver. */
    Solver solver;

    @BeforeEach
    @SuppressWarnings("javadoc")
    public void setup() {
        output = new DebugImageOutput();
        solver = new Solver();
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testScratchColorOverride() {
        assertEquals(convertRGB(Color.WHITE), convertRGB(output.getOverrideColor("diagram.background.color")));
        assertEquals(convertRGB(Color.BLACK), convertRGB(output.getOverrideColor("something.else")));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEmptyOutput() {
        output.prepareOutputFile(64, 32, Color.WHITE);
        BufferedImage result = output.getOutput();
        assertEquals(64, result.getWidth());
        assertEquals(32, result.getHeight());
        assertEquals(convertRGB(BACKGROUND), convertRGB(result.getRGB(7, 5)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEmptyHorLine() throws IOException {
        final int lineWidth = 1;
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        HorLine hLine = new HorLine(solver, "prefix", Color.BLACK, lineWidth);
        solver.solve("hl", config);

        output.addGraphic(3, 5, solver, hLine);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestHLineimg0_%d.png", testDumpDir, File.separator, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(2, 5)));
        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(3, 5)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testHorLineOneDistance() throws IOException {
        final int lineWidth = 1;
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        HorLine hLine = new HorLine(solver, "prefix", Color.BLACK, lineWidth);
        solver.addLe(hLine.left, 0, hLine.right);
        solver.solve("hl", config);

        output.addGraphic(3, 5, solver, hLine);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestHLineimg1_%d.png", testDumpDir, File.separator, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(2, 5)));
        assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(3, 5)));
        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(4, 5)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testHorLineTwoDistance() throws IOException {
        final int lineWidth = 1;
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        HorLine hLine = new HorLine(solver, "prefix", Color.BLACK, lineWidth);
        solver.addLe(hLine.left, 1, hLine.right);
        solver.solve("prefix", config);

        output.addGraphic(3, 5, solver, hLine);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestHLineimg2_%d.png", testDumpDir, File.separator, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(2, 5)));
        assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(3, 5)));
        assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(4, 5)));
        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(5, 5)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testEmptyVertLine() throws IOException {
        final int lineWidth = 1;
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        VertLine vLine = new VertLine(solver, "prefix", Color.BLACK, lineWidth);
        solver.solve("prefix", config);

        output.addGraphic(3, 5, solver, vLine);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestVLineimg0_%d.png", testDumpDir, File.separator, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(3, 4)));
        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(3, 5)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVertLineOneDistance() throws IOException {
        final int lineWidth = 1;
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        VertLine vLine = new VertLine(solver, "prefix", Color.BLACK, lineWidth);
        solver.addLe(vLine.top, 0, vLine.bottom);
        solver.solve("prefix", config);

        output.addGraphic(3, 5, solver, vLine);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestVLineimg1_%d.png", testDumpDir, File.separator, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(3, 4)));
        assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(3, 5)));
        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(3, 6)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testVertLineTwoDistance() throws IOException {
        final int lineWidth = 1;
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        VertLine vLine = new VertLine(solver, "prefix", Color.BLACK, lineWidth);
        solver.addLe(vLine.top, 1, vLine.bottom);
        solver.solve("prefix", config);

        output.addGraphic(3, 5, solver, vLine);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestVLineimg2_%d.png", testDumpDir, File.separator, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(3, 4)));
        assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(3, 5)));
        assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(3, 6)));
        assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(3, 7)));
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBottomRightArc11w1() throws IOException {
        tryBottomRightArc(11, 1);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBottomRightArc10w2() throws IOException {
        tryBottomRightArc(10, 2);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBottomRightArc14w3() throws IOException {
        tryBottomRightArc(14, 3);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBottomRightArc15w4() throws IOException {
        tryBottomRightArc(15, 4);
    }

    /**
     * Verify bottom-right arc for the given size and line-width.
     *
     * @param size Size of the arc.
     * @param lineWidth Width of the line.
     * @throws IOException In case of an I/O error.
     */
    private void tryBottomRightArc(int size, int lineWidth) throws IOException {
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        final int xpos = 3;
        final int ypos = 5;

        final int inTop = ypos;
        final int inBot = ypos + size - 1;
        final int inLeft = xpos;
        final int inRight = xpos + size - 1;

        Arc arc = new BottomRightArc(solver, "arc", Color.BLACK, size, lineWidth);
        solver.solve("prefix", config);

        output.addGraphic(xpos, ypos, solver, arc);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestBRimg_%d_%d.png", testDumpDir, File.separator, size, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        for (int i = 0; i < lineWidth; i++) {
            assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(inLeft, inBot - i)));
            assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(inRight - i, inTop)));
            assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(inLeft - 1, inBot - i)));
            assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(inRight - i, inTop - 1)));
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTopLeftArc11w1() throws IOException {
        tryTopLeftArc(11, 1);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTopLeftArc10w2() throws IOException {
        tryTopLeftArc(10, 2);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTopLeftArc14w3() throws IOException {
        tryTopLeftArc(14, 3);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTopLeftArc15w4() throws IOException {
        tryTopLeftArc(15, 4);
    }

    /**
     * Verify top-left arc for the given size and line-width.
     *
     * @param size Size of the arc.
     * @param lineWidth Width of the line.
     * @throws IOException In case of an I/O error.
     */
    private void tryTopLeftArc(int size, int lineWidth) throws IOException {
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        final int xpos = 3;
        final int ypos = 5;

        final int inTop = ypos;
        final int inBot = ypos + size - 1;
        final int inLeft = xpos;
        final int inRight = xpos + size - 1;

        Arc arc = new TopLeftArc(solver, "arc", Color.BLACK, size, lineWidth);
        solver.solve("prefix", config);

        output.addGraphic(xpos, ypos, solver, arc);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestTLimg_%d_%d.png", testDumpDir, File.separator, size, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        for (int i = 0; i < lineWidth; i++) {
            assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(inLeft + i, inBot)));
            assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(inRight, inTop + i)));
            assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(inLeft + i, inBot + 1)));
            assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(inRight + 1, inTop + i)));
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBottomLeftArc11w1() throws IOException {
        tryBottomLeftArc(11, 1);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBottomLeftArc10w2() throws IOException {
        tryBottomLeftArc(10, 2);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBottomLeftArc14w3() throws IOException {
        tryBottomLeftArc(14, 3);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testBottomLeftArc15w4() throws IOException {
        tryBottomLeftArc(15, 4);
    }

    /**
     * Verify bottom-left arc for the given size and line-width.
     *
     * @param size Size of the arc.
     * @param lineWidth Width of the line.
     * @throws IOException In case of an I/O error.
     */
    private void tryBottomLeftArc(int size, int lineWidth) throws IOException {
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        final int xpos = 3;
        final int ypos = 5;

        final int inTop = ypos;
        final int inBot = ypos + size - 1;
        final int inLeft = xpos;
        final int inRight = xpos + size - 1;

        Arc arc = new BottomLeftArc(solver, "arc", Color.BLACK, size, lineWidth);
        solver.solve("prefix", config);

        output.addGraphic(xpos, ypos, solver, arc);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestBLimg_%d_%d.png", testDumpDir, File.separator, size, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        for (int i = 0; i < lineWidth; i++) {
            assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(inLeft + i, inTop)));
            assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(inRight, inBot - i)));
            assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(inLeft + i, inTop - 1)));
            assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(inRight + 1, inBot - i)));
        }
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTopRightArc11w1() throws IOException {
        tryTopRightArc(11, 1);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTopRightArc10w2() throws IOException {
        tryTopRightArc(10, 2);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTopRightArc14w3() throws IOException {
        tryTopRightArc(14, 3);
    }

    @Test
    @SuppressWarnings("javadoc")
    public void testTopRightArc15w4() throws IOException {
        tryTopRightArc(15, 4);
    }

    /**
     * Verify top-right arc for the given size and line-width.
     *
     * @param size Size of the arc.
     * @param lineWidth Width of the line.
     * @throws IOException In case of an I/O error.
     */
    private void tryTopRightArc(int size, int lineWidth) throws IOException {
        Configuration config = new Configuration(output, debugLogger);

        output.prepareOutputFile(64, 32, Color.WHITE);
        final int xpos = 3;
        final int ypos = 5;

        final int inTop = ypos;
        final int inBot = ypos + size - 1;
        final int inLeft = xpos;
        final int inRight = xpos + size - 1;

        Arc arc = new TopRightArc(solver, "arc", Color.BLACK, size, lineWidth);
        solver.solve("prefix", config);

        output.addGraphic(xpos, ypos, solver, arc);
        BufferedImage result = output.getOutput();
        if (testDumpDir != null) {
            String fname = fmt("%s%stestTRimg_%d_%d.png", testDumpDir, File.separator, size, lineWidth);
            output.writeOutputFile(Paths.get(fname));
        }

        for (int i = 0; i < lineWidth; i++) {
            assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(inLeft, inTop + i)));
            assertEquals(convertRGB(SINGLE_RAIL), convertRGB(result.getRGB(inRight - i, inBot)));
            assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(inLeft - 1, inTop + i)));
            assertEquals(convertRGB(CONNECT_POINT), convertRGB(result.getRGB(inRight - i, inBot + 1)));
        }
    }

    /**
     * Convert RGB values to useful names or numbers.
     *
     * @param col Color to convert.
     * @return The textual representation of the color.
     */
    private static String convertRGB(Color col) {
        return convertRGB(col.getRGB());
    }

    /**
     * Convert RGB values to useful names or numbers.
     *
     * @param rgbVal Value to convert.
     * @return The textual representation of the color.
     */
    private static String convertRGB(int rgbVal) {
        switch (rgbVal) {
            case SINGLE_RAIL:
                return "SINGLE_RAIL";
            case CONNECT_POINT:
                return "CONNECT_POINT";
            case BACKGROUND:
                return "BACKGROUND";
            default: {
                String s = Integer.toHexString(rgbVal);
                return "0x0000000".substring(0, 10 - s.length()) + s;
            }
        }
    }
}
