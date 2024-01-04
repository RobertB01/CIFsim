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

package org.eclipse.escet.chi.runtime;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.exceptions.ApplicationException;

/** Exception class denoting a fatal runtime error of the Chi simulator. */
public class ChiSimulatorException extends ApplicationException {
    /**
     * Constructor for the {@link ChiSimulatorException} class.
     *
     * @param message Text describing what went wrong in the execution.
     */
    public ChiSimulatorException(String message) {
        super(message);
        Assert.check(message.endsWith("."));
    }

    /**
     * Constructor for the {@link ChiSimulatorException} class, in case an underlying exception detected the failure.
     *
     * @param message Text describing what went wrong in the execution.
     * @param cause Underlying exception that detected the failure.
     */
    public ChiSimulatorException(String message, Throwable cause) {
        super(message, cause);
        Assert.check(message.endsWith("."));
    }
}
