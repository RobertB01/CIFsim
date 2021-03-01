//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.io;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;

/** {@link AppStream} that writes to an {@link OutputStream}. */
public class OutputStreamAppStream extends AppStream {
    /** The underlying output stream to use. */
    private final OutputStream stream;

    /**
     * Constructor for the {@link OutputStreamAppStream} class.
     *
     * @param stream The underlying output stream to use.
     */
    public OutputStreamAppStream(OutputStream stream) {
        this.stream = stream;
    }

    @Override
    protected void writeImpl(byte b) {
        try {
            stream.write(b);
        } catch (IOException ex) {
            String msg = fmt("Failed to write to an output stream.");
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    protected void writeImpl(byte[] bytes, int offset, int length) {
        try {
            stream.write(bytes, offset, length);
        } catch (IOException ex) {
            String msg = fmt("Failed to write to an output stream.");
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    protected void flushImpl() {
        try {
            stream.flush();
        } catch (IOException ex) {
            String msg = fmt("Failed to flush an output stream.");
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    protected void closeImpl() {
        try {
            stream.close();
        } catch (IOException ex) {
            String msg = fmt("Failed to close an output stream.");
            throw new InputOutputException(msg, ex);
        }
    }
}
