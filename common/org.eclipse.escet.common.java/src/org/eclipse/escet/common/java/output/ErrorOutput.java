//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

/** Interface to send errors to the user. */
public interface ErrorOutput {
    /**
     * Produce a line with the given message in the output stream.
     *
     * @param message Message to output.
     */
    public void line(String message);

    /**
     * {@link Strings#fmt Format} the parameterized message and produce the result as a line of text in the output
     * stream.
     *
     * @param message Pattern of the message text to output.
     * @param args The arguments of the pattern text.
     */
    public default void line(String message, Object... args) {
        line(fmt(message, args));
    }

    /** Produce an empty line in the output stream. */
    public default void line() {
        line("");
    }
}
