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

import static org.eclipse.escet.common.java.Sets.setc;

import java.util.Set;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/** Applications view command utility methods. */
public class CommandUtils {
    /** Constructor for the {@link CommandUtils} class. */
    private CommandUtils() {
        // Static class.
    }

    /**
     * Returns the root items for all selected items of the given tree.
     *
     * @param tree The tree.
     * @return The root items for all selected items.
     */
    public static Set<TreeItem> getSelectedRoots(Tree tree) {
        TreeItem[] selected = tree.getSelection();
        Set<TreeItem> roots = setc(selected.length);
        for (TreeItem item: selected) {
            while (item.getParentItem() != null) {
                item = item.getParentItem();
            }
            roots.add(item);
        }
        return roots;
    }

    /**
     * Collect items for a (sub-)tree.
     *
     * @param item The item to be collect. This item and all its descendants are collected.
     * @param items The already collected items. Is modified in-place.
     */
    public static void collectItems(TreeItem item, Set<TreeItem> items) {
        items.add(item);
        for (TreeItem child: item.getItems()) {
            collectItems(child, items);
        }
    }
}
