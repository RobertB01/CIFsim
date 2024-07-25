//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2010, 2024 Contributors to the Eclipse Foundation
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

package org.eclipse.escet.cif.plcgen.targets;

import java.util.List;

import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.writers.AbbWriter;
import org.eclipse.escet.cif.plcgen.writers.Writer;

/** Code generator for the ABB PLC type. */
public class AbbTarget extends PlcBaseTarget {
    /** Constructor of the {@link AbbTarget} class. */
    public AbbTarget() {
        // TODO Verify settings of the ABB target.
        super(PlcTargetType.ABB, ConvertEnums.INTS);
    }

    @Override
    public Writer getPlcCodeWriter() {
        return new AbbWriter(this);
    }

    @Override
    public boolean supportsArrays() {
        return true;
    }

    @Override
    public boolean supportsConstant(Constant constant) {
        return false;
    }

    @Override
    public List<PlcElementaryType> getSupportedIntegerTypes() {
        return PlcElementaryType.INTEGER_TYPES_32;
    }

    @Override
    public int getMaxIntegerTypeSize() {
        return 32;
    }

    @Override
    public List<PlcElementaryType> getSupportedRealTypes() {
        return PlcElementaryType.REAL_TYPES_32;
    }

    @Override
    public int getMaxRealTypeSize() {
        return 32;
    }

    @Override
    public String getPathSuffixReplacement() {
        return "_abb";
    }
}
