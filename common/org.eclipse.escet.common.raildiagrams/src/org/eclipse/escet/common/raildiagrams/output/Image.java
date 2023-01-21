//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022, 2023 Contributors to the Eclipse Foundation
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.eclipse.escet.common.java.Assert;

/** Class representing an image with direct pixel access. */
public class Image {
    /** Pixel data of the {@link #image}, an ARGB top-down sequence of horizontal lines. */
    public final int[] pixels;

    /** Image providing the {@link #pixels} array. */
    public final BufferedImage image;

    /** Width of the image. */
    public final int width;

    /** Height of the image. */
    public final int height;

    /**
     * Constructor of the {@link Image} class.
     *
     * @param width Width of the image.
     * @param height Height of the image.
     */
    public Image(int width, int height) {
        Assert.check(width >= 0 && height >= 0);
        this.width = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        DataBuffer buffer = image.getRaster().getDataBuffer();
        Assert.check(buffer.getDataType() == DataBuffer.TYPE_INT);
        int[] offsets = buffer.getOffsets();
        Assert.check(offsets.length == 1);
        Assert.check(offsets[0] == 0);

        DataBufferInt dbInt = (DataBufferInt)buffer;
        pixels = dbInt.getData();
    }

    /**
     * Construct a graphics handle for the image.
     *
     * @return The configured handle.
     */
    public Graphics2D getGraphics() {
        return getGraphics(image);
    }

    /**
     * Fill the entire image with the given ARGB color.
     *
     * @param argb Color to use for filling.
     */
    public void fill(int argb) {
        Arrays.fill(pixels, argb);
    }

    /**
     * Fill the entire image with the given color.
     *
     * @param col Color to use for filling.
     */
    public void fill(Color col) {
        fill(col.getRGB());
    }

    /**
     * Compute the index into {@link #pixels} for accessing a pixel from the given x and y values.
     *
     * @param x X coordinate of the pixel to access.
     * @param y Y coordinate of the pixel to access.
     * @return Storage index of the pixel in {@link #pixels}.
     */
    public int getIndex(int x, int y) {
        Assert.check(x >= 0 && x < width);
        Assert.check(y >= 0 && y < height);
        return x + y * width;
    }

    /**
     * Save the image to a PNG file at the indicated path.
     *
     * @param path Destination of the file.
     * @throws IOException In case of an I/O error.
     */
    public void saveImage(Path path) throws IOException {
        try {
            ImageIO.write(image, "png", path.toFile());
        } catch (IOException ex) {
            String msg = fmt("Failed to write PNG image file \"%s\".", path);
            throw new IOException(msg, ex);
        }
    }

    /**
     * Construct a graphics handle for a buffer and configure it.
     *
     * @param image Buffer that needs a graphics handle.
     * @return The configured handle.
     */
    public static Graphics2D getGraphics(BufferedImage image) {
        Graphics2D gd = image.createGraphics();
        gd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return gd;
    }
}
