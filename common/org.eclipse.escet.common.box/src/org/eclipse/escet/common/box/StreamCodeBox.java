//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import java.io.Closeable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.escet.common.app.framework.io.AppStream;

/**
 * Box that supports indented code blocks, and writes their lines directly to a stream, to avoid having to keep them in
 * memory. Since the lines of code are not kept in memory, the {@link #getLines} method is not supported.
 */
public class StreamCodeBox extends CodeBox implements Closeable {
    /** The stream to which to write the lines of code. */
    private final AppStream stream;

    /** Is the code box empty? */
    private boolean empty = true;

    /**
     * Constructor for the {@link StreamCodeBox} class, with an indentation amount of 4, meaning each indentation level
     * adds 4 more spaces at the beginning of each code line.
     *
     * @param stream The stream to which to write the lines of code.
     */
    public StreamCodeBox(AppStream stream) {
        this(stream, 4);
    }

    /**
     * Constructor for the {@link StreamCodeBox} class, with a custom indentation amount.
     *
     * @param stream The stream to which to write the lines of code.
     * @param indentAmount The indentation amount, the amount of spaces to indent per indentation level. Must be a
     *     positive value.
     * @throws IllegalArgumentException If the given indentation amount is not a positive value.
     */
    public StreamCodeBox(AppStream stream, int indentAmount) {
        super(indentAmount);
        this.stream = stream;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    protected void addLine(String line) {
        empty = false;
        line = StringUtils.stripEnd(line, null);
        stream.println(line);
    }

    @Override
    public List<String> getLines() {
        throw new UnsupportedOperationException();
    }

    /** Closes the underlying stream. */
    @Override
    public void close() {
        stream.close();
    }
}
