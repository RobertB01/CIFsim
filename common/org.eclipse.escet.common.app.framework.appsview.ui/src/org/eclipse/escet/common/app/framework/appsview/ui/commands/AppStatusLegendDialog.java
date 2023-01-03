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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

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
        area.setLayout(new GridLayout());

        // Add centered composite.
        Composite center = new Composite(area, SWT.NONE);
        center.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
        GridLayout centerLayout = new GridLayout(2, false);
        centerLayout.marginWidth = 8;
        centerLayout.marginHeight = 8;
        centerLayout.horizontalSpacing = 8;
        centerLayout.verticalSpacing = 8;
        center.setLayout(centerLayout);

        // Add each status.
        for (AppStatus status: AppStatus.values()) {
            // Get icon for status.
            Image icon = icons.getIcon(status);
            if (icon == null || icon.isDisposed()) {
                continue;
            }

            // Add canvas for icon.
            Canvas canvas = new Canvas(center, SWT.NO_REDRAW_RESIZE);
            canvas.addPaintListener(e -> e.gc.drawImage(icon, 0, 0));
            canvas.setLayoutData(new GridData(icon.getBounds().width, icon.getBounds().height));

            // Add label for description.
            Label label = new Label(center, SWT.NONE);
            String text = status.toString().toLowerCase(Locale.US);
            text = StringUtils.capitalize(text);
            label.setText(text);
        }

        // Resize shell a bit wider, to make sure the title is fully shown.
        Point size = center.computeSize(SWT.DEFAULT, SWT.DEFAULT);
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
