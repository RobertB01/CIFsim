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

package org.eclipse.escet.setext.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.TextPosition;

/** Scanner token. */
public class Token {
    /** The scanned text, or {@code null} for end-of-file. This text may have been post-processed. */
    public String text;

    /** The original scanned text, or {@code null} for end-of-file. This text has not been post-processed. */
    public final String originalText;

    /** The unique id of the recognized terminal. */
    public final int id;

    /** The position information for the scanned text. May be {@code null} if not available. */
    public final TextPosition position;

    /**
     * Constructor for the {@link Token} class.
     *
     * @param text The scanned text, or {@code null} for end-of-file.
     * @param id The unique id of the recognized terminal.
     * @param position The position information for the scanned text. May be {@code null} if not available.
     */
    public Token(String text, int id, TextPosition position) {
        this.text = text;
        this.originalText = text;
        this.id = id;
        this.position = position;
    }

    /**
     * Is this an end-of-file token?
     *
     * @return {@code true} if this token is an end-of-file token, {@code false} otherwise.
     */
    public boolean isEof() {
        return text == null;
    }

    @Override
    public String toString() {
        // Position information source and start/end offset not included in
        // output.
        String posTxt;
        if (position == null) {
            posTxt = "";
        } else {
            posTxt = position.startLine + ":" + position.startColumn + "-" + position.endLine + ":"
                    + position.endColumn;
        }
        return fmt("Token(%d, %s, %s)", id, Strings.stringToJava(text), posTxt);
    }
}
