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

import java.util.List;

import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.Decl;
import org.eclipse.escet.setext.parser.ast.Identifier;
import org.eclipse.escet.setext.parser.ast.Symbol;

/**
 * Terminals declaration. Declares terminals with the same priority. This class is no longer used after type checking.
 */
public class TerminalsDecl extends Decl {
    /** The terminal symbols to recognize. */
    public final List<Symbol> terminals;

    /** The state in which to recognize these terminal, or {@code null} for the default state. */
    public final Identifier state;

    /**
     * Constructor for the {@link TerminalsDecl} class.
     *
     * @param state The state in which to recognize these terminal, or {@code null} for the default state.
     * @param terminals The terminal symbols to recognize.
     * @param position Position information.
     */
    public TerminalsDecl(Identifier state, List<Symbol> terminals, Position position) {
        super(position);
        this.state = state;
        this.terminals = terminals;

        // Set state for each of the symbols.
        for (Symbol terminal: terminals) {
            if (terminal instanceof Terminal) {
                ((Terminal)terminal).state = state;
            } else if (terminal instanceof KeywordsTerminal) {
                ((KeywordsTerminal)terminal).state = state;
            } else {
                throw new RuntimeException("Unknown terminal: " + terminal);
            }
        }
    }
}
