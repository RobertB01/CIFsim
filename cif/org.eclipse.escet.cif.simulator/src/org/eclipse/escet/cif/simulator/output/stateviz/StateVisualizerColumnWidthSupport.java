//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output.stateviz;

import java.util.stream.IntStream;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;

/** {@link StateVisualizer} column width support. */
public class StateVisualizerColumnWidthSupport {
    /** The table column for which to provide width support. */
    private final TableColumn column;

    /** The in-memory image from which {@link #gc} was created. */
    private final Image image;

    /** The graphic context on which to measure text widths. */
    private final GC gc;

    /** Per ASCII character, its width in pixels, or {@code -1} if not yet measured. */
    private final int[] widths = IntStream.generate(() -> -1).limit(256).toArray();

    /** The current width of the column, in pixels. */
    private int curWidth;

    /**
     * New width of the column, in pixels. Value is only accurate after a call to {@link #initNewWidth} and until the
     * next call to {@link #applyNewWidth}.
     */
    private int newWidth = -1;

    /** The extra width of the column, beyond the header/cell texts, in pixels. Is {@code -1} if not yet initialized. */
    private int valueColumnExtraWidth = -1;

    /** Last {@link System#currentTimeMillis time} that the width of the column was changed. */
    private long lastWidthChangeTime = System.currentTimeMillis();

    /**
     * Constructor for the {@link StateVisualizerColumnWidthSupport} class.
     *
     * @param column The table column for which to provide width support.
     */
    public StateVisualizerColumnWidthSupport(TableColumn column) {
        this.column = column;
        curWidth = column.getWidth();
        image = new Image(column.getDisplay(), 1, 1);
        gc = new GC(image);
        gc.setFont(column.getParent().getFont());
    }

    /**
     * Get the width of the given text.
     *
     * @param text The text.
     * @return The width in pixels.
     */
    private int getTextWidth(String text) {
        // Measure the text width, one character at a time.
        int calc = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            Assert.check(c >= 0 && c < widths.length);
            int l = widths[c];
            if (l == -1) {
                l = gc.textExtent(String.valueOf(c)).x;
                widths[c] = l;
            }
            calc += l;
        }

        // Return the text width.
        // Add a few pixels, just in case per-character measurements deviate from 'entire text at once' measurements.
        return calc + 4;
    }

    /**
     * Sets a new width of the column.
     *
     * @param width The new width, in pixels.
     */
    private void setWidth(int width) {
        column.setWidth(width);
        curWidth = width;
        lastWidthChangeTime = System.currentTimeMillis();
    }

    /** Initialize the {@link #newWidth new column width} to the header text width. */
    public void initNewWidth() {
        newWidth = getTextWidth(column.getText());
    }

    /**
     * Update the {@link #newWidth new column width} for the width of the given cell text.
     *
     * @param cellText Cell text.
     */
    public void updateNewWidth(String cellText) {
        newWidth = Math.max(newWidth, getTextWidth(cellText));
    }

    /** Apply the {@link #newWidth new column width}, if applicable. */
    public void applyNewWidth() {
        // Auto size the column.
        //
        // Packing a table column has become very expensive since changes to SWT in February 2022:
        // https://github.com/eclipse-platform/eclipse.platform.swt/commit/9976faf68805ba9231fa58fe046a20c6ff00d56c.
        // As a workaround, the first time we let SWT pack the column. We then determine the extra width compared to
        // our own computed maximum width of the texts, to account for margins. We remember that extra width for
        // subsequent updates of the table values, where we avoid packing, and compute the table column width
        // ourselves, updating it if needed.
        if (valueColumnExtraWidth == -1) {
            column.pack();
            valueColumnExtraWidth = column.getWidth() - newWidth;
            valueColumnExtraWidth = Math.max(0, valueColumnExtraWidth); // Ensure non-negative extra width.
        } else {
            // Always increase column size if needed, to ensure we can read the cell texts. Only reduce the column
            // width after a delay, to prevent changing the width too often, for performance reasons.
            newWidth += valueColumnExtraWidth;
            if (newWidth > curWidth) {
                setWidth(newWidth);
            } else if (newWidth < curWidth && (System.currentTimeMillis() - lastWidthChangeTime) > 1000) {
                setWidth(newWidth);
            }
        }
    }

    /**
     * Disposes of the operating system resources associated with this resource. Applications must dispose of all
     * resources which they allocate. This method does nothing if the resource is already disposed.
     */
    public void dispose() {
        image.dispose();
        gc.dispose();
    }
}
