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

package org.eclipse.escet.common.eclipse.ui;

import static org.eclipse.escet.common.java.DateTimeUtils.durationToString;
import static org.eclipse.escet.common.java.DateTimeUtils.formatDateTime;
import static org.eclipse.escet.common.java.DateTimeUtils.nanoTimeToMillis;
import static org.eclipse.escet.common.java.Lists.first;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.escet.common.app.framework.Application;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.console.Console;
import org.eclipse.escet.common.app.framework.io.AppStreams;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Base class for command handlers, which execute application framework applications.
 *
 * <p>
 * The file(s) on which to execute the application can be obtained from various sources, to account for the various ways
 * in which the user may execute the command. These include:
 * <ul>
 * <li>The popup menu of text editors.</li>
 * <li>The popup menu of files in the project and package explorers.</li>
 * <li>Pressing a shortcut key when a text editor has the focus.</li>
 * <li>Pressing a shortcut key registered when a file is selected in the project or package explorer, and the project or
 * package explorer has the focus.</li>
 * </ul>
 * This handler must only be enabled when those conditions, or a subset of those conditions, hold.
 * </p>
 *
 * @see Application
 */
public abstract class BaseFileCommandHandler extends AbstractHandler {
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        // Get the file(s) that the command should execute on. We do this now,
        // and not in the new thread, to make sure that the selection of the
        // UI has not changed.
        final List<IFile> files = getFiles(event);

        // Save all dirty editors (needs to be executed on the UI thread).
        // If any editor has unsaved changes, the user is presented with a
        // dialog. The user can choose 'Yes' to save the changes and continue,
        // 'No' to not save the changes and continue, or 'Cancel' to not save
        // the changes and not continue. If multiple editors are dirty, a
        // different dialog is shown, where the user can select the files to
        // save, and must choose between 'OK' (continue) and 'Cancel' (don't
        // continue).
        boolean saveSuccess = PlatformUI.getWorkbench().saveAllEditors(true);
        if (!saveSuccess) {
            return null;
        }

        // To ensure that we don't block the IDE, execute the command code
        // in a separate thread. In that thread, execute the 'run' method
        // from this instance, which is captured in the inner class.
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                BaseFileCommandHandler.this.run(files);
            }
        };

        // Construct a new thread and name it after this class, for debugging.
        Thread t = new Thread(runnable, getClass().getName());
        t.start();

        // Nothing to return. The output of this command can not be used for
        // other commands.
        return null;
    }

    /**
     * This method determines the file(s) that the application should execute on.
     *
     * @param event The execution event to use to obtain the file(s) to operate on.
     * @return The file(s) that the application should execute on.
     */
    private final List<IFile> getFiles(ExecutionEvent event) {
        // Debug output.
        boolean debug = false;
        if (debug) {
            Object[][] infos = { //
                    {"Trigger:                  ", event.getTrigger()},
                    {"Current selection:        ", HandlerUtil.getCurrentSelection(event)},
                    {"Active menu selection:    ", HandlerUtil.getActiveMenuSelection(event)},
                    {"Active menu editor input: ", HandlerUtil.getActiveMenuEditorInput(event)},
                    {"Active part:              ", HandlerUtil.getActivePart(event)},
                    {"Active editor:            ", HandlerUtil.getActiveEditor(event)},
                    {"Active editor input:      ", HandlerUtil.getActiveEditorInput(event)},
                    {"Parameters:               ", event.getParameters().size()},
                    //
            };
            for (Object[] info: infos) {
                System.out.format("%s%s\n", info[0], info[1]);
            }
            for (Object param: event.getParameters().entrySet()) {
                @SuppressWarnings("unchecked")
                Entry<String, String> entry = (Entry<String, String>)param;
                System.out.format(" - %s: %s\n", entry.getKey(), entry.getValue());
            }
            System.out.println();
        }

        // If explicit 'IPath' given, use it.
        String ipathParam = event.getParameter(IPath.class.getName());
        if (ipathParam != null) {
            IPath path = new Path(ipathParam);
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IFile file = root.getFileForLocation(path);
            if (file == null) {
                throw new RuntimeException("No file found for path: " + path);
            }
            return list(file);
        }

        // Get the selection on which the command is executed. Active parts and
        // editors may exist, and have active selections, even if they were not
        // used to execute the command. Therefore, we have to be careful in the
        // order in which we obtain the selection.
        ISelection selection = HandlerUtil.getActiveMenuSelection(event);
        if (selection == null) {
            selection = HandlerUtil.getCurrentSelection(event);
        }

        // Obtain the file(s) from the selection.
        if (selection instanceof ITextSelection) {
            // Use the active editor instead of the active editor input, as the
            // active editor input is not properly updated (by Eclipse text
            // editors) on 'Save as'.
            // This ensures that if 'Save as' is used, and the focus has not
            // shifted away from the editor, and a command is executed for
            // the right-click menu of the text editor, the new file is
            // properly used, and not the old file.
            // Instead of 'fixing' the text editor, I opted to change the
            // commands started via the right click menu of the text editor.
            // This was simpler, and I have no idea how to change the
            // active editor input.
            IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
            IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
            Assert.check(activePart == editorPart);
            Assert.notNull(editorPart);
            IEditorInput editorInput = editorPart.getEditorInput();
            Assert.check(editorInput instanceof FileEditorInput);
            FileEditorInput fileInput = (FileEditorInput)editorInput;
            List<IFile> files = list(fileInput.getFile());
            return files;
        } else if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
            List<IFile> files = structuredSelection.toList();
            return files;
        } else {
            throw new RuntimeException("Unexpected selection: " + selection);
        }
    }

    /**
     * Executes the application (with a fresh Eclipse console) for the given file(s). After the application terminates,
     * the workspace project that the file belongs to (if any) is refreshed.
     *
     * <p>
     * This method is automatically executed in a fresh thread (by the {@link #execute} method).
     * </p>
     *
     * @param files The file(s) for which to execute the application.
     * @see #getApplicationClass
     * @see #getCommandLineArgs
     */
    public void run(List<IFile> files) {
        // Make sure we have at least one file.
        Assert.check(!files.isEmpty());
        checkFileCount(files);

        // Construct and display new console.
        Console console = new Console("Initializing...");

        // Create application.
        Class<? extends Application<?>> appClass = getApplicationClass();
        Constructor<? extends Application<?>> constructor;
        try {
            constructor = appClass.getConstructor(AppStreams.class);
        } catch (NoSuchMethodException e) {
            String msg = "No constructor with AppStreams argument found for class: " + appClass;
            throw new RuntimeException(msg, e);
        }

        Application<?> app;
        try {
            app = constructor.newInstance(console.getStreams());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to create app: " + appClass, e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Failed to create app: " + appClass, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to create app: " + appClass, e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to create app: " + appClass, e);
        }

        // Get start date/time.
        long startNano = System.nanoTime();
        Date startDate = new Date();
        String startDateText = formatDateTime(startDate, false);

        // Set console title.
        String filesTitleText = (files.size() == 1) ? getWorkspacePath(first(files)) : fmt("%d files", files.size());
        String title = fmt("%s [running] %s (started at %s)", app.getAppName(), filesTitleText, startDateText);
        console.setNameSync(title);

        // Couple the application to the console.
        console.setApplication(app);

        // Set current working directory to the parent of the first file.
        String parentDir = getParentLocal(first(files));
        Paths.setCurWorkingDir(parentDir);

        // Run application.
        String[] args = getCommandLineArgs(files, parentDir);
        int exitCode = app.run(args, false);

        // Refresh projects, asynchronously.
        Thread refreshThread = new Thread(() -> refreshFileProjects(files));
        refreshThread.setName(getClass().getName() + ": refreshFileProjects");
        refreshThread.start();

        // Console cleanup.
        console.cleanup();

        // Application finished.
        String exitTxt;
        if (exitCode != 0) {
            exitTxt = "FAILED after";
        } else if (app.isTerminationRequested()) {
            exitTxt = "TERMINATED after";
        } else {
            exitTxt = "FINISHED in";
        }
        long runningNano = System.nanoTime() - startNano;
        title = fmt("%s [%s %s] %s (started at %s)", app.getAppName(), exitTxt,
                durationToString(nanoTimeToMillis(runningNano), true), filesTitleText, startDateText);
        console.setNameSync(title);
    }

    /**
     * Checks the file count, using an assertion.
     *
     * @param files The file(s) for which to execute the application.
     * @see Assert
     */
    protected abstract void checkFileCount(List<IFile> files);

    /**
     * Returns the application class to use to create a new application.
     *
     * @return The application class to use to create a new application.
     */
    protected abstract Class<? extends Application<?>> getApplicationClass();

    /**
     * Returns the command line arguments for the application.
     *
     * <p>
     * An example of an implementation of this method could be:
     *
     * <pre>
     *     String[] args = new String[files.size() + 1];
     *     for (int i = 0; i < files.size(); i++) {
     *         IFile file = files.get(i);
     *         String absPath = file.getLocation().toString();
     *         args[i] = Paths.getRelativePath(absPath, workingDir);
     *     }
     *     args[files.size()] = "--option-dialog=yes";
     *     return args;
     * </pre>
     * </p>
     *
     * @param files The file(s) that this action operates on.
     * @param workingDir The absolute local file system path used as the current working directory for the application.
     *     Command line file paths are generally resolved relative to this directory.
     * @return The command line arguments for the application.
     */
    protected abstract String[] getCommandLineArgs(List<IFile> files, String workingDir);

    /**
     * Returns the absolute local file system path for the given file.
     *
     * @param file The file to get the absolute local file system path for.
     * @return The absolute local file system path for the given file.
     */
    protected static String getLocalFilePath(IFile file) {
        return file.getLocation().toOSString();
    }

    /**
     * Returns the workspace path for the given file.
     *
     * @param file The file to get the workspace path for.
     * @return The workspace path for the given file.
     */
    protected static String getWorkspacePath(IFile file) {
        return file.getFullPath().toString();
    }

    /**
     * Returns the file name, excluding any path, for the given file.
     *
     * @param file The file to get the file name for.
     * @return The file name, excluding any path, for the given file.
     */
    protected static String getFileName(IFile file) {
        return file.getName();
    }

    /**
     * Returns the absolute local file system path for the parent directory of the given file.
     *
     * @param file The file to get the parent directory for.
     * @return The absolute local file system path for the parent directory of the given file.
     */
    protected static String getParentLocal(IFile file) {
        return file.getLocation().removeLastSegments(1).toOSString();
    }

    /**
     * Returns the workspace path for the parent directory of the given file.
     *
     * @param file The file to get the parent directory for.
     * @return The workspace path for the parent directory of the given file.
     */
    protected static String getParentWorkspace(IFile file) {
        return file.getParent().toString();
    }

    /**
     * Refreshes the project that contains the given file. This method should never fail. The return value can be used
     * to check for refresh failures. In most cases however, the exception can be ignored, meaning this method tries its
     * best to refresh the project, and failures are silently discarded.
     *
     * @param file The file to refresh the project for.
     * @return {@code null} if refreshing succeeded, or a {@link CoreException} indicating the reason of the refresh
     *     failure otherwise.
     * @see IResource#refreshLocal
     */
    protected static CoreException refreshFileProject(IFile file) {
        try {
            file.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
            return null;
        } catch (CoreException e) {
            return e;
        }
    }

    /**
     * Refreshes the projects that contains the given files. This method should never fail. The return value can be used
     * to check for refresh failures. In most cases however, the exception can be ignored, meaning this method tries its
     * best to refresh the projects, and failures are silently discarded.
     *
     * <p>
     * Each project is refreshed at most once. That is, if multiple files are part of the same project, the project is
     * only refreshed once.
     * </p>
     *
     * @param files The files to refresh the projects for.
     * @return The {@link CoreException} exceptions indicating the reasons of the refresh failures, if any.
     * @see IResource#refreshLocal
     */
    protected static List<CoreException> refreshFileProjects(List<IFile> files) {
        // Get projects. Use a set to avoid refreshing a project more than
        // once. Use a linked set for deterministic iteration order.
        Set<IProject> projs = set();
        for (IFile file: files) {
            projs.add(file.getProject());
        }

        // Refresh each of the projects, and collect all failures.
        List<CoreException> failures = list();
        for (IProject proj: projs) {
            try {
                proj.refreshLocal(IResource.DEPTH_INFINITE, null);
            } catch (CoreException e) {
                failures.add(e);
            }
        }

        // Returns the failures, if any.
        return failures;
    }
}
