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

package org.eclipse.escet.cif.plcgen.conversion.expressions;

import java.util.List;

import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;

/** Storage of a write-only converted CIF expression. */
public class ExprAddressableResult extends ExprGenResult<PlcVarExpression> {
    /**
     * Constructor of the {@link ExprAddressableResult} class.
     *
     * @param generator Expression generator managing the temporary variables.
     * @param parentResults Results of child sub-expressions. Their code, code variables and value variables are copied
     *     into this result in the specified order.
     */
    public ExprAddressableResult(ExprGenerator generator, List<ExprGenResult<?>> parentResults) {
        super(generator, parentResults);
    }

    /**
     * Constructor of the {@link ExprAddressableResult} class.
     *
     * @param generator Expression generator managing the temporary variables.
     */
    public ExprAddressableResult(ExprGenerator generator) {
        super(generator);
    }

    /**
     * Daisy-chain method to set the value of the result to the provided expression.
     *
     * @param plcExpr Value of the result to store.
     * @return The called instance.
     */
    public ExprAddressableResult setValue(PlcVarExpression plcExpr) {
        value = plcExpr;
        return this;
    }
}
