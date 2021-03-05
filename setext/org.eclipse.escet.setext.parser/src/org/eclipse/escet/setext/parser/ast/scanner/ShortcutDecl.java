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

package org.eclipse.escet.setext.parser.ast.scanner;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.Decl;
import org.eclipse.escet.setext.parser.ast.regex.RegEx;

/** Shortcut declaration. Defines a named regular expression. This class is no longer used after type checking. */
public class ShortcutDecl extends Decl {
    /** The name of the shortcut (regular expression). */
    public final String name;

    /** The regular expression. */
    public final RegEx regEx;

    /** Is this shortcut used? This information is used internally by the type checker only. */
    public boolean used = false;

    /**
     * Constructor for the {@link ShortcutDecl}.
     *
     * @param name The name of the shortcut (regular expression).
     * @param regEx The regular expression.
     * @param position Position information.
     */
    public ShortcutDecl(String name, RegEx regEx, Position position) {
        super(position);
        this.name = name;
        this.regEx = regEx;
    }
}
