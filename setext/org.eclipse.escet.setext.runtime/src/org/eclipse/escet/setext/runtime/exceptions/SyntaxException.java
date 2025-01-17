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

package org.eclipse.escet.setext.runtime.exceptions;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.common.java.exceptions.ApplicationException;

/**
 * Exception indicating a syntax exception. Scanners and parsers should only throw exceptions that extend this class.
 */
public abstract class SyntaxException extends ApplicationException {
    /** The position information (possibly including source information) for the syntax error. */
    private final TextPosition position;

    /**
     * Constructor for the {@link SyntaxException} class.
     *
     * @param position The position information (possibly including source information) for the syntax error.
     */
    public SyntaxException(TextPosition position) {
        Assert.notNull(position);
        this.position = position;
    }

    /**
     * Returns the position information (possibly including source information) for the syntax error.
     *
     * @return The position information (possibly including source information) for the syntax error.
     */
    public TextPosition getPosition() {
        return position;
    }

    @Override
    public abstract String getMessage();
}
