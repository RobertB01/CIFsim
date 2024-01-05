//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

import java.util.List;

import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.parser.ast.Identifier;
import org.eclipse.escet.setext.parser.ast.Symbol;

/** Terminals defined by means of a collection of keywords. This class is no longer used after type checking. */
public class KeywordsTerminal extends Symbol {
    /**
     * The state in which to recognize this terminal, or {@code null} for the default state. Not set via the
     * constructor.
     */
    public Identifier state = null;

    /** The keywords. */
    public final List<KeywordsIdentifier> keywords;

    /**
     * Constructor for the {@link KeywordsTerminal} class.
     *
     * @param name The name of the keywords group. If this name is used in the grammar, it is also a non-terminal.
     * @param keywords The keywords.
     * @param position Position information.
     */
    public KeywordsTerminal(String name, List<KeywordsIdentifier> keywords, TextPosition position) {
        super(name, position);
        this.keywords = keywords;
    }
}
