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

import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.escet.setext.texteditorbase.Style;
import org.eclipse.ui.PlatformUI;

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
@SuppressWarnings("restriction")
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
        theme = isDarkThemeInUse() ? darkTheme : lightTheme;
    }

    /**
     * Returns whether the current Eclipse theme is a dark theme.
     *
     * @return {@code true} if the current Eclipse theme is a dark theme, {@code false} otherwise.
     */
    private boolean isDarkThemeInUse() {
        IThemeEngine themeEngine = PlatformUI.getWorkbench().getService(IThemeEngine.class);
        ITheme theme = themeEngine.getActiveTheme();
        return theme != null && theme.getId().equals(ThemeEngine.E4_DARK_THEME_ID);
    }

    @Override
    public Style getStyle(T namedStyle) {
        return theme.getStyle(namedStyle);
    }
}
