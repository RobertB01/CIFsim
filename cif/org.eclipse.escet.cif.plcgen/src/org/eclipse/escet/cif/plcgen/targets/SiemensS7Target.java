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

import java.util.Map;

import org.eclipse.escet.cif.cif2plc.options.PlcOutputType;
import org.eclipse.escet.cif.cif2plc.writers.OutputTypeWriter;
import org.eclipse.escet.cif.cif2plc.writers.S7Writer;
import org.eclipse.escet.common.java.Assert;

/** Code generator for Siemens S7-300, S7-400, S7-1200, and S7-1500 PLC types. */
public class SiemensS7Target extends PlcTarget {
    /** Replacement strings for the extension in the CIF input file name to construct an output path for each target. */
    private static final Map<PlcTargetType, String> OUT_SUFFIX_REPLACEMENTS;

    /** Equivalent Siemens S7 enumeration values in the {@code org.eclipse.escet.cif2plc} project for each target. */
    private static final Map<PlcTargetType, PlcOutputType> OUTPUT_TYPES;

    /** Maximum supported stored integer type size for each target. */
    private static final Map<PlcTargetType, Integer> MAX_INTEGER_SIZES;

    /** Maximum supported stored floating type size for each target. */
    private static final Map<PlcTargetType, Integer> MAX_FLOAT_SIZES;

    static {
        OUT_SUFFIX_REPLACEMENTS = Map.of( //
                PlcTargetType.S7_300, "_s7_300", PlcTargetType.S7_400, "_s7_400", //
                PlcTargetType.S7_1200, "_s7_1200", PlcTargetType.S7_1500, "_s7_1500");

        OUTPUT_TYPES = Map.of( //
                PlcTargetType.S7_300, PlcOutputType.S7_300, PlcTargetType.S7_400, PlcOutputType.S7_400, //
                PlcTargetType.S7_1200, PlcOutputType.S7_1200, PlcTargetType.S7_1500, PlcOutputType.S7_1500);

        MAX_INTEGER_SIZES = Map.of( //
                PlcTargetType.S7_300, 32, PlcTargetType.S7_400, 32, //
                PlcTargetType.S7_1200, 32, PlcTargetType.S7_1500, 64);

        MAX_FLOAT_SIZES = Map.of( //
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
    public OutputTypeWriter getPlcCodeWriter() {
        return new S7Writer(OUTPUT_TYPES.get(targetType));
    }

    @Override
    public boolean supportsConstants() {
        return true;
    }

    @Override
    protected int getMaxIntegerTypeSize() {
        return MAX_INTEGER_SIZES.get(targetType);
    }

    @Override
    protected int getMaxFloatTypeSize() {
        return MAX_FLOAT_SIZES.get(targetType);
    }

    @Override
    public String getPathSuffixReplacement() {
        return OUT_SUFFIX_REPLACEMENTS.get(targetType);
    }
}
