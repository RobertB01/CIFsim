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

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.exceptions.InputOutputException;

/**
 * Box that supports indented code blocks, and writes their lines directly to an {@link OutputStream}, to avoid having
 * to keep them in memory.
 *
 * <p>
 * Since the lines of code are not kept in memory, the {@link #getLines} method is not supported.
 * </p>
 *
 * <p>
 * This class uses UTF-8 encoding and platform-specific newline characters.
 * </p>
 */
public class OutputStreamCodeBox extends CodeBox implements Closeable {
    /** The bytes to write for a platform-specific newline. */
    private static final byte[] NL_BYTES = Strings.NL.getBytes(StandardCharsets.UTF_8);

    /** The output stream to which to write the lines of code. */
    private final OutputStream stream;

    /** A description of the stream, for use in I/O error messages. Is used as a part of a sentence. */
    private final String description;

    /** Is the code box empty? */
    private boolean empty = true;

    /**
     * Constructor for the {@link OutputStreamCodeBox} class, with an indentation amount of 4, meaning each indentation
     * level adds 4 more spaces at the beginning of each code line.
     *
     * @param stream The output stream to which to write the lines of code.
     * @param description A description of the stream, for use in I/O error messages. Is used as a part of a sentence.
     */
    public OutputStreamCodeBox(OutputStream stream, String description) {
        this(stream, description, 4);
    }

    /**
     * Constructor for the {@link OutputStreamCodeBox} class, with a custom indentation amount.
     *
     * @param stream The output stream to which to write the lines of code.
     * @param description A description of the stream, for use in I/O error messages. Is used as a part of a sentence.
     * @param indentAmount The indentation amount, the amount of spaces to indent per indentation level. Must be a
     *     positive value.
     * @throws IllegalArgumentException If the given indentation amount is not a positive value.
     */
    public OutputStreamCodeBox(OutputStream stream, String description, int indentAmount) {
        super(indentAmount);
        this.stream = stream;
        this.description = description;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    protected void addLine(String line) {
        empty = false;
        line = StringUtils.stripEnd(line, null);
        byte[] bytes = line.getBytes(StandardCharsets.UTF_8);
        try {
            stream.write(bytes);
            stream.write(NL_BYTES);
        } catch (IOException ex) {
            String msg = fmt("Failed to write to %s.", description);
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    public List<String> getLines() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {
        try {
            stream.close();
        } catch (IOException ex) {
            String msg = fmt("Failed to close %s.", description);
            throw new InputOutputException(msg, ex);
        }
    }
}
