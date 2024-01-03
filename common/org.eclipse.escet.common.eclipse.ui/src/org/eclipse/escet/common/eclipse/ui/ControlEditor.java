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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.net.URI;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.ReflectionUtils;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.internal.ErrorEditorPart;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Eclipse editor part that contains a single {@link Control} as the editor's contents. If Eclipse is not running, it
 * falls back to a new SWT shell.
 *
 * <p>
 * As editor input, we support:
 * <ul>
 * <li>The standard Eclipse {@link FileEditorInput} class. This class wraps an Eclipse {@link IFile} resource. It is
 * used by default for files in the workspace.</li>
 * <li>The standard Eclipse {@link FileStoreEditorInput} class. This class provides us a {@link URI}. It is used by
 * default for files outside the workspace (drag/dropped from external applications such as file explorers, or opened
 * via 'File -> Open file...').</li>
 * <li>Our own {@link FilePathEditorInput} class, which wraps an absolute local file system path.</li>
 * </ul>
 * </p>
 */
@SuppressWarnings("restriction")
public abstract class ControlEditor extends EditorPart {
    /**
     * Whether to show the GUI for the editor. May be disabled for automated testing. Should only be set to
     * {@code false} by {@link #show}.
     */
    protected boolean showGui = true;

    /**
     * The editor input.
     *
     * @see FilePathEditorInput#getAbsoluteFilePath
     */
    protected FilePathEditorInput input;

    /** The control that represents the editor's contents. */
    protected Control contents;

    /** Indicates whether the editor has been closed, i.e., its {@link #contents} has been disposed. */
    private final AtomicBoolean contentsDisposed = new AtomicBoolean(false);

    /**
     * Implementation of the {@link EditorPart#init} method.
     *
     * <p>
     * Derived classes must use the {@link #createContents} method for initialization (as well as the usual creation of
     * the UI widgets), instead of this method. This is to get around Eclipse bug
     * <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=387475">#387475</a>.
     * </p>
     */
    @Override
    public final void init(IEditorSite site, IEditorInput input) {
        // Store site and input.
        setSite(site);
        setInput(input);

        // Store editor input locally as well.
        if (input instanceof FilePathEditorInput) {
            this.input = (FilePathEditorInput)input;
        } else if (input instanceof FileEditorInput) {
            FileEditorInput input2 = (FileEditorInput)input;
            String absPath = input2.getPath().toOSString();
            this.input = new FilePathEditorInput(absPath);
        } else if (input instanceof FileStoreEditorInput) {
            URI uri = ((FileStoreEditorInput)input).getURI();
            Assert.check(uri.isAbsolute());
            Assert.check(uri.getAuthority() == null);
            Assert.check(uri.getFragment() == null);
            Assert.check(uri.getHost() == null);
            Assert.check(uri.getPort() == -1);
            Assert.check(uri.getQuery() == null);
            Assert.check(uri.getScheme() != null);
            Assert.check(uri.getScheme().equals("file"));
            Assert.check(uri.getUserInfo() == null);
            String absPath = uri.getPath();
            this.input = new FilePathEditorInput(absPath);
        } else {
            throw new RuntimeException("Unknown editor input: " + input);
        }
    }

    @Override
    public final void createPartControl(Composite parent) {
        // The control editor only contains a single control.
        contents = createContents(parent);
        Assert.check(contents.getParent() == parent);

        contents.addDisposeListener(e -> {
            // Release all waiting threads.
            synchronized (contents) {
                contentsDisposed.set(true);
                contents.notifyAll();
            }
        });
    }

    /**
     * Creates and returns the control that represents the editor's content.
     *
     * <p>
     * This method may also be used by derived classes to perform initialization, and should at least include loading of
     * the editor {@link #input}. This should be done in this method instead of the normal initialization methods, to
     * get around Eclipse bug <a href="https://bugs.eclipse.org/bugs/show_bug.cgi?id=387475">#387475</a>.
     * </p>
     *
     * @param parent The {@link Composite} to use as the parent for the canvas to create.
     * @return The newly created control.
     */
    protected abstract Control createContents(Composite parent);

    @Override
    public void setFocus() {
        // Setting focus for the editor means setting focus for the control
        // that represents the content.
        if (contents != null) {
            contents.setFocus();
        }
    }

    @Override
    public boolean isDirty() {
        // By default, the editor is never dirty, which implies that the
        // "Save" action is never enabled in the IDE.
        return false;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        // By default, editors don't support "Save".
        throw new UnsupportedOperationException("Save is not supported.");
    }

    @Override
    public boolean isSaveAsAllowed() {
        // By default, editors don't support "Save As".
        return false;
    }

    @Override
    public void doSaveAs() {
        // By default, editors don't support "Save As".
        throw new UnsupportedOperationException("Save As is not supported.");
    }

    /**
     * Returns a value indicating whether the editor is available. An editor is available if the contents control has
     * been created, and has not been disposed.
     *
     * @return A value indicating whether the editor is available.
     */
    public boolean isAvailable() {
        return contents != null && !contents.isDisposed();
    }

    /** Redraws the editor's contents. */
    public void redraw() {
        final Control c = contents;
        if (c.isDisposed()) {
            return;
        }

        // TODO: asyncExec may give better performance than syncExec, but to
        // avoid issues, we use syncExec for now.
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                if (c.isDisposed()) {
                    return;
                }
                c.redraw(0, 0, c.getBounds().width, c.getBounds().height, true);
            }
        });
    }

    /**
     * Creates and shows a new instance of an editor. If Eclipse is running, then it asks the current workbench page to
     * open the editor. Otherwise, it falls back to creating a new SWT shell to display the editor in.
     *
     * @param <T> The class type of the editor.
     * @param filePath The path to the file to open in the editor. May be an absolute or relative local file system
     *     path. It is resolved against the current working directory of the application.
     * @param cls The class of the editor to create an instance of. If Eclipse is running, the full name of the class is
     *     used as Eclipse editor id for the editor to open. It must be the same id that is used to identify the editor
     *     in a plugin.xml file.
     * @param functionality A text indicating the functionality that requires the GUI to display the editor. See also
     *     {@link AppEnv#checkGuiAvailable(String)}.
     * @return The newly created editor.
     *
     * @see Paths#getCurWorkingDir
     */
    public static <T extends ControlEditor> T show(String filePath, final Class<T> cls, String functionality) {
        return show(filePath, cls, functionality, true);
    }

    /**
     * Creates and shows a new instance of an editor. If the GUI is not to be shown, a dummy SWT shell and editor are
     * created. If the GUI is to be shown, and if Eclipse is running, it asks the current workbench page to open the
     * editor. Otherwise, it falls back to creating a new SWT shell to display the editor in.
     *
     * @param <T> The class type of the editor.
     * @param filePath The path to the file to open in the editor. May be an absolute or relative local file system
     *     path. It is resolved against the current working directory of the application.
     * @param cls The class of the editor to create an instance of. If Eclipse is running, the full name of the class is
     *     used as Eclipse editor id for the editor to open. It must be the same id that is used to identify the editor
     *     in a plugin.xml file.
     * @param functionality A text indicating the functionality that requires the GUI to display the editor. See also
     *     {@link AppEnv#checkGuiAvailable(String)}.
     * @param showGui Whether to show the GUI for the editor. May be disabled for automated testing.
     * @return The newly created editor.
     *
     * @see Paths#getCurWorkingDir
     */
    public static <T extends ControlEditor> T show(String filePath, final Class<T> cls, String functionality,
            final boolean showGui)
    {
        // Make sure GUI is available.
        AppEnv.checkGuiAvailable(functionality);

        // Create and open new editor in GUI thread.
        final IEditorPart[] rslt = {null};
        final Throwable[] error = {null};
        final Display display = Display.getDefault();
        final String absFilePath = Paths.resolve(filePath);
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                if (!showGui) {
                    // Create invisible shell, and editor.
                    if (display.isDisposed()) {
                        return;
                    }
                    Shell shell = new Shell(display);
                    shell.setVisible(false);

                    T editor;
                    try {
                        editor = cls.getDeclaredConstructor().newInstance();
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to create editor.", e);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Failed to create editor.", e);
                    } catch (SecurityException e) {
                        throw new RuntimeException("Failed to create editor.", e);
                    }

                    editor.init(new DummyEditorSite(), new FilePathEditorInput(absFilePath));

                    try {
                        editor.createPartControl(shell);
                    } catch (Throwable e) {
                        error[0] = e;
                    }

                    // Disable GUI for the editor.
                    editor.showGui = false;

                    // Return the editor.
                    rslt[0] = editor;
                } else if (Platform.isRunning() && PlatformUI.isWorkbenchRunning()) {
                    // Get active workbench page to create editor in.
                    IWorkbenchWindow dw = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    Assert.notNull(dw);
                    IWorkbenchPage page = dw.getActivePage();
                    Assert.notNull(page);

                    // Create new editor part.
                    IEditorInput input = new FilePathEditorInput(absFilePath);
                    try {
                        rslt[0] = page.openEditor(input, cls.getName());
                    } catch (PartInitException e) {
                        error[0] = e;
                    } catch (NullPointerException e) {
                        // We should not have to catch this, as it should not
                        // occur. Hacky temporary workaround for:
                        // https://bugs.eclipse.org/bugs/show_bug.cgi?id=387475
                    } catch (Throwable e) {
                        error[0] = e;
                    }
                } else {
                    // Create new shell, and editor, and display it.
                    Shell shell = new Shell(display);
                    shell.setLayout(new FillLayout());
                    shell.setText(absFilePath);

                    ControlEditor editor;
                    try {
                        editor = cls.getDeclaredConstructor().newInstance();
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException("Failed to create editor.", e);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Failed to create editor.", e);
                    } catch (SecurityException e) {
                        throw new RuntimeException("Failed to create editor.", e);
                    }

                    editor.init(new DummyEditorSite(), new FilePathEditorInput(absFilePath));

                    try {
                        editor.createPartControl(shell);
                        editor.setFocus();
                        shell.open();
                        rslt[0] = editor;
                    } catch (Throwable e) {
                        error[0] = e;
                    }
                }
            }
        });

        // NOTE: 'rslt[0] == null' should never be possible, but it is. See
        // also https://bugs.eclipse.org/bugs/show_bug.cgi?id=387475
        if (rslt[0] == null || !cls.isInstance(rslt[0])) {
            if (error[0] == null) {
                error[0] = getErrorFromEditorPart(rslt[0]);
            }
        }

        // Propagate error.
        if (error[0] != null) {
            String msg = fmt("Failed to open editor \"%s\" for file \"%s\".", cls.getName(), filePath);
            throw new InvalidInputException(msg, error[0]);
        }

        // Return the editor.
        @SuppressWarnings("unchecked")
        T editor = (T)rslt[0];
        return editor;
    }

    /**
     * Returns the exception contained in the error status of the editor part, if it is an {@link ErrorEditorPart}, and
     * {@code null} otherwise.
     *
     * @param part The editor part to return the exception for.
     * @return The exception contained in the error status of the editor part, or {@code null}.
     */
    protected static Throwable getErrorFromEditorPart(IEditorPart part) {
        // Is there even an error?
        if (!(part instanceof ErrorEditorPart)) {
            return null;
        }

        // Get the error status from the error editor part. It is a private
        // variable, without getter, so use reflection.
        IStatus s = ReflectionUtils.getFieldValue(part, "error");

        // Return the exception from the error status.
        return s.getException();
    }

    /** Close this editor. If the editor is already closed, does nothing. */
    public void close() {
        if (!isAvailable()) {
            return;
        }

        final ControlEditor editor = this;
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                if (!editor.isAvailable()) {
                    return;
                }

                // Editor is still available. Close it.
                if (!showGui) {
                    editor.contents.getShell().close();
                } else if (Platform.isRunning() && PlatformUI.isWorkbenchRunning()) {
                    boolean rslt = getSite().getPage().closeEditor(editor, false);
                    Assert.check(rslt);
                } else {
                    editor.contents.getShell().close();
                }
            }
        });
    }

    /** Causes the caller to wait until the editor is closed. */
    public void waitUntilClosed() {
        if (contents == null) {
            return;
        }
        synchronized (contents) {
            if (contentsDisposed.get()) {
                return;
            }

            try {
                contents.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
