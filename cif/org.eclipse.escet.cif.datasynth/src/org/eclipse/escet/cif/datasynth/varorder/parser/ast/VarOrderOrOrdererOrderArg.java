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

import org.eclipse.escet.setext.runtime.Token;

/** Argument of a variable order(er) that has as value a variable order(er). */
public class VarOrderOrOrdererOrderArg extends VarOrderOrOrdererArg {
    /** The value of the argument. */
    public final VarOrderOrOrdererInstance value;

    /**
     * Constructor for the {@link VarOrderOrOrdererOrderArg} class.
     *
     * @param name The name of the argument.
     * @param value The value of the argument.
     */
    public VarOrderOrOrdererOrderArg(Token name, VarOrderOrOrdererInstance value) {
        super(name);
        this.value = value;
    }
}
