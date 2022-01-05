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

package org.eclipse.escet.setext.runtime.exceptions;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Exception indicating a parse error occurred. */
public class ParseException extends SyntaxException {
    /** The current token text, at which parsing failed. May be {@code null} to indicate premature end of input. */
    private final String tokenText;

    /**
     * An end user readable description of the current terminal, at which parsing failed. May be {@code null} if not
     * available.
     */
    private final String foundTermText;

    /**
     * An end user readable description of the terminals expected, instead of the current terminal. Is {@code null} if
     * and only if {@link #foundTermText} is {@code null}.
     */
    private final String expectedTermsText;

    /**
     * Constructor for the {@link ParseException} class.
     *
     * @param tokenText The current token text, at which parsing failed. May be {@code null} to indicate premature end
     *     of input.
     * @param position The position information (possibly including source information) for the parse error.
     */
    public ParseException(String tokenText, Position position) {
        this(tokenText, position, null, null);
    }

    /**
     * Constructor for the {@link ParseException} class.
     *
     * @param tokenText The current token text, at which parsing failed. May be {@code null} to indicate premature end
     *     of input.
     * @param position The position information (possibly including source information) for the parse error.
     * @param foundTermText An end user readable description of the current terminal, at which parsing failed. May be
     *     {@code null} if not available.
     * @param expectedTermsText An end user readable description of the terminals expected, instead of the current
     *     terminal. Is {@code null} if and only if {@code foundTermText} is {@code null}.
     */
    public ParseException(String tokenText, Position position, String foundTermText, String expectedTermsText) {
        super(position);
        this.tokenText = tokenText;
        this.foundTermText = foundTermText;
        this.expectedTermsText = expectedTermsText;
        Assert.check((foundTermText == null) == (expectedTermsText == null));
    }

    /**
     * Returns the current token text, at which parsing failed. May be {@code null} to indicate premature end of input.
     *
     * @return The current token text, or {@code null}.
     */
    public String getTokenText() {
        return tokenText;
    }

    @Override
    public String getMessage() {
        String src = getPosition().getSource();
        if (src == null) {
            src = "";
        }
        String postToken = (tokenText == null) ? ", most likely due to premature end of input"
                : fmt(", at or near \"%s\"", tokenText);
        String postTerms = (foundTermText == null) ? ""
                : fmt(" (found %s, expected %s)", foundTermText, expectedTermsText);
        return fmt("%sParsing failed at line %d, column %d%s%s.", src, getPosition().getStartLine(),
                getPosition().getStartColumn(), postToken, postTerms);
    }
}
