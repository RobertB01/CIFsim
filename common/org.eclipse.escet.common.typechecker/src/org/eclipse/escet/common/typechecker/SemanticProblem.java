//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.common.PositionUtils;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Semantic problem information. */
public class SemanticProblem implements Comparable<SemanticProblem> {
    /** The message describing the semantic problem. */
    public final String message;

    /** The severity of the problem. */
    public final SemanticProblemSeverity severity;

    /** Position information. */
    public final Position position;

    /**
     * Constructor for the {@link SemanticProblem} class.
     *
     * @param message The message describing the semantic problem.
     * @param severity The severity of the problem.
     * @param position Position information.
     */
    public SemanticProblem(String message, SemanticProblemSeverity severity, Position position) {
        Assert.notNull(message);
        Assert.notNull(position);
        this.message = message;
        this.severity = severity;
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        SemanticProblem other = (SemanticProblem)obj;
        return severity == other.severity && //
                PositionUtils.equalPositions(position, other.position) && //
                message.equals(other.message);
    }

    @Override
    public int hashCode() {
        return severity.hashCode() ^ //
                PositionUtils.hashPosition(position) ^ //
                message.hashCode();
    }

    @Override
    public int compareTo(SemanticProblem other) {
        // Compare severity.
        int rslt = severity.compareTo(other.severity);
        if (rslt != 0) {
            return rslt;
        }

        // Compare position.
        String src1 = position.getSource();
        String src2 = other.position.getSource();
        if (src1 == null && src2 != null) {
            return -1;
        }
        if (src1 != null && src2 == null) {
            return 1;
        }
        if (src1 != null && src2 != null) {
            rslt = Strings.SORTER.compare(src1, src2);
            if (rslt != 0) {
                return rslt;
            }
        }

        rslt = Strings.SORTER.compare(position.getLocation(), other.position.getLocation());
        if (rslt != 0) {
            return rslt;
        }

        rslt = Integer.valueOf(position.getStartOffset()).compareTo(other.position.getStartOffset());
        if (rslt != 0) {
            return rslt;
        }

        rslt = Integer.valueOf(position.getEndOffset()).compareTo(other.position.getEndOffset());
        if (rslt != 0) {
            return rslt;
        }

        // Compare message.
        return Strings.SORTER.compare(message, other.message);
    }

    @Override
    public String toString() {
        String src = position.getSource();
        if (src == null) {
            src = "";
        }
        return fmt("%sSemantic %s at line %d, column %d: %s", src, severity.name().toLowerCase(Locale.US),
                position.getStartLine(), position.getStartColumn(), message);
    }
}
