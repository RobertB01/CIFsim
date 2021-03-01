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

import java.util.Set;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.scanner.ShortcutDecl;

/**
 * Shortcut regular expression (matches the regular expression defined by the shortcut}.
 *
 * @see ShortcutDecl
 */
public class RegExShortcut extends RegEx {
    /** The name of the shortcut. */
    public final String name;

    /** The shortcut. Only available after type checking. */
    public ShortcutDecl shortcut = null;

    /**
     * Constructor for the {@link RegExShortcut} class.
     *
     * @param name The name of the shortcut.
     * @param position Position information.
     */
    public RegExShortcut(String name, Position position) {
        super(position);
        this.name = name;
    }

    @Override
    public boolean acceptsEmptyString() {
        return shortcut.regEx.acceptsEmptyString();
    }

    @Override
    public Set<Integer> getCodePoints() {
        return shortcut.regEx.getCodePoints();
    }

    @Override
    public Set<RegExChar> getChars() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDescriptionText() {
        return shortcut.regEx.isDescriptionText();
    }

    @Override
    public String getDescriptionText() {
        return shortcut.regEx.getDescriptionText();
    }

    @Override
    public int getBindingStrength() {
        return 3;
    }

    @Override
    public String toString() {
        return "{" + name + "}";
    }
}
