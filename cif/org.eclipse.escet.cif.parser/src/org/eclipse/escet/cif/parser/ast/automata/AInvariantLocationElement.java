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

package org.eclipse.escet.cif.parser.ast.automata;

import java.util.List;

import org.eclipse.escet.cif.parser.ast.AInvariant;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.runtime.Token;

/** Invariants of a location. */
public class AInvariantLocationElement extends ALocationElement {
    /** The supervisory kind of the invariants, or {@code null} if not specified. */
    public final Token kind;

    /** The invariants of the location. */
    public final List<AInvariant> invariants;

    /**
     * Constructor for the {@link AInvariantLocationElement} class.
     *
     * @param kind The supervisory kind of the invariants, or {@code null} if not specified.
     * @param invariants The invariants of the location.
     * @param position Position information.
     */
    public AInvariantLocationElement(Token kind, List<AInvariant> invariants, TextPosition position) {
        super(position);
        this.kind = kind;
        this.invariants = invariants;
    }
}
