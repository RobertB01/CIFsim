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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;

/** 2D marching cubes for computing colors of arc pixels. */
public class MarchingRectangles {
    /** X coordinate of the arc center. */
    private int cx;

    /** Y coordinate of the arc center. */
    private int cy;

    /** Optional lower X bound for the area that may have {@link Pixel}s. */
    private Optional<Integer> minX;

    /** Optional upper inclusive X bound for the area that may have {@link Pixel}s. */
    private Optional<Integer> maxX;

    /** Optional lower Y bound for the area that may have {@link Pixel}s. */
    private Optional<Integer> minY;

    /** Optional upper inclusive Y bound for the area that may have {@link Pixel}s. */
    private Optional<Integer> maxY;

    /** Pixels that have been found. */
    private Set<Pixel> seen = set();

    /** Pixels that still need processing by the algorithm. */
    private Deque<Pixel> queued = new ArrayDeque<>();

    /**
     * Corners of a rectangle with relative offsets.
     * <p>
     * Note that in reality coordinate systems may be mirrored but that doesn't make any difference in the algorithm.
     * </p>
     */
    static enum Corner {
        /** Bottom-left corner. */
        BL(0, 0, 0),
        /** Bottom-right corner. */
        BR(1, 0, 1),
        /** Top-right corner. */
        TR(1, 1, 2),
        /** Top-left corner. */
        TL(0, 1, 3);

        /** Relative offset of the corner in X direction. */
        public final int dx;

        /** Relative offset of the corner in Y direction. */
        public final int dy;

        /** Index of the corner in the {@link Pixel#values} array. */
        public final int index;

        /**
         * Constructor of the {@link Corner} enumeration.
         *
         * @param dx Relative offset of the corner in X direction.
         * @param dy Relative offset of the corner in Y direction.
         * @param levelIndex Index in {@link Pixel#values} of the corner.
         */
        private Corner(int dx, int dy, int levelIndex) {
            this.dx = dx;
            this.dy = dy;
            this.index = levelIndex;
        }
    }

    /** Edge definitions of a {@link Pixel}. */
    public static enum Edge {
        /** Bottom edge. */
        BOTTOM(0, -1, Corner.BL, Corner.BR),
        /** Right edge. */
        RIGHT(1, 0, Corner.BR, Corner.TR),
        /** Top edge. */
        TOP(0, 1, Corner.TR, Corner.TL),
        /** Left edge. */
        LEFT(-1, 0, Corner.TL, Corner.BL);

        /** Relative X position of the neighbor {@link Pixel} against the edge. */
        public final int dx;

        /** Relative Y position of the neighbor {@link Pixel} against the edge. */
        public final int dy;

        /** First corner of the edge. */
        public Corner firstCorner;

        /** Second corner of the edge. */
        public Corner secondCorner;

        /**
         * Constructor of the {@link Edge} class.
         *
         * @param dx Relative X position of the neighbor {@link Pixel} against the edge.
         * @param dy Relative Y position of the neighbor {@link Pixel} against the edge.
         * @param firstCorner First corner of the edge.
         * @param secondCorner Second corner of the edge.
         */
        private Edge(int dx, int dy, Corner firstCorner, Corner secondCorner) {
            this.dx = dx;
            this.dy = dy;
            this.firstCorner = firstCorner;
            this.secondCorner = secondCorner;
        }
    }

    /** Data about a pixel in the marching pixels algorithm. */
    public static class Pixel {
        /** Base position of the rectangle in X direction. */
        public final int x;

        /** Base position of the rectangle in Y direction. */
        public final int y;

        /**
         * Squared distance of the pixel corners to the circle center.
         *
         * <p>
         * Indices match {@link Corner#index}.
         * </p>
         */
        public final int[] values;

        /**
         * Constructor of the {@link MarchingRectangles.Pixel} class.
         *
         * @param x Base position of the rectangle in X direction.
         * @param y Base position of the rectangle in Y direction.
         * @param values Squared distance to the center point of the arc circles.
         */
        public Pixel(int x, int y, int[] values) {
            this.x = x;
            this.y = y;
            this.values = values;

            Assert.check(Corner.values().length == 4);
            Assert.check(values.length == 4);
        }

        /**
         * Get the largest value at a corner.
         *
         * @return Largest value at a corner.
         */
        public int getMaxValue() {
            return Math.max(Math.max(values[0], values[1]), Math.max(values[2], values[3]));
        }

        /**
         * Get the smallest value at a corner.
         *
         * @return Smallest value at a corner.
         */
        public int getMinValue() {
            return Math.min(Math.min(values[0], values[1]), Math.min(values[2], values[3]));
        }

        /**
         * Find neighbors that are interesting for further exploration.
         *
         * @param sqdInnerRadius Lower bound of corners with interesting values.
         * @param sqdOuterRadius Upper bound of corners with interesting values.
         * @return Edges to interesting neighbors.
         */
        public List<Edge> getExpansionEdges(double sqdInnerRadius, double sqdOuterRadius) {
            List<Edge> neighbors = listc(4);
            for (Edge e: Edge.values()) {
                int c1 = e.firstCorner.index;
                // All but both corners inside or both corners outside are relevant.
                boolean interesting = values[c1] >= sqdInnerRadius && values[c1] <= sqdOuterRadius;
                if (!interesting) {
                    int c2 = e.secondCorner.index;
                    interesting = values[c2] >= sqdInnerRadius && values[c2] <= sqdOuterRadius;
                }

                if (interesting) {
                    neighbors.add(e);
                }
            }
            return neighbors;
        }

        /**
         * Compute area covered by the pixel at or below the given boundary.
         *
         * @param boundary Inclusive upper bound of the area to compute.
         * @return Area covered by the pixel at or below the given upper bound.
         */
        public double getInnerArea(double boundary) {
            final int blIndex = Corner.BL.index;
            final int brIndex = Corner.BR.index;
            final int trIndex = Corner.TR.index;
            final int tlIndex = Corner.TL.index;

            // TODO: Compute real offset from the center point, the boundary, and the pixel corner position.
            if (values[blIndex] <= boundary) {
                // BL corner at or below boundary.
                if (values[brIndex] <= boundary) {
                    // BL corner at or below boundary.
                    // BR corner at or below boundary.
                    if (values[trIndex] <= boundary) {
                        // BL corner at or below boundary.
                        // BR corner at or below boundary.
                        // TR corner at or below boundary.
                        if (values[tlIndex] <= boundary) {
                            // BL corner at or below boundary.
                            // BR corner at or below boundary.
                            // TR corner at or below boundary.
                            // TL corner at or below boundary.
                            return 1.0;
                        } else {
                            // BL corner at or below boundary.
                            // BR corner at or below boundary.
                            // TR corner at or below boundary.
                            // TL corner above boundary.
                            double topOffset = getOffset(values[tlIndex], values[trIndex], boundary);
                            double leftOffset = getOffset(values[tlIndex], values[blIndex], boundary);
                            return 1 - topOffset * leftOffset / 2;
                        }
                    } else {
                        // BL corner at or below boundary.
                        // BR corner at or below boundary.
                        // TR corner above boundary.
                        if (values[tlIndex] <= boundary) {
                            // BL corner at or below boundary.
                            // BR corner at or below boundary.
                            // TR corner above boundary.
                            // TL corner at or below boundary.
                            double topOffset = getOffset(values[trIndex], values[tlIndex], boundary);
                            double rightOffset = getOffset(values[trIndex], values[brIndex], boundary);
                            return 1 - topOffset * rightOffset / 2;
                        } else {
                            // BL corner at or below boundary.
                            // BR corner at or below boundary.
                            // TR corner above boundary.
                            // TL corner above boundary.
                            double leftOffset = getOffset(values[tlIndex], values[blIndex], boundary);
                            double rightOffset = getOffset(values[trIndex], values[brIndex], boundary);
                            return 1 - (leftOffset + rightOffset) / 2;
                        }
                    }
                } else {
                    // BL corner at or below boundary.
                    // BR corner above boundary.
                    if (values[trIndex] <= boundary) {
                        // BL corner at or below boundary.
                        // BR corner above boundary.
                        // TR corner at or below boundary.
                        if (values[tlIndex] <= boundary) {
                            // BL corner at or below boundary.
                            // BR corner above boundary.
                            // TR corner at or below boundary.
                            // TL corner at or below boundary.
                            double bottomOffset = getOffset(values[brIndex], values[blIndex], boundary);
                            double rightOffset = getOffset(values[brIndex], values[trIndex], boundary);
                            return 1 - bottomOffset * rightOffset / 2;
                        } else {
                            // BL corner at or below boundary.
                            // BR corner above boundary.
                            // TR corner at or below boundary.
                            // TL corner above boundary.
                            throw new AssertionError("Impossible rectangle.");
                        }
                    } else {
                        // BL corner at or below boundary.
                        // BR corner above boundary.
                        // TR corner above boundary.
                        if (values[tlIndex] <= boundary) {
                            // BL corner at or below boundary.
                            // BR corner above boundary.
                            // TR corner above boundary.
                            // TL corner at or below boundary.
                            double topOffset = getOffset(values[trIndex], values[tlIndex], boundary);
                            double bottomOffset = getOffset(values[brIndex], values[blIndex], boundary);
                            return 1 - (topOffset + bottomOffset) / 2;
                        } else {
                            // BL corner at or below boundary.
                            // BR corner above boundary.
                            // TR corner above boundary.
                            // TL corner above boundary.
                            double bottomOffset = getOffset(values[blIndex], values[brIndex], boundary);
                            double leftOffset = getOffset(values[blIndex], values[tlIndex], boundary);
                            return bottomOffset * leftOffset / 2;
                        }
                    }
                }
            } else {
                // BL corner above boundary.
                if (values[brIndex] <= boundary) {
                    // BL corner above boundary.
                    // BR corner at or below boundary.
                    if (values[trIndex] <= boundary) {
                        // BL corner above boundary.
                        // BR corner at or below boundary.
                        // TR corner at or below boundary.
                        if (values[tlIndex] <= boundary) {
                            // BL corner above boundary.
                            // BR corner at or below boundary.
                            // TR corner at or below boundary.
                            // TL corner at or below boundary.
                            double bottomOffset = getOffset(values[blIndex], values[brIndex], boundary);
                            double leftOffset = getOffset(values[blIndex], values[tlIndex], boundary);
                            return 1 - bottomOffset * leftOffset / 2;
                        } else {
                            // BL corner above boundary.
                            // BR corner at or below boundary.
                            // TR corner at or below boundary.
                            // TL corner above boundary.
                            double topOffset = getOffset(values[trIndex], values[tlIndex], boundary);
                            double bottomOffset = getOffset(values[brIndex], values[blIndex], boundary);
                            return (topOffset + bottomOffset) / 2;
                        }
                    } else {
                        // BL corner above boundary.
                        // BR corner at or below boundary.
                        // TR corner above boundary.
                        if (values[tlIndex] <= boundary) {
                            // BL corner above boundary.
                            // BR corner at or below boundary.
                            // TR corner above boundary.
                            // TL corner at or below boundary.
                            throw new AssertionError("Impossible rectangle.");
                        } else {
                            // BL corner above boundary.
                            // BR corner at or below boundary.
                            // TR corner above boundary.
                            // TL corner above boundary.
                            double bottomOffset = getOffset(values[brIndex], values[blIndex], boundary);
                            double rightOffset = getOffset(values[brIndex], values[trIndex], boundary);
                            return bottomOffset * rightOffset / 2;
                        }
                    }
                } else {
                    // BL corner above boundary.
                    // BR corner above boundary.
                    if (values[trIndex] <= boundary) {
                        // BL corner above boundary.
                        // BR corner above boundary.
                        // TR corner at or below boundary.
                        if (values[tlIndex] <= boundary) {
                            // BL corner above boundary.
                            // BR corner above boundary.
                            // TR corner at or below boundary.
                            // TL corner at or below boundary.
                            double leftOffset = getOffset(values[blIndex], values[tlIndex], boundary);
                            double rightOffset = getOffset(values[brIndex], values[trIndex], boundary);
                            return 1 - (leftOffset + rightOffset) / 2;
                        } else {
                            // BL corner above boundary.
                            // BR corner above boundary.
                            // TR corner at or below boundary.
                            // TL corner above boundary.
                            double topOffset = getOffset(values[trIndex], values[tlIndex], boundary);
                            double rightOffset = getOffset(values[trIndex], values[brIndex], boundary);
                            return topOffset * rightOffset / 2;
                        }
                    } else {
                        // BL corner above boundary.
                        // BR corner above boundary.
                        // TR corner above boundary.
                        if (values[tlIndex] <= boundary) {
                            // BL corner above boundary.
                            // BR corner above boundary.
                            // TR corner above boundary.
                            // TL corner at or below boundary.
                            double leftOffset = getOffset(values[tlIndex], values[blIndex], boundary);
                            double topOffset = getOffset(values[tlIndex], values[trIndex], boundary);
                            return leftOffset * topOffset / 2;
                        } else {
                            // BL corner above boundary.
                            // BR corner above boundary.
                            // TR corner above boundary.
                            // TL corner above boundary.
                            return 0;
                        }
                    }
                }
            }
        }

        /**
         * Compute the offset at an edge.
         *
         * @param value1 Value of the near corner.
         * @param value2 Value of the far corner.
         * @param boundary Boundary value to find at or between both given values.
         * @return Fraction from the near corner.
         */
        public double getOffset(double value1, double value2, double boundary) {
            if (value1 == boundary) {
                return 0;
            }
            if (value2 == boundary) {
                return 1;
            }

            Assert.check((value1 > boundary && value2 < boundary) || (value1 < boundary && value2 > boundary));
            // Perform linear interpolation between the known values at the corner points.
            // As the real function is quadratic, this introduces a small error.
            //
            // 0 1
            // | |
            // | * value2
            // | /|
            // | / |
            // boundary ----* |
            // |/ |
            // value1 * |
            // | |
            double offset = (boundary - value1) / (value2 - value1);
            Assert.check(offset >= 0 && offset <= 1);
            return offset;
        }

        @Override
        public int hashCode() {
            return x + 13 * y;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Pixel)) {
                return false;
            }
            Pixel oPixel = (Pixel)other;
            return oPixel.x == x && oPixel.y == y;
        }

        @Override
        public String toString() {
            return fmt("Pixel[%d, %d] values(%.2f %.2f %.2f %.2f)", x, y, values[0], values[1], values[2], values[3]);
        }
    }

    /** Position of a pixel and its coverage of the arc. */
    public static class PixelCoverage implements Comparable<PixelCoverage> {
        /** Base position of the pixel in X direction. */
        public final int x;

        /** Base position of the pixel in Y direction. */
        public final int y;

        /** Fraction of how much the pixel covers the arc. */
        public final double coverage;

        /**
         * Constructor of the {@link PixelCoverage} class.
         *
         * @param x Base position of the pixel in X direction.
         * @param y Base position of the pixel in Y direction.
         * @param coverage Fraction of how much the pixel covers the arc.
         */
        public PixelCoverage(int x, int y, double coverage) {
            this.x = x;
            this.y = y;
            this.coverage = coverage;
        }

        @Override
        public int compareTo(PixelCoverage other) {
            if (x != other.x) {
                return (x < other.x) ? -1 : 1;
            }
            if (y != other.y) {
                return (y < other.y) ? -1 : 1;
            }
            return 0;
        }
    }

    /**
     * Compute non-empty coverage of pixels between the inner and the outer radius from the given (cx, cy) center point
     * within the specified area.
     *
     * @param cx X coordinate of the arc center.
     * @param cy Y coordinate of the arc center.
     * @param innerRadius Inner radius of the arc.
     * @param outerRadius Outer radius of the arc.
     * @param minX Optional lower X bound for the area that may have {@link Pixel}s.
     * @param maxX Optional upper inclusive X bound for the area that may have {@link Pixel}s.
     * @param minY Optional lower Y bound for the area that may have {@link Pixel}s.
     * @param maxY Optional upper inclusive Y bound for the area that may have {@link Pixel}s.
     * @return Pixels with non-empty coverage at the arc within the specified area.
     */
    @SuppressWarnings("null")
    public List<PixelCoverage> getCoverage(int cx, int cy, double innerRadius, double outerRadius,
            Optional<Integer> minX, Optional<Integer> maxX, Optional<Integer> minY, Optional<Integer> maxY)
    {
        // Copy parameters to the class variables.
        this.cx = cx;
        this.cy = cy;
        Assert.check(innerRadius > 0 && innerRadius < outerRadius);
        double sqdInnerRadius = innerRadius * innerRadius;
        double sqdOuterRadius = outerRadius * outerRadius;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;

        // Add initial pixel.
        seen.clear();
        queued.clear();
        addPixel(clamp(this.minX, cx, this.maxX), clamp(this.minY, cy, this.maxY));
        Assert.check(!queued.isEmpty()); // Initial pixel should be valid now.

        // Process pixels until we run out.
        List<PixelCoverage> coverages = list();

        final Edge[] edges = Edge.values();
        while (!queued.isEmpty()) {
            Pixel p = queued.pop();

            double minValue = p.getMinValue();
            double maxValue = p.getMaxValue();

            // There are 5 significant points in the squared distance from origin.
            // 1. Below the inner circle.
            // 2. At the inner circle (happens a lot as squared distance is an integer value).
            // 3. Between the inner and outer circle.
            // 4. At the outer circle (happens a lot as squared distance is an integer value).
            // 5. Beyond the outer circle.
            //
            // By taking the minimum and maximum value at the corner points of a pixel, this
            // leads to covering a sub-range in the distance, giving A to M. These are covered
            // in 6 cases below.
            //
            // --> squared distance
            // origin inner outer
            // | | |
            // 1 2 3 4 5
            // | | Covered in
            // A *---* | | Case I
            // B *-----* | Case I
            // C *---------* | Case III
            // D *--------------* Case III
            // E *--------------------* Case III
            // F *--* | Case III
            // G *--------* Case III
            // H *--------------* Case III
            // I | *---* | Case III
            // J | *------* Case III
            // K | *------------* Case III
            // L | *-----* Case II
            // M | | *---* Case II
            // | |

            // Case I: Completely inside the inner circle, move outwards towards higher values.
            //
            // origin inner outer
            // | | |
            // A *---* | |
            // B *-----* |
            if (maxValue <= sqdInnerRadius) {
                Edge bestEdge = null;
                int bestVal = 0; // Initialized at first iteration.
                for (Edge e: edges) {
                    int valueIndex1 = e.firstCorner.index;
                    int valueIndex2 = e.secondCorner.index;
                    int edgeVal = p.values[valueIndex1] + p.values[valueIndex2];
                    if (bestEdge == null || bestVal < edgeVal) {
                        bestEdge = e;
                        bestVal = edgeVal;
                    }
                }
                addPixel(p.x + bestEdge.dx, p.y + bestEdge.dy);
                continue;
            }

            // Case II: Completely outside the outer circle, move inwards towards smaller values.
            //
            // origin inner outer
            // | | |
            // L | *-----*
            // M | | *---*
            if (minValue >= sqdOuterRadius) {
                Edge bestEdge = null;
                int bestVal = 0; // Initialized at first iteration.
                for (Edge e: edges) {
                    int valueIndex1 = e.firstCorner.index;
                    int valueIndex2 = e.secondCorner.index;
                    int edgeVal = p.values[valueIndex1] + p.values[valueIndex2];
                    if (bestEdge == null || bestVal > edgeVal) {
                        bestEdge = e;
                        bestVal = edgeVal;
                    }
                }
                addPixel(p.x + bestEdge.dx, p.y + bestEdge.dy);
                continue;
            }

            // Case III: Pixel is at least partially covering.
            //
            // C *---------* |
            // D *--------------*
            // E *--------------------*
            // F *--* |
            // G *--------*
            // H *--------------*
            // I | *---* |
            // J | *------*
            // K | *------------*
            {
                double belowInner = p.getInnerArea(sqdInnerRadius);
                double belowOuter = p.getInnerArea(sqdOuterRadius);
                coverages.add(new PixelCoverage(p.x, p.y, belowOuter - belowInner));
                for (Edge e: p.getExpansionEdges(sqdInnerRadius, sqdOuterRadius)) {
                    if (inArea(p.x + e.dx, p.y + e.dy)) {
                        addPixel(p.x + e.dx, p.y + e.dy);
                    }
                }
                continue;
            }
        }

        return coverages;
    }

    /**
     * Add a new pixel to the queue unless it has already been done.
     *
     * @param x X coordinate of the pixel.
     * @param y Y coordinate of the pixel.
     */
    private void addPixel(int x, int y) {
        int[] values = new int[4];
        Pixel p = new Pixel(x, y, values);
        if (seen.add(p)) {
            // New pixel, compute corner values as well.
            for (Corner c: Corner.values()) {
                int cornerX = x + c.dx;
                int cornerY = y + c.dy;
                int value = (cornerX - cx) * (cornerX - cx) + (cornerY - cy) * (cornerY - cy);
                values[c.index] = value;
            }
            queued.add(p);
        }
    }

    /**
     * Force val between the given position bounds.
     *
     * @param low Lower boundary if not empty.
     * @param val Initial value.
     * @param high Upper inclusive boundary if not empty.
     * @return Value between the boundaries.
     */
    private int clamp(Optional<Integer> low, int val, Optional<Integer> high) {
        if (!low.isEmpty()) {
            val = Math.max(val, low.get());
        }
        if (!high.isEmpty()) {
            val = Math.min(val, high.get() - 1);
        }
        return val;
    }

    /**
     * Check that a position is within the boundaries of the area.
     *
     * @param x X coordinate of the position.
     * @param y Y coordinate of the position.
     * @return Whether the given position is within the allowed area.
     */
    private boolean inArea(int x, int y) {
        if (!minX.isEmpty() && x < minX.get()) {
            return false;
        }
        if (!minY.isEmpty() && y < minY.get()) {
            return false;
        }
        if (!maxX.isEmpty() && x + 1 > maxX.get()) {
            return false;
        }
        if (!maxY.isEmpty() && y + 1 > maxY.get()) {
            return false;
        }
        return true;
    }
}
