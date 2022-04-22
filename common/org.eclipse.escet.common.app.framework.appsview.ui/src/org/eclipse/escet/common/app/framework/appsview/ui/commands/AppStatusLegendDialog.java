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

package org.eclipse.escet.common.app.framework.appsview.ui.commands;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.app.framework.appsview.ui.icons.AppStatusIcons;
import org.eclipse.escet.common.app.framework.management.AppStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/** Application status legend dialog. */
public class AppStatusLegendDialog extends Dialog {
    /** The application status icons. */
    private final AppStatusIcons icons;

    /**
     * Constructor for the {@link AppStatusLegendDialog} class.
     *
     * @param parentShell The parent shell.
     * @param icons The application status icons.
     */
    protected AppStatusLegendDialog(Shell parentShell, AppStatusIcons icons) {
        super(parentShell);
        this.icons = icons;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        // Get dialog area.
        Composite area = (Composite)super.createDialogArea(parent);
        area.setLayout(new FillLayout());

        // Add background.
        Composite background = new Composite(area, SWT.BORDER);
        background.setLayout(new GridLayout());

        // Put a tree in the center.
        Tree tree = new Tree(background, SWT.NO_SCROLL);
        tree.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

        // Set background color, same as background of tree.
        background.setBackground(tree.getBackground());

        // Add items for each status, with the proper icon.
        for (AppStatus status: AppStatus.values()) {
            // Get icon for status.
            Image icon = icons.getIcon(status);
            if (icon == null || icon.isDisposed()) {
                continue;
            }

            // Add item to tree.
            TreeItem item = new TreeItem(tree, SWT.NONE);
            item.setImage(icon);

            // Set item text.
            String text = status.toString().toLowerCase(Locale.US);
            text = StringUtils.capitalize(text);
            item.setText(text);
        }

        // Resize shell a bit wider, to make sure the title is fully shown.
        Point size = tree.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        parent.getShell().setMinimumSize((int)(size.x * 2.5), 50);

        // Return the dialog area.
        return area;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // Create only an 'OK' button. Overrides the default, which also
        // creates a 'Cancel' button.
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);

        // Upon showing the dialog, set focus to the 'OK' button.
        parent.getShell().addShellListener(new ShellAdapter() {
            @Override
            public void shellActivated(ShellEvent e) {
                getButton(IDialogConstants.OK_ID).setFocus();
            }
        });

        // Allow pressing 'ESC' to close the dialog.
        parent.getShell().addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent event) {
                if (event.character == SWT.ESC) {
                    // Pressing 'ESC' has the same affect as clicking 'OK'.
                    okPressed();

                    // The 'ESC' has been processed. Don't process it further.
                    event.detail = SWT.TRAVERSE_NONE;
                    event.doit = false;
                }
            }
        });
    }
}
