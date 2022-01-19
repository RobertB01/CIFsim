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

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.raildiagrams.output.MarchingRectangles.PixelCoverage;
import org.junit.Test;

/** Test marching rectangles computation. */
public class TestMarchingRectangles {
    /** Radius of the outer circle. */
    private static final double OUTER_RAD = 4.8;

    /** Radius of the inner circle. */
    private static final double INNER_RAD = 3.6;

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

    /**
     * Compute the remaining X or Y fraction.
     *
     * <p>
     * The triangle formula {@code x**2 + y**2 == r**2} holds for points at a circle. At a given y coordinate this
     * results in {@code x = sqrt(r**2 - y**2)}. Computing y from x is the same formula. To get coverage of a pixel,
     * only the fraction is relevant.
     * </p>
     *
     * @param r Radius of the circle.
     * @param xy The x or y coordinate.
     * @return Fraction of the y or x coordinate.
     * @note Fraction is computed from the distance to the axes.
     */
    private double getFraction(double r, double xy) {
        double value = Math.sqrt(r * r - xy * xy);
        Assert.check(value >= 0);
        return value - Math.floor(value);
    }

    /**
     * Check that area of a vertically crossing boundary matches with the expectation.
     *
     * @param baseX X coordinate of the pixel being examined.
     * @param baseY Y coordinate of the pixel being examined.
     * @param y1 Y coordinate of the first X fraction.
     * @param y2 Y coordinate of the second X fraction.
     * @param rad Radius of the circle.
     * @param invertFrac Whether to invert fractions.
     * @param invertCoverage Whether to invert the coverage result.
     * @param pixels Computed pixel coverage to verify.
     */
    private void checkXX(int baseX, int baseY, int y1, int y2, double rad, boolean invertFrac, boolean invertCoverage,
            List<PixelCoverage> pixels)
    {
        double measured = getCoverage(baseX, baseY, pixels).coverage;
        double frac1 = getFraction(rad, y1);
        if (invertFrac) {
            frac1 = 1 - frac1;
        }
        double frac2 = getFraction(rad, y2);
        if (invertFrac) {
            frac2 = 1 - frac2;
        }
        double coverage = (frac1 + frac2) / 2;
        if (invertCoverage) {
            coverage = 1 - coverage;
        }
        assertEquals(coverage, measured, 0.03);
    }

    /**
     * Check that area of a vertically crossing boundary matches with the expectation.
     *
     * @param baseX X coordinate of the pixel being examined.
     * @param baseY Y coordinate of the pixel being examined.
     * @param y Y coordinate of the X fraction.
     * @param x X coordinate of the Y fraction.
     * @param rad Radius of the circle.
     * @param invertFrac Whether to invert fractions.
     * @param invertCoverage Whether to invert the coverage result.
     * @param pixels Computed pixel coverage to verify.
     */
    private void checkXY(int baseX, int baseY, int y, int x, double rad, boolean invertFrac, boolean invertCoverage,
            List<PixelCoverage> pixels)
    {
        double measured = getCoverage(baseX, baseY, pixels).coverage;
        double frac1 = getFraction(rad, y);
        if (invertFrac) {
            frac1 = 1 - frac1;
        }
        double frac2 = getFraction(rad, x);
        if (invertFrac) {
            frac2 = 1 - frac2;
        }
        double coverage = frac1 * frac2 / 2;
        if (invertCoverage) {
            coverage = 1 - coverage;
        }
        assertEquals(coverage, measured, 0.03);
    }

    /**
     * Check that area of a vertically crossing boundary matches with the expectation.
     *
     * @param baseX X coordinate of the pixel being examined.
     * @param baseY Y coordinate of the pixel being examined.
     * @param x1 X coordinate of the first Y fraction.
     * @param x2 X coordinate of the second Y fraction.
     * @param rad Radius of the circle.
     * @param invertFrac Whether to invert fractions.
     * @param invertCoverage Whether to invert the coverage result.
     * @param pixels Computed pixel coverage to verify.
     */
    private void checkYY(int baseX, int baseY, int x1, int x2, double rad, boolean invertFrac, boolean invertCoverage,
            List<PixelCoverage> pixels)
    {
        double measured = getCoverage(baseX, baseY, pixels).coverage;
        double frac1 = getFraction(rad, x1);
        if (invertFrac) {
            frac1 = 1 - frac1;
        }
        double frac2 = getFraction(rad, x2);
        if (invertFrac) {
            frac2 = 1 - frac2;
        }
        double coverage = (frac1 + frac2) / 2;
        if (invertCoverage) {
            coverage = 1 - coverage;
        }
        assertEquals(coverage, measured, 0.03);
    }

    /** Check that all pixels at least partially covering the circle are returned. */
    @Test
    public void testCoverageCollection() {
        // It's an O shape. For a graphical view, please open "collection.png".
        String[] expectedCircle = new String[] {"            ", "   ******   ", "  ********  ", " ****  **** ",
                " ***    *** ", " **      ** ", " **      ** ", " ***    *** ", " ****  **** ", "  ********  ",
                "   ******   ", "            "};
        MarchingRectangles mr = new MarchingRectangles();
        List<PixelCoverage> pixels = mr.getCoverage(0, 0, 0, 0, INNER_RAD, OUTER_RAD, Optional.of(-10), Optional.of(10),
                Optional.of(-10), Optional.of(10));

        for (int y = -6; y < 6; y++) {
            assertEquals(expectedCircle[y + 6], getLine(y, -6, 5, pixels));
        }
    }

    /** Verify the returned coverage value. */
    @Test
    public void testCoverageValues() {
        MarchingRectangles mr = new MarchingRectangles();
        List<PixelCoverage> pixels = mr.getCoverage(0, 0, 0, 0, INNER_RAD, OUTER_RAD, Optional.of(-10), Optional.of(10),
                Optional.of(-10), Optional.of(10));

        // For a graphical view, please open "single_partial_coverage.png".
        checkXY(-5, -3, -2, -4, OUTER_RAD, false, false, pixels);
        checkXY(-4, -2, -1, -3, INNER_RAD, false, true, pixels);
        checkXX(-4, 0, 0, 1, INNER_RAD, false, false, pixels);
        checkXY(-3, 4, 4, -2, OUTER_RAD, false, false, pixels);
        checkXY(-2, -3, -3, -2, INNER_RAD, true, false, pixels);
        checkYY(-1, -4, -1, 0, INNER_RAD, true, true, pixels);
        checkYY(-1, 4, -1, 0, OUTER_RAD, false, false, pixels);
        checkXY(1, -3, -3, 2, INNER_RAD, true, false, pixels);
        checkXY(2, -5, -4, 2, OUTER_RAD, false, false, pixels);
        checkXY(3, -2, -1, 3, INNER_RAD, false, true, pixels);
        checkXX(4, 0, 0, 1, OUTER_RAD, false, false, pixels);
        checkXY(4, 2, 2, 4, OUTER_RAD, false, false, pixels);
    }
}
