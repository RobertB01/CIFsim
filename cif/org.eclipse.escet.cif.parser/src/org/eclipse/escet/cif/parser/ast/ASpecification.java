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
import org.eclipse.escet.common.java.TextPosition;

/** Specification. */
public class ASpecification extends ACifObject {
    /** The annotations of the specification. */
    public final List<AAnnotation> annotations;

    /** The body of the specification. */
    public final AGroupBody body;

    /**
     * Constructor for the {@link ASpecification} class.
     *
     * @param annotations The annotations of the specification.
     * @param body The body of the specification.
     * @param position Position information.
     */
    public ASpecification(List<AAnnotation> annotations, AGroupBody body, TextPosition position) {
        super(position);
        this.annotations = annotations;
        this.body = body;
    }
}
