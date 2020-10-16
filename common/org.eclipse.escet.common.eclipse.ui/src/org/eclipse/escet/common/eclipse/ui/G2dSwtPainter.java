//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

/** Interface for painting call-backs in the context of the {@link G2dSwtCanvas}. */
public interface G2dSwtPainter {
    /**
     * Paints the contents of the {@link G2dSwtCanvas} that the given {@link Graphics2D} object was created for.
     *
     * @param g The {@link Graphics2D} to paint on.
     * @param width The width of the {@link Graphics2D} canvas.
     * @param height The height of the {@link Graphics2D} canvas.
     */
    void paint(Graphics2D g, int width, int height);
}
