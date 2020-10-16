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

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Dot regular expression (matches any character except newline). */
public class RegExDot extends RegEx {
    /**
     * Constructor for the {@link RegExDot} class.
     *
     * @param position Position information.
     */
    public RegExDot(Position position) {
        super(position);
    }

    @Override
    public boolean acceptsEmptyString() {
        // Accepts a single character only.
        return false;
    }

    @Override
    public Set<Integer> getCodePoints() {
        Set<Integer> rslt = set();
        for (int c = 0; c <= 127; c++) {
            if (c == '\n') {
                continue;
            }
            rslt.add(c);
        }
        return rslt;
    }

    @Override
    public Set<RegExChar> getChars() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDescriptionText() {
        // Choice, as it matches many characters.
        return false;
    }

    @Override
    public String getDescriptionText() {
        return null;
    }

    @Override
    public int getBindingStrength() {
        return 3;
    }

    @Override
    public String toString() {
        return ".";
    }
}
