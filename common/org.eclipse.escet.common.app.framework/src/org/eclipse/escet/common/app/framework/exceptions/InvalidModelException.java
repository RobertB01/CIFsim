//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.app.framework.exceptions;

/** Exception indicating a user-supplied model is invalid. */
public class InvalidModelException extends InvalidInputException {
    /**
     * Constructor for the {@link InvalidModelException} class.
     *
     * @param message Message describing the exception.
     */
    public InvalidModelException(String message) {
        super(message);
    }

    /**
     * Constructor for the {@link InvalidModelException} class.
     *
     * @param message Message describing the exception.
     * @param cause A deeper cause of the exception.
     */
    public InvalidModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
