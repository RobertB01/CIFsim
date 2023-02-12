//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.datasynth.varorder.parser.ast;

import org.eclipse.escet.common.java.Strings;
import org.eclipse.escet.setext.runtime.Token;

/** String argument of a variable order(er). */
public class VarOrderOrOrdererStringArg extends VarOrderOrOrdererArg {
    /** The value of the argument. */
    public final Token value;

    /** String text, excluding the surrounding quotes, and with escape sequences replaced by their actual characters. */
    public final String text;

    /**
     * Constructor for the {@link VarOrderOrOrdererStringArg} class.
     *
     * @param name The name of the argument.
     * @param value The value of the argument.
     */
    public VarOrderOrOrdererStringArg(Token name, Token value) {
        super(name);
        this.value = value;
        this.text = Strings.unescape(Strings.slice(value.text, 1, -1));
    }
}
