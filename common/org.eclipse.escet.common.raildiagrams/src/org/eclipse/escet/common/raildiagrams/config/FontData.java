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

import java.awt.Font;

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
