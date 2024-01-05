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
 * Exception that indicates a problem with an external dependency. Common problems are:
 * <ul>
 * <li>The external dependency could not be found.</li>
 * <li>The external dependency could not be loaded.</li>
 * <li>The external dependency could has an incompatible version.</li>
 * </ul>
 */
public class DependencyException extends ApplicationException {
    /**
     * Constructor for the {@link DependencyException} class.
     *
     * @param message Message describing the exception.
     */
    public DependencyException(String message) {
        super(message);
    }

    /**
     * Constructor for the {@link DependencyException} class.
     *
     * @param message Message describing the exception.
     * @param cause A deeper cause of the exception.
     */
    public DependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
