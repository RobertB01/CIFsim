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

package org.eclipse.escet.product.perspective;

import org.eclipse.escet.common.app.framework.appsview.ui.AppsViewConstants;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

/** Eclipse ESCET perspective factory. */
public class ESCETPerspectiveFactory implements IPerspectiveFactory {
    @Override
    public void createInitialLayout(IPageLayout layout) {
        defineActions(layout);
        defineLayout(layout);
    }

    /**
     * Define the actions of the perspective.
     *
     * @param layout The page layout.
     */
    private void defineActions(IPageLayout layout) {
        // Add 'new wizards' (File -> New -> ...).
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.project");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");
        layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");

        // Add 'show views' (Window -> Show View -> ...).
        layout.addShowViewShortcut(IPageLayout.ID_PROJECT_EXPLORER);
        layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
        layout.addShowViewShortcut(IPageLayout.ID_PROP_SHEET);
        layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
        layout.addShowViewShortcut("org.eclipse.pde.ui.PluginsView");
        layout.addShowViewShortcut("org.eclipse.pde.runtime.RegistryBrowser");
        layout.addShowViewShortcut("org.eclipse.pde.runtime.LogView");
        layout.addShowViewShortcut("org.eclipse.jdt.ui.PackageExplorer");
        layout.addShowViewShortcut(AppsViewConstants.APPLICATIONS_VIEW_ID);

        // Add action sets (toolbar).
        layout.addActionSet("org.eclipse.ui.edit.text.actionSet.presentation");
    }

    /**
     * Define the layout of the perspective.
     *
     * @param layout The page layout.
     */
    private void defineLayout(IPageLayout layout) {
        // The editor area is available by default.
        String editorAreaId = layout.getEditorArea();

        // Create and fill folder of views to the left of the editor area.
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.20f, editorAreaId);
        left.addView(IPageLayout.ID_PROJECT_EXPLORER);

        // Create and fill folder of views at the bottom of the left area.
        IFolderLayout bottomLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.75f, "left");
        bottomLeft.addView(AppsViewConstants.APPLICATIONS_VIEW_ID);

        // Create and fill folder of views at the bottom of the editor area.
        IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.75f, editorAreaId);
        bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
        bottom.addView(IPageLayout.ID_PROP_SHEET);
        bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);
        bottom.addPlaceholder("org.eclipse.pde.runtime.LogView");
        bottom.addPlaceholder("org.eclipse.pde.ui.PluginsView");
        bottom.addPlaceholder("org.eclipse.pde.runtime.RegistryBrowser");
    }
}
