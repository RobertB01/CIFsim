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

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Sets.set;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.List;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Character class regular expression (matches any of the characters in the class). */
public class RegExCharClass extends RegEx {
    /** Whether the class is negated. */
    public final boolean negated;

    /** The characters that make up the class. Is never empty. */
    public final List<RegExChars> chars;

    /**
     * Constructor for the {@link RegExCharClass} class.
     *
     * @param negated Whether the class is negated.
     * @param chars The characters that make up the class. Must not be empty.
     * @param position Position information.
     */
    public RegExCharClass(boolean negated, List<RegExChars> chars, Position position) {
        super(position);
        this.negated = negated;
        this.chars = chars;
        Assert.check(!chars.isEmpty());
    }

    @Override
    public boolean acceptsEmptyString() {
        // Either it accepts nothing, or it accepts a single character.
        return false;
    }

    @Override
    public Set<Integer> getCodePoints() {
        Set<Integer> rslt = set();
        if (negated) {
            for (int i = 0; i <= 127; i++) {
                rslt.add(i);
            }
            for (RegExChars part: chars) {
                rslt.removeAll(part.getCodePoints());
            }
        } else {
            for (RegExChars part: chars) {
                rslt.addAll(part.getCodePoints());
            }
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
        return 3;
    }

    @Override
    public String toString() {
        List<String> txts = list();
        for (RegExChars cs: chars) {
            txts.add(cs.toString());
        }
        return fmt("[%s%s]", negated ? "^" : "", String.join("", txts));
    }
}
