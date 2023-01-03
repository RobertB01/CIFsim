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

import static org.eclipse.escet.common.app.framework.output.OutputProvider.err;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Lists.listc;
import static org.eclipse.escet.common.java.Pair.pair;

import java.io.File;
import java.net.URI;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;
import org.eclipse.escet.common.app.framework.exceptions.EndUserException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.app.framework.management.AppManager;
import org.eclipse.escet.common.app.framework.management.AppStatus;
import org.eclipse.escet.common.app.framework.options.DevModeOption;
import org.eclipse.escet.common.app.framework.options.GuiMode;
import org.eclipse.escet.common.app.framework.options.GuiOption;
import org.eclipse.escet.common.app.framework.options.HelpOption;
import org.eclipse.escet.common.app.framework.options.LicenseOption;
import org.eclipse.escet.common.app.framework.options.Option;
import org.eclipse.escet.common.app.framework.options.OptionCategory;
import org.eclipse.escet.common.app.framework.options.OptionDialog;
import org.eclipse.escet.common.app.framework.options.OptionDialogOption;
import org.eclipse.escet.common.app.framework.options.OptionValue;
import org.eclipse.escet.common.app.framework.options.Options;
import org.eclipse.escet.common.app.framework.output.IOutputComponent;
import org.eclipse.escet.common.app.framework.output.OutputModeOption;
import org.eclipse.escet.common.app.framework.output.OutputProvider;
import org.eclipse.escet.common.app.framework.output.StreamOutputComponent;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Base class for all applications that support/use the <em>application framework</em>.
 *
 * <p>
 * See the application framework documentation for more information.
 * </p>
 *
 * @param <T> The application's output interface.
 */
public abstract class Application<T extends IOutputComponent> {
    /** The application specific environment data for this application. */
    private final AppEnvData appEnvData;

    /** Whether the next exception to report is the first one being reported. */
    private boolean firstException = true;

    /**
     * Constructor for the {@link Application} class. Uses {@link System#in}, {@link System#out}, and {@link System#err}
     * as streams. Asks the application for the output provider, creates a fresh {@link Options} instance, and a fresh
     * {@link AppProperties} instance.
     */
    public Application() {
        this(null, null, null, null);
    }

    /**
     * Constructor for the {@link Application} class. Asks the application for the output provider, creates a fresh
     * {@link Options} instance, and a fresh {@link AppProperties} instance.
     *
     * @param streams The streams to use for input, output, and error streams.
     */
    public Application(AppStreams streams) {
        this(streams, null, null, null);
    }

    /**
     * Constructor for the {@link Application} class. Asks the application for the output provider, and uses a fresh
     * {@link AppProperties} instance.
     *
     * @param streams The streams to use for input, output, and error streams.
     * @param options The options to use for the application.
     */
    public Application(AppStreams streams, Options options) {
        this(streams, null, options, null);
    }

    /**
     * Constructor for the {@link Application} class. Re-uses the given application data for the new application. Note
     * that for instance application options and properties will be shared by both applications, and a change to an
     * option or property in one application will be reflected in the other application. This includes the current
     * working directory of both applications.
     *
     * @param data The application environment data to use for the application.
     */
    public Application(AppEnvData data) {
        this(data.getStreams(), data.getProvider(), data.getOptions(), data.getProperties());
    }

    /**
     * Constructor for the {@link Application} class.
     *
     * @param streams The application stream to use for the application, or {@code null} to use {@link System#in},
     *     {@link System#out}, and {@link System#err} as streams.
     * @param provider The output provider to use for the application, or {@code null} to ask the application for the
     *     output provider.
     * @param options The options to use for the application, or {@code null} to create a fresh {@link Options}
     *     instance.
     * @param properties The properties to use for the application, or {@code null} to create a fresh
     *     {@link AppProperties} instance.
     */
    public Application(AppStreams streams, OutputProvider<?> provider, Options options, AppProperties properties) {
        // Construct application environment data.
        appEnvData = new AppEnvData(this, //
                (streams == null) ? new AppStreams() : streams, //
                (provider == null) ? getProvider() : provider, //
                (options == null) ? new Options() : options, //
                (properties == null) ? new AppProperties() : properties //
        );

        // Register the application.
        AppEnv.registerApplication(appEnvData);

        // Set default values for application framework options. If a crash
        // occurs in the application framework, we are at least able to get
        // values for the application framework options.
        Options.set(DevModeOption.class, false);
        Options.set(OptionDialogOption.class, false);
    }

    /**
     * Returns the output provider for this application.
     *
     * <p>
     * If the application uses an output interface other than {@link IOutputComponent}, it should have it's own derived
     * output provider class that implements that specific output interface. Otherwise, it may use an instance of the
     * standard {@link OutputProvider} class.
     * </p>
     *
     * @return The output provider for this application.
     */
    protected abstract OutputProvider<T> getProvider();

    /**
     * Runs the actual application, in the application framework. Should in general be called from the application's
     * 'main' method.
     *
     * <p>
     * If run in within Eclipse (OSGi platform), this method returns the exit code of the application. If this method is
     * called from a stand-alone application, this method never returns. Instead, the {@link System#exit} method is used
     * to return the exit code, and terminate the application.
     * </p>
     *
     * @param args The command line arguments supplied to the application.
     * @return The application exit code, but only if called from an Eclipse (OSGi platform) environment. See the
     *     application framework documentation for a description of the exit codes.
     */
    public final int run(String[] args) {
        // Add application to application manager.
        AppManager.add(this, null);

        // Run the application.
        int exitCode;
        try {
            exitCode = run(args, true);
        } finally {
            // Remove application from application manager.
            AppManager.remove(this);
        }

        // Return exit code.
        return exitCode;
    }

    /**
     * Runs the actual application, in the application framework. Should in general be called from Eclipse plug-in unit
     * tests.
     *
     * @param args The command line arguments supplied to the application.
     * @return The application exit code. See the application framework documentation for a description of the exit
     *     codes.
     */
    public final int runTest(String[] args) {
        // Add application to application manager.
        AppManager.add(this, null);

        // Run the application.
        int exitCode;
        try {
            exitCode = run(args, false);
        } finally {
            // Remove application from application manager.
            AppManager.remove(this);
        }

        // Return exit code.
        return exitCode;
    }

    /**
     * Runs the actual application, in the application framework. Should in general be called from the application's
     * 'main' method.
     *
     * <p>
     * The caller should add the application to the application manager before calling this method. The caller should
     * also remove the application from the application manager afterwards. See {@link AppManager#add} and
     * {@link AppManager#remove}.
     * </p>
     *
     * @param args The command line arguments supplied to the application.
     * @param exit If enabled and run within Eclipse (OSGi platform), this method returns the exit code of the
     *     application. If enabled and this method is called from a stand-alone application, this method never returns.
     *     Instead, the {@link System#exit} method is used to return the exit code, and terminate the application. If
     *     disabled, always returns the exit code.
     * @return The application exit code, but only if called from an Eclipse (OSGi platform) environment. See the
     *     application framework documentation for a description of the exit codes.
     */
    public final int run(String[] args, boolean exit) {
        // Make sure application is added to application manager.
        if (!AppManager.checkExists(this)) {
            throw new RuntimeException("Application is not known by the application manager.");
        }

        // Initialize exit code.
        int exitCode = 0;
        boolean managerReported = false;

        // Set up default console/stream output component. This needs to be
        // done before the exception handling framework kicks in, as we can't
        // report problems without stdout/stderr streams. We also need to do
        // this before processing options, because some options print text to
        // stdout, and also because option processing must be done inside the
        // exception handling framework, as options and option values can be
        // invalid.
        if (OutputProvider.getComponentCount() == 0) {
            // No output providers available. Add default console output
            // component. If there are already output providers available,
            // we assume that a console output component was already added.
            AppStream out = AppEnv.getStreams().out;
            AppStream err = AppEnv.getStreams().err;
            IOutputComponent output = getStreamOutputComponent(out, err);
            OutputProvider.register(output);
        }

        // Run application. All exceptions are caught and handled/reported such
        // that the application doesn't crash.
        try {
            // Store application start time.
            AppEnv.setProperty("org.eclipse.escet.common.app.framework.start.epoch",
                    String.valueOf(System.currentTimeMillis()));
            AppEnv.setProperty("org.eclipse.escet.common.app.framework.start.nanos", String.valueOf(System.nanoTime()));

            // Store the original command line arguments. This is useful
            // for crash reports.
            AppEnv.setProperty("org.eclipse.escet.common.app.framework.args.cmdline", Strings.stringArrayToJava(args));

            // Initialize option processing.
            OptionCategory options = getAllOptions();
            Options.initialize(options, true);
            Options.set(OptionDialogOption.class, false);
            preOptions();

            // Process options from the command line.
            Options.parse(options, args);

            // Create SWT display thread, if needed. Required for everything
            // GUI related, including the option dialog, visualizers, etc.
            GuiMode guiMode = GuiOption.getGuiMode();
            if (guiMode != GuiMode.OFF) {
                Pair<Boolean, SWTDisplayThread> guiInfo = SWTDisplayThread.create(guiMode);
                boolean guiAvailable = guiInfo.left;
                SWTDisplayThread swtThread = guiInfo.right;
                appEnvData.setGuiAvailable(guiAvailable);
                appEnvData.setSwtDisplayThread(swtThread);
            }

            // Process options from the option dialog. Don't show interactive
            // dialog if termination already requested.
            if (OptionDialogOption.isEnabled() && !isTerminationRequested()) {
                // Option dialog requires GUI.
                appEnvData.checkGuiAvailable("show the option dialog");

                // Show dialog and option selected options.
                String[] dialogArgs = OptionDialog.showDialog(options);
                if (dialogArgs == null) {
                    throw new SuccessfulExitException();
                }

                // Store the dialog command line arguments. This is useful
                // for crash reports.
                AppEnv.setProperty("org.eclipse.escet.common.app.framework.args.dialog",
                        Strings.stringArrayToJava(dialogArgs));

                // Reset all options to their default values. This also clears
                // the remaining arguments option, if any. The command line
                // dialog options contain all options anyway, so parsing them
                // will subsequently overwrite all options again. However,
                // don't re-initialize options not shown in the option dialog,
                // as their values would be lost.
                Options.initialize(options, false);
                Options.parse(options, dialogArgs);
            }

            // Finalize option processing.
            Options.postProcess(options);
            Options.verifyOptions(options);
            postOptions();

            // Store the current command line arguments. This is useful
            // for crash reports.
            List<String> finalArgs = list();
            for (Entry<Option<?>, OptionValue<?>> entry: Options.getOptionMap().entrySet()) {
                String[] optArgs = entry.getKey().getCmdLine(entry.getValue().getValue());
                for (String optArg: optArgs) {
                    finalArgs.add(optArg);
                }
            }
            AppEnv.setProperty("org.eclipse.escet.common.app.framework.args.final",
                    Strings.stringArrayToJava(finalArgs.toArray(new String[] {})));

            // Run the actual application code, but only if termination not
            // yet requested.
            if (!isTerminationRequested()) {
                AppManager.update(this, AppStatus.RUNNING);
                exitCode = runInternal();
            }
        } catch (SuccessfulExitException ex) {
            // Application finished with zero exit code. Finalization happens
            // next, to properly terminate the application.
            AppManager.update(this, AppStatus.FINISHED);
            managerReported = true;
            exitCode = 0;
        } catch (ApplicationException ex) {
            // Exception for the end user.
            AppManager.update(this, AppStatus.FAILED);
            managerReported = true;
            exitCode = reportEx(ex);
        } catch (Throwable ex) {
            // Internal error (crash).
            AppManager.update(this, AppStatus.CRASHED);
            managerReported = true;
            exitCode = reportEx(ex);
        } finally {
            // Clean up output providers. If exceptions occur, the output
            // component that threw the exception should still have cleaned
            // itself up. The remaining components however, may still need
            // cleanup. As performing cleanup multiple times is allowed, retry
            // cleanup until it succeeds.
            int maxRetry = OutputProvider.getComponentCount();
            int retryCnt = 0;
            while (true) {
                try {
                    // Try cleanup. If succeeds, we are done.
                    OutputProvider.cleanup();
                    break;
                } catch (ApplicationException ex) {
                    // Exception for the end user.
                    int newExitCode = reportEx(ex);
                    if (newExitCode > exitCode) {
                        AppManager.update(this, AppStatus.FAILED);
                        managerReported = true;
                    }
                    exitCode = Math.max(exitCode, newExitCode);
                } catch (Throwable ex) {
                    // Internal error (crash).
                    int newExitCode = reportEx(ex);
                    if (newExitCode > exitCode) {
                        AppManager.update(this, AppStatus.CRASHED);
                        managerReported = true;
                    }
                    exitCode = Math.max(exitCode, newExitCode);
                }

                // Protection against infinite cleanup retry.
                retryCnt++;
                if (retryCnt > maxRetry) {
                    String msg = "Cleanup retry count exceeded: " + retryCnt;
                    int newExitCode = reportEx(new RuntimeException(msg));
                    if (newExitCode > exitCode) {
                        AppManager.update(this, AppStatus.CRASHED);
                        managerReported = true;
                    }
                    exitCode = Math.max(exitCode, newExitCode);
                    break;
                }
            }

            // If success, update status of application manager.
            if (!managerReported) {
                AppStatus status;
                if (exitCode > 0) {
                    status = AppStatus.FAILED;
                } else if (isTerminationRequested()) {
                    status = AppStatus.TERMINATED;
                } else {
                    status = AppStatus.FINISHED;
                }
                AppManager.update(this, status);
                managerReported = true;
            }

            // Close the SWT display. We do this after cleaning up the output
            // components, as the output components may own SWT components.
            if (appEnvData.getSwtDisplayThread() != null) {
                appEnvData.getSwtDisplayThread().close();
                appEnvData.setSwtDisplayThread(null);
            }

            // Unregister the application. We do this after closing the SWT
            // display, as SWT call-backs may be using application framework
            // options, etc.
            AppEnv.unregisterApplication();
        }

        // Process exit code.
        if (!exit || (Platform.isRunning() && PlatformUI.isWorkbenchRunning())) {
            // We can't use {@link System#exit} in Eclipse, as it would shut
            // down the JVM, and thus Eclipse. So, we simply return the exit
            // code. The application main methods should decide what to do with
            // non-zero exit codes. It could for instance throw an exception.
            return exitCode;
        }

        // Stand-alone.
        System.exit(exitCode);
        return 0; // Never reached.
    }

    /**
     * Internal class to throw in cases of successful early program termination. May only be used by this class and
     * other classes from the application framework.
     */
    public static class SuccessfulExitException extends RuntimeException {
        /** Constructor for the {@link SuccessfulExitException} class. */
        public SuccessfulExitException() {
            // No message, cause, etc, as the exception itself is enough.
        }
    }

    /**
     * Runs the actual application code. This could be considered the real 'main' method. This method must
     * <strong>not</strong> be called directly. Instead, use the {@link #run} method.
     *
     * @return The application exit code. See the application framework documentation for a description of the exit
     *     codes.
     */
    protected abstract int runInternal();

    /**
     * Reports an exception to the user. Reports both the exception itself and all suppressed exceptions. Distinguishes
     * end user exceptions and crashes (internal errors).
     *
     * @param terminationEx The uncaught application exception that caused the application to terminate.
     * @return The application exit code. {@code 1} for end user error, {@code 2} for out of memory crash, {@code 3} for
     *     other crashes. The application exit code is the maximum exit code of the exception and all suppressed
     *     exceptions.
     */
    private int reportEx(Throwable terminationEx) {
        // Get exceptions to report, including all suppressed exceptions.
        List<Throwable> reportExs = listc(1);
        reportExs.add(terminationEx);
        addSuppresseds(terminationEx, reportExs);

        // Report the exceptions and determine application exit code.
        int exitCode = 0;
        for (Throwable reportEx: reportExs) {
            if (!firstException) {
                err("");
            }
            firstException = false;

            if (reportEx instanceof ApplicationException) {
                reportAppEx((ApplicationException)reportEx);
                if (exitCode < 1) {
                    exitCode = 1;
                }
            } else if (reportEx instanceof OutOfMemoryError) {
                String msg = "The application has run out of memory.";
                reportAppEx(new ApplicationException(msg, reportEx));
                if (exitCode < 2) {
                    exitCode = 2;
                }
            } else {
                reportCrash(reportEx);
                if (exitCode < 3) {
                    exitCode = 3;
                }
            }
        }

        // Return application exit code.
        return exitCode;
    }

    /**
     * Add suppressed exceptions, suppressed exceptions of the suppressed exceptions, etc, to the given list.
     *
     * @param ex The root exception to investigate.
     * @param exs The list to which to add the suppressed exceptions.
     */
    private static void addSuppresseds(Throwable ex, List<Throwable> exs) {
        // Use a loop instead of recursion to avoid Java method stack overflow
        // for very long exception/cause stacks.
        Deque<Pair<Throwable, Boolean>> queue = new LinkedList<>();
        queue.add(pair(ex, false));

        while (!queue.isEmpty()) {
            // Get next item.
            Pair<Throwable, Boolean> item = queue.pollFirst();
            Throwable curEx = item.left;
            Boolean suppressed = item.right;

            // Add current item if it is a suppressed exception, rather than
            // the root exception or a cause.
            if (suppressed) {
                exs.add(curEx);
            }

            // Add all suppressions, in reverse order.
            Throwable[] suppressions = curEx.getSuppressed();
            for (int i = suppressions.length - 1; i >= 0; i--) {
                queue.addFirst(pair(suppressions[i], true));
            }

            // Add cause.
            Throwable cause = curEx.getCause();
            if (cause != null) {
                queue.addFirst(pair(cause, false));
            }
        }
    }

    /**
     * Reports an application exception to the user.
     *
     * @param ex The uncaught application exception that caused the application to terminate.
     */
    private void reportAppEx(ApplicationException ex) {
        err("ERROR: " + ex.getMessage());
        if (ex.getCause() != null) {
            reportAppExCause(ex.getCause());
        }
    }

    /**
     * Reports an application exception cause to the user.
     *
     * @param ex The application exception cause that caused the application exception, or a cause of one of the other
     *     causes.
     */
    private void reportAppExCause(Throwable ex) {
        // Use a loop instead of recursion to avoid Java method stack overflow
        // for very long exception/cause stacks.
        while (ex != null) {
            // Report cause.
            String exMsg = ex.getMessage();
            if (exMsg == null) {
                exMsg = "<cause details not available>";
            }
            if (ex instanceof EndUserException) {
                err("CAUSE: %s", exMsg);
            } else {
                err("CAUSE: (%s) %s", ex.getClass().getSimpleName(), exMsg);
            }

            // Move on to next cause.
            ex = ex.getCause();
        }
    }

    /**
     * Reports a crash (internal error) to the user.
     *
     * @param ex The uncaught exception that caused the crash.
     */
    private void reportCrash(Throwable ex) {
        // No crash report for development mode, write to console only.
        if (DevModeOption.isEnabled()) {
            AppEnv.getStreams().err.print("CRASH: ");
            AppEnv.getStreams().err.printStackTrace(ex);
            return;
        }

        // Write crash report and inform user via the console.
        String crashReportPath = CrashReport.writeCrashReportFile(ex);
        err("ERROR: The application has crashed. Please always report crashes to the Eclipse ESCET development team.");
        err("REPORT: A crash report was generated in file \"%s\".", crashReportPath);

        // Refresh the directory (or directories) that contain the crash
        // report, if they can be found in the workspace.
        if (Platform.isRunning() && PlatformUI.isWorkbenchRunning()) {
            File crashFile = new File(crashReportPath);
            String parentPath = crashFile.getParent();

            URI parentURI = null;
            try {
                parentURI = Paths.createJavaURI(parentPath);
            } catch (RuntimeException e) {
                err("ERROR: Failed to resolve crash report location for refresh: " + parentPath);
            }

            if (parentURI != null) {
                try {
                    IFile[] parentFiles = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(parentURI);
                    for (IFile parentFile: parentFiles) {
                        parentFile.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
                    }
                } catch (CoreException e) {
                    err("ERROR: Failed to refresh: " + parentPath);
                }
            }
        }
    }

    /**
     * Prints the application's help message to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public void printHelpMessage(AppStream s) {
        printHelpHeader(s);
        printHelpOptions(s);
        printHelpNotes(s);
        printHelpExitCodes(s);
        printHelpCopyright(s);
    }

    /**
     * Prints the application's help message header to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public void printHelpHeader(AppStream s) {
        HelpOption.printHelpHeader(getAppName(), getAppVersionSafe(), getAppDescription(), s);
    }

    /**
     * Prints the application option's help messages to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public void printHelpOptions(AppStream s) {
        HelpOption.printHelpOptions(getAllOptions(), s);
    }

    /**
     * Prints the application's help message notes to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public void printHelpNotes(AppStream s) {
        HelpOption.printHelpNotes(getHelpMessageNotes(), s);
    }

    /**
     * Prints the application exit codes explanation to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public void printHelpExitCodes(AppStream s) {
        HelpOption.printHelpExitCodes(s);
    }

    /**
     * Prints the application's copyright message to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public void printHelpCopyright(AppStream s) {
        HelpOption.printHelpCopyrightEclipseEscet(s);
    }

    /**
     * Prints the application's license information to the given stream.
     *
     * @param s The stream to print the output to.
     */
    public void printHelpLicense(AppStream s) {
        HelpOption.printHelpLicenseEclipseEscet(s);
    }

    /**
     * Returns the full name of application.
     *
     * @return The full name of application.
     */
    public abstract String getAppName();

    /**
     * Returns the version of the application.
     *
     * <p>
     * The default implementation of the {@link Application} class returns the version of OSGi bundle that contains the
     * application class. It requires that the application class is part of an OSGI bundle and that the
     * {@link Platform#isRunning OSGi platform is running}. Derived classes should override the method if something
     * different is required.
     * </p>
     *
     * @return The version of the application.
     * @exception UnsupportedOperationException If OSGI platform is not running, or the application class is not defined
     *     by a bundle class loader.
     */
    public String getAppVersion() {
        // Get application class.
        Class<?> appClass = getClass();

        // Get bundle that contains the application class.
        if (!Platform.isRunning()) {
            String msg = "OSGi Platform is not running. Override Application.getAppVersion for " + appClass.getName();
            throw new UnsupportedOperationException(msg);
        }
        Bundle bundle = FrameworkUtil.getBundle(appClass);
        if (bundle == null) {
            String msg = "Application class not defined by bundle class loader. Override Application.getAppVersion for "
                    + appClass.getName();
            throw new UnsupportedOperationException(msg);
        }

        // Return bundle version.
        return bundle.getVersion().toString();
    }

    /**
     * Returns the version of the application.
     *
     * <p>
     * This method, unlike {@link #getAppVersion}, is safe against applications that don't overwrite the
     * {@link #getAppVersion}, but nonetheless the default implementation fails. In such cases the version returned is
     * {@code "<version-could-not-be-determined>"}.
     * </p>
     *
     * @return The version of the application, or {@code "<version-could-not-be-determined>"}.
     */
    public String getAppVersionSafe() {
        String version;
        try {
            return getAppVersion();
        } catch (UnsupportedOperationException e) {
            version = "<version-could-not-be-determined>";
        }
        return version;
    }

    /**
     * Returns a description of the application.
     *
     * @return A description of the application.
     */
    public abstract String getAppDescription();

    /**
     * Returns the application's help message notes. By default applications have no notes. Applications may override
     * this method to supply application specific notes.
     *
     * @return The application's help message notes.
     */
    public String[] getHelpMessageNotes() {
        return new String[] {};
    }

    /**
     * Returns a text with issue reporting instructions, for crash reports.
     *
     * <p>
     * The default implementation is specific for Eclipse ESCET. Applications may override this method to provide
     * instructions matching their own application.
     * </p>
     *
     * @return The text with issue reporting instructions.
     */
    public String getCrashReportIssueReportingInstructions() {
        return "Issues can be reported using the Eclipse ESCET issue tracker, at "
                + "https://gitlab.eclipse.org/eclipse/escet/escet/-/issues. For more information, see "
                + "https://eclipse.org/escet/contact-and-support.html. This crash report "
                + "contains valuable information that you can include in your bug report, to make it easier for the "
                + "bug to be fixed. We appreciate you taking the effort to report issues to us!";
    }

    /**
     * Returns the option category with the default options that apply to all application that use the application
     * framework. Derived classes should invoke this method in the {@link #getAllOptions} method, to obtain the general
     * option category.
     *
     * @return The option category with the default options that apply to all application that use the application
     *     framework.
     */
    protected static OptionCategory getGeneralOptionCategory() {
        return new OptionCategory("General", "General application options.", list(),
                list(Options.getInstance(HelpOption.class), Options.getInstance(OptionDialogOption.class),
                        Options.getInstance(OutputModeOption.class), Options.getInstance(LicenseOption.class),
                        Options.getInstance(GuiOption.class), Options.getInstance(DevModeOption.class)));
    }

    /**
     * Returns the options wrapper category for the application, with all categories and all options that apply to the
     * application.
     *
     * <p>
     * One of the (sub-)categories must be the general application options, obtained by invoking the
     * {@link #getGeneralOptionCategory} method.
     * </p>
     *
     * @return The options wrapper category for the application, with all categories and all options that apply to the
     *     application.
     */
    protected abstract OptionCategory getAllOptions();

    /**
     * Returns the default stream output component for the application. A stream output component is mandatory, as it
     * provides a way to display internal errors, etc.
     *
     * <p>
     * The default implementation returns an instance of the {@link StreamOutputComponent}. If the output of the
     * application is provided by the {@link IOutputComponent} interface, that is enough. Otherwise, the application
     * should override this method and provide a more an instance of a class that implements the full output component
     * interface for that particular application.
     * </p>
     *
     * @param out The output stream.
     * @param err The error stream.
     * @return The stream output component to use for the application.
     */
    protected IOutputComponent getStreamOutputComponent(AppStream out, AppStream err) {
        return new StreamOutputComponent(out, err);
    }

    /**
     * Handler for the pre-option handling event. The application framework is about to process the options (from
     * command line arguments etc). By default, this method does nothing. Derived classes may override it to initialize
     * certain options, etc.
     */
    protected void preOptions() {
        // By default, do nothing.
    }

    /**
     * Handler for the post-option handling event. The application framework has just finished processing options. By
     * default, this method does nothing. Derived classes may override it to register output components, etc.
     */
    protected void postOptions() {
        // By default, do nothing.
    }

    /**
     * Returns a value indicating whether termination of the application is requested.
     *
     * @return {@code true} if termination is requested, {@code false} otherwise.
     */
    public boolean isTerminationRequested() {
        return appEnvData.isTerminationRequested();
    }

    /** Requests termination of the application. */
    public void terminate() {
        appEnvData.terminate();
    }

    /**
     * Returns the application specific environment data for this application. Note that from within the application
     * itself, such information should be obtained using the {@link AppEnv} class instead.
     *
     * @return The application specific environment data for this application.
     */
    public AppEnvData getAppEnvData() {
        return appEnvData;
    }
}
