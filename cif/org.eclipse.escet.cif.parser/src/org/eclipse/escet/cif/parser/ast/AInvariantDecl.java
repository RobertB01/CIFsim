//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.parser.ast;

import java.util.List;

import org.eclipse.escet.cif.parser.ast.annotations.AAnnotation;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.runtime.Token;

/** Invariant declaration. */
public class AInvariantDecl extends ADecl {
    /** The annotations of the invariants. */
    public final List<AAnnotation> annotations;

    /** The supervisory kind of the invariants, or {@code null} if not specified. */
    public final Token kind;

    /** The invariants. */
    public final List<AInvariant> invariants;

    /**
     * Constructor for the {@link AInvariantDecl} class.
     *
     * @param annotations The annotations of the invariants.
     * @param kind The supervisory kind of the invariants, or {@code null} if not specified.
     * @param invariants Invariants.
     * @param position Position information.
     */
    public AInvariantDecl(List<AAnnotation> annotations, Token kind, List<AInvariant> invariants,
            TextPosition position)
    {
        super(position);
        this.annotations = annotations;
        this.kind = kind;
        this.invariants = invariants;
        Assert.check(!invariants.isEmpty());
    }
}
