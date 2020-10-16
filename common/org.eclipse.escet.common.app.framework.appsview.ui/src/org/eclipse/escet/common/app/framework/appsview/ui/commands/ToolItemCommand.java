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

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;

/** Eclipse e4 base class for tool item commands. */
public abstract class ToolItemCommand {
    /**
     * Returns the tool's icon URI.
     *
     * @return The tool's icon URI.
     */
    public abstract String getIconUri();

    /**
     * Returns the tool item's label. Should contain a {@code "&"} mnemonic character.
     *
     * @return The tool item's label.
     */
    public abstract String getItemLabel();

    /**
     * Returns the tool item's tooltip.
     *
     * @return The tool item's tooltip.
     */
    public abstract String getTooltip();

    /**
     * Returns the tool's contribution URI, i.e. the URI for the class implementing the command, i.e for this class.
     *
     * @return The tool's contribution URI.
     */
    public abstract String getContributionUri();

    /**
     * Returns the tool item's type.
     *
     * @return The tool item's type.
     */
    public abstract ItemType getItemType();

    /**
     * Execute the command.
     *
     * @param part The part that contains the view that hosts this command.
     */
    @Execute
    public abstract void execute(MPart part);
}
