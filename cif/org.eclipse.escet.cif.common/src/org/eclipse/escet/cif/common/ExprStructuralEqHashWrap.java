//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.common;

import static org.eclipse.escet.cif.common.CifValueUtils.areStructurallySameExpression;
import static org.eclipse.escet.cif.common.CifValueUtils.hashExpr;

import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;

/** CIF expression wrapper class, for proper hashing and equality. */
public class ExprStructuralEqHashWrap {
    /** The wrapped expression. */
    public final Expression expr;

    /**
     * Constructor for the {@link ExprStructuralEqHashWrap}.
     *
     * @param expr The wrapped expression.
     */
    public ExprStructuralEqHashWrap(Expression expr) {
        this.expr = expr;
    }

    @Override
    public int hashCode() {
        return hashExpr(expr);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        ExprStructuralEqHashWrap other = (ExprStructuralEqHashWrap)obj;
        return areStructurallySameExpression(this.expr, other.expr);
    }
}
