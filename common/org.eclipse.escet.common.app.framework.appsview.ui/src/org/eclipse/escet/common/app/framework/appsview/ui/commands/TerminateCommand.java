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

import static org.eclipse.escet.common.java.Sets.setc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.appsview.ui.AppsView;
import org.eclipse.escet.common.app.framework.management.AppManager;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/** Terminate command. */
public class TerminateCommand extends TerminateCommandBase {
    @Override
    public String getIconUri() {
        return "platform:/plugin/org.eclipse.escet.common.app.framework.appsview.ui/icons/command_remove.png";
    }

    @Override
    public String getItemLabel() {
        return "T&erminate";
    }

    @Override
    public String getTooltip() {
        return "Terminate all selected application";
    }

    @Override
    public String getContributionUri() {
        return "bundleclass://org.eclipse.escet.common.app.framework.appsview.ui/" + getClass().getName();
    }

    @Override
    public ItemType getItemType() {
        return ItemType.PUSH;
    }

    /**
     * Is the command enabled?
     *
     * @param part The part that contains the view that hosts this command.
     * @return {@code true} if the command is enabled, {@code false} otherwise.
     */
    @CanExecute
    public boolean isEnabled(MPart part) {
        return canTerminate(part);
    }

    /**
     * Can a selected root application or any of its descendants be terminated?
     *
     * @param part The part that contains the view that hosts this command.
     * @return {@code true} if a selected root application or any of its descendants can be terminated, {@code false}
     *     otherwise.
     */
    private boolean canTerminate(MPart part) {
        // Get the view.
        AppsView view = (AppsView)part.getObject();
        if (view == null) {
            return false;
        }

        // Get the tree.
        Tree tree = view.getTree();
        if (tree == null || tree.isDisposed()) {
            return false;
        }

        // Check whether can terminate anything.
        Set<TreeItem> selectedRoots = CommandUtils.getSelectedRoots(tree);
        for (TreeItem item: selectedRoots) {
            if (canTerminate(view, item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute(MPart part) {
        // Get the view.
        AppsView view = (AppsView)part.getObject();
        if (view == null) {
            return;
        }

        // Get the tree.
        Tree tree = view.getTree();
        if (tree == null || tree.isDisposed()) {
            return;
        }

        // Get items to terminate.
        Set<TreeItem> selectedRoots = CommandUtils.getSelectedRoots(tree);
        Set<TreeItem> rootsToTerminate = setc(selectedRoots.size());
        for (TreeItem root: selectedRoots) {
            if (canTerminate(view, root)) {
                rootsToTerminate.add(root);
            }
        }

        // Get applications to terminate.
        Set<Application<?>> appsToTerminate = setc(rootsToTerminate.size());
        Map<Application<?>, TreeItem> treeItems = view.getTreeItems();
        for (Entry<Application<?>, TreeItem> entry: treeItems.entrySet()) {
            if (rootsToTerminate.contains(entry.getValue())) {
                appsToTerminate.add(entry.getKey());
            }
        }

        // Terminate root applications.
        for (Application<?> app: appsToTerminate) {
            AppManager.terminate(app);
        }
    }
}
