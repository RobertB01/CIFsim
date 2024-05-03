//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

/** Class to store and handle file system paths in an application. */
public class PathPair {
    /** Path entered by the user. */
    public final String userPath;

    /** Path used by the system. */
    public final String systemPath;

    /**
     * Constructor of the {@link PathPair} class.
     *
     * @param userPath Path entered by the user.
     * @param systemPath Path used by the system.
     */
    public PathPair(String userPath, String systemPath) {
        this.userPath = userPath;
        this.systemPath = systemPath;
    }
}
