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

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Optional regular expression (zero or one occurrence). */
public class RegExOpt extends RegEx {
    /** Child regular expression. */
    public final RegEx child;

    /**
     * Constructor for the {@link RegExOpt} class.
     *
     * @param child Child regular expression.
     * @param position Position information.
     */
    public RegExOpt(RegEx child, Position position) {
        super(position);
        this.child = child;
    }

    @Override
    public boolean acceptsEmptyString() {
        return true;
    }

    @Override
    public Set<Integer> getCodePoints() {
        return child.getCodePoints();
    }

    @Override
    public Set<RegExChar> getChars() {
        return child.getChars();
    }

    @Override
    public boolean isDescriptionText() {
        // Choice, as it is either included or it isn't.
        return false;
    }

    @Override
    public String getDescriptionText() {
        return null;
    }

    @Override
    public int getBindingStrength() {
        return 2;
    }

    @Override
    public String toString() {
        String childTxt = child.toString();
        int myStrength = getBindingStrength();
        int childStrength = child.getBindingStrength();
        if (myStrength > childStrength) {
            childTxt = "(" + childTxt + ")";
        }
        return childTxt + "?";
    }
}
