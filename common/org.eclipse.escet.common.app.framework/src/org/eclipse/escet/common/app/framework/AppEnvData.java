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

package org.eclipse.escet.common.app.framework;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.escet.common.app.framework.console.Console;
import org.eclipse.escet.common.app.framework.exceptions.DependencyException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.management.AppManager;
import org.eclipse.escet.common.app.framework.management.AppStatus;
import org.eclipse.escet.common.app.framework.options.GuiMode;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputProvider;

/**
 * Application specific environment data, that is to be managed by the application environment manager (the
 * {@link AppEnv} class}.
 */
public class AppEnvData {
    /** The application that the environment data belongs to. */
    private final Application<?> application;

    /** The application's streams. */
    private final AppStreams streams;

    /** The application's output provider. */
    private final OutputProvider<?> provider;

    /** The application's options. */
    private final Options options;

    /** The application specific properties. */
    private final AppProperties properties;

    /** Whether termination of the application is requested. */
    private AtomicBoolean terminationRequested = new AtomicBoolean(false);

    /**
     * The Eclipse console that the application is connected to, or {@code null} if not applicable or not (yet)
     * available.
     */
    private Console console;

    /** Whether a SWT-based GUI is available. */
    private AtomicBoolean guiAvailable = new AtomicBoolean(false);

    /**
     * SWT display thread. Is {@code null} until created. Remains {@code null} if running in Eclipse, if already created
     * and maintained by a parent application framework application, or if running without GUI.
     *
     * @see GuiMode
     */
    private AtomicReference<SWTDisplayThread> swtThread = new AtomicReference<>();

    /**
     * Constructor for the {@link AppEnvData} class, with standard application streams, a new default provider (using
     * {@link IOutputComponent}), a new empty options collection, and the current system properties as application
     * properties.
     *
     * @param application The application that the environment data belongs to.
     */
    public AppEnvData(Application<?> application) {
        this(application, new AppStreams(), new OutputProvider<>(), new Options(), new AppProperties());
    }

    /**
     * Constructor for the {@link AppEnvData} class.
     *
     * @param application The application that the environment data belongs to.
     * @param streams The application's streams.
     * @param provider The application's output provider.
     * @param options The application's options.
     * @param properties The application's properties.
     */
    public AppEnvData(Application<?> application, AppStreams streams, OutputProvider<?> provider, Options options,
            AppProperties properties)
    {
        this.application = application;
        this.streams = streams;
        this.provider = provider;
        this.options = options;
        this.properties = properties;
    }

    /**
     * Returns the application that the environment data belongs to.
     *
     * @return The application that the environment data belongs to.
     */
    public Application<?> getApplication() {
        return application;
    }

    /**
     * Returns the application's streams.
     *
     * @return The application's streams.
     */
    public AppStreams getStreams() {
        return streams;
    }

    /**
     * Returns the application's output provider.
     *
     * @return The application's output provider.
     */
    public OutputProvider<?> getProvider() {
        return provider;
    }

    /**
     * Returns the application's options.
     *
     * @return The application's options.
     */
    public Options getOptions() {
        return options;
    }

    /**
     * Returns the application's properties.
     *
     * @return The application's properties.
     */
    public AppProperties getProperties() {
        return properties;
    }

    /**
     * Returns a value indicating whether termination of the application is requested.
     *
     * @return {@code true} if termination is requested, {@code false} otherwise.
     */
    public boolean isTerminationRequested() {
        return terminationRequested.get();
    }

    /** Requests termination of the application. */
    public synchronized void terminate() {
        // Method is synchronized to prevent two termination requests to occur
        // at the same time, which could lead to two updates being sent to the
        // application manager.

        // Request termination.
        boolean previouslyRequested = terminationRequested.getAndSet(true);

        // Inform the application manager of the termination request.
        if (!previouslyRequested) {
            AppManager.update(application, AppStatus.TERMINATING);
        }
    }

    /**
     * Returns the Eclipse console that the application is connected to, or {@code null} if not applicable or not (yet)
     * available.
     *
     * @return The Eclipse console that the application is connected to, or {@code null}.
     */
    public Console getConsole() {
        return console;
    }

    /**
     * Sets the Eclipse console that the application is connected to. This method must only be called by the
     * {@link Console#setApplication} method.
     *
     * @param console The Eclipse console that the application is connected to.
     */
    public void setConsole(Console console) {
        this.console = console;
    }

    /**
     * Returns the SWT display thread. Is {@code null} until created. Remains {@code null} if running in Eclipse, if
     * already created and maintained by a parent application framework application, or if running without GUI.
     *
     * @return The SWT display thread, or {@code null}.
     * @see GuiMode
     */
    public SWTDisplayThread getSwtDisplayThread() {
        return swtThread.get();
    }

    /**
     * Sets or unsets the SWT display thread. Must only be set or unset by the application framework.
     *
     * @param thread The SWT display thread to set, or {@code null} to unset.
     * @see GuiMode
     */
    public void setSwtDisplayThread(SWTDisplayThread thread) {
        swtThread.set(thread);
    }

    /**
     * Set a value indicating whether an SWT-based GUI is available.
     *
     * @param available The value to set, indicating whether a SWT-based GUI is available. {@code true} if a GUI is
     *     available, {@code false} otherwise.
     */
    public void setGuiAvailable(boolean available) {
        guiAvailable.set(available);
    }

    /**
     * Is an SWT-based GUI available?
     *
     * @return {@code true} if a GUI is available, {@code false} otherwise.
     */
    public boolean isGuiAvailable() {
        return guiAvailable.get();
    }

    /**
     * Checks whether an SWT-based GUI is available. Only succeeds if it is available, and throws a
     * {@link DependencyException} otherwise.
     *
     * @param functionality A text indicating the functionality that requires the GUI. It is incorporated as part of the
     *     following sentence: {@code "A GUI is required to [functionality]."}. For instance:
     *     {@code "show the option dialog"} or {@code "use SVG visualization"}.
     * @throws DependencyException If a GUI is not available.
     */
    public void checkGuiAvailable(String functionality) {
        if (isGuiAvailable()) {
            return;
        }
        String msg = fmt("A GUI is required to %s. Enable the GUI (\"--gui=on\" command line option), and make sure "
                + "to connect a display (e.g. on Linux enable X11 server/forwarding).", functionality);
        throw new DependencyException(msg);
    }
}
