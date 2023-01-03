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

package org.eclipse.escet.common.eclipse.ui;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.EditorPart;

/**
 * Dummy implementation of {@link IEditorSite} that does nothing. It is used by the {@link ControlEditor} class as value
 * passed to the {@link EditorPart#init} method, as {@code null} is not allowed.
 */
@SuppressWarnings("deprecation")
public class DummyEditorSite implements IEditorSite {
    @Override
    public String getId() {
        return "dummy";
    }

    @Override
    public String getPluginId() {
        return "dummy";
    }

    @Override
    public String getRegisteredName() {
        return "dummy";
    }

    @Override
    public void registerContextMenu(String menuId, MenuManager menuManager, ISelectionProvider selectionProvider) {
        // Dummy body.
    }

    @Override
    public void registerContextMenu(MenuManager menuManager, ISelectionProvider selectionProvider) {
        // Dummy body.
    }

    @Override
    public IKeyBindingService getKeyBindingService() {
        return null;
    }

    @Override
    public IWorkbenchPart getPart() {
        return null;
    }

    @Override
    public IWorkbenchPage getPage() {
        return null;
    }

    @Override
    public ISelectionProvider getSelectionProvider() {
        return null;
    }

    @Override
    public Shell getShell() {
        return null;
    }

    @Override
    public IWorkbenchWindow getWorkbenchWindow() {
        return null;
    }

    @Override
    public void setSelectionProvider(ISelectionProvider provider) {
        // Dummy body.
    }

    @Override
    public <T> T getAdapter(Class<T> adapter) {
        return null;
    }

    @Override
    public <T> T getService(Class<T> api) {
        return null;
    }

    @Override
    public boolean hasService(Class<?> api) {
        return false;
    }

    @Override
    public IEditorActionBarContributor getActionBarContributor() {
        return null;
    }

    @Override
    public IActionBars getActionBars() {
        return null;
    }

    @Override
    public void registerContextMenu(MenuManager menuManager, ISelectionProvider selectionProvider,
            boolean includeEditorInput)
    {
        // Dummy body.
    }

    @Override
    public void registerContextMenu(String menuId, MenuManager menuManager, ISelectionProvider selectionProvider,
            boolean includeEditorInput)
    {
        // Dummy body.
    }
}
