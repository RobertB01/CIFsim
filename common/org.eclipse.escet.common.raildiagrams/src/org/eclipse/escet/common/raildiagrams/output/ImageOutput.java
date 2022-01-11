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

import static org.eclipse.escet.common.java.Strings.fmt;
import static org.eclipse.escet.common.raildiagrams.graphics.PaintSupport.getGraphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.escet.common.raildiagrams.config.FontData;
import org.eclipse.escet.common.raildiagrams.config.TextSizeOffset;

/** Base class for generating images. */
public abstract class ImageOutput extends OutputTarget {
    /** Graphics driver for text layout. */
    private Graphics2D textGd;

    /** Constructor of the {@link ImageOutput} class. */
    public ImageOutput() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        textGd = getGraphics(image);
    }

    @Override
    public TextSizeOffset getTextSizeOffset(String text, FontData fontData) {
        return new TextSizeOffset(fontData.getTextOffset(textGd, text), fontData.getTextSize(textGd, text));
    }

    /**
     * Save the given image to a PNG file at the indicated path.
     *
     * @param image Image to write.
     * @param path Destination of the file.
     */
    protected void saveImage(BufferedImage image, String path) {
        try {
            ImageIO.write(image, "png", new File(path));
        } catch (IOException ex) {
            String msg = fmt("Failed to write PNG image file \"%s\".", path);
            throw new RuntimeException(msg, ex);
        }
    }
}
