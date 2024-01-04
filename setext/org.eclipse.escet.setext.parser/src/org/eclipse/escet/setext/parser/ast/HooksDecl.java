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

package org.eclipse.escet.setext.parser.ast;

import org.eclipse.escet.common.java.TextPosition;
import org.eclipse.escet.setext.parser.ast.parser.JavaType;

/** Hooks class declaration. Specifies the class where to find the implementation of the scanner/parser hook methods. */
public class HooksDecl extends Decl {
    /** The hooks Java class. */
    public final JavaType hooksClass;

    /**
     * Constructor for the {@link HooksDecl} class.
     *
     * @param hooksClass The hooks Java class.
     * @param position Position information.
     */
    public HooksDecl(JavaType hooksClass, TextPosition position) {
        super(position);
        this.hooksClass = hooksClass;
    }
}
