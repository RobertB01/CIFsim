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

package org.eclipse.escet.common.box;

import static org.eclipse.escet.common.java.Lists.list;

import java.util.List;

/** Box that supports indented code blocks, and holds their lines in memory. */
public class MemoryCodeBox extends CodeBox {
    /** The lines of code. Is modified in-place. */
    private final List<String> lines = list();

    /**
     * Constructor for the {@link MemoryCodeBox} class, with an indentation amount of 4, meaning each indentation level
     * adds 4 more spaces at the beginning of each code line.
     */
    public MemoryCodeBox() {
        this(4);
    }

    /**
     * Constructor for the {@link MemoryCodeBox} class, with a custom indentation amount.
     *
     * @param indentAmount The indentation amount, the amount of spaces to indent per indentation level. Must be a
     *     positive value.
     * @throws IllegalArgumentException If the given indentation amount is not a positive value.
     */
    public MemoryCodeBox(int indentAmount) {
        super(indentAmount);
    }

    @Override
    public boolean isEmpty() {
        return lines.isEmpty();
    }

    @Override
    protected void addLine(String line) {
        lines.add(line);
    }

    @Override
    public List<String> getLines() {
        return lines;
    }
}
