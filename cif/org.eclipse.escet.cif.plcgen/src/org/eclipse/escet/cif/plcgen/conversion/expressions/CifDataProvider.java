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

import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;

/** Provider of PLC equivalents of CIF data. */

/*
 * TODO: Currently this class is an empty shell listing discovered needed conversions for expression generation from CIF
 * complex components. It is not complete (for example initial locations and user-defined internal functions have not
 * been considered). It is also possible that the currently proposed functions here are non-optimal for implementing
 * this class.
 */
public abstract class CifDataProvider {
    /**
     * Return the PLC expression to get the value of the provided constant.
     *
     * @param constant Constant to access.
     * @return The expression to get the value of the constant in the PLC.
     */
    protected abstract PlcExpression getExprForConstant(Constant constant);

    /**
     * Return the PLC expression to access the provided discrete variable.
     *
     * @param variable Variable to access.
     * @return The expression to access the provided discrete variable.
     */
    protected abstract PlcExpression getExprForDiscVar(DiscVariable variable);

    /**
     * Return the PLC expression to access the value or the derivative of the provided continuous variable.
     *
     * @param variable Variable to access.
     * @param getDerivative Whether access to the derivative value is requested, otherwise access to the value of the
     *     variable itself is requested.
     * @return The expression to access a value of the provided continuous variable.
     */
    protected abstract PlcExpression getExprForContvar(ContVariable variable, boolean getDerivative);

    /**
     * Return the PLC expression to access the provided input variable.
     *
     * @param variable Variable to access.
     * @return The expression to access the provided input variable.
     * @note The returned expression may not allow writing to the variable.
     */
    protected abstract PlcExpression getExprForInputVar(InputVariable variable);
}
