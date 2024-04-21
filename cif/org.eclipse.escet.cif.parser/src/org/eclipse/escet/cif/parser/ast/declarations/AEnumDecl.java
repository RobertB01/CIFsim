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

package org.eclipse.escet.cif.parser.ast.declarations;

import java.util.List;

import org.eclipse.escet.cif.parser.ast.ADecl;
import org.eclipse.escet.cif.parser.ast.annotations.AAnnotation;
import org.eclipse.escet.cif.parser.ast.tokens.AAnnotatedIdentifier;
import org.eclipse.escet.common.java.TextPosition;

/** Enumeration declaration. */
public class AEnumDecl extends ADecl {
    /** The annotations of the enumeration. */
    public final List<AAnnotation> annotations;

    /** The name of the enumeration. */
    public final String name;

    /** The enumeration literals that are part of the enumeration. */
    public final List<AAnnotatedIdentifier> literals;

    /**
     * Constructor for the {@link AEnumDecl} class.
     *
     * @param annotations The annotations of the enumeration.
     * @param name The name of the enumeration.
     * @param literals The enumeration literals that are part of the enumeration.
     * @param position Position information.
     */
    public AEnumDecl(List<AAnnotation> annotations, String name, List<AAnnotatedIdentifier> literals,
            TextPosition position)
    {
        super(position);
        this.annotations = annotations;
        this.name = name;
        this.literals = literals;
    }
}
