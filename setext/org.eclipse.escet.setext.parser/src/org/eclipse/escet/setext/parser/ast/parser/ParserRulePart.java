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

package org.eclipse.escet.setext.parser.ast.parser;

import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.Position;
import org.eclipse.escet.setext.parser.ast.SeTextObject;
import org.eclipse.escet.setext.parser.ast.Symbol;

/** A part of a {@link ParserRule parser rule} (or production). */
public class ParserRulePart extends SeTextObject {
    /** The name of the symbol for this part. */
    public final String name;

    /** The symbol for this part. Only available after type checking. */
    public Symbol symbol = null;

    /**
     * Whether to include this part in the arguments of the call-back method. After parsing, this represents the
     * callback annotations specified in the source, subject to defaults. After type checking, this represents whether
     * to include this part in the arguments of the call-back method, regardless of defaults.
     */
    public boolean callBackArg;

    /**
     * Constructor for the {@link ParserRulePart} class.
     *
     * @param name The name of the symbol for this part.
     * @param callBackArg Whether to include this part in the arguments of the call-back method. Only affects terminals.
     * @param position Position information.
     */
    public ParserRulePart(String name, boolean callBackArg, Position position) {
        super(position);
        this.name = name;
        this.callBackArg = callBackArg;
        Assert.notNull(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
