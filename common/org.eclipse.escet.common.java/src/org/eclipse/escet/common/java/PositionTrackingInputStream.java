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

package org.eclipse.escet.common.java;

import java.io.IOException;
import java.io.InputStream;

/**
 * Wrapper input stream supporting position, line, and column tracking.
 *
 * <p>
 * Supports reading a single byte at a time ({@link #read()}). Does not support:
 * <ul>
 * <li>reading multiple bytes or a single byte into an array ({@link #read(byte[])} and
 * {@link #read(byte[], int, int)})</li>
 * <li>skipping bytes ({@link #skip(long)})</li>
 * <li>checking for number of available bytes ({@link #available()})</li>
 * </ul>
 * </p>
 *
 * <p>
 * This class is mainly intended for scanners that read and process a single character at a time.
 * </p>
 */
public class PositionTrackingInputStream extends InputStream {
    /** End-of-file byte. */
    private static final int EOF = -1;

    /** The wrapped input stream. */
    private final InputStream stream;

    /** The 0-based offset into the stream of the last character read, or {@code -1} if not available/applicable. */
    private long prevPos = -1;

    /** The 1-based line number of the last character read, or {@code -1} if not available/applicable. */
    private long prevLine = -1;

    /** The 1-based column number of the last character read, or {@code -1} if not available/applicable. */
    private long prevCol = -1;

    /** The 0-based offset into the stream of the next character to read. */
    private long nextPos = 0;

    /** The 1-based line number of the next character to read. */
    private long nextLine = 1;

    /** The 1-based column number of the next character to read. */
    private long nextCol = 1;

    /**
     * The 0-based offset into the stream of the last character read, at the moment of marking, or {@code -1} if not
     * available/applicable.
     */
    private long markedPrevPos = -1;

    /**
     * The 1-based line number of the last character read, at the moment of marking, or {@code -1} if not
     * available/applicable.
     */
    private long markedPrevLine = -1;

    /**
     * The 1-based column number of the last character read, at the moment of marking, or {@code -1} if not
     * available/applicable.
     */
    private long markedPrevCol = -1;

    /**
     * The 0-based offset into the stream of the next character to read, at the moment of marking, or {@code -1} if not
     * available/applicable.
     */
    private long markedNextPos = -1;

    /**
     * The 1-based line number of the next character to read, at the moment of marking, or {@code -1} if not
     * available/applicable.
     */
    private long markedNextLine = -1;

    /**
     * The 1-based column number of the next character to read, at the moment of marking, or {@code -1} if not
     * available/applicable.
     */
    private long markedNextCol = -1;

    /**
     * Constructor for the {@link PositionTrackingInputStream} class.
     *
     * @param stream The wrapped input stream.
     */
    public PositionTrackingInputStream(InputStream stream) {
        this.stream = stream;
    }

    /**
     * Returns the 0-based offset into the stream of the last character read, or {@code -1} if not available/applicable.
     *
     * @return The 0-based offset into the stream of the last character read, or {@code -1}.
     */
    public long getPrevPos() {
        return prevPos;
    }

    /**
     * Returns the 1-based line number of the last character read, or {@code -1} if not available/applicable.
     *
     * @return The 1-based line number of the last character read, or {@code -1}.
     */
    public long getPrevLine() {
        return prevLine;
    }

    /**
     * Returns the 1-based column number of the last character read, or {@code -1} if not available/applicable.
     *
     * @return The 1-based column number of the last character read, or {@code -1}.
     */
    public long getPrevCol() {
        return prevCol;
    }

    @Override
    public int read() throws IOException {
        // Read next character, or end-of-file.
        int c = stream.read();

        // Update previous positions.
        prevPos = nextPos;
        prevLine = nextLine;
        prevCol = nextCol;

        // Update next positions, if applicable.
        if (c != EOF) {
            nextPos++;
            nextCol++;
            if (c == '\n') {
                nextLine++;
                nextCol = 1;
            }
        }

        // Return read character, or end-of-file.
        return c;
    }

    @Override
    public int read(byte[] b) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long skip(long n) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int available() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return stream.markSupported();
    }

    @Override
    public synchronized void mark(int readlimit) {
        // Mark stream.
        stream.mark(readlimit);

        // Store marked positions.
        markedPrevPos = prevPos;
        markedPrevLine = prevLine;
        markedPrevCol = prevCol;

        markedNextPos = nextPos;
        markedNextLine = nextLine;
        markedNextCol = nextCol;
    }

    @Override
    public synchronized void reset() throws IOException {
        // Make sure marked position information is available.
        if (markedNextPos == -1) {
            throw new IllegalStateException();
        }

        // Reset stream.
        stream.reset();

        // Restore positions.
        prevPos = markedPrevPos;
        prevLine = markedPrevLine;
        prevCol = markedPrevCol;

        nextPos = markedNextPos;
        nextLine = markedNextLine;
        nextCol = markedNextCol;

        // Marking no longer active.
        markedPrevPos = -1;
        markedPrevLine = -1;
        markedPrevCol = -1;

        markedNextPos = -1;
        markedNextLine = -1;
        markedNextCol = -1;
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
