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

import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.metamodel.cif.declarations.ContVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.InputVariable;
import org.eclipse.escet.cif.plcgen.model.expressions.PlcExpression;

/** Provider of PLC equivalents of CIF variables, location pointers and locations. */
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
     * @note The expression returned for a derivative cannot be assumed to be writable.
     */
    protected abstract PlcExpression getExprForContvar(ContVariable variable, boolean getDerivative);
    // TODO: Extend the plc target with a way to obtain the writability status of derivatives.

    /**
     * Return the PLC expression for deciding whether the automaton owning the given location is at the time of
     * evaluation in that location.
     *
     * @param location Location being queried.
     * @return A boolean expression expressing whether the owning automaton of the location is in that location.
     */
    protected abstract PlcExpression getExprForLocation(Location location);

    /**
     * Return the PLC expression to access the provided input variable.
     *
     * @param variable Variable to access.
     * @return The expression to access the provided input variable.
     * @note The returned expression may not allow writing to the variable.
     */
    protected abstract PlcExpression getExprForInputVar(InputVariable variable);
}
