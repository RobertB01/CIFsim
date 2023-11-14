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

/** Interface for obtaining an error output stream. */
public interface ErrorOutputProvider {
    /**
     * Get the error output stream for this provider. Every call returns the same instance.
     *
     * <p>
     * This function sets the prefix to {@code "ERROR: "} if no error output stream has been created before.
     * </p>
     *
     * @return The constructed error output stream.
     */
    public default ErrorOutput getErrorOutput() {
        return getErrorOutput("ERROR: ");
    }

    /**
     * Get the error output stream for this provider. Every call returns the same instance.
     *
     * @param linePrefix Prefix text added before an error line. May be {@code null} to disable a prefix. Is used only
     *     during the first call since every next call will return the same instance.
     * @return The constructed error output stream.
     */
    public ErrorOutput getErrorOutput(String linePrefix);
}
