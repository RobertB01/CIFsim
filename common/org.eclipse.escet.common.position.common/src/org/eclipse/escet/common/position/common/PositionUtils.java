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

package org.eclipse.escet.common.position.common;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.common.position.metamodel.position.PositionFactory;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** {@link Position} class helper methods. */
public class PositionUtils {
    /** Constructor for the {@link PositionUtils} class. */
    private PositionUtils() {
        // Static class.
    }

    /**
     * Creates and returns a dummy {@link Position} object, covering the first character in the input.
     *
     * @param location The location of the source file that contains the position. Must be an absolute local file system
     *     path, with platform specific path separators. The path does not have to refer to an existing file.
     * @param src Source information, or {@code null} if not available.
     * @return {@link Position} covering the first character of the input.
     */
    public static Position createDummy(String location, String src) {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setSource(src);
        pos.setLocation(location);
        pos.setStartLine(1);
        pos.setEndLine(1);
        pos.setStartColumn(1);
        pos.setEndColumn(1);
        pos.setStartOffset(0);
        pos.setEndOffset(0);
        return pos;
    }

    /**
     * Creates and returns a copy of the position information of the given object.
     *
     * @param obj The object from which to get the position information to copy.
     * @return The copied position information, or {@code null} if {@code obj} has no position information.
     * @see #copyPosition(Position)
     */
    public static Position copyPosition(PositionObject obj) {
        return copyPosition(obj.getPosition());
    }

    /**
     * Creates and returns a copy of the given position information. This method performs better than using EMF deep
     * cloning, as this method uses a dedicated implementation.
     *
     * @param pos The position information to copy, or {@code null}.
     * @return The copied position information, or {@code null} if {@code pos} is {@code null}.
     */
    public static Position copyPosition(Position pos) {
        if (pos == null) {
            return pos;
        }

        Position pos2 = PositionFactory.eINSTANCE.createPosition();
        pos2.setSource(pos.getSource());
        pos2.setLocation(pos.getLocation());
        pos2.setStartLine(pos.getStartLine());
        pos2.setEndLine(pos.getEndLine());
        pos2.setStartColumn(pos.getStartColumn());
        pos2.setEndColumn(pos.getEndColumn());
        pos2.setStartOffset(pos.getStartOffset());
        pos2.setEndOffset(pos.getEndOffset());
        return pos2;
    }

    /**
     * Returns a human-readable description of the position information of the given object.
     *
     * @param obj The object with the position information.
     * @return A human-readable description of the position information of the given object, or
     *     {@code "<no-position-available>"} if the object has no position information.
     */
    public static String pos2str(PositionObject obj) {
        Position pos = obj.getPosition();
        if (pos == null) {
            return "<no-position-available>";
        }
        return pos2str(pos);
    }

    /**
     * Returns a human-readable description of the position information. Does not includes the source file location.
     *
     * @param position The position information.
     * @return A human-readable description of the position information.
     */
    public static String pos2str(Position position) {
        StringBuilder txt = new StringBuilder();
        if (position.getSource() != null) {
            txt.append(position.getSource());
        }
        if (position.getStartLine() == position.getEndLine()) {
            txt.append(fmt("line %d", position.getStartLine()));
            if (position.getStartColumn() == position.getEndColumn()) {
                txt.append(fmt(", column %d", position.getStartColumn()));
            } else {
                txt.append(fmt(", columns %d .. %d", position.getStartColumn(), position.getEndColumn()));
            }
        } else {
            txt.append(fmt("line %d column %d .. line %d column %d", position.getStartLine(), position.getStartColumn(),
                    position.getEndLine(), position.getEndColumn()));
        }
        return txt.toString();
    }

    /**
     * Returns the length of the range covered by the position information. New line characters are included in the
     * length. Tab characters count as one character.
     *
     * @param position The position for which to return the length. May cover multiple lines.
     * @return The length of the range covered by the position information.
     */
    public static int length(Position position) {
        return position.getEndOffset() - position.getStartOffset() + 1;
    }

    /**
     * Creates a new position for a sub-range covered by the given position.
     *
     * @param orig The original position. Must not span multiple lines.
     * @param offset The 0-based offset, from the start of the original position. Must be in the range
     *     {@code [0 .. length(orig))}.
     * @param length The length of the sub-range. Must be in the range {@code [1 .. length(orig) - offset]}.
     * @return The new position, for the sub-range of the original position.
     */
    public static Position getSubRange(Position orig, int offset, int length) {
        // Precondition checking.
        Assert.check(orig.getStartLine() == orig.getEndLine());
        int origLength = length(orig);
        Assert.check(0 <= offset);
        Assert.check(offset < origLength);
        Assert.check(1 <= length);
        Assert.check(length <= origLength - offset);

        // Copy, restrict range, return.
        Position rslt = copyPosition(orig);
        rslt.setStartOffset(rslt.getStartOffset() + offset);
        rslt.setEndOffset(rslt.getStartOffset() + length - 1);
        rslt.setStartColumn(rslt.getStartColumn() + offset);
        rslt.setEndColumn(rslt.getStartColumn() + length - 1);
        return rslt;
    }

    /**
     * Convert a {@link TextPosition} to a {@link Position}.
     *
     * @param textPos Position to copy.
     * @return A fresh copy of the provided position.
     */
    public static Position toPosition(TextPosition textPos) {
        Position pos = PositionFactory.eINSTANCE.createPosition();
        pos.setSource(textPos.source);
        pos.setLocation(textPos.location);
        pos.setStartLine(textPos.startLine);
        pos.setEndLine(textPos.endLine);
        pos.setStartColumn(textPos.startColumn);
        pos.setEndColumn(textPos.endColumn);
        pos.setStartOffset(textPos.startOffset);
        pos.setEndOffset(textPos.endOffset);
        return pos;
    }

    /**
     * Convert a {@link Position} to a {@link TextPosition}.
     *
     * @param pos Position to copy.
     * @return The created position.
     */
    public static TextPosition toTextPosition(Position pos) {
        return new TextPosition(pos.getLocation(), pos.getSource(), pos.getStartLine(), pos.getStartColumn(),
                pos.getEndLine(), pos.getEndColumn(), pos.getStartOffset(), pos.getEndOffset());
    }
}
