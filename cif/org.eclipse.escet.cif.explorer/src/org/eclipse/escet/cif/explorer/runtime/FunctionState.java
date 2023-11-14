//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2023 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.explorer.runtime;

import static org.eclipse.escet.common.java.Lists.list;
import static org.eclipse.escet.common.java.Maps.mapc;
import static org.eclipse.escet.common.java.Strings.fmt;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.escet.cif.common.CifEvalException;
import org.eclipse.escet.cif.common.CifTextUtils;
import org.eclipse.escet.cif.common.CifValueUtils;
import org.eclipse.escet.cif.metamodel.cif.automata.Location;
import org.eclipse.escet.cif.metamodel.cif.declarations.AlgVariable;
import org.eclipse.escet.cif.metamodel.cif.declarations.DiscVariable;
import org.eclipse.escet.cif.metamodel.cif.expressions.Expression;
import org.eclipse.escet.cif.metamodel.cif.functions.FunctionParameter;
import org.eclipse.escet.cif.metamodel.cif.functions.InternalFunction;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.common.app.framework.exceptions.InvalidModelException;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.position.metamodel.position.PositionObject;

/** {@link BaseState} for functions. */
public class FunctionState extends BaseState {
    /** The internal user-defined function for which to store the state. */
    public final InternalFunction func;

    /** Mapping from function parameters and local variables to indices into {@link #values}. */
    public final Map<DiscVariable, Integer> indices;

    /**
     * Constructor of the {@link FunctionState} class.
     *
     * @param explorer Managing object of the exploration.
     * @param func The internal user-defined function for which to store the state.
     * @param argValues The values of the arguments, as passed by the function call, in the order of the parameters
     *     of the function.
     * @param varValues A {@code null} value for each of the local variables of the function.
     */
    public FunctionState(Explorer explorer, InternalFunction func, Object[] argValues, Object[] varValues) {
        super(explorer, new Location[0], ArrayUtils.addAll(argValues, varValues));

        // Store function.
        this.func = func;

        // Compute indices.
        this.indices = mapc(values.length);
        int i = 0;
        for (FunctionParameter param: func.getParameters()) {
            indices.put(param.getParameter(), i);
            i++;
        }
        for (DiscVariable var: func.getVariables()) {
            indices.put(var, i);
            i++;
        }
    }

    /**
     * Constructor of the {@link FunctionState} class.
     *
     * @param state The function state to copy.
     */
    public FunctionState(FunctionState state) {
        super(state.explorer, new Location[0], state.values.clone());
        this.func = state.func;
        this.indices = state.indices;
    }

    @Override
    public boolean isInitial() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMarked() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getVarValue(PositionObject var) {
        // Get parameter/variable index.
        Integer idx = indices.get(var);
        Assert.notNull(idx);

        // If already has a value, use that value.
        Object value = values[idx];
        if (value != null) {
            return value;
        }

        // Has no value yet. Must be a local variable.
        Assert.check(!(var.eContainer() instanceof FunctionParameter));
        DiscVariable localVar = (DiscVariable)var;

        // Compute initial value.
        Expression valueExpr;
        if (localVar.getValue() != null) {
            Assert.check(localVar.getValue().getValues().size() == 1);
            valueExpr = localVar.getValue().getValues().get(0);
        } else {
            CifType type = localVar.getType();
            valueExpr = CifValueUtils.getDefaultValue(type, list());
        }

        try {
            value = eval(valueExpr, null);
        } catch (CifEvalException ex) {
            String msg = fmt("Failed to compute initial value of variable \"%s\".", CifTextUtils.getAbsName(var));
            throw new InvalidModelException(msg, ex);
        }

        // Store initial value.
        values[idx] = value;

        // Return initial value.
        return value;
    }

    @Override
    public void setVarValue(PositionObject var, Object value) {
        Assert.notNull(value);
        int index = indices.get(var);
        values[index] = value;
    }

    @Override
    public Location getCurrentLocation(int autIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Expression getAlgExpression(AlgVariable algVar) {
        throw new UnsupportedOperationException();
    }
}
