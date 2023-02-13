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

/** Argument of a variable order(er) that has as value a list of variable order(er)s. */
public class VarOrderOrOrdererListOrdersArg extends VarOrderOrOrdererArg {
    /** The value of the argument. */
    public final List<VarOrderOrOrdererInstance> value;

    /**
     * Constructor for the {@link VarOrderOrOrdererListOrdersArg} class.
     *
     * @param name The name of the argument.
     * @param value The value of the argument.
     */
    public VarOrderOrOrdererListOrdersArg(Token name, List<VarOrderOrOrdererInstance> value) {
        super(name);
        this.value = value;
    }
}
