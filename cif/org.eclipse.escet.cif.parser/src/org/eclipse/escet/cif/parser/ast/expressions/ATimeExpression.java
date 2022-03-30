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

import org.eclipse.escet.common.java.TextPosition;

/** Time variable reference expression. */
public class ATimeExpression extends AExpression {
    /**
     * Constructor for the {@Link ATimeExpression} class.
     *
     * @param position Position information.
     */
    public ATimeExpression(TextPosition position) {
        super(position);
    }
}
