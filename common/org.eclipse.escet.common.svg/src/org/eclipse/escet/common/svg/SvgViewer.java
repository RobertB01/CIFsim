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

package org.eclipse.escet.common.svg;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.escet.common.app.framework.Paths;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.eclipse.ui.ControlEditor;
import org.eclipse.escet.common.eclipse.ui.MsgBox;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Exceptions;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;

/** SVG image viewer, implemented as an Eclipse based editor. */
public class SvgViewer extends ControlEditor {
    /**
     * Whether to show an initialization {@link #text} box until {@link #initDone} is invoked. If enabled, pre-rendered
     * images must be provided to the canvas, before {@link #initDone} is invoked, and may be updated afterwards. If
     * disabled, pre-rendered images are not used, and the canvas instead renders the image by itself.
     */
    private final boolean showInitText;

    /**
     * Whether to use the file name of the SVG file as the title of the editor. If {@code true}, the file name (without
     * path) is used as the title. Otherwise the name of the editor itself is used as title.
     */
    private final boolean showFileName;

    /**
     * The SVG document: the XML document of the SVG image to display on the {@link #canvas}. The document may be
     * modified in-place, but should always be locked (using the document itself as locking object) while modifying or
     * reading (includes painting) it, to prevent synchronization issues. The document is {@code null} until set by the
     * {@link #createContents} method.
     */
    private Document document;

    /**
     * The SVG canvas associated with this SVG editor. Is {@code null} until created by the {@link #createContents}
     * method.
     */
    private SvgCanvas canvas;

    /**
     * Text box used to show that {@link #showInitText initialization} is in progress, as well as to show SVG image
     * loading errors. Is {@code null} until created by the {@link #createContents} method.
     */
    private Text text;

    /**
     * The error that occurred while loading the SVG image, if any. Is {@code null} until the SVG image is loaded by the
     * {@link #createContents} method.
     */
    private SvgException svgLoadError;

    /**
     * Constructor for the {@link SvgViewer} class. Does not show the {@link #showInitText initialization}
     * {@link #text}. Does not forward SVG loading to the calling code, but only shows on them using a {@link #text} box
     * on the editor's GUI}. Uses the name of the SVG file (without path) as the {@link #showFileName title} of the
     * editor.
     *
     * <p>
     * Don't manually create instances of this class. Use the {@link ControlEditor#show} method to create and show the
     * SVG viewer. If opening the SVG viewer fails due to an error while loading the SVG image, the error can be
     * retrieved after the call to the {@link ControlEditor#show} method, by using the {@link #getSvgLoadError} method.
     * </p>
     */
    public SvgViewer() {
        this(false, true);
    }

    /**
     * Constructor for the {@link SvgViewer} class.
     *
     * <p>
     * Don't manually create instances of this class. Use the {@link ControlEditor#show} method to create and show the
     * SVG viewer. If opening the SVG viewer fails due to an error while loading the SVG image, the error can be
     * retrieved after the call to the {@link ControlEditor#show} method, by using the {@link #getSvgLoadError} method.
     * </p>
     *
     * @param showInitText Whether to show an initialization {@link #text} box until {@link #initDone} is invoked.
     * @param showFileName Whether to use the file name of the SVG file as the title of the editor. If {@code true}, the
     *     file name (without path) is used as the title. Otherwise the name of the editor itself is used as title.
     */
    public SvgViewer(boolean showInitText, boolean showFileName) {
        this.showInitText = showInitText;
        this.showFileName = showFileName;
    }

    /**
     * Returns the SVG document: the XML document of the SVG image to display on the {@link #canvas}. The document may
     * be modified in-place, but should always be locked (using the document itself as locking object) while modifying
     * or reading (includes painting) it, to prevent synchronization issues.
     *
     * @return The SVG document that is being displayed.
     * @throws IllegalStateException If the document is not yet available.
     */
    public Document getDocument() {
        if (document == null) {
            throw new IllegalStateException("Document not yet available.");
        }
        return document;
    }

    /**
     * Returns the SVG canvas associated with this SVG editor.
     *
     * @return The SVG canvas associated with this SVG editor.
     * @throws IllegalStateException If the canvas is not yet available.
     */
    public SvgCanvas getSvgCanvas() {
        if (canvas == null) {
            throw new IllegalStateException("Canvas not yet available.");
        }
        return canvas;
    }

    /**
     * Returns the error that occurred while loading the SVG image. Returns {@code null} if loading succeeded without
     * problems, or if loading has not yet completed.
     *
     * @return The error that occurred while loading the SVG image or {@code null}.
     */
    public SvgException getSvgLoadError() {
        return svgLoadError;
    }

    /**
     * Informs the SVG editor that initialization has finished, and the document may be painted onto the canvas.
     *
     * <p>
     * This method will hide the initialization {@link #text} box, and show the canvas with the image. When the canvas
     * becomes visible, it will automatically ask to be painted. As long as the canvas is not visible, it will never ask
     * to be painted, at least not by itself. Avoid calls to the {@link #redraw} method until this method has been
     * invoked.
     * </p>
     *
     * <p>
     * If the viewer has been closed, this request will be silently ignored.
     * </p>
     *
     * @throws IllegalStateException If {@link #showInitText} is {@code false} or the image failed to load.
     */
    public void initDone() {
        // Check the state.
        if (!showInitText) {
            throw new IllegalStateException("!showInitText");
        }
        if (canvas == null) {
            throw new IllegalStateException("no canvas");
        }

        // Hide initialization label, and show canvas.
        Assert.notNull(contents);
        Assert.notNull(canvas);
        Assert.notNull(text);
        final ScrolledComposite c1 = (ScrolledComposite)contents;
        final SvgCanvas c2 = canvas;
        final Text t = text;
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                // If the SVG viewer has been closed, skip this request. We
                // need to check this on the UI thread, so we can't do this
                // earlier.
                if (!SvgViewer.this.isAvailable()) {
                    return;
                }

                // Perform actual request.
                c1.setContent(c2);
                c2.setVisible(true);
                t.setVisible(false);
            }
        });
    }

    @Override
    protected Control createContents(Composite parent) {
        // Set editor title.
        String svgAbsPath = input.getAbsoluteFilePath();
        if (showFileName) {
            String fileName = svgAbsPath;
            int idx = Math.max(fileName.lastIndexOf('\\'), fileName.lastIndexOf('/'));
            if (idx != -1) {
                fileName = fileName.substring(idx + 1);
            }
            setPartName(fileName);
        }

        // Set background color of parent to white.
        Color white = parent.getDisplay().getSystemColor(SWT.COLOR_WHITE);
        parent.setBackground(white);
        parent.setLayout(new FormLayout());

        // Construct scrolled composite.
        ScrolledComposite scroll = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
        scroll.setBackground(white);
        FormData scrollData = new FormData();
        scrollData.left = new FormAttachment(0);
        scrollData.right = new FormAttachment(100);
        scrollData.top = new FormAttachment(0);
        scrollData.bottom = new FormAttachment(100);
        scroll.setLayoutData(scrollData);
        scroll.setLayout(new FormLayout());

        // Construct text box.
        text = new Text(scroll, SWT.MULTI | SWT.READ_ONLY);
        text.setText("Waiting for initialization to finish...");
        text.pack();
        text.setBackground(white);
        FormData textData = new FormData();
        textData.top = new FormAttachment(0);
        textData.left = new FormAttachment(0);
        textData.right = new FormAttachment(100);
        text.setLayoutData(textData);

        // Load SVG document from SVG file.
        try {
            document = SvgUtils.loadSvgFile(svgAbsPath);
        } catch (SvgException ex) {
            svgLoadError = ex;
        }

        // Show image, if loading succeeded. This builds the image, which may
        // fail as well.
        if (svgLoadError == null) {
            // Construct SVG canvas in scrolled composite.
            try {
                canvas = new SvgCanvas(scroll, document, showInitText);
            } catch (SvgException ex) {
                svgLoadError = ex;
            }
        }

        // Wrap load error to indicate SVG image it applies to.
        if (svgLoadError != null) {
            String msg = fmt("Failed to load SVG image \"%s\".", svgAbsPath);
            svgLoadError = new SvgException(msg, svgLoadError);
        }

        // Handle load failures.
        if (svgLoadError != null) {
            // Show the load error on the GUI.
            Color red = parent.getDisplay().getSystemColor(SWT.COLOR_RED);
            text.setText(Exceptions.exToStr(svgLoadError));
            text.pack();
            text.setForeground(red);
        }

        // Set initial visibility of text box and image (if available).
        if (svgLoadError == null) {
            // Loading succeeded, show the text box or the image.
            text.setVisible(showInitText);
            canvas.setVisible(!showInitText);
            scroll.setContent(showInitText ? text : canvas);
        } else {
            // Loading failed, and the text box shows the error.
            text.setVisible(true);
            scroll.setContent(text);
        }

        // Set up popup menu.
        if (svgLoadError == null) {
            Menu popupMenu = new Menu(parent);
            final MenuItem saveItem = new MenuItem(popupMenu, SWT.NONE);
            saveItem.setEnabled(isSaveAsAllowed());
            saveItem.setText("Save as...");

            scroll.setMenu(popupMenu);
            canvas.setMenu(popupMenu);

            popupMenu.addMenuListener(new MenuListener() {
                @Override
                public void menuShown(MenuEvent e) {
                    saveItem.setEnabled(isSaveAsAllowed());
                }

                @Override
                public void menuHidden(MenuEvent e) {
                    // Nothing to do here.
                }
            });

            saveItem.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    doSaveAs();
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                    widgetSelected(e);
                }
            });
        }

        // Return the contents of the editor. This is the scrolled composite,
        // which contains the SVG canvas and/or text box.
        return scroll;
    }

    @Override
    public boolean isSaveAsAllowed() {
        // Allow 'Save as' when the required data is available.
        return document != null && canvas != null && !canvas.isDisposed() && canvas.isVisible();
    }

    @Override
    public void doSaveAs() {
        // Get directory that contains the original SVG file.
        String path = input.getAbsoluteFilePath();
        int idx = Math.max(path.lastIndexOf('\\'), path.lastIndexOf('/'));
        Assert.check(idx > 0);
        path = path.substring(0, idx);

        // Determine suggested file name.
        String fileName = Paths.getFileName(input.getAbsoluteFilePath());
        fileName = Paths.pathChangeExtension(fileName, "svg", "viz.svg");

        // Set up the dialog.
        FileDialog fd = new FileDialog(contents.getShell(), SWT.SAVE);
        fd.setText("Save SVG as");
        fd.setFilterPath(path);
        fd.setFilterExtensions(new String[] {"*.svg;*.png;*.jpg;*.gif"});
        fd.setFileName(fileName);
        fd.setOverwrite(true);

        // Get save path using dialog. Dialog handles overwrites.
        String savePath = fd.open();

        // Check cancellation.
        if (savePath == null || savePath.trim().length() == 0) {
            return;
        }

        // Get file.
        File saveFile = new File(savePath);
        Assert.check(saveFile.isAbsolute());

        // Check valid file extension.
        if (!savePath.endsWith(".svg") && !savePath.endsWith(".png") && !savePath.endsWith(".jpg")
                && !savePath.endsWith(".gif"))
        {
            MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save SVG as",
                    "Invalid file extension: use .svg, .png, .jpg, or .gif.");
            return;
        }

        // Save image.
        if (savePath.endsWith(".svg")) {
            // Get transformer from Apache Xalan.
            TransformerFactory factory = TransformerFactory
                    .newInstance("org.apache.xalan.processor.TransformerFactoryImpl", null);

            Transformer transformer;
            try {
                transformer = factory.newTransformer();
            } catch (TransformerConfigurationException e) {
                MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save SVG as",
                        "Failed to save image." + Strings.NL + Strings.NL + Exceptions.exToStr(e));
                return;
            }

            // Get target stream.
            OutputStream tgtStream;
            try {
                tgtStream = new FileOutputStream(savePath);
            } catch (FileNotFoundException e) {
                String msg = fmt("Failed to open \"%s\" for writing.", savePath);
                throw new InputOutputException(msg, e);
            }
            tgtStream = new BufferedOutputStream(tgtStream);

            // Set source and target of transformation.
            //
            // Note that the target has no 'system id'; setting it is
            // problematic (see below). Not setting it could
            // potentially also lead to problems in the future.
            // However, we'll let it become a problem before
            // attempting to solve it.
            //
            // Problem:
            // Assume we want to save the contents of the
            // SVG viewer/visualizer to "/home/some/path/a b/c.svg".
            // This absolute file path is converted to URI
            // "file:/home/some/path/a%20b/c.svg" as a URI is required
            // by the javax.xml.transform.stream.StreamResult
            // constructor. The space is URL encoded to '%20'. The
            // Apache Xalan implementation simply strips the prefix to
            // get the absolute path. It does not decode the URI. The
            // result is that the directory can not be found, and we
            // thus get "Failed to save image." as error:
            // "ERROR: java.io.FileNotFoundException:
            // /home/some/path/a%20b/c.svg (No such file or directory)
            // CAUSE: /home/some/path/a%20b/c.svg (No such file or
            // directory)"
            // This is a known bug in Apache Xalan, see
            // https://issues.apache.org/jira/browse/XALANJ-2511 and
            // https://issues.apache.org/jira/browse/XALANJ-2461.
            // Once that bug is fixed, we can upgrade to the new
            // version, and saving SVG images is fixed as well.
            DOMSource src = new DOMSource(document);
            StreamResult tgt = new StreamResult(tgtStream);

            // Transform.
            try {
                synchronized (document) {
                    try {
                        transformer.transform(src, tgt);
                    } catch (TransformerException e) {
                        MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save SVG as",
                                "Failed to save image." + Strings.NL + Strings.NL + Exceptions.exToStr(e));
                        return;
                    }
                }
            } finally {
                try {
                    tgtStream.close();
                } catch (IOException e) {
                    String msg = fmt("Failed to close file \"%s\".", savePath);
                    throw new InputOutputException(msg, e);
                }
            }
        } else {
            // Save as raster image.
            try {
                getSvgCanvas().saveImage(savePath);
            } catch (NullPointerException e) {
                // Access denied (etc?) leads to NullPointerException
                // at javax.imageio.ImageIO.write(ImageIO.java:1523), due to
                // attempting to close a 'null' stream.
                MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save SVG as", "Failed to save image.");
                return;
            } catch (IOException e) {
                MsgBox.show(contents.getShell(), SWT.ICON_ERROR | SWT.OK, "Save SVG as",
                        "Failed to save image." + Strings.NL + Strings.NL + Exceptions.exToStr(e));
                return;
            }
        }

        // Refresh workspace, for the parent directory of the saved image file.
        // If the directory is not in the workspace, nothing is refreshed.
        if (Platform.isRunning() && PlatformUI.isWorkbenchRunning()) {
            URI saveDirUri = saveFile.getParentFile().toURI();
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IContainer[] dirs = root.findContainersForLocationURI(saveDirUri);
            for (IContainer dir: dirs) {
                try {
                    dir.refreshLocal(IResource.DEPTH_INFINITE, null);
                } catch (CoreException e) {
                    // Ignore refresh failures.
                }
            }
        }
    }
}
