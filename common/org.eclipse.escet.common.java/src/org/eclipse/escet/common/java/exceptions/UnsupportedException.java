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

package org.eclipse.escet.common.java.exceptions;

/**
 * Exception indicating something is not supported in the current implementation. This is an exception that reports
 * being unsupported to the end user.
 *
 * <p>
 * If something is unsupported at the code level, and it is thus something that interests only developers, and how they
 * may call certain methods, then Java's {@link UnsupportedOperationException} exception class should be used instead,
 * as in such cases the exception is an internal error.
 * </p>
 */
public class UnsupportedException extends ApplicationException {
    /**
     * Constructor for the {@link UnsupportedException} class.
     *
     * @param message Message describing the exception.
     */
    public UnsupportedException(String message) {
        super(message);
    }

    /**
     * Constructor for the {@link UnsupportedException} class.
     *
     * @param message Message describing the exception.
     * @param cause A deeper cause of the exception.
     */
    public UnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
