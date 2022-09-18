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

package org.eclipse.escet.common.app.framework.eclipse.themes;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.ui.PlatformUI;

/** Eclipse theme utility methods. */
@SuppressWarnings("restriction")
public class EclipseThemeUtils {
    /** Constructor for the {@link EclipseThemeUtils} class. */
    private EclipseThemeUtils() {
        // Static class.
    }

    /**
     * Returns whether the current Eclipse theme is a dark theme.
     *
     * <p>
     * This method only recognizes and supports built-in Eclipse themes. Any custom dark theme will thus not be detected
     * as a dark theme.
     * </p>
     *
     * @return {@code true} if the current Eclipse theme is a dark theme, {@code false} otherwise.
     */
    public static boolean isDarkThemeInUse() {
        IThemeEngine themeEngine = PlatformUI.getWorkbench().getService(IThemeEngine.class);
        ITheme theme = themeEngine.getActiveTheme();
        return theme != null && theme.getId().equals(ThemeEngine.E4_DARK_THEME_ID);
    }

    /**
     * Returns the Eclipse theme preferences.
     *
     * @return The Eclipse theme preferences.
     */
    public static IEclipsePreferences getEclipseThemePreferences() {
        return InstanceScope.INSTANCE.getNode(ThemeEngine.THEME_PLUGIN_ID);
    }
}
