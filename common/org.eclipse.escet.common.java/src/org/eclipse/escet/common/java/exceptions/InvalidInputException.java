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

package org.eclipse.escet.common.java.exceptions;

/**
 * Exception indicating the user-supplied invalid data. If one of the derived classes fits better, it should be used.
 * However, it is allowed to use this class directly, if appropriate.
 */
public class InvalidInputException extends ApplicationException {
    /**
     * Constructor for the {@link InvalidInputException} class.
     *
     * @param message Message describing the exception.
     */
    public InvalidInputException(String message) {
        super(message);
    }

    /**
     * Constructor for the {@link InvalidInputException} class.
     *
     * @param message Message describing the exception.
     * @param cause A deeper cause of the exception.
     */
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
