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

import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcVarExpression;

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
     * @param constant Constant to read.
     * @return The expression to read the value of the constant in the PLC.
     */
    public abstract PlcExpression getValueForConstant(Constant constant);

    /**
     * Return the PLC expression to read the value of the provided discrete variable.
     *
     * @param variable Variable to read.
     * @return The expression to read the value of the provided discrete variable.
     */
    public abstract PlcExpression getValueForDiscVar(DiscVariable variable);

    /**
     * Return the PLC expression to write to the provided discrete variable.
     *
     * @param variable Variable to write.
     * @return The expression to write the provided discrete variable.
     */
    public abstract PlcVarExpression getAddressableForDiscVar(DiscVariable variable);

    /**
     * Return the PLC expression to read the value or the derivative of the provided continuous variable.
     *
     * @param variable Variable to read.
     * @param getDerivative Whether read to the derivative value is requested, otherwise read to the value of the
     *     variable itself is requested.
     * @return The expression to read the value or its derivative of the provided continuous variable.
     */
    public abstract PlcExpression getValueForContvar(ContVariable variable, boolean getDerivative);

    /**
     * Return the PLC expression to write to the value of the provided continuous variable.
     *
     * @param variable The continuous variable to write to.
     * @param writeDerivative Whether a write to the derivative of the variable is requested, otherwise write to the
     *     value of the variable itself is requested.
     * @return The expression to write a value or its derivative into the provided continuous variable.
     */
    public abstract PlcVarExpression getAddressableForContvar(ContVariable variable, boolean writeDerivative);

    /**
     * Return the PLC expression to access the provided input variable.
     *
     * @param variable Variable to read.
     * @return The expression to read the value of the provided input variable.
     */
    public abstract PlcExpression getValueForInputVar(InputVariable variable);

    /**
     * Return the PLC expression to write to the provided input variable.
     *
     * @param variable Variable to write.
     * @return The expression to write a value into the provided input variable.
     */
    public abstract PlcVarExpression getAddressableForInputVar(InputVariable variable);
}
