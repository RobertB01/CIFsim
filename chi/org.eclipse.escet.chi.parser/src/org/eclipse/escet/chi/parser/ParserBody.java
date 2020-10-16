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

package org.eclipse.escet.chi.parser;

import java.util.List;

import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.chi.metamodel.chi.VariableDeclaration;

/** Temporary storage of the components of a body. */
public class ParserBody {
    /** Variables of the body. */
    public final List<VariableDeclaration> vardefs;

    /** Statements of the body. */
    public final List<Statement> stats;

    /**
     * Constructor of the {@link ParserBody} class.
     *
     * @param vardefs Variables of the body.
     * @param stats Statements of the body.
     */
    public ParserBody(List<VariableDeclaration> vardefs, List<Statement> stats) {
        this.vardefs = vardefs;
        this.stats = stats;
    }
}
