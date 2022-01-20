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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.eclipse.escet.common.raildiagrams.output.MarchingRectangles.PixelCoverage;
import org.eclipse.escet.common.raildiagrams.util.Position2D;
import org.junit.Test;

/** Test marching rectangles computation. */
public class TestMarchingRectangles {
    /** Radius of the outer circle. */
    private static final int OUTER_RAD = 4;

    /** Radius of the inner circle. */
    private static final int INNER_RAD = 2;

    /**
     * Find coverage information for a given position.
     *
     * @param x X coordinate of the position.
     * @param y Y coordinate of the position.
     * @param pixels Available coverage information.
     * @return Coverage information at the requested position if it exists, else {@code null}.
     */
    private PixelCoverage getCoverage(int x, int y, List<PixelCoverage> pixels) {
        for (PixelCoverage pc: pixels) {
            if (pc.x == x && pc.y == y) {
                return pc;
            }
        }
        return null;
    }

    /**
     * Get a horizontal line of coverage information, and transform it to a string.
     *
     * @param y Y coordinate of the line.
     * @param minx Minimum x to query.
     * @param maxx Maximum x to query.
     * @param pixels Available coverage information.
     * @return A string showing what pixels have coverage information. A {@code '*'} at a position means coverage was
     *     found else a {@code ' '} is used.
     */
    private String getLine(int y, int minx, int maxx, List<PixelCoverage> pixels) {
        char[] kars = new char[maxx - minx + 1];
        Arrays.fill(kars, '?');
        int i = 0;
        for (int x = minx; x <= maxx; x++) {
            kars[i++] = (getCoverage(x, y, pixels) != null) ? '*' : ' ';
        }
        return new String(kars);
    }

    /** Check that all pixels at least partially covering the circle are returned. */
    @Test
    public void testCoverageCollection() {
        // For a graphical view, please open "collection.png" and match the shape with the positions between
        // the red polygons.
        String[] expectedCircle = new String[] {"          ", // 0
                                                "  ******  ",
                                                " ******** ", // 2
                                                " ******** ",
                                                " ***  *** ", // 4
                                                " ***  *** ",
                                                " ******** ", // 6
                                                " ******** ",
                                                "  ******  ", // 8
                                                "          "};
        MarchingRectangles mr = new MarchingRectangles();
        Position2D center = new Position2D(0, 0);
        Position2D relativeInitial = new Position2D(0, 0);
        List<PixelCoverage> pixels = mr.getCoverage(center, relativeInitial, INNER_RAD, OUTER_RAD, Optional.of(-10), Optional.of(10),
                Optional.of(-10), Optional.of(10));

        for (int y = -5; y < 5; y++) {
            assertEquals(expectedCircle[y + 5], getLine(y, -5, 4, pixels));
        }
    }

    /** Verify the returned coverage value. */
    @Test
    public void testCoverageValues() {
        MarchingRectangles mr = new MarchingRectangles();
        Position2D center = new Position2D(0, 0);
        Position2D relativeInitial = new Position2D(0, 0);
        List<PixelCoverage> pixels = mr.getCoverage(center, relativeInitial, INNER_RAD, OUTER_RAD, Optional.of(-10), Optional.of(10),
                Optional.of(-10), Optional.of(10));

        // For a graphical view, please open "collection.png" and find the light-blue areas.
        // Expectations are approximated by counting pixels in the image.
        assertEquals((10.0 / 20 + 18.0 / 20) / 2, getCoverage(-4, -2, pixels).coverage, 0.06);
        assertEquals(1 - (9.0 / 20 * 8.0 / 20) / 2, getCoverage(-3, 2, pixels).coverage, 0.06);
        assertEquals(1 - (14.0 / 20 * 16.0 / 20) / 2, getCoverage(-2, -2, pixels).coverage, 0.06);
        assertEquals((17.0 / 20 + 8.0 / 20) / 2, getCoverage(1, -4, pixels).coverage, 0.06);
        assertEquals((6.0 / 20 * 1) / 2, getCoverage(1, -1, pixels).coverage, 0.06);
        assertEquals((17.0 / 20 + 8.0 / 20) / 2, getCoverage(1, 3, pixels).coverage, 0.06);
        assertEquals((17.0 / 20 + 8.0 / 20) / 2, getCoverage(3, -2, pixels).coverage, 0.06);
    }
}
