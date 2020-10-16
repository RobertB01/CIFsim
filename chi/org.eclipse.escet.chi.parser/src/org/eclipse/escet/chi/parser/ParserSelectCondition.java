//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2020 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.chi.parser;

import org.eclipse.escet.chi.metamodel.chi.Expression;
import org.eclipse.escet.chi.metamodel.chi.Statement;
import org.eclipse.escet.common.position.metamodel.position.Position;

/** Temporary storage of a select condition. */
public class ParserSelectCondition {
    /** Guard of the select condition, {@code null} if no guard available. */
    public final Expression expr;

    /** Statement of the select condition. */
    public final Statement stat;

    /**
     * Return the position at the left of the condition.
     *
     * @return the left position.
     */
    public Position getLeftPosition() {
        if (expr != null) {
            return expr.getPosition();
        }
        return stat.getPosition();
    }

    /**
     * Constructor of the {@link ParserSelectCondition} class.
     *
     * @param expr Guard of the select condition, {@code null} if no guard available.
     * @param stat Statement of the select condition.
     */
    public ParserSelectCondition(Expression expr, Statement stat) {
        this.expr = expr;
        this.stat = stat;
    }
}
