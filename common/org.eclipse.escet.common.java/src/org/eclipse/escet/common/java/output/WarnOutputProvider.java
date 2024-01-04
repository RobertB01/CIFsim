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

/** Interface for obtaining a warning output stream. */
public interface WarnOutputProvider {
    /**
     * Get the warning output stream for this provider. Every call returns the same instance.
     *
     * <p>
     * This function sets the prefix to {@code "WARNING: "} if no warning output stream has been created before.
     * </p>
     *
     * @return The constructed warning output stream.
     */
    public default WarnOutput getWarnOutput() {
        return getWarnOutput("WARNING: ");
    }

    /**
     * Get the warning output stream for this provider. Every call returns the same instance.
     *
     * @param linePrefix Prefix text added before a warning line. May be {@code null} to disable a prefix. Is used only
     *     during the first call since every next call will return the same instance.
     * @return The constructed warning output stream.
     */
    public WarnOutput getWarnOutput(String linePrefix);
}
