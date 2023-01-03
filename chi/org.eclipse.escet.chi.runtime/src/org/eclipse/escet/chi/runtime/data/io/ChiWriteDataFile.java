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

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;

/** File handle for writing to a data file. */
public class ChiWriteDataFile extends ChiFileHandle {
    /**
     * Real file handle of the data file.
     *
     * <p>
     * Only available if the file is actually open.
     * </p>
     */
    private FileWriter handle;

    /** The file has been closed. */
    private boolean closed;

    /**
     * Constructor of the {@link ChiWriteDataFile} class.
     *
     * @param absPath Absolute path to the file.
     */
    public ChiWriteDataFile(String absPath) {
        super(absPath, "w");
        handle = null;
        closed = false;
    }

    /** Make sure the file is opened for write access. */
    private void ensureOpened() {
        if (closed) {
            String msg = fmt("Write-only file \"%s\" has already been closed.", filename);
            throw new InputOutputException(msg);
        }
        if (handle == null) {
            try {
                handle = new FileWriter(filename);
            } catch (IOException e) {
                String msg = fmt("Opening of write-only file \"%s\" failed.", filename);
                throw new InputOutputException(msg, e);
            }
        }
    }

    @Override
    public int read() {
        String msg = fmt("Reading from write-only file \"%s\" is not allowed.", filename);
        throw new InputOutputException(msg);
    }

    @Override
    public void write(String data) {
        ensureOpened();
        try {
            handle.write(data);
        } catch (IOException e) {
            String msg = fmt("Writing to write-only file \"%s\" failed.", filename);
            throw new InputOutputException(msg, e);
        }
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
                String msg = fmt("Closing of write-only file \"%s\" failed.", filename);
                throw new InputOutputException(msg, e);
            }
        }
        handle = null;
        closed = true;
    }

    @Override
    public void flush() {
        try {
            handle.flush();
        } catch (IOException e) {
            String msg = fmt("Flushing of write-only file \"%s\" failed.", filename);
            throw new InputOutputException(msg, e);
        }
    }

    @Override
    public void markStream(int count) {
        String msg = fmt("Marking of write-only file \"%s\" is not allowed.", filename);
        throw new InputOutputException(msg);
    }

    @Override
    public void resetStream() {
        String msg = fmt("Resetting of write-only file \"%s\" is not allowed.", filename);
        throw new InputOutputException(msg);
    }
}
