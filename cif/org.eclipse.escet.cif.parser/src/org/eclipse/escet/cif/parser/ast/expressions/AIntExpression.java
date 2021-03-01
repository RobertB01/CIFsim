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

/** Integer literal expression. */
public class AIntExpression extends AExpression {
    /** The value of the integer literal expression, as scanned text. */
    public final String value;

    /**
     * Constructor for the {@link AIntExpression} class.
     *
     * @param value The value of the integer literal expression, as scanned text.
     * @param position Position information.
     */
    public AIntExpression(String value, Position position) {
        super(position);
        this.value = value;
    }
}
