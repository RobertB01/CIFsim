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

import java.util.List;

import org.eclipse.escet.setext.runtime.Token;

/** Variable order or variable orderer. */
public class VarOrderOrOrdererSingleInstance extends VarOrderOrOrdererInstance {
    /** The name of the variable order(er). */
    public final Token name;

    /** The arguments of the variable order(er). */
    public final List<VarOrderOrOrdererArg> arguments;

    /** Whether argument parentheses were given ({@code true}) or just an identifier ({@code false}). */
    public final boolean hasArgs;

    /**
     * Constructor for the {@link VarOrderOrOrdererSingleInstance} class.
     *
     * @param name The name of the variable order(er).
     * @param arguments The arguments of the variable order(er).
     * @param hasArgs Whether argument parentheses were given ({@code true}) or just an identifier ({@code false}).
     */
    public VarOrderOrOrdererSingleInstance(Token name, List<VarOrderOrOrdererArg> arguments, boolean hasArgs) {
        super(name.position);
        this.name = name;
        this.arguments = arguments;
        this.hasArgs = hasArgs;
    }
}
