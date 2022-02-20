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

package org.eclipse.escet.common.app.framework.output;

import static org.eclipse.escet.common.java.Lists.reverse;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;

/**
 * Application specific output provider. Provides the output to the output components registered with this provider.
 *
 * <p>
 * Instances of this class, and derived classes, should generally only be created from the
 * {@link Application#getProvider} method.
 * </p>
 *
 * @param <T> The application's output interface.
 */
public class OutputProvider<T extends IOutputComponent> {
    /** The output components to send output to. */
    protected final List<T> components = new CopyOnWriteArrayList<>();

    /** The current indentation level for debug output. */
    protected int dbgLevel = 0;

    /** The current indentation level for 'normal' output. */
    protected int outLevel = 0;

    /** The number of warnings so far. */
    protected int warningCount = 0;

    /**
     * Returns the number of output components registered with this provider.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @return The number of output components registered with this provider.
     */
    protected int getComponentCountInternal() {
        return components.size();
    }

    /**
     * Returns the number of output components registered with the application's provider.
     *
     * @return The number of output components registered with the application's provider.
     */
    public static int getComponentCount() {
        return AppEnv.getProvider().getComponentCountInternal();
    }

    /**
     * Registers an output component with this provider, and initializes the output component.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @param component The output component to register.
     * @param append Whether to append the new output component to the list of already registered output components
     *     ({@code true}) or prepend it ({@code false}).
     * @throws IllegalArgumentException If the output component is already registered with this provider.
     */
    protected void registerInternal(T component, boolean append) {
        if (components.contains(component)) {
            throw new IllegalArgumentException("Output component already registered.");
        }

        if (append) {
            components.add(component);
        } else {
            components.add(0, component);
        }

        component.initialize();
    }

    /**
     * Registers an output component with the application's provider, and initializes the output component. The new
     * output component is appended to the list of already registered output components.
     *
     * @param component The output component to register.
     * @throws IllegalArgumentException If the output component is already registered with the application's provider.
     */
    public static void register(IOutputComponent component) {
        register(component, true);
    }

    /**
     * Registers an output component with the application's provider, and initializes the output component.
     *
     * @param component The output component to register.
     * @param append Whether to append the new output component to the list of already registered output components
     *     ({@code true}) or prepend it ({@code false}).
     * @throws IllegalArgumentException If the output component is already registered with the application's provider.
     */
    public static void register(IOutputComponent component, boolean append) {
        AppEnv.getProvider().registerInternal(component, append);
    }

    /**
     * Unregisters an output component with this provider. Note that if an event is currently being forwarded to the
     * output providers, the unregistered component may still be notified of the event.
     *
     * <p>
     * The component is also asked to perform {@link IOutputComponent#cleanup cleanup}, as the application framework can
     * no longer request this, now that the component is no longer registered. As such, this method must always be
     * called from within the application framework, to properly handle potential exceptions.
     * </p>
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @param component The output component to unregister.
     * @throws IllegalArgumentException If the output component is not registered with this provider.
     */
    protected void unregisterInternal(T component) {
        // Remove the component from the list of output components.
        boolean removed = components.remove(component);
        if (!removed) {
            throw new IllegalArgumentException("Output component not registered.");
        }

        // Perform cleanup of the unregistered component, as the application
        // framework can no longer do that. Note that cleanup should be
        // allowed, even if it is already in progress.
        component.cleanup();
    }

    /**
     * Unregisters an output component with the application's provider. Note that if an event is currently being
     * forwarded to the output providers, the unregistered component may still be notified of the event.
     *
     * <p>
     * The component is also asked to perform {@link IOutputComponent#cleanup cleanup}, as the application framework can
     * no longer request this, now that the component is no longer registered. As such, this method must always be
     * called from within the application framework, to properly handle potential exceptions.
     * </p>
     *
     * @param component The output component to unregister.
     * @throws IllegalArgumentException If the output component is not registered with the application's provider.
     */
    public static void unregister(IOutputComponent component) {
        AppEnv.getProvider().unregisterInternal(component);
    }

    /**
     * Resets the current indentation levels for debug and 'normal' output. This method should be used as little as
     * possible. If possible, the {@link #idbg}, {@link #ddbg}, {@link #iout}, and {@link #dout} methods should be used
     * instead.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     */
    protected void resetLevelsInternal() {
        dbgLevel = 0;
        outLevel = 0;
    }

    /**
     * Resets the current indentation levels for debug and 'normal' output. This method should be used as little as
     * possible. If possible, the {@link #idbg}, {@link #ddbg}, {@link #iout}, and {@link #dout} methods should be used
     * instead.
     */
    public static void resetLevels() {
        AppEnv.getProvider().resetLevelsInternal();
    }

    /**
     * Returns the current indentation level for debug output. Using this method should be avoided, if at all possible.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @return The current indentation level for debug output.
     */
    protected int getDbgLevelInternal() {
        return dbgLevel;
    }

    /**
     * Returns the current indentation level for debug output. Using this method should be avoided, if at all possible.
     *
     * @return The current indentation level for debug output.
     */
    public static int getDbgLevel() {
        return AppEnv.getProvider().getDbgLevelInternal();
    }

    /**
     * Returns the current indentation level for 'normal' output. Using this method should be avoided, if at all
     * possible.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @return The current indentation level for 'normal' output.
     */
    protected int getOutLevelInternal() {
        return outLevel;
    }

    /**
     * Returns the current indentation level for 'normal' output. Using this method should be avoided, if at all
     * possible.
     *
     * @return The current indentation level for 'normal' output.
     */
    public static int getOutLevel() {
        return AppEnv.getProvider().getOutLevelInternal();
    }

    /**
     * Resets the warning count.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     */
    protected void resetWarningCountInternal() {
        warningCount = 0;
    }

    /** Resets the warning count. */
    public static void resetWarningCount() {
        AppEnv.getProvider().resetWarningCountInternal();
    }

    /**
     * Returns the warning count.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @return The warning count.
     */
    protected int getWarningCountInternal() {
        return warningCount;
    }

    /**
     * Returns the warning count.
     *
     * @return The warning count.
     */
    public static int getWarningCount() {
        return AppEnv.getProvider().getWarningCountInternal();
    }

    /**
     * Returns a value indicating whether debug output is enabled in the current output mode.
     *
     * @return A value indicating whether debug output is enabled in the current output mode.
     */
    public static boolean dodbg() {
        return OutputModeOption.getOutputMode() == OutputMode.DEBUG;
    }

    /**
     * Increases the current indentation level for debug output by one, if debug output is enabled in the current output
     * mode.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     */
    protected void idbgInternal() {
        if (dodbg()) {
            dbgLevel++;
        }
    }

    /**
     * Increases the current indentation level for debug output by one, if debug output is enabled in the current output
     * mode.
     */
    public static void idbg() {
        AppEnv.getProvider().idbgInternal();
    }

    /**
     * Decreases the current indentation level for debug output by one, if debug output is enabled in the current output
     * mode.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @throws AssertionError If the current indentation level for debug output is zero.
     */
    protected void ddbgInternal() {
        if (dodbg()) {
            Assert.check(dbgLevel > 0);
            dbgLevel--;
        }
    }

    /**
     * Decreases the current indentation level for debug output by one, if debug output is enabled in the current output
     * mode.
     *
     * @throws AssertionError If the current indentation level for debug output is zero.
     */
    public static void ddbg() {
        AppEnv.getProvider().ddbgInternal();
    }

    /**
     * Forward the given debug output to the registered output components, if debug output is enabled in the current
     * output mode.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @param msg The debug output to forward.
     */
    protected void dbgInternal(String msg) {
        if (!dodbg()) {
            return;
        }
        for (T component: components) {
            component.dbg(msg, dbgLevel + outLevel);
        }
    }

    /**
     * Forward the given debug output to the registered output components, if debug output is enabled in the current
     * output mode.
     *
     * @param msg The debug output to forward.
     */
    public static void dbg(String msg) {
        AppEnv.getProvider().dbgInternal(msg);
    }

    /**
     * Forward the given debug output to the registered output components, if debug output is enabled in the current
     * output mode. This method uses {@link Strings#fmt} to obtain the real output.
     *
     * <p>
     * For performance reasons, it may be better to call this method like this:
     *
     * <pre>if (dodbg()) dbg(...)</pre>
     *
     * This avoids evaluating the arguments if debugging is disabled.
     * </p>
     *
     * @param msg The debug output (pattern) to forward.
     * @param args The arguments of the debug output pattern.
     */
    public static void dbg(String msg, Object... args) {
        dbg(fmt(msg, args));
    }

    /**
     * Forward an empty message as debug output to the registered output components, if debug output is enabled in the
     * current output mode.
     */
    public static void dbg() {
        dbg("");
    }

    /**
     * Returns a value indicating whether 'normal' output is enabled in the current output mode.
     *
     * @return A value indicating whether 'normal' output is enabled in the current output mode.
     */
    public static boolean doout() {
        OutputMode mode = OutputModeOption.getOutputMode();
        return mode == OutputMode.NORMAL || mode == OutputMode.DEBUG;
    }

    /**
     * Increases the current indentation level for 'normal' output by one, if 'normal' output is enabled in the current
     * output mode.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     */
    protected void ioutInternal() {
        if (doout()) {
            outLevel++;
        }
    }

    /**
     * Increases the current indentation level for 'normal' output by one, if 'normal' output is enabled in the current
     * output mode.
     */
    public static void iout() {
        AppEnv.getProvider().ioutInternal();
    }

    /**
     * Decreases the current indentation level for 'normal' output by one, if 'normal' output is enabled in the current
     * output mode.
     *
     * @throws AssertionError If the current indentation level for 'normal' output is zero.
     */
    protected void doutInternal() {
        if (doout()) {
            Assert.check(outLevel > 0);
            outLevel--;
        }
    }

    /**
     * Decreases the current indentation level for 'normal' output by one, if 'normal' output is enabled in the current
     * output mode.
     *
     * @throws AssertionError If the current indentation level for 'normal' output is zero.
     */
    public static void dout() {
        AppEnv.getProvider().doutInternal();
    }

    /**
     * Forward the given 'normal' output to the registered output components, if 'normal' output is enabled in the
     * current output mode.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @param msg The 'normal' output to forward.
     */
    protected void outInternal(String msg) {
        if (!doout()) {
            return;
        }
        for (T component: components) {
            component.out(msg, dbgLevel + outLevel);
        }
    }

    /**
     * Forward the given 'normal' output to the registered output components, if 'normal' output is enabled in the
     * current output mode.
     *
     * @param msg The 'normal' output to forward.
     */
    public static void out(String msg) {
        AppEnv.getProvider().outInternal(msg);
    }

    /**
     * Forward the given 'normal' output to the registered output components, if 'normal' output is enabled in the
     * current output mode. This method uses {@link Strings#fmt} to obtain the real output.
     *
     * <p>
     * For performance reasons, it may be better to call this method like this:
     *
     * <pre>if (doout()) out(...)</pre>
     *
     * This avoids evaluating the arguments if 'normal' output is disabled.
     * </p>
     *
     * @param msg The 'normal' (pattern) to forward.
     * @param args The arguments of the 'normal' output pattern.
     */
    public static void out(String msg, Object... args) {
        out(fmt(msg, args));
    }

    /**
     * Forward an empty message as 'normal' output to the registered output components, if 'normal' output is enabled in
     * the current output mode.
     */
    public static void out() {
        out("");
    }

    /**
     * {@link Strings#wrap Wrap} and forward the given 'normal' output to the registered output components, if 'normal'
     * output is enabled in the current output mode. This method uses {@link Strings#fmt} to obtain the string to wrap.
     *
     * <p>
     * For performance reasons, it may be better to call this method like this:
     *
     * <pre>if (doout()) outw(...)</pre>
     *
     * This avoids evaluating the arguments if 'normal' output is disabled.
     * </p>
     *
     * @param msg The 'normal' output (pattern) to wrap and forward.
     * @param args The arguments of the 'normal' output pattern.
     */
    public static void outw(String msg, Object... args) {
        for (String txt: Strings.wrap(fmt(msg, args))) {
            out(txt);
        }
    }

    /**
     * Returns a value indicating whether warning output is enabled in the current output mode.
     *
     * @return A value indicating whether warning output is enabled in the current output mode.
     */
    public static boolean dowarn() {
        return OutputModeOption.getOutputMode() != OutputMode.ERROR;
    }

    /**
     * Forward the given warning output to the registered output components, if warning output is enabled in the current
     * output mode.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * <p>
     * The default stream output component automatically prefixes warning messages with {@code "WARNING: "}.
     * </p>
     *
     * @param msg The warning output to forward.
     */
    protected void warnInternal(String msg) {
        if (!dowarn()) {
            return;
        }
        warningCount++;
        for (T component: components) {
            component.warn(msg, dbgLevel + outLevel);
        }
    }

    /**
     * Forward the given warning output to the registered output components, if warning output is enabled in the current
     * output mode.
     *
     * <p>
     * The default stream output component automatically prefixes warning messages with {@code "WARNING: "}.
     * </p>
     *
     * @param msg The warning output to forward.
     */
    public static void warn(String msg) {
        AppEnv.getProvider().warnInternal(msg);
    }

    /**
     * Forward the given warning output to the registered output components, if warning output is enabled in the current
     * output mode. This method uses {@link Strings#fmt} to obtain the real output.
     *
     * <p>
     * For performance reasons, it may be better to call this method like this:
     *
     * <pre>if (dowarn()) warn(...)</pre>
     *
     * This avoids evaluating the arguments if warning output is disabled.
     * </p>
     *
     * <p>
     * The default stream output component automatically prefixes warning messages with {@code "WARNING: "}.
     * </p>
     *
     * @param msg The warning output (pattern) to forward.
     * @param args The arguments of the warning output pattern.
     */
    public static void warn(String msg, Object... args) {
        warn(fmt(msg, args));
    }

    /**
     * Forward an empty message as warning output to the registered output components, if warning output is enabled in
     * the current output mode.
     *
     * <p>
     * The default stream output component automatically prefixes warning messages with {@code "WARNING: "}.
     * </p>
     */
    public static void warn() {
        warn("");
    }

    /**
     * Forward the given fatal error message to the registered output components.
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * <p>
     * For a consistent user experience, it is recommended to always use {@code "ERROR: "} as a prefix for error
     * messages. The default stream output component does <em>not</em> automatically prefix error messages.
     * </p>
     *
     * @param msg The fatal error message to forward.
     */
    protected void errInternal(String msg) {
        for (T component: components) {
            component.err(msg);
        }
    }

    /**
     * Forward the given fatal error message to the registered output components.
     *
     * <p>
     * This method should generally not be invoked directly. If an uncaught exception is caught by the exception
     * framework, the exception framework automatically invokes this method to print the error message. However, in
     * certain cases, for instance if multiple error messages should be printed, it may be useful to directly invoke
     * this method.
     * </p>
     *
     * <p>
     * For a consistent user experience, it is recommended to always use {@code "ERROR: "} as a prefix for error
     * messages. The default stream output component does <em>not</em> automatically prefix error messages.
     * </p>
     *
     * @param msg The fatal error message to forward.
     */
    public static void err(String msg) {
        AppEnv.getProvider().errInternal(msg);
    }

    /**
     * Forward the given fatal error message to the registered output components.
     *
     * <p>
     * This method should generally not be invoked directly. If an uncaught exception is caught by the exception
     * framework, the exception framework automatically invokes this method to print the error message. However, in
     * certain cases, for instance if multiple error messages should be printed, it may be useful to directly invoke
     * this method.
     * </p>
     *
     * <p>
     * For a consistent user experience, it is recommended to always use {@code "ERROR: "} as a prefix for error
     * messages. The default stream output component does <em>not</em> automatically prefix error messages.
     * </p>
     *
     * @param msg The fatal error message (pattern) to forward.
     * @param args The arguments of the fatal error message output pattern.
     */
    public static void err(String msg, Object... args) {
        err(fmt(msg, args));
    }

    /**
     * Just before termination of the application, all output components will be asked to cleanup after themselves. They
     * should release all their resources.
     *
     * <p>
     * It is allowed to ask to perform cleanup multiple times, as output components should be able to handle such
     * duplicate requests.
     * </p>
     *
     * <p>
     * This method should not be invoked directly. It is invoked by its static variant, when appropriate.
     * </p>
     *
     * @see IOutputComponent#cleanup
     */
    protected void cleanupInternal() {
        // Output components are cleaned up in reverse order of registration,
        // to ensure the stream output component (which is registered first)
        // is cleaned up last. The 'reverse order' assumes that all output
        // components were appended on registration, rather than prepended.
        for (T component: reverse(components)) {
            component.cleanup();
        }
    }

    /**
     * Just before termination of the application, all output components will be asked to cleanup after themselves. They
     * should release all their resources.
     *
     * <p>
     * It is allowed to ask to perform cleanup multiple times, as output components should be able to handle such
     * duplicate requests.
     * </p>
     *
     * <p>
     * This method should not be invoked directly. When appropriate, it is automatically invoked by the application
     * framework.
     * </p>
     *
     * @see IOutputComponent#cleanup
     */
    public static void cleanup() {
        AppEnv.getProvider().cleanupInternal();
    }
}
