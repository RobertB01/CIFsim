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

package org.eclipse.escet.setext.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.TextPosition;

/** Syntax warning information. */
public class SyntaxWarning implements Comparable<SyntaxWarning> {
    /** The message describing the syntax warning. */
    public final String message;

    /** Position information. */
    public final TextPosition position;

    /**
     * Constructor for the {@link SyntaxWarning} class.
     *
     * @param message The message describing the syntax warning.
     * @param position Position information.
     */
    public SyntaxWarning(String message, TextPosition position) {
        Assert.notNull(message);
        Assert.notNull(position);
        this.message = message;
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        SyntaxWarning other = (SyntaxWarning)obj;
        return position.equals(other.position) && message.equals(other.message);
    }

    @Override
    public int hashCode() {
        return position.hashCode() ^ message.hashCode();
    }

    @Override
    public int compareTo(SyntaxWarning other) {
        int rslt = 0;

        // Compare position.
        rslt = position.compareTo(other.position);
        if (rslt != 0) {
            return rslt;
        }

        // Compare message.
        return message.compareTo(other.message);
    }

    @Override
    public String toString() {
        String src = position.source;
        if (src == null) {
            src = "";
        }
        return fmt("%sSyntax warning at line %d, column %d: %s", src, position.startLine, position.startColumn,
                message);
    }
}
