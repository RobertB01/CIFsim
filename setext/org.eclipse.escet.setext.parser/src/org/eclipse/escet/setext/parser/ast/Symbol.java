//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.setext.parser.ast;

import org.eclipse.escet.common.java.TextPosition;

/** SeText grammar symbol. */
public abstract class Symbol extends Decl {
    /** The name of the symbol. May be {@code null} for terminals. */
    public final String name;

    /**
     * Constructor for the {@link Symbol} class.
     *
     * @param name The name of the symbol. May be {@code null} for terminals.
     * @param position Position information.
     */
    public Symbol(String name, TextPosition position) {
        super(position);
        this.name = name;
    }
}
