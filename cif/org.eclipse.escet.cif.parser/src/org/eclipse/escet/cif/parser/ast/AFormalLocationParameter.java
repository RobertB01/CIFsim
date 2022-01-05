//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.parser.ast.tokens.AIdentifier;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Formal location parameter. */
public class AFormalLocationParameter extends AFormalParameter {
    /** The names of the parameters. */
    public final List<AIdentifier> names;

    /**
     * Constructor for the {@link AFormalLocationParameter} class.
     *
     * @param names The names of the parameters.
     * @param position Position information.
     */
    public AFormalLocationParameter(List<AIdentifier> names, Position position) {
        super(position);
        this.names = names;
    }
}
