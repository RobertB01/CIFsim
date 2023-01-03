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

package org.eclipse.escet.tooldef.runtime;

/**
 * Exception that indicates execution encountered an 'exit' statement or for some other reason should stop execution
 * with a specific exit code.
 */
public class ExitException extends RuntimeException {
    /** The exit code. */
    public final int exitCode;

    /**
     * Constructor for the {@link ExitException} class.
     *
     * @param exitCode The exit code.
     */
    public ExitException(int exitCode) {
        this.exitCode = exitCode;
    }
}
