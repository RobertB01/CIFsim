//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2023, 2024 Contributors to the Eclipse Foundation
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

import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;

/** Storage of a write-only converted CIF expression. */
public class ExprAddressableResult extends ExprGenResult<PlcVarExpression, ExprAddressableResult> {
    /** CIF variable that is updated. */
    public final Declaration varDecl;

    /**
     * Whether the derivative of {@link #varDecl} is updated. Is only valid if {@link #varDecl} is a continuous
     * variable.
     */
    private final boolean isDerivative;

    /**
     * Constructor of the {@link ExprAddressableResult} class.
     *
     * @param varDecl CIF variable that is updated.
     * @param isDerivative Whether the derivative of {@link #varDecl} is updated. Is only valid if {@link #varDecl} is a
     *     continuous variable.
     * @param generator Expression generator managing the temporary variables.
     * @param parentResults Results of child sub-expressions. Their code, code variables and value variables are copied
     *     into this result in the specified order.
     */
    public ExprAddressableResult(Declaration varDecl, boolean isDerivative, ExprGenerator generator,
            ExprGenResult<?, ?>... parentResults)
    {
        super(generator, parentResults);
        this.varDecl = varDecl;
        this.isDerivative = isDerivative;
    }

    /**
     * Return whether a derivative is updated in the result.
     *
     * @return Whether a derivative is updated in the result.
     */
    public boolean isDerivativeAssigned() {
        return isDerivative && varDecl instanceof ContVariable;
    }
}
