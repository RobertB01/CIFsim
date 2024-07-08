//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.generators;

import static org.eclipse.escet.common.java.Lists.last;
import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

/**
 * A class for handling a collection of text discussing various topics.
 *
 * <p>
 * It has lines of text, where a consecutive number of non-empty lines discusses one 'topic'. Different topics are
 * separated by a single empty line.
 * </p>
 * <p>
 * The form of an empty line can be configured.
 * </p>
 */
public class TextTopics {
    /** Already added text lines. */
    private final List<String> lines = list();

    /** The definition of the empty line in the text. */
    private final String emptyLine;

    /** Constructor of the {@link TextTopics} class. Uses {@code ""} as empty line. */
    public TextTopics() {
        this("");
    }

    /**
     * Constructor of the {@link TextTopics} class.
     *
     * @param emptyLine The definition of the empty line in the text.
     */
    public TextTopics(String emptyLine) {
        this.emptyLine = emptyLine.stripTrailing();
    }

    /**
     * Append a line of text.
     *
     * <p>
     * Adding an empty line is skipped if the stored lines already have an empty line at the end. See
     * {@link #isLastEmpty()} for the precise definition.
     * </p>
     *
     * @param line The line of text to add. Trailing whitespace is removed.
     */
    public void add(String line) {
        line = line.stripTrailing();

        // Non-empty lines are always allowed, empty lines are allowed only if there is a non-empty line before it.
        if (!line.equals(emptyLine) || !isLastEmpty()) {
            lines.add(line);
        }
    }

    /**
     * Add many lines at once. Equivalent to adding each line separately.
     *
     * @param lines Lines to add.
     */
    public void addAll(List<String> lines) {
        // For simplicity, process each line separately.
        for (String line: lines) {
            add(line);
        }
    }

    /**
     * Append a line of text after applying {@link String#format} on the given format and arguments.
     *
     * <p>
     * Adding an empty line fails silently if the stored lines already have an empty line at the end. See
     * {@link #isLastEmpty()} for the precise definition.
     * </p>
     *
     * @param format Format of the line to add.
     * @param args Arguments to insert in the format.
     */
    public void add(String format, Object... args) {
        add(fmt(format, args));
    }

    /**
     * Convenience method to (try to) append an empty line. Fails silently if the stored lines already have an empty
     * line at the end. See {@link #isLastEmpty()} for the precise definition.
     */
    public void appendEmpty() {
        if (!isLastEmpty()) {
            lines.add(emptyLine);
        }
    }

    /**
     * Ensure that the lines have an appropriate empty line at the end, reliably allowing to add lines on the next
     * topic.
     */
    public void ensureEmptyAtEnd() {
        appendEmpty(); // Relies on having the check for allowing empty lines in 'appendEmpty'.
    }

    /**
     * Ensure that the lines do not have an empty line at the end.
     *
     * <p>
     * Here, having no lines at all counts as non-empty.
     * </p>
     */
    public void dropEmptyAtEnd() {
        if (!lines.isEmpty() && last(lines).equals(emptyLine)) {
            int lastLineIndex = lines.size() - 1;
            lines.remove(lastLineIndex);
        }
    }

    /**
     * Retrieve the added lines of text.
     *
     * @return The added lines of text.
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Does the last line count as an empty line?
     *
     * @return Whether the last added line is an empty line or no lines have been added at all.
     */
    public boolean isLastEmpty() {
        return lines.isEmpty() || last(lines).equals(emptyLine);
    }

    /**
     * Get the number of lines in the text topics. For best results, first apply {@link #ensureEmptyAtEnd} or
     * {@link #dropEmptyAtEnd} to eliminate unwanted layout.
     *
     * @return The number of text lines in the text topics.
     */
    public int size() {
        return lines.size();
    }
}
