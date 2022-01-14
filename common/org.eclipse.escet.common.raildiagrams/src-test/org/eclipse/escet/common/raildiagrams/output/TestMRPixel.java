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

package org.eclipse.escet.common.raildiagrams.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.escet.common.raildiagrams.output.MarchingRectangles.Edge;
import org.eclipse.escet.common.raildiagrams.output.MarchingRectangles.Pixel;
import org.junit.Test;

/** Tests of the {@link MarchingRectangles.Pixel} class. */
public class TestMRPixel {
    @SuppressWarnings("javadoc")
    @Test
    public void equalInsideLevelsTest() {
        int[] values = new int[] {0, 0, 0, 0};
        Pixel p = new Pixel(0, 0, values);
        assertEquals(0, p.getMinValue());
        assertEquals(0, p.getMaxValue());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void unequalInsideLevelsTest() {
        int[] values = new int[] {2, 2, 4, 2};
        Pixel p = new Pixel(0, 0, values);
        assertEquals(2, p.getMinValue());
        assertEquals(4, p.getMaxValue());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void expandInsideTest() {
        int[] values = new int[] {0, 0, 0, 0};
        Pixel p = new Pixel(0, 0, values);
        assertEquals(0, p.getExpansionEdges(1, 3).size());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void expandOutsideTest() {
        int[] values = new int[] {4, 4, 4, 4};
        Pixel p = new Pixel(0, 0, values);
        assertEquals(0, p.getExpansionEdges(1, 3).size());
    }

    @SuppressWarnings("javadoc")
    @Test
    public void expandBLTest() {
        int[] values = new int[] {2, 4, 4, 4};
        Pixel p = new Pixel(0, 0, values);
        List<Edge> edges = p.getExpansionEdges(1, 3);
        assertEquals(2, edges.size());
        assertTrue(edges.contains(Edge.BOTTOM));
        assertTrue(edges.contains(Edge.LEFT));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void expandBRTest() {
        int[] values = new int[] {4, 2, 4, 4};
        Pixel p = new Pixel(0, 0, values);
        List<Edge> edges = p.getExpansionEdges(1, 3);
        assertEquals(2, edges.size());
        assertTrue(edges.contains(Edge.BOTTOM));
        assertTrue(edges.contains(Edge.RIGHT));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void expandTRTest() {
        int[] values = new int[] {4, 4, 2, 4};
        Pixel p = new Pixel(0, 0, values);
        List<Edge> edges = p.getExpansionEdges(1, 3);
        assertEquals(2, edges.size());
        assertTrue(edges.contains(Edge.TOP));
        assertTrue(edges.contains(Edge.RIGHT));
    }

    @SuppressWarnings("javadoc")
    @Test
    public void expandTLTest() {
        int[] values = new int[] {4, 4, 4, 2};
        Pixel p = new Pixel(0, 0, values);
        List<Edge> edges = p.getExpansionEdges(1, 3);
        assertEquals(2, edges.size());
        assertTrue(edges.contains(Edge.TOP));
        assertTrue(edges.contains(Edge.LEFT));
    }

    /**
     * Get the computed offset.
     *
     * @param p Pixel to examine.
     * @param edge Edge to examine.
     * @param boundary Boundary value.
     * @return Offset of the boundary position along the edge
     */
    private double getOffset(Pixel p, Edge edge, double boundary) {
        return p.getOffset(p.values[edge.firstCorner.index], p.values[edge.secondCorner.index], boundary);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void offsetAreaHLHLTest() {
        int[] values = new int[] {4, 1, 4, 1};
        Pixel p = new Pixel(0, 0, values);
        double highBound = 3;
        assertEquals(0.33, getOffset(p, Edge.BOTTOM, highBound), 0.01);
        assertEquals(0.66, getOffset(p, Edge.RIGHT, highBound), 0.01);
        assertEquals(0.33, getOffset(p, Edge.TOP, highBound), 0.01);
        assertEquals(0.66, getOffset(p, Edge.LEFT, highBound), 0.01);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void offsetAreaLHLHTest() {
        int[] values = new int[] {1, 4, 1, 4};
        Pixel p = new Pixel(0, 0, values);
        double highBound = 3;
        assertEquals(0.66, getOffset(p, Edge.BOTTOM, highBound), 0.01);
        assertEquals(0.33, getOffset(p, Edge.RIGHT, highBound), 0.01);
        assertEquals(0.66, getOffset(p, Edge.TOP, highBound), 0.01);
        assertEquals(0.33, getOffset(p, Edge.LEFT, highBound), 0.01);
    }

    @SuppressWarnings("javadoc")
    @Test
    public void areaTest() {
        int[] values;
        Pixel p;

        final double lowCornerArea = 0.33 * 0.33 / 2;
        final double highCornerArea = 0.66 * 0.66 / 2;

        // BL corner boundary crossing.
        values = new int[] {1, 4, 4, 4};
        p = new Pixel(0, 0, values);
        assertEquals(lowCornerArea, p.getInnerArea(2), 0.01);
        assertEquals(highCornerArea, p.getInnerArea(3), 0.01);

        // BR corner boundary crossing.
        values = new int[] {4, 1, 4, 4};
        p = new Pixel(0, 0, values);
        assertEquals(lowCornerArea, p.getInnerArea(2), 0.01);
        assertEquals(highCornerArea, p.getInnerArea(3), 0.01);

        // TR corner boundary crossing.
        values = new int[] {4, 4, 1, 4};
        p = new Pixel(0, 0, values);
        assertEquals(lowCornerArea, p.getInnerArea(2), 0.01);
        assertEquals(highCornerArea, p.getInnerArea(3), 0.01);

        // TLR corner boundary crossing.
        values = new int[] {4, 4, 4, 1};
        p = new Pixel(0, 0, values);
        assertEquals(lowCornerArea, p.getInnerArea(2), 0.01);
        assertEquals(highCornerArea, p.getInnerArea(3), 0.01);

        // Vertical boundary crossing.
        values = new int[] {1, 4, 4, 1};
        p = new Pixel(0, 0, values);
        assertEquals(0.33, p.getInnerArea(2), 0.01);
        assertEquals(0.66, p.getInnerArea(3), 0.01);

        // Horizontal boundary crossing.
        values = new int[] {1, 1, 4, 4};
        p = new Pixel(0, 0, values);
        assertEquals(0.33, p.getInnerArea(2), 0.01);
        assertEquals(0.66, p.getInnerArea(3), 0.01);
    }
}
