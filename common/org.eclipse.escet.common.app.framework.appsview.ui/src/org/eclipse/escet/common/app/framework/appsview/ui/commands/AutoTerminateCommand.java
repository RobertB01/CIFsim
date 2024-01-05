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

import static org.eclipse.escet.common.app.framework.appsview.ui.AppsViewConstants.AUTO_TERMINATE_PREF_ID;
import static org.eclipse.escet.common.java.Strings.str;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.escet.common.app.framework.appsview.ui.AppsView;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;

/** Auto terminate command. */
public class AutoTerminateCommand extends ToolItemCheckCommand {
    @Override
    public String getIconUri() {
        return "platform:/plugin/org.eclipse.escet.common.app.framework.appsview.ui/icons/command_auto_terminate.png";
    }

    @Override
    public String getItemLabel() {
        return "&Auto Terminate";
    }

    @Override
    public String getTooltip() {
        return "Auto Terminate: when starting a new application, automatically terminate all running applications";
    }

    @Override
    public String getContributionUri() {
        return "bundleclass://org.eclipse.escet.common.app.framework.appsview.ui/" + getClass().getName();
    }

    @Override
    public ItemType getItemType() {
        return ItemType.CHECK;
    }

    @Override
    public void execute(MPart part) {
        // Get the view.
        AppsView view = (AppsView)part.getObject();
        if (view == null) {
            return;
        }

        // Invert selected status.
        setSelected(view, !view.autoTerminate.get());
    }

    @Override
    public boolean isSelected(AppsView view) {
        return view.autoTerminate.get();
    }

    @Override
    public void setSelected(AppsView view, boolean selected) {
        // Update locally stored preference.
        view.autoTerminate.set(selected);

        // Update preference store.
        IPreferenceStore store = PlatformUI.getPreferenceStore();
        store.setValue(AUTO_TERMINATE_PREF_ID, str(selected));
    }
}
