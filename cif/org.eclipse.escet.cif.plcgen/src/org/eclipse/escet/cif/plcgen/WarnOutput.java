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

package org.eclipse.escet.cif.plcgen;

import static org.eclipse.escet.common.java.Strings.fmt;

/** Interface to send warnings to the user. */
public interface WarnOutput {
    /**
     * Produce a line with a message in the warnings output.
     *
     * @param message Message to send.
     */
    public void warn(String message);

    /**
     * Produce a line with a message in the warnings output.
     *
     * @param message Message to send.
     * @param params Message parameters.
     */
    public default void warn(String message, Object... params) {
        warn(fmt(message, params));
    }

    /** Produce an empty line in the warnings output. */
    public default void warn() {
        warn("");
    }
}
