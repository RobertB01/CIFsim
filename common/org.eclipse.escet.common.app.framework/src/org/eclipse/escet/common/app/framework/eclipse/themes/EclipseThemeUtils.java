//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import org.apache.commons.lang3.SystemUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.ui.css.swt.internal.theme.ThemeEngine;
import org.eclipse.e4.ui.css.swt.theme.ITheme;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.swt.widgets.Display;
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
        if (PlatformUI.isWorkbenchRunning()) {
            // This method uses internal Eclipse APIs to prevent hardcoding the theme id. This way, if the theme id
            // changes, or is removed, we should get a compile error, and we know something is broken. Otherwise, it
            // would just never detect a dark theme.
            IThemeEngine themeEngine = PlatformUI.getWorkbench().getService(IThemeEngine.class);
            ITheme theme = themeEngine.getActiveTheme();
            return theme != null && theme.getId().equals(ThemeEngine.E4_DARK_THEME_ID);
        } else if (SystemUtils.IS_OS_WINDOWS) {
            // In stand-alone mode on Windows, SWT is always light-mode.
            return false;
        } else {
            // Running stand-alone on macOS or Linux, SWT colors depend on the system-mode.
            return Display.isSystemDarkTheme();
        }
    }

    /**
     * Returns the Eclipse theme preferences.
     *
     * @return The Eclipse theme preferences.
     */
    public static IEclipsePreferences getEclipseThemePreferences() {
        // This method uses internal Eclipse APIs to prevent hardcoding the theme plugin id. This way, if the plugin id
        // changes, we get a compile error that notifies us something is broken.
        return InstanceScope.INSTANCE.getNode(ThemeEngine.THEME_PLUGIN_ID);
    }
}
