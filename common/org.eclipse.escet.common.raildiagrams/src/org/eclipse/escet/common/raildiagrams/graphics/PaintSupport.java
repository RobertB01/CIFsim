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

package org.eclipse.escet.common.raildiagrams.graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/** Functions to simplify writing graphics paint code. */
public class PaintSupport {
    /** Constructor of the static {@link PaintSupport} class. */
    private PaintSupport() {
        // Static class.
    }

    /**
     * Set the width of the stroke.
     *
     * @param gd Graphics output handle.
     * @param width Desired width of the stroke.
     */
    public static void setLineWidth(Graphics2D gd, int width) {
        if (width < 1) {
            width = 1; // Make sure the line is shown.
        }
        gd.setStroke(new BasicStroke(width));
    }

    /**
     * Construct the graphics handle for a buffer and configure it.
     *
     * @param image Buffer that needs a graphics handle.
     * @return The configured handle.
     */
    public static Graphics2D getGraphics(BufferedImage image) {
        Graphics2D gd = image.createGraphics();
        gd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gd.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        return gd;
    }

    /**
     * Draw a quarter of an arc inside a square 'size * size' box.
     *
     * @param gd Graphics output handle.
     * @param xpos Horizontal coordinate of top-left corner of the arc box.
     * @param ypos Vertical coordinate of top-left corner of the arc box.
     * @param arcType Quadrant of the arc to draw.
     * @param size Width and height of the box containing the arc.
     * @param lineWidth Width of the arc line.
     * @param color Color of the arc line.
     */
    public static void drawArc(Graphics2D gd, double xpos, double ypos, ArcType arcType, double size, double lineWidth,
            Color color)
    {
        gd.setColor(color);
        int lWidth = (int)lineWidth;
        setLineWidth(gd, lWidth);

        int xShift = (lWidth & 1) != 0 ? arcType.xOddShift : arcType.xEvenShift;
        int yShift = (lWidth & 1) != 0 ? arcType.yOddShift : arcType.yEvenShift;
        int arcX = xShift + (int)(xpos + arcType.xSizeMul * size + arcType.xLwidthMul * lineWidth);
        int arcY = yShift + (int)(ypos + arcType.ySizeMul * size + arcType.yLwidthMul * lineWidth);
        gd.drawArc(arcX, arcY, (int)(2 * size - 2 * lineWidth), (int)(2 * size - 2 * lineWidth), arcType.startAngle,
                90);
    }

    /** Arcs that can be painted. */
    public static enum ArcType {
        /** Bottom-left arc. */
        BL_ARC(180, 0, 0, 0, 0.5, -1, 0, -1, 1.5),

        /** Bottom-right arc. */
        BR_ARC(270, -1, 0, -1, 1.5, -1, 0, -1, 1.5),

        /** Top-left arc. */
        TL_ARC(90, 0, 0, 0, 0.5, 0, 0, 0, 0.5),

        /** Top-right arc. */
        TR_ARC(0, -1, 0, -1, 1.5, 0, 0, 0, 0.5);

        /** Start angle for drawing the arc. */
        public final int startAngle;

        /** Translation in X direction for odd line widths. */
        int xOddShift;

        /** Translation in X direction for even line widths. */
        int xEvenShift;

        /** Size multiplication factor for X. */
        int xSizeMul;

        /** Line width multiplication factor for X. */
        double xLwidthMul;

        /** Translation in Y direction for odd line widths. */
        int yOddShift;

        /** Translation in Y direction for even line widths. */
        int yEvenShift;

        /** Size multiplication factor for Y. */
        int ySizeMul;

        /** Line width multiplication factor for Y. */
        double yLwidthMul;

        /**
         * Constructor of the {@link ArcType} class.
         *
         * @param startAngle Start angle for drawing the arc.
         * @param xOddShift Translation in X direction for odd line widths.
         * @param xEvenShift Translation in X direction for even line widths.
         * @param xSizeMul Size multiplication factor for X.
         * @param xLwidthMul Line width multiplication factor for X.
         * @param yOddShift Translation in Y direction for odd line widths.
         * @param yEvenShift Translation in Y direction for even line widths.
         * @param ySizeMul Size multiplication factor for Y.
         * @param yLwidthMul Line width multiplication factor for Y.
         */
        private ArcType(int startAngle, int xOddShift, int xEvenShift, int xSizeMul, double xLwidthMul, int yOddShift,
                int yEvenShift, int ySizeMul, double yLwidthMul)
        {
            this.startAngle = startAngle;
            this.xOddShift = xOddShift;
            this.xEvenShift = xEvenShift;
            this.xSizeMul = xSizeMul;
            this.xLwidthMul = xLwidthMul;
            this.yOddShift = yOddShift;
            this.yEvenShift = yEvenShift;
            this.ySizeMul = ySizeMul;
            this.yLwidthMul = yLwidthMul;
        }
    }
}
