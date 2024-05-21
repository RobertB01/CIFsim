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
import org.eclipse.escet.cif.parser.ast.tokens.AIdentifier;
import org.eclipse.escet.cif.parser.ast.types.ACifType;
import org.eclipse.escet.common.java.TextPosition;

/** Algebraic parameter. */
public class AAlgParameter extends AParameter {
    /** The annotations of the algebraic parameters. */
    public final List<AAnnotation> annotations;

    /** The type of the parameters. */
    public final ACifType type;

    /** The names of the parameters. */
    public final List<AIdentifier> names;

    /**
     * Constructor for the {@link AAlgParameter} class.
     *
     * @param annotations The annotations of the algebraic parameters.
     * @param type The type of the parameters.
     * @param names The names of the parameters.
     * @param position Position information.
     */
    public AAlgParameter(List<AAnnotation> annotations, ACifType type, List<AIdentifier> names, TextPosition position) {
        super(position);
        this.annotations = annotations;
        this.type = type;
        this.names = names;
    }
}
