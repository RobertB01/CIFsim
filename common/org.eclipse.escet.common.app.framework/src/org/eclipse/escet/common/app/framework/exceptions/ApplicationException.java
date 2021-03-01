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

package org.eclipse.escet.common.app.framework.exceptions;

/**
 * Base class for all application exceptions. Application exceptions are unchecked (runtime) exceptions.
 *
 * <p>
 * In general, it is considered bad practice to construct instances of this class directly, similarly to it being bad
 * practice to construct instances of the {@link Exception} class. Instead, instances of derived classes should be
 * constructed.
 * </p>
 */
public class ApplicationException extends RuntimeException implements EndUserException {
    /**
     * Constructor for the {@link ApplicationException} class, without a message. Use this constructor only for derived
     * class that provide their own {@link #getMessage} implementation.
     */
    protected ApplicationException() {
        // Nothing to do here.
    }

    /**
     * Constructor for the {@link ApplicationException} class.
     *
     * @param message Message describing the exception.
     */
    public ApplicationException(String message) {
        this(message, null);
    }

    /**
     * Constructor for the {@link ApplicationException} class, without a message. Use this constructor only for derived
     * class that provide their own {@link #getMessage} implementation.
     *
     * @param cause A deeper cause of the exception.
     */
    public ApplicationException(Throwable cause) {
        this(null, cause);
    }

    /**
     * Constructor for the {@link ApplicationException} class.
     *
     * @param message Message describing the exception.
     * @param cause A deeper cause of the exception.
     */
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public final String toString() {
        // Don't override toString. Override getMessage() instead.
        return getMessage();
    }
}
