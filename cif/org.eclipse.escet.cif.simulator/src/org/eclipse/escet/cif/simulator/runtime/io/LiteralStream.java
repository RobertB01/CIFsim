//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.runtime.io;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.PositionTrackingInputStream;

/** Wrapper around {@link InputStream} with additional functionality. */
public class LiteralStream extends InputStream {
    /** UTF-8 {@link Charset}. */
    private static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    /** The wrapped stream. */
    private final InputStream stream;

    /**
     * Constructor for the {@link LiteralStream} class.
     *
     * @param loader The class loader to use to obtain the resource stream.
     * @param path The resource path.
     */
    public LiteralStream(ClassLoader loader, String path) {
        // Currently literal resource reading is only used internally, so there
        // is no need to add position tracking, as it only makes it more
        // expensive.
        stream = loader.getResourceAsStream(path);
        if (stream == null) {
            String msg = fmt("Failed to load literal resource \"%s\".", path);
            throw new InputOutputException(msg);
        }
    }

    /**
     * Constructor for the {@link LiteralStream} class.
     *
     * @param valueText The literal text from which to read the value.
     */
    public LiteralStream(String valueText) {
        byte[] bytes = valueText.getBytes(UTF8_CHARSET);
        InputStream bstream = new ByteArrayInputStream(bytes);
        stream = new PositionTrackingInputStream(bstream);
    }

    @Override
    public int read() {
        try {
            return stream.read();
        } catch (IOException ex) {
            String msg = "Failed to read literal data.";
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    public int read(byte[] b) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read(byte[] b, int off, int len) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long skip(long n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int available() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        try {
            stream.close();
        } catch (IOException ex) {
            String msg = fmt("Failed to close literal data stream.");
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    public synchronized void mark(int readlimit) {
        stream.mark(readlimit);
    }

    @Override
    public synchronized void reset() {
        try {
            stream.reset();
        } catch (IOException ex) {
            String msg = fmt("Failed to reset literal data stream.");
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    public boolean markSupported() {
        return stream.markSupported();
    }

    /**
     * Is the given character a whitespace character?
     *
     * @param c The character.
     * @return {@code true} if the character is a whitespace character, {@code false} otherwise.
     */
    public static boolean isWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    /** Skip white space characters in the input. */
    public void skipWhitespace() {
        int c;
        do {
            mark(1);
            c = read();
        } while (isWhitespace(c));
        reset();
    }

    /**
     * Skips whitespace, reads the first non-whitespace character, checks that the non-whitespace character matches the
     * expected character.
     *
     * @param expected Expected first non-whitespace character.
     * @throws InputOutputException In case of I/O failure.
     * @throws InputOutputException If the expected character is not found.
     */
    public void expectCharacter(int expected) {
        expectCharacter(expected, -1);
    }

    /**
     * Skips whitespace, reads the first non-whitespace character, checks that the non-whitespace character matches one
     * of the two potential expected characters.
     *
     * @param expected1 First expected non-whitespace character.
     * @param expected2 Second expected non-whitespace character, or {@code -1} to not have a second expected
     *     non-whitespace character.
     * @return The matched non-whitespace character.
     * @throws InputOutputException In case of I/O failure.
     * @throws InputOutputException If neither of the expected characters is found.
     */
    public int expectCharacter(int expected1, int expected2) {
        int c;
        while (true) {
            c = read();
            if (c == -1) {
                break;
            }

            if (isWhitespace(c)) {
                continue;
            }
            if (c == expected1 || c == expected2) {
                return c;
            }

            break;
        }

        // Found unexpected first non-whitespace character.
        String msg = fmt("Expected \"%c\"", expected1);
        if (expected2 != -1) {
            msg += fmt(" or \"%c\"", expected2);
        }
        msg += ", but found ";
        msg += ((c == -1) ? "end-of-file" : fmt("\"%c\"", c));
        String lineColMsg = getLineColMsg();
        if (lineColMsg != null) {
            msg += ", " + lineColMsg;
        }
        msg += ".";
        throw new InputOutputException(msg);
    }

    /**
     * Skips whitespace and peeks the first non-whitespace character. If it matches the given expected character, the
     * character is read. Otherwise, it is not read.
     *
     * @param expected Expected non-whitespace character.
     * @return {@code true} if the peeked first non-whitespace character matches the expected non-whitespace character,
     *     {@code false} otherwise.
     * @throws InputOutputException In case of I/O failure or end-of-file.
     */
    public boolean matchCharacter(int expected) {
        int c;
        do {
            mark(1);
            c = read();
        } while (isWhitespace(c));

        if (c == -1) {
            String msg = "Expected a non-whitespace character, but found end-of-file";
            String lineColMsg = getLineColMsg();
            if (lineColMsg != null) {
                msg += ", " + lineColMsg;
            }
            msg += ".";
            throw new InputOutputException(msg);
        }

        boolean match = (c == expected);
        if (!match) {
            reset();
        }
        return match;
    }

    /**
     * Returns a message with line/column information for the last character read, or {@code null} if no line/column
     * information is available.
     *
     * @return The message, i.e {@code "at line %d column %d"} with the line and column numbers inserted.
     */
    public String getLineColMsg() {
        if (!(stream instanceof PositionTrackingInputStream)) {
            return null;
        }

        PositionTrackingInputStream ptstream = (PositionTrackingInputStream)stream;
        long line = ptstream.getPrevLine();
        long col = ptstream.getPrevCol();
        Assert.check(line >= 1);
        Assert.check(col >= 1);
        return fmt("at line %d column %d", line, col);
    }
}
