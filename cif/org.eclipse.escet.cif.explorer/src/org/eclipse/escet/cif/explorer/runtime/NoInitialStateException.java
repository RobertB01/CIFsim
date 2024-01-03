//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.common.java.exceptions.EndUserException;

/** Exception indicating there is no initial state. */
public class NoInitialStateException extends RuntimeException implements EndUserException {
    /** The reason the initial state is invalid (doesn't exist). */
    public final NoInitialStateReason reason;

    /**
     * Constructor for the {@link NoInitialStateException} class.
     *
     * @param reason The reason the initial state is invalid (doesn't exist).
     */
    public NoInitialStateException(NoInitialStateReason reason) {
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return reason.getMessage();
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
