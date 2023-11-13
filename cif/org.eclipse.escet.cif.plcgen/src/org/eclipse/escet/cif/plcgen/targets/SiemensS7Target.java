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
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_MAX;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_MIN;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_SIN;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_SQRT;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.STDLIB_TAN;

import java.util.EnumSet;
import java.util.Map;

import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcFuncNotation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.writers.S7Writer;
import org.eclipse.escet.cif.plcgen.writers.Writer;
import org.eclipse.escet.common.java.Assert;

/** Code generator for Siemens S7-300, S7-400, S7-1200, and S7-1500 PLC types. */
public class SiemensS7Target extends PlcBaseTarget {
    /** Set of targets S7-300 and S7-400. */
    private static final EnumSet<PlcTargetType> S7_300_400 = EnumSet.of(PlcTargetType.S7_300, PlcTargetType.S7_400);

    /** Replacement strings for the extension in the CIF input file name to construct an output path for each target. */
    private static final Map<PlcTargetType, String> OUT_SUFFIX_REPLACEMENTS;

    /** Maximum supported stored integer type size for each target. */
    private static final Map<PlcTargetType, Integer> MAX_INTEGER_SIZES;

    /** Maximum supported stored real type size for each target. */
    private static final Map<PlcTargetType, Integer> MAX_REAL_SIZES;

    static {
        OUT_SUFFIX_REPLACEMENTS = Map.of( //
                PlcTargetType.S7_300, "_s7_300", PlcTargetType.S7_400, "_s7_400", //
                PlcTargetType.S7_1200, "_s7_1200", PlcTargetType.S7_1500, "_s7_1500");

        MAX_INTEGER_SIZES = Map.of( //
                PlcTargetType.S7_300, 32, PlcTargetType.S7_400, 32, //
                PlcTargetType.S7_1200, 32, PlcTargetType.S7_1500, 64);

        MAX_REAL_SIZES = Map.of( //
                PlcTargetType.S7_300, 32, PlcTargetType.S7_400, 32, //
                PlcTargetType.S7_1200, 64, PlcTargetType.S7_1500, 64);
    }

    /**
     * Constructor of the {@link SiemensS7Target} class.
     *
     * @param targetType A Siemens S7 target type.
     */
    public SiemensS7Target(PlcTargetType targetType) {
        super(targetType);
        // TODO Verify settings of the Siemens target.

        Assert.check(OUT_SUFFIX_REPLACEMENTS.containsKey(targetType)); // Java can't check existence before super().
    }

    @Override
    public Writer getPlcCodeWriter() {
        return new S7Writer(this);
    }

    @Override
    public boolean supportsArrays() {
        // S7 transformation doesn't support list types. That is because S7 doesn't support functions
        // returning arrays and doesn't support arrays of arrays.
        return false;
    }

    @Override
    public boolean supportsConstants() {
        return true;
    }

    @Override
    public boolean supportsEnumerations() {
        return false;
    }

    @Override
    public boolean supportsPower(boolean baseIsInt, boolean exponentIsInt) {
        // S7-400 and S7-300 only support power on real types.
        if (S7_300_400.contains(targetType)) {
            return !baseIsInt && !exponentIsInt;
        } else {
            return super.supportsPower(baseIsInt, exponentIsInt);
        }
    }

    @Override
    public EnumSet<PlcFuncNotation> getsupportedFuncNotations(PlcFuncOperation funcOper, int numArgs) {
        // S7 doesn't have a function for log10.
        if (funcOper.equals(STDLIB_LOG)) {
            return PlcFuncNotation.UNSUPPORTED;
        }

        EnumSet<PlcFuncNotation> funcSupport = super.getsupportedFuncNotations(funcOper, numArgs);

        // S7-300 and S7-400 don't support "**" for pow(a, b).
        if (S7_300_400.contains(targetType) && funcOper.equals(POWER_OP)) {
            funcSupport = EnumSet.copyOf(funcSupport);
            funcSupport.remove(PlcFuncNotation.INFIX);
            return funcSupport;
        }

        // S7 does not allow formal function call syntax for the following functions and not for functions with two or
        // more parameters.
        EnumSet<PlcFuncOperation> notFormalFuncs = EnumSet.of(COMPLEMENT_OP, STDLIB_ABS, STDLIB_EXP, STDLIB_SQRT,
                STDLIB_LN, STDLIB_LOG, STDLIB_ACOS, STDLIB_ASIN, STDLIB_ATAN, STDLIB_COS, STDLIB_SIN, STDLIB_TAN);
        if (notFormalFuncs.contains(funcOper) || numArgs > 2) {
            funcSupport = EnumSet.copyOf(funcSupport);
            funcSupport.remove(PlcFuncNotation.FORMAL);
            return funcSupport;
        }

        // Functions that should always be formal (derived from cif2plc test).
        EnumSet<PlcFuncOperation> formalFuncs = EnumSet.of(STDLIB_MIN, STDLIB_MAX);
        if (formalFuncs.contains(funcOper)) {
            funcSupport = EnumSet.copyOf(funcSupport);
            funcSupport.retainAll(PlcFuncNotation.FORMAL_ONLY);
            return funcSupport;
        }

        return funcSupport;
    }

    @Override
    public int getMaxIntegerTypeSize() {
        return MAX_INTEGER_SIZES.get(targetType);
    }

    @Override
    public int getMaxRealTypeSize() {
        return MAX_REAL_SIZES.get(targetType);
    }

    @Override
    public String getPathSuffixReplacement() {
        return OUT_SUFFIX_REPLACEMENTS.get(targetType);
    }
}
