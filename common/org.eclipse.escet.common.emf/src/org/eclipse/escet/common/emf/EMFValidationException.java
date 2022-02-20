//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

/** Exception used by the {@link EMFValidationHelper} class to report EMF validation failures. */
public class EMFValidationException extends RuntimeException {
    /**
     * Constructor for the {@link EMFValidationException} class.
     *
     * @param message The validation error message.
     */
    public EMFValidationException(String message) {
        super(message);
    }
}
