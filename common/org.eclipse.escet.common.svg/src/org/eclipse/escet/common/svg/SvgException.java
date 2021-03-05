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

package org.eclipse.escet.common.svg;

import org.eclipse.escet.common.app.framework.exceptions.ApplicationException;

/** SVG exception. */
public class SvgException extends ApplicationException {
    /**
     * Constructor for the {@link SvgException} class.
     *
     * @param message Message describing the exception.
     */
    public SvgException(String message) {
        super(message);
    }

    /**
     * Constructor for the {@link SvgException} class.
     *
     * @param message Message describing the exception.
     * @param cause A deeper cause of the exception.
     */
    public SvgException(String message, Throwable cause) {
        super(message, cause);
    }
}
