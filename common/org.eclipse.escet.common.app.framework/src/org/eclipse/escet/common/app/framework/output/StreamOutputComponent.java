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

package org.eclipse.escet.common.app.framework.output;

import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.StdAppStream;
import org.eclipse.escet.common.java.Strings;

/** Output component that writes all information to streams ({@link System#out} and {@link System#err} by default). */
public class StreamOutputComponent implements IOutputComponent {
    /** The stream to write all normal (non-warning and non-error) output to. */
    protected final AppStream out;

    /** The stream to write all warning output to. */
    protected final AppStream warn;

    /** The stream to write all error output to. */
    protected final AppStream err;

    /**
     * Constructor for the {@link StreamOutputComponent} class. Sets the output stream to {@link StdAppStream#OUT}, and
     * the warning and error streams to {@link StdAppStream#ERR}.
     */
    public StreamOutputComponent() {
        this(StdAppStream.OUT, StdAppStream.ERR, StdAppStream.ERR);
    }

    /**
     * Constructor for the {@link StreamOutputComponent} class, with custom output, warning, and error streams.
     *
     * @param out The stream to write all normal (non-warning and non-error) output to.
     * @param warn The stream to write all warning output to.
     * @param err The stream to write all error output to.
     */
    public StreamOutputComponent(AppStream out, AppStream warn, AppStream err) {
        this.out = out;
        this.warn = warn;
        this.err = err;
    }

    @Override
    public void dbg(String msg, int indent) {
        out.println(Strings.spaces(indent * 4) + msg);
    }

    @Override
    public void out(String msg, int indent) {
        out.println(Strings.spaces(indent * 4) + msg);
    }

    @Override
    public void warn(String msg, int indent) {
        warn.println(Strings.spaces(indent * 4) + "WARNING: " + msg);
    }

    @Override
    public void err(String msg) {
        err.println(msg);
    }

    @Override
    public void initialize() {
        // No initialization required.
    }

    @Override
    public void cleanup() {
        // No cleanup required.
    }
}
