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

package org.eclipse.escet.cif.parser.ast.automata;

import java.util.List;

import org.eclipse.escet.cif.parser.ast.ADecl;
import org.eclipse.escet.cif.parser.ast.tokens.AIdentifier;
import org.eclipse.escet.common.java.TextPosition;

/** Location. */
public class ALocation extends ADecl {
    /** The name of the location, or {@code null}. */
    public final AIdentifier name;

    /** The elements of the location, or {@code null}. */
    public final List<ALocationElement> elements;

    /**
     * Constructor for the {@link ALocation} class.
     *
     * @param name The name of the location, or {@code null}.
     * @param elements The elements of the location, or {@code null}.
     * @param position Position information.
     */
    public ALocation(AIdentifier name, List<ALocationElement> elements, TextPosition position) {
        super(position);
        this.name = name;
        this.elements = elements;
    }
}
