//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.util;

import static org.eclipse.escet.common.java.Strings.fmt;

/** Class for storing a size. */
public class Size2D {
    /** Width of the size. */
    public final int width;

    /** Height of the size. */
    public final int height;

    /**
     * Constructor of the {@link Size2D} class.
     *
     * @param width Width of the size.
     * @param height Height of the size.
     */
    public Size2D(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return fmt("Size2D(%d, %d)", width, height);
    }
}
