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

package org.eclipse.escet.common.emf;

import org.eclipse.escet.common.java.exceptions.EndUserException;

/** Exception that indicates there is a problem with an EMF resource. */
public class EMFResourceException extends Exception implements EndUserException {
    /**
     * Constructor for the {@link EMFResourceException} class. The cause is {@code null}.
     *
     * @param message The message describing the exception.
     */
    public EMFResourceException(String message) {
        super(message);
    }

    /**
     * Constructor for the {@link EMFResourceException} class.
     *
     * @param message The message describing the exception.
     * @param cause The root cause of the exception.
     */
    public EMFResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
