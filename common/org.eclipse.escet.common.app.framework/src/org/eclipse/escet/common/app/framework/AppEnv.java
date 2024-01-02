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

import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.options.DevModeOption;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputMode;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.app.framework.output.StreamOutputComponent;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.DependencyException;

/**
 * Application environment manager. It manages application environment data, as provided by the {@link AppEnvData}
 * class, per application, and per application thread. This allow for multiple applications to have unique instances of
 * such objects, which allows multiple applications to run within a single process (Java interpreter), and thus also
 * within a single Eclipse environment.
 */
public final class AppEnv {
    /**
     * Per thread application environment data. Don't access this field directly. Use the {@link #getData()} method
     * instead.
     */
    private static ThreadLocal<AppEnvData> data = new ThreadLocal<>();

    /** Constructor for the {@link AppEnv} class. */
    private AppEnv() {
        // Static class.
    }

    /**
     * Is the current/calling thread registered with the application framework?
     *
     * @return {@code true} if the current/calling thread is registered with the application framework, {@code false}
     *     otherwise.
     */
    public static boolean isRegistered() {
        AppEnvData rslt = data.get();
        return rslt != null;
    }

    /**
     * Returns the application environment data for the current application (thread).
     *
     * @return The application environment data for the current application (thread).
     */
    public static AppEnvData getData() {
        AppEnvData rslt = data.get();
        if (rslt == null) {
            throw new RuntimeException("Current thread is not registered with the application framework. "
                    + "Register an application, or register the current thread for an already registered application. "
                    + "See the application framework documentation for more information.");
        }
        return rslt;
    }

    /**
     * Returns the application streams for the current application (thread).
     *
     * @return The application streams for the current application (thread).
     */
    public static AppStreams getStreams() {
        return getData().getStreams();
    }

    /**
     * Returns the application output provider for the current application (thread).
     *
     * @param <T> The application's output interface.
     * @return The application output provider for the current application (thread).
     */
    @SuppressWarnings("unchecked")
    public static <T extends IOutputComponent> OutputProvider<T> getProvider() {
        return (OutputProvider<T>)getData().getProvider();
    }

    /**
     * Returns the application options for the current application (thread).
     *
     * @return The application options for the current application (thread).
     */
    public static Options getOptions() {
        return getData().getOptions();
    }

    /**
     * Returns the current application (thread).
     *
     * @return The current application (thread).
     */
    public static Application<?> getApplication() {
        return getData().getApplication();
    }

    /**
     * Returns the application properties for the current application (thread).
     *
     * @return The application properties for the current application (thread).
     */
    public static AppProperties getProperties() {
        return getData().getProperties();
    }

    /**
     * Searches for the property with the specified name in the application properties for the current application
     * (thread). If the property is not found in that property list, the default property list, and its defaults,
     * recursively, are then checked. The method returns null if the property is not found.
     *
     * @param propertyName The name of the property to search for.
     * @return The value of property, or {@code null}.
     *
     * @see #getProperties
     * @see AppProperties#get(String)
     */
    public static String getProperty(String propertyName) {
        return getProperties().get(propertyName);
    }

    /**
     * Searches for the property with the specified name in the application properties for the current application
     * (thread). If the property is not found in that property list, the default property list, and its defaults,
     * recursively, are then checked. The method returns the default value argument if the property is not found.
     *
     * @param propertyName The name of the property to search for.
     * @param defaultValue The default value to return if the property is not found.
     * @return The value of property, or the default value if the property is not found.
     *
     * @see #getProperties
     * @see AppProperties#get(String, String)
     */
    public static String getProperty(String propertyName, String defaultValue) {
        return getProperties().get(propertyName, defaultValue);
    }

    /**
     * Sets the property with the specified name to the given value in the application properties for the current
     * application (thread).
     *
     * @param propertyName The name of the property to set.
     * @param value The new value of the property.
     * @return The previous value of the specified property, or {@code null} if it did not have one.
     *
     * @see AppProperties#set
     * @see #getProperties
     */
    public static Object setProperty(String propertyName, String value) {
        return getProperties().set(propertyName, value);
    }

    /**
     * Is an SWT-based GUI available for the current application (thread)?
     *
     * @return {@code true} if a GUI is available, {@code false} otherwise.
     */
    public static boolean isGuiAvailable() {
        return getData().isGuiAvailable();
    }

    /**
     * Checks whether an SWT-based GUI is available for the current application (thread). Only succeeds if it is
     * available, and throws a {@link DependencyException} otherwise.
     *
     * @param functionality A text indicating the functionality that requires the GUI. It is incorporated as part of the
     *     following sentence: {@code "A GUI is required to [functionality]."}. For instance:
     *     {@code "show the option dialog"} or {@code "use SVG visualization"}.
     * @throws DependencyException If a GUI is not available.
     */
    public static void checkGuiAvailable(String functionality) {
        getData().checkGuiAvailable(functionality);
    }

    /**
     * Returns a value indicating whether termination of the application is requested.
     *
     * @return {@code true} if termination is requested, {@code false} otherwise.
     */
    public static boolean isTerminationRequested() {
        return getData().isTerminationRequested();
    }

    /** Requests termination of the application. */
    public static void terminate() {
        getData().terminate();
    }

    /**
     * Registers the application with the application environment manager. This method must be called from the
     * application's main thread, and in general should only be called by the application framework itself.
     *
     * @param appData The application's environment data.
     */
    public static void registerApplication(AppEnvData appData) {
        Assert.notNull(appData);
        if (data.get() != null) {
            String msg = "Can't register an application on a thread that already has a registered application.";
            throw new IllegalStateException(msg);
        }
        data.set(appData);
    }

    /**
     * Unregisters the application with the application environment manager. This method must be called from the
     * application's main thread, and in general should only be called by the application framework itself.
     */
    public static void unregisterApplication() {
        getData(); // Make sure application is registered.
        data.set(null);
    }

    /**
     * Registers the current thread with the application environment manager. This method must be called from the thread
     * that is to be registered.
     *
     * @param appData The application's environment data.
     * @param allowReRegister Whether re-registration is allowed ({@code true}), or not ({@code false}).
     */
    public static void registerThread(AppEnvData appData, boolean allowReRegister) {
        Assert.notNull(appData);
        if (!allowReRegister && data.get() != null) {
            String msg = "Can't register an already registered thread with the application framework.";
            throw new IllegalStateException(msg);
        }
        data.set(appData);
    }

    /**
     * Unregisters the current thread with the application environment manager. This method must be called from the
     * thread that is to be unregistered.
     */
    public static void unregisterThread() {
        getData(); // Make sure thread is registered.
        data.set(null);
    }

    /**
     * Registers the current thread using a {@link AppEnvData default application environment}, without an actual
     * application, registers a default stream output provider, sets the output mode to {@link OutputMode#WARNING
     * warning}, and disables development mode. This is particularly useful for unit testing.
     */
    public static void registerSimple() {
        AppEnv.registerApplication(new AppEnvData(null));
        OutputProvider.register(new StreamOutputComponent());
        Options.set(OutputModeOption.class, OutputMode.WARNING);
        Options.set(DevModeOption.class, false);
    }
}
