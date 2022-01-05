//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.console;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

/**
 * Console page participant for the {@link Console} console. Adds a {@link ConsoleTerminateAction} to the tool bar
 * manager of the console view.
 */
public class ConsolePageParticipant implements IConsolePageParticipant {
    /** The tool bar manager to use to show and hide the item. */
    private IToolBarManager tbman;

    /** The tool bar 'Terminate' action. */
    private IAction action;

    /** The tool bar 'Terminate' contribution item. */
    private IContributionItem item;

    @Override
    public void init(IPageBookViewPage page, IConsole console2) {
        // Get real console.
        Assert.check(console2 instanceof Console);
        Console console = (Console)console2;

        // Inform the console of our existence.
        console.setConsolePageParticipant(this);

        // Get tool bar manager.
        tbman = page.getSite().getActionBars().getToolBarManager();

        // Construct new action and contribution item.
        action = new ConsoleTerminateAction(console);
        item = new ActionContributionItem(action);

        // Add contribution item (with action) to the tool bar.
        tbman.add(item);
    }

    @Override
    public void dispose() {
        // Remove the item from the tool bar, and release all references to
        // allow garbage collection.
        if (tbman != null && item != null) {
            tbman.remove(item);
        }
        if (item != null) {
            item.dispose();
        }
        tbman = null;
        action = null;
        item = null;
    }

    @Override
    public void activated() {
        // Make the tool bar item visible and force update.
        item.setVisible(true);
        tbman.update(true);
    }

    @Override
    public void deactivated() {
        // Make the tool bar item invisible and force update.
        item.setVisible(false);
        tbman.update(true);
    }

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        // No adapter support.
        return null;
    }

    /** Disables the tool bar buttons. */
    public void disable() {
        final IAction action = this.action;
        final Display display = Display.getDefault();

        // Disable the button, on the display thread.
        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                if (display.isDisposed()) {
                    return;
                }
                if (action != null) {
                    action.setEnabled(false);
                }
            }
        });
    }
}
