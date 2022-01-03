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

package org.eclipse.escet.common.raildiagrams.config;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

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
     * Compute the size of the provided text.
     *
     * @param gd Graphics context.
     * @param text Text to measure.
     * @return Size of the text.
     */
    public Size2D getTextSize(Graphics2D gd, String text) {
        FontMetrics metrics = gd.getFontMetrics(font);
        int width = metrics.stringWidth(text);
        int heigt = metrics.getHeight();
        return new Size2D(width, heigt);
    }

    /**
     * Compute the offset to print the provided text relative to its top-left corner.
     *
     * @param gd Graphics context.
     * @param text Text to measure.
     * @return Offset of the text relative to its top-left corner.
     */
    public Position2D getTextOffset(Graphics2D gd, String text) {
        FontMetrics metrics = gd.getFontMetrics(font);
        int ascent = metrics.getAscent();
        return new Position2D(0, ascent);
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
