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

package org.eclipse.escet.tooldef.runtime;

import org.eclipse.escet.common.java.exceptions.ApplicationException;

/** ToolDef end user exception. */
public class ToolDefException extends ApplicationException {
    /**
     * Constructor for the {@link ToolDefException} class.
     *
     * @param message Message describing the exception.
     */
    public ToolDefException(String message) {
        this(message, null);
    }

    /**
     * Constructor for the {@link ToolDefException} class.
     *
     * @param message Message describing the exception.
     * @param cause A deeper cause of the exception.
     */
    public ToolDefException(String message, Throwable cause) {
        super(message, cause);
    }
}
