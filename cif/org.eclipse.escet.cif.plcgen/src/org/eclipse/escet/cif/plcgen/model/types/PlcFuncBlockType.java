//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.model.types;

import static org.eclipse.escet.common.java.Strings.fmt;

import org.eclipse.escet.cif.plcgen.model.functions.PlcFunctionBlockDescription;

/** Type describing a function block. */
public class PlcFuncBlockType extends PlcType {
    /** Name of the function block type. */
    public final String typeName;

    /** Description of the function block. */
    public final PlcFunctionBlockDescription funcBlockcDescription;

    /**
     * Constructor of the {@link PlcFuncBlockType} class.
     *
     * @param funcBlockcDescription Description of the function block.
     */
    public PlcFuncBlockType(PlcFunctionBlockDescription funcBlockcDescription) {
        this.funcBlockcDescription = funcBlockcDescription;
        typeName = funcBlockcDescription.typeName;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof PlcFuncBlockType othFuncBlkType)
                && funcBlockcDescription == othFuncBlkType.funcBlockcDescription;
    }

    @Override
    public int hashCode() {
        return funcBlockcDescription.hashCode();
    }

    @Override
    public String toString() {
        return fmt("PlcFuncBlockType(\"%s\")", typeName);
    }
}
