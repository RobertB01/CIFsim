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

package org.eclipse.escet.common.svg;

import org.eclipse.escet.common.eclipse.ui.ControlEditor;

/**
 * SVG image visualizer, implemented as an Eclipse based editor.
 *
 * <p>
 * Unlike the {@link SvgViewer} class, this class is used to visualize SVG images for simulations. The differences with
 * the {@link SvgViewer} class are:
 * <ul>
 * <li>The SVG image is not shown until {@link #initDone} is called, ensuring that the simulator can modify the image to
 * correspond to the initial state of the model being simulated. An initialization text box is shown until the image
 * becomes visible.</li>
 * <li>The image is never automatically rendered by the canvas. Instead, rendered images should be provided to the
 * canvas.</li>
 * <li>The name of the editor is used as the title of the editor.</li>
 * </ul>
 * </p>
 */
public class SvgVisualizer extends SvgViewer {
    /**
     * Constructor for the {@link SvgVisualizer} class.
     *
     * <p>
     * Don't manually create instances of this class. Use the {@link ControlEditor#show} method to create and show the
     * SVG visualizer. If opening the SVG viewer fails due to an error while loading the SVG image, the error can be
     * retrieved after the call to the {@link ControlEditor#show} method, by using the {@link #getSvgLoadError} method.
     * </p>
     */
    public SvgVisualizer() {
        super(true, false);
    }
}
