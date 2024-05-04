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

package org.eclipse.escet.cif.parser.ast.functions;

import java.util.List;

import org.eclipse.escet.cif.parser.ast.ACifObject;
import org.eclipse.escet.cif.parser.ast.annotations.AAnnotation;
import org.eclipse.escet.cif.parser.ast.tokens.AIdentifier;
import org.eclipse.escet.cif.parser.ast.types.ACifType;

/** Function parameters. */
public class AFuncParam extends ACifObject {
    /** The annotations of the function parameters. */
    public final List<AAnnotation> annotations;

    /** The type of the parameters. */
    public final ACifType type;

    /** The names of the function parameters. */
    public final List<AIdentifier> names;

    /**
     * Constructor for the {@link AFuncParam} class.
     *
     * @param annotations The annotations of the function parameters.
     * @param type The type of the parameters.
     * @param names The names of the function parameters.
     */
    public AFuncParam(List<AAnnotation> annotations, ACifType type, List<AIdentifier> names) {
        super(null);
        this.annotations = annotations;
        this.type = type;
        this.names = names;
    }
}
