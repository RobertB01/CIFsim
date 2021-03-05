//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.simulator.runtime;

import org.eclipse.escet.cif.simulator.runtime.model.RuntimeState;
import org.eclipse.escet.common.app.framework.exceptions.EndUserException;

/** CIF simulator runtime exception. */
public class CifSimulatorException extends RuntimeException implements EndUserException {
    /**
     * The state for which simulation failed, or {@code null} if not available. During initialization, a state may be
     * incomplete, but only complete states may be used here.
     */
    public final RuntimeState state;

    /**
     * Constructor for the {@link CifSimulatorException} class.
     *
     * @param message The message describing the exception.
     */
    public CifSimulatorException(String message) {
        this(message, null, null);
    }

    /**
     * Constructor for the {@link CifSimulatorException} class.
     *
     * @param message The message describing the exception.
     * @param cause The root cause of the exception.
     */
    public CifSimulatorException(String message, Throwable cause) {
        this(message, cause, null);
    }

    /**
     * Constructor for the {@link CifSimulatorException} class.
     *
     * @param message The message describing the exception.
     * @param state The state for which simulation failed, or {@code null} if not available. During initialization, a
     *     state may be incomplete, but only complete states may be used here.
     */
    public CifSimulatorException(String message, RuntimeState state) {
        this(message, null, state);
    }

    /**
     * Constructor for the {@link CifSimulatorException} class.
     *
     * @param message The message describing the exception.
     * @param cause The root cause of the exception.
     * @param state The state for which simulation failed, or {@code null} if not available. During initialization, a
     *     state may be incomplete, but only complete states may be used here.
     */
    public CifSimulatorException(String message, Throwable cause, RuntimeState state) {
        super(message, cause);
        this.state = state;
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
