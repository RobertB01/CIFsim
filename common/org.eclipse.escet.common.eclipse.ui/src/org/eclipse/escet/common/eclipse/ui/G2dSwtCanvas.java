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

package org.eclipse.escet.common.eclipse.ui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * G2D/SWT canvas. Provides a way to use AWT's {@link Graphics2D} to draw the contents of a SWT canvas. This class uses
 * no external libraries, is pixel-by-pixel compatible with the native {@link Graphics2D} output, and has high
 * performance.
 *
 * <p>
 * Internally, it allows for painting on a {@link Graphics2D} of a {@link BufferedImage}. After painting, the internal
 * AWT pixel data buffer is used to construct an SWT {@link ImageData}, which is drawn on the SWT canvas. One could say
 * that the pixels are directly transfered from AWT to SWT, resulting in high performance.
 * </p>
 */
public abstract class G2dSwtCanvas extends Canvas implements PaintListener, G2dSwtPainter {
    /** A value indicating whether the draw count should be painted onto the canvas. This can be used for debugging. */
    private static final boolean DRAW_PAINT_COUNT = false;

    /** The number of times that the {@link #paint} method has been invoked on this canvas. */
    private int paintCount = 0;

    /**
     * The additional painters (besides this canvas itself) that are called upon to paint the canvas, after the canvas
     * itself has painted.
     */
    private final List<G2dSwtPainter> painters = new CopyOnWriteArrayList<>();

    /**
     * Constructor for the {@link G2dSwtCanvas} class.
     *
     * <p>
     * Uses {@link SWT#NO_BACKGROUND} and {@link SWT#NO_REDRAW_RESIZE} as SWT styling bits.
     * </p>
     *
     * @param parent The parent of the G2D/SWT canvas.
     */
    public G2dSwtCanvas(Composite parent) {
        this(parent, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE);
    }

    /**
     * Constructor for the {@link G2dSwtCanvas} class.
     *
     * @param parent The parent of the G2D/SWT canvas.
     * @param style SWT styling bits for the SWT {@link Canvas}.
     */
    public G2dSwtCanvas(Composite parent, int style) {
        super(parent, style);
        addPaintListener(this);
    }

    /**
     * Adds an additional painter, used to paint the canvas.
     *
     * @param painter The additional painter to add.
     */
    public void addPainter(G2dSwtPainter painter) {
        painters.add(painter);
    }

    /**
     * Removes a painter that was previously added by using the {@link #addPainter} method.
     *
     * @param painter The painter to remove.
     */
    public void removePainter(G2dSwtPainter painter) {
        painters.remove(painter);
    }

    @Override
    public final void paintControl(PaintEvent e) {
        // Update paint count.
        paintCount++;

        // Get width and height in pixels.
        int w = getBounds().width;
        int h = getBounds().height;

        // Paint the image in-memory, and obtain the pixel data.
        byte[] imgData = getImageToPaint(w, h);

        // Construct SWT image from array, and draw it.
        PaletteData pal = new PaletteData(0xFF, 0xFF00, 0xFF0000);
        ImageData data = new ImageData(w, h, 24, pal, w * 3, imgData);
        Image img = new Image(getDisplay(), data);
        e.gc.drawImage(img, 0, 0);
        img.dispose();

        // Draw paint count. We don't dispose of the system color, as it was
        // allocated by the system, not the application.
        if (DRAW_PAINT_COUNT) {
            Color c = getDisplay().getSystemColor(SWT.COLOR_YELLOW);
            e.gc.setBackground(c);
            e.gc.drawString(String.valueOf(paintCount), 0, 0);
        }
    }

    /**
     * Returns the pixel data of the image to paint onto the control. The default implementation uses
     * {@link #paintInMemory} method.
     *
     * <p>
     * This method may be overridden, to for instance allow pre-rendering.
     * </p>
     *
     * @param width The width of the image, in pixels.
     * @param height The height of the image, in pixels.
     * @return The pixel data, in {@link BufferedImage#TYPE_3BYTE_BGR} format.
     */
    public byte[] getImageToPaint(int width, int height) {
        return paintInMemory(width, height);
    }

    /**
     * Paint in memory, to a buffered image, and return the pixel data. This method uses the {@link #paint} method to
     * paint the actual image, and allows additional {@link #painters} to paint over the image afterwards.
     *
     * @param width The width of the image, in pixels.
     * @param height The height of the image, in pixels.
     * @return The pixel data, in {@link BufferedImage#TYPE_3BYTE_BGR} format.
     */
    public byte[] paintInMemory(int width, int height) {
        // Construct buffered image, and obtain Graphics2D instance.
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = createGraphics(image);

        // Let the image be painted.
        paint(g, width, height);
        for (G2dSwtPainter painter: painters) {
            painter.paint(g, width, height);
        }
        g.dispose();

        // Get AWT image buffer data as an array.
        DataBuffer buf = image.getRaster().getDataBuffer();
        DataBufferByte bbuf = (DataBufferByte)buf;
        byte[] imgData = bbuf.getData();

        // Return pixel data of the image.
        return imgData;
    }

    /**
     * Create a {@link Graphics2D} from a {@link BufferedImage}. By default uses the
     * {@link BufferedImage#createGraphics()} method, but this may be changed by derived classes.
     *
     * @param image The buffered image.
     * @return The 2-dimensional graphics context.
     */
    protected Graphics2D createGraphics(BufferedImage image) {
        return image.createGraphics();
    }
}
