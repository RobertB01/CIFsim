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

import static org.eclipse.escet.common.java.Sets.set;

import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.eclipse.escet.common.position.metamodel.position.Position;

/**
 * A sequence of characters for a regular expression character class.
 *
 * @see RegExCharClass
 */
public class RegExCharSeq extends RegExChars {
    /** The start character of the character sequence. */
    public final RegExChar start;

    /** The end character of the character sequence. */
    public final RegExChar end;

    /**
     * Constructor for the {@link RegExCharSeq} class.
     *
     * @param start The start character of the character sequence.
     * @param end The end character of the character sequence.
     * @param position Position information.
     */
    public RegExCharSeq(RegExChar start, RegExChar end, Position position) {
        super(position);
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean acceptsEmptyString() {
        // Either it accepts nothing, or it accepts a single character.
        return false;
    }

    @Override
    public Set<Integer> getCodePoints() {
        Set<Integer> rslt = set();
        for (int i = start.character; i <= end.character; i++) {
            rslt.add(i);
        }
        return rslt;
    }

    @Override
    public Set<RegExChar> getChars() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDescriptionText() {
        // Choice, so not always the same text. Note that we don't check the
        // individual characters to see whether there is only one of them, or
        // whether they are the same.
        return false;
    }

    @Override
    public String getDescriptionText() {
        return null;
    }

    @Override
    public int getBindingStrength() {
        // Binding strength is irrelevant for this class.
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return start.toString() + "-" + end.toString();
    }
}
