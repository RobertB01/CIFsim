//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java.output;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.java.Strings;

/** Interface to send debug or normal output to the user. */
public interface DebugNormalOutput {
    /**
     * Query whether the output stream is enabled.
     *
     * @return {@code true} if the stream is enabled, {@code false} otherwise.
     */
    public boolean isEnabled();

    /**
     * Increases the current indentation level for the output stream by one, if and only if the output stream is
     * enabled.
     */
    public void inc();

    /**
     * Decreases the current indentation level for the output stream by one, if and only if the output stream is
     * enabled.
     *
     * @throws AssertionError If the current indentation level of the output is zero.
     */
    public void dec();

    /**
     * Produce a line with the given message in the output stream, if and only if the stream is enabled.
     *
     * @param message Message to output.
     * @see #isEnabled
     */
    public void line(String message);

    /**
     * {@link Strings#fmt Format} the parameterized message and produce the result as a line of text in the output
     * stream, if and only if the stream is enabled.
     *
     * <p>
     * For performance reasons, it may be better to call this method like this:
     *
     * <pre>if (isEnabled()) { line(...); }</pre>
     *
     * This avoids evaluating the arguments if the output stream is disabled.
     * </p>
     *
     * @param message Pattern of the message text to output.
     * @param args The arguments of the pattern text.
     * @see #isEnabled
     */
    public default void line(String message, Object... args) {
        line(fmt(message, args));
    }

    /**
     * Produce an empty line in the output stream, if and only if the stream is enabled.
     *
     * @see #isEnabled
     */
    public default void line() {
        line("");
    }

    /**
     * {@link Strings#fmt Format} the parameterized message, {@link Strings#wrap wrap} the line if it is too long. Then
     * produce the result in the output stream, if the stream is enabled.
     *
     * <p>
     * For performance reasons, it may be better to call this method like this:
     *
     * <pre>if (isEnabled()) { wrapped(...); }</pre>
     *
     * This avoids evaluating the arguments if the output is not enabled.
     * </p>
     *
     * @param msg Pattern of the message text to wrap and output.
     * @param args The arguments of the pattern text.
     * @see #isEnabled
     */
    public default void wrapped(String msg, Object... args) {
        for (String txt: Strings.wrap(fmt(msg, args))) {
            line(txt);
        }
    }
}
