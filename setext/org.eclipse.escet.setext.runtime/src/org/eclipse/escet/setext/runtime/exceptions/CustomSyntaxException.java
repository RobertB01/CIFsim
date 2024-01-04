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

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.TextPosition;

/** Exception indicating a syntax error occurred. This is a {@link SyntaxException} with custom message. */
public class CustomSyntaxException extends SyntaxException {
    /** The message describing the syntax error. */
    private final String message;

    /**
     * Constructor for the {@link CustomSyntaxException} class.
     *
     * @param message Message describing the syntax error.
     * @param position The position information (possibly including source information) for the syntax error.
     */
    public CustomSyntaxException(String message, TextPosition position) {
        super(position);
        Assert.notNull(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        String src = getPosition().source;
        if (src == null) {
            src = "";
        }
        return fmt("%sSyntax error at line %d, column %d: %s", src, getPosition().startLine, getPosition().startColumn,
                message);
    }
}
