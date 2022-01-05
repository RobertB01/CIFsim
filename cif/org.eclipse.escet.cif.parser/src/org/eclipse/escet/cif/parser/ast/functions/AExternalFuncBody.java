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

package org.eclipse.escet.cif.parser.ast.functions;

import org.eclipse.escet.common.position.metamodel.position.Position;

/** External function body. */
public class AExternalFuncBody extends AFuncBody {
    /** A textual reference to the external function. */
    public final String functionRef;

    /**
     * Constructor for the {@link AExternalFuncBody} class.
     *
     * @param functionRef A textual reference to the external function.
     * @param position Position information.
     */
    public AExternalFuncBody(String functionRef, Position position) {
        super(position);
        this.functionRef = functionRef;
    }
}
