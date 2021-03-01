//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.escet.common.app.framework.appsview.ui.AppsView;

/** Eclipse e4 base class for tool item check commands. */
public abstract class ToolItemCheckCommand extends ToolItemCommand {
    @Override
    public ItemType getItemType() {
        return ItemType.CHECK;
    }

    /**
     * Is this command currently selected?
     *
     * @param view The view that hosts this command.
     * @return {@code true} if the command is currently selected, {@code false} otherwise.
     */
    public abstract boolean isSelected(AppsView view);

    /**
     * Selects or deselects this command.
     *
     * @param view The view that hosts this command.
     * @param selected {@code true} to select this command, {@code false} to deselect it.
     */
    public abstract void setSelected(AppsView view, boolean selected);
}
