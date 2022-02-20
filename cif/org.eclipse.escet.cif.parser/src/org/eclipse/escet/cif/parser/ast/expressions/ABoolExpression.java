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

package org.eclipse.escet.cif.parser.ast.expressions;

import org.eclipse.escet.common.position.metamodel.position.Position;

/** Boolean literal expression. */
public class ABoolExpression extends AExpression {
    /** The value of the boolean literal expression. */
    public final boolean value;

    /**
     * Constructor for the {@link ABoolExpression} class.
     *
     * @param value The value of the boolean expression.
     * @param position Position information.
     */
    public ABoolExpression(boolean value, Position position) {
        super(position);
        this.value = value;
    }
}
