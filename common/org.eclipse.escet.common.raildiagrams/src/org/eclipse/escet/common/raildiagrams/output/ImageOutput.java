//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.output;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.eclipse.escet.common.raildiagrams.config.FontData;
import org.eclipse.escet.common.raildiagrams.config.TextSizeOffset;

/** Output class for generating PNG image files. */
public class ImageOutput extends OutputTarget {
    /** Graphics driver for text layout. */
    private Graphics2D textGd;

    /**
     * Constructor of the {@link ImageOutput} class.
     */
    public ImageOutput() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        textGd = image.createGraphics();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TextSizeOffset getTextSizeOffset(String text, FontData fontData) {
        return new TextSizeOffset(fontData.getTextOffset(textGd, text), fontData.getTextSize(textGd, text));
    }
}
