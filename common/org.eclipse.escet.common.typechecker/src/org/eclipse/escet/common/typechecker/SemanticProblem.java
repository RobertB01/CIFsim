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

package org.eclipse.escet.common.typechecker;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Locale;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.TextPosition;

/** Semantic problem information. */
public class SemanticProblem implements Comparable<SemanticProblem> {
    /** The message describing the semantic problem. */
    public final String message;

    /** The severity of the problem. */
    public final SemanticProblemSeverity severity;

    /** Position information. */
    public final TextPosition position;

    /**
     * Constructor for the {@link SemanticProblem} class.
     *
     * @param message The message describing the semantic problem.
     * @param severity The severity of the problem.
     * @param position Position information.
     */
    public SemanticProblem(String message, SemanticProblemSeverity severity, TextPosition position) {
        Assert.notNull(message);
        Assert.notNull(position);
        this.message = message;
        this.severity = severity;
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        SemanticProblem other = (SemanticProblem)obj;
        return severity == other.severity && position.equals(other.position) && message.equals(other.message);
    }

    @Override
    public int hashCode() {
        return severity.hashCode() ^ position.hashCode() ^ message.hashCode();
    }

    @Override
    public int compareTo(SemanticProblem other) {
        // Compare severity.
        int result = severity.compareTo(other.severity);
        if (result != 0) {
            return result;
        }

        // Compare position.
        result = position.compareTo(other.position);
        if (result != 0) {
            return result;
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
        return fmt("%sSemantic %s at line %d, column %d: %s", src, severity.name().toLowerCase(Locale.US),
                position.startLine, position.startColumn, message);
    }
}
