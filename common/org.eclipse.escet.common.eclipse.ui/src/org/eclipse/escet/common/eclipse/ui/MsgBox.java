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

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * Simple helper class for SWT message boxes.
 *
 * @see MessageBox
 */
public final class MsgBox {
    /** Constructor for the {@link MsgBox} class. */
    private MsgBox() {
        // Private constructor to turn this class into a static class.
    }

    /**
     * Shows a {@link MessageBox}, using a single method call.
     *
     * @param shell The shell to display the message box for.
     * @param style The style of the message box. See also {@link MessageBox} and
     *     {@link MessageBox#MessageBox(Shell, int)}.
     * @param title The title of the message box.
     * @param message The message to display in the message box.
     * @return The id of the button that was selected to dismiss the message box (e.g. SWT.OK, SWT.CANCEL, etc). See
     *     also {@link MessageBox#open}.
     */
    public static final int show(Shell shell, int style, String title, String message) {
        MessageBox mb = new MessageBox(shell, style);
        mb.setText(title);
        mb.setMessage(message);
        return mb.open();
    }
}
