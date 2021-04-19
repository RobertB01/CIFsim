//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021 Contributors to the Eclipse Foundation
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
        if (width < 1)
            width = 1; // Make sure the line is shown.
        gd.setStroke(new BasicStroke(width));
    }

    /**
     * Draw a quarter of an arc inside a square 'size * size' box.
     *
     * @param gd Graphics output handle.
     * @param xpos Horizontal position of top-left corner of the arc box.
     * @param ypos Vertical position of top-left corner of the arc box.
     * @param arcType Quadrant of the arc to draw.
     * @param size Width and height of the box containing the arc.
     * @param lineWidth Width of the arc line.
     * @param color Color of the arc line.
     */
    public static void drawArc(Graphics2D gd, double xpos, double ypos, ArcType arcType, double size, double lineWidth,
            Color color)
    {
        gd.setColor(color);
        setLineWidth(gd, (int)lineWidth);

        int arcX = (int)(xpos + arcType.xSizeMul * size + arcType.xLwidthMul * lineWidth);
        int arcY = (int)(ypos + arcType.ySizeMul * size + arcType.yLwidthMul * lineWidth);
        gd.drawArc(arcX, arcY, (int)(2 * size - 2 * lineWidth), (int)(2 * size - 2 * lineWidth), arcType.startAngle,
                90);
    }

    public static enum ArcType {
        /** Bottom-left arc. */
        BL_ARC(180, 0, 0.5, -1, 1.5),
        /** Bottom-right arc. */
        BR_ARC(270, -1, 1.5, -1, 1.5),
        /** Top-left arc. */
        TL_ARC(90, 0, 0.5, 0, 0.5),
        /** Top-right arc. */
        TR_ARC(0, -1, 1.5, 0, 0.5);

        /** Start angle for drawing the arc. */
        public final int startAngle;

        /** Size multiplication factor for X. */
        int xSizeMul;

        /** Line width multiplication factor for X. */
        double xLwidthMul;

        /** Size multiplication factor for Y. */
        int ySizeMul;

        /** Line width multiplication factor for Y. */
        double yLwidthMul;

        /**
         * Constructor of the {@link ArcType} class.
         *
         * @param startAngle Start angle for drawing the arc.
         * @param xSizeMul Size multiplication factor for X.
         * @param xLwidthMul Line width multiplication factor for X.
         * @param ySizeMul Size multiplication factor for Y.
         * @param yLwidthMul Line width multiplication factor for Y.
         */
        private ArcType(int startAngle, int xSizeMul, double xLwidthMul, int ySizeMul, double yLwidthMul) {
            this.startAngle = startAngle;
            this.xSizeMul = xSizeMul;
            this.xLwidthMul = xLwidthMul;
            this.ySizeMul = ySizeMul;
            this.yLwidthMul = yLwidthMul;
        }
    }
}
