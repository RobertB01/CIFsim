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
import org.eclipse.escet.cif.parser.ast.tokens.AIdentifier;
import org.eclipse.escet.cif.parser.ast.types.ACifType;
import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.runtime.Token;

/** Events declaration. */
public class AEventDecl extends ADecl {
    /** The annotations of the event declaration. */
    public final List<AAnnotation> annotations;

    /** The controllability of the events, or {@code null} if not specified. */
    public final Token controllability;

    /** The names of the events. */
    public final List<AIdentifier> names;

    /** The type of the event, or {@code null} if no type. */
    public final ACifType type;

    /**
     * Constructor for the {@link AEventDecl} class.
     *
     * @param annotations The annotations of the event declaration.
     * @param controllability The controllability of the events, or {@code null} if not specified.
     * @param names The names of the events.
     * @param type The type of the event, or {@code null} if no type.
     * @param position Position information.
     */
    public AEventDecl(List<AAnnotation> annotations, Token controllability, List<AIdentifier> names, ACifType type,
            TextPosition position)
    {
        super(position);
        this.annotations = annotations;
        this.controllability = controllability;
        this.names = names;
        this.type = type;
    }
}
