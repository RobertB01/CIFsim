//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2021 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.parser.ast.expressions;

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Automaton 'self' reference expression. */
public class ASelfExpression extends AExpression {
    /**
     * Constructor for the {@Link ASelfExpression} class.
     *
     * @param position Position information.
     */
    public ASelfExpression(Position position) {
        super(position);
    }
}
