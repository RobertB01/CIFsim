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

package org.eclipse.escet.common.app.framework.management;

import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Maps.mapc;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.escet.common.app.framework.Application;

/**
 * Manager that keeps track of running applications, and application status. The manager can post notifications for
 * status changes.
 */
public class AppManager {
    /** Data about all known applications. */
    private static final Map<Application<?>, AppManagerData> APPLICATIONS = mapc(32);

    /** Registered application status listeners. */
    private static final List<AppStatusListener> LISTENERS = listc(1);

    /** Constructor for the {@link AppManager} class. */
    private AppManager() {
        // Static class.
    }

    /**
     * Checks whether the given application exists (is known to the application manager). Should only be invoked by the
     * application framework itself.
     *
     * @param app The application to check.
     * @return {@code true} if the application exists, {@code false} otherwise.
     */
    public static synchronized boolean checkExists(Application<?> app) {
        return APPLICATIONS.containsKey(app);
    }

    /**
     * Adds a new application to the manager.
     *
     * @param app The application to add.
     * @param parent The parent of the application to add, or {@code null} if no parent.
     */
    public static synchronized void add(Application<?> app, Application<?> parent) {
        // Application should not yet exist.
        if (APPLICATIONS.containsKey(app)) {
            throw new RuntimeException("Duplicate application: " + app);
        }

        // Parent application, if any, should be known.
        if (parent != null && !APPLICATIONS.containsKey(parent)) {
            throw new RuntimeException("Unknown parent application: " + parent);
        }

        // No ancestry loops allowed.
        Application<?> ancestor = parent;
        while (ancestor != null) {
            if (ancestor == app) {
                throw new RuntimeException("App is own ancestor: " + app);
            }
            ancestor = getParent(ancestor);
        }

        // Update registration.
        AppManagerData data = new AppManagerData(app, parent);
        APPLICATIONS.put(app, data);

        // Notify listeners.
        notifyStatusListeners(data, true);
    }

    /**
     * Updates the status of an application.
     *
     * @param app The application.
     * @param status The new status.
     */
    public static synchronized void update(Application<?> app, AppStatus status) {
        // Get application data.
        AppManagerData data = APPLICATIONS.get(app);
        if (data == null) {
            if (status == AppStatus.TERMINATING) {
                // Ignore termination requests after application is no longer
                // known.
                return;
            }
            throw new RuntimeException("Unknown application: " + app);
        }

        // Update status. Ignore termination requests after application has
        // already terminated, either successfully or unsuccessfully.
        if (status == AppStatus.TERMINATING) {
            switch (data.status) {
                case FINISHED:
                case TERMINATED:
                case FAILED:
                case CRASHED:
                    // Ignore.
                    return;

                default:
                    break;
            }
        }
        data.status = status;

        // Notify listeners.
        notifyStatusListeners(data, false);
    }

    /**
     * Removes an application from the manager.
     *
     * @param app The application to remove.
     */
    public static synchronized void remove(Application<?> app) {
        // Application should exist.
        if (!APPLICATIONS.containsKey(app)) {
            throw new RuntimeException("Unknown application: " + app);
        }

        // Application should not have any descendants.
        for (Entry<Application<?>, AppManagerData> entry: APPLICATIONS.entrySet()) {
            if (entry.getValue().parent == app) {
                throw new RuntimeException("Can't remove application, child present: " + app);
            }
        }

        // Remove application.
        APPLICATIONS.remove(app);
    }

    /**
     * Terminates an application and all its descendants.
     *
     * @param appToTerminate The application to terminate.
     */
    public static synchronized void terminate(Application<?> appToTerminate) {
        // Initialize data structures.
        Deque<Application<?>> queue = new LinkedList<>();
        queue.push(appToTerminate);

        // Terminate application and its descendants.
        while (!queue.isEmpty()) {
            // Get next application, and terminate it.
            Application<?> app = queue.pop();
            app.terminate();

            // Add children to the queue. Since we don't have cycles, we must
            // not have encountered the children before. So there is no need
            // to keep track of the already terminated children, to reach a
            // fixed point.
            for (Entry<Application<?>, AppManagerData> entry: APPLICATIONS.entrySet()) {
                if (entry.getValue().parent == app) {
                    queue.push(entry.getKey());
                }
            }
        }
    }

    /** Terminates all applications, including all their descendants. */
    public static synchronized void terminateAll() {
        // Terminate all known applications. This terminates root applications,
        // as well as all their descendants.
        for (Application<?> app: APPLICATIONS.keySet()) {
            app.terminate();
        }
    }

    /**
     * Returns the parent application of the given application, or {@code null} if it has no parent.
     *
     * @param app The application.
     * @return The parent application or {@code null}.
     */
    private static synchronized Application<?> getParent(Application<?> app) {
        AppManagerData data = APPLICATIONS.get(app);
        if (data == null) {
            throw new RuntimeException("Unknown application: " + app);
        }
        return data.parent;
    }

    /**
     * Adds a status listener to the application manager.
     *
     * @param listener The status listener to add.
     * @param notifyCurrent Whether to notify about the current status of all currently registered applications
     *     ({@code true}), or not notify until anything changes ({@code false}). If requested, notifications will be
     *     sent for each application known to this manager, in order of addition to the manager.
     */
    public static synchronized void addStatusListener(AppStatusListener listener, boolean notifyCurrent) {
        // Add status listener.
        if (LISTENERS.contains(listener)) {
            throw new RuntimeException("Duplicate listener: " + listener);
        }
        LISTENERS.add(listener);

        // Notify about current status.
        if (notifyCurrent) {
            for (AppManagerData data: APPLICATIONS.values()) {
                listener.appStatusChanged(data.copy(), true);
            }
        }
    }

    /**
     * Removes a status listener from the application manager.
     *
     * @param listener The status listener to remove.
     */
    public static synchronized void removeStatusListener(AppStatusListener listener) {
        LISTENERS.remove(listener);
    }

    /**
     * Notifies all status listeners of a status change. Is also invoked for new applications (with status
     * {@link AppStatus#STARTING}).
     *
     * @param data The data about the application.
     * @param newApp Whether the status listener should consider this notification to be about a new application, about
     *     which it has not been previously notified.
     */
    private static synchronized void notifyStatusListeners(AppManagerData data, boolean newApp) {
        AppManagerData dataCopy = data.copy();
        for (AppStatusListener listener: LISTENERS) {
            listener.appStatusChanged(dataCopy, newApp);
        }
    }
}
