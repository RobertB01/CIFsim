//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.java;

/** Position of a contiguous area in a text file. */
public class TextPosition implements Comparable<TextPosition> {
    /**
     * The location of the source file that contains the position.
     *
     * <p>
     * Must be an absolute path with platform-specific path separators, but does not need to exist at the file system.
     * </p>
     */
    public final String location;

    /** Source identification (usually a filename), may be {@code null}. */
    public final String source;

    /**
     * The {@code 1}-based line index of the start (inclusive) of the position region, counted from the start of the
     * text.
     */
    public final int startLine;

    /**
     * The {@code 1}-based line index of the end (inclusive) of the position region, counted from the start of the text.
     */
    public final int endLine;

    /**
     * The {@code 1}-based column index of the start (inclusive) of the position region, counted from the start of the
     * text.
     */
    public final int startColumn;

    /**
     * The {@code 1}-based column index of the end (inclusive) of the position region, counted from the start of the
     * text.
     */
    public final int endColumn;

    /**
     * The {@code 0}-based byte index of the start (inclusive) of the position region, counted from the start of the
     * text.
     */
    public final int startOffset;

    /**
     * The {@code 0}-based byte index of the end (inclusive) of the position region, counted from the start of the text.
     */
    public final int endOffset;

    /**
     * Constructor of the {@link TextPosition} class.
     *
     * @param location The location of the source file that contains the position. Must be an absolute path with
     *     platform-specific path separators, but does not need to exist at the file system.
     * @param startLine The {@code 1}-based line index of the start (inclusive) of the position region, counted from the
     *     start of the text.
     * @param startColumn The {@code 1}-based column index of the start (inclusive) of the position region, counted from
     *     the start of the text.
     * @param endLine The {@code 1}-based line index of the end (inclusive) of the position region, counted from the
     *     start of the text.
     * @param endColumn The {@code 1}-based column index of the end (inclusive) of the position region, counted from the
     *     start of the text.
     * @param startOffset The {@code 0}-based byte index of the start (inclusive) of the position region, counted from
     *     the start of the text.
     * @param endOffset The {@code 0}-based byte index of the end (inclusive) of the position region, counted from the
     *     start of the text.
     */
    public TextPosition(String location, int startLine, int startColumn, int endLine, int endColumn, int startOffset,
            int endOffset)
    {
        this(location, null, startLine, startColumn, endLine, endColumn, startOffset, endOffset);
    }

    /**
     * Constructor of the {@link TextPosition} class.
     *
     * @param location The location of the source file that contains the position. Must be an absolute path with
     *     platform-specific path separators, but does not need to exist at the file system.
     * @param source Source identification (usually a filename), may be {@code null}.
     * @param startLine The {@code 1}-based line index of the start (inclusive) of the position region, counted from the
     *     start of the text.
     * @param startColumn The {@code 1}-based column index of the start (inclusive) of the position region, counted from
     *     the start of the text.
     * @param endLine The {@code 1}-based line index of the end (inclusive) of the position region, counted from the
     *     start of the text.
     * @param endColumn The {@code 1}-based column index of the end (inclusive) of the position region, counted from the
     *     start of the text.
     * @param startOffset The {@code 0}-based byte index of the start (inclusive) of the position region, counted from
     *     the start of the text.
     * @param endOffset The {@code 0}-based byte index of the end (inclusive) of the position region, counted from the
     *     start of the text.
     */
    public TextPosition(String location, String source, int startLine, int startColumn, int endLine, int endColumn,
            int startOffset, int endOffset)
    {
        this.location = location;
        this.source = source;
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
        this.startOffset = startOffset;
        this.endOffset = endOffset;

        Assert.check(startLine >= 1);
        Assert.check(startColumn >= 1);
        Assert.check(startOffset >= 0);
        Assert.check(endLine >= 1 && endLine >= startLine);
        Assert.check(endColumn >= 1);
        Assert.check(endOffset >= 0 && endOffset >= startOffset);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TextPosition)) {
            return false;
        }
        return compareTo((TextPosition)other) == 0;
    }

    @Override
    public int hashCode() {
        int hash = location.hashCode() + ((source == null) ? 0 : 13 * source.hashCode());
        hash += 17 * startLine + 23 * startColumn + 29 * startOffset + 37 * endLine + 41 * endColumn + 43 * endOffset;
        return hash;
    }

    @Override
    public int compareTo(TextPosition other) {
        if (other == null) {
            throw new NullPointerException();
        }

        // Compare the data of both positions, first the location and source.
        int result = location.compareTo(other.location);

        if (result == 0) {
            if (source == null) {
                if (other.source != null) {
                    return -1;
                }
                // Else fall-through to next comparison.
            } else {
                if (other.source == null) {
                    return 1;
                } else {
                    result = source.compareTo(other.source);
                }
            }
        }

        // If no decision has been made at this point, both positions are in the same file and thus should have the
        // same offset to line/column mapping function. Use the offsets first as that likely makes a quick decision.
        result = updateResult(result, startOffset, other.startOffset);
        result = updateResult(result, endOffset, other.endOffset);
        result = updateResult(result, startLine, other.startLine);
        result = updateResult(result, endLine, other.endLine);
        result = updateResult(result, startColumn, other.startColumn);
        result = updateResult(result, endColumn, other.endColumn);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        if (source != null) {
            txt.append(source);
        }
        if (startLine == endLine) {
            txt.append("line ");
            txt.append(startLine);
            if (startColumn == endColumn) {
                txt.append(", column ");
                txt.append(endColumn);
            } else {
                txt.append(", columns ");
                txt.append(startColumn);
                txt.append(" .. ");
                txt.append(endColumn);
            }
        } else {
            txt.append("line ");
            txt.append(startLine);
            txt.append(" column ");
            txt.append(startColumn);
            txt.append(" .. line ");
            txt.append(endLine);
            txt.append(" column ");
            txt.append(endColumn);
        }
        return txt.toString();
    }

    /**
     * If necessary compute an updated compare result, else return the old decision.
     *
     * @param compareResult Existing compare result.
     * @param value First value to compare against if the compare result needs more comparisons.
     * @param otherValue Second value to compare against if the compare result needs more comparisons.
     * @return The possibly updated compare result.
     */
    private int updateResult(int compareResult, int value, int otherValue) {
        if (compareResult == 0 && value != otherValue) {
            compareResult = (value < otherValue) ? -1 : 1;
        }
        return compareResult;
    }

    /**
     * Construct a dummy position containing the first character of the of the given location.
     *
     * @param location The location of the source file that contains the position. Must be an absolute path with
     *     platform-specific path separators, but does not need to exist at the file system.
     * @return The created position.
     */
    public static TextPosition createDummy(String location) {
        return createDummy(location, null);
    }

    /**
     * Construct a dummy position containing the first character of the of the given location and source.
     *
     * @param location The location of the source file that contains the position. Must be an absolute path with
     *     platform-specific path separators, but does not need to exist at the file system.
     * @param source Source identification (usually a filename), may be {@code null}.
     * @return The created position.
     */
    public static TextPosition createDummy(String location, String source) {
        return new TextPosition(location, source, 1, 1, 1, 1, 0, 0);
    }

    /**
     * Merges two positions and returns the merged position (the minimal position that spans both original positions).
     *
     * @param p1 The first position.
     * @param p2 The second position.
     * @return The merged position.
     */
    public static TextPosition mergePositions(TextPosition p1, TextPosition p2) {
        Assert.notNull(p1);
        Assert.notNull(p2);

        // Locations must be equal.
        Assert.check(p1.location.equals(p2.location));

        // Sources must be equal.
        Assert.check((p1.source == null) == (p2.source == null));
        if (p1.source != null && p2.source != null) {
            Assert.check(p1.source.equals(p2.source));
        }

        // Construct merged position.
        int startOffset, startLine, startColumn;
        if (p1.startOffset < p2.startOffset) {
            startOffset = p1.startOffset;
            startLine = p1.startLine;
            startColumn = p1.startColumn;
        } else {
            startOffset = p2.startOffset;
            startLine = p2.startLine;
            startColumn = p2.startColumn;
        }

        int endOffset, endLine, endColumn;
        if (p1.endOffset > p2.endOffset) {
            endOffset = p1.endOffset;
            endLine = p1.endLine;
            endColumn = p1.endColumn;
        } else {
            endOffset = p2.endOffset;
            endLine = p2.endLine;
            endColumn = p2.endColumn;
        }

        // Return merged position.
        return new TextPosition(p1.location, p1.source, startLine, startColumn, endLine, endColumn, startOffset,
                endOffset);
    }
}
