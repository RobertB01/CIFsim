//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.appsview.ui.commands;

import org.eclipse.escet.common.app.framework.appsview.ui.AppsView;
import org.eclipse.escet.common.app.framework.management.AppStatus;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/** Base class for terminate commands. */
public abstract class TerminateCommandBase extends ToolItemCommand {
    /**
     * Can the application or one of its descendants be terminated?
     *
     * @param view The view that hosts this command.
     * @param item The tree item that represents the application for which to check whether it or its descendants can be
     *     terminated.
     * @return {@code true} if an application can be terminated or one of its descendants can be terminated,
     *     {@code false} otherwise.
     */
    protected boolean canTerminate(AppsView view, TreeItem item) {
        // Get status.
        Image icon = item.getImage();
        AppStatus status = view.getIcons().getStatus(icon);

        // Check terminate, based on status.
        if (status.canTerminate()) {
            return true;
        }

        // Can't terminate. Check children.
        for (TreeItem child: item.getItems()) {
            if (canTerminate(view, child)) {
                return true;
            }
        }
        return false;
    }
}
