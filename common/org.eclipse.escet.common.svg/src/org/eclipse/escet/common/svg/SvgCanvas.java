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

package org.eclipse.escet.common.svg;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.BridgeException;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.ext.awt.image.GraphicsUtil;
import org.apache.batik.gvt.GraphicsNode;
import org.eclipse.core.runtime.Platform;
import org.eclipse.escet.common.eclipse.ui.G2dSwtCanvas;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.exceptions.InvalidInputException;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.w3c.dom.Document;

/** SVG canvas, used to display an SVG image (an SVG document). */
public class SvgCanvas extends G2dSwtCanvas {
    /**
     * The SVG document: the XML document of the SVG image to display on the canvas. The document may be modified
     * in-place, but should always be locked (using the document itself as locking object) while modifying or reading
     * (includes painting) it, to prevent synchronization issues.
     */
    private final Document document;

    /**
     * The graphics node that represents the SVG document. Is modified in-place by the bridge context, whenever the SVG
     * document is changed. Is not to be modified manually.
     */
    private final GraphicsNode graphicsNode;

    /** The bridge context that connects the SVG document and graphics node. */
    private final BridgeContext bridgeContext;

    /** The user agent to which the bridge context reports exceptions. */
    private final SvgUserAgent userAgent;

    /** The width of the SVG image, in pixels. */
    private int width;

    /** The height of the SVG image, in pixels. */
    private int height;

    /**
     * Whether to use {@link #pixelData} ({@code true}) or let the canvas render the SVG image by itself
     * ({@code false}).
     */
    public final boolean usePixelData;

    /** The lock object to use to synchronize the {@link #pixelData}. */
    public final Object pixelDataLock = new Object();

    /**
     * The current pixel data (image) to paint. Must be {@code null} if not used ({@code #usePixelData} is
     * {@code false}). May be {@code null} otherwise, if the image is not yet available (not yet rendered).
     *
     * <p>
     * All read and write access to this field must be synchronized using the {@link #pixelDataLock} lock object, to
     * prevent synchronization issues.
     * </p>
     */
    public byte[] pixelData = null;

    /**
     * The absolute or relative local file system path to the SVG image file from which the SVG document was loaded, or
     * {@code null} if not available.
     */
    public String path = null;

    /**
     * Constructor for the {@link SvgCanvas} class.
     *
     * @param parent The parent of the SVG canvas.
     * @param document The SVG document: the XML document of the SVG image to display on the canvas.
     * @param usePixelData Whether to use {@link #pixelData} ({@code true}) or let the canvas render the SVG image by
     *     itself ({@code false}).
     * @throws SvgException If the SVG image file can not be processed, because it is invalid. The caller should wrap
     *     this with a message that includes the path to the SVG image file.
     */
    public SvgCanvas(Composite parent, Document document, boolean usePixelData) {
        super(parent);
        this.document = document;
        this.usePixelData = usePixelData;

        // Construct user agent, bridge context, and graphics node.
        userAgent = new SvgUserAgent();
        bridgeContext = new BridgeContext(userAgent);
        bridgeContext.setDynamic(true);
        GVTBuilder builder = new GVTBuilder();
        try {
            graphicsNode = builder.build(bridgeContext, document);
        } catch (BridgeException ex) {
            userAgent.displayError(ex);
            try {
                userAgent.reportProblem();
                throw new RuntimeException("Never reached.");
            } catch (SvgException ex2) {
                String msg = "Failed to process SVG image file. The SVG image is not valid.";
                throw new SvgException(msg, ex2);
            }
        }

        // Get the dimensions.
        updateImageSize();
    }

    /**
     * Returns the SVG document: the XML document of the SVG image to display on the canvas. The document may be
     * modified in-place, but should always be locked (using the document itself as locking object) while modifying or
     * reading (includes painting) it, to prevent synchronization issues.
     *
     * @return The SVG document.
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Returns the graphics node that represents the SVG document. Is modified in-place by the bridge context, whenever
     * the SVG document is changed. Is not to be modified manually.
     *
     * @return The graphics node that represents the SVG document.
     */
    public GraphicsNode getGraphicsNode() {
        return graphicsNode;
    }

    /**
     * Returns the bridge context that connects the SVG document and graphics node. Is not to be modified manually.
     *
     * @return The bridge context that connects the SVG document and graphics node.
     */
    public BridgeContext getBridgeContext() {
        return bridgeContext;
    }

    /**
     * Returns the width of the SVG image, in pixels.
     *
     * @return The width of the SVG image, in pixels.
     */
    public int getImageWidth() {
        return width;
    }

    /**
     * Returns the height of the SVG image, in pixels.
     *
     * @return The height of the SVG image, in pixels.
     */
    public int getImageHeight() {
        return height;
    }

    @Override
    protected Graphics2D createGraphics(BufferedImage image) {
        // Let Batik create the 2-dimensional graphics context.
        return GraphicsUtil.createGraphics(image);
    }

    @Override
    public void paint(Graphics2D g, int w, int h) {
        // Enable anti-aliasing.
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw a white background.
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, w, h);

        // Paint the actual image. We make sure that painting the image and
        // modifying the image are interleaved.
        synchronized (document) {
            graphicsNode.paint(g);
        }

        // Check for a problem recorded by the user agent and report it.
        try {
            userAgent.reportProblem();
        } catch (SvgException ex) {
            String msg;
            if (path == null) {
                msg = "SVG image rendering failed.";
            } else {
                msg = fmt("SVG image rendering failed for file \"%s\".", path);
            }
            throw new SvgException(msg, ex);
        }
    }

    @Override
    public byte[] getImageToPaint(int width, int height) {
        // Use pre-rendered pixel data.
        if (usePixelData) {
            synchronized (pixelDataLock) {
                Assert.notNull(pixelData);
                return pixelData;
            }
        }

        // Render the SVG image by ourselves.
        return super.getImageToPaint(width, height);
    }

    /**
     * Saves the SVG image to a file.
     *
     * @param fileName The absolute local file system path of the file to save to. The file extension should be
     *     {@code ".png"}, {@code ".jpg"}, or {@code ".gif"}.
     * @throws IOException If saving the image file fails, or if the file extension is unknown or unsupported.
     */
    public void saveImage(String fileName) throws IOException {
        // Paint SVG image to a buffered image using Graphics2D.
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        paint(createGraphics(image), width, height);

        // Write image to file.
        File outFile = new File(fileName);
        String imgFormat = Strings.slice(fileName, -3, null);
        boolean rslt = ImageIO.write(image, imgFormat, outFile);
        if (!rslt) {
            String msg = fmt("Failed to save image to \"%s\": unknown or unsupported file extension.", fileName);
            throw new IOException(msg);
        }
    }

    /**
     * Updates the canvas to reflect the current size of the SVG image.
     *
     * <p>
     * If running standalone (outside of Eclipse), the client area of the {@link Shell} of the canvas is also resized to
     * be equal to the size of the canvas.
     * </p>
     *
     * <p>
     * This method may be invoked from any thread, including non-UI threads.
     * </p>
     *
     * @throws InvalidInputException If the SVG image width or height is not positive.
     */
    public void updateImageSize() {
        // Invoke on UI thread if current thread is not a UI thread.
        if (Display.getCurrent() == null) {
            final InvalidInputException[] ex = {null};
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    if (SvgCanvas.this.isDisposed()) {
                        return;
                    }
                    try {
                        SvgCanvas.this.updateImageSize();
                    } catch (InvalidInputException e) {
                        ex[0] = e;
                    }
                }
            });
            if (ex[0] != null) {
                throw ex[0];
            }
            return;
        }

        // Perform actual update.
        synchronized (document) {
            // Update image width/height.
            Dimension2D docsize = bridgeContext.getDocumentSize();
            width = (int)Math.ceil(docsize.getWidth());
            height = (int)Math.ceil(docsize.getHeight());

            // Check width/height.
            if (width <= 0) {
                String msg = fmt("SVG image width is %d, but must be positive.", width);
                throw new InvalidInputException(msg);
            }
            if (height <= 0) {
                String msg = fmt("SVG image height is %d, but must be positive.", height);
                throw new InvalidInputException(msg);
            }

            // Resize canvas.
            setSize(width, height);

            // Resize shell to the size of the image, if running outside of
            // Eclipse workbench.
            if (!Platform.isRunning() || !PlatformUI.isWorkbenchRunning()) {
                Shell shell = getShell();
                Rectangle bounds = shell.getBounds();
                Rectangle client = shell.getClientArea();
                int extraWidth = bounds.width - client.width;
                int extraHeight = bounds.height - client.height;
                int shellWidth = width + extraWidth;
                int shellHeight = height + extraHeight;
                shell.setSize(shellWidth, shellHeight);
            }
        }
    }
}
