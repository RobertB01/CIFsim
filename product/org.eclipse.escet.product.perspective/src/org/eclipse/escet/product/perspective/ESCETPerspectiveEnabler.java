//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.product.perspective;

import static org.eclipse.escet.product.perspective.ESCETPerspectiveConstants.ESCET_ENABLED_PREF_ID;
import static org.eclipse.escet.product.perspective.ESCETPerspectiveConstants.ESCET_PERSPECTIVE_ID;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/** Eclipse ESCET perspective enabler. */
public class ESCETPerspectiveEnabler implements IStartup {
    @Override
    public void earlyStartup() {
        // Check whether to enable perspective.
        IPreferenceStore store = PlatformUI.getPreferenceStore();
        String pref = store.getString(ESCET_ENABLED_PREF_ID);
        boolean alreadyEnabled = pref.equals("true");
        if (alreadyEnabled) {
            return;
        }

        // Get display.
        Display display = Display.getDefault();
        if (display.isDisposed()) {
            return;
        }

        // Activate perspective, on UI thread.
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                activatePerspective(ESCET_PERSPECTIVE_ID);
            }
        });

        // Set preference that perspective has been changed.
        store.setValue(ESCET_ENABLED_PREF_ID, "true");
    }

    /**
     * Activate a perspective.
     *
     * @param perspectiveId The unique ID of the perspective.
     */
    private void activatePerspective(String perspectiveId) {
        // Get workbench.
        IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null) {
            return;
        }

        // Get active window.
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window == null) {
            return;
        }

        // Get active page.
        IWorkbenchPage page = window.getActivePage();
        if (page == null) {
            return;
        }

        // Get perspective registry.
        IPerspectiveRegistry registry = workbench.getPerspectiveRegistry();
        if (registry == null) {
            return;
        }

        // Get Eclipse Foundation perspective.
        IPerspectiveDescriptor perspective = registry.findPerspectiveWithId(perspectiveId);
        if (perspective == null) {
            return;
        }

        // Change perspective.
        page.setPerspective(perspective);
    }
}
