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

/** Elementary box with a single line of text. */
public class TextBox extends Box {
    /** Text of the box. Must <strong>not</strong> contain new line characters. */
    private final String line;

    /**
     * Constructor for the {@link TextBox} class, for a single line of text.
     *
     * @param line Text of the box. It must <strong>not</strong> contain new line characters.
     */
    public TextBox(String line) {
        this.line = line;
    }

    /**
     * Constructor for the {@link TextBox} class, for a single line of formatted text.
     *
     * @param lineFormat Text format pattern to use. It must <strong>not</strong> contain new line characters.
     * @param args The arguments to use for formatting. Their textual representations must <strong>not</strong> contain
     *     new line characters.
     */
    public TextBox(String lineFormat, Object... args) {
        this(fmt(lineFormat, args));
    }

    @Override
    public List<String> getLines() {
        return list(line);
    }
}
