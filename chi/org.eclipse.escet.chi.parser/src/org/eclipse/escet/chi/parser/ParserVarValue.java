//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.parser;

import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Temporary storage of a variable name and its value. */
public class ParserVarValue extends ParserIdentifier {
    /** Value of the variable, may be {@code null}. */
    public final Expression expr;

    /**
     * Constructor of the {@link ParserVarValue} class.
     *
     * @param name Variable name.
     * @param pos Position of the name in the source file.
     * @param expr Value of the variable, may be {@code null}.
     */
    public ParserVarValue(String name, Position pos, Expression expr) {
        super(name, pos);
        this.expr = expr;
    }
}
