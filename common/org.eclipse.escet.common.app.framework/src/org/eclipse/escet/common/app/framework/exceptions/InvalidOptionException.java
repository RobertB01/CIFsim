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

package org.eclipse.escet.common.app.framework.exceptions;

import org.eclipse.escet.common.java.Assert;

/**
 * Exception that indicates that an option (or its value) is invalid, or that there is an other option related problem.
 */
public class InvalidOptionException extends InvalidInputException {
    /**
     * Constructor for the {@link InvalidOptionException} class.
     *
     * @param message Message describing the exception.
     */
    public InvalidOptionException(String message) {
        super(message);
        Assert.notNull(message);
    }

    /**
     * Constructor for the {@link InvalidOptionException} class.
     *
     * @param message Message describing the exception.
     * @param cause A deeper cause of the exception.
     */
    public InvalidOptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
