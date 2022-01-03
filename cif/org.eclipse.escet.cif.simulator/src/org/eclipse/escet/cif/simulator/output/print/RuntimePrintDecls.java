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

package org.eclipse.escet.cif.simulator.output.print;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.simulator.CifSimulatorContext;
import org.eclipse.escet.cif.simulator.output.NormalOutputType;
import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.app.framework.exceptions.InputOutputException;
import org.eclipse.escet.common.app.framework.io.AppStream;
import org.eclipse.escet.common.app.framework.io.FileAppStream;

/** Runtime print I/O declarations, for a single file/target. */
public abstract class RuntimePrintDecls {
    /** Whether printing of output is enabled. Is {@code false} until set by the {@link #init} method. */
    private boolean enabled;

    /**
     * The stream to which to print all output. Is {@code null} until set by the {@link #setStream} method. Remains
     * {@code null} if printing is not {@link #enabled}.
     */
    protected AppStream stream;

    /**
     * Whether the {@link #stream} should be closed after simulation. Is {@code false} until set by the
     * {@link #setStream} method.
     */
    private boolean shouldClose;

    /**
     * Returns the local file system path to the file to use for printing, relative to the directory that contains the
     * CIF specification being simulated. If it starts with {@code ":"} it is a special target.
     *
     * <p>
     * May not be a valid relative path if previously compiled code is used. Use this path for shorter/readable error
     * messages only.
     * </p>
     *
     * @return The local file system path to the file to use for printing, relative to the directory that contains the
     *     CIF specification being simulated. If it starts with {@code ":"} it is a special target.
     */
    public abstract String getRelPath();

    /**
     * Returns the absolute local file system path to the file to use for printing. If it starts with {@code ":"} it is
     * a special target.
     *
     * @return The absolute local file system path to the file to use for printing. If it starts with {@code ":"} it is
     *     a special target.
     */
    public abstract String getAbsPath();

    /**
     * Prints output for a certain transition.
     *
     * <p>
     * Prints for both the pre/source state and the post/target state. First all pre output is printed, then all post
     * output is printed. Since this method is only concerned with printing for its own file/target, it does not have to
     * consider other files/targets when ordering the print output. The only exception might be stdout/stderr, which
     * share a console for their output. However, we don't treat them special.
     * </p>
     *
     * @param preState The pre/source state of the transition.
     * @param postState The post/target start of the transition.
     * @param kind The kind of the transition.
     * @param eventIdx The 0-based index of the event of the transition, or {@code -1} if not applicable.
     */
    public abstract void print(RuntimeState preState, RuntimeState postState, PrintTransitionKind kind, int eventIdx);

    /**
     * Initializes the print I/O declarations.
     *
     * @param ctxt The simulator runtime context.
     */
    public void init(CifSimulatorContext ctxt) {
        // Figure out whether print output is enabled.
        enabled = ctxt.normal.contains(NormalOutputType.PRINT);

        // Open the stream, if printing is enabled.
        if (enabled) {
            setStream(ctxt);
        }
    }

    /**
     * Sets the stream to use for printing. Must only be invoked once, by the {@link #init} method.
     *
     * @param ctxt The simulator runtime context.
     * @see #stream
     * @see #shouldClose
     */
    private void setStream(CifSimulatorContext ctxt) {
        String absPath = getAbsPath();
        if (absPath.equals(":stdout")) {
            stream = ctxt.appEnvData.getStreams().out;
            shouldClose = false;
        } else if (absPath.equals(":stderr")) {
            stream = ctxt.appEnvData.getStreams().err;
            shouldClose = false;
        } else {
            try {
                stream = new FileAppStream(absPath);
            } catch (InputOutputException ex) {
                String msg = fmt("Failed to open file \"%s\" for writing using print declarations.", getRelPath());
                throw new InputOutputException(msg, ex);
            }
            shouldClose = true;
        }
    }

    /**
     * Closes the stream associated with these print declarations. If the stream is a special stream that should not be
     * closed, this method does nothing. It is allowed to call this method multiple times.
     */
    public void close() {
        if (enabled && shouldClose && stream != null) {
            stream.close();
            stream = null;
        }
    }
}
