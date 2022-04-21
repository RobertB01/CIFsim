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

package org.eclipse.escet.setext.parser.ast.regex;

import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Set;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.java.TextPosition;

/** Single character regular expression (matches the exact character). */
public class RegExChar extends RegExChars {
    /**
     * The character to match, as Unicode code point. Negative values represent (non-Unicode) marker characters. In
     * particular, value {@code -1} represents the end-of-file marker, and other negative values represent custom
     * (situation dependent) markers.
     */
    public final int character;

    /**
     * Constructor for the {@link RegExChar} class.
     *
     * @param character The character to match, as Unicode code point. Negative values represent special (non-Unicode)
     *     characters. See also {@link #character} for the meaning of negative values.
     * @param position Position information.
     */
    public RegExChar(int character, TextPosition position) {
        super(position);
        this.character = character;
    }

    @Override
    public boolean acceptsEmptyString() {
        // Accepts a single character only.
        return false;
    }

    @Override
    public Set<Integer> getCodePoints() {
        return set(character);
    }

    @Override
    public Set<RegExChar> getChars() {
        return set(this);
    }

    @Override
    public boolean isDescriptionText() {
        if (isCustomMarker()) {
            // Custom markers are used internally only, and we shouldn't
            // invoke this method for them.
            throw new RuntimeException("Unexpected custom marker: " + this);
        } else if (isEof()) {
            // Not a graphic code point.
            return false;
        } else if (Strings.isGraphicCodePoint(character)) {
            return true;
        } else {
            // Not a graphic code point.
            return false;
        }
    }

    @Override
    public String getDescriptionText() {
        if (isCustomMarker()) {
            // Custom markers are used internally only, and we shouldn't
            // invoke this method for them.
            throw new RuntimeException("Unexpected custom marker: " + this);
        } else if (isEof()) {
            // Not a graphic code point.
            return null;
        } else if (Strings.isGraphicCodePoint(character)) {
            return Strings.codePointToStr(character);
        } else {
            // Not a graphic code point.
            return null;
        }
    }

    /**
     * Does this regular expression character represent the end-of-file marker?
     *
     * @return {@code true} if this regular expression character represents the end-of-file marker, {@code false}
     *     otherwise.
     */
    public boolean isEof() {
        return character == -1;
    }

    /**
     * Does this regular expression character represent a (non-Unicode) marker character?
     *
     * @return {@code true} if this regular expression character represents a (non-Unicode) marker character,
     *     {@code false} otherwise.
     */
    public boolean isMarker() {
        return character < 0;
    }

    /**
     * Does this regular expression character represent a (non-Unicode) custom marker character?
     *
     * @return {@code true} if this regular expression character represents a (non-Unicode) custom marker character,
     *     {@code false} otherwise.
     */
    public boolean isCustomMarker() {
        return character < -1;
    }

    @Override
    public int getBindingStrength() {
        return 3;
    }

    @Override
    public String toString() {
        switch (character) {
            case -1:
                // Pilcrow character for EOF.
                return "\u00B6";
            case '\n':
                return "\\n";
            case '\t':
                return "\\t";
            case '\r':
                return "\\r";
            case '"':
            case '(':
            case ')':
            case '*':
            case '+':
            case '?':
            case '[':
            case ']':
            case '^':
            case '-':
            case '|':
            case '{':
            case '}':
                // Needs escaping.
                return "\\" + (char)character;
            default:
                if (character < 0) {
                    // Put custom marker number between guillemets.
                    return fmt("\u00AB%d\u00BB", character);
                } else {
                    // Simply convert Unicode code point to Java string.
                    return Strings.codePointToStr(character);
                }
        }
    }
}
