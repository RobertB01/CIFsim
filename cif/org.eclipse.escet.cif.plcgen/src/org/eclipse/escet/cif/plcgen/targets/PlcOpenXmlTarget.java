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
import org.eclipse.escet.cif.plcgen.writers.PlcOpenXmlWriter;
import org.eclipse.escet.cif.plcgen.writers.Writer;

/** Code generator for the PLCopen XML PLC type. */
public class PlcOpenXmlTarget extends PlcBaseTarget {
    /** Constructor of the {@link PlcOpenXmlTarget} class. */
    public PlcOpenXmlTarget() {
        super(PlcTargetType.PLC_OPEN_XML, ConvertEnums.KEEP);
    }

    @Override
    public Writer getPlcCodeWriter() {
        return new PlcOpenXmlWriter(this);
    }

    @Override
    public boolean supportsArrays() {
        return true;
    }

    @Override
    public boolean supportsConstant(Constant constant) {
        return commonSupportedConstants(constant);
    }

    @Override
    public List<PlcElementaryType> getSupportedIntegerTypes() {
        return PlcElementaryType.INTEGER_TYPES_64;
    }

    @Override
    public List<PlcElementaryType> getSupportedRealTypes() {
        return PlcElementaryType.REAL_TYPES_64;
    }

    @Override
    public String getPathSuffixReplacement() {
        return ".plcopen.xml";
    }
}
