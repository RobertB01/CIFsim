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

package org.eclipse.escet.common.app.framework.appsview.ui.commands;

import org.eclipse.escet.common.app.framework.appsview.ui.AppsView;
import org.eclipse.escet.common.app.framework.management.AppStatus;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/** Base class for remove commands. */
public abstract class RemoveCommandBase extends ToolItemCommand {
    /**
     * Can the application and all of its descendants be removed?
     *
     * @param view The view that hosts this command.
     * @param item The tree item that represents the application for which to check whether it and all of its
     *     descendants can be removed.
     * @return {@code true} if an application and all of its descendants can be removed, {@code false} otherwise.
     */
    protected boolean canRemove(AppsView view, TreeItem item) {
        // Get status.
        Image icon = item.getImage();
        AppStatus status = view.getIcons().getStatus(icon);

        // Check remove, based on status.
        if (status.isBusy()) {
            return false;
        }

        // Can remove. Check children.
        for (TreeItem child: item.getItems()) {
            if (!canRemove(view, child)) {
                return false;
            }
        }
        return true;
    }
}
