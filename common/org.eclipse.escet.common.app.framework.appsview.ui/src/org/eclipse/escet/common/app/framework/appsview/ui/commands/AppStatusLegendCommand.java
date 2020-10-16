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

package org.eclipse.escet.common.app.framework.appsview.ui.commands;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.escet.common.app.framework.appsview.ui.AppsView;
import org.eclipse.escet.common.app.framework.appsview.ui.icons.AppStatusIcons;
import org.eclipse.swt.widgets.Tree;

/** Legend command. */
public class AppStatusLegendCommand extends ToolItemCommand {
    @Override
    public String getIconUri() {
        return "platform:/plugin/org.eclipse.escet.common.app.framework.appsview.ui/icons/command_app_status_legend.png";
    }

    @Override
    public String getItemLabel() {
        return "Application Status &Legend";
    }

    @Override
    public String getTooltip() {
        return "Show the legend for the application status icons";
    }

    @Override
    public String getContributionUri() {
        return "bundleclass://org.eclipse.escet.common.app.framework.appsview.ui/" + getClass().getName();
    }

    @Override
    public ItemType getItemType() {
        return ItemType.PUSH;
    }

    @Override
    public void execute(MPart part) {
        // Get view.
        AppsView view = (AppsView)part.getObject();
        if (view == null) {
            return;
        }

        // Get tree.
        Tree tree = view.getTree();
        if (tree == null || tree.isDisposed()) {
            return;
        }

        // Get icons.
        AppStatusIcons icons = view.getIcons();

        // Show dialog.
        AppStatusLegendDialog dialog = new AppStatusLegendDialog(tree.getShell(), icons);
        dialog.create();
        dialog.getShell().setText(getItemLabel().replace("&", ""));
        dialog.getShell().setVisible(true);
    }
}
