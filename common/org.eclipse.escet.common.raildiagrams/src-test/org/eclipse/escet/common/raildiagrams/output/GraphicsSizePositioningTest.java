//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.util.function.Consumer;

import org.eclipse.escet.common.raildiagrams.config.Configuration;
import org.eclipse.escet.common.raildiagrams.graphics.Arc;
import org.eclipse.escet.common.raildiagrams.graphics.Area;
import org.eclipse.escet.common.raildiagrams.graphics.BottomLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.BottomRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.HorLine;
import org.eclipse.escet.common.raildiagrams.graphics.TextArea;
import org.eclipse.escet.common.raildiagrams.graphics.TopLeftArc;
import org.eclipse.escet.common.raildiagrams.graphics.TopRightArc;
import org.eclipse.escet.common.raildiagrams.graphics.VertLine;
import org.eclipse.escet.common.raildiagrams.solver.Solver;
import org.eclipse.escet.common.raildiagrams.solver.Variable;
import org.eclipse.escet.common.raildiagrams.util.Position2D;
import org.eclipse.escet.common.raildiagrams.util.Size2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Test graphics sizes and positioning. */
public class GraphicsSizePositioningTest {
    /** Solver for deciding position of graphics. */
    private Solver solver;

    /** Default configuration. */
    private Configuration config;

    @SuppressWarnings("javadoc")
    @BeforeEach
    public void setup() {
        OutputTarget target = new NormalImageOutput();
        Consumer<String> debugLogger = null;
        config = new Configuration(target, debugLogger);
        solver = new Solver();
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testTLarcSize17w1() {
        final int size = 17;
        final int lineWidth = 1;
        Area a = new TopLeftArc(solver, "x", Color.WHITE, size, lineWidth);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, 0);
        assertEquals(top, 0);
        assertEquals(right, size - 1);
        assertEquals(bottom, size - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testTRarcSize17w1() {
        final int size = 17;
        final int lineWidth = 1;
        Area a = new TopRightArc(solver, "x", Color.WHITE, size, lineWidth);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, 0);
        assertEquals(top, 0);
        assertEquals(right, size - 1);
        assertEquals(bottom, size - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testBLarcSize17w1() {
        final int size = 17;
        final int lineWidth = 1;
        Area a = new BottomLeftArc(solver, "x", Color.WHITE, size, lineWidth);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, 0);
        assertEquals(top, 0);
        assertEquals(right, size - 1);
        assertEquals(bottom, size - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testBRarcSize17w1() {
        final int size = 17;
        final int lineWidth = 1;
        Area a = new BottomRightArc(solver, "x", Color.WHITE, size, lineWidth);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, 0);
        assertEquals(top, 0);
        assertEquals(right, size - 1);
        assertEquals(bottom, size - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testTLarcSize17w3() {
        final int size = 17;
        final int lineWidth = 3;
        Area a = new TopLeftArc(solver, "x", Color.WHITE, size, lineWidth);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, 0);
        assertEquals(top, 0);
        assertEquals(right, size - 1);
        assertEquals(bottom, size - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testTRarcSize17w3() {
        final int size = 17;
        final int lineWidth = 3;
        Area a = new TopRightArc(solver, "x", Color.WHITE, size, lineWidth);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, 0);
        assertEquals(top, 0);
        assertEquals(right, size - 1);
        assertEquals(bottom, size - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testBLarcSize17w3() {
        final int size = 17;
        final int lineWidth = 3;
        Area a = new BottomLeftArc(solver, "x", Color.WHITE, size, lineWidth);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, 0);
        assertEquals(top, 0);
        assertEquals(right, size - 1);
        assertEquals(bottom, size - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testBRarcSize17w3() {
        final int size = 17;
        final int lineWidth = 3;
        Area a = new BottomRightArc(solver, "x", Color.WHITE, size, lineWidth);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, 0);
        assertEquals(top, 0);
        assertEquals(right, size - 1);
        assertEquals(bottom, size - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testHLineDefaultLengthW3() {
        final int lineWidth = 3;
        final Position2D base = new Position2D(10, 10);
        Variable baseLeft = solver.newVar("baseLeft");
        Variable baseTop = solver.newVar("baseTop");

        Area a = new HorLine(solver, "x", Color.WHITE, lineWidth);
        // Push TL of the line to base to keep all variables non-negative.
        solver.addEq(baseLeft, base.x, a.left);
        solver.addEq(baseTop, base.y, a.top);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, base.x);
        assertEquals(top, base.y);
        assertEquals(right, base.x - 1);
        assertEquals(bottom, base.y + lineWidth - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testVLineDefaultLengthW3() {
        final int lineWidth = 3;
        final Position2D base = new Position2D(10, 10);
        Variable baseLeft = solver.newVar("baseLeft");
        Variable baseTop = solver.newVar("baseTop");

        Area a = new VertLine(solver, "x", Color.WHITE, lineWidth);
        // Push TL of the line to base to keep all variables non-negative.
        solver.addEq(baseLeft, base.x, a.left);
        solver.addEq(baseTop, base.y, a.top);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, base.x);
        assertEquals(top, base.y);
        assertEquals(right, base.x + lineWidth - 1);
        assertEquals(bottom, base.y - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testHLineLength17W1() {
        final int lineWidth = 1;
        final int length = 17;
        final Position2D base = new Position2D(10, 10);

        Area a = makeHLine(length, lineWidth);

        // Push TL of the line to base to keep all variables non-negative.
        Variable baseLeft = solver.newVar("baseLeft");
        Variable baseTop = solver.newVar("baseTop");
        solver.addEq(baseLeft, base.x, a.left);
        solver.addEq(baseTop, base.y, a.top);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, base.x);
        assertEquals(top, base.y);
        assertEquals(right, base.x + length - 1);
        assertEquals(bottom, base.y + lineWidth - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testVLineLengt17hW1() {
        final int lineWidth = 1;
        final int length = 17;
        final Position2D base = new Position2D(10, 10);
        Variable baseLeft = solver.newVar("baseLeft");
        Variable baseTop = solver.newVar("baseTop");

        Area a = makeVLine(length, lineWidth);

        // Push TL of the line to base to keep all variables non-negative.
        solver.addEq(baseLeft, base.x, a.left);
        solver.addEq(baseTop, base.y, a.top);
        solver.solve("s", config);

        int left = solver.getVarValue(a.left);
        int right = solver.getVarValue(a.right);
        int top = solver.getVarValue(a.top);
        int bottom = solver.getVarValue(a.bottom);
        assertEquals(left, base.x);
        assertEquals(top, base.y);
        assertEquals(right, base.x + lineWidth - 1);
        assertEquals(bottom, base.y + length - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testTextArea() {
        final Size2D size = new Size2D(20, 5);

        Area ta = new TextArea(solver, "ta", "", Color.WHITE, null, new Position2D(0, 0), size);
        solver.solve("s", config);
        int left = solver.getVarValue(ta.left);
        int right = solver.getVarValue(ta.right);
        int top = solver.getVarValue(ta.top);
        int bottom = solver.getVarValue(ta.bottom);
        assertEquals(left, 0);
        assertEquals(top, 0);
        assertEquals(right, size.width - 1);
        assertEquals(bottom, size.height - 1);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testTLarcConnectLines() {
        final int size = 20;
        final int lineWidth = 2;
        final Position2D base = new Position2D(10, 10);
        Variable baseLeft = solver.newVar("baseLeft");
        Variable baseTop = solver.newVar("baseTop");

        Arc a = new TopLeftArc(solver, "x", Color.WHITE, size, lineWidth);
        HorLine h = makeHLine(5, lineWidth);
        VertLine v = makeVLine(5, lineWidth);
        solver.addEq(baseLeft, base.x, h.left);
        solver.addEq(baseTop, base.y, v.top);
        a.connectLine(solver, h);
        a.connectLine(solver, v);
        solver.solve("s", config);

        int arcLeft = solver.getVarValue(a.left);
        int arcRight = solver.getVarValue(a.right);
        int arcTop = solver.getVarValue(a.top);
        int arcBottom = solver.getVarValue(a.bottom);
        assertEquals(arcLeft, solver.getVarValue(v.left));
        assertEquals(arcBottom + 1, solver.getVarValue(v.top));

        assertEquals(arcRight + 1, solver.getVarValue(h.left));
        assertEquals(arcTop, solver.getVarValue(h.top));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testBLarcConnectLines() {
        final int size = 20;
        final int lineWidth = 2;
        final Position2D base = new Position2D(10, 10);
        Variable baseLeft = solver.newVar("baseLeft");
        Variable baseTop = solver.newVar("baseTop");

        Arc a = new BottomLeftArc(solver, "x", Color.WHITE, size, lineWidth);
        HorLine h = makeHLine(5, lineWidth);
        VertLine v = makeVLine(5, lineWidth);
        solver.addEq(baseLeft, base.x, h.left);
        solver.addEq(baseTop, base.y, v.top);
        a.connectLine(solver, h);
        a.connectLine(solver, v);
        solver.solve("s", config);

        int arcLeft = solver.getVarValue(a.left);
        int arcRight = solver.getVarValue(a.right);
        int arcTop = solver.getVarValue(a.top);
        int arcBottom = solver.getVarValue(a.bottom);
        assertEquals(arcLeft, solver.getVarValue(v.left));
        assertEquals(arcTop - 1, solver.getVarValue(v.bottom));

        assertEquals(arcRight + 1, solver.getVarValue(h.left));
        assertEquals(arcBottom, solver.getVarValue(h.bottom));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testTRarcConnectLines() {
        final int size = 20;
        final int lineWidth = 2;
        final Position2D base = new Position2D(50, 50);
        Variable baseLeft = solver.newVar("baseLeft");
        Variable baseTop = solver.newVar("baseTop");

        Arc a = new TopRightArc(solver, "arc", Color.WHITE, size, lineWidth);
        HorLine h = makeHLine(5, lineWidth);
        VertLine v = makeVLine(5, lineWidth);
        a.connectLine(solver, h);
        a.connectLine(solver, v);
        solver.addEq(baseLeft, base.x, h.left);
        solver.addEq(baseTop, base.y, v.top);
        solver.solve("s", config);

        int arcLeft = solver.getVarValue(a.left);
        int arcRight = solver.getVarValue(a.right);
        int arcTop = solver.getVarValue(a.top);
        int arcBottom = solver.getVarValue(a.bottom);
        assertEquals(arcRight, solver.getVarValue(v.right));
        assertEquals(arcBottom + 1, solver.getVarValue(v.top));

        assertEquals(arcLeft - 1, solver.getVarValue(h.right));
        assertEquals(arcTop, solver.getVarValue(h.top));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void testBRarcConnectLines() {
        final int size = 20;
        final int lineWidth = 2;
        final Position2D base = new Position2D(10, 10);
        Variable baseLeft = solver.newVar("baseLeft");
        Variable baseTop = solver.newVar("baseTop");

        Arc a = new BottomRightArc(solver, "x", Color.WHITE, size, lineWidth);
        HorLine h = makeHLine(5, lineWidth);
        VertLine v = makeVLine(5, lineWidth);
        solver.addEq(baseLeft, base.x, h.left);
        solver.addEq(baseTop, base.y, v.top);
        a.connectLine(solver, h);
        a.connectLine(solver, v);
        solver.solve("s", config);

        int arcLeft = solver.getVarValue(a.left);
        int arcRight = solver.getVarValue(a.right);
        int arcTop = solver.getVarValue(a.top);
        int arcBottom = solver.getVarValue(a.bottom);
        assertEquals(arcRight, solver.getVarValue(v.right));
        assertEquals(arcTop - 1, solver.getVarValue(v.bottom));

        assertEquals(arcLeft - 1, solver.getVarValue(h.right));
        assertEquals(arcBottom, solver.getVarValue(h.bottom));
    }

    /**
     * Make a horizontal line with given minimum length and width.
     *
     * @param length Minimum length of the line.
     * @param lineWidth Width of the line.
     * @return The constructed horizontal line.
     */
    private HorLine makeHLine(int length, int lineWidth) {
        HorLine a = new HorLine(solver, "h", Color.WHITE, lineWidth);
        solver.addLe(a.left, length - 1, a.right);
        return a;
    }

    /**
     * Make a vertical line with given minimum length and width.
     *
     * @param length Minimum length of the line.
     * @param lineWidth Width of the line.
     * @return The constructed vertical line.
     */
    private VertLine makeVLine(int length, int lineWidth) {
        VertLine a = new VertLine(solver, "v", Color.WHITE, lineWidth);
        solver.addLe(a.top, length - 1, a.bottom);
        return a;
    }
}
