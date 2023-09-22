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

/** Interface for obtaining a debug or normal output stream. */
public interface DebugNormalOutputProvider {
    /**
     * Get the debug output stream for this provider. Every call returns the same instance.
     *
     * <p>
     * This function sets an empty prefix if no debug output stream has been created before.
     * </p>
     *
     * @return The constructed debug output stream.
     */
    public default DebugNormalOutput getDebugOutput() {
        return getDebugOutput(null);
    }

    /**
     * Get the debug output stream for this provider. Every call returns the same instance.
     *
     * @param linePrefix Prefix text added before a debug line. May be {@code null} to disable a prefix. Is used only
     *     during the first call since every next call will return the same instance.
     * @return The constructed debug output stream.
     */
    public DebugNormalOutput getDebugOutput(String linePrefix);

    /**
     * Get the normal output stream for this provider. Every call returns the same instance.
     *
     * <p>
     * This function sets an empty prefix if no normal output stream has been created before.
     * </p>
     *
     * @return The constructed normal output stream.
     */
    public default DebugNormalOutput getNormalOutput() {
        return getNormalOutput(null);
    }

    /**
     * Get the normal output stream for this provider. Every call returns the same instance.
     *
     * @param linePrefix Prefix text added before a normal line. May be {@code null} to disable a prefix. Is used only
     *     during the first call since every next call will return the same instance.
     * @return The constructed normal output stream.
     */
    public DebugNormalOutput getNormalOutput(String linePrefix);
}
