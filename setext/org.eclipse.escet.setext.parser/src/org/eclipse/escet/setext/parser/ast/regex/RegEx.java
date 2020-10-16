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

package org.eclipse.escet.setext.parser.ast.regex;

import java.util.Set;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.SeTextObject;

/** Regular expression. */
public abstract class RegEx extends SeTextObject {
    /**
     * Constructor for the {@link RegEx} class.
     *
     * @param position Position information.
     */
    public RegEx(Position position) {
        super(position);
    }

    /**
     * Does the regular expression accept the empty string?
     *
     * @return {@code true} if the regular expression accepts the empty string, {@code false} otherwise.
     */
    public abstract boolean acceptsEmptyString();

    /**
     * Returns the Unicode code points of the character(s) that make up the alphabet of the regular expression. Negative
     * values represent (non-Unicode) marker characters. See also {@link RegExChar}.
     *
     * @return The Unicode code points of the character(s) that make up the alphabet of the regular expression.
     */
    public abstract Set<Integer> getCodePoints();

    /**
     * Returns the unique character nodes from the regular expression. Not all regular expressions support this
     * operation.
     *
     * @return The unique character nodes from the regular expression.
     * @throws UnsupportedOperationException If the regular expression does not support the operation.
     */
    public abstract Set<RegExChar> getChars();

    /**
     * Does this regular expression represent literal text that can be used as a terminal description? A regular
     * expression represents literal text that can be used as a terminal description, if there is no choice in the
     * regular expression, and the characters that can be recognized include only graphical characters (no markers, no
     * control characters, new lines, etc).
     *
     * @return {@code true} if the this regular expression represents literal text that can be used as a terminal
     *     description, {@code false} otherwise.
     * @see RegExChar#isMarker
     * @see Strings#isGraphicCodePoint
     */
    public abstract boolean isDescriptionText();

    /**
     * Returns the literal text that can be used as a terminal description, if this regular expression represents such
     * literal text, or {@code null} otherwise.
     *
     * @return The literal text that can be used as a terminal description, or {@code null}
     * @see #isDescriptionText
     */
    public abstract String getDescriptionText();

    @Override
    public abstract String toString();

    /**
     * Returns the binding strength of the regular expression. The binding strength defines the order in which sub
     * regular expressions are applied in a regular expression. Regular expressions with higher binding strength are
     * applied before expressions with lower binding strength.
     *
     * @return The binding strength of the regular expression.
     */
    public abstract int getBindingStrength();
}
