//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.common.box;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;

import org.eclipse.escet.common.java.Strings;

/** Box that holds lines of code, and supports indented code blocks. */
public abstract class CodeBox extends Box {
    /** The indentation amount, the amount of spaces to indent per indentation level. Must be a positive value. */
    private int indentAmount;

    /** The current indentation level. */
    private int indentLevel = 0;

    /**
     * Constructor for the {@link CodeBox} class, with a custom indentation amount.
     *
     * @param indentAmount The indentation amount, the amount of spaces to indent per indentation level. Must be a
     *     positive value.
     * @throws IllegalArgumentException If the given indentation amount is not a positive value.
     */
    public CodeBox(int indentAmount) {
        if (indentAmount <= 0) {
            String msg = "indentAmount must be positive";
            throw new IllegalArgumentException(msg);
        }
        this.indentAmount = indentAmount;
    }

    /**
     * Is the code box empty? Note that if an empty line is added, the code box is no longer considered empty.
     *
     * @return {@code true} if the code box is empty, {@code false} otherwise.
     */
    public abstract boolean isEmpty();

    /**
     * Sets the indentation amount, the amount of spaces to indent per indentation level.
     *
     * <p>
     * Note that code that is already added, is not affected by this change of the indentation amount.
     * </p>
     *
     * @param amount The indentation amount, the amount of spaces to indent per indentation level. Must be a positive
     *     value.
     * @throws IllegalArgumentException If the given indentation amount is not a positive value.
     */
    public void setIndentAmount(int amount) {
        if (amount <= 0) {
            String msg = "indentAmount must be positive";
            throw new IllegalArgumentException(msg);
        }
        indentAmount = amount;
    }

    /**
     * Returns the indentation amount, the amount of spaces to indent per indentation level. The amount is always
     * positive.
     *
     * @return The indentation amount.
     */
    public int getIndentAmount() {
        return indentAmount;
    }

    /** Increases the indentation level by one. */
    public void indent() {
        indentLevel++;
    }

    /**
     * Decreases the indentation level by one.
     *
     * @throws IllegalStateException If the current level is zero.
     */
    public void dedent() {
        if (indentLevel == 0) {
            throw new IllegalStateException("Can't dedent: level already 0.");
        }
        indentLevel--;
    }

    /**
     * Returns the current indentation level.
     *
     * @return The current indentation level.
     */
    public int getIndentLevel() {
        return indentLevel;
    }

    /**
     * Returns the current indentation.
     *
     * @return The current indentation.
     */
    public String getIndentation() {
        return Strings.spaces(indentLevel * indentAmount);
    }

    /**
     * Adds a line to the code box. The line is already processed (formatted, indented, etc), and should be added to the
     * code box 'as is'.
     *
     * <p>
     * Derived classes of {@link CodeBox} should implement this method to add the line to their own storage medium.
     * </p>
     *
     * @param line The line to add.
     */
    protected abstract void addLine(String line);

    /** Adds an empty line to the code. Does not use indentation. */
    public void add() {
        addLine("");
    }

    /**
     * Adds the given line of code. Indentation is automatically added at the start of the line. It is assumed that the
     * line of code does not contain new line characters.
     *
     * @param line The line of code to add.
     */
    public void add(String line) {
        if (indentLevel > 0) {
            line = getIndentation() + line;
        }
        addLine(line);
    }

    /**
     * Adds the given formatted line of code. Indentation is automatically added at the start of the line. It is assumed
     * that the line of code does not contain new line characters.
     *
     * @param line The line format pattern to use.
     * @param args The arguments to use for formatting.
     */
    public void add(String line, Object... args) {
        add(fmt(line, args));
    }

    /**
     * Adds the given lines of code. Indentation is automatically added at the start of each line. It is assumed that
     * the lines of code do not contain new line characters.
     *
     * @param lines The lines of code to add.
     */
    public void add(List<String> lines) {
        for (String line: lines) {
            add(line);
        }
    }

    /**
     * Adds the given lines of code. Indentation is automatically added at the start of each line. It is assumed that
     * the lines of code do not contain new line characters.
     *
     * @param lines The lines of code to add.
     */
    public void add(String[] lines) {
        for (String line: lines) {
            add(line);
        }
    }

    /**
     * Adds the lines of code from the given box. Indentation is automatically added at the start of each line. It is
     * assumed that the lines of code do not contain new line characters.
     *
     * @param box The box to use to get the lines of code to add.
     */
    public void add(Box box) {
        add(box.getLines());
    }

    /**
     * Adds the lines of code from the box representation of the given boxable object. Indentation is automatically
     * added at the start of each line. It is assumed that the lines of code do not contain new line characters.
     *
     * @param obj The boxable object to use to get the lines of code to add.
     */
    public void add(Boxable obj) {
        add(obj.toBox().getLines());
    }
}
