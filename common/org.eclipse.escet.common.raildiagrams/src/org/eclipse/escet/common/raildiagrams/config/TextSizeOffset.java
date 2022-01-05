//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.raildiagrams.config;

import org.eclipse.escet.common.raildiagrams.util.Position2D;
import org.eclipse.escet.common.raildiagrams.util.Size2D;

/** Class holding size of a given text, and offset relative to top-left. */
public class TextSizeOffset {
    /** Offset of the text relative to its top-left corner. */
    public final Position2D offset;

    /** Size of the text. */
    public final Size2D size;

    /**
     * Constructor of the {@link TextSizeOffset} class.
     *
     * @param offset Offset of the text relative to its top-left corner.
     * @param size Size of the text.
     */
    public TextSizeOffset(Position2D offset, Size2D size) {
        this.offset = offset;
        this.size = size;
    }
}
