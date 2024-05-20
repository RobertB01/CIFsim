//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2021, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.controllercheck.mdd;

import static org.eclipse.escet.cif.common.CifTypeUtils.getUpperBound;
import static org.eclipse.escet.cif.common.CifTypeUtils.getVariableType;
import static org.eclipse.escet.cif.common.CifTypeUtils.isRangeless;

import org.eclipse.escet.cif.common.CifTypeUtils;
import org.eclipse.escet.cif.metamodel.cif.declarations.Declaration;
import org.eclipse.escet.cif.metamodel.cif.types.BoolType;
import org.eclipse.escet.cif.metamodel.cif.types.CifType;
import org.eclipse.escet.cif.metamodel.cif.types.IntType;
import org.eclipse.escet.common.java.Assert;
import org.eclipse.escet.common.multivaluetrees.VarInfoBuilder;

/** MDD {@link VarInfoBuilder} for boolean and ranged integer discrete and input CIF variables. */
public class MddCifVarInfoBuilder extends VarInfoBuilder<Declaration> {
    /**
     * Constructor of the {@link MddCifVarInfoBuilder} class.
     *
     * @param numUseKinds Number of use-kinds for a single CIF variable.
     * @see VarInfoBuilder
     */
    public MddCifVarInfoBuilder(int numUseKinds) {
        super(numUseKinds);
    }

    @Override
    protected String getName(Declaration var) {
        return var.getName();
    }

    @Override
    protected int getLowerBound(Declaration var) {
        CifType type = getVariableType(var);
        if (type instanceof BoolType) {
            return 0;
        }

        Assert.check(type instanceof IntType);
        IntType intType = (IntType)type;
        Assert.check(!isRangeless(intType));
        return CifTypeUtils.getLowerBound(intType);
    }

    @Override
    protected int getNumValues(Declaration var) {
        CifType type = getVariableType(var);
        if (type instanceof BoolType) {
            return 2;
        }

        Assert.check(type instanceof IntType);
        IntType intType = (IntType)type;
        Assert.check(!isRangeless(intType));
        return getUpperBound(intType) - CifTypeUtils.getLowerBound(intType) + 1;
    }
}
