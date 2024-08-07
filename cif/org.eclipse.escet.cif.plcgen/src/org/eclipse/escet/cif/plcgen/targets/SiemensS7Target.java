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

import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.COMPLEMENT_OP;
import static org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation.SEL_OP;
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
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.BIT_STRING_TYPES_32;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.BIT_STRING_TYPES_64;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.INTEGER_TYPES_32;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.INTEGER_TYPES_64;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.REAL_TYPES_32;
import static org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType.REAL_TYPES_64;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.eclipse.escet.cif.metamodel.cif.declarations.Constant;
import org.eclipse.escet.cif.plcgen.generators.PlcVariablePurpose;
import org.eclipse.escet.cif.plcgen.model.functions.PlcBasicFuncDescription.PlcFuncNotation;
import org.eclipse.escet.cif.plcgen.model.functions.PlcFuncOperation;
import org.eclipse.escet.cif.plcgen.model.types.PlcElementaryType;
import org.eclipse.escet.cif.plcgen.options.ConvertEnums;
import org.eclipse.escet.cif.plcgen.writers.S7Writer;
import org.eclipse.escet.cif.plcgen.writers.Writer;
import org.eclipse.escet.common.java.Assert;

/** Code generator for Siemens S7-300, S7-400, S7-1200, and S7-1500 PLC types. */
public class SiemensS7Target extends PlcBaseTarget {
    /** Replacement strings for the extension in the CIF input file name to construct an output path for each target. */
    private static final Map<PlcTargetType, String> OUT_SUFFIX_REPLACEMENTS;

    /** Supported integer types for each target, ordered in increasing size. */
    private static final Map<PlcTargetType, List<PlcElementaryType>> INTEGER_TYPES;

    /** Supported real types for each target, ordered in increasing size. */
    private static final Map<PlcTargetType, List<PlcElementaryType>> REAL_TYPES;

    /** Supported bit string types for each target, ordered in increasing size. */
    private static final Map<PlcTargetType, List<PlcElementaryType>> BIT_STRING_TYPES;

    /** Special characters in an identifier. */
    private static final BitSet SPECIAL_CHARS;

    /** Normal characters in an identifier. */
    private static final BitSet NORMAL_CHARS;

    static {
        OUT_SUFFIX_REPLACEMENTS = Map.of(
                PlcTargetType.S7_300, "_s7_300", PlcTargetType.S7_400, "_s7_400",
                PlcTargetType.S7_1200, "_s7_1200", PlcTargetType.S7_1500, "_s7_1500");

        INTEGER_TYPES = Map.of(
                PlcTargetType.S7_300, INTEGER_TYPES_32, PlcTargetType.S7_400, INTEGER_TYPES_32,
                PlcTargetType.S7_1200, INTEGER_TYPES_32, PlcTargetType.S7_1500, INTEGER_TYPES_64);

        REAL_TYPES = Map.of(
                PlcTargetType.S7_300, REAL_TYPES_32, PlcTargetType.S7_400, REAL_TYPES_32,
                PlcTargetType.S7_1200, REAL_TYPES_64, PlcTargetType.S7_1500, REAL_TYPES_64);

        BIT_STRING_TYPES = Map.of(
                PlcTargetType.S7_300, BIT_STRING_TYPES_32, PlcTargetType.S7_400, BIT_STRING_TYPES_32,
                PlcTargetType.S7_1200, BIT_STRING_TYPES_32, PlcTargetType.S7_1500, BIT_STRING_TYPES_64);

        // Source: https://cache.industry.siemens.com/dl/files/857/109477857/att_865202/v1/109477857_Bezeichner_Anfuehrungszeichen_en.pdf
        // Consulted: Jul 10, 2024. Valid for TIA portal 10 and higher.
        //
        // ASCII characters neither in NORMAL_CHARS nor in SPECIAL_CHARS are 0..31, 127, '"', '+' and '~'.
        SPECIAL_CHARS = new BitSet(128);
        char[] specials = new char[] {' ', '!', '#', '$', '%', '&', '\'', '(', ')', '*', ',', '-', '.', '/', ':', ';',
                '<', '=', '>', '?', '@', '[', '\\', ']', '^', '`', '{', '|', '}'};
        for (char c: specials) {
            SPECIAL_CHARS.set(c);
        }

        // Source: https://cache.industry.siemens.com/dl/files/857/109477857/att_865202/v1/109477857_Bezeichner_Anfuehrungszeichen_en.pdf
        // Consulted: Jul 10, 2024. Valid for TIA portal 10 and higher.
        //
        // ASCII characters in regular identifiers (letters, digits, and '_').
        NORMAL_CHARS = new BitSet(128);
        NORMAL_CHARS.set('_');
        for (int i = 0; i < 26; i++) {
            NORMAL_CHARS.set('A' + i);
            NORMAL_CHARS.set('a' + i);
        }
        for (int i = 0; i < 10; i++) {
            NORMAL_CHARS.set('0' + i);
        }
    }

    /**
     * Constructor of the {@link SiemensS7Target} class.
     *
     * @param targetType A Siemens S7 target type.
     */
    public SiemensS7Target(PlcTargetType targetType) {
        super(targetType, ConvertEnums.CONSTS, "TON");
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
    public boolean supportsConstant(Constant constant) {
        return commonSupportedConstants(constant);
    }

    @Override
    public EnumSet<PlcFuncNotation> getSupportedFuncNotations(PlcFuncOperation funcOper, int numArgs) {
        // S7 doesn't have a function for log10.
        if (funcOper == STDLIB_LOG) {
            return PlcFuncNotation.UNSUPPORTED;
        }

        EnumSet<PlcFuncNotation> funcSupport = super.getSupportedFuncNotations(funcOper, numArgs);

        // Functions that should always be formal.
        EnumSet<PlcFuncOperation> formalFuncs = EnumSet.of(SEL_OP, STDLIB_MAX, STDLIB_MIN);
        if (formalFuncs.contains(funcOper)) {
            funcSupport = EnumSet.copyOf(funcSupport);
            funcSupport.retainAll(PlcFuncNotation.FORMAL_ONLY);
            return funcSupport;
        }

        // S7 does not allow formal function call syntax for the following functions and not for functions with two or
        // more parameters (except for MIN / MAX above).
        EnumSet<PlcFuncOperation> notFormalFuncs = EnumSet.of(COMPLEMENT_OP, STDLIB_ABS, STDLIB_ACOS, STDLIB_ASIN,
                STDLIB_ATAN, STDLIB_COS, STDLIB_EXP, STDLIB_LN, STDLIB_LOG, STDLIB_SIN, STDLIB_SQRT, STDLIB_TAN);
        if (notFormalFuncs.contains(funcOper) || numArgs >= 2) {
            funcSupport = EnumSet.copyOf(funcSupport);
            funcSupport.remove(PlcFuncNotation.FORMAL);
            return funcSupport;
        }

        return funcSupport;
    }

    @Override
    public List<PlcElementaryType> getSupportedIntegerTypes() {
        return INTEGER_TYPES.get(targetType);
    }

    @Override
    public List<PlcElementaryType> getSupportedRealTypes() {
        return REAL_TYPES.get(targetType);
    }

    @Override
    public List<PlcElementaryType> getSupportedBitStringTypes() {
        return BIT_STRING_TYPES.get(targetType);
    }

    @Override
    public String getUsageVariableText(PlcVariablePurpose purpose, String varName) {
        if (purpose == PlcVariablePurpose.STATE_VAR) {
            return "\"DB\"." + varName;
        } else if (purpose == PlcVariablePurpose.INPUT_VAR || purpose == PlcVariablePurpose.OUTPUT_VAR) {
            String encodedName = encodeTagName(varName, false);
            Assert.notNull(encodedName);
            return encodedName;
        }
        return super.getUsageVariableText(purpose, varName);
    }

    @Override
    public String getPathSuffixReplacement() {
        return OUT_SUFFIX_REPLACEMENTS.get(targetType);
    }

    @Override
    public boolean checkIoVariableName(String name) {
        return encodeTagName(name, false) != null;
    }

    /**
     * Test whether a tag name is acceptable to an S7 system, and if so, how to write it.
     *
     * @param name The name to test.
     * @param isLocal Whether the name is defined locally (as in, not in a global table).
     * @return If {@code null}, the name is not acceptable. Otherwise, the name is converted and returned in the form
     *     needed for using it.
     */
    public static String encodeTagName(String name, boolean isLocal) {
        // Source: https://cache.industry.siemens.com/dl/files/857/109477857/att_865202/v1/109477857_Bezeichner_Anfuehrungszeichen_en.pdf
        // Consulted: Jul 10, 2024. Valid for TIA portal 10 and higher.

        // Decide whether to add double quotes around the name or to reject it.

        // Name cannot be empty.
        if (name.isEmpty()) {
            return null;
        }

        boolean mustBeQuoted = false;
        for (int i = 0; i < name.length(); i++) {
            // S7 allows more than just ASCII, but that gives problems in CIF and CSV files.
            char c = name.charAt(i);
            if (c < 32 || c >= 127) {
                return null; // Control character or not ASCII.
            }
            if (NORMAL_CHARS.get(c)) {
                continue;
            }
            if (SPECIAL_CHARS.get(c)) {
                mustBeQuoted = true;
                continue;
            }
            return null; // Neither a control character, nor a normal or special character.
        }

        // Names starting with a decimal digit must be double-quoted. Obviously this includes names consisting of only
        // digits.
        char first = name.charAt(0);
        mustBeQuoted |= (first >= '0' && first <= '9'); // This leaves [A-Za-z_] as normal first characters.

        // Names with "__" or ending with an underscore must be quoted.
        mustBeQuoted |= (name.endsWith("_") || name.contains("__"));

        if (!mustBeQuoted) {
            return name;
        } else if (isLocal) {
            return "#\"" + name + "\"";
        } else {
            return "\"" + name + "\"";
        }
    }
}
