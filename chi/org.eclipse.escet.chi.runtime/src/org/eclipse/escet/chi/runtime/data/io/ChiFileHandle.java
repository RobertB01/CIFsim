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

package org.eclipse.escet.chi.runtime.data.io;

import static org.eclipse.escet.common.app.framework.Paths.resolve;
import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.exceptions.InvalidInputException;

/** Class representing a file handle in a Chi simulation. */
public abstract class ChiFileHandle {
    /** Filename associated with the file handle, {@code null} if using stdio. */
    public final String filename;

    /** Allowed operations to the file. Either {@code "w"} or {@code "r"}. */
    public final String operations;

    /**
     * Constructor of a file handle.
     *
     * <p>
     * Handle is not actually opened, since it may exist already in the open files of the coordinator. Instead, the file
     * gets opened on the first attempt to use it.
     * </p>
     *
     * @param path Path to the file. Use {@code null} for stdio.
     * @param operations Allowed operations on the handle. Should be either {@code "r"} or {@code "w"}.
     * @param type Type of file IO. Current only supported type is {@code "text"}.
     * @return Constructed file handle object.
     */
    public static ChiFileHandle createFile(String path, String operations, String type) {
        if (path == null || path.isEmpty()) {
            path = null;
        } else {
            path = resolve(path);
        }

        if (!type.equals("text")) {
            String msg = "Only type \"text\" is supported for file IO.";
            throw new InvalidInputException(msg);
        }

        if (operations.equals("w") || operations.equals("W")) {
            if (path == null) {
                return new ChiStdioFile("w");
            } else {
                return new ChiWriteDataFile(path);
            }
        } else if (operations.equals("r") || operations.equals("R")) {
            if (path == null) {
                return new ChiStdioFile("r");
            } else {
                return new ChiReadDataFile(path);
            }
        } else {
            String msg = "Unknown file operation \"" + operations
                    + "\". Only \"r\" and \"w\" operations are supported.";
            throw new InvalidInputException(msg);
        }
    }

    /**
     * Constructor of the file handle.
     *
     * @param filename Name of the file. Use {@code null} for stdio.
     * @param operations Allowed operations on the handle. Should be either {@code "r"} or {@code "w"}.
     */
    public ChiFileHandle(String filename, String operations) {
        this.filename = filename;
        this.operations = operations;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ChiFileHandle)) {
            return false;
        }
        ChiFileHandle otherHandle = (ChiFileHandle)other;
        if (filename != null) {
            if (otherHandle.filename == null) {
                return false;
            }
            return filename.equals(otherHandle.filename);
        } else {
            if (otherHandle.filename != null) {
                return false;
            }
            return operations.equals(otherHandle.operations);
        }
    }

    @Override
    public int hashCode() {
        if (filename == null) {
            return operations.hashCode();
        }
        return filename.hashCode();
    }

    /**
     * Read a symbol from the file.
     *
     * @return Read character from the file, or {@code -1} if at end of file.
     */
    public abstract int read();

    /** Mark an input stream for one character read-ahead. */
    public void markStream() {
        markStream(1);
    }

    /**
     * Mark an input stream.
     *
     * @param count Number of characters to read ahead.
     */
    public abstract void markStream(int count);

    /** Reset a stream to the last marker (or the start of the file). */
    public abstract void resetStream();

    /**
     * Return whether the provided character is a whitespace character.
     *
     * @param kar Provided character to test.
     * @return Whether the provided character is a whitespace character.
     */
    public static boolean isWhitespace(int kar) {
        return kar == ' ' || kar == '\t' || kar == '\r' || kar == '\n';
    }

    /** Skip white space characters in the input. */
    public void skipWhitespace() {
        int kar;
        // Skip whitespace.
        do {
            markStream();
            kar = read();
        } while (isWhitespace(kar));
        resetStream();
    }

    /**
     * Verify that the first non-whitespace character is 'expect'.
     *
     * @param expect Expected first non-whitespace character.
     * @throws InputOutputException When the expected character is not found.
     */
    public void expectCharacter(int expect) {
        expectCharacter(expect, -1);
    }

    /**
     * Verify that the first non-whitespace character is 'expect1' or 'expect2'.
     *
     * @param expect1 First expected non-whitespace character.
     * @param expect2 Second expected non-whitespace character. {@code -1} means that the second character is not used.
     * @return Found character.
     * @throws InputOutputException When none of the expected characters is found.
     */
    public int expectCharacter(int expect1, int expect2) {
        int ch;
        for (;;) {
            ch = read();
            if (ch == -1) {
                break;
            }

            if (isWhitespace(ch)) {
                continue;
            }
            if (ch == expect1 || ch == expect2) {
                return ch;
            }

            break;
        }

        // Found incorrect first non-whitespace character, throw an exception.
        String msg = fmt("Expected '%c'", expect1);
        if (expect2 > 0) {
            msg += fmt(" or '%c'", expect2);
        }
        msg += ", but found " + ((ch == -1) ? "EOF" : fmt("'%c'", ch));
        throw new InputOutputException(msg);
    }

    /**
     * Write text to the file. File handle should allow writing.
     *
     * @param data Text to write.
     */
    public abstract void write(String data);

    /** Flush the stream (optional operation). */
    public void flush() {
        // Default implementation does nothing.
    }

    /**
     * Has the file handle been closed?
     *
     * @return {@code true} if the file is already closed, {@code false} if the file is still open.
     */
    public abstract boolean isClosed();

    /** Close the file. */
    public abstract void close();
}
