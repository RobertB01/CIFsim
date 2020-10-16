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

package org.eclipse.escet.chi.runtime.data.io;

import static org.eclipse.escet.common.app.framework.output.OutputProvider.doout;
import static org.eclipse.escet.common.app.framework.output.OutputProvider.out;

import java.io.IOException;

import org.eclipse.escet.common.app.framework.AppEnv;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.io.AppStreams;

/** File handle for standard input or output. */
public class ChiStdioFile extends ChiFileHandle {
    /** Temporary buffer for output. */
    private String outBuffer;

    /**
     * Constructor for the {@link ChiStdioFile} class.
     *
     * @param operations Operations that may be performed on the handle.
     */
    public ChiStdioFile(String operations) {
        super(null, operations);
        outBuffer = "";
    }

    /** Ensure the console can be accessed for reading. */
    private void ensureReadAllowed() {
        if (!operations.equals("r")) {
            String msg = "Console is not opened for reading.";
            throw new InputOutputException(msg);
        }
    }

    /** Ensure the console can be accessed for writing. */
    private void ensureWriteAllowed() {
        if (!operations.equals("w")) {
            String msg = "Console is not opened for writing.";
            throw new InputOutputException(msg);
        }
    }

    @Override
    public int read() {
        ensureReadAllowed();

        AppStreams streams = AppEnv.getStreams();
        try {
            return streams.in.read();
        } catch (IOException e) {
            String msg = "Console read failed";
            throw new InputOutputException(msg, e);
        }
    }

    @Override
    public void write(String data) {
        ensureWriteAllowed();

        outBuffer += data;
        for (;;) {
            int i = outBuffer.indexOf('\n');
            if (i < 0) {
                break;
            }
            // Do not output '\n', since 'out' does that already.
            if (doout()) {
                out(outBuffer.substring(0, i));
            }
            outBuffer = outBuffer.substring(i + 1);
        }
    }

    @Override
    public boolean isClosed() {
        return false; // Standard IO streams are never closed.
    }

    @Override
    public void close() {
        flush();
        // Don't actually close, framework handles closing of console streams.
    }

    @Override
    public void flush() {
        if (operations.equals("w") && !outBuffer.isEmpty()) {
            if (doout()) {
                out(outBuffer);
            }
            outBuffer = "";
        }
    }

    @Override
    public void markStream(int count) {
        ensureReadAllowed();

        AppStreams streams = AppEnv.getStreams();
        try {
            streams.in.mark(count);
        } catch (IOException e) {
            String msg = "Console marking failed";
            throw new InputOutputException(msg, e);
        }
    }

    @Override
    public void resetStream() {
        ensureReadAllowed();

        AppStreams streams = AppEnv.getStreams();
        try {
            streams.in.reset();
        } catch (IOException e) {
            String msg = "Console resetting failed";
            throw new InputOutputException(msg, e);
        }
    }
}
