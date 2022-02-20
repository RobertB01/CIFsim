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

package org.eclipse.escet.common.app.framework.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/** A tuple of input, output, and error streams. */
public class AppStreams {
    /** The buffered input stream reader. */
    public final BufferedReader in;

    /** The input stream. If possible, use {@link #in} instead. */
    public final InputStream inStream;

    /** The output stream. */
    public final AppStream out;

    /** The error stream. */
    public final AppStream err;

    /**
     * Constructor for the {@link AppStreams} class. Uses {@link System#in}, {@link System#out}, and {@link System#err}.
     */
    public AppStreams() {
        this(System.in, StdAppStream.OUT, StdAppStream.ERR);
    }

    /**
     * Constructor for the {@link AppStreams} class.
     *
     * @param in The input stream.
     * @param out The output stream.
     * @param err The error stream.
     */
    public AppStreams(InputStream in, AppStream out, AppStream err) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.inStream = in;
        this.out = out;
        this.err = err;
    }
}
