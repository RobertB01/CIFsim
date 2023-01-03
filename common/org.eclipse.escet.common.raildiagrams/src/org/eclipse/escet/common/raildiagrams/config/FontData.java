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

package org.eclipse.escet.common.raildiagrams.config;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import org.eclipse.escet.common.raildiagrams.util.Position2D;
import org.eclipse.escet.common.raildiagrams.util.Size2D;

/** Data about the font used for generating text in the output. */
public class FontData {
    /** Name of the font. */
    public final String fontName;

    /** Style of the font. */
    public final FontStyle fontStyle;

    /** Size of the font in pt. */
    public final int fontSize;

    /** The constructed font. */
    public final Font font;

    /**
     * Constructor of the {@link FontData} class.
     *
     * @param name Name of the font.
     * @param style Style of the font.
     * @param size Size of the font (in pt).
     */
    public FontData(String name, FontStyle style, int size) {
        this.fontName = name;
        this.fontStyle = style;
        this.fontSize = size;
        font = new Font(name, style.fontStyle, size);
    }

    /**
     * Paint the provided text at the indicated position.
     *
     * @param gd Graphics context.
     * @param x X coordinate of the top-left position.
     * @param y Y coordinate of the top-left position.
     * @param color Color of the text.
     * @param text Text to paint.
     */
    public void paint(int x, int y, Color color, Graphics2D gd, String text) {
        FontRenderContext renderContext = gd.getFontRenderContext();
        TextLayout layout = new TextLayout(text, font, renderContext);
        Rectangle2D bounds = layout.getPixelBounds(renderContext, 0, 0);
        gd.setColor(color);
        layout.draw(gd, (float)(x - bounds.getX()), (float)(y - bounds.getY()));
    }

    /**
     * Get the size and top-left offset of the box around the provided text.
     *
     * @param gd Graphics context.
     * @param text Text to measure.
     * @return Size and offset of the text.
     */
    public TextSizeOffset getTextSizeOffset(Graphics2D gd, String text) {
        FontRenderContext renderContext = gd.getFontRenderContext();
        TextLayout layout = new TextLayout(text, font, renderContext);
        Rectangle2D bounds = layout.getPixelBounds(renderContext, 0, 0);
        return new TextSizeOffset(new Position2D(0, 0),
                new Size2D((int)Math.ceil(bounds.getWidth()), (int)Math.ceil(bounds.getHeight())));
    }

    /** Available styles of text. */
    public static enum FontStyle {
        /** Italic style. */
        ITALIC(Font.ITALIC),

        /** Bold style. */
        BOLD(Font.BOLD),

        /** Plain style. */
        PLAIN(Font.PLAIN);

        /** Value of the style in {@link Font}. */
        public final int fontStyle;

        /**
         * Constructor of the {@link FontStyle} enum.
         *
         * @param fontStyle Value of the style in {@link Font}.
         */
        FontStyle(int fontStyle) {
            this.fontStyle = fontStyle;
        }
    }
}
