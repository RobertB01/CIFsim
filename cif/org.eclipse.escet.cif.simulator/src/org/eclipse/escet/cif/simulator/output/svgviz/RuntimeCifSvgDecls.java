//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.output.svgviz;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Set;

import org.apache.batik.bridge.BridgeContext;
import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.escet.cif.simulator.CifSimulatorContext;
import org.eclipse.escet.cif.simulator.output.DebugOutputType;
import org.eclipse.escet.cif.simulator.runtime.CifSimulatorMath;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.svg.SvgCanvas;
import org.eclipse.escet.common.svg.SvgUtils;
import org.w3c.dom.Document;

/** Runtime representation of CIF/SVG declarations. */
public abstract class RuntimeCifSvgDecls {
    /**
     * The SVG document: the XML document of the SVG image. Is {@code null} until set by the {@link #init} method.
     *
     * @see SvgCanvas#getDocument
     */
    protected Document document;

    /**
     * The bridge context, which links the SVG document and the graphical nodes. Is {@code null} until set by the
     * {@link #init} method.
     *
     * @see SvgCanvas#getBridgeContext
     */
    protected BridgeContext bridgeContext;

    /** Whether to debug the CIF/SVG declarations. Is {@code false} until set by the {@link #init} method. */
    public boolean debug;

    /** The stream to write all debug output to. Is {@code null} until set by the {@link #init} method. */
    public AppStream dbg;

    /**
     * Returns the local file system path to the SVG image file to use for the SVG visualization, relative to the
     * directory that contains the CIF specification being simulated.
     *
     * <p>
     * May not be a valid relative path if previously compiled code is used. Use this path for shorter/readable error
     * messages only.
     * </p>
     *
     * @return The local file system path to the SVG image file to use for the SVG visualization, relative to the
     *     directory that contains the CIF specification being simulated.
     */
    public abstract String getSvgRelPath();

    /**
     * Returns the absolute local file system path to the SVG image file to use for the SVG visualization.
     *
     * @return The absolute local file system path to the SVG image file to use for the SVG visualization.
     */
    public abstract String getSvgAbsPath();

    /**
     * Initializes the CIF/SVG declarations, by caching as much data as possible, to speed up the simulation. Also
     * stores the document for later use.
     *
     * @param document The SVG document: the XML document of the SVG image.
     * @param bridgeContext The bridge context, which links the SVG document and the graphical nodes.
     * @param ctxt The simulator runtime context.
     */
    public void init(Document document, BridgeContext bridgeContext, CifSimulatorContext ctxt) {
        this.document = document;
        this.bridgeContext = bridgeContext;
        this.debug = ctxt.debug.contains(DebugOutputType.SVG);
        this.dbg = ctxt.appEnvData.getStreams().out;
        applyCopies();
        applyMoves();
        initCaches();
    }

    /** Updates the SVG document, by applying the CIF/SVG copy declarations. Should use {@link #applyCopy(int)}. */
    protected void applyCopies() {
        // Nothing to do if no copy declarations.
        int count = getCopyCount();
        if (count == 0) {
            return;
        }

        // No copy declaration has been applied yet.
        boolean[] applied = new boolean[count];
        while (true) {
            boolean progress = false;

            for (int i = 0; i < count; i++) {
                // Skip if already applied.
                if (applied[i]) {
                    continue;
                }

                // Apply the copy.
                boolean success = applyCopy(i);
                if (success) {
                    applied[i] = true;
                    progress = true;
                }
            }

            if (!progress) {
                break;
            }
        }

        // All copies should have been applied.
        Assert.check(!ArrayUtils.contains(applied, false));

        // Print empty line on debug output.
        if (debug) {
            dbg.println();
        }
    }

    /** Updates the SVG document, by applying the CIF/SVG move declarations. Should use {@link #applyMove}. */
    protected abstract void applyMoves();

    /**
     * Returns the number of copy declarations.
     *
     * @return The number of copy declarations.
     */
    protected abstract int getCopyCount();

    /**
     * Applies an SVG copy declaration.
     *
     * @param idx The 0-based index of the SVG copy declaration to apply.
     * @return Whether the copy was successfully applied ({@code true}), or it failed due to the element to copy not yet
     *     being present ({@code false}). If {@code false}, the caller should retry this copy later on, after other
     *     copies have created the element to copy.
     * @see SvgUtils#copy
     */
    protected abstract boolean applyCopy(int idx);

    /**
     * Applies an SVG copy declaration.
     *
     * @param id The element id of the root element of the (sub-)tree to copy.
     * @param pre The prefix to prepend to the ids of the elements of the copy of the (sub-)tree.
     * @param post The postfix to append to the ids of the elements of the copy of the (sub-)tree.
     * @return Whether the copy was successfully applied ({@code true}), or it failed due to the element to copy not yet
     *     being present ({@code false}). If {@code false}, the caller should retry this copy later on, after other
     *     copies have created the element to copy.
     * @see SvgUtils#copy
     */
    protected boolean applyCopy(String id, String pre, String post) {
        boolean doDebug = true;
        try {
            // Apply mapping.
            boolean copied = SvgUtils.copy(document, id, pre, post, getSvgRelPath(), false);

            // If not actually copied, don't print the debug output. Only print
            // it once we apply it again, and it succeeds.
            if (!copied) {
                doDebug = false;
            }

            // Inform caller of success.
            return copied;
        } finally {
            // If debugging is enabled, print debug output. We do this both for
            // successfully applied copies, as well as for copies that resulted
            // in exceptions. However, if we failed to apply the copy due to
            // a missing element (see above), don't print the debug output yet,
            // as we'll do it later, once the element exists.
            if (debug && doDebug) {
                String preTxt = pre.isEmpty() ? "" : fmt(" pre \"%s\"", pre);
                String postTxt = post.isEmpty() ? "" : fmt(" post \"%s\"", post);
                dbg.println(fmt("SVG copy (\"%s\") id \"%s\"%s%s.", getSvgRelPath(), id, preTxt, postTxt));
            }
        }
    }

    /**
     * Applies an SVG move declaration.
     *
     * @param id The element id of the element to move.
     * @param x The target x coordinate of upper left corner of the bounding rectangle of the graphical representation
     *     of the element, relative to the upper left corner of the canvas.
     * @param y The target y coordinate of upper left corner of the bounding rectangle of the graphical representation
     *     of the element, relative to the upper left corner of the canvas.
     */
    protected void applyMove(String id, double x, double y) {
        if (debug) {
            dbg.println(fmt("SVG move (\"%s\") id \"%s\" to %s, %s.", getSvgRelPath(), id,
                    CifSimulatorMath.realToStr(x), CifSimulatorMath.realToStr(y)));
        }

        SvgUtils.move(document, bridgeContext, id, x, y, getSvgRelPath());
    }

    /**
     * Initializes the CIF/SVG mappings, by caching as much data as possible, to speed up the simulation.
     *
     * @see #document
     */
    protected abstract void initCaches();

    /**
     * Updates the SVG document to the given state, by applying the CIF/SVG output mappings.
     *
     * @param state The state to use to update the SVG document.
     */
    public void applyOutput(RuntimeState state) {
        synchronized (document) {
            applyOutputInternal(state);
        }
    }

    /**
     * Updates the SVG document to the given state, by applying the CIF/SVG output mappings. There is no need to lock
     * the SVG document, as the {@link #applyOutput} method takes care of this.
     *
     * @param state The state to use to update the SVG document.
     */
    protected abstract void applyOutputInternal(RuntimeState state);

    /**
     * Returns the ids of the interactive SVG elements.
     *
     * @return The ids of the interactive SVG elements.
     */
    public abstract Set<String> getInteractiveIds();

    /**
     * Returns for each event in the specification, whether it can be chosen by clicking on an interactive SVG element.
     *
     * @return For each event in the specification, whether it can be chosen by clicking on an interactive SVG element.
     */
    public abstract boolean[] getInteractiveEvents();

    /**
     * Maps an SVG element id to a 0-based event index, by applying the corresponding CIF/SVG input mapping.
     *
     * @param id The SVG element id.
     * @param state The state to use to evaluate the input mapping.
     * @return The 0-based event index of the event resulting from the mapping.
     */
    public abstract int applyInput(String id, RuntimeState state);
}
