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

package org.eclipse.escet.common.app.framework;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.io.input.NullInputStream;
import org.eclipse.escet.common.app.framework.console.Console;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.io.FileAppStream;
import org.eclipse.escet.common.app.framework.io.NullAppStream;
import org.eclipse.escet.common.app.framework.management.AppManager;
import org.eclipse.escet.common.java.Assert;

/**
 * Class that can be used to start one application framework application (the child) from another application framework
 * application (the parent).
 */
public class ChildAppStarter {
    /** Constructor for the {@link ChildAppStarter} class. */
    private ChildAppStarter() {
        // Private constructor to turn class into static class.
    }

    /**
     * Executes an application framework application (the child) from the current application framework application (the
     * parent). The child application is executed in a new thread, reusing the parent application streams, working
     * directory, and console. If the child application finishes with a non-zero exit code, termination is requested for
     * the parent application.
     *
     * @param <T> The type (class) of the application to execute.
     * @param appClass The class of the application to execute as child application. The class must have a constructor
     *     with a single argument of type {@link AppStreams}.
     * @param args The arguments for the child application.
     * @return The exit code of the child application. See the application framework documentation for a description of
     *     the exit codes.
     * @throws RuntimeException If the constructor can not be found, or obtained; If an instance of the child
     *     application could not be constructed; If execution of the child application failed due to the child
     *     application thread being interrupted.
     * @throws InputOutputException If the stdout or stderr redirection file stream could not be opened.
     * @see #exec(Class, String[], String, String, boolean)
     */
    public static <T extends Application<?>> int exec(Class<T> appClass, String[] args) {
        return exec(appClass, args, "-", "-", "-", false, false, false, false);
    }

    /**
     * Executes an application framework application (the child) from the current application framework application (the
     * parent). The child application is executed in a new thread, reusing the parent application streams, working
     * directory, and console.
     *
     * @param <T> The type (class) of the application to execute.
     * @param appClass The class of the application to execute as child application. The class must have a constructor
     *     with a single argument of type {@link AppStreams}.
     * @param args The arguments for the child application.
     * @param stdoutPath The absolute local file system path to the file to redirect stdout to, or {@code null} to use
     *     the stdout stream of the parent application.
     * @param stderrPath The absolute local file system path to the file to redirect stderr to, or {@code null} to use
     *     the stderr stream of the parent application.
     * @param ignoreExitCode Whether to ignore non-zero exit codes. If {@code true}, non-zero exit code are ignored. If
     *     {@code false}, non-zero exit codes result in a termination request for the parent application.
     * @return The exit code of the child application. See the application framework documentation for a description of
     *     the exit codes.
     * @throws RuntimeException If the constructor can not be found, or obtained; If an instance of the child
     *     application could not be constructed; If execution of the child application failed due to the child
     *     application thread being interrupted.
     * @throws InputOutputException If the stdout or stderr redirection file stream could not be opened.
     */
    public static <T extends Application<?>> int exec(Class<T> appClass, String[] args, String stdoutPath,
            String stderrPath, boolean ignoreExitCode)
    {
        if (stdoutPath == null) {
            stdoutPath = "-";
        }
        if (stderrPath == null) {
            stderrPath = "-";
        }
        return exec(appClass, args, "-", stdoutPath, stderrPath, false, false, false, ignoreExitCode);
    }

    /**
     * Executes an application framework application (the child) from the current application framework application (the
     * parent). The child application is executed in a new thread, reusing the parent application streams, working
     * directory, and console. If the child application finishes with a non-zero exit code, termination is requested for
     * the parent application.
     *
     * @param <T> The type (class) of the application to execute.
     * @param appClass The class of the application to execute as child application. The class must have a constructor
     *     with a single argument of type {@link AppStreams}.
     * @param args The arguments for the child application.
     * @param stdinPath Specify whether to have a standard input (stdin) stream and where the input comes from. Use
     *     {@code ""} to not have a stdin stream, {@code "-"} to use the stdin stream of the ToolDef interpreter, or
     *     otherwise an absolute or relative local file system path of the file from which to read the standard input.
     *     May contain both {@code "\"} and {@code "/"} as file separators.
     * @param stdoutPath Specify whether to have a standard output (stdout) stream and where to write the standard
     *     output. Use {@code ""} to not have a stdout stream, {@code "-"} to use the stdout stream of the ToolDef
     *     interpreter, or otherwise an absolute or relative local file system path of the file to which to write the
     *     standard output. May contain both {@code "\"} and {@code "/"} as file separators.
     * @param stderrPath Specify whether to have a standard error (stderr) stream and where to write the standard error
     *     output. Use {@code ""} to not have a stderr stream, {@code "-"} to use the stderr stream of the ToolDef
     *     interpreter, or otherwise an absolute or relative local file system path of the file to which to write the
     *     standard error output. May contain both {@code "\"} and {@code "/"} as file separators. Is ignored when the
     *     standard error stream is redirected to the standard output stream.
     * @param appendOut Whether to append to the stdout file ({@code true}) or overwrite it ({@code false}). Is ignored
     *     when standard output is not written to a file.
     * @param appendErr Whether to append to the stderr file ({@code true}) or overwrite it ({@code false}). Is ignored
     *     if standard error output is not written to a file. Is also ignored when standard error stream is redirected
     *     to the standard output stream.
     * @param errToOut Whether to redirect the standard error stream to the standard output stream ({@code true}) or use
     *     separate stream ({@code false}).
     * @param ignoreExitCode Whether to ignore non-zero exit codes. If {@code true}, non-zero exit code are ignored. If
     *     {@code false}, non-zero exit codes result in a termination request for the parent application.
     * @return The exit code of the child application. See the application framework documentation for a description of
     *     the exit codes.
     * @throws RuntimeException If the constructor can not be found, or obtained; If an instance of the child
     *     application could not be constructed; If execution of the child application failed due to the child
     *     application thread being interrupted.
     * @throws InputOutputException If the use of a stdin/stdout/stderr stream, that is redirected to a file, causes an
     *     I/O error.
     */
    @SuppressWarnings("resource")
    public static <T extends Application<?>> int exec(Class<T> appClass, final String[] args, String stdinPath,
            String stdoutPath, String stderrPath, boolean appendOut, boolean appendErr, boolean errToOut,
            boolean ignoreExitCode)
    {
        // Find constructor.
        final Constructor<T> appConstructor;
        try {
            appConstructor = appClass.getConstructor(AppStreams.class);
        } catch (SecurityException ex) {
            String msg = fmt("Application framework application execution failed, due to security settings prohibiting "
                    + "obtaining of a constructor for the \"%s\" class.", appClass.getName());
            throw new RuntimeException(msg, ex);
        } catch (NoSuchMethodException ex) {
            String msg = fmt("Application framework application execution "
                    + "failed, due the \"%s\" class missing a proper constructor.", appClass.getName());
            throw new RuntimeException(msg, ex);
        }

        // Prepare child application environment.
        AppEnvData parentAppEnvData = AppEnv.getData();
        final String parentWorkDir = Paths.getCurWorkingDir();
        final Console console = parentAppEnvData.getConsole();

        // Prepare child application streams.
        AppStreams parentStreams = parentAppEnvData.getStreams();
        InputStream inStream;
        AppStream outStream;
        AppStream errStream;

        if (stdinPath.isEmpty()) {
            inStream = new NullInputStream(0);
        } else if (stdinPath.equals("-")) {
            inStream = parentStreams.inStream;
        } else {
            String absStdinPath = Paths.resolve(stdinPath);
            try {
                inStream = new FileInputStream(absStdinPath);
            } catch (FileNotFoundException ex) {
                String msg = fmt(
                        "Failed to open file \"%s\" for reading, as it can not be found, is a directory "
                                + "rather than a file, or for some other reason can not be opened for reading.",
                        stdinPath);
                throw new InputOutputException(msg, ex);
            }
            inStream = new BufferedInputStream(inStream);
        }

        if (stdoutPath.isEmpty()) {
            outStream = new NullAppStream();
        } else if (stdoutPath.equals("-")) {
            outStream = parentStreams.out;
        } else {
            outStream = new FileAppStream(stdoutPath, appendOut);
        }

        if (errToOut) {
            errStream = outStream;
        } else if (stderrPath.isEmpty()) {
            errStream = new NullAppStream();
        } else if (stderrPath.equals("-")) {
            errStream = parentStreams.err;
        } else {
            errStream = new FileAppStream(stderrPath, appendErr);
        }

        final AppStreams childStreams;
        childStreams = new AppStreams(inStream, outStream, errStream);

        // Execute the child application, in a new thread.
        final AtomicReference<Application<?>> app = new AtomicReference<>();
        final AtomicReference<String> msg = new AtomicReference<>();
        final AtomicReference<Exception> ex = new AtomicReference<>();
        final AtomicReference<Integer> exitCode = new AtomicReference<>();
        final AtomicReference<Boolean> terminated = new AtomicReference<>();

        Runnable childRunnable = new Runnable() {
            @Override
            public void run() {
                // Create new application.
                try {
                    app.set(appConstructor.newInstance(childStreams));
                } catch (IllegalArgumentException e) {
                    // Should never happen, as we asked for this type of
                    // constructor.
                    msg.set("Application framework application execution failed, due to invalid constructor "
                            + "parameters.");
                    ex.set(e);
                } catch (InstantiationException e) {
                    msg.set("Application framework application execution failed, due to a failure to instantiate the "
                            + "application class. Is the class abstract?");
                    ex.set(e);
                } catch (IllegalAccessException e) {
                    msg.set("Application framework application execution failed, due to the constructor being "
                            + "inaccessible.");
                    ex.set(e);
                } catch (InvocationTargetException e) {
                    msg.set("Application framework application execution failed, due to an uncaught exception "
                            + "of the application class constructor.");
                    ex.set(e);
                }

                if (app.get() == null) {
                    return;
                }

                // Set current working directory for child application.
                Paths.setCurWorkingDir(parentWorkDir);

                // Get application environment data for child application,
                // while we still can.
                AppEnvData childAppEnvData = AppEnv.getData();

                // Switch console to child application.
                if (console != null) {
                    console.setApplication(app.get());
                }

                // Add application to application manager.
                AppManager.add(app.get(), parentAppEnvData.getApplication());

                // Terminate child if parent has received termination request
                // since the start of this method. Such termination request
                // was not yet forwarded to this child, as the child
                // application was not yet registered. Now that it is
                // registered, inform the child of the termination.
                if (parentAppEnvData.isTerminationRequested()) {
                    // We explicitly send a termination request to the
                    // application, so that status change listeners are
                    // properly notified.
                    app.get().terminate();
                }

                // Run child application, with given arguments. We want to run
                // the application, even if termination is already requested,
                // to ensure the application properly cleans up after itself.
                // In such cases, only application framework code will run, no
                // user code.
                int appExitCode = app.get().run(args, false);

                // Remove application from the application manager.
                AppManager.remove(app.get());
                app.set(null);

                // Store results.
                exitCode.set(appExitCode);
                terminated.set(childAppEnvData.isTerminationRequested());
            }
        };

        Thread t = new Thread(childRunnable, appClass.getName());
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            msg.set("Application framework application execution failed, due to its thread being interrupted.");
            ex.set(e);
        }

        // Remove application from application manager, if not yet done.
        if (app.get() != null) {
            AppManager.remove(app.get());
            app.set(null);
        }

        // Check for crash in the application framework application, not caught
        // by the exception handling framework of the application framework
        // application. This happens for instance due to crashes in the
        // exception handling of application framework itself.
        if (exitCode.get() == null) {
            throw new RuntimeException("Application framework application crashed. Crash not properly handled by "
                    + "crash handling of the application framework application.");
        }

        // Restore application/console coupling for the parent application.
        if (console != null) {
            console.setApplication(parentAppEnvData.getApplication());
        }

        // Flush and close standard streams that are redirected to a file.
        if (!stdinPath.isEmpty() && !stdinPath.equals("-")) {
            try {
                inStream.close();
            } catch (IOException ex2) {
                String msg2 = fmt("Failed to close file \"%s\".", stdinPath);
                throw new InputOutputException(msg2, ex2);
            }
        }

        if (!stdoutPath.isEmpty() && !stdoutPath.equals("-")) {
            outStream.flush();
            outStream.close();
        }

        if (!errToOut && !stderrPath.isEmpty() && !stderrPath.equals("-")) {
            errStream.flush();
            errStream.close();
        }

        // Forward child application termination to the parent.
        if (terminated.get() != null && terminated.get()) {
            AppEnv.terminate();
        }

        // Forward fatal exceptions.
        if (ex.get() != null) {
            throw new RuntimeException(msg.get(), ex.get());
        }

        // Forward child application failure to parent termination, unless we
        // indicated we want to ignore non-zero exit codes.
        Assert.notNull(exitCode.get());
        if (!ignoreExitCode && exitCode.get() != 0) {
            parentAppEnvData.terminate();
        }

        // Return the exit code.
        return exitCode.get();
    }
}
