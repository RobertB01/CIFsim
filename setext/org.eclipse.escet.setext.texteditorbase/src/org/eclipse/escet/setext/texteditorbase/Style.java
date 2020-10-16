//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.texteditorbase;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;

/**
 * Style for syntax highlighting in Eclipse text editors. Basically, it is a {@link TextAttribute}, but with color
 * description instead of an actual color.
 */
public class Style {
    /** The foreground color description, or {@code null} for default. */
    private final RGB foreground;

    /** The background color description, or {@code null} for default. */
    private final RGB background;

    /** The {@link Font font}, or {@code null} for default. */
    private final Font font;

    /** The additional {@link SWT} styles. */
    private final int style;

    /**
     * Constructor for the {@link Style} class, with default background color, default font, and no additional styles.
     *
     * @param foreground The foreground color description, or {@code null} for default.
     */
    public Style(RGB foreground) {
        this(foreground, null, null, SWT.NORMAL);
    }

    /**
     * Constructor for the {@link Style} class, with default background color, default font, and no additional styles.
     *
     * @param red The red component of the foreground color.
     * @param green The green component of the foreground color.
     * @param blue The blue component of the foreground color.
     */
    public Style(int red, int green, int blue) {
        this(new RGB(red, green, blue), null, null, SWT.NORMAL);
    }

    /**
     * Constructor for the {@link Style} class, with default background color and default font.
     *
     * @param foreground The foreground color description, or {@code null} for default.
     * @param style The additional {@link SWT} styles.
     */
    public Style(RGB foreground, int style) {
        this(foreground, null, null, style);
    }

    /**
     * Constructor for the {@link Style} class, with default background color and default font.
     *
     * @param red The red component of the foreground color.
     * @param green The green component of the foreground color.
     * @param blue The blue component of the foreground color.
     * @param style The additional {@link SWT} styles.
     */
    public Style(int red, int green, int blue, int style) {
        this(new RGB(red, green, blue), null, null, style);
    }

    /**
     * Constructor for the {@link Style} class.
     *
     * @param foreground The foreground color description, or {@code null} for default.
     * @param background The background color description, or {@code null} for default.
     * @param font The {@link Font font}, or {@code null} for default.
     * @param style The additional {@link SWT} styles.
     */
    public Style(RGB foreground, RGB background, Font font, int style) {
        this.foreground = foreground;
        this.background = background;
        this.font = font;
        this.style = style;
    }

    /**
     * Creates and returns a style token for this style.
     *
     * @param manager The color manager to use to create the color.
     * @return A token for this style.
     */
    public IToken createToken(ColorManager manager) {
        Color f = (foreground == null) ? null : manager.getColor(foreground);
        Color b = (background == null) ? null : manager.getColor(background);
        TextAttribute attr = new TextAttribute(f, b, style, font);
        return new Token(attr);
    }
}
