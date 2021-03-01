//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

/** Alternatives regular expression (matches any of the alternatives). */
public class RegExAlts extends RegEx {
    /** The alternatives of the alternatives regular expression. */
    public final List<RegEx> alts;

    /**
     * Constructor for the {@link RegExAlts} class.
     *
     * @param alts The alternatives of the alternatives regular expression.
     */
    public RegExAlts(List<RegEx> alts) {
        super(null);
        this.alts = alts;
        Assert.check(alts.size() >= 2);
    }

    @Override
    public boolean acceptsEmptyString() {
        // Alternatives accepts empty string if any of the alternatives accepts
        // the empty string.
        for (RegEx alt: alts) {
            if (alt.acceptsEmptyString()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<Integer> getCodePoints() {
        Set<Integer> rslt = null;
        for (RegEx alt: alts) {
            Set<Integer> altRslt = alt.getCodePoints();
            rslt = (rslt == null) ? altRslt : Sets.union(rslt, altRslt);
        }
        return rslt;
    }

    @Override
    public Set<RegExChar> getChars() {
        Set<RegExChar> rslt = null;
        for (RegEx alt: alts) {
            Set<RegExChar> altRslt = alt.getChars();
            rslt = (rslt == null) ? altRslt : Sets.union(rslt, altRslt);
        }
        return rslt;
    }

    @Override
    public boolean isDescriptionText() {
        // Choice, so not always the same text. Note that we don't check the
        // alternatives to see whether they are the same.
        return false;
    }

    @Override
    public String getDescriptionText() {
        return null;
    }

    @Override
    public int getBindingStrength() {
        return 0;
    }

    @Override
    public String toString() {
        int myStrength = getBindingStrength();
        StringBuilder rslt = new StringBuilder();
        boolean first = true;
        for (RegEx part: alts) {
            if (!first) {
                rslt.append("|");
            }
            String partTxt = part.toString();
            int partStrength = part.getBindingStrength();
            if (myStrength > partStrength || (myStrength == partStrength && !first)) {
                // Local operator has higher binding strength, or any non-first
                // has same binding strength but is is left-associative.
                rslt.append("(" + partTxt + ")");
            } else {
                rslt.append(partTxt);
            }
            first = false;
        }
        return rslt.toString();
    }
}
