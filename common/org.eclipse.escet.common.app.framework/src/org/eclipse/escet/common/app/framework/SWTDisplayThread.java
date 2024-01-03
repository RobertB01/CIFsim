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

import static org.eclipse.escet.common.java.Pair.pair;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.escet.common.app.framework.options.GuiMode;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.exceptions.ApplicationException;
import org.eclipse.escet.common.java.exceptions.DependencyException;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.Display;

/**
 * SWT display thread. If the application is running on the command line, we want to create a thread devoted to SWT, to
 * read and dispatch events. If we run inside Eclipse, no such thread is necessary, as we can reuse the SWT display
 * thread of Eclipse. As such, an instance of this class should only be created for command line execution.
 */
public class SWTDisplayThread extends Thread {
    /**
     * Synchronization object to use to signal the caller that it is known whether this thread has become the display
     * thread or not.
     */
    private final AtomicBoolean ready = new AtomicBoolean(false);

    /**
     * Whether this thread has become the SWT display thread. Only has a relevant value after when {@link #ready}
     * becomes {@code true} and if there was no {@link #error}.
     */
    private final AtomicBoolean isDisplayThread = new AtomicBoolean(false);

    /**
     * The error that occurred starting the display thread. Only has a relevant value after when {@link #ready} becomes
     * {@code true}. Value remains {@code null} if there was no problem starting the display thread.
     */
    private final AtomicReference<ApplicationException> error = new AtomicReference<>();

    /** SWT display owned by this thread. Is {@code null} until created. */
    private Display display;

    /** Constructor for the {@link SWTDisplayThread} class. */
    private SWTDisplayThread() {
        super("SWTDisplayThread");
    }

    @Override
    public void run() {
        // Ask for the default display. It either does not yet exist, and is
        // created on this thread, or it already exists.
        boolean failed = false;
        try {
            display = Display.getDefault();
        } catch (SWTError | UnsatisfiedLinkError ex) {
            // May be out of handles, running headless, without display,
            // without X11 forwarding, without X11 libraries, etc.
            String msg = "Failed to obtain SWT display. If running headless (without a display), make sure to "
                    + "connect a display (e.g. on Linux enable X11 server/forwarding), or disable GUI functionality "
                    + "(\"--gui=off\" command line option).";
            error.set(new DependencyException(msg, ex));
            failed = true;
        }

        // If this thread is not the display thread, another thread already
        // existed. Otherwise, this thread is now the display thread.
        if (!failed) {
            Thread thisThread = Thread.currentThread();
            Thread displayThread = display.getThread();
            isDisplayThread.set(thisThread == displayThread);
        }

        // Signal the caller that it is known whether this thread has become
        // the display thread.
        synchronized (ready) {
            ready.set(true);
            ready.notify();
        }

        // If this thread has not become the SWT display thread, it is no
        // longer needed.
        if (failed || !isDisplayThread.get()) {
            return;
        }

        // Event loop. Keep reading and dispatching SWT events until the
        // display is disposed. This loop spends most of its time sleeping,
        // waiting to be notified of new events.
        while (!display.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Creates and starts an {@link SWTDisplayThread}, if no SWT display thread exists yet.
     *
     * @param mode The GUI mode. Must not be {@link GuiMode#OFF}.
     * @return A pair consisting of a value indicating whether a GUI is available, and the newly created SWT display
     *     thread. The thread may be {@code null} if an SWT display thread already exists, or could not be created and
     *     the {@link GuiMode#AUTO} is used (in which case the GUI is not available). If an SWT display thread is
     *     returned, a GUI is available.
     */
    public static Pair<Boolean, SWTDisplayThread> create(GuiMode mode) {
        // Create potential SWT display thread and start it.
        SWTDisplayThread thread = new SWTDisplayThread();
        thread.start();

        // Wait to be notified by the potential SWT display thread that it is
        // known whether is has become the SWT display thread.
        synchronized (thread.ready) {
            while (!thread.ready.get()) {
                try {
                    thread.ready.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Check for error creating display thread.
        boolean guiAvailable;
        if (thread.error.get() == null) {
            guiAvailable = true;
        } else {
            switch (mode) {
                case ON:
                    throw thread.error.get();

                case AUTO:
                    guiAvailable = false;
                    break;

                default:
                    throw new RuntimeException("Unknown GUI mode: " + mode);
            }
        }

        // Return whether a GUI is available, and the newly created SWT display
        // thread (if it has actually become a SWT display thread).
        SWTDisplayThread swtThread = thread.isDisplayThread.get() ? thread : null;
        Assert.implies(swtThread != null, guiAvailable);
        Assert.implies(mode == GuiMode.ON, guiAvailable);
        Assert.implies(!guiAvailable, mode == GuiMode.AUTO);
        return pair(guiAvailable, swtThread);
    }

    /** Closes the SWT display owned by this thread. */
    public void close() {
        if (display.isDisposed()) {
            return;
        }

        final Display d = display;
        d.syncExec(new Runnable() {
            @Override
            public void run() {
                if (d.isDisposed()) {
                    return;
                }
                d.close();
            }
        });
    }
}
