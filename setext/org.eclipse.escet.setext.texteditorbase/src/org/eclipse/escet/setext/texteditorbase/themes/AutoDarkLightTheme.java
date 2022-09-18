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

package org.eclipse.escet.setext.texteditorbase.themes;

import org.eclipse.escet.common.app.framework.eclipse.themes.EclipseThemeUtils;
import org.eclipse.escet.setext.texteditorbase.Style;

/**
 * A text editor that automatically uses a dark theme or light theme, depending on the theme currently used by Eclipse.
 *
 * <p>
 * This theme only recognizes and supports built-in Eclipse themes. Any custom dark theme will thus not be detected as a
 * dark theme, but as a light theme.
 * </p>
 *
 * @param <T> The enum with the named styles of a text editor.
 */
public class AutoDarkLightTheme<T> implements TextEditorTheme<T> {
    /** The dark or light theme to use. */
    private final TextEditorTheme<T> theme;

    /**
     * Constructor for the {@link AutoDarkLightTheme} class.
     *
     * @param darkTheme The dark theme to use.
     * @param lightTheme The light them to use.
     */
    public AutoDarkLightTheme(TextEditorTheme<T> darkTheme, TextEditorTheme<T> lightTheme) {
        theme = EclipseThemeUtils.isDarkThemeInUse() ? darkTheme : lightTheme;
    }

    @Override
    public Style getStyle(T namedStyle) {
        return theme.getStyle(namedStyle);
    }
}
