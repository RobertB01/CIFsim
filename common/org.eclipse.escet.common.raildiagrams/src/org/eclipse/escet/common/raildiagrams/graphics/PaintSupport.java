//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation
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
        return gd;
    }
}
