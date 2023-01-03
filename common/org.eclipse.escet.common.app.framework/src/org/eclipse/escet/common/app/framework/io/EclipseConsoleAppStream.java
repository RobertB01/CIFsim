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

package org.eclipse.escet.common.app.framework.io;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.io.IOException;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.ui.console.IOConsoleOutputStream;

/** {@link AppStream} that writes to an Eclipse console I/O stream. */
public class EclipseConsoleAppStream extends AppStream {
    /** The underlying stream to use. */
    private final IOConsoleOutputStream stream;

    /**
     * Constructor for the {@link EclipseConsoleAppStream} class.
     *
     * @param stream The underlying stream to use.
     */
    public EclipseConsoleAppStream(IOConsoleOutputStream stream) {
        this.stream = stream;
    }

    @Override
    protected void writeImpl(byte b) {
        try {
            stream.write(b);
        } catch (IOException ex) {
            String msg = fmt("Failed to write to an Eclipse console.");
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    protected void writeImpl(byte[] bytes, int offset, int length) {
        try {
            stream.write(bytes, offset, length);
        } catch (IOException ex) {
            String msg = fmt("Failed to write to an Eclipse console.");
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    protected void flushImpl() {
        try {
            stream.flush();
        } catch (IOException ex) {
            String msg = fmt("Failed to flush an Eclipse console stream.");
            throw new InputOutputException(msg, ex);
        }
    }

    @Override
    protected void closeImpl() {
        try {
            stream.close();
        } catch (IOException ex) {
            String msg = fmt("Failed to close an Eclipse console stream.");
            throw new InputOutputException(msg, ex);
        }
    }
}
