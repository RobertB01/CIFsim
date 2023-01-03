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

import java.io.PrintStream;

import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;

/** {@link AppStream} that writes to {@link System#out} or {@link System#err}. */
public class StdAppStream extends AppStream {
    /** The {@link System#out} stream of the JVM as an {@link AppStream}. */
    public static final AppStream OUT = new StdAppStream(System.out, true);

    /** The {@link System#err} stream of the JVM as an {@link AppStream}. */
    public static final AppStream ERR = new StdAppStream(System.err, false);

    /** The {@link System#out} or {@link System#err} stream. */
    private final PrintStream stream;

    /** Whether this stream wraps {@link System#out} ({@code true}) or {@link System#err} ({@code false}). */
    private final boolean stdout;

    /**
     * Constructor for the {@link StdAppStream} class.
     *
     * @param stream The {@link System#out} or {@link System#err} stream.
     * @param stdout Whether this stream wraps {@link System#out} ({@code true}) or {@link System#err} ({@code false}).
     */
    public StdAppStream(PrintStream stream, boolean stdout) {
        this.stream = stream;
        this.stdout = stdout;
    }

    @Override
    protected void writeImpl(byte b) {
        stream.write(b);
        if (stream.checkError()) {
            String msg = fmt("Failed to write to %s.", getStreamName());
            throw new InputOutputException(msg);
        }
    }

    @Override
    protected void writeImpl(byte[] bytes, int offset, int length) {
        stream.write(bytes, offset, length);
        if (stream.checkError()) {
            String msg = fmt("Failed to write to %s.", getStreamName());
            throw new InputOutputException(msg);
        }
    }

    @Override
    protected void flushImpl() {
        stream.flush();
        if (stream.checkError()) {
            String msg = fmt("Failed to write to %s.", getStreamName());
            throw new InputOutputException(msg);
        }
    }

    @Override
    protected void closeImpl() {
        stream.close();
        if (stream.checkError()) {
            String msg = fmt("Failed to write to %s.", getStreamName());
            throw new InputOutputException(msg);
        }
    }

    /**
     * Returns the name of the stream.
     *
     * @return {@code "stdout"} or {@code "stderr"}.
     */
    private String getStreamName() {
        return stdout ? "stdout" : "stderr";
    }
}
