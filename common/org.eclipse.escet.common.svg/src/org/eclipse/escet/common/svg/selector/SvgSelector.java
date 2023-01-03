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

package org.eclipse.escet.common.svg.selector;

import static org.eclipse.escet.common.java.Pair.pair;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.batik.gvt.GraphicsNode;
import org.eclipse.escet.common.eclipse.ui.G2dSwtPainter;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Pair;
import org.eclipse.escet.common.svg.SvgCanvas;
import org.eclipse.escet.common.svg.SvgUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.Element;

/**
 * SVG selector. Can be used in combination with an {@link SvgCanvas}, to let the user hover over SVG elements, and
 * click on them. When the user hovers over an interactive SVG element, its {@link #hoverShape outline} is highlighted,
 * to indicate that the SVG element is interactive. By clicking on such an interactive element, the user selects it, and
 * the id of the interactive SVG element is put into a {@link #idQueue queue}, for further processing (outside the SVG
 * selector).
 */
public class SvgSelector implements MouseListener, MouseMoveListener, G2dSwtPainter {
    /** Whether to output internal/developer debugging information. */
    private static final boolean DEBUG = false;

    /** The canvas to use for interaction with the user. */
    protected final SvgCanvas canvas;

    /**
     * The queue of SVG element ids of the interactive SVG elements on which the user has clicked, but that have not yet
     * been processed further. Each entry also includes the SVG selector that added it, to allow multiple selectors to
     * share the same queue. For thread-safety, {@link ConcurrentLinkedQueue} instances are used. Is modified in-place.
     */
    public final Queue<Pair<SvgSelector, String>> idQueue;

    /** The ids of the interactive SVG elements. */
    private final Set<String> interactiveIds;

    /**
     * Queue of mouse events to be processed. For thread-safety, {@link ConcurrentLinkedQueue} instances are used. Is
     * modified in-place.
     */
    private final Queue<MouseEventWrapper> mouseEventQueue = new ConcurrentLinkedQueue<>();

    /** The currently hovered interactive graphics node, or {@code null} if not available or not applicable. */
    private GraphicsNode hoverNode = null;

    /** The id of the currently hovered interactive SVG element, or {@code null} if not available or not applicable. */
    private String hoverId = null;

    /**
     * The current hover shape, representing the outline of the interactive SVG element over which the mouse pointer
     * currently hovers. The value is {@code null} if the mouse is outside of the canvas, hovers the background of the
     * canvas, or if the mouse hovers a non-interactive element.
     */
    private Shape hoverShape = null;

    /**
     * The transform to apply to the {@link #hoverShape hover shape}, or {@code null} if the hover shape is also
     * {@code null}.
     */
    private AffineTransform hoverTransform = null;

    /** Whether the user is currently still holding down the mouse button with which (s)he clicked the canvas. */
    private boolean mouseDown = false;

    /**
     * The last x-axis position of the mouse, relative to the SVG canvas, or {@link Integer#MIN_VALUE} if not
     * applicable.
     */
    private int lastX = Integer.MIN_VALUE;

    /**
     * The last y-axis position of the mouse, relative to the SVG canvas, or {@link Integer#MIN_VALUE} if not
     * applicable.
     */
    private int lastY = Integer.MIN_VALUE;

    /** The mouse cursor to use when hovering over an interactive element. */
    protected Cursor hoverCursor;

    /** The current mouse cursor, or {@code null} for the default. */
    protected Cursor curCursor;

    /**
     * Constructor for the {@link SvgSelector} class. Uses a fresh queue of SVG element ids.
     *
     * @param canvas The canvas to use for interaction with the user.
     * @param interactiveIds The ids of the interactive SVG elements.
     */
    public SvgSelector(final SvgCanvas canvas, Set<String> interactiveIds) {
        this(canvas, interactiveIds, new ConcurrentLinkedQueue<Pair<SvgSelector, String>>());
    }

    /**
     * Constructor for the {@link SvgSelector} class. Allows using a shared queue of SVG element ids.
     *
     * @param canvas The canvas to use for interaction with the user.
     * @param interactiveIds The ids of the interactive SVG elements.
     * @param idQueue The queue of SVG element ids of the interactive SVG elements on which the user has clicked, but
     *     that have not yet been processed further. For thread-safety, {@link ConcurrentLinkedQueue} only instances are
     *     allowed.
     */
    public SvgSelector(final SvgCanvas canvas, Set<String> interactiveIds, Queue<Pair<SvgSelector, String>> idQueue) {
        // Store parameters.
        this.canvas = canvas;
        this.idQueue = idQueue;
        this.interactiveIds = interactiveIds;
        Assert.check(idQueue instanceof ConcurrentLinkedQueue);

        // Register call-back methods for mouse events. Also get the hover
        // cursor, and current cursor.
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                // If the canvas has been closed, there is no need to continue.
                if (canvas.isDisposed()) {
                    return;
                }

                // Get cursors.
                Display display = Display.getDefault();
                hoverCursor = display.getSystemCursor(SWT.CURSOR_HAND);
                curCursor = canvas.getCursor();

                // Register call-backs.
                canvas.addMouseListener(SvgSelector.this);
                canvas.addMouseMoveListener(SvgSelector.this);
                canvas.addPainter(SvgSelector.this);
            }
        });
    }

    @Override
    public void mouseDoubleClick(MouseEvent e) {
        // Nothing to do here.
    }

    @Override
    public void mouseDown(MouseEvent e) {
        if (DEBUG) {
            debug("Mouse down: " + e.x + ", " + e.y);
        }
        mouseEventQueue.add(new MouseEventWrapper(e, MouseEventType.DOWN));
    }

    @Override
    public void mouseUp(MouseEvent e) {
        if (DEBUG) {
            debug("Mouse up: " + e.x + ", " + e.y);
        }
        mouseEventQueue.add(new MouseEventWrapper(e, MouseEventType.UP));
    }

    @Override
    public void mouseMove(MouseEvent e) {
        if (DEBUG) {
            debug("Mouse move: " + e.x + ", " + e.y);
        }
        mouseEventQueue.add(new MouseEventWrapper(e, MouseEventType.MOVE));
    }

    /**
     * Processes the mouse event queue, updating the selector state to represent the current status.
     *
     * @param svgChanged Whether the SVG image might have been changed since the last call to this method ({@code true})
     *     or was definitely not changed since the last call ({@code false}).
     */
    public void processQueue(boolean svgChanged) {
        // Process entire queue, until it is empty.
        boolean hoverInfoUpToDate = false;
        boolean hoverShapeUpToDate = false;
        while (true) {
            // Get next mouse event. If none left, we are done.
            MouseEventWrapper event = mouseEventQueue.poll();
            if (event == null) {
                break;
            }

            // Process mouse event.
            int x = event.event.x;
            int y = event.event.y;
            updateHoverInfo(x, y);
            switch (event.type) {
                case DOWN:
                    // Mouse is down, so process the click.
                    mouseDown = true;
                    updateQueue();
                    hoverShapeUpToDate = false;
                    break;

                case UP:
                    // Mouse is up, and since we didn't update the hover shape
                    // while the mouse was down, update it now.
                    updateHoverShape();
                    hoverShapeUpToDate = true;
                    mouseDown = false;
                    break;

                case MOVE:
                    // Mouse moved, so update the hover shape. However, if the
                    // mouse is down, we don't update the hover shape, in order
                    // to keep the shape that we clicked while the mouse
                    // remains down.
                    if (!mouseDown) {
                        updateHoverShape();
                        hoverShapeUpToDate = true;
                    }
                    break;

                default:
                    throw new RuntimeException("Unexpected event: " + event);
            }

            lastX = x;
            lastY = y;
        }

        // If the SVG might have changed, we need to update the hover shape.
        // However, if the mouse is down, we don't update the hover shape, in
        // order to keep the shape that we clicked while the mouse remains
        // down. Also, if we know for sure that the hover shape is already up
        // to date, there is no reason to update it again.
        if (svgChanged && !mouseDown && !hoverShapeUpToDate) {
            if (!hoverInfoUpToDate) {
                updateHoverInfo(lastX, lastY);
            }
            updateHoverShape();
        }

        // Update the cursor, on the UI thread, if it actually changed.
        final Cursor newCursor = (hoverShape == null) ? null : hoverCursor;
        if (newCursor != curCursor) {
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    // If the canvas was closed, there is no need to update.
                    if (canvas.isDisposed()) {
                        return;
                    }

                    // Update actual cursor and cached current cursor.
                    canvas.setCursor(newCursor);
                    curCursor = newCursor;
                }
            });
        }
    }

    /**
     * Updates the hover info to the interactive SVG element over which we are current hovering (if any), based on the
     * current mouse position.
     *
     * @param x The x-axis position of the mouse, relative to the canvas.
     * @param y The y-axis position of the mouse, relative to the canvas.
     *
     * @see #hoverNode
     * @see #hoverId
     */
    private void updateHoverInfo(int x, int y) {
        // Initialization.
        GraphicsNode node = null;
        Element elem = null;
        String id = null;

        // Obtain the graphics node for the given coordinates.
        node = canvas.getGraphicsNode().nodeHitAt(new Point2D.Double(x, y));

        // Keep looking for an interactive graphics node, while traveling up
        // the graphics node hierarchy.
        while (node != null) {
            // Get the SVG element corresponding to the graphics node.
            elem = canvas.getBridgeContext().getElement(node);

            // Get the id of the SVG element. Note that the element may not
            // have an id.
            if (elem != null) {
                id = SvgUtils.getSvgElementId(elem);

                // If the id is one of the interactive SVG elements, we have
                // found an interactive graphics node at the given coordinates.
                if (id != null && interactiveIds.contains(id)) {
                    break;
                }
            }

            // Continue with parent.
            node = node.getParent();
        }

        // Update current hover info.
        if (node != null && elem != null && id != null) {
            // We are hovering an interactive SVG element.
            hoverNode = node;
            hoverId = id;
        } else {
            // We are not hovering an interactive SVG element.
            hoverNode = null;
            hoverId = null;
        }
    }

    /**
     * Updates the {@link #hoverShape} and {@link #hoverTransform} based on the current hover info. If the mouse does
     * not hover an interactive SVG element, the hover shape and transform will be cleared. Assumes that
     * {@link #updateHoverInfo} has been used to update the current hover info.
     */
    private void updateHoverShape() {
        if (hoverNode == null) {
            hoverShape = null;
            hoverTransform = null;
        } else {
            hoverShape = hoverNode.getOutline();
            hoverTransform = hoverNode.getGlobalTransform();
        }
    }

    /**
     * Updates the {@link #idQueue} with the id of the interactive SVG element on which the user clicked. If the user
     * did not click an interactive SVG element, this method has no visible effect. Assumes that
     * {@link #updateHoverInfo} has been used to update the current hover info.
     */
    private void updateQueue() {
        // If we are not hovering an interactive SVG element, ignore the click.
        if (hoverNode == null) {
            return;
        }

        // Add the id to the queue.
        idQueue.add(pair(this, hoverId));
    }

    @Override
    public void paint(Graphics2D g, int w, int h) {
        // If we have a hover shape, draw it.
        if (hoverShape != null) {
            // Set shape color, and apply the hover transform.
            g.setColor(mouseDown ? java.awt.Color.green : java.awt.Color.RED);
            g.transform(hoverTransform);

            // NOTE: The next line is a (temporary) workaround to avoid a thick
            // borders issue. The thick borders may be a Batik bug, but it is
            // just as likely that it is caused by our limited understanding
            // of the Batik API. See also this batik-users mailing list post:
            // http://mail-archives.apache.org/mod_mbox/xmlgraphics-batik-users/201108.mbox/<4E5B466B.2080304@tue.nl>
            g.setStroke(new BasicStroke(1.0f));

            // Draw the actual hover shape.
            g.draw(hoverShape);
        }
    }

    /**
     * Prints a line of internal/developer debug output. Should only be invoked if {@link #DEBUG} is {@code true}.
     *
     * @param txt The line of text to print.
     */
    private void debug(String txt) {
        System.out.println(getClass().getSimpleName() + ": " + txt);
    }
}
