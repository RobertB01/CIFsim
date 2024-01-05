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

package org.eclipse.escet.cif.explorer.runtime;

/** Indication of why there is no initial state. */
public abstract class NoInitialStateReason {
    /**
     * Returns an end user readable message indicating why the initial state is invalid (doesn't exist).
     *
     * @return The message.
     */
    public abstract String getMessage();
}
