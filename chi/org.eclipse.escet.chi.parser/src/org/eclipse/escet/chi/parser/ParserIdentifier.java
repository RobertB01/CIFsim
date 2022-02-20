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

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Temporary storage of an identifier and its position. */
public class ParserIdentifier {
    /** Name of the identifier. */
    public final String name;

    /** Position of the identifier in the source file. */
    public final Position pos;

    /**
     * Constructor of the {@link ParserIdentifier} class.
     *
     * @param name Name of the identifier.
     * @param pos Position of the name in the source file.
     */
    public ParserIdentifier(String name, Position pos) {
        this.name = name;
        this.pos = pos;
    }
}
