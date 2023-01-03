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

package org.eclipse.escet.common.app.framework;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

/** {@link SWT} related utility methods. */
public class SWTUtils {
    /** Constructor for the {@link SWTUtils} class. */
    private SWTUtils() {
        // Static class.
    }

    /**
     * Resizes the shell to a fraction of the width and height of the client area of the primary monitor.
     *
     * @param shell The shell to resize.
     * @param fraction The fraction of the width and height of the client area (0.1 for 10%, 0.75 for 75%, etc).
     */
    public static void resize(Shell shell, double fraction) {
        resize(shell, null, fraction);
    }

    /**
     * Resizes the shell to a fraction of the width and height of the client area of the given monitor.
     *
     * @param shell The shell to resize.
     * @param monitor The monitor to use, or {@code null} for the primary monitor.
     * @param fraction The fraction of the width and height of the client area (0.1 for 10%, 0.75 for 75%, etc).
     */
    public static void resize(Shell shell, Monitor monitor, double fraction) {
        // Get monitor.
        if (monitor == null) {
            monitor = shell.getDisplay().getPrimaryMonitor();
        }

        // Resize.
        shell.setSize((int)(monitor.getClientArea().width * fraction),
                (int)(monitor.getClientArea().height * fraction));
    }

    /**
     * Centers the shell in the client area of the primary monitor.
     *
     * @param shell The shell to center.
     */
    public static void center(Shell shell) {
        center(shell, null);
    }

    /**
     * Centers the shell in the client area of the given monitor.
     *
     * @param shell The shell to center.
     * @param monitor The monitor on which to center, or {@code null} for the primary monitor.
     */
    public static void center(Shell shell, Monitor monitor) {
        // Get monitor.
        if (monitor == null) {
            monitor = shell.getDisplay().getPrimaryMonitor();
        }

        // Get monitor client area and shell bounds. The monitor client area
        // origin may be negative, for multi-monitor setups.
        Rectangle monitorBounds = monitor.getClientArea();
        Rectangle shellBounds = shell.getBounds();

        // Calculate upper left coordinates of shell relative to upper left
        // coordinates of monitor, to center the shell on the monitor.
        int cx = (monitorBounds.width - shellBounds.width) / 2;
        int cy = (monitorBounds.height - shellBounds.height) / 2;

        // Compensate for shells larger than the monitor, by aligning them with
        // the upper left corner of the monitor's client area.
        cx = Math.max(0, cx);
        cy = Math.max(0, cy);

        // Get upper left coordinates of the shell, relative to the display.
        int x = monitorBounds.x + cx;
        int y = monitorBounds.y + cy;

        // Set the bounds of the shell, relative to the display.
        shell.setBounds(x, y, shellBounds.width, shellBounds.height);
    }

    /**
     * Causes the {@link Runnable#run run()} method of the runnable to be invoked by the SWT user-interface thread at
     * the next reasonable opportunity.
     *
     * <p>
     * Specifying {@code null} as the runnable simply wakes the user-interface thread, when run.
     * </p>
     *
     * <p>
     * If executed synchronously, the thread which calls this method is suspended until the runnable completes. If
     * executed asynchronously, the caller of this method continues to run in parallel, and is not notified when the
     * runnable has completed.
     * </p>
     *
     * <p>
     * Note that at the time the runnable is invoked, widgets that have the receiver as their display may have been
     * disposed. Therefore, it is necessary to check for this case inside the runnable before accessing the widget.
     * </p>
     *
     * @param display The SWT display to use to obtain the SWT user-interface thread on which to execute the code.
     * @param sync Whether to execute synchronously ({@code true}) or asynchronously ({@code false}).
     * @param runnable The code to run on the user-interface thread or {@code null}.
     * @see Display#asyncExec(Runnable)
     * @see Display#syncExec(Runnable)
     */
    public static void exec(Display display, boolean sync, Runnable runnable) {
        if (sync) {
            display.syncExec(runnable);
        } else {
            display.asyncExec(runnable);
        }
    }

    /**
     * Causes the {@link Runnable#run run()} method of the runnable to be invoked by the SWT user-interface thread at
     * the next reasonable opportunity.
     *
     * <p>
     * Specifying {@code null} as the runnable simply wakes the user-interface thread, when run.
     * </p>
     *
     * <p>
     * If executed synchronously, the thread which calls this method is suspended until the runnable completes. If
     * executed asynchronously, the caller of this method continues to run in parallel, and is not notified when the
     * runnable has completed.
     * </p>
     *
     * <p>
     * Note that at the time the runnable is invoked, widgets that have the receiver as their display may have been
     * disposed. Therefore, it is necessary to check for this case inside the runnable before accessing the widget.
     * </p>
     *
     * <p>
     * {@link Display#getDefault} is used to obtain the SWT user-interface thread on which to execute the code.
     * </p>
     *
     * @param sync Whether to execute synchronously ({@code true}) or asynchronously ({@code false}).
     * @param runnable The code to run on the user-interface thread or {@code null}.
     * @see Display#asyncExec(Runnable)
     * @see Display#syncExec(Runnable)
     */
    public static void exec(boolean sync, Runnable runnable) {
        exec(Display.getDefault(), sync, runnable);
    }
}
