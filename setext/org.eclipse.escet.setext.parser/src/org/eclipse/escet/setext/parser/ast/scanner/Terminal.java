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

package org.eclipse.escet.setext.parser.ast.scanner;

import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Comparator;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.parser.ast.Identifier;
import org.eclipse.escet.setext.parser.ast.Symbol;
import org.eclipse.escet.setext.parser.ast.TerminalDescription;
import org.eclipse.escet.setext.parser.ast.regex.RegEx;
import org.eclipse.escet.setext.parser.ast.regex.RegExChar;

/** Terminal symbol, defined by means of a regular expression. */
public class Terminal extends Symbol {
    /**
     * The state in which to recognize this terminal, or {@code null} for the default state. Not set via the
     * constructor.
     */
    public Identifier state = null;

    /** The regular expression. */
    public final RegEx regEx;

    /**
     * The name of the Java method to invoke for post-processing on tokens for this terminal, or {@code null} if not
     * available or not applicable.
     */
    public final Identifier func;

    /**
     * The new state of the scanner after recognizing this terminal. May be {@code null} to indicate that the state
     * remains the same, and may be {@code ""} to go to the default state.
     */
    public final Identifier newState;

    /** The description of the terminal in end user readable text, or {@code null} if not available. */
    public final TerminalDescription description;

    /**
     * The priority of this terminal. The lower the value the higher the priority. Only available after type checking.
     */
    public int priority = -1;

    /**
     * Constructor for the {@link Terminal} class.
     *
     * @param name Name of the terminal, or {@code null} if no name.
     * @param regEx The regular expression.
     * @param func The name of the Java method to invoke for post-processing on tokens for this terminal, or
     *     {@code null} if not available or not applicable.
     * @param newState The new state of the scanner after recognizing this terminal. May be {@code null} to indicate
     *     that the state remains the same, and may be {@code ""} to go to the default state.
     * @param description The description of the terminal in end user readable text, or {@code null} if not available.
     * @param position Position information.
     */
    public Terminal(String name, RegEx regEx, Identifier func, Identifier newState, TerminalDescription description,
            TextPosition position)
    {
        super(name, position);
        this.regEx = regEx;
        this.func = func;
        this.newState = newState;
        this.description = description;

        Assert.notNull(regEx);
        boolean isEof = isEof();
        Assert.implies(isEof, name == null);
        Assert.implies(isEof, func == null);
        Assert.implies(isEof, newState == null);
        Assert.implies(isEof, description == null);
    }

    /**
     * Is this an end-of-file terminal?
     *
     * @return {@code true} if this terminal is an end-of-file terminal, {@code false} otherwise.
     */
    public boolean isEof() {
        return (regEx instanceof RegExChar) && ((RegExChar)regEx).isEof();
    }

    /**
     * Returns the state in which to recognize this terminal. For the default state, {@code ""} is returned.
     *
     * @return The state in which to recognize this terminal.
     * @see #state
     */
    public String getStateName() {
        return (state == null) ? "" : state.id;
    }

    /**
     * The new state of the scanner after recognizing this terminal. For the default state, {@code ""} is returned.
     *
     * @return The state in which to recognize this terminal.
     * @see #state
     */
    public String getNewStateName() {
        return (newState == null) ? getStateName() : newState.id;
    }

    @Override
    public String toString() {
        return fmt("Terminal(%s\"%s\" [\"%s\" -> \"%s\"]%s prio=%d)", (name == null) ? "" : name + "=",
                regEx.toString(), getStateName(), getNewStateName(), (func == null) ? "" : " {" + func.id + "}",
                priority);
    }

    /** Singleton instance of the {@link PriorityComparer} class. */
    public static final PriorityComparer PRIORITY_COMPARER = new PriorityComparer();

    /** Comparator for {@link Terminal} instances which orders ascendingly by priority. */
    protected static class PriorityComparer implements Comparator<Terminal> {
        @Override
        public int compare(Terminal o1, Terminal o2) {
            Integer prio1 = o1.priority;
            return prio1.compareTo(o2.priority);
        }
    }

    /** Singleton instance of the {@link NameComparer} class. */
    public static final NameComparer NAME_COMPARER = new NameComparer();

    /**
     * Comparator for {@link Terminal} instances which orders ascendingly by name. Terminals without name are sorted
     * before terminals with names.
     *
     * @see Strings#SORTER
     */
    protected static class NameComparer implements Comparator<Terminal> {
        @Override
        public int compare(Terminal o1, Terminal o2) {
            if (o1.name == null && o2.name == null) {
                return 0;
            }
            if (o1.name == null && o2.name != null) {
                return -1;
            }
            if (o1.name != null && o2.name == null) {
                return 1;
            }
            return Strings.SORTER.compare(o1.name, o2.name);
        }
    }
}
