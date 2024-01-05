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

package org.eclipse.escet.common.app.framework.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/** A tuple of input, output, warning, and error streams. */
public class AppStreams {
    /** The buffered input stream reader. */
    public final BufferedReader in;

    /** The input stream. If possible, use {@link #in} instead. */
    public final InputStream inStream;

    /** The output stream. */
    public final AppStream out;

    /** The warning stream. */
    public final AppStream warn;

    /** The error stream. */
    public final AppStream err;

    /**
     * Constructor for the {@link AppStreams} class. Uses {@link System#in}, {@link System#out}, and {@link System#err}
     * (for warnings and errors).
     */
    public AppStreams() {
        this(System.in, StdAppStream.OUT, StdAppStream.ERR, StdAppStream.ERR);
    }

    /**
     * Constructor for the {@link AppStreams} class.
     *
     * @param in The input stream.
     * @param out The output stream.
     * @param warn The warning stream.
     * @param err The error stream.
     */
    public AppStreams(InputStream in, AppStream out, AppStream warn, AppStream err) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.inStream = in;
        this.out = out;
        this.warn = warn;
        this.err = err;
    }
}
