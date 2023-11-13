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

package org.eclipse.escet.cif.plcgen.targets;

import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.COMPLEMENT_OP;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.POWER_OP;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_ABS;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_ACOS;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_ASIN;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_ATAN;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_COS;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_EXP;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_LN;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_LOG;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_SIN;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_SQRT;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_TAN;

import java.util.EnumSet;

import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcFuncNotation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.writers.TwinCatWriter;
import org.eclipse.escet.cif.plcgen.writers.Writer;

/** Code generator for the TwinCAT PLC type. */
public class TwinCatTarget extends PlcBaseTarget {
    /** Constructor of the {@link TwinCatTarget} class. */
    public TwinCatTarget() {
        super(PlcTargetType.TWINCAT);
    }

    @Override
    public Writer getPlcCodeWriter() {
        return new TwinCatWriter(this);
    }

    @Override
    public boolean supportsArrays() {
        return true;
    }

    @Override
    public boolean supportsConstants() {
        return true;
    }

    @Override
    public boolean supportsEnumerations() {
        return true;
    }

    @Override
    public EnumSet<PlcFuncNotation> getSupportedFuncNotations(PlcFuncOperation funcOper, int numArgs) {
        EnumSet<PlcFuncNotation> funcSupport = super.getSupportedFuncNotations(funcOper, numArgs);

        // TwinCAT 3.1 does not support "**" for pow(a, b).
        if (funcOper.equals(POWER_OP)) {
            funcSupport = EnumSet.copyOf(funcSupport);
            funcSupport.remove(PlcFuncNotation.INFIX);
            return funcSupport;
        }

        // TwinCAT does not allow formal function call syntax for the following functions and not for functions with two
        // or more parameters.
        EnumSet<PlcFuncOperation> notFormalFuncs = EnumSet.of(COMPLEMENT_OP, STDLIB_ABS, STDLIB_EXP, STDLIB_SQRT,
                STDLIB_LN, STDLIB_LOG, STDLIB_ACOS, STDLIB_ASIN, STDLIB_ATAN, STDLIB_COS, STDLIB_SIN, STDLIB_TAN);
        if (notFormalFuncs.contains(funcOper) || numArgs > 2) {
            funcSupport = EnumSet.copyOf(funcSupport);
            funcSupport.remove(PlcFuncNotation.FORMAL);
            return funcSupport;
        }

        return funcSupport;
    }

    @Override
    public int getMaxIntegerTypeSize() {
        return 64;
    }

    @Override
    public int getMaxRealTypeSize() {
        return 64;
    }

    @Override
    public String getPathSuffixReplacement() {
        return "_twincat";
    }
}
