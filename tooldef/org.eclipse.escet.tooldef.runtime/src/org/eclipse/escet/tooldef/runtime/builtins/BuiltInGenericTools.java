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

package org.eclipse.escet.tooldef.runtime.builtins;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.ChildAppStarter;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.PlatformUtils;
import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.CommandLineUtils;
import org.eclipse.escet.common.java.UncachedUrlClassLoader.OpenUrlException;
import org.eclipse.escet.tooldef.common.ClassLoaderObtainer;
import org.eclipse.escet.tooldef.runtime.ExitException;
import org.eclipse.escet.tooldef.runtime.ToolDefException;
import org.osgi.framework.Bundle;

/** ToolDef built-in generic tools. */
public class BuiltInGenericTools {
    /** Constructor for the {@link BuiltInGenericTools} class. */
    private BuiltInGenericTools() {
        // Static class.
    }

    /**
     * Executes an application framework application.
     *
     * <p>
     * The application is specified as a Java class that extends the {@link Application} class. It should have a
     * constructor that accepts one parameter, of type {@link AppStreams}.
     * </p>
     *
     * <p>
     * The application is started within the current Java interpreter, in a new thread. If executed from Eclipse, the
     * class is loaded using the class loader of the given plug-in (if provided), otherwise, it is loaded using the
     * default class loader. In the latter case, the class should be in the class path.
     * </p>
     *
     * <p>
     * This tool is only machine independent and platform independent, as long as the applications that are executed,
     * are machine independent and platform independent as well.
     * </p>
     *
     * @param plugin The name of the plug-in (OSGi bundle) in which to resolve the application class. If the Java
     *     application is executed from Eclipse, then the class is loaded by the class loader of this plug-in, otherwise
     *     it is resolved using the default class loader. May be {@code null} to always use the default class loader.
     * @param classname The absolute name of the Java class to execute as application.
     * @param args The command line arguments of the application to execute. Each argument is parsed using
     *     {@link CommandLineUtils#parseArgs} to zero or more actual arguments.
     * @param stdin Specify whether to have a standard input (stdin) stream and where the input comes from. Use
     *     {@code ""} to not have a stdin stream, {@code "-"} to use the stdin stream of the ToolDef interpreter, or
     *     otherwise an absolute or relative local file system path of the file from which to read the standard input.
     *     May contain both {@code "\"} and {@code "/"} as path separators.
     * @param stdout Specify whether to have a standard output (stdout) stream and where to write the standard output.
     *     Use {@code ""} to not have a stdout stream, {@code "-"} to use the stdout stream of the ToolDef interpreter,
     *     or otherwise an absolute or relative local file system path of the file to which to write the standard
     *     output. May contain both {@code "\"} and {@code "/"} as path separators.
     * @param stderr Specify whether to have a standard error (stderr) stream and where to write the standard error
     *     output. Use {@code ""} to not have a stderr stream, {@code "-"} to use the stderr stream of the ToolDef
     *     interpreter, or otherwise an absolute or relative local file system path of the file to which to write the
     *     standard error output. May contain both {@code "\"} and {@code "/"} as path separators. Is ignored when the
     *     standard error stream is redirected to the standard output stream.
     * @param appendOut Whether to append to the stdout file ({@code true}) or overwrite it ({@code false}). Is ignored
     *     when standard output is not written to a file.
     * @param appendErr Whether to append to the stderr file ({@code true}) or overwrite it ({@code false}). Is ignored
     *     if standard error output is not written to a file. Is also ignored when standard error stream is redirected
     *     to the standard output stream.
     * @param errToOut Whether to redirect the standard error stream to the standard output stream ({@code true}) or use
     *     separate streams ({@code false}).
     * @param ignoreNonZeroExitCode Whether to ignore non-zero exit codes ({@code true}) or consider them as errors
     *     ({@code false}).
     * @return The application exit code. See the application framework documentation for a description of the exit
     *     codes.
     * @throws ToolDefException If the class loader can not be obtained, the class can not be found or loaded, the class
     *     is not an application framework application, an appropriate constructor is not available, in instance of the
     *     application class can not be constructed, the command line arguments can not be parsed, or the application
     *     fails to execute due to its thread being interrupted.
     * @throws ExitException If the application exits with a non-zero exit code and non-zero exit codes are not ignored.
     */
    public static int app(String plugin, String classname, List<String> args, String stdin, String stdout,
            String stderr, boolean appendOut, boolean appendErr, boolean errToOut, boolean ignoreNonZeroExitCode)
    {
        // Get class loader.
        ClassLoader loader = getClassLoader(plugin);

        // Load the application class
        Class<?> cls;
        try {
            cls = loader.loadClass(classname);
        } catch (ClassNotFoundException ex) {
            String msg = fmt("Failed to load/find class \"%s\".", classname);
            throw new ToolDefException(msg, ex);
        }

        // Make sure it is an application framework application class.
        Class<Application<?>> appClass;
        try {
            @SuppressWarnings("unchecked")
            Class<Application<?>> tmpClass = (Class<Application<?>>)cls;
            appClass = tmpClass;
        } catch (ClassCastException ex) {
            String msg = fmt("Class \"%s\" is not an application framework application class.", classname);
            throw new ToolDefException(msg, ex);
        }

        // Process arguments.
        List<String> processedArgs = list();
        for (String arg: args) {
            String[] parsedArgs;
            try {
                parsedArgs = CommandLineUtils.parseArgs(arg);
            } catch (IllegalArgumentException ex) {
                String msg = fmt("Failed to parse command line argument: \"%s\".", arg);
                throw new ToolDefException(msg, ex);
            }

            for (String parsedArg: parsedArgs) {
                processedArgs.add(parsedArg);
            }
        }
        String[] argsArray = processedArgs.toArray(new String[0]);

        // Run child application.
        int exitCode;
        try {
            exitCode = ChildAppStarter.exec(appClass, argsArray, stdin, stdout, stderr, appendOut, appendErr, errToOut,
                    true);
        } catch (RuntimeException ex) {
            String msg = "Application framework application execution failed.";
            throw new ToolDefException(msg, ex);
        }

        // Return exit code.
        if (exitCode != 0 && !ignoreNonZeroExitCode) {
            throw new ExitException(exitCode);
        }
        return exitCode;
    }

    /**
     * Obtains and returns a class loader for a plug-in, for loading an application framework application from it.
     *
     * @param name The name of the plug-in (OSGi bundle) for which to obtain the class loader. Ignored if the
     *     Eclipse/OSGi framework is not running. May be {@code null} to always ignore.
     * @return The class loader.
     * @throws ToolDefException If obtaining the class loader failed.
     */
    private static ClassLoader getClassLoader(String name) {
        return new ClassLoaderObtainer() {
            @Override
            protected void errNotPluginProj(String name) {
                String msg = fmt("Cannot load application from plug-in (Eclipse project) \"%s\", as the project "
                        + "is not a plug-in project, or it contains a manifest file that is malformed or "
                        + "missing vital information.", name);
                throw new ToolDefException(msg);
            }

            @Override
            protected void errPluginClassicFormat(String name) {
                String msg = fmt("Cannot load application from plug-in (Eclipse project) \"%s\", as the plug-in "
                        + "project is in a classic format (does not use the new OSGi bundle layout).", name);
                throw new ToolDefException(msg);
            }

            @Override
            protected void errComputeClassPath(String name, CoreException ex) {
                String exMsg = ex.getMessage();
                if (exMsg == null) {
                    exMsg = "no additional details available.";
                }
                String msg = fmt("Cannot load application from plug-in (Eclipse project) \"%s\", as the class "
                        + "path of the project could not be computed. The plug-in may not have a Java "
                        + "nature. Cause: %s", name, exMsg);
                throw new ToolDefException(msg);
            }

            @Override
            protected void errMalformedUrl(String name, MalformedURLException ex) {
                String exMsg = ex.getMessage();
                if (exMsg == null) {
                    exMsg = "no additional details available.";
                }
                String msg = fmt("Cannot load application from plug-in (Eclipse project) \"%s\", as an URL from "
                        + "the project's class path is malformed. Cause: %s", name, exMsg);
                throw new ToolDefException(msg);
            }

            @Override
            protected void errOpenUrl(String name, OpenUrlException ex) {
                String exMsg = ex.getMessage();
                if (exMsg == null) {
                    exMsg = "no additional details available.";
                }
                String msg = fmt(
                        "Cannot load application from plug-in (Eclipse project) \"%s\", as URL \"%s\" "
                                + "from the project's class path could not be opened. Cause: %s",
                        name, ex.url.toString(), exMsg);
                throw new ToolDefException(msg);
            }

            @Override
            protected void errNotFound(String name) {
                String msg = fmt("Cannot find a plug-in (OSGi bundle or Eclipse project) with name \"%s\".", name);
                throw new ToolDefException(msg);
            }

            @Override
            protected void errWrongState(String name, Bundle bundle, int state) {
                String msg = fmt(
                        "Cannot load application from plug-in (OSGi bundle) \"%s\", as the plug-in is "
                                + "in a wrong state (%s), as it should be in state RESOLVED, STARTING, or ACTIVE.",
                        name, PlatformUtils.getStateName(bundle));
                throw new ToolDefException(msg);
            }

            @Override
            protected void errNoClassLoader(String name, Bundle bundle) {
                String msg = fmt("Cannot load application from plug-in (OSGi bundle) \"%s\", as a class loader "
                        + "could not be obtained for the plug-in.", name);
                throw new ToolDefException(msg);
            }
        }.getClassLoader(name);
    }

    /**
     * Executes a system command or external application as a sub process.
     *
     * <p>
     * This tool should be avoided if possible, as it is in general not guaranteed to be cross platform and machine
     * independent.
     * </p>
     *
     * @param cmd The name of the command or the absolute or relative local file system path of the external application
     *     to execute, using platform specific new line characters.
     * @param args The command line arguments of the command or external application to execute. Each argument is parsed
     *     using {@link CommandLineUtils#parseArgs} to zero or more actual arguments.
     * @param stdin Specify whether to have a standard input (stdin) stream and where the input comes from. Use
     *     {@code ""} to not have a stdin stream, {@code "-"} to use the stdin stream of the ToolDef interpreter, or
     *     otherwise an absolute or relative local file system path of the file from which to read the standard input.
     *     May contain both {@code "\"} and {@code "/"} as path separators.
     * @param stdout Specify whether to have a standard output (stdout) stream and where to write the standard output.
     *     Use {@code ""} to not have a stdout stream, {@code "-"} to use the stdout stream of the ToolDef interpreter,
     *     or otherwise an absolute or relative local file system path of the file to which to write the standard
     *     output. May contain both {@code "\"} and {@code "/"} as path separators.
     * @param stderr Specify whether to have a standard error (stderr) stream and where to write the standard error
     *     output. Use {@code ""} to not have a stderr stream, {@code "-"} to use the stderr stream of the ToolDef
     *     interpreter, or otherwise an absolute or relative local file system path of the file to which to write the
     *     standard error output. May contain both {@code "\"} and {@code "/"} as path separators. Is ignored when the
     *     standard error stream is redirected to the standard output stream.
     * @param appendOut Whether to append to the stdout file ({@code true}) or overwrite it ({@code false}). Is ignored
     *     when standard output is not written to a file.
     * @param appendErr Whether to append to the stderr file ({@code true}) or overwrite it ({@code false}). Is ignored
     *     if standard error output is not written to a file. Is also ignored when standard error stream is redirected
     *     to the standard output stream.
     * @param errToOut Whether to redirect the standard error stream to the standard output stream ({@code true}) or use
     *     separate streams ({@code false}).
     * @param ignoreNonZeroExitCode Whether to ignore non-zero exit codes ({@code true}) or consider them as errors
     *     ({@code false}).
     * @return The exit code resulting from the execution of the command or external application.
     * @throws ToolDefException If the command line arguments can not be parsed, of if execution fails.
     */
    @SuppressWarnings("null")
    public static int exec(String cmd, List<String> args, String stdin, String stdout, String stderr, boolean appendOut,
            boolean appendErr, boolean errToOut, boolean ignoreNonZeroExitCode)
    {
        // Initialization.
        AppStreams tooldefStreams = AppEnv.getStreams();
        InputOutputException ioErr = null;

        // Configure child stdin stream.
        InputStream inStream = null;
        if (ioErr == null) {
            if (stdin.equals("")) {
                inStream = new NullInputStream(0);
            } else if (stdin.equals("-")) {
                inStream = tooldefStreams.inStream;
            } else {
                String absStdin = Paths.resolve(stdin);
                try {
                    inStream = new FileInputStream(absStdin);
                    inStream = new BufferedInputStream(inStream);
                } catch (FileNotFoundException ex) {
                    String msg = fmt("Failed to open file \"%s\" for reading, as it can not be found, is a "
                            + "directory rather than a file, or for some other reason can not be opened "
                            + "for reading.", stdin);
                    ioErr = new InputOutputException(msg, ex);
                }
            }
        }

        // Configure child stdout stream.
        OutputStream outStream = null;
        if (ioErr == null) {
            if (stdout.equals("")) {
                outStream = new NullOutputStream();
            } else if (stdout.equals("-")) {
                outStream = tooldefStreams.out.asOutputStream();
            } else {
                String absStdout = Paths.resolve(stdout);
                try {
                    outStream = new FileOutputStream(absStdout);
                    outStream = new BufferedOutputStream(outStream);
                } catch (FileNotFoundException ex) {
                    String msg = fmt("Failed to open file \"%s\" for writing, as it already exists as a "
                            + "directory, does not exist but cannot be created, or for some other reason "
                            + "cannot be opened for opened for writing.", stdout);
                    ioErr = new InputOutputException(msg, ex);
                }
            }
        }

        // Configure child stderr stream.
        OutputStream errStream = null;
        if (ioErr == null) {
            if (errToOut) {
                errStream = outStream;
            } else if (stderr.equals("")) {
                errStream = new NullOutputStream();
            } else if (stderr.equals("-")) {
                errStream = tooldefStreams.err.asOutputStream();
            } else {
                String absStderr = Paths.resolve(stderr);
                try {
                    errStream = new FileOutputStream(absStderr);
                    errStream = new BufferedOutputStream(errStream);
                } catch (FileNotFoundException ex) {
                    String msg = fmt("Failed to open file \"%s\" for writing, as it already exists as a "
                            + "directory, does not exist but cannot be created, or for some other reason "
                            + "cannot be opened for opened for writing.", stderr);
                    ioErr = new InputOutputException(msg, ex);
                }
            }
        }

        // Do actual execution.
        Throwable execErr = null;
        Integer exitCode = null;
        if (ioErr == null) {
            try {
                exitCode = execInternal(cmd, args, inStream, outStream, errStream, ignoreNonZeroExitCode);
            } catch (ApplicationException | ExitException ex) {
                execErr = ex;
            }
        }

        // Flush standard output and error streams.
        try {
            if (outStream != null) {
                outStream.flush();
            }
        } catch (IOException ex) {
            String msg = "Failed to flush stdout stream.";
            if (ioErr != null) {
                ioErr = new InputOutputException(msg, ex);
            }
        }

        try {
            if (errStream != null) {
                errStream.flush();
            }
        } catch (IOException ex) {
            String msg = "Failed to flush stderr stream.";
            if (ioErr != null) {
                ioErr = new InputOutputException(msg, ex);
            }
        }

        // Close standard streams that are redirected to a file.
        if (!stdin.equals("") && !stdin.equals("-")) {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException ex) {
                String msg = fmt("Failed to close file \"%s\".", stdin);
                if (ioErr != null) {
                    ioErr = new InputOutputException(msg, ex);
                }
            }
        }

        if (!stdout.equals("") && !stdout.equals("-")) {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException ex) {
                String msg = "Failed to close stdout stream.";
                if (ioErr != null) {
                    ioErr = new InputOutputException(msg, ex);
                }
            }
        }

        if (!errToOut && !stderr.equals("") && !stderr.equals("-")) {
            try {
                if (errStream != null) {
                    errStream.close();
                }
            } catch (IOException ex) {
                String msg = "Failed to close stderr stream.";
                if (ioErr != null) {
                    ioErr = new InputOutputException(msg, ex);
                }
            }
        }

        // Report execution failure. Can only be executed if no I/O error
        // beforehand.
        if (execErr != null) {
            if (execErr instanceof ApplicationException) {
                throw (ApplicationException)execErr;
            } else if (execErr instanceof ExitException) {
                throw (ExitException)execErr;
            } else {
                throw new RuntimeException("Unknown execErr: " + execErr);
            }
        }

        // Report I/O failure.
        if (ioErr != null) {
            throw ioErr;
        }

        // Return the exit code.
        Assert.notNull(exitCode);
        return exitCode;
    }

    /**
     * Executes a system command or external application as a sub process.
     *
     * <p>
     * Note that this tool should be avoided if possible, as it is in general not guaranteed to be cross platform and
     * machine independent.
     * </p>
     *
     * @param cmd The name of the command or the absolute or relative local file system path of the external application
     *     to execute, using platform specific new line characters.
     * @param args The command line arguments of the command or external application to execute. Each argument is parsed
     *     using {@link CommandLineUtils#parseArgs} to zero or more actual arguments.
     * @param stdin The stream to use for standard input.
     * @param stdout The stream to use for standard output.
     * @param stderr The stream to use for standard error output.
     * @param ignoreNonZeroExitCode Whether to ignore non-zero exit codes ({@code true}) or consider them as errors
     *     ({@code false}).
     * @return The exit code resulting from the execution of the command or external application.
     * @throws ToolDefException If the command line arguments can not be parsed, or if execution fails.
     */
    private static int execInternal(String cmd, List<String> args, InputStream stdin, OutputStream stdout,
            OutputStream stderr, boolean ignoreNonZeroExitCode)
    {
        // Create command line.
        CommandLine commandLine = new CommandLine(cmd);
        for (String arg: args) {
            String[] parsedArgs;
            try {
                parsedArgs = CommandLineUtils.parseArgs(arg);
            } catch (IllegalArgumentException ex) {
                String msg = fmt("Failed to parse command line argument: \"%s\".", arg);
                throw new ToolDefException(msg, ex);
            }

            for (String parsedArg: parsedArgs) {
                commandLine.addArgument(parsedArg, false);
            }
        }

        // Initialize executor and result handler.
        Executor executor = new DefaultExecutor();
        DefaultExecuteResultHandler rslt = new DefaultExecuteResultHandler();

        // Configure standard I/O streams.
        PumpStreamHandler streams = new PumpStreamHandler(stdout, stderr, stdin);
        executor.setStreamHandler(streams);

        // Set working directory.
        File workingDir = new File(Paths.getCurWorkingDir());
        executor.setWorkingDirectory(workingDir);

        // Execute command asynchronously.
        try {
            executor.execute(commandLine, rslt);
        } catch (ExecuteException ex) {
            String msg = "Failed to execute system command or external application.";
            throw new ToolDefException(msg, ex);
        } catch (IOException ex) {
            String msg = "Failed to execute system command or external application, due to I/O failure.";
            throw new ToolDefException(msg, ex);
        }

        // Wait for execution to complete.
        try {
            rslt.waitFor();
        } catch (InterruptedException ex) {
            String msg = "Failed to execute system command or external application: the application was interrupted.";
            throw new ToolDefException(msg, ex);
        }

        // Check the result.
        if (rslt.getException() != null) {
            String msg = fmt("Execution of system command or external application failed (exit code = %d).",
                    rslt.getExitValue());
            throw new ToolDefException(msg, rslt.getException());
        }

        // Return exit code.
        int exitCode = rslt.getExitValue();
        if (exitCode != 0 && !ignoreNonZeroExitCode) {
            throw new ExitException(exitCode);
        }
        return exitCode;
    }

    /**
     * Executes a ToolDef script.
     *
     * <p>
     * The script is executed within the current Java interpreter, in a new thread, using a new ToolDef interpreter.
     * </p>
     *
     * @param args The command line arguments for the ToolDef interpreter, including the path to the script to execute.
     *     Each argument is parsed using {@link CommandLineUtils#parseArgs} to zero or more actual arguments.
     * @param stdin Specify whether to have a standard input (stdin) stream and where the input comes from. Use
     *     {@code ""} to not have a stdin stream, {@code "-"} to use the stdin stream of the ToolDef interpreter, or
     *     otherwise an absolute or relative local file system path of the file from which to read the standard input.
     *     May contain both {@code "\"} and {@code "/"} as path separators.
     * @param stdout Specify whether to have a standard output (stdout) stream and where to write the standard output.
     *     Use {@code ""} to not have a stdout stream, {@code "-"} to use the stdout stream of the ToolDef interpreter,
     *     or otherwise an absolute or relative local file system path of the file to which to write the standard
     *     output. May contain both {@code "\"} and {@code "/"} as path separators.
     * @param stderr Specify whether to have a standard error (stderr) stream and where to write the standard error
     *     output. Use {@code ""} to not have a stderr stream, {@code "-"} to use the stderr stream of the ToolDef
     *     interpreter, or otherwise an absolute or relative local file system path of the file to which to write the
     *     standard error output. May contain both {@code "\"} and {@code "/"} as path separators. Is ignored when the
     *     standard error stream is redirected to the standard output stream.
     * @param appendOut Whether to append to the stdout file ({@code true}) or overwrite it ({@code false}). Is ignored
     *     when standard output is not written to a file.
     * @param appendErr Whether to append to the stderr file ({@code true}) or overwrite it ({@code false}). Is ignored
     *     if standard error output is not written to a file. Is also ignored when standard error stream is redirected
     *     to the standard output stream.
     * @param errToOut Whether to redirect the standard error stream to the standard output stream ({@code true}) or use
     *     separate streams ({@code false}).
     * @param ignoreNonZeroExitCode Whether to ignore non-zero exit codes ({@code true}) or consider them as errors
     *     ({@code false}).
     * @return The ToolDef interpreter exit code, i.e. the exit code of the script.
     * @throws ToolDefException If the class loader can not be obtained, the class can not be found or loaded, the class
     *     is not an application framework application, an appropriate constructor is not available, an instance of the
     *     application class can not be constructed, or the application fails to execute due to its thread being
     *     interrupted.
     * @throws ExitException If execution results in a non-zero exit code and non-zero exit codes are not ignored.
     */
    public static int tooldef(List<String> args, String stdin, String stdout, String stderr, boolean appendOut,
            boolean appendErr, boolean errToOut, boolean ignoreNonZeroExitCode)
    {
        return app("org.eclipse.escet.tooldef.interpreter",
                "org.eclipse.escet.tooldef.interpreter.ToolDefInterpreterApp", args, stdin, stdout, stderr, appendOut,
                appendErr, errToOut, ignoreNonZeroExitCode);
    }
}
