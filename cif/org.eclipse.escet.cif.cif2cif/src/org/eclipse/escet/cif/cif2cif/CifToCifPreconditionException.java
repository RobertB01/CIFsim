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

package org.eclipse.escet.cif.cif2cif;

import org.eclipse.escet.common.app.framework.exceptions.EndUserException;

/** CIF to CIF transformation precondition violation exception. */
public class CifToCifPreconditionException extends RuntimeException implements EndUserException {
    /**
     * Constructor for the {@link CifToCifPreconditionException} class.
     *
     * @param message The detail message.
     */
    public CifToCifPreconditionException(String message) {
        super(message);
    }

    /**
     * Constructor for the {@link CifToCifPreconditionException} class.
     *
     * @param message The detail message.
     * @param cause The underlying cause of the exception.
     */
    public CifToCifPreconditionException(String message, Throwable cause) {
        super(message, cause);
    }
}
