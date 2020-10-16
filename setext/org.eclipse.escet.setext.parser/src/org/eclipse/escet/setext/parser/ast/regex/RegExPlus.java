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

/** Plus regular expression (one or more occurrences). */
public class RegExPlus extends RegEx {
    /** Child regular expression. */
    public final RegEx child;

    /**
     * Constructor for the {@link RegExPlus} class.
     *
     * @param child Child regular expression.
     * @param position Position information.
     */
    public RegExPlus(RegEx child, Position position) {
        super(position);
        this.child = child;
    }

    @Override
    public boolean acceptsEmptyString() {
        return child.acceptsEmptyString();
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
        // Choice, as it may match multiple times.
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
        return childTxt + "+";
    }
}
