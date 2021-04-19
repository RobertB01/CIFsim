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

package org.eclipse.escet.setext.runtime;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.common.PositionUtils;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Syntax warning information. */
public class SyntaxWarning implements Comparable<SyntaxWarning> {
    /** The message describing the syntax warning. */
    public final String message;

    /** Position information. */
    public final Position position;

    /**
     * Constructor for the {@link SyntaxWarning} class.
     *
     * @param message The message describing the syntax warning.
     * @param position Position information.
     */
    public SyntaxWarning(String message, Position position) {
        Assert.notNull(message);
        Assert.notNull(position);
        this.message = message;
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        SyntaxWarning other = (SyntaxWarning)obj;
        return PositionUtils.equalPositions(position, other.position) && message.equals(other.message);
    }

    @Override
    public int hashCode() {
        return PositionUtils.hashPosition(position) ^ message.hashCode();
    }

    @Override
    public int compareTo(SyntaxWarning other) {
        int rslt = 0;

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
        return fmt("%sSyntax warning at line %d, column %d: %s", src,
                position.getStartLine(), position.getStartColumn(), message);
    }
}
