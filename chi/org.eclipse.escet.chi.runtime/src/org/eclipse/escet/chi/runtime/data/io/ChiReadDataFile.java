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

package org.eclipse.escet.chi.runtime.data.io;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;

/** Class for reading from a data file. */
public class ChiReadDataFile extends ChiFileHandle {
    /**
     * Real file handle of the data file.
     *
     * <p>
     * Only available if the file is actually open.
     * </p>
     */
    private BufferedReader handle = null;

    /** EOF detected while reading ahead. */
    private boolean foundEOF = false;

    /** Number of found EOL characters while reading ahead. */
    private int eolCount = 0;

    /** The file has been closed. */
    private boolean closed = false;

    /**
     * Constructor for the {@link ChiReadDataFile} class.
     *
     * @param absPath Absolute path to the file to read.
     */
    public ChiReadDataFile(String absPath) {
        super(absPath, "r");
    }

    /** Make sure the file is opened for read access. */
    private void ensureOpened() {
        if (closed) {
            String msg = fmt("Read-only file \"%s\" has already been closed.", filename);
            throw new InputOutputException(msg);
        }
        if (handle == null) {
            try {
                handle = new BufferedReader(new FileReader(filename));
            } catch (FileNotFoundException e) {
                String msg = fmt("Read-only file \"%s\" is not available.", filename);
                throw new InputOutputException(msg, e);
            }
        }
    }

    @Override
    public int read() {
        ensureOpened();
        if (foundEOF) {
            return -1;
        }
        // Reset EOL count.
        // Users of this function silently discard leading EOL and white space,
        // thus dropping the read-ahead result is not a problem.
        eolCount = 0;

        return readCharacter();
    }

    /**
     * Elementary read of a character.
     *
     * @return Read character from the file, or -1 if at end of file.
     */
    private int readCharacter() {
        try {
            return handle.read();
        } catch (IOException e) {
            String msg = fmt("Reading from read-only file \"%s\" failed.", filename);
            throw new InputOutputException(msg);
        }
    }

    /** Read whitespace in the stream, and check for EOL and EOF characters. */
    private void detectEolEof() {
        ensureOpened();
        // Previous result is still valid, no need to read again.
        if (foundEOF || eolCount > 0) {
            return;
        }

        while (true) {
            markStream(1);
            int k = readCharacter();
            if (k == -1) {
                foundEOF = true;
                return;
            }
            if (k == '\n') {
                eolCount++;
                continue;
            }
            if (isWhitespace(k)) {
                continue;
            }

            // Non-whitespace found, quit reading.
            resetStream();
            return;
        }
    }

    /**
     * Check how many EOL characters will be read before the next value, if such a value is available. {@code -1} means
     * no more values are available.
     *
     * @return Number of EOL characters read before the next value, or {@code -1} if at EOF.
     */
    public int getNewlinesInfo() {
        detectEolEof();
        if (foundEOF) {
            return -1;
        }
        return eolCount;
    }

    @Override
    public void write(String data) {
        String msg = fmt("Writing to read-only file \"%s\" is not allowed.", filename);
        throw new InputOutputException(msg);
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        if (handle != null) {
            try {
                handle.close();
            } catch (IOException e) {
                String msg = fmt("Closing of read-only file \"%s\" failed.", filename);
                throw new InputOutputException(msg, e);
            }
        }
        handle = null;
        closed = true;
    }

    @Override
    public void markStream(int count) {
        ensureOpened();
        try {
            handle.mark(count);
        } catch (IOException e) {
            String msg = fmt("Marking of read-only file \"%s\" failed.", filename);
            throw new InputOutputException(msg);
        }
    }

    @Override
    public void resetStream() {
        ensureOpened();
        try {
            handle.reset();
        } catch (IOException e) {
            String msg = fmt("Resetting of read-only file \"%s\" failed.", filename);
            throw new InputOutputException(msg);
        }
    }
}
