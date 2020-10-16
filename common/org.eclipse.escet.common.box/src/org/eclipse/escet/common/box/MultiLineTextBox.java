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

package org.eclipse.escet.common.box;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.java.Assert;

/** Elementary box with lines of text. */
public class MultiLineTextBox extends Box {
    /** Text of the box. The individual lines must not contain new line characters. */
    private final List<String> lines;

    /**
     * Constructor for the {@link MultiLineTextBox} class, for a single line of text.
     *
     * @param text Text of the box. It may contain new line characters.
     */
    public MultiLineTextBox(String text) {
        this(text.split("\n"));
    }

    /**
     * Constructor for the {@link MultiLineTextBox} class, for a single line of formatted text.
     *
     * @param format Text format pattern to use. It may contain new line characters.
     * @param args The arguments to use for formatting.
     */
    public MultiLineTextBox(String format, Object... args) {
        this(fmt(format, args));
    }

    /**
     * Constructor for the {@link MultiLineTextBox} class, for several lines of text.
     *
     * @param lines Lines of text of the box. The individual lines may not contain new line characters.
     */
    public MultiLineTextBox(String[] lines) {
        this(list(lines));
    }

    /**
     * Constructor for the {@link MultiLineTextBox} class, for several lines of text.
     *
     * @param lines Lines of text of the box. The individual lines may not contain new line characters.
     */
    public MultiLineTextBox(List<String> lines) {
        Assert.notNull(lines);
        this.lines = lines;
    }

    @Override
    public List<String> getLines() {
        return lines;
    }
}
