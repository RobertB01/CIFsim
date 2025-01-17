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
    public final PlcFunctionBlockDescription funcBlockDescription;

    /**
     * Constructor of the {@link PlcFuncBlockType} class.
     *
     * @param funcBlockDescription Description of the function block.
     */
    public PlcFuncBlockType(PlcFunctionBlockDescription funcBlockDescription) {
        this.funcBlockDescription = funcBlockDescription;
        typeName = funcBlockDescription.typeName;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof PlcFuncBlockType othFuncBlkType)
                && funcBlockDescription == othFuncBlkType.funcBlockDescription;
    }

    @Override
    public int hashCode() {
        return funcBlockDescription.hashCode();
    }

    @Override
    public String toString() {
        return fmt("PlcFuncBlockType(\"%s\")", typeName);
    }
}
