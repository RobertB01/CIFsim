//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2022 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.typechecker.declwrap;

import org.eclipse.escet.cif.metamodel.cif.Invariant;
import org.eclipse.escet.cif.parser.ast.AInvariant;
import org.eclipse.escet.cif.parser.ast.tokens.AName;

/**
 * The necessary information to type check a named and nameless invariant. For named ones, instances of this class are
 * wrapped in an {@link InvDeclWrap} for inclusion into the symbol table.
 */
public class InvariantInfo {
    /** The CIF AST representation of the invariant. */
    public final AInvariant astInv;

    /** The CIF AST name of the referenced event, or {@code null} for state invariants. */
    public final AName event;

    /** The CIF metamodel representation of the invariant. */
    public final Invariant mmInv;

    /**
     * Constructor for the {@link InvariantInfo} class.
     *
     * @param astInv The CIF AST representation of the invariant.
     * @param event The CIF AST name of the referenced event, or {@code null} for state invariants.
     * @param mmInv The CIF metamodel representation of the invariant.
     */
    public InvariantInfo(AInvariant astInv, AName event, Invariant mmInv) {
        this.astInv = astInv;
        this.event = event;
        this.mmInv = mmInv;
    }
}
