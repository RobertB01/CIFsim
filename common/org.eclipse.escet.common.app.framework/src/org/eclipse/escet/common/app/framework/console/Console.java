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

package org.eclipse.escet.common.app.framework.console;

import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.io.EclipseConsoleAppStream;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;

/**
 * Console for use within the Eclipse UI. Provides:
 * <ul>
 * <li>Standard input, output, and error streams, with colors.</li>
 * <li>Line buffered output and error.</li>
 * <li>A convenient, thread-safe way to set the console name (title).</li>
 * </ul>
 *
 * @see <a href="http://wiki.eclipse.org/FAQ_How_do_I_write_to_the_console_from_a_plug-in%3F">FAQ: How do I write to the
 *     console from a plug-in?</a>
 */
public class Console extends IOConsole {
    /** The color to use for the input stream. */
    private static final Color COLOR_IN = new Color(0, 200, 125);

    /** The color to use for the output stream. */
    private static final Color COLOR_OUT = new Color(0, 0, 0);

    /** The color to use for the error stream. */
    private static final Color COLOR_ERR = new Color(255, 0, 0);

    /**
     * The console streams. The input stream reader uses a default buffer size, and a default character encoding. The
     * output and error streams also use default buffer sizes, and automatically flush on new lines.
     */
    protected final AppStreams streams;

    /**
     * The application that uses this console. Used to figure out which application to terminate if the 'Terminate'
     * button on the console is used. Is {@code null} if not available.
     *
     * @see #setApplication
     * @see #cleanup
     */
    private Application<?> application;

    /**
     * The console page participant for this console. Manages the tool bar buttons. Is {@code null} if not available.
     */
    private ConsolePageParticipant consolePageParticipant;

    /**
     * Constructor for the {@link Console} class.
     *
     * @param title The (initial) console title.
     */
    public Console(String title) {
        super(title, null);

        // Get input stream, and construct the output and error streams.
        IOConsoleInputStream in = getInputStream();
        IOConsoleOutputStream out = newOutputStream();
        IOConsoleOutputStream err = newOutputStream();

        // Set stream colors.
        in.setColor(COLOR_IN);
        out.setColor(COLOR_OUT);
        err.setColor(COLOR_ERR);

        // Save streams.
        AppStream appOut = new EclipseConsoleAppStream(out);
        AppStream appErr = new EclipseConsoleAppStream(err);
        streams = new AppStreams(in, appOut, appErr);

        // Register the console with the console manager, and show it.
        IConsoleManager manager = ConsolePlugin.getDefault().getConsoleManager();
        manager.addConsoles(new IConsole[] {this});
        manager.showConsoleView(this);
    }

    /**
     * Sets the console page participant. Should only be called by the {@link ConsolePageParticipant#init} method.
     *
     * @param participant The console page participant.
     * @see #consolePageParticipant
     */
    public void setConsolePageParticipant(ConsolePageParticipant participant) {
        consolePageParticipant = participant;
    }

    /**
     * Returns the console streams. The input stream reader uses a default buffer size, and a default character
     * encoding. The output and error streams also use default buffer sizes, and automatically flush on new lines.
     *
     * @return The console streams.
     */
    public AppStreams getStreams() {
        return streams;
    }

    /**
     * Returns the application that uses this console, or {@code null} if not available.
     *
     * @return The application that uses this console, or {@code null}.
     * @see #application
     */
    public Application<?> getApplication() {
        return application;
    }

    /**
     * Sets the console name (title) synchronously, on the Eclipse UI thread.
     *
     * @param name The new name (title) of the console.
     */
    public void setNameSync(final String name) {
        final Display display = Display.getDefault();

        display.syncExec(new Runnable() {
            @Override
            public void run() {
                if (display.isDisposed()) {
                    return;
                }
                Console.super.setName(name);
            }
        });
    }

    /**
     * Sets the console name (title) asynchronously, on the Eclipse UI thread.
     *
     * @param name The new name (title) of the console.
     */
    public void setNameAsync(final String name) {
        final Display display = Display.getDefault();

        display.asyncExec(new Runnable() {
            @Override
            public void run() {
                if (display.isDisposed()) {
                    return;
                }
                Console.super.setName(name);
            }
        });
    }

    /**
     * Sets the application that uses this console. Also informs the application that it is coupled to this console.
     *
     * @param application The application that uses this console.
     * @see #application
     */
    public void setApplication(Application<?> application) {
        this.application = application;
        application.getAppEnvData().setConsole(this);
    }

    /**
     * Cleanup this console. Should be called if the console is no longer used by an application. Frees up the
     * resources, to allow garbage collection, and disables any console buttons.
     */
    public void cleanup() {
        // Disable console buttons.
        if (consolePageParticipant != null) {
            consolePageParticipant.disable();
        }

        // Free up resources (references) to allow garbage collection.
        if (application != null) {
            application.getAppEnvData().setConsole(null);
        }
        this.application = null;
        this.consolePageParticipant = null;
    }
}
