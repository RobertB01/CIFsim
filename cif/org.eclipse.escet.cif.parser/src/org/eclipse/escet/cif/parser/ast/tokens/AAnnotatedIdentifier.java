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

package org.eclipse.escet.cif.parser.ast.tokens;

import java.util.List;

import org.eclipse.escet.cif.parser.ast.ACifObject;
import org.eclipse.escet.cif.parser.ast.annotations.AAnnotation;
import org.eclipse.escet.common.java.TextPosition;

/** Annotated identifier token. */
public class AAnnotatedIdentifier extends ACifObject {
    /** The annotations of the identifier. */
    public final List<AAnnotation> annotations;

    /** Identifier, without any {@code $} characters. */
    public final String id;

    /**
     * Constructor for the {@link AAnnotatedIdentifier} class.
     *
     * @param annotations The annotations of the identifier.
     * @param id Identifier. May include {@code $} characters.
     * @param position Position information.
     */
    public AAnnotatedIdentifier(List<AAnnotation> annotations, String id, TextPosition position) {
        super(position);
        this.annotations = annotations;
        this.id = id.replace("$", "");
    }
}
