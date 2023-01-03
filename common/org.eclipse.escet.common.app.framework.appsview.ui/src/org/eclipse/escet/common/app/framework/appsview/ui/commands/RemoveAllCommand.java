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

import static org.eclipse.escet.common.java.Sets.setc;

import java.util.Set;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.escet.common.app.framework.appsview.ui.AppsView;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/** Remove all command. */
public class RemoveAllCommand extends RemoveCommandBase {
    @Override
    public String getIconUri() {
        return "platform:/plugin/org.eclipse.escet.common.app.framework.appsview.ui/icons/command_remove_all.png";
    }

    @Override
    public String getItemLabel() {
        return "&Remove All";
    }

    @Override
    public String getTooltip() {
        return "Remove all listed applications that have terminated";
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
        return canRemove(part);
    }

    /**
     * Can an application be removed?
     *
     * @param part The part that contains the view that hosts this command.
     * @return {@code true} if an application can be removed, {@code false} otherwise.
     */
    private boolean canRemove(MPart part) {
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

        // Check whether can remove anything.
        for (TreeItem item: tree.getItems()) {
            if (canRemove(view, item)) {
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

        // Get items to remove.
        TreeItem[] roots = tree.getItems();
        Set<TreeItem> rootsToRemove = setc(roots.length);
        for (TreeItem root: roots) {
            if (canRemove(view, root)) {
                rootsToRemove.add(root);
            }
        }

        // Remove items.
        view.removeRootItems(rootsToRemove);
    }
}
