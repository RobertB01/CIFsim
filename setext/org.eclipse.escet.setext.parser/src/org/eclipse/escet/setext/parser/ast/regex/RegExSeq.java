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

import java.util.List;
import java.util.Set;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.Sets;

/** Sequence regular expression. Matches the regular expressions, one after the other. */
public class RegExSeq extends RegEx {
    /** The sequence of regular expressions. */
    public final List<RegEx> sequence;

    /**
     * Constructor for the {@link RegExSeq} class.
     *
     * @param sequence The sequence of regular expressions.
     */
    public RegExSeq(List<RegEx> sequence) {
        super(null);
        this.sequence = sequence;
        Assert.check(sequence.size() >= 2);
    }

    @Override
    public boolean acceptsEmptyString() {
        // Sequence accepts empty string if all parts accept the empty string.
        for (RegEx part: sequence) {
            if (!part.acceptsEmptyString()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Set<Integer> getCodePoints() {
        Set<Integer> rslt = null;
        for (RegEx part: sequence) {
            Set<Integer> partRslt = part.getCodePoints();
            rslt = (rslt == null) ? partRslt : Sets.union(rslt, partRslt);
        }
        return rslt;
    }

    @Override
    public Set<RegExChar> getChars() {
        Set<RegExChar> rslt = null;
        for (RegEx part: sequence) {
            Set<RegExChar> partRslt = part.getChars();
            rslt = (rslt == null) ? partRslt : Sets.union(rslt, partRslt);
        }
        return rslt;
    }

    @Override
    public boolean isDescriptionText() {
        for (RegEx child: sequence) {
            if (!child.isDescriptionText()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getDescriptionText() {
        StringBuilder txt = new StringBuilder();
        for (RegEx child: sequence) {
            String childTxt = child.getDescriptionText();
            if (childTxt == null) {
                return null;
            }
            txt.append(childTxt);
        }
        return txt.toString();
    }

    @Override
    public int getBindingStrength() {
        return 1;
    }

    @Override
    public String toString() {
        int myStrength = getBindingStrength();
        StringBuilder rslt = new StringBuilder();
        boolean first = true;
        for (RegEx part: sequence) {
            String partTxt = part.toString();
            int partStrength = part.getBindingStrength();
            if (myStrength > partStrength || (myStrength == partStrength && !first)) {
                // Local operator has higher binding strength, or any non-first
                // has same binding strength but is left-associative.
                rslt.append("(" + partTxt + ")");
            } else {
                rslt.append(partTxt);
            }
            first = false;
        }
        return rslt.toString();
    }
}
