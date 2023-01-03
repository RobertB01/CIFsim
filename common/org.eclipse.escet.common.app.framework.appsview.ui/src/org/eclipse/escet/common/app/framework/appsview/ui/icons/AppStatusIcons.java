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

package org.eclipse.escet.common.app.framework.appsview.ui.icons;

import static org.eclipse.escet.common.java.Maps.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import org.eclipse.escet.common.app.framework.management.AppStatus;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/** Application icons. */
public class AppStatusIcons {
    /** The mapping from statuses to icons. Empty after all icons have been disposed. */
    private Map<AppStatus, Image> icons = map();

    /** The mapping from icons to statuses. Empty after all icons have been disposed. */
    private Map<Image, AppStatus> statuses = map();

    /** Constructor for the {@link AppStatusIcons} class. */
    public AppStatusIcons() {
        for (AppStatus status: AppStatus.values()) {
            loadIcon(status);
        }
    }

    /**
     * Loads a single icon for an application status.
     *
     * @param status The application status.
     */
    private void loadIcon(AppStatus status) {
        // Load only once.
        if (icons.containsKey(status)) {
            throw new RuntimeException("Duplicate status: " + status);
        }

        // Get resource name.
        String statusName = status.toString().toLowerCase(Locale.US);
        String resName = getClass().getPackage().getName().replace('.', '/');
        resName += "/status_" + statusName + ".png";

        // Load image and store it.
        ClassLoader loader = getClass().getClassLoader();
        try (InputStream stream = loader.getResourceAsStream(resName)) {
            if (stream == null) {
                throw new RuntimeException("Could not find icon: " + resName);
            }

            Image image = new Image(Display.getDefault(), stream);
            icons.put(status, image);
            statuses.put(image, status);
        } catch (IOException ex) {
            throw new RuntimeException("Could not load icon: " + resName, ex);
        }
    }

    /**
     * Returns the icon for the given status.
     *
     * @param status The status.
     * @return The icon for the given status. May be {@code null} if no longer available.
     */
    public Image getIcon(AppStatus status) {
        return icons.get(status);
    }

    /**
     * Returns the status for the given icon.
     *
     * @param icon The icon.
     * @return The status for the given icon. May be {@code null} if no longer available.
     */
    public AppStatus getStatus(Image icon) {
        return statuses.get(icon);
    }

    /** Disposes of all the icon image resources. */
    public void dispose() {
        // Dispose icons mapping.
        for (Image image: icons.values()) {
            if (!image.isDisposed()) {
                image.dispose();
            }
        }
        icons.clear();

        // Dispose statuses mapping.
        for (Image image: statuses.keySet()) {
            if (!image.isDisposed()) {
                image.dispose();
            }
        }
        statuses.clear();
    }
}
