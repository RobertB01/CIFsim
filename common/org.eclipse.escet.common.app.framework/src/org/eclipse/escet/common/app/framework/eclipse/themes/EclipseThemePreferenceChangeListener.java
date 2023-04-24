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

import java.util.function.Consumer;

import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.ui.PlatformUI;

/** Eclipse theme preference change listener. */
public class EclipseThemePreferenceChangeListener implements IPreferenceChangeListener {
    /** The callback to invoke when the Eclipse theme preference changes. */
    private final Consumer<PreferenceChangeEvent> callback;

    /**
     * Constructor for the {@link EclipseThemePreferenceChangeListener} class.
     *
     * <p>
     * Both creates and registers the listener.
     * </p>
     *
     * @param callback The callback to invoke when the Eclipse theme preference changes.
     */
    public EclipseThemePreferenceChangeListener(Consumer<PreferenceChangeEvent> callback) {
        this.callback = callback;
        register();
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent event) {
        // Handle Eclipse theme preference change.
        if (event.getKey().equals("themeid")) {
            callback.accept(event);
        }
    }

    /**
     * Registers this Eclipse theme preference change listener.
     *
     * <p>
     * For stand-alone (no workspace) executions, this call has no effect (retheming is not supported).
     * </p>
     */
    private void register() {
        if (PlatformUI.isWorkbenchRunning()) {
            EclipseThemeUtils.getEclipseThemePreferences().addPreferenceChangeListener(this);
        }
    }

    /**
     * Unregisters this Eclipse theme preference change listener.
     *
     * <p>
     * For stand-alone (no workspace) executions, this call has no effect (retheming is not supported).
     * </p>
     */
    public void unregister() {
        if (PlatformUI.isWorkbenchRunning()) {
            EclipseThemeUtils.getEclipseThemePreferences().removePreferenceChangeListener(this);
        }
    }
}
